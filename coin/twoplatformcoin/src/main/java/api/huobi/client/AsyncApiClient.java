package api.huobi.client;

import api.MAsyncRestClient;
import api.huobi.response.*;
import api.huobi.task.*;
import api.req.MCancelOrderRequest;
import api.req.MGetBalanceRequest;
import api.req.MPlaceOrderRequest;
import api.req.MQueryOrderRequest;
import api.rsp.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class AsyncApiClient implements MAsyncRestClient {
    ExecutorService executorService = Executors.newFixedThreadPool(100);

    ApiClient apiClient;

    public final String accessKeyId;
    public final String accessKeySecret;
    public final String assetPassword;

    public AsyncApiClient(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = null;

        this.apiClient = new ApiClient(accessKeyId, accessKeySecret);
    }

    public AsyncApiClient(String accessKeyId, String accessKeySecret, String assetPassword) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = assetPassword;

        this.apiClient = new ApiClient(accessKeyId, accessKeySecret, assetPassword);
    }

    /**
     * 查询交易对
     *
     * @return List of symbols.
     */
    public FutureTask<List<Symbol>> getSymbols() {

        GetSymbolTask getSymbolTask = new GetSymbolTask(apiClient);
        FutureTask<List<Symbol>> futureTask = new FutureTask(getSymbolTask);
        executorService.execute(futureTask);
        return futureTask;
    }

    /**
     * GET /market/depth 获取 Market MDepth 数据
     *
     * "bids": 买盘,[price(成交价), amount(成交量)], 按price降序,
     * "asks": 卖盘,[price(成交价), amount(成交量)], 按price升序
     *
     * @return
     */
    public FutureTask<MDepth> depth(String baseCoin, String quoteCoin) {

        DepthTask depthTask = new DepthTask(apiClient, baseCoin, quoteCoin);
        FutureTask<MDepth> futureTask = new FutureTask<>(depthTask);
        executorService.execute(futureTask);

        return futureTask;
    }

    public FutureTask<MPlaceOrderRsp> placeOrder(MPlaceOrderRequest request) {
        PlaceOrderTask placeOrderTask = new PlaceOrderTask(apiClient, request);
        FutureTask<MPlaceOrderRsp> futureTask = new FutureTask<>(placeOrderTask);
        executorService.execute(futureTask);
        return futureTask;

    }

    /**
     * GET /v1/order/orders/{order-id} 查询某个订单详情
     *
     * @return
     */
    public FutureTask<MQueryOrderRsp> queryOrder(MQueryOrderRequest request) {
        QueryOrderTask queryOrderTask = new QueryOrderTask(apiClient, request);
        FutureTask<MQueryOrderRsp> futureTask = new FutureTask<>(queryOrderTask);
        executorService.execute(futureTask);
        return futureTask;
    }

    public FutureTask<MGetAccountsRsp> getAccounts(){
        GetAccountsTask getAccountsTask = new GetAccountsTask(apiClient);
        FutureTask<MGetAccountsRsp> futureTask = new FutureTask<>(getAccountsTask);
        executorService.execute(futureTask);
        return futureTask;
    }


    /**
     * POST /v1/order/orders/{order-id}/submitcancel 申请撤销一个订单请求
     *
     * @return
     */
    public FutureTask<MCancelOrderRsp> cancelOrder(MCancelOrderRequest mCancelOrderRequest) {

        CancelOrderTask cancelOrderTask = new CancelOrderTask(apiClient, mCancelOrderRequest);
        FutureTask<MCancelOrderRsp> futureTask = new FutureTask<>(cancelOrderTask);
        executorService.execute(futureTask);
        return futureTask;
    }


    public FutureTask<MGetBalanceRsp> getBalance(MGetBalanceRequest mGetBalanceRequest) {
        GetBalanceTask getBalanceTask = new GetBalanceTask(apiClient, mGetBalanceRequest);
        FutureTask<MGetBalanceRsp> futureTask = new FutureTask<MGetBalanceRsp>(getBalanceTask);
        executorService.execute(futureTask);
        return futureTask;
    }

        public static void main(String[] args) {

    }


    public void close() {
        executorService.shutdown();
    }
}
