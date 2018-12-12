package api.req;

import api.rsp.MOrderSide;
import api.rsp.MOrderType;
import com.google.common.base.MoreObjects;

public class MPlaceOrderRequest {

    private MOrderSide side;
    private MOrderType type;
    private double price;
    private double quantity;
    private String baseCoin;
    private String quoteCoin;

    //huobi
    private String accountId;


    public MOrderSide getSide() {
        return side;
    }

    public void setSide(MOrderSide side) {
        this.side = side;
    }

    public MOrderType getType() {
        return type;
    }

    public void setType(MOrderType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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
                .add("price", price)
                .add("quantity", quantity)
                .add("baseCoin", baseCoin)
                .add("quoteCoin", quoteCoin)
                .add("accountId", accountId)
                .toString();
    }
}
