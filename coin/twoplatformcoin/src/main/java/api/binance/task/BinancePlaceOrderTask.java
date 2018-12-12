/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/11
 */

package api.binance.task;

import api.binance.BinanceApiRestClient;
import api.binance.constant.BinanceApiConstants;
import api.binance.domain.OrderSide;
import api.binance.domain.OrderType;
import api.binance.domain.TimeInForce;
import api.binance.domain.account.NewOrder;
import api.binance.domain.account.NewOrderResponse;
import api.binance.domain.account.Trade;
import api.binance.util.BinanceUtils;
import api.req.MPlaceOrderRequest;
import api.rsp.MOrderSide;
import api.rsp.MOrderStatus;
import api.rsp.MOrderType;
import api.rsp.MPlaceOrderRsp;
import api.rsp.MTimeInForce;
import api.rsp.MTrade;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BinancePlaceOrderTask implements Callable<MPlaceOrderRsp> {

    private BinanceApiRestClient binanceApiRestClient;
    private MPlaceOrderRequest mPlaceOrderRequest;

    public BinancePlaceOrderTask(BinanceApiRestClient binanceApiRestClient, MPlaceOrderRequest mPlaceOrderRequest) {
        this.binanceApiRestClient = binanceApiRestClient;
        this.mPlaceOrderRequest = mPlaceOrderRequest;
    }

    @Override
    public MPlaceOrderRsp call() throws Exception {
        NewOrder order = new NewOrder();
        String symbol = BinanceUtils.toSymbol(mPlaceOrderRequest.getBaseCoin(), mPlaceOrderRequest.getQuoteCoin());
        order.setSymbol(symbol);
        order.setPrice(String.valueOf(mPlaceOrderRequest.getPrice()));
        order.setQuantity(String.valueOf(mPlaceOrderRequest.getQuantity()));
        OrderType orderType = mPlaceOrderRequest.getType() == MOrderType.market ? OrderType.MARKET : OrderType.LIMIT;
        order.setType(orderType);
        order.setTimeInForce(TimeInForce.GTC); //good until cancel

        OrderSide orderSide = mPlaceOrderRequest.getSide() == MOrderSide.buy? OrderSide.BUY : OrderSide.SELL;
        order.setSide(orderSide);

        order.setTimestamp(System.currentTimeMillis());
        order.setRecvWindow(BinanceApiConstants.DEFAULT_RECEIVING_WINDOW);


        NewOrderResponse newOrderResponse = binanceApiRestClient.newOrder(order);
        MPlaceOrderRsp mPlaceOrderRsp = new MPlaceOrderRsp();

        mPlaceOrderRsp.setOrderId(String.valueOf(newOrderResponse.getOrderId()));
        mPlaceOrderRsp.setSymbol(newOrderResponse.getSymbol());
        mPlaceOrderRsp.setOrderId(String.valueOf(newOrderResponse.getOrderId()));
        mPlaceOrderRsp.setClientOrderId(newOrderResponse.getClientOrderId());
        mPlaceOrderRsp.setTransactTime(newOrderResponse.getTransactTime());
        mPlaceOrderRsp.setPrice(newOrderResponse.getPrice());
        mPlaceOrderRsp.setOrigQty(newOrderResponse.getOrigQty());
        mPlaceOrderRsp.setExecutedQty(newOrderResponse.getExecutedQty());
        mPlaceOrderRsp.setCummulativeQuoteQty(newOrderResponse.getCummulativeQuoteQty());

        MOrderStatus mOrderStatus = MOrderStatus.valueOf(newOrderResponse.getStatus().toString());
        mPlaceOrderRsp.setStatus(mOrderStatus);

        MTimeInForce mTimeInForce = MTimeInForce.valueOf(newOrderResponse.getTimeInForce().toString());
        mPlaceOrderRsp.setTimeInForce(mTimeInForce);

        MOrderType mOrderType = newOrderResponse.getType() == OrderType.LIMIT ? MOrderType.limit : MOrderType.market;
        mPlaceOrderRsp.setType(mOrderType);

        MOrderSide mOrderSide = newOrderResponse.getSide() == OrderSide.BUY ? MOrderSide.buy : MOrderSide.sell;
        mPlaceOrderRsp.setSide(mOrderSide);

        List<MTrade> mTradeList = new ArrayList<>();

        for(Trade trade : newOrderResponse.getFills()) {
            MTrade mTrade = BinanceUtils.toMTrade(trade);
            mTradeList.add(mTrade);
        }

        mPlaceOrderRsp.setFills(mTradeList);

        return mPlaceOrderRsp;
    }


}
