package api.rsp;

import com.google.common.base.MoreObjects;

public class MGetBalanceRsp {

    private MBalance mBalance;

    public MBalance getmBalance() {
        return mBalance;
    }

    public void setmBalance(MBalance mBalance) {
        this.mBalance = mBalance;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("mBalance", mBalance)
            .toString();
    }
}
