package api.rsp;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class PriceQtyPair {
    private double price;
    private double qty;

    public PriceQtyPair() {
    }

    public PriceQtyPair(double price, double qty) {
        this.price = price;
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("price", price)
                .add("qty", qty)
                .toString();
    }
}
