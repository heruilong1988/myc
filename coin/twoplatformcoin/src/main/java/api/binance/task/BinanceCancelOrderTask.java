/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/12
 */

package api.binance.task;

import api.binance.BinanceApiRestClient;
import api.binance.domain.account.request.CancelOrderRequest;
import api.binance.domain.account.request.CancelOrderResponse;
import api.binance.util.BinanceUtils;
import api.req.MCancelOrderRequest;
import api.rsp.MCancelOrderRsp;
import java.util.concurrent.Callable;

public class BinanceCancelOrderTask implements Callable<MCancelOrderRsp> {

    private BinanceApiRestClient binanceApiRestClient;
    private MCancelOrderRequest mCancelOrderRequest;

    public BinanceCancelOrderTask(BinanceApiRestClient binanceApiRestClient,
        MCancelOrderRequest mCancelOrderRequest) {
        this.binanceApiRestClient = binanceApiRestClient;
        this.mCancelOrderRequest = mCancelOrderRequest;
    }

    @Override
    public MCancelOrderRsp call() throws Exception {

        String orderId = mCancelOrderRequest.getOrderId();

        String symbol = BinanceUtils.toSymbol(mCancelOrderRequest.getBaseCoin(), mCancelOrderRequest.getQuoteCoin());

        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(symbol, Long.parseLong(orderId));
        CancelOrderResponse cancelOrderResponse = binanceApiRestClient.cancelOrder(cancelOrderRequest);

        MCancelOrderRsp mCancelOrderRsp = new MCancelOrderRsp();
        mCancelOrderRsp.setOrderId(cancelOrderResponse.getOrderId());
        mCancelOrderRsp.setClientOrderId(cancelOrderResponse.getClientOrderId());
        mCancelOrderRsp.setOriClientOrderId(cancelOrderResponse.getOrigClientOrderId());
        mCancelOrderRsp.setSymbol(cancelOrderResponse.getSymbol());

        return mCancelOrderRsp;
    }
}
