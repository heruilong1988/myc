package api.rsp;

import com.google.common.base.MoreObjects;

public class MQueryOrderRsp {

    private String accountId;
    private String quantity;
    private Long canceledAtTimestamp;
    private Long createdAtTimestamp;
    private String fieldQuantity;
    private String fieldCashQuantity;
    private String fieldFees;
    private Long finishedAtTimestamp;
    private String orderId;
    private String price;
    private String source;
    private MOrderStatus state;
    private String baseCoin;
    private String quoteCoin;
    private String symbol;

    private MOrderSide side;
    private MOrderType type;

    //binance
    private MTimeInForce mTimeInForce;
    private String stopPrice;
    private String icebergQuantity;
    private Long time;
    private String clientOrderId;

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getIcebergQuantity() {
        return icebergQuantity;
    }

    public void setIcebergQuantity(String icebergQuantity) {
        this.icebergQuantity = icebergQuantity;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public MTimeInForce getmTimeInForce() {
        return mTimeInForce;
    }

    public void setmTimeInForce(MTimeInForce mTimeInForce) {
        this.mTimeInForce = mTimeInForce;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Long getCanceledAtTimestamp() {
        return canceledAtTimestamp;
    }

    public void setCanceledAtTimestamp(Long canceledAtTimestamp) {
        this.canceledAtTimestamp = canceledAtTimestamp;
    }

    public Long getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public void setCreatedAtTimestamp(Long createdAtTimestamp) {
        this.createdAtTimestamp = createdAtTimestamp;
    }

    public String getFieldQuantity() {
        return fieldQuantity;
    }

    public void setFieldQuantity(String fieldQuantity) {
        this.fieldQuantity = fieldQuantity;
    }

    public String getFieldCashQuantity() {
        return fieldCashQuantity;
    }

    public void setFieldCashQuantity(String fieldCashQuantity) {
        this.fieldCashQuantity = fieldCashQuantity;
    }

    public String getFieldFees() {
        return fieldFees;
    }

    public void setFieldFees(String fieldFees) {
        this.fieldFees = fieldFees;
    }

    public Long getFinishedAtTimestamp() {
        return finishedAtTimestamp;
    }

    public void setFinishedAtTimestamp(Long finishedAtTimestamp) {
        this.finishedAtTimestamp = finishedAtTimestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MOrderStatus getState() {
        return state;
    }

    public void setState(MOrderStatus state) {
        this.state = state;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("accountId", accountId)
            .add("quantity", quantity)
            .add("canceledAtTimestamp", canceledAtTimestamp)
            .add("createdAtTimestamp", createdAtTimestamp)
            .add("fieldQuantity", fieldQuantity)
            .add("fieldCashQuantity", fieldCashQuantity)
            .add("fieldFees", fieldFees)
            .add("finishedAtTimestamp", finishedAtTimestamp)
            .add("orderId", orderId)
            .add("price", price)
            .add("source", source)
            .add("state", state)
            .add("baseCoin", baseCoin)
            .add("quoteCoin", quoteCoin)
            .add("symbol", symbol)
            .add("side", side)
            .add("type", type)
            .add("mTimeInForce", mTimeInForce)
            .add("stopPrice", stopPrice)
            .add("icebergQuantity", icebergQuantity)
            .add("time", time)
            .add("clientOrderId", clientOrderId)
            .toString();
    }
}
