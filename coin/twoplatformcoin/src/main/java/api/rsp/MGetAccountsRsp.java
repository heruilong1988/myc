package api.rsp;

import api.huobi.response.Account;

import com.google.common.base.MoreObjects;
import java.util.List;

public class MGetAccountsRsp {

    private List<MAccount> accountList;

    public List<MAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<MAccount> accountList) {
        this.accountList = accountList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("accountList", accountList)
            .toString();
    }
}
