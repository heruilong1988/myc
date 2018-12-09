package com.xiaohai.jaxrs.service.platform;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.xiaohai.common.utils.base.NumberUtil;
import com.xiaohai.jaxrs.vo.base.CancelOrderVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xiaohai.constant.enums.OrderDealType;
import com.xiaohai.constant.enums.PlatformDealLimit;
import com.xiaohai.jaxrs.service.base.PlatformService;
import com.xiaohai.jaxrs.vo.base.MyOrderVO;
import com.xiaohai.jaxrs.vo.base.OrderDataVO;
import com.xiaohai.jaxrs.vo.platform.binance.OrderInfoBinance;
import com.xiaohai.utils.http.HttpUtil;

@Service
public class BinanceService extends PlatformService {

	private static final Logger LOG = LoggerFactory.getLogger(BinanceService.class);

	@Value("${binanceApiKey}")
	private String apiKey;
	@Value("${binanceSecretKey}")
	private String secret;
	
	private String urlPre = "https://api.binance.com/api/v1";
	
	public static void main(String[] args) {
		BinanceService binanceService = new BinanceService();
		OrderDataVO orderData = binanceService.getCoinInfoOrderData("eth", "usdt");
		System.out.println(orderData);
		
		
	}
	
	@Override
	public OrderDataVO getCoinInfoOrderData(String coinName, String baseCoinName) {
		OrderDataVO orderData = new OrderDataVO();

		LOG.info(String.format("【%s】获取【%s】订单信息【%s】", this.getServiceName(), coinName, "开始"));
		String orderBookUrl = String.format("%s/depth?symbol=%s%s&limit=10", this.urlPre, coinName.toUpperCase(), baseCoinName.toUpperCase());
		String orderInfoJson = "";
		
		orderInfoJson = HttpUtil.sendGet(orderBookUrl, "");
		OrderInfoBinance orderInfoBinance = JSON.parseObject(orderInfoJson, OrderInfoBinance.class);
		
		Object[][] bids = orderInfoBinance.getBids(); //买的挂单
		Object[][] asks = orderInfoBinance.getAsks(); //卖的挂单
				
		int bidsLength = bids.length;
		int asksLength = asks.length;
		
		double[][] sellData = new double[asksLength][2];
		double[][] buyData = new double[bidsLength][2];
		
		for (int i = 0; i < asksLength; i++) {
			sellData[i][0] = NumberUtil.doubleValue((String) asks[i][0]);
			sellData[i][1] = NumberUtil.doubleValue((String) asks[i][1]);
		}
		
		for (int i = 0; i < bidsLength; i++) {
			buyData[i][0] = NumberUtil.doubleValue((String) bids[i][0]);
			buyData[i][1] = NumberUtil.doubleValue((String) bids[i][1]);
		}
		
		Date nowDate = new Date();
		orderData.setCreateTime(nowDate);
		orderData.setTimestamp(nowDate.getTime());
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

		return orderData;
	}

	@Override
	public boolean getOrderIsExsit(String coinName, String baseCoinName, List<String> orderIds) {
		return !getOrders(coinName, baseCoinName, orderIds).isEmpty();
	}

	@Override
	public List<MyOrderVO> getOrders(String coinName, String baseCoinName, List<String> orderIds) {
        List<MyOrderVO> myOrderVOS = new ArrayList<>();
//
//        List<BinanceOrder> binanceOrders = new ArrayList<BinanceOrder>();
//		String currencyPair = String.format("%s%s", coinName.toUpperCase(), baseCoinName.toUpperCase());
//		String url = "https://api.binance.com/api/v3/allOrders";
//		while (flag) {
//			try {
//				long timestamp = System.currentTimeMillis();
//				String queryArgs = "";
//				if (orderId > 0) {
//					queryArgs = String.format("symbol=%s&orderId=%d&limit=%d&timestamp=%d", 
//							currencyPair, orderId, limit, timestamp);
//				} else {
//					queryArgs = String.format("symbol=%s&limit=%d&timestamp=%d", currencyPair, limit, timestamp);
//				}
//				
////				List<NameValuePair> params = new ArrayList<NameValuePair>();
////				params.add(new BasicNameValuePair("symbol", currencyPair));
////				if (orderId > 0) {
////					params.add(new BasicNameValuePair("orderId", String.valueOf(orderId)));
////				}
////				params.add(new BasicNameValuePair("timestamp", String.valueOf(timestamp)));
//				
//				queryArgs = queryArgs + "&signature=" + this.getSignature(queryArgs);
//				
//				String openOrdersJson = this.sendGet(url, queryArgs);
////				LOG.info(openOrdersJson);
//				binanceOrders =  JSON.parseArray(openOrdersJson, BinanceOrder.class);
//				for (BinanceOrder binanceOrder : binanceOrders) {
//					if ("NEW".equals(binanceOrder.getStatus())) {
//						result.add(binanceOrder);
//					}
//				}
//				if (!openOrdersJson.contains("error")) {
//					flag = false;
//				}
//			} catch (Exception e) {
//				LOG.info(String.format("binnace获取【%s】币订单失败【%s】，稍等重试", currencyPair, e.getMessage()));
//				e.printStackTrace();
//				ThreadUtil.sleep(500);
//			}
//		}
//		
//		return result;
		return myOrderVOS;
	}
	
	@Override
	public List<MyOrderVO> getOrders(List<String> orderIds) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.xiaohai.jaxrs.service.base.PlatformService#getBalance(java.util.List)
	 */
	@Override
	public Map<String, Double> getBalance(List<String> coinNames) {
//        Account account = binanceApiRestClient.getAccount();

        Map<String, Double>  balanceMap = new HashMap<>();
//        for(String coinName : coinNames) {
//            balanceMap.put(coinName,Double.parseDouble(account.getAssetBalance(coinName).getFree()));
//        }

		// TODO Auto-generated method stub
		return balanceMap;
	}

	/* (non-Javadoc)
	 * @see com.xiaohai.jaxrs.service.base.PlatformService#cancelOrder(java.util.List)
	 */
	@Override
	public boolean cancelOrder(List<CancelOrderVO> cancelOrderVOList) {
		// TODO Auto-generated method stub
//        for(CancelOrderVO cancelOrderVO : cancelOrderVOList) {
//            CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(BinanceUtils.buildSymbol(cancelOrderVO.getCoinName(), cancelOrderVO.getBaseCoinName()), cancelOrderVO.getOrderNumber());
//            binanceApiRestClient.cancelOrder(cancelOrderRequest);
//        }
		return true;
	}

	/* (non-Javadoc)
	 * @see com.xiaohai.jaxrs.service.base.PlatformService#order(com.xiaohai.constant.enums.OrderDealType, java.lang.String, java.lang.String, double, double, boolean)
	 */
	@Override
	public String order(OrderDealType orderDealType, String coinName,
			String baseCoinName, double price, double amount,
			boolean needLimitAmount) {

//
//        NewOrder order = null;
//
//        if(orderDealType == OrderDealType.Buy) {
//            OrderSide orderSide = OrderSide.BUY;
//            order = new NewOrder(BinanceUtils.buildSymbol(coinName, baseCoinName),
//                    orderSide, OrderType.LIMIT, TimeInForce.GTC, String.valueOf(amount), String.valueOf(price));
//
//        }else if(orderDealType == OrderDealType.BuyMarket) {
//            OrderSide orderSide = OrderSide.BUY;
//            order = new NewOrder(BinanceUtils.buildSymbol(coinName, baseCoinName),
//                    orderSide, OrderType.MARKET, TimeInForce.GTC, String.valueOf(amount));
//
//        }else if(orderDealType == OrderDealType.Sell) {
//            OrderSide orderSide = OrderSide.SELL;
//            order = new NewOrder(BinanceUtils.buildSymbol(coinName, baseCoinName),
//                    orderSide, OrderType.LIMIT, TimeInForce.GTC, String.valueOf(amount), String.valueOf(price));
//
//        }else {
//            OrderSide orderSide = OrderSide.SELL;
//            order = new NewOrder(BinanceUtils.buildSymbol(coinName, baseCoinName),
//                    orderSide, OrderType.MARKET, TimeInForce.GTC, String.valueOf(amount));
//        }
//
//        binanceApiRestClient.newOrder(order);
		return "0";
	}

	public String getServiceName() {
		return "binance";
	}

}
