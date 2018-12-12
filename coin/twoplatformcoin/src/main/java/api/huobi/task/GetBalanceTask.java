package api.huobi.task;

import api.huobi.client.ApiClient;
import api.huobi.response.Balance;
import api.huobi.response.BalanceResponse;
import api.req.MGetBalanceRequest;
import api.rsp.MBalance;
import api.rsp.MCurrencyBalance;
import api.rsp.MGetBalanceRsp;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBalanceTask implements Callable<MGetBalanceRsp> {

    Logger LOGGER = LoggerFactory.getLogger(GetBalanceTask.class);

    private ApiClient apiClient;
    private MGetBalanceRequest mGetBalanceRequest;

    public GetBalanceTask(ApiClient apiClient, MGetBalanceRequest mGetBalanceRequest) {
        this.apiClient = apiClient;
        this.mGetBalanceRequest = mGetBalanceRequest;
    }


    @Override
    public MGetBalanceRsp call() throws Exception {

        String accountId = mGetBalanceRequest.getAccountId();

        BalanceResponse resp = apiClient
            .get("/v1/account/accounts/" + accountId + "/balance", null, new TypeReference<BalanceResponse<Balance>>() {
            });

        if (!"ok".equals(resp.getStatus())) {
            LOGGER.info("fail to get balance. mGetBalanceRequest:{},resp:{}", mGetBalanceRequest, resp);
            throw new Exception(resp.toString() + "---" + mGetBalanceRequest.toString());
        }

        Balance balance = (Balance) resp.getData();

        List<Map<String, String>> currencyBalanceList = (List)balance.getList();

        MBalance mBalance = new MBalance();
        mBalance.setId(balance.getId());
        mBalance.setState(balance.getState());
        mBalance.setType(balance.getType());
        mBalance.setUserid(balance.getUserid());


        Map<String, MCurrencyBalance> mCurrencyBalanceMap = new HashMap<>();

        for (int i = 0; i < currencyBalanceList.size(); i++) {
            //JSONObject balanceJson = (JSONObject) jsonArray.get(i);
            Map<String,String> currencyMap = currencyBalanceList.get(i);

            MCurrencyBalance mCurrencyBalance = new MCurrencyBalance();

            String currency = currencyMap.get("currency");
            mCurrencyBalance.setCurrency(currency);

            if(mCurrencyBalanceMap.containsKey(currency)) {
                mCurrencyBalance = mCurrencyBalanceMap.get(currency);
            }

            if("trade".equals(currencyMap.get("type"))) {
                mCurrencyBalance.setTradeBalance(Double.parseDouble(currencyMap.get("balance")));
            }else {
                mCurrencyBalance.setFrozenBalance(Double.parseDouble(currencyMap.get("balance")));
            }

            mCurrencyBalanceMap.put(currency, mCurrencyBalance);
        }

        mBalance.setmCurrencyBalanceMap(mCurrencyBalanceMap);

        MGetBalanceRsp mGetBalanceRsp = new MGetBalanceRsp();
        mGetBalanceRsp.setmBalance(mBalance);
        return mGetBalanceRsp;
    }
}
