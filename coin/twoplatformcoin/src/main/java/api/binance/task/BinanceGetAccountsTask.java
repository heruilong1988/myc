/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/12
 */

package api.binance.task;

import api.binance.BinanceApiRestClient;
import api.binance.domain.account.Account;
import api.rsp.MAccount;
import api.rsp.MGetAccountsRsp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BinanceGetAccountsTask implements Callable<MGetAccountsRsp> {

    private BinanceApiRestClient binanceApiRestClient;

    public BinanceGetAccountsTask(BinanceApiRestClient binanceApiRestClient) {
        this.binanceApiRestClient = binanceApiRestClient;
    }

    @Override
    public MGetAccountsRsp call() throws Exception {

        Account account = binanceApiRestClient.getAccount();

        MAccount mAccount = new MAccount();
        //mAccount.setAccountId(account);
        if(account.isCanTrade()) {
            mAccount.setState("working");
        }else {
            mAccount.setState("locked");
        }

        mAccount.setBuyerCommission(account.getBuyerCommission());
        mAccount.setSellerCommission(account.getSellerCommission());
        mAccount.setMakerCommission(account.getMakerCommission());
        mAccount.setTakerCommission(account.getTakerCommission());
        mAccount.setCanTrade(account.isCanTrade());
        mAccount.setCanDeposit(account.isCanDeposit());
        mAccount.setCanWithdraw(account.isCanWithdraw());

        MGetAccountsRsp mGetAccountsRsp = new MGetAccountsRsp();

        List<MAccount> mAccountList = new ArrayList<>();
        mAccountList.add(mAccount);

        mGetAccountsRsp.setAccountList(mAccountList);
        return mGetAccountsRsp;
    }
}
