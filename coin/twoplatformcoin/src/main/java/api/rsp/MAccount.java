package api.rsp;

public class MAccount {

    private String accountId;
    public String type;
    public String state;


    private int makerCommission;

    /**
     * Taker commission.
     */
    private int takerCommission;

    /**
     * Buyer commission.
     */
    private int buyerCommission;

    /**
     * Seller commission.
     */
    private int sellerCommission;

    /**
     * Whether or not this account can trade.
     */
    private boolean canTrade;

    /**
     * Whether or not it is possible to withdraw from this account.
     */
    private boolean canWithdraw;

    /**
     * Whether or not it is possible to deposit into this account.
     */
    private boolean canDeposit;

    /**
     * Last account update time.
     */
    private long updateTime;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(int makerCommission) {
        this.makerCommission = makerCommission;
    }

    public int getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(int takerCommission) {
        this.takerCommission = takerCommission;
    }

    public int getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(int buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public int getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(int sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public boolean isCanTrade() {
        return canTrade;
    }

    public void setCanTrade(boolean canTrade) {
        this.canTrade = canTrade;
    }

    public boolean isCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public boolean isCanDeposit() {
        return canDeposit;
    }

    public void setCanDeposit(boolean canDeposit) {
        this.canDeposit = canDeposit;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MAccount{");
        sb.append("accountId='").append(accountId).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", makerCommission=").append(makerCommission);
        sb.append(", takerCommission=").append(takerCommission);
        sb.append(", buyerCommission=").append(buyerCommission);
        sb.append(", sellerCommission=").append(sellerCommission);
        sb.append(", canTrade=").append(canTrade);
        sb.append(", canWithdraw=").append(canWithdraw);
        sb.append(", canDeposit=").append(canDeposit);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
