/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/11
 */

package api.rsp;

import com.google.common.base.MoreObjects;

public class MCurrencyBalance {


    private String currency;
    private double tradeBalance;
    private double frozenBalance;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getTradeBalance() {
        return tradeBalance;
    }

    public void setTradeBalance(double tradeBalance) {
        this.tradeBalance = tradeBalance;
    }

    public double getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(double frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("currency", currency)
            .add("tradeBalance", tradeBalance)
            .add("frozenBalance", frozenBalance)
            .toString();
    }
}
