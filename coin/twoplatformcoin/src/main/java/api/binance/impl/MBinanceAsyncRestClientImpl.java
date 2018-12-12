/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/11
 */

package api.binance.impl;

import api.MAsyncRestClient;
import api.binance.BinanceApiClientFactory;
import api.binance.BinanceApiRestClient;
import api.binance.task.BinanceCancelOrderTask;
import api.binance.task.BinanceDepthTask;
import api.binance.task.BinanceGetAccountsTask;
import api.binance.task.BinanceGetBalanceTask;
import api.binance.task.BinancePlaceOrderTask;
import api.binance.task.BinanceQueryOrderTask;
import api.huobi.task.DepthTask;
import api.req.MCancelOrderRequest;
import api.req.MGetBalanceRequest;
import api.req.MPlaceOrderRequest;
import api.req.MQueryOrderRequest;
import api.rsp.MCancelOrderRsp;
import api.rsp.MDepth;
import api.rsp.MGetAccountsRsp;
import api.rsp.MGetBalanceRsp;
import api.rsp.MPlaceOrderRsp;
import api.rsp.MQueryOrderRsp;
import java.rmi.server.ExportException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MBinanceAsyncRestClientImpl implements MAsyncRestClient{

    private BinanceApiRestClient binanceApiRestClient;
    private ExecutorService executorService;

    public MBinanceAsyncRestClientImpl(BinanceApiRestClient binanceApiRestClient) {
        this.binanceApiRestClient = binanceApiRestClient;
        this.executorService = Executors.newFixedThreadPool(100);
    }

    public MBinanceAsyncRestClientImpl(String accesskey, String secretkey) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(accesskey, secretkey);
        this.binanceApiRestClient = factory.newRestClient();
        this.executorService = Executors.newFixedThreadPool(100);
    }

    public FutureTask<MDepth> depth(String baseCoin, String quoteCoin){
        BinanceDepthTask depthTask = new BinanceDepthTask(binanceApiRestClient, baseCoin, quoteCoin);
        FutureTask<MDepth> futureTask = new FutureTask<>(depthTask);
        executorService.execute(futureTask);

        return futureTask;
    }

    public FutureTask<MPlaceOrderRsp> placeOrder(MPlaceOrderRequest request){

        BinancePlaceOrderTask binancePlaceOrderTask = new BinancePlaceOrderTask(binanceApiRestClient, request);
        FutureTask<MPlaceOrderRsp> futureTask = new FutureTask<>(binancePlaceOrderTask);
        executorService.execute(futureTask);
        return futureTask;

    }

    public FutureTask<MQueryOrderRsp> queryOrder(MQueryOrderRequest request){
        BinanceQueryOrderTask binanceQueryOrderTask = new BinanceQueryOrderTask(binanceApiRestClient, request);
        FutureTask<MQueryOrderRsp> futureTask = new FutureTask<MQueryOrderRsp>(binanceQueryOrderTask);
        executorService.execute(futureTask);
        return futureTask;
    }

    public FutureTask<MCancelOrderRsp> cancelOrder(MCancelOrderRequest request) {
        BinanceCancelOrderTask binanceCancelOrderTask = new BinanceCancelOrderTask(binanceApiRestClient, request);
        FutureTask<MCancelOrderRsp> futureTask = new FutureTask<MCancelOrderRsp>(binanceCancelOrderTask);
        executorService.execute(futureTask);
        return futureTask;
    }

    public FutureTask<MGetAccountsRsp> getAccounts(){
        BinanceGetAccountsTask binanceGetAccountsTask = new BinanceGetAccountsTask(binanceApiRestClient);
        FutureTask<MGetAccountsRsp> futureTask = new FutureTask<MGetAccountsRsp>(binanceGetAccountsTask);
        executorService.execute(futureTask);
        return futureTask;
    }

    public FutureTask<MGetBalanceRsp> getBalance(MGetBalanceRequest mGetBalanceRequest){
        BinanceGetBalanceTask binanceGetBalanceTask = new BinanceGetBalanceTask(binanceApiRestClient);
        FutureTask<MGetBalanceRsp> futureTask = new FutureTask<MGetBalanceRsp>(binanceGetBalanceTask);
        executorService.execute(futureTask);
        return futureTask;
    }

}
