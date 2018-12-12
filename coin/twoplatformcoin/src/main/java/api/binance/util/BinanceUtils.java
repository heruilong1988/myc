/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/11
 */

package api.binance.util;

import api.binance.domain.OrderSide;
import api.binance.domain.OrderStatus;
import api.binance.domain.OrderType;
import api.binance.domain.account.Trade;
import api.rsp.MOrderSide;
import api.rsp.MOrderStatus;
import api.rsp.MOrderType;
import api.rsp.MTrade;

public class BinanceUtils {

    public static String toSymbol(String baseCoin, String quoteCoin) {
        return
            baseCoin.toUpperCase() + quoteCoin.toUpperCase();
    }

    public static MTrade toMTrade(Trade trade) {
        MTrade mTrade = new MTrade();
        mTrade.setCommission(trade.getCommission());
        mTrade.setCommissionAsset(trade.getCommissionAsset());
        mTrade.setPrice(trade.getPrice());
        mTrade.setQuantity(trade.getQty());
        mTrade.setSymbol(trade.getSymbol());
        mTrade.setTime(trade.getTime());
        mTrade.setTradeId(String.valueOf(trade.getId()));
        return mTrade;
    }

    public static MOrderStatus toMOrderstatus(OrderStatus orderStatus) {
        return MOrderStatus.valueOf(orderStatus.toString());
    }

    public static MOrderType toMOrderType(OrderType orderType) {
        if(orderType == OrderType.LIMIT) {
            return MOrderType.limit;
        }

        return MOrderType.market;
    }

    public static MOrderSide toMOrderSide(OrderSide orderSide) {
        if(orderSide == OrderSide.BUY) {
            return MOrderSide.buy;
        }

        return MOrderSide.sell;
    }
}
