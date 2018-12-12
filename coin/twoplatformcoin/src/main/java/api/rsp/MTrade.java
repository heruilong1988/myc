/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/11
 */

package api.rsp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class MTrade {
    /**
     * Trade id.
     */
    private String tradeId;

    /**
     * Price.
     */
    private String price;

    /**
     * Quantity.
     */
    private String quantity;

    /**
     * Commission.
     */
    private String commission;

    /**
     * Asset on which commission is taken
     */
    private String commissionAsset;

    /**
     * Trade execution time.
     */
    private long time;

    /**
     * The symbol of the trade.
     */
    private String symbol;

    @JsonProperty("isBuyer")
    private boolean buyer;

    @JsonProperty("isMaker")
    private boolean maker;

    @JsonProperty("isBestMatch")
    private boolean bestMatch;

    private String orderId;


    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCommissionAsset() {
        return commissionAsset;
    }

    public void setCommissionAsset(String commissionAsset) {
        this.commissionAsset = commissionAsset;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("tradeId", tradeId)
            .add("price", price)
            .add("quantity", quantity)
            .add("commission", commission)
            .add("commissionAsset", commissionAsset)
            .add("time", time)
            .add("symbol", symbol)
            .add("buyer", buyer)
            .add("maker", maker)
            .add("bestMatch", bestMatch)
            .add("orderId", orderId)
            .toString();
    }
}
