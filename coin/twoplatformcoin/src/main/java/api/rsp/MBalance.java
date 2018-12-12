package api.rsp;

import com.google.common.base.MoreObjects;
import java.util.List;
import java.util.Map;

public class MBalance {

    /**
     * id : 100009
     * type : spot
     * state : working
     * list : [{"currency":"usdt","type":"trade","balance":"500009195917.4362872650"}]
     * user-id : 1000
     */

    private String id;
    private String type;
    private String state;
    private String userid;
    private Map<String, MCurrencyBalance> mCurrencyBalanceMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Map<String, MCurrencyBalance> getmCurrencyBalanceMap() {
        return mCurrencyBalanceMap;
    }

    public void setmCurrencyBalanceMap(Map<String,MCurrencyBalance> mCurrencyBalanceMap) {
        this.mCurrencyBalanceMap = mCurrencyBalanceMap;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("type", type)
            .add("state", state)
            .add("userid", userid)
            .add("mCurrencyBalanceMap", mCurrencyBalanceMap)
            .toString();
    }
}
