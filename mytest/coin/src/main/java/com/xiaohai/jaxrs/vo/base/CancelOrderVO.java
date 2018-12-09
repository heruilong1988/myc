package com.xiaohai.jaxrs.vo.base;

import com.google.common.base.MoreObjects;

public class CancelOrderVO {
    String coinName;
    String baseCoinName;
    String orderNumber;

    public CancelOrderVO(String coinName, String baseCoinName, String orderNumber) {
        this.coinName = coinName;
        this.baseCoinName = baseCoinName;
        this.orderNumber = orderNumber;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getBaseCoinName() {
        return baseCoinName;
    }

    public void setBaseCoinName(String baseCoinName) {
        this.baseCoinName = baseCoinName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("coinName", coinName)
                .add("baseCoinName", baseCoinName)
                .add("orderNumber", orderNumber)
                .toString();
    }
}
