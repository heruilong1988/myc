package com.xiaohai.jaxrs.api;

import com.xiaohai.constant.enums.OrderDealType;
import com.xiaohai.jaxrs.service.base.PlatformFactory;
import com.xiaohai.jaxrs.service.base.PlatformService;
import com.xiaohai.jaxrs.vo.base.CancelOrderVO;
import com.xiaohai.jaxrs.vo.base.MyOrderVO;
import com.xiaohai.jaxrs.vo.base.OrderDataVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// AB  BC  AC 三个交易对
//x_ETH , ETH_BTC, x_BTC

//卖X(换ETH)，卖ETH（换BTC），买X（卖BTC）

public class TImpl {


    private static Logger LOGGER = LoggerFactory.getLogger(TImpl.class);

    public static void main(String[] args) {
        PlatformFactory platformFactory = new PlatformFactory();
        TImpl timpl = new TImpl(platformFactory.getPlatformService("binance"));
    }

    public TImpl(PlatformService platformService) {
        this.platformService = platformService;
    }

    PlatformService platformService;

    double profitRatio;
    int orderDataLength;
    String coinA;
    String coinB;
    String coinC;

    //AB挂单上一次查询是订单还剩余没交易的
    double lastABSellLimitOrderRemainQty;

    //上一次查询中C币的剩余数量
    double lastCBalance;

    boolean stop = false;

    String currentOrderId;

    //第一单的限制，避免交易量过大
    double maxBCoinSellLimit;

    public void service() {

        //init stage
        while (!stop) {

            OrderDataVO BCPairOrderDataVO = platformService.getCoinInfoOrderData(coinB, coinC);

            //C的数量基于卖掉B
            double amoutOfC = transferFromB2CByFirstOrderData(BCPairOrderDataVO);

            //A的数量基于卖掉C
            double amountOfTransferedA = transferFromC2A(amoutOfC);

            //根据BC第一个挂单获取B的数量，通过B的数量反推需要挂单A的数量
            double amountOfB = getBQtyOfFirstBCPairSellOrderData(BCPairOrderDataVO);

            //AB交易对排在第一的单价
            double priceOfABPairFirstSellOrderData = getPriceOfFirstOrderDataOfABPair();

            //通过B的数量，AB交易对的单价，推算挂单A的数量
            double originalAmountOfA = calcABPairSellOrderQty(priceOfABPairFirstSellOrderData, amountOfB);

            //计算是否达到利润
            boolean reachProfitRatio = reachProfit(originalAmountOfA, amountOfTransferedA);

            if (reachProfitRatio) {
                //下单
                String orderId = placeABPairSellLimitOrder(priceOfABPairFirstSellOrderData, originalAmountOfA);
                currentOrderId = orderId;

                trackOrderStatus();
            }

        }

    }

    public void trackOrderStatus() {
        while (true) {
            List<String> orderIds = new ArrayList<>();
            orderIds.add(currentOrderId);

            //挂单AB的order
            List<MyOrderVO> myOrderVOList = platformService.getOrders(coinA, coinB, orderIds);
            MyOrderVO myOrderVO = myOrderVOList.get(0);

            OrderDataVO bcOrderData = platformService.getCoinInfoOrderData(coinB, coinC);
            OrderDataVO acOrderData = platformService.getCoinInfoOrderData(coinA, coinC);

            tradeBCAndACIfThereIsNewTradeOnAB(myOrderVO, acOrderData.getSellData());

            if (isOrderCompleted(myOrderVO)) {
                //clean context
                currentOrderId = null;
                break;
            } else {
                //
                if(!checkIfStilProfit(myOrderVO.getPrice(), myOrderVO.getRemainAmount(), bcOrderData.getBuyData(),
                    acOrderData.getSellData())) {
                    //没有利润了
                    //cancel order
                    CancelOrderVO cancelOrderVO = new CancelOrderVO(coinA,coinB, String.valueOf(currentOrderId));
                    List<CancelOrderVO> cancelOrderVOS = new ArrayList<>();
                    cancelOrderVOS.add(cancelOrderVO);
                    platformService.cancelOrder(cancelOrderVOS);

                    currentOrderId = null;

                }else {
                    //still got profit
                    //continue for next round
                }
            }
        }

    }


    public boolean checkIfStilProfit(double ABSellPrice, double ABSellQty, double[][] BCBuyOrderData,
        double[][] ACSellOrderData) {
        double BQty = ABSellPrice * ABSellQty;

        double BQtyLeftToTrade = BQty;

        //Sell B from BC
        double CQtyTransferFromB = 0.0;
        for (int i = 0; i < orderDataLength; i++) {
            double price = BCBuyOrderData[i][0];
            double qty = BCBuyOrderData[i][1];

            if (qty < BQtyLeftToTrade) {
                CQtyTransferFromB += price * qty;
                BQtyLeftToTrade -= qty;
            } else {
                CQtyTransferFromB += price * BQtyLeftToTrade;
                BQtyLeftToTrade = 0;
                break;
            }

        }

        //Buy A from AC Pair
        double CQtyLeftToTradeForA = CQtyTransferFromB;
        double AQtyTransferFromC = 0.0;
        for (int i = 0; i < orderDataLength; i++) {
            double price = ACSellOrderData[i][0];
            double qty = ACSellOrderData[i][1];

            if (qty * price < CQtyLeftToTradeForA) {
                AQtyTransferFromC += qty;
                CQtyLeftToTradeForA -= qty * price;
            } else {
                AQtyTransferFromC += CQtyLeftToTradeForA / price;
                CQtyLeftToTradeForA = 0;
                break;
            }
        }

        return (AQtyTransferFromC - ABSellQty ) / ABSellQty > profitRatio;

    }

    public boolean isOrderCompleted(MyOrderVO myOrderVO) {
        return myOrderVO.getRemainAmount() == 0;
    }


    public double tradeBCAndACIfThereIsNewTradeOnAB(MyOrderVO myOrderVO, double[][] acSellOrderData) {
        double orderRemainQty = myOrderVO.getRemainAmount();
        double price = myOrderVO.getPrice();

        double tradeQty = lastABSellLimitOrderRemainQty - orderRemainQty;

        if (tradeQty > 0) {
            //there is new trade
            double amountOfB = tradeQty * price;
            placeBCPairSellMarketOrder(amountOfB);

            //check balance of c
            double cBalance = getBalanceOfC();

            //because BC pair has been traded, cBalance should increase
            double cTradeAmount = cBalance - lastCBalance;

            if (cTradeAmount > 0) {
                placeACPairBuyMarketOrder(cTradeAmount, acSellOrderData);
            } else {
                //abnormal
                LOGGER.info("abnormal cTradeAmount:{}", cTradeAmount);
            }

        }

        return 0.0;
    }


    public void placeACPairBuyMarketOrder(double coinCQty, double[][] acSellOrderData) {
        double coinAQty = 0.0;

        double leftCoinCTradeQty = coinCQty;

        for (int i = 0; i < orderDataLength; i++) {
            double price = acSellOrderData[i][0];
            double qty = acSellOrderData[i][1];

            if (price * qty > leftCoinCTradeQty) {
                coinAQty += leftCoinCTradeQty;
                leftCoinCTradeQty = 0;
                break;
            } else {
                coinAQty += price * qty;
                leftCoinCTradeQty -= (price * qty);
            }
        }

        platformService.order(OrderDealType.BuyMarket, coinA, coinC, 0, coinAQty, true);
    }

    public double getBalanceOfC() {
        List<String> coins = new ArrayList<>();
        coins.add(coinC);

        Map<String, Double> balanceList = platformService.getBalance(coins);

        double cBalance = balanceList.get(coinC);

        return cBalance;
    }

    public double getPriceOfFirstOrderDataOfABPair() {
        OrderDataVO orderDataVO = platformService.getCoinInfoOrderData(coinA, coinB);
        double[][] buyData = orderDataVO.getBuyData();
        return buyData[0][1];
    }

    public void placeBCPairSellMarketOrder(double qty) {
        platformService.order(OrderDealType.SellMarket, coinB, coinC, 0, qty, true);
    }

    public String placeABPairSellLimitOrder(double price, double qty) {

        String orderId = null;

        if(price * qty >  this.maxBCoinSellLimit) {
            //超过B的交易数量， 应该减少交易数量
            double limitQty = this.maxBCoinSellLimit / price;

            orderId =  platformService.order(OrderDealType.Sell, coinA, coinB, price, limitQty, true);
        }else {

            orderId = platformService.order(OrderDealType.Sell, coinA, coinB, price, qty, true);
        }
        return orderId;
    }

    /**
     * 卖B（C为), 根据第一单BC
     * @param orderDataVO
     * @return C的
     */
    public double transferFromB2CByFirstOrderData(OrderDataVO orderDataVO) {
        //因为卖B，所以需要获取的是买家的买单
        double[][] buyData = orderDataVO.getBuyData();
        double price = buyData[0][0];
        double qty = buyData[0][1];

        double amountOfC = price * qty;

        return amountOfC;
    }

    /**
     * BC第一个交易对中B的数量有多少，根据第二个交易对中的B的数量，推倒AB交易对中需要卖A的数量
     * @param orderDataVO
     * @return
     */

    public double getBQtyOfFirstBCPairSellOrderData(OrderDataVO orderDataVO) {
        double[][] sellData = orderDataVO.getSellData();
        double qty = sellData[0][1];
        return qty;
    }

    /**
     * 买A（卖C）
     * @param amountOfC
     * @return
     */
    public double transferFromC2A(double amountOfC) {
        //由于买A，所以获取的是卖家的卖单
        //coin pair is A_C
        OrderDataVO orderDataVO = platformService.getCoinInfoOrderData(coinA, coinC);
        double[][] sellData = orderDataVO.getSellData();

        double leftTradeAmountOfC = amountOfC;

        double amountOfA = 0.0;
        for (int i = 0; i < orderDataLength; i++) {
            double price = sellData[i][0];
            double qty = sellData[i][1];

            if (qty * price >= leftTradeAmountOfC) {
                amountOfA += leftTradeAmountOfC / price;
            } else {
                amountOfA += qty * price;
            }
        }

        return amountOfA;
    }

    public boolean reachProfit(double originalAmount, double transferAmount) {
        return (transferAmount - originalAmount) / originalAmount > profitRatio;
    }

    public double calcABPairSellOrderQty(double price, double amountOfB) {
        return amountOfB / price;
    }


}
