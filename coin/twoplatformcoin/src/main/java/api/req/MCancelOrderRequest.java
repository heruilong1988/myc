package api.req;

import com.google.common.base.MoreObjects;

public class MCancelOrderRequest {
    private String orderId;

    private String baseCoin;
    private String quoteCoin;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getQuoteCoin() {
        return quoteCoin;
    }

    public void setQuoteCoin(String quoteCoin) {
        this.quoteCoin = quoteCoin;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("orderId", orderId)
            .add("baseCoin", baseCoin)
            .add("quoteCoin", quoteCoin)
            .toString();
    }
}
