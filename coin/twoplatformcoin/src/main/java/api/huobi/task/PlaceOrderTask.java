package api.huobi.task;

import api.huobi.client.ApiClient;
import api.huobi.request.CreateOrderRequest;
import api.huobi.response.ApiResponse;
import api.huobi.util.Utils;
import api.req.MPlaceOrderRequest;
import api.rsp.MPlaceOrderRsp;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.Callable;

public class PlaceOrderTask implements Callable<MPlaceOrderRsp> {

    private ApiClient apiClient;
    private MPlaceOrderRequest mPlaceOrderRequest;

    public PlaceOrderTask(ApiClient apiClient, MPlaceOrderRequest mPlaceOrderRequest) {
        this.apiClient = apiClient;
        this.mPlaceOrderRequest = mPlaceOrderRequest;
    }

    @Override
    public MPlaceOrderRsp call() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest();
        request.accountId = mPlaceOrderRequest.getAccountId();
        request.amount = String.valueOf(mPlaceOrderRequest.getQuantity());
        request.price = String.valueOf(mPlaceOrderRequest.getPrice());
        request.source = "api";
        request.type = Utils.toOrderType(mPlaceOrderRequest.getSide(), mPlaceOrderRequest.getType());
        request.symbol = Utils.toSymbol(mPlaceOrderRequest.getBaseCoin(), mPlaceOrderRequest.getQuoteCoin());

        ApiResponse<Long> resp =
                apiClient.post("/v1/order/orders/place", request, new TypeReference<ApiResponse<Long>>() {
                });
        Long orderId = resp.checkAndReturn();

        MPlaceOrderRsp mPlaceOrderRsp = new MPlaceOrderRsp(String.valueOf(orderId));
        return mPlaceOrderRsp;
    }
}
