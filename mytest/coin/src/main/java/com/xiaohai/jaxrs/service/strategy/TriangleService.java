/** 
 * @version 
 * @author xiaohai
 * @date Jul 28, 2018 9:40:54 PM 
 * 
 */
package com.xiaohai.jaxrs.service.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.xiaohai.common.async.AsyncTask;
import com.xiaohai.common.async.AsyncThreadPool;
import com.xiaohai.common.utils.base.NumberUtil;
import com.xiaohai.common.utils.base.ThreadUtil;
import com.xiaohai.constant.enums.OrderDealType;
import com.xiaohai.constant.enums.PlatformDealFee;
import com.xiaohai.constant.enums.PlatformDealLimit;
import com.xiaohai.jaxrs.pojo.stratefgy.DealCoinInfo;
import com.xiaohai.jaxrs.pojo.stratefgy.DealCoinPair;
import com.xiaohai.jaxrs.pojo.stratefgy.TriangleReq;
import com.xiaohai.jaxrs.service.base.PlatformFactory;
import com.xiaohai.jaxrs.service.base.PlatformService;
import com.xiaohai.jaxrs.vo.base.MyOrderVO;
import com.xiaohai.jaxrs.vo.base.OrderDataVO;
import com.xiaohai.jaxrs.vo.stratefgy.CircleRioOrderInfo;

/**
 * @version
 * @author xiaohai
 * @date Jul 28, 2018 9:40:54 PM
 * 
 */
@Service
public class TriangleService {

	private static final Logger LOG = LoggerFactory.getLogger(TriangleService.class);

	@Autowired
	private PlatformFactory platformFactory;

	private Map<String, Integer> orderNumMap = new HashMap<String, Integer>();
	private Map<String, Integer> dealSleepMsMap = new HashMap<String, Integer>();
	private int orderNumMax = 1;
	private int dealSleepMs = 3000;

	public void process(TriangleReq req) {
		int count = req.getCount();
		orderNumMax = (orderNumMax > req.getOrderNumMax() ? orderNumMax : req.getOrderNumMax());
		dealSleepMs = (req.getDealSleepMs() <= 0 ? dealSleepMs : req.getDealSleepMs());
		int sleepMs = NumberUtil.intValue(req.getSleepMs()) >= 0 ? req.getSleepMs() : 10;

		// 先调接口初始化订单查询Map
		this.checkOrderNums(req.getDealCoinInfos(), 0);
		long startTimestamp = 0;
		for (int i = 0; i < count; i++) {
			// 判断程序是否继续执行
			// if (!this.weatherStopLoop()) {
			// System.out.println("程序获取数据库标志-----停止---退出循环");
			// break;
			// }

			// check挂单数量
			if (i % orderNumMax == 0) {
				this.checkOrderNums(req.getDealCoinInfos(), this.orderNumMax);
			}

			// 获取现有的销售币在数据库中的数据
			startTimestamp = System.currentTimeMillis();
			try {
				this.processByCircle(req.getDealCoinInfos());
			} catch (Exception e) {
				e.printStackTrace();
			}
			LOG.info(String.format("使用时间【%d】ms", System.currentTimeMillis()
					- startTimestamp));
			ThreadUtil.sleep(sleepMs); // 每次循环暂停100ms
		}
	}

	/**
	 * 检查现有挂单数量是否过多
	 * 
	 * @param dealCoinInfos
	 */
	private void checkOrderNums(List<DealCoinInfo> dealCoinInfos, int maxNum) {
		if (CollectionUtils.isEmpty(dealCoinInfos)) {
			return;
		}

		PlatformService platformService = null;
		List<DealCoinPair> dealCoinPairs = null;
		List<MyOrderVO> myOrders = new ArrayList<MyOrderVO>();
		String orderNumKey = "";
		for (DealCoinInfo dealCoinInfo : dealCoinInfos) {
			platformService = platformFactory.getPlatformService(dealCoinInfo
					.getPlatformName());
			dealCoinPairs = dealCoinInfo.getDealCoinPairs();
			if (CollectionUtils.isEmpty(dealCoinInfos)) {
				continue;
			}

			for (DealCoinPair dealCoinPair : dealCoinPairs) {
				orderNumKey = dealCoinPair.getCoinName1() + "_"
						+ dealCoinPair.getCoinName2();
				if (NumberUtil.intValue(orderNumMap.get(orderNumKey)) >= maxNum) {
					myOrders = platformService.getOrders(
							dealCoinPair.getCoinName1(),
							dealCoinPair.getCoinName2(), null);
					orderNumMap.put(orderNumKey, myOrders.size());
				}
			}
		}

		LOG.info("检查挂单数量结束");
	}

	// 根据平台和币种，获取后计算内部循环的收益率
	private void processByCircle(List<DealCoinInfo> dealCoinInfos) {
		List<OrderDataVO> orderDatas = new ArrayList<OrderDataVO>();
		Map<String, Double> rioMap = new HashMap<String, Double>();

		this.getOrderDatas(dealCoinInfos, orderDatas, rioMap); // 获取平台订单信息列表和交易比map
		this.computeProfitRioByCircle(orderDatas, rioMap);
	}

	/**
	 * 获取平台的订单信息和交易比Map信息
	 * 
	 * @param dealCoinInfos
	 * @param orderDatas
	 * @param rioMap
	 */
	private void getOrderDatas(List<DealCoinInfo> dealCoinInfos,
			List<OrderDataVO> orderDatas, Map<String, Double> rioMap) {
		if (CollectionUtils.isEmpty(dealCoinInfos)) {
			return;
		}

		String platfromName = "";
		PlatformService platformService = null;
		ArrayList<Future<OrderDataVO>> futures = new ArrayList<Future<OrderDataVO>>();
		List<DealCoinPair> dealCoinPairs = new ArrayList<DealCoinPair>();
		for (DealCoinInfo dealCoinInfo : dealCoinInfos) {
			platfromName = dealCoinInfo.getPlatformName();
			platformService = platformFactory.getPlatformService(platfromName);
			dealCoinPairs = dealCoinInfo.getDealCoinPairs();
			if (CollectionUtils.isEmpty(dealCoinPairs)) {
				continue;
			}
			for (DealCoinPair dealCoinPair : dealCoinPairs) {
				rioMap.put(String.format("%s_%s", dealCoinPair.getCoinName1(),
						dealCoinPair.getCoinName2()), dealCoinPair.getRio());
				dealSleepMsMap.put(String.format("%s_%s",
						dealCoinPair.getCoinName1(),
						dealCoinPair.getCoinName2()), dealCoinPair
						.getDealSleepMs());

				futures.add(AsyncThreadPool
						.submit(new AsyncTask<OrderDataVO>() {

							@Override
							protected OrderDataVO task() {
								PlatformService platformService = this
										.getParam("platform",
												PlatformService.class);
								String coinName1 = this.getParam("coinName1",
										String.class);
								String coinName2 = this.getParam("coinName2",
										String.class);
								OrderDataVO orderData = platformService
										.getCoinInfoOrderData(coinName1,
												coinName2);
								return orderData;
							}
						}.addParam("platform", platformService)
								.addParam("coinName1",
										dealCoinPair.getCoinName1())
								.addParam("coinName2",
										dealCoinPair.getCoinName2())));
			}
		}

		OrderDataVO orderData = null;
		for (Future<OrderDataVO> future : futures) {
			try {
				orderData = future.get();
				orderDatas.add(orderData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 根据获取到的orderData列表数据，在单平台内计算内部循环收益比
	private void computeProfitRioByCircle(List<OrderDataVO> orderDatas,
			Map<String, Double> rioMap) {
		Map<String, List<OrderDataVO>> platformOrderDataMap = new HashMap<String, List<OrderDataVO>>();

		if (CollectionUtils.isEmpty(orderDatas)) {
			return;
		}

		String platformName = "";
		List<OrderDataVO> platformOrderDataList = null;
		for (OrderDataVO orderData : orderDatas) {
			if (orderData == null || orderData.getCoinName() == null
					|| orderData.getBaseCoinName() == null
					|| orderData.getBuyData() == null
					|| orderData.getSellData() == null) {
				continue;
			}
			platformName = orderData.getPlatform();

			platformOrderDataList = platformOrderDataMap.get(platformName);
			if (platformOrderDataList == null) {
				platformOrderDataList = new ArrayList<OrderDataVO>();
			}
			if (!platformOrderDataList.contains(orderData)) {
				platformOrderDataList.add(orderData);
			}

			platformOrderDataMap.put(platformName, platformOrderDataList);
		}

		Map<String, OrderDataVO> btcOrderDataMap = new HashMap<String, OrderDataVO>();
		Map<String, OrderDataVO> ethOrderDataMap = new HashMap<String, OrderDataVO>();
		Map<String, OrderDataVO> usdtOrderDataMap = new HashMap<String, OrderDataVO>();

		Iterator<Map.Entry<String, List<OrderDataVO>>> entries = platformOrderDataMap
				.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, List<OrderDataVO>> entry = entries.next();
			platformOrderDataList = entry.getValue();
			if (!CollectionUtils.isEmpty(platformOrderDataList)) {
				// 这里拿到的是单平台的orderData数据，要分别存放到btc、eth、和usdt的map中；
				btcOrderDataMap = new HashMap<String, OrderDataVO>();
				ethOrderDataMap = new HashMap<String, OrderDataVO>();
				usdtOrderDataMap = new HashMap<String, OrderDataVO>();

				for (OrderDataVO orderData : platformOrderDataList) {
					switch (orderData.getBaseCoinName().toLowerCase()) {
					case "btc":
						btcOrderDataMap.put(orderData.getCoinName(), orderData);
						break;

					case "eth":
						ethOrderDataMap.put(orderData.getCoinName(), orderData);
						break;

					case "usdt":
						usdtOrderDataMap
								.put(orderData.getCoinName(), orderData);
						break;

					default:
						break;
					}
				}
			}

			List<CircleRioOrderInfo> circleRioOrderInfos = this
					.computeOrderDataProfitRioByCircle(platformName,
							btcOrderDataMap, ethOrderDataMap, usdtOrderDataMap,
							rioMap);
			CircleRioOrderInfo maxRioCircleRioOrderInfo = null;
			if (!CollectionUtils.isEmpty(circleRioOrderInfos)) {
				double maxRio = 1;
				for (CircleRioOrderInfo circleRioOrderInfo : circleRioOrderInfos) {
					if (circleRioOrderInfo.getRio() >= maxRio) {
						maxRio = circleRioOrderInfo.getRio();
						maxRioCircleRioOrderInfo = circleRioOrderInfo;
					}
				}

				if (maxRioCircleRioOrderInfo != null) {
					List<OrderDataVO> orderDataList = new ArrayList<OrderDataVO>();
					orderDataList.add(maxRioCircleRioOrderInfo.getOrderData1());
					orderDataList.add(maxRioCircleRioOrderInfo.getOrderData2());
					orderDataList.add(maxRioCircleRioOrderInfo.getOrderData3());
					String key = "";
					boolean dealFlag = true;

					for (OrderDataVO orderData : orderDataList) {
						key = String.format("%s_%s", orderData.getCoinName(),
								orderData.getBaseCoinName());
						if (orderNumMap.get(key) >= orderNumMax) {
							LOG.info(String.format("交易对【%s】的挂单数量大于【%d】，此单暂不交易",
									key, orderNumMax));
							dealFlag = false;
						}
					}

					if (dealFlag) {
						this.dealByCircle(
								maxRioCircleRioOrderInfo.getPlatformName(),
								maxRioCircleRioOrderInfo.getDirect(),
								maxRioCircleRioOrderInfo.getOrderData1(),
								maxRioCircleRioOrderInfo.getOrderData2(),
								maxRioCircleRioOrderInfo.getOrderData3());

						LOG.info(String.format("这里交易结束：【%s】",
								JSON.toJSONString(maxRioCircleRioOrderInfo)));
						int orderNumNow = 0;
						List<MyOrderVO> myOrders = null;
						PlatformService platformService = platformFactory
								.getPlatformService(maxRioCircleRioOrderInfo
										.getPlatformName());
						int realDealSleepMs = 0;
						int tempDealSleepMs = 0;
						for (OrderDataVO orderData : orderDataList) {
							key = String.format("%s_%s",
									orderData.getCoinName(),
									orderData.getBaseCoinName());
							tempDealSleepMs = NumberUtil
									.intValue(dealSleepMsMap.get(key));
							realDealSleepMs = (realDealSleepMs > tempDealSleepMs ? realDealSleepMs
									: tempDealSleepMs);
							orderNumNow = NumberUtil.intValue(orderNumMap
									.get(key)) + 1;
							if (orderNumNow >= orderNumMax) {
								myOrders = platformService.getOrders(
										orderData.getCoinName(),
										orderData.getBaseCoinName(), null);
								orderNumNow = myOrders.size();
							}
							orderNumMap.put(key, orderNumNow);
						}

						// 交易完成后，需要休眠3s，避免平台数据有缓存。导致多次交易后，后续的交易实际上做不到吃单
						ThreadUtil.sleep(realDealSleepMs > 0 ? realDealSleepMs
								: this.dealSleepMs);
					}
				}
			}

			// // 如果有收益率符合交易的，则将本次的结果入库
			// if (maxRioCircleRioOrderInfo != null) {
			// orderDataExMapper.insertOrderDatas(orderDatas);
			// }
		}
	}

	private List<CircleRioOrderInfo> computeOrderDataProfitRioByCircle(
			String platformName, Map<String, OrderDataVO> btcOrderDataMap,
			Map<String, OrderDataVO> ethOrderDataMap,
			Map<String, OrderDataVO> usdtOrderDataMap,
			Map<String, Double> rioMap) {
		List<CircleRioOrderInfo> circleRioOrderInfos = new ArrayList<CircleRioOrderInfo>();
		double rio = 0;
		double minRio = 1;

		PlatformDealFee platformDealFee = PlatformDealFee
				.getPlatformDealFee(platformName);
		double sellFee = platformDealFee.getSellFee();
		double buyFee = platformDealFee.getBuyFee();

		OrderDataVO orderData = null;
		String tempCoinName = "";
		OrderDataVO tempOrderData = null;
		Iterator<Map.Entry<String, OrderDataVO>> entries = usdtOrderDataMap
				.entrySet().iterator();
		double tempRio1 = 0;
		double tempRio2 = 0;
		while (entries.hasNext()) {
			Map.Entry<String, OrderDataVO> entry = entries.next();
			orderData = entry.getValue();
			tempCoinName = orderData.getCoinName();
			tempOrderData = btcOrderDataMap.get(tempCoinName);
			tempRio1 = rioMap.get(String.format("%s_%s",
					orderData.getCoinName(), orderData.getBaseCoinName()));

			if (tempOrderData != null) {
				tempRio2 = rioMap.get(String.format("%s_%s",
						tempOrderData.getCoinName(),
						tempOrderData.getBaseCoinName()));
				minRio = NumberUtils.max(tempRio1, tempRio2, 1);
				rio = this.computeOrderDataProfitRioByCircle(platformName,
						orderData, tempOrderData, usdtOrderDataMap.get("btc"),
						1, buyFee, sellFee);
				if (rio >= minRio) {
					circleRioOrderInfos.add(new CircleRioOrderInfo(
							platformName, rio, 1, orderData, tempOrderData,
							usdtOrderDataMap.get("btc")));
				}
				rio = this.computeOrderDataProfitRioByCircle(platformName,
						orderData, tempOrderData, usdtOrderDataMap.get("btc"),
						2, buyFee, sellFee);
				if (rio >= minRio) {
					circleRioOrderInfos.add(new CircleRioOrderInfo(
							platformName, rio, 2, orderData, tempOrderData,
							usdtOrderDataMap.get("btc")));
				}
			}

			tempOrderData = ethOrderDataMap.get(tempCoinName);
			if (tempOrderData != null) {
				tempRio2 = rioMap.get(String.format("%s_%s",
						tempOrderData.getCoinName(),
						tempOrderData.getBaseCoinName()));
				minRio = NumberUtils.max(tempRio1, tempRio2, 1);
				rio = this.computeOrderDataProfitRioByCircle(platformName,
						orderData, tempOrderData, usdtOrderDataMap.get("eth"),
						1, buyFee, sellFee);
				if (rio >= minRio) {
					circleRioOrderInfos.add(new CircleRioOrderInfo(
							platformName, rio, 1, orderData, tempOrderData,
							usdtOrderDataMap.get("eth")));
				}
				rio = this.computeOrderDataProfitRioByCircle(platformName,
						orderData, tempOrderData, usdtOrderDataMap.get("eth"),
						2, buyFee, sellFee);
				if (rio >= minRio) {
					circleRioOrderInfos.add(new CircleRioOrderInfo(
							platformName, rio, 2, orderData, tempOrderData,
							usdtOrderDataMap.get("eth")));
				}
			}
		}

		entries = btcOrderDataMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, OrderDataVO> entry = entries.next();
			orderData = entry.getValue();
			tempCoinName = orderData.getCoinName();
			tempOrderData = ethOrderDataMap.get(tempCoinName);

			if (tempOrderData != null) {
				tempRio1 = rioMap.get(String.format("%s_%s",
						orderData.getCoinName(), orderData.getBaseCoinName()));
				tempRio2 = rioMap.get(String.format("%s_%s",
						tempOrderData.getCoinName(),
						tempOrderData.getBaseCoinName()));
				minRio = NumberUtils.max(tempRio1, tempRio2, 1);
				rio = this.computeOrderDataProfitRioByCircle(platformName,
						orderData, tempOrderData, btcOrderDataMap.get("eth"),
						1, buyFee, sellFee);
				if (rio >= minRio) {
					circleRioOrderInfos.add(new CircleRioOrderInfo(
							platformName, rio, 1, orderData, tempOrderData,
							btcOrderDataMap.get("eth")));
				}
				rio = this.computeOrderDataProfitRioByCircle(platformName,
						orderData, tempOrderData, btcOrderDataMap.get("eth"),
						2, buyFee, sellFee);
				if (rio >= minRio) {
					circleRioOrderInfos.add(new CircleRioOrderInfo(
							platformName, rio, 2, orderData, tempOrderData,
							btcOrderDataMap.get("eth")));
				}
			}
		}

		return circleRioOrderInfos;
	}

	// direct 为1则正向传递（usdt->a->btc->usdt），为2则反向传递(usdt->btc->a->usdt)
	// orderData1(usdt交易对)，orderData2（btc交易对），orderData3（usdtbtc交易对）
	private double computeOrderDataProfitRioByCircle(String platformName,
			OrderDataVO orderData1, OrderDataVO orderData2,
			OrderDataVO orderData3, int direct, double buyFee, double sellFee) {
		double rio = 0;

		String orderData1CoinName1 = orderData1.getCoinName();
		String orderData1CoinName2 = orderData1.getBaseCoinName();
		String orderData2CoinName1 = orderData2.getCoinName();
		String orderData2CoinName2 = orderData2.getBaseCoinName();
		String orderData3CoinName1 = orderData3.getCoinName();
		String orderData3CoinName2 = orderData3.getBaseCoinName();
		if (direct == 1) {
			// 正向传递
			double orderData1BuyMoney = orderData1.getActualSellPrice();
			double orderData2SellMoney = orderData2.getActualBuyPrice();
			double ordreData3SellMoney = orderData3.getActualBuyPrice();

			double orderData1BuyCoin1Rio = 1.0 / orderData1BuyMoney
					* orderData2SellMoney * ordreData3SellMoney * buyFee
					* sellFee * sellFee;
			String remark = String
					.format("platform1【%s】，coin【%s】->coin【%s】【%10.8f】->coin【%s】【%10.8f】->coin【%s】【%10.8f】，收益率【%10.8f】",
							platformName,
							orderData1CoinName2,
							orderData1CoinName1,
							orderData1.getActualSellAmount()
									* orderData1.getActualSellPrice(),
							orderData2CoinName2,
							orderData2.getActualBuyAmount()
									* orderData2.getActualBuyPrice(),
							orderData3CoinName2,
							orderData3.getActualBuyAmount()
									* orderData3.getActualBuyPrice(),
							orderData1BuyCoin1Rio);
			rio = orderData1BuyCoin1Rio;
			LOG.info(remark);
		} else if (direct == 2) {
			// 反向传递
			double ordreData1SellMoney = orderData1.getActualBuyPrice();
			double ordreData2BuyMoney = orderData2.getActualSellPrice();
			double ordreData3BuyMoney = orderData3.getActualSellPrice();

			double orderData1SellCoin1Rio = 1.0 * ordreData1SellMoney
					/ ordreData2BuyMoney / ordreData3BuyMoney * sellFee
					* buyFee * buyFee;
			String remark = String
					.format("platform1【%s】，coin【%s】->coin【%s】【%10.8f】->coin【%s】【%10.8f】->coin【%s】【%10.8f】，收益率【%10.8f】",
							platformName,
							orderData3CoinName2,
							orderData3CoinName1,
							orderData3.getActualSellAmount()
									* orderData3.getActualSellPrice(),
							orderData2CoinName1,
							orderData2.getActualSellAmount()
									* orderData2.getActualSellPrice(),
							orderData3CoinName2,
							orderData1.getActualBuyAmount()
									* orderData1.getActualBuyPrice(),
							orderData1SellCoin1Rio);

			rio = orderData1SellCoin1Rio;
			LOG.info(remark);
		}

		return rio;
	}

	/**
	 * 根据入参开始循环deal
	 */
	private void dealByCircle(String platfromName, int direct,
			OrderDataVO orderData1, OrderDataVO orderData2,
			OrderDataVO orderData3) {
		PlatformService platformService = platformFactory
				.getPlatformService(platfromName);

		double rio = 1;
		long startTime = System.currentTimeMillis();
		PlatformDealFee fee = PlatformDealFee.Bitz;
		double sellFee = fee.getSellFee();
		double buyFee = fee.getBuyFee();
		if (direct == 1) {
			// 正向传递
			double price1 = orderData1.getActualSellPrice();
			double amount1 = orderData1.getActualSellAmount();
			double price2 = orderData2.getActualBuyPrice();
			double amount2 = orderData2.getActualBuyAmount();
			double price3 = orderData3.getActualBuyPrice();
			double amount3 = orderData3.getActualBuyAmount();

			double orderData1MinusBaseAmount = price1 * amount1;
			double orderData2MinusBaseAmount = price2 * amount2;
			double orderData3MinusBaseAmount = price3 * amount3;

			PlatformDealLimit orderData1Limit = PlatformDealLimit
					.getPlatformDealLimit(platfromName,
							orderData1.getBaseCoinName());
			PlatformDealLimit orderData2Limit = PlatformDealLimit
					.getPlatformDealLimit(platfromName,
							orderData2.getBaseCoinName());
			PlatformDealLimit orderData3Limit = PlatformDealLimit
					.getPlatformDealLimit(platfromName,
							orderData3.getBaseCoinName());
			double orderData1BaseAmountMaxDeal = orderData1Limit.getMaxLimit();
			double orderData2BaseAmountMaxDeal = orderData2Limit.getMaxLimit();
			double orderData3BaseAmountMaxDeal = orderData3Limit.getMaxLimit();

			orderData1MinusBaseAmount = Math.min(orderData1MinusBaseAmount,
					orderData1BaseAmountMaxDeal);
			orderData2MinusBaseAmount = Math.min(orderData2MinusBaseAmount,
					orderData2BaseAmountMaxDeal);
			orderData3MinusBaseAmount = Math.min(orderData3MinusBaseAmount,
					orderData3BaseAmountMaxDeal);

			orderData2MinusBaseAmount = Math.min(orderData1MinusBaseAmount
					/ price1 * price2, orderData2MinusBaseAmount);
			orderData3MinusBaseAmount = Math.min(orderData2MinusBaseAmount
					* price3, orderData3MinusBaseAmount);

			orderData2MinusBaseAmount = orderData3MinusBaseAmount / price3;
			orderData1MinusBaseAmount = orderData2MinusBaseAmount / price2
					* price1;

			orderData2MinusBaseAmount = orderData2MinusBaseAmount * buyFee
					* sellFee;
			orderData3MinusBaseAmount = orderData3MinusBaseAmount * buyFee
					* sellFee * sellFee;

			// 看各种基数交易额是否大于设置的最小值
			if (!(orderData1MinusBaseAmount > orderData1Limit.getMinLimit()
					* rio
					&& orderData2MinusBaseAmount > orderData2Limit
							.getMinLimit() * rio && orderData3MinusBaseAmount > orderData3Limit
					.getMinLimit() * rio)) {
				LOG.info(String
						.format("计算出来的交易额【%f】【%f】【%f】小于【%f】【%f】【%f】orderData1【%s】orderData2【%s】orderData3【%s】",
								orderData1MinusBaseAmount,
								orderData2MinusBaseAmount,
								orderData3MinusBaseAmount,
								orderData1Limit.getMinLimit() * rio,
								orderData2Limit.getMinLimit() * rio,
								orderData3Limit.getMinLimit() * rio,
								JSON.toJSONString(orderData1),
								JSON.toJSONString(orderData2),
								JSON.toJSONString(orderData3)));
				return;
			}

			amount1 = orderData1MinusBaseAmount / price1;
			amount2 = orderData2MinusBaseAmount / price2;
			amount3 = orderData3MinusBaseAmount / price3;

			// 3个异步交易开始
			ArrayList<Future<Map<String, String>>> futures = new ArrayList<Future<Map<String, String>>>();
			futures.add(AsyncThreadPool
					.submit(new AsyncTask<Map<String, String>>() {
						@Override
						protected Map<String, String> task() {
							PlatformService platformService = this.getParam(
									"platformService", PlatformService.class);
							OrderDealType orderDealType = this.getParam(
									"orderDealType", OrderDealType.class);
							String coinName1 = this.getParam("coinName1",
									String.class);
							String coinName2 = this.getParam("coinName2",
									String.class);
							double price = this.getParam("price", Double.class);
							double amount = this.getParam("amount",
									Double.class);
							String orderId = platformService.order(
									orderDealType, coinName1, coinName2, price,
									amount, true);

							Map<String, String> result = new HashMap<String, String>();
							result.put("orderDataId", orderId);
							result.put("coinName1", coinName1);
							result.put("coinName2", coinName2);
							result.put("price", String.valueOf(price));
							result.put("amount", String.valueOf(amount));
							result.put("orderDataName", "orderData1");
							result.put("dealType", orderDealType.getRemark());

							return result;
						}
					}.addParam("platformService", platformService)
							.addParam("orderDealType", OrderDealType.Buy)
							.addParam("coinName1", orderData1.getCoinName())
							.addParam("coinName2", orderData1.getBaseCoinName())
							.addParam("price", price1)
							.addParam("amount", amount1)));

			futures.add(AsyncThreadPool
					.submit(new AsyncTask<Map<String, String>>() {
						@Override
						protected Map<String, String> task() {
							PlatformService platformService = this.getParam(
									"platformService", PlatformService.class);
							OrderDealType orderDealType = this.getParam(
									"orderDealType", OrderDealType.class);
							String coinName1 = this.getParam("coinName1",
									String.class);
							String coinName2 = this.getParam("coinName2",
									String.class);
							double price = this.getParam("price", Double.class);
							double amount = this.getParam("amount",
									Double.class);
							String orderId = platformService.order(
									orderDealType, coinName1, coinName2, price,
									amount, true);

							Map<String, String> result = new HashMap<String, String>();
							result.put("orderDataId", orderId);
							result.put("coinName1", coinName1);
							result.put("coinName2", coinName2);
							result.put("price", String.valueOf(price));
							result.put("amount", String.valueOf(amount));
							result.put("orderDataName", "orderData2");
							result.put("dealType", orderDealType.getRemark());

							return result;
						}
					}.addParam("platformService", platformService)
							.addParam("orderDealType", OrderDealType.Sell)
							.addParam("coinName1", orderData2.getCoinName())
							.addParam("coinName2", orderData2.getBaseCoinName())
							.addParam("price", price2)
							.addParam("amount", amount2)));

			futures.add(AsyncThreadPool
					.submit(new AsyncTask<Map<String, String>>() {
						@Override
						protected Map<String, String> task() {
							PlatformService platformService = this.getParam(
									"platformService", PlatformService.class);
							OrderDealType orderDealType = this.getParam(
									"orderDealType", OrderDealType.class);
							String coinName1 = this.getParam("coinName1",
									String.class);
							String coinName2 = this.getParam("coinName2",
									String.class);
							double price = this.getParam("price", Double.class);
							double amount = this.getParam("amount",
									Double.class);
							String orderId = platformService.order(
									orderDealType, coinName1, coinName2, price,
									amount, true);

							Map<String, String> result = new HashMap<String, String>();
							result.put("orderDataId", orderId);
							result.put("coinName1", coinName1);
							result.put("coinName2", coinName2);
							result.put("price", String.valueOf(price));
							result.put("amount", String.valueOf(amount));
							result.put("orderDataName", "orderData3");
							result.put("dealType", orderDealType.getRemark());

							return result;
						}
					}.addParam("platformService", platformService)
							.addParam("orderDealType", OrderDealType.Sell)
							.addParam("coinName1", orderData3.getCoinName())
							.addParam("coinName2", orderData3.getBaseCoinName())
							.addParam("price", price3)
							.addParam("amount", amount3)));

			Map<String, String> orderDataMap = null;
			for (Future<Map<String, String>> future : futures) {
				try {
					orderDataMap = future.get();
					LOG.info(String
							.format("【%s】内循环orderDataName【%s】coinName1【%s】coinName2【%s】price【%s】amount【%s】,time【%d】",
									platfromName,
									orderDataMap.get("orderDataName"),
									orderDataMap.get("coinName1"),
									orderDataMap.get("coinName2"),
									orderDataMap.get("price"),
									orderDataMap.get("amount"),
									System.currentTimeMillis() - startTime));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if (direct == 2) {
			// 反向传递（usdt买-btc买-a卖-usdt）
			double price1 = orderData1.getActualBuyPrice();
			double amount1 = orderData1.getActualBuyAmount();
			double price2 = orderData2.getActualSellPrice();
			double amount2 = orderData2.getActualSellAmount();
			double price3 = orderData3.getActualSellPrice();
			double amount3 = orderData3.getActualSellAmount();

			double orderData1MinusAmount = amount1 * price1;
			double orderData2MinusAmount = amount2 * price2;
			double orderData3MinusAmount = amount3 * price3;

			PlatformDealLimit orderData1Limit = PlatformDealLimit
					.getPlatformDealLimit(platfromName,
							orderData1.getBaseCoinName());
			PlatformDealLimit orderData2Limit = PlatformDealLimit
					.getPlatformDealLimit(platfromName,
							orderData2.getBaseCoinName());
			PlatformDealLimit orderData3Limit = PlatformDealLimit
					.getPlatformDealLimit(platfromName,
							orderData3.getBaseCoinName());
			double orderData1BaseAmountMaxDeal = orderData1Limit.getMaxLimit();
			double orderData2BaseAmountMaxDeal = orderData2Limit.getMaxLimit();
			double orderData3BaseAmountMaxDeal = orderData3Limit.getMaxLimit();

			orderData1MinusAmount = Math.min(orderData1MinusAmount,
					orderData1BaseAmountMaxDeal);
			orderData2MinusAmount = Math.min(orderData2MinusAmount,
					orderData2BaseAmountMaxDeal);
			orderData3MinusAmount = Math.min(orderData3MinusAmount,
					orderData3BaseAmountMaxDeal);

			orderData2MinusAmount = Math.min(orderData3MinusAmount / price3,
					orderData2MinusAmount);
			orderData1MinusAmount = Math.min(orderData2MinusAmount / price2
					* price1, orderData1MinusAmount);

			orderData2MinusAmount = orderData1MinusAmount / price1 * price2;
			orderData3MinusAmount = orderData2MinusAmount * price3;

			orderData2MinusAmount = orderData2MinusAmount * buyFee;
			orderData1MinusAmount = orderData1MinusAmount * buyFee * buyFee
					* sellFee;

			// 看各种基数交易额是否大于设置的最小值
			if (!(orderData1MinusAmount > orderData1Limit.getMinLimit() * rio
					&& orderData2MinusAmount > orderData2Limit.getMinLimit()
							* rio && orderData3MinusAmount > orderData3Limit
					.getMinLimit() * rio)) {
				LOG.info(String
						.format("计算出来的交易额【%f】【%f】【%f】小于【%f】【%f】【%f】orderData1【%s】orderData2【%s】orderData3【%s】",
								orderData1MinusAmount, orderData2MinusAmount,
								orderData3MinusAmount,
								orderData1Limit.getMinLimit() * rio,
								orderData2Limit.getMinLimit() * rio,
								orderData3Limit.getMinLimit() * rio,
								JSON.toJSONString(orderData1),
								JSON.toJSONString(orderData2),
								JSON.toJSONString(orderData3)));
				return;
			}

			amount1 = orderData1MinusAmount / price1;
			amount2 = orderData2MinusAmount / price2;
			amount3 = orderData3MinusAmount / price3;

			// 3个异步交易开始
			ArrayList<Future<Map<String, String>>> futures = new ArrayList<Future<Map<String, String>>>();
			futures.add(AsyncThreadPool
					.submit(new AsyncTask<Map<String, String>>() {
						@Override
						protected Map<String, String> task() {
							PlatformService platformService = this.getParam(
									"platformService", PlatformService.class);
							OrderDealType orderDealType = this.getParam(
									"orderDealType", OrderDealType.class);
							String coinName1 = this.getParam("coinName1",
									String.class);
							String coinName2 = this.getParam("coinName2",
									String.class);
							double price = this.getParam("price", Double.class);
							double amount = this.getParam("amount",
									Double.class);
							String orderId = platformService.order(
									orderDealType, coinName1, coinName2, price,
									amount, true);

							Map<String, String> result = new HashMap<String, String>();
							result.put("orderDataId", orderId);
							result.put("coinName1", coinName1);
							result.put("coinName2", coinName2);
							result.put("price", String.valueOf(price));
							result.put("amount", String.valueOf(amount));
							result.put("orderDataName", "orderData1");
							result.put("dealType", orderDealType.getRemark());

							return result;
						}
					}.addParam("platformService", platformService)
							.addParam("orderDealType", OrderDealType.Sell)
							.addParam("coinName1", orderData1.getCoinName())
							.addParam("coinName2", orderData1.getBaseCoinName())
							.addParam("price", price1)
							.addParam("amount", amount1)));

			futures.add(AsyncThreadPool
					.submit(new AsyncTask<Map<String, String>>() {
						@Override
						protected Map<String, String> task() {
							PlatformService platformService = this.getParam(
									"platformService", PlatformService.class);
							OrderDealType orderDealType = this.getParam(
									"orderDealType", OrderDealType.class);
							String coinName1 = this.getParam("coinName1",
									String.class);
							String coinName2 = this.getParam("coinName2",
									String.class);
							double price = this.getParam("price", Double.class);
							double amount = this.getParam("amount",
									Double.class);
							String orderId = platformService.order(
									orderDealType, coinName1, coinName2, price,
									amount, true);

							Map<String, String> result = new HashMap<String, String>();
							result.put("orderDataId", orderId);
							result.put("coinName1", coinName1);
							result.put("coinName2", coinName2);
							result.put("price", String.valueOf(price));
							result.put("amount", String.valueOf(amount));
							result.put("orderDataName", "orderData2");
							result.put("dealType", orderDealType.getRemark());

							return result;
						}
					}.addParam("platformService", platformService)
							.addParam("orderDealType", OrderDealType.Buy)
							.addParam("coinName1", orderData2.getCoinName())
							.addParam("coinName2", orderData2.getBaseCoinName())
							.addParam("price", price2)
							.addParam("amount", amount2)));

			futures.add(AsyncThreadPool
					.submit(new AsyncTask<Map<String, String>>() {
						@Override
						protected Map<String, String> task() {
							PlatformService platformService = this.getParam(
									"platformService", PlatformService.class);
							OrderDealType orderDealType = this.getParam(
									"orderDealType", OrderDealType.class);
							String coinName1 = this.getParam("coinName1",
									String.class);
							String coinName2 = this.getParam("coinName2",
									String.class);
							double price = this.getParam("price", Double.class);
							double amount = this.getParam("amount",
									Double.class);
							String orderId = platformService.order(
									orderDealType, coinName1, coinName2, price,
									amount, true);

							Map<String, String> result = new HashMap<String, String>();
							result.put("orderDataId", orderId);
							result.put("coinName1", coinName1);
							result.put("coinName2", coinName2);
							result.put("price", String.valueOf(price));
							result.put("amount", String.valueOf(amount));
							result.put("orderDataName", "orderData3");
							result.put("dealType", orderDealType.getRemark());

							return result;
						}
					}.addParam("platformService", platformService)
							.addParam("orderDealType", OrderDealType.Buy)
							.addParam("coinName1", orderData3.getCoinName())
							.addParam("coinName2", orderData3.getBaseCoinName())
							.addParam("price", price3)
							.addParam("amount", amount3)));

			Map<String, String> orderDataMap = null;
			for (Future<Map<String, String>> future : futures) {
				try {
					orderDataMap = future.get();
					LOG.info(String
							.format("【%s】内循环orderDataName【%s】coinName1【%s】coinName2【%s】price【%s】amount【%s】,time【%d】",
									platfromName,
									orderDataMap.get("orderDataName"),
									orderDataMap.get("coinName1"),
									orderDataMap.get("coinName2"),
									orderDataMap.get("price"),
									orderDataMap.get("amount"),
									System.currentTimeMillis() - startTime));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
