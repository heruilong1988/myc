/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/12
 */

package api.binance.task;

import api.binance.BinanceApiRestClient;
import api.binance.domain.account.Account;
import api.binance.domain.account.AssetBalance;
import api.rsp.MBalance;
import api.rsp.MCurrencyBalance;
import api.rsp.MGetBalanceRsp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class BinanceGetBalanceTask implements Callable<MGetBalanceRsp> {

    private BinanceApiRestClient binanceApiRestClient;


    public BinanceGetBalanceTask(BinanceApiRestClient binanceApiRestClient) {
        this.binanceApiRestClient = binanceApiRestClient;
    }

    @Override
    public MGetBalanceRsp call() throws Exception {

        Account account = binanceApiRestClient.getAccount();

        List<AssetBalance> balances = account.getBalances();

        MGetBalanceRsp mGetBalanceRsp = new MGetBalanceRsp();
        MBalance mBalance = new MBalance();
        Map<String, MCurrencyBalance> mCurrencyBalanceMap = new HashMap<>();
        for(AssetBalance balance : balances) {
            String asset = balance.getAsset();
            String free = balance.getFree();
            String locked = balance.getLocked();

            MCurrencyBalance mCurrencyBalance = new MCurrencyBalance();
            mCurrencyBalance.setCurrency(asset);
            mCurrencyBalance.setFrozenBalance(Double.parseDouble(locked));
            mCurrencyBalance.setTradeBalance(Double.parseDouble(free));

            mCurrencyBalanceMap.put(asset, mCurrencyBalance);
        }

        mBalance.setmCurrencyBalanceMap(mCurrencyBalanceMap);
        mGetBalanceRsp.setmBalance(mBalance);
        return mGetBalanceRsp;
    }
}
