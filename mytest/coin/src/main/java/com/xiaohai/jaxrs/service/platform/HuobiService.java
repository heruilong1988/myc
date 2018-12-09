package com.xiaohai.jaxrs.service.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaohai.common.exception.BizException;
import com.xiaohai.common.utils.base.CollectionUtil;
import com.xiaohai.common.utils.base.StrUtil;
import com.xiaohai.common.utils.http.RestClient;
import com.xiaohai.constant.enums.AllDealType;
import com.xiaohai.constant.enums.OrderDealType;
import com.xiaohai.constant.enums.PlatformDealLimit;
import com.xiaohai.jaxrs.service.base.PlatformService;
import com.xiaohai.jaxrs.vo.base.CancelOrderVO;
import com.xiaohai.jaxrs.vo.base.MyOrderVO;
import com.xiaohai.jaxrs.vo.base.OrderDataVO;
import com.xiaohai.jaxrs.vo.platform.huobi.CancelOrderHuobi;
import com.xiaohai.jaxrs.vo.platform.huobi.HuobiBalance;
import com.xiaohai.jaxrs.vo.platform.huobi.HuobiBalanceList;
import com.xiaohai.jaxrs.vo.platform.huobi.HuobiOrder;
import com.xiaohai.jaxrs.vo.platform.huobi.HuobiOrderData;
import com.xiaohai.jaxrs.vo.platform.huobi.HuobiPlaceResp;
import com.xiaohai.jaxrs.vo.platform.huobi.OrderInfoHuobi;
import com.xiaohai.jaxrs.vo.platform.huobi.SymbolPrecision;
import com.xiaohai.jaxrs.vo.platform.huobi.SymbolPrecisionData;
import com.xiaohai.utils.base.DateUtil;
import com.xiaohai.utils.base.RetryUtil;
import com.xiaohai.utils.http.HttpUtil;

@SuppressWarnings("restriction")
@Service
public class HuobiService extends PlatformService {
	
	private static final Logger LOG = LoggerFactory.getLogger(HuobiService.class);
	
	private static Map<String, String> precision;
	

	@Value("${huobiApiKey}")
	private String apiKey;
	@Value("${huobiSecretKey}")
	private String secret;
	@Value("${huobiSAccountId}")
	private String accountId;
	private String marketUrlPre = "https://api.huobi.pro/market";
	private String tradeUrlPre = "https://api.huobi.pro";
	private static Mac shaMac = null;
	
	private static String status = "ok";

	static {
		precision = getSymbolPrecision();
	}
	
	public static void main(String[] args) {
		HuobiService huobiService = new HuobiService();
		
//		OrderDataVO orderData = huobiService.getCoinInfoOrderData("pnt", "btc");
//		System.out.println(JSON.toJSONString(orderData));
		
//		List<MyOrderVO> myOrderVOs = huobiService.getOrders("pnt", "btc");
//		System.out.println(JSON.toJSONString(myOrderVOs)); // 10740283889 10740290296 10740294019
		
//		List<String> coinNames = new ArrayList<String>();
//		coinNames.add("eth");
//		Map<String, Double> map = huobiService.getBalance(coinNames);
//		System.out.println(map.get("eth"));
		
//		List<CancelOrderVO> cancelOrderVOList = new ArrayList<CancelOrderVO>();
//		CancelOrderVO cancelOrderVO = new CancelOrderVO("pnt", "btc", "10740283889");
//		cancelOrderVOList.add(cancelOrderVO);
//		cancelOrderVO = new CancelOrderVO("pnt", "btc", "10740290296");
//		cancelOrderVOList.add(cancelOrderVO);
//		huobiService.cancelOrder(cancelOrderVOList);
		
		String orderId = huobiService.order(OrderDealType.Sell, "pnt", "btc", 0.0000004, 1000, false);
		System.out.println(orderId);
		
	}

	@Override
	public OrderDataVO getCoinInfoOrderData(String coinName, String baseCoinName) {
		String orderBookUrl = String.format("%s/depth?symbol=%s%s&type=step0", 
				this.marketUrlPre, coinName.toLowerCase(), baseCoinName.toLowerCase());
		
		OrderDataVO orderData = new OrderDataVO();
		Date nowDate = new Date();
		orderData.setCreateTime(nowDate);
		orderData.setCoinName(coinName);
		orderData.setBaseCoinName(baseCoinName);
		orderData.setPlatform(this.getServiceName());
		
		String orderInfoJson = "";
		OrderInfoHuobi orderInfoHuobi = null;
		
		orderInfoJson = RestClient.getClient().getForObject(orderBookUrl, String.class);
		orderInfoHuobi = JSON.parseObject(orderInfoJson, OrderInfoHuobi.class);
		
		if (orderInfoHuobi == null || !status.equals(orderInfoHuobi.getStatus()) || orderInfoHuobi.getTick() == null) {
			return orderData;
		}
		
		orderData.setTimestamp(orderInfoHuobi.getTick().getTs());
		double[][] bids = orderInfoHuobi.getTick().getBids(); //买的挂单
		double[][] asks = orderInfoHuobi.getTick().getAsks(); //卖的挂单
		orderData.setBuyData(bids);
		orderData.setSellData(asks);
		
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
		for (int i = 0; i < bids.length; i++) {
			coinBuyMoney = bids[i][0];
			coinBuyNum += bids[i][1];
			coinBuyTotalAmount = coinBuyMoney * coinBuyNum;
			if (coinBuyTotalAmount > minDealLimit) {
				break;
			}
		}
		
		// 挂的卖单，要买入时超过最小金额限制的价格和数量
		for (int i = 0; i < asks.length; i++) {
			coinSellMoney = asks[i][0];
			coinSellNum += asks[i][1];
			coinSellTotalAmount = coinSellMoney * coinSellNum;
			if (coinSellTotalAmount > minDealLimit) {
				break;
			}
		}
		
		orderData.setActualBuyAmount(coinBuyNum);
		orderData.setActualBuyPrice(coinBuyMoney);
		orderData.setActualSellPrice(coinSellMoney);
		orderData.setActualSellAmount(coinSellNum);
		
		return orderData;
	}

	@Override
	public boolean getOrderIsExsit(String coinName, String baseCoinName, List<String> orderIds) {
		boolean result = false;
		List<MyOrderVO> myOrderVOs = this.getOrders(coinName, baseCoinName, orderIds);
		if (CollectionUtils.isEmpty(myOrderVOs)) {
			return result;
		}
		
		result = true;
		return result;
	}

	/**
	 * 根据交易对查询订单详情，当orderIds为空时，查询该交易对所有挂单信息
	 * @param coinName
	 * @param baseCoinName
	 * @param orderIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MyOrderVO> getOrders(String coinName, String baseCoinName, List<String> orderIds) {
		List<MyOrderVO> myOrderVOs = new ArrayList<MyOrderVO>();
		
		try {
			String symbol = String.format("%s%s", coinName.toLowerCase(), baseCoinName.toLowerCase());
			String openOrdersJson = "";
			
			String direct = "prev";
			String SignatureMethod="HmacSHA256";
			String SignatureVersion = "2";
			String states = "submitted";
			
			try {
				states = URLEncoder.encode("pre-submitted,submitted,partial-filled", "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String size = "50";
			
			String url = String.format("%s/v1/order/orders", this.tradeUrlPre);
			String timestamp = DateUtil.getUTCTimeStr();
			
			String queryArgs = String.format("AccessKeyId=%s&SignatureMethod=%s&SignatureVersion=%s&Timestamp=%s&direct=%s&size=%s&states=%s&symbol=%s", 
					this.apiKey, SignatureMethod, SignatureVersion, timestamp, direct, size, states, symbol);
			
			String Signature = this.getSignature(String.format("%s\n%s\n%s\n%s", "GET", "api.huobi.pro", "/v1/order/orders", queryArgs));
			queryArgs = queryArgs + "&Signature=" + Signature;
//		url = String.format("%s%s", url, StrUtil.isBlank(queryArgs) ? "" : "?" + queryArgs);
//		openOrdersJson = RestClient.getClient().getForObject(url, String.class);
			openOrdersJson = HttpUtil.sendGet(url, queryArgs);
			
			LOG.info(String.format("openOrdersJson:【%s】 ", openOrdersJson));
			HuobiOrder huobiOrder =  JSON.parseObject(openOrdersJson, HuobiOrder.class);
			huobiOrder =  JSON.parseObject(openOrdersJson, HuobiOrder.class);
			
			if (huobiOrder != null && status.equals(huobiOrder.getStatus()) && huobiOrder.getData() != null) {
				List<HuobiOrderData> huobiOrderDatas = huobiOrder.getData();
				MyOrderVO myOrderVO = null;
				boolean orderIdHasVal = CollectionUtil.isEmpty(orderIds);
				for (HuobiOrderData huobiOrderData : huobiOrderDatas) {
					if (!orderIdHasVal || orderIds.contains(String.valueOf(huobiOrderData.getId())));
					
					myOrderVO = new MyOrderVO();
					myOrderVO.setCoinName(coinName);
					myOrderVO.setBaseCoinName(baseCoinName);
					myOrderVO.setAmount(huobiOrderData.getAmount() != null ? Double.parseDouble(huobiOrderData.getAmount()) : 0);
					myOrderVO.setPrice(huobiOrderData.getPrice() != null ? Double.parseDouble(huobiOrderData.getPrice()) : 0);
					myOrderVO.setDealType(null);
					myOrderVO.setRemainAmount(huobiOrderData.getFieldAmoint() != null ? Double.parseDouble(huobiOrderData.getFieldAmoint()) : 0);
					myOrderVO.setTime(null);
					myOrderVOs.add(myOrderVO);
				}
			} else {
				throw new BizException(-1, String.format("【%s】获取市场详情失败，现在重试", this.getServiceName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(String.format("【%s】获取市场详情失败，现在重试", this.getServiceName()));
			myOrderVOs = (List<MyOrderVO>) RetryUtil.setRetryTimes(1).retry(coinName, baseCoinName);
			myOrderVOs = CollectionUtil.isEmpty(myOrderVOs) ? new ArrayList<MyOrderVO>() : myOrderVOs;
		}
		
		return myOrderVOs;
	}
	
	@Override
	public List<MyOrderVO> getOrders(List<String> orderIds) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> getBalance(List<String> coinNames) {
		Map<String, Double> result = new HashMap<String, Double>();
		
		if (CollectionUtils.isEmpty(coinNames)) {
			return result;
		}
		
		try {
			String urlAfter = String.format("/v1/account/accounts/%s/balance", this.accountId);
			String url = String.format("%s%s", this.tradeUrlPre, urlAfter);
			String SignatureMethod="HmacSHA256";
			String SignatureVersion = "2";
			String timestamp = DateUtil.getUTCTimeStr();
			String queryArgs = String.format("AccessKeyId=%s&SignatureMethod=%s&SignatureVersion=%s&Timestamp=%s", 
					this.apiKey, SignatureMethod, SignatureVersion, timestamp);
			String Signature = this.getSignature(String.format("%s\n%s\n%s\n%s", "GET", "api.huobi.pro", urlAfter, queryArgs));
			queryArgs = queryArgs + "&Signature=" + Signature;
			
//				String accountInfoJson = SimpleRestClient.getClient().getForObject(url, String.class, com.xiaohai.utils.base.StrUtil.getParamsToMap(queryArgs));
			String accountInfoJson = HttpUtil.sendGet(url, queryArgs);
			LOG.info(String.format("accountInfoJson:【%s】 ", accountInfoJson));
			
			if (StrUtil.isBlank(accountInfoJson)) {
				throw new BizException(-1, "获取余额失败");
			}
			HuobiBalance huobiBalance =  JSON.parseObject(accountInfoJson, HuobiBalance.class);
			if (huobiBalance == null || !status.equals(huobiBalance.getStatus()) || huobiBalance.getData() == null || CollectionUtils.isEmpty(huobiBalance.getData().getList())) {
				throw new BizException(-1, "获取余额失败");
			}
			
			double balance = 0;
			List<HuobiBalanceList> huobiBalanceLists = huobiBalance.getData().getList();
			for (HuobiBalanceList huobiBalanceList : huobiBalanceLists) {
				if (!"trade".equals(huobiBalanceList.getType())) {
					continue;
				}
				for (String coinName : coinNames) {
					if (coinName.toLowerCase().equals(huobiBalanceList.getCurrency().toLowerCase())) {
						balance = (huobiBalanceList.getBalance() != null ? Double.parseDouble(huobiBalanceList.getBalance()) : 0);
						result.put(coinName, balance);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(String.format("【%s】获取市场详情失败，现在重试", this.getServiceName()));
			result = (Map<String, Double>) RetryUtil.setRetryTimes(1).retry(coinNames);
			result = MapUtils.isEmpty(result) ? new HashMap<String, Double>() : result;
		}

		return result;
	}

	@Override
	public boolean cancelOrder(List<CancelOrderVO> cancelOrderVOList) {
		boolean result = false;
		if (CollectionUtil.isEmpty(cancelOrderVOList)) {
			result = true;
			return result;
		}
		
		try {
			Map<String, CancelOrderVO> cancelOrderMap = new HashMap<String, CancelOrderVO>();
			for (CancelOrderVO cancelOrder : cancelOrderVOList) {
				cancelOrderMap.put(cancelOrder.getOrderNumber(), cancelOrder);
			}
			
			List<String> orderIds = new ArrayList<String>();
			String urlAfter = "/v1/order/orders/batchcancel";
			String url = String.format("%s%s", this.tradeUrlPre, urlAfter);
			String queryArgs = "";
			String timestamp = "";
			String SignatureMethod="HmacSHA256";
			String SignatureVersion = "2";
			String Signature = "";
			String orderNum = "";
			for (CancelOrderVO cancelOrder : cancelOrderVOList) {
				if (StrUtil.isNotBlank(cancelOrder.getOrderNumber()) && !orderIds.contains(cancelOrder.getOrderNumber())) {
					orderIds.add(cancelOrder.getOrderNumber());
					continue;
				}
			}
			
			timestamp = DateUtil.getUTCTimeStr();
			queryArgs = String.format("AccessKeyId=%s&SignatureMethod=%s&SignatureVersion=%s&Timestamp=%s", 
					this.apiKey, SignatureMethod, SignatureVersion, timestamp, orderNum);
			Signature = this.getSignature(String.format("%s\n%s\n%s\n%s", "POST", "api.huobi.pro", urlAfter, queryArgs));
			queryArgs = queryArgs + "&Signature=" + Signature;
			
			JSONObject params = new JSONObject();
			params.put("order-ids", orderIds);
//		String cancelOrderJson = SimpleRestClient.postObject(url + "?" + queryArgs, null, params.toString(), String.class);
			String cancelOrderJson = HttpUtil.postUrl(url + "?" + queryArgs, params);
			LOG.info(String.format("cancelOrderJson:【%s】 ", cancelOrderJson));
			
			CancelOrderHuobi cancelOrderHuobi = JSON.parseObject(cancelOrderJson, CancelOrderHuobi.class);
			
			if (cancelOrderHuobi != null && status.equals(cancelOrderHuobi.getStatus())) {
				if (cancelOrderHuobi.getData() != null && !CollectionUtil.isEmpty(cancelOrderHuobi.getData().getSuccess())) {
					Map<String, String> successOrderIdMap = new HashMap<String, String>();
					for (String orderId : cancelOrderHuobi.getData().getSuccess()) {
						successOrderIdMap.put(orderId, orderId);
					}
					
					CancelOrderVO cancelOrder = null; 
					for (int i = 0; i < cancelOrderVOList.size(); i++) {
						cancelOrder = cancelOrderVOList.get(i);
						if (successOrderIdMap.get(cancelOrder.getOrderNumber()) != null) {
							cancelOrderVOList.remove(i);
							i--;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!CollectionUtil.isEmpty(cancelOrderVOList)) {
			LOG.info(String.format("【%s】取消订单失败，现在重试", this.getServiceName()));
			Boolean temp = (Boolean) RetryUtil.setRetryTimes(1).retry(cancelOrderVOList);
			result = (temp != null ? temp : false);
		} else {
			result = true;
		}
		
		return result;
	}

	@SuppressWarnings("static-access")
	@Override
	public String order(OrderDealType orderDealType, String coinName, String baseCoinName, double price, double amount, boolean needLimitAmount) {
		String orderId = "";
		try {
			if (MapUtils.isEmpty(precision)) {
				precision = this.getSymbolPrecision();
			}
	
			String SignatureMethod="HmacSHA256";
			String SignatureVersion = "2";
			AllDealType allDealType = AllDealType.getEnum(this.getServiceName(), orderDealType.getValue());
			String source = "api";
			String symbol = String.format("%s%s", coinName.toLowerCase(), baseCoinName.toLowerCase());
			String symbolPrecisionKey = String.format("%s_%s", coinName.toLowerCase(), baseCoinName.toLowerCase());
			String url = String.format("%s/v1/order/orders/place", this.tradeUrlPre);
		
			String timestamp = DateUtil.getUTCTimeStr();
			String queryArgs = String.format("AccessKeyId=%s&SignatureMethod=%s&SignatureVersion=%s&Timestamp=%s", 
						this.apiKey, SignatureMethod, SignatureVersion, timestamp);
			String Signature = this.getSignature(String.format("%s\n%s\n%s\n%s", "POST", "api.huobi.pro", "/v1/order/orders/place", queryArgs));
			queryArgs = queryArgs + "&Signature=" + Signature;
			
			JSONObject params = new JSONObject();
			params.put("account-id", this.accountId);
			String priceAmountPrecision = precision.get(symbolPrecisionKey);
			String[] priceAmountPrecisionArr = priceAmountPrecision.split("_");
			if (AllDealType.HuobiBuy.equals(allDealType) || AllDealType.HuobiSell.equals(allDealType)) {
				params.put("amount", String.format("%." + priceAmountPrecisionArr[1] + "f", amount));
				params.put("price", String.format("%." + priceAmountPrecisionArr[0]  + "f", price));
			}  else if (AllDealType.HuobiBuyMarket.equals(allDealType)) {
				params.put("amount", String.format("%." + priceAmountPrecisionArr[0]  + "f", amount * price));
			} else if (AllDealType.HuobiSellMarket.equals(allDealType)) {
				params.put("amount", String.format("%." + priceAmountPrecisionArr[1]  + "f", amount));
			}
			
			params.put("source", source);
			params.put("symbol", symbol);
			params.put("type", allDealType.getValue());

//			dealJson = SimpleRestClient.postObject(url + "?" + queryArgs, null, com.xiaohai.utils.base.StrUtil.getParamsToMap(queryArgs).toString(), String.class);
			String dealJson = HttpUtil.postUrl(url + "?" + queryArgs, params);
			LOG.info("dealJson" + dealJson);
			HuobiPlaceResp huobiPlaceResp = JSON.parseObject(dealJson, HuobiPlaceResp.class);
			
			if (huobiPlaceResp != null && status.equals(huobiPlaceResp.getStatus())) {
				orderId = huobiPlaceResp.getData();
			} else {
				throw new BizException(-1, "下单失败");
			}
		} catch (Exception e) {
			LOG.info(String.format("【%s】下单失败，现在重试", this.getServiceName()));
			orderId = (String) RetryUtil.setRetryTimes(1).retry(orderDealType, coinName, baseCoinName, price, amount, needLimitAmount);
			orderId = (orderId != null ? orderId : "");
		}
		
		return orderId;
	}

	@Override
	public String getServiceName() {
		return "huobi";
	}
	
	/**
	 * 获取签名
	 * @param queryArgs
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	private String getSignature(String queryArgs) {
		String sign = "";
		try {
			if (this.shaMac == null) {
				this.shaMac = Mac.getInstance("HmacSHA256");
				SecretKeySpec keySpec = new SecretKeySpec(this.secret.getBytes(),
						"HmacSHA256");
				shaMac.init(keySpec);	
			}

			final byte[] macData = shaMac.doFinal(queryArgs.getBytes());

			final Base64 base64 = new Base64();
			//BASE64Encoder encoder = new BASE64Encoder();
			//sign = encoder.encode(macData);
			sign = base64.encodeToString(macData);
			sign = URLEncoder.encode(sign, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sign;
    }
	
	/**
	 * 获取下单精度map<coinname_basecoinname, price_amount>
	 * @return
	 */
	public static Map<String, String> getSymbolPrecision() {
		String status = "ok";
		
		Map<String, String> precision = new HashMap<String, String>();
		String hadaxSymbolUrl = String.format("https://api.hadax.com/v1/hadax/common/symbols");
		String huobiSymbolUrl = String.format("https://api.huobi.pro/v1/common/symbols");

		SymbolPrecision huobiSymbolPrecision = RestClient.getClient().getForObject(huobiSymbolUrl, SymbolPrecision.class);
		SymbolPrecision hadaxSymbolPrecision = RestClient.getClient().getForObject(hadaxSymbolUrl, SymbolPrecision.class);
		
		List<SymbolPrecisionData> symbolPrecisionDatas = null;
		if (hadaxSymbolPrecision != null && status.equals(hadaxSymbolPrecision.getStatus())
				&& huobiSymbolPrecision != null && status.equals(huobiSymbolPrecision.getStatus())) {
			symbolPrecisionDatas = hadaxSymbolPrecision.getData();
			
			String key = "";
			String value = "";
			for (SymbolPrecisionData symbolPrecisionData : symbolPrecisionDatas) {
				key = String.format("%s_%s", symbolPrecisionData.getBaseCurrency(), symbolPrecisionData.getQuoteCurrency());
				value = String.format("%s_%s", symbolPrecisionData.getPricePrecision(), symbolPrecisionData.getAmountPrecision());
				precision.put(key, value);
			}
			
			symbolPrecisionDatas = huobiSymbolPrecision.getData();
			for (SymbolPrecisionData symbolPrecisionData : symbolPrecisionDatas) {
				key = String.format("%s_%s", symbolPrecisionData.getBaseCurrency(), symbolPrecisionData.getQuoteCurrency());
				value = String.format("%s_%s", symbolPrecisionData.getPricePrecision(), symbolPrecisionData.getAmountPrecision());
				precision.put(key, value);
			}
		}
		
		return precision;
	}

}
