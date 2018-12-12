package api.rsp;

import java.util.List;

public class MDepth {


    private List<PriceQtyPair> bids;
    private List<PriceQtyPair> asks;

    public MDepth(List<PriceQtyPair> bids, List<PriceQtyPair> asks) {
        this.bids = bids;
        this.asks = asks;
    }

    public List<PriceQtyPair> getBids() {
        return bids;
    }

    public void setBids(List<PriceQtyPair> bids) {
        this.bids = bids;
    }

    public List<PriceQtyPair> getAsks() {
        return asks;
    }

    public void setAsks(List<PriceQtyPair> asks) {
        this.asks = asks;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("bids", bids)
                .add("asks", asks)
                .toString();
    }
}
