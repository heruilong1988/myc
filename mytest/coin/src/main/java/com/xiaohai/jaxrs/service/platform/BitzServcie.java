package com.xiaohai.jaxrs.service.platform;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xiaohai.common.exception.BizException;
import com.xiaohai.common.utils.base.CollectionUtil;
import com.xiaohai.common.utils.base.NumberUtil;
import com.xiaohai.common.utils.base.StrUtil;
import com.xiaohai.common.utils.http.SimpleRestClient;
import com.xiaohai.common.utils.json.JSONUtil;
import com.xiaohai.constant.enums.AllDealType;
import com.xiaohai.constant.enums.DealType;
import com.xiaohai.constant.enums.OrderDealType;
import com.xiaohai.constant.enums.PlatformDealLimit;
import com.xiaohai.jaxrs.service.base.PlatformService;
import com.xiaohai.jaxrs.vo.base.CancelOrderVO;
import com.xiaohai.jaxrs.vo.base.MyOrderVO;
import com.xiaohai.jaxrs.vo.base.OrderDataVO;
import com.xiaohai.jaxrs.vo.platform.bitz.BlanceInfoBit;
import com.xiaohai.jaxrs.vo.platform.bitz.BlanceInfoBitDataInfo;
import com.xiaohai.jaxrs.vo.platform.bitz.CancelOrderBit;
import com.xiaohai.jaxrs.vo.platform.bitz.MakeOrderBit;
import com.xiaohai.jaxrs.vo.platform.bitz.OpenOrderBit;
import com.xiaohai.jaxrs.vo.platform.bitz.OpenOrderBitDataData;
import com.xiaohai.jaxrs.vo.platform.bitz.OrderInfoBit;
import com.xiaohai.utils.base.HttpHeaderUtil;
import com.xiaohai.utils.base.RetryUtil;

@Service
public class BitzServcie extends PlatformService {
	
	private static final Logger LOG = LoggerFactory.getLogger(BitzServcie.class);

	@Value("${bitzApiKey}")
	private String apiKey;
	@Value("${bitzSecretKey}")
	private String secretKey;
	@Value("${bitzTradePwd}")
	private String tradePwd;
	
	private String urlPre = "https://apiv2.bitz.com";
	
	private int status = 200;
	
	public static void main(String[] args) {
		BitzServcie bitzServcie = new BitzServcie();
		
//		boolean result = bitzServcie.getOrderIsExsit("eth", "btc");
//		System.out.println(result);
		
//		List<String> coinNames = new ArrayList<String>();
//		coinNames.add("eth");
//		Map<String, Double> map = bitzServcie.getBalance(coinNames);
//		System.out.println(JSON.toJSONString(map));
		
//		List<MyOrderVO> myOrderVOs = bitzServcie.getOrders("oc", "btc");
//		System.out.println(JSON.toJSONString(myOrderVOs));
		
//		long orderId = bitzServcie.order(OrderDealType.Buy, "oc", "btc", 0.00000019, 1000, false);
//		long orderId = bitzServcie.order(OrderDealType.Sell, "pnt", "btc", 0.00000026, 2000, false);
//		System.out.println(orderId);
		
//		List<CancelOrderVO> cancelOrderVOList = new ArrayList<CancelOrderVO>();
//		CancelOrderVO cancelOrderVO = new CancelOrderVO("", "", "777751975");
//		cancelOrderVOList.add(cancelOrderVO);
//		bitzServcie.cancelOrder(cancelOrderVOList);
		
		System.out.println(JSON.toJSONString(bitzServcie.getCoinInfoOrderData("eth12", "btc")));
		
	}
	
	@Override
	public OrderDataVO getCoinInfoOrderData(String coinName, String baseCoinName) {
		OrderDataVO orderData = new OrderDataVO();
		try {
			String orderBookUrl = String.format("%s/Market/depth?symbol=%s_%s", this.urlPre, coinName.toLowerCase(), baseCoinName.toLowerCase());
			
			String orderInfoBitString = SimpleRestClient.getClient().getForObject(orderBookUrl, String.class);
			System.out.println(orderInfoBitString);
			OrderInfoBit orderInfoBit = JSONUtil.toBean(orderInfoBitString, OrderInfoBit.class);
			
			if (orderInfoBit == null || orderInfoBit.getStatus() != this.status || orderInfoBit.getData() == null) {
				throw new BizException(-1, "获取平台挂单详情数据失败");
			}
			
			String[][] bids = orderInfoBit.getData().getBids(); //买的挂单
			String[][] asks = orderInfoBit.getData().getAsks(); //卖的挂单
			
			// 因为asks的卖单是高到低，要转换成低到高；
			int bidsLength = bids.length;
			int asksLength = asks.length;
			String[][] asksCopy = new String[asksLength][3];
			for (int i = 0; i < asksLength; i++) {
				asksCopy[i][0] = asks[asksLength - 1 - i][0];
				asksCopy[i][1] = asks[asksLength - 1 - i][1];
				asksCopy[i][2] = asks[asksLength - 1 - i][2];
			}
			asks = asksCopy;
			orderInfoBit.getData().setAsks(asks);
			
			double[][] sellData = new double[asksLength][2];
			double[][] buyData = new double[bidsLength][2];
			
			for (int i = 0; i < asksLength; i++) {
				sellData[i][0] = NumberUtil.doubleValue(asks[i][0]);
				sellData[i][1] = NumberUtil.doubleValue(asks[i][1]);
			}
			
			for (int i = 0; i < bidsLength; i++) {
				buyData[i][0] = NumberUtil.doubleValue(bids[i][0]);
				buyData[i][1] = NumberUtil.doubleValue(bids[i][1]);
			}
			
			Date nowDate = new Date();
			orderData.setCreateTime(nowDate);
			orderData.setTimestamp(orderInfoBit.getTime());
			orderData.setCoinName(coinName);
			orderData.setBaseCoinName(baseCoinName);
			orderData.setPlatform(this.getServiceName());
			orderData.setBuyData(buyData);
			orderData.setSellData(sellData);
			
			// 根据最小交易量，设置最后最小交易的金额和数量
			PlatformDealLimit limit = PlatformDealLimit.getPlatformDealLimit(this.getServiceName(), baseCoinName);
			double minDealLimit = limit.getMinLimit();
			double coinBuyMoney = 0;
			double coinBuyNum = 0;
			double coinBuyTotalAmount = 0;
			double coinSellMoney = 0;
			double coinSellNum = 0;
			double coinSellTotalAmount = 0;
			
			// 挂的买单，要卖出时超过最小金额限制的价格和数量
			for (int i = 0; i < bidsLength; i++) {
				coinBuyMoney = buyData[i][0];
				coinBuyNum += buyData[i][1];
				coinBuyTotalAmount = coinBuyMoney * coinBuyNum;
				if (coinBuyTotalAmount > minDealLimit) {
					break;
				}
			}
			
			// 挂的卖单，要买入时超过最小金额限制的价格和数量
			for (int i = 0; i < asksLength; i++) {
				coinSellMoney = sellData[i][0];
				coinSellNum += sellData[i][1];
				coinSellTotalAmount = coinSellMoney * coinSellNum;
				if (coinSellTotalAmount > minDealLimit) {
					break;
				}
			}
			
			orderData.setActualBuyAmount(coinBuyNum);
			orderData.setActualBuyPrice(coinBuyMoney);
			orderData.setActualSellPrice(coinSellMoney);
			orderData.setActualSellAmount(coinSellNum);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(String.format("【%s】获取市场详情失败，现在重试", this.getServiceName()));
			orderData = (OrderDataVO) RetryUtil.setRetryTimes(1).retry(coinName, baseCoinName);
			orderData = (orderData != null ? orderData : new OrderDataVO());
		}
			
		return orderData;
	}

	/**
	 * 获取挂单是否存在
	 */
	@Override
	public boolean getOrderIsExsit(String coinName, String baseCoinName, List<String> orderIds) {
		return !CollectionUtil.isEmpty(this.getOrders(coinName, baseCoinName, orderIds));
	}

	/**
	 * 获取订单详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MyOrderVO> getOrders(String coinName, String baseCoinName, List<String> orderIds) {
		List<MyOrderVO> openOrders = new ArrayList<MyOrderVO>();
		try {
			String url = String.format("%s/Trade/getUserNowEntrustSheet", this.urlPre);
			String page = "1";
			String pageSize = "100";
			
			String nonce = StrUtil.getRandomString(6);
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("apiKey", apiKey);
			param.put("coinFrom", coinName);
			param.put("coinTo", baseCoinName);
			param.put("nonce", nonce);
			param.put("page", page);
			param.put("pageSize", pageSize);
			param.put("timeStamp", timestamp);
			
			String sign = this.buildMysignV1(param, secretKey).toLowerCase();
			param.put("sign", sign);
			
			String openOrderBitString = SimpleRestClient.postObject(url, HttpHeaderUtil.getBitzHttpHeader(), StrUtil.createLinkString(param), String.class);
			System.out.println(JSON.toJSONString(openOrderBitString));
			OpenOrderBit openOrderBit = JSONUtil.toBean(openOrderBitString, OpenOrderBit.class);
			
			if (openOrderBit != null && openOrderBit.getStatus() == this.status) {
				List<OpenOrderBitDataData> openOrderDataDatas = new ArrayList<OpenOrderBitDataData>();
				if (openOrderBit.getData() != null && !CollectionUtil.isEmpty(openOrderBit.getData().getData())) {
					openOrderDataDatas = openOrderBit.getData().getData();
				}
				
				
				MyOrderVO myOrderVO= null;
				boolean orderIdHasValue = !CollectionUtil.isEmpty(orderIds);
				for (OpenOrderBitDataData openOrderDataData : openOrderDataDatas) {
					// 0:未成交, 1:部分成交, 2:全部成交, 3:已经撤销
					if (!"0".equals(openOrderDataData.getStatus()) && !"1".equals(openOrderDataData.getStatus())
							&& (orderIdHasValue && !orderIds.contains(openOrderDataData.getId()))) {
						continue;
					}
					
					myOrderVO = new MyOrderVO();
					myOrderVO.setCoinName(coinName);
					myOrderVO.setBaseCoinName(baseCoinName);
					myOrderVO.setAmount(NumberUtil.doubleValue(openOrderDataData.getNumber()));
					myOrderVO.setPrice(NumberUtil.doubleValue(openOrderDataData.getPrice()));
					myOrderVO.setDealType(DealType.getEnum(openOrderDataData.getFlag()));
					myOrderVO.setRemainAmount(NumberUtil.doubleValue(openOrderDataData.getNumberOver()));
					myOrderVO.setTime(new Date(NumberUtil.longValue(openOrderDataData.getCreated()) * 1000));
					openOrders.add(myOrderVO);
				}
				LOG.info(String.format("平台【%s】获取【%s】【%s】订单详情,结果【%s】",
						this.getServiceName(), coinName, baseCoinName, JSON.toJSONString(openOrderDataDatas)));
			} else {
				throw new BizException(-1, "查询订单详情报错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(String.format("【%s】获取订单详情失败，现在重试", this.getServiceName()));
			openOrders = (List<MyOrderVO>) RetryUtil.setRetryTimes(1).retry(coinName, baseCoinName);
			openOrders = (openOrders != null ? openOrders : new ArrayList<MyOrderVO>());
		}
		
		return openOrders;
	}

	@Override
	public List<MyOrderVO> getOrders(List<String> orderIds) {
		return null;
	}
	
	/**
	 * 获取余额
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> getBalance(List<String> coinNames) {
		Map<String, Double> blanceMap = new HashMap<>();
		Map<String, Double> blances = new HashMap<String, Double>();

		try {
			String url = String.format("%s/Assets/getUserAssets", this.urlPre);
			
			String nonce = "123455"; //StrUtil.getRandomString(6);
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("apiKey", apiKey);
			param.put("nonce", nonce);
			param.put("timeStamp", timestamp);
			
			String sign = this.buildMysignV1(param, secretKey).toLowerCase();
			param.put("sign", sign);
			
			String blanceInfoBitString = SimpleRestClient.postObject(url, HttpHeaderUtil.getBitzHttpHeader(), StrUtil.createLinkString(param), String.class);
			BlanceInfoBit blanceInfoBit = JSONUtil.toBean(blanceInfoBitString, BlanceInfoBit.class);
			
			if (blanceInfoBit == null || blanceInfoBit.getStatus() != this.status) {
				throw new BizException(-1, "获取余额失败");
			}
			
			
			List<BlanceInfoBitDataInfo> infos = blanceInfoBit.getData() == null 
					? new ArrayList<BlanceInfoBitDataInfo>() : blanceInfoBit.getData().getInfo() == null 
					? new ArrayList<BlanceInfoBitDataInfo>() : blanceInfoBit.getData().getInfo();
					
					for (BlanceInfoBitDataInfo info : infos) {
						blanceMap.put(info.getName().toLowerCase(), NumberUtil.doubleValue(info.getNum()));
					}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(String.format("【%s】获取余额失败，现在重试", this.getServiceName()));
			blances = (Map<String, Double>) RetryUtil.setRetryTimes(1).retry(coinNames);
			blances = (blances != null ? blances : new HashMap<String, Double>());
		}
				
		for (String coinName : coinNames) {
			blances.put(coinName, NumberUtil.doubleValue(blanceMap.get(coinName.toLowerCase())));
		}
		
		return blances;
	}

	/**
	 * 根据订单号列表批量取消订单号
	 */
	@Override
	public boolean cancelOrder(List<CancelOrderVO> cancelOrderVOList) {
		if (CollectionUtil.isEmpty(cancelOrderVOList)) {
			return true;
		}
		
		boolean result = false;
		
		try {
			String ids = "";
			for (CancelOrderVO cancelOrderVO : cancelOrderVOList) {
				ids += cancelOrderVO.getOrderNumber() + ",";
			}
			
			// 去掉最后的英文逗号
			if (StrUtil.isNotBlank(ids) && ",".equals(ids.charAt(ids.length() - 1))) {
				ids = ids.substring(0, ids.length() - 1);
			}
			
			String url = String.format("%s/Trade/cancelAllEntrustSheet", this.urlPre);
			
			String nonce = StrUtil.getRandomString(6);
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
			
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("apiKey", apiKey);
			param.put("ids", ids);
			param.put("nonce", nonce);
			param.put("timeStamp", timestamp);
			
			String sign = this.buildMysignV1(param, secretKey).toLowerCase();
			param.put("sign", sign);
			
			String cancelOrderBitString = SimpleRestClient.postObject(url, HttpHeaderUtil.getBitzHttpHeader(), StrUtil.createLinkString(param), String.class);
			System.out.println(cancelOrderBitString);
			CancelOrderBit cancelOrderBit = JSONUtil.toBean(cancelOrderBitString, CancelOrderBit.class);
			
			if (cancelOrderBit == null || cancelOrderBit.getStatus() != this.status) {
				throw new BizException(-1, "取消订单失败");
			} else {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(String.format("【%s】取消订单失败，现在重试", this.getServiceName()));
			Boolean temp = (Boolean) RetryUtil.setRetryTimes(1).retry(cancelOrderVOList);
			result = (temp == null ? false : temp);
		}
		
		return result;
	}

	/**
	 * 下单
	 */
	@Override
	public String order(OrderDealType orderDealType, String coinName, String baseCoinName, 
			double price, double amount, boolean needLimitAmount) {
		String orderId = "";
		
		try {
			String symbol = String.format("%s_%s", coinName.toLowerCase(), baseCoinName.toLowerCase());
			String url = String.format("%s/Trade/addEntrustSheet", this.urlPre);
			
			String nonce = StrUtil.getRandomString(6);
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//		System.out.println(String.format("获取时间戳结束%d", System.currentTimeMillis() - startTime));
			String type = AllDealType.getEnum(this.getServiceName(), orderDealType.getValue()).getValue();
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("apiKey", apiKey);
			param.put("nonce", nonce);
			param.put("number", String.format("%.10f", amount));
			param.put("price", String.format("%.10f", price));
			param.put("symbol", symbol);
			param.put("timeStamp", timestamp);
			param.put("tradePwd", this.tradePwd);
			param.put("type", type);
			
			String sign = this.buildMysignV1(param, secretKey).toLowerCase();
			param.put("sign", sign);
			
			String makeOrderBitString = SimpleRestClient.postObject(url, HttpHeaderUtil.getBitzHttpHeader(), StrUtil.createLinkString(param), String.class);
			LOG.info(makeOrderBitString);
			MakeOrderBit makeOrderBit = JSONUtil.toBean(makeOrderBitString, MakeOrderBit.class);
			LOG.info(JSON.toJSONString(makeOrderBit));
			
			if (makeOrderBit != null && makeOrderBit.getStatus() == this.status) {
				orderId = makeOrderBit.getData() != null ? String.valueOf(makeOrderBit.getData().getId()) : "0";
			} else {
				throw new BizException(-1, "提交订单报错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(String.format("【%s】下单失败，现在重试", this.getServiceName()));
			String temp = (String) RetryUtil.setRetryTimes(1).retry(orderDealType, coinName, baseCoinName, price, amount, needLimitAmount);
			orderId = (temp != null ? temp : "0");
		}
		
		return orderId;
	}

	@Override
	public String getServiceName() {
		return "bitz";
	}
	
	/**
	 * 生成签名结果(新版本使用)
	 * 
	 * @param sArray 要签名的数组
	 * @return 签名结果字符串
	 */
	private String buildMysignV1(Map<String, String> sArray, String secretKey) {
		String mysign = "";
		try {
			String prestr = StrUtil.createLinkString(sArray); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
			prestr = prestr  + secretKey; // 把拼接后的字符串再与安全校验码连接起来
			mysign = StrUtil.getMD5String(prestr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mysign;
	}
	

}
