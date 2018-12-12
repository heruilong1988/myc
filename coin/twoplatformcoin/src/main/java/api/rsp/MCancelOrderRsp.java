package api.rsp;

import com.google.common.base.MoreObjects;

public class MCancelOrderRsp {
    private String orderId;

    private String clientOrderId;
    private String oriClientOrderId;
    private String symbol;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getOriClientOrderId() {
        return oriClientOrderId;
    }

    public void setOriClientOrderId(String oriClientOrderId) {
        this.oriClientOrderId = oriClientOrderId;
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
            .add("orderId", orderId)
            .add("clientOrderId", clientOrderId)
            .add("oriClientOrderId", oriClientOrderId)
            .add("symbol", symbol)
            .toString();
    }
}
