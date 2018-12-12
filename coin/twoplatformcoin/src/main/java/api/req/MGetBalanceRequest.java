package api.req;

import com.google.common.base.MoreObjects;

public class MGetBalanceRequest {

    private String accountId;
    private String baseCoin;
    private String quoteCoin;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
            .add("accountId", accountId)
            .add("baseCoin", baseCoin)
            .add("quoteCoin", quoteCoin)
            .toString();
    }
}
