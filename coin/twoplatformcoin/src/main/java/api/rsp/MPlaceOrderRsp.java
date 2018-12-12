package api.rsp;

import com.google.common.base.MoreObjects;
import java.util.List;

public class MPlaceOrderRsp {
    String orderId;

    private String symbol;


    private String clientOrderId;

    private String price;

    private String origQty;

    private String executedQty;

    private String cummulativeQuoteQty;

    private MOrderStatus status;

    private MTimeInForce timeInForce;

    private MOrderType type;

    private MOrderSide side;

    // @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<MTrade> fills;

    /**
     * Transact time for this order.
     */
    private Long transactTime;

    public MPlaceOrderRsp() {
    }

    public MPlaceOrderRsp(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrigQty() {
        return origQty;
    }

    public void setOrigQty(String origQty) {
        this.origQty = origQty;
    }

    public String getExecutedQty() {
        return executedQty;
    }

    public void setExecutedQty(String executedQty) {
        this.executedQty = executedQty;
    }

    public String getCummulativeQuoteQty() {
        return cummulativeQuoteQty;
    }

    public void setCummulativeQuoteQty(String cummulativeQuoteQty) {
        this.cummulativeQuoteQty = cummulativeQuoteQty;
    }

    public MOrderStatus getStatus() {
        return status;
    }

    public void setStatus(MOrderStatus status) {
        this.status = status;
    }

    public MTimeInForce getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(MTimeInForce timeInForce) {
        this.timeInForce = timeInForce;
    }

    public MOrderType getType() {
        return type;
    }

    public void setType(MOrderType type) {
        this.type = type;
    }

    public MOrderSide getSide() {
        return side;
    }

    public void setSide(MOrderSide side) {
        this.side = side;
    }

    public List<MTrade> getFills() {
        return fills;
    }

    public void setFills(List<MTrade> fills) {
        this.fills = fills;
    }

    public Long getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(Long transactTime) {
        this.transactTime = transactTime;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("orderId", orderId)
            .add("symbol", symbol)
            .add("clientOrderId", clientOrderId)
            .add("price", price)
            .add("origQty", origQty)
            .add("executedQty", executedQty)
            .add("cummulativeQuoteQty", cummulativeQuoteQty)
            .add("status", status)
            .add("timeInForce", timeInForce)
            .add("type", type)
            .add("side", side)
            .add("fills", fills)
            .add("transactTime", transactTime)
            .toString();
    }
}
