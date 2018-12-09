package api.rsp;

import java.util.List;

public class Depth {


    private List<PriceQtyPair> bids;
    private List<PriceQtyPair> asks;


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
}
