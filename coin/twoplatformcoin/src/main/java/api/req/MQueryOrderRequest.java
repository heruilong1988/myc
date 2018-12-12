package api.req;

import com.google.common.base.MoreObjects;

public class MQueryOrderRequest {

    private String orderId;
    private String baseCoin;
    private String quoteCoin;
    private String accountId;

    //unique orderid
    private String clientOrderId;

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("orderId", orderId)
                .add("baseCoin", baseCoin)
                .add("quoteCoin", quoteCoin)
                .add("accountId", accountId)
                .toString();
    }
}
