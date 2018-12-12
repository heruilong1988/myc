package api;

import api.req.MCancelOrderRequest;
import api.req.MGetBalanceRequest;
import api.req.MPlaceOrderRequest;
import api.req.MQueryOrderRequest;
import api.rsp.*;

import java.util.concurrent.FutureTask;

public interface MAsyncRestClient {


    FutureTask<MDepth> depth(String baseCoin, String quoteCoin);

    FutureTask<MPlaceOrderRsp> placeOrder(MPlaceOrderRequest request);

    FutureTask<MQueryOrderRsp> queryOrder(MQueryOrderRequest request);

    FutureTask<MCancelOrderRsp> cancelOrder(MCancelOrderRequest request);

    FutureTask<MGetAccountsRsp> getAccounts();

    FutureTask<MGetBalanceRsp> getBalance(MGetBalanceRequest mGetBalanceRequest);
}
