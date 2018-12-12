package api.huobi.task;

import api.huobi.client.ApiClient;
import api.huobi.response.SubmitcancelResponse;
import api.req.MCancelOrderRequest;
import api.rsp.MCancelOrderRsp;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class CancelOrderTask implements Callable<MCancelOrderRsp> {

    Logger LOGGER = LoggerFactory.getLogger(CancelOrderTask.class);

    private ApiClient apiClient;
    private MCancelOrderRequest mCancelOrderRequest;

    public CancelOrderTask(ApiClient apiClient, MCancelOrderRequest mCancelOrderRequest) {
        this.apiClient = apiClient;
        this.mCancelOrderRequest = mCancelOrderRequest;
    }

    @Override
    public MCancelOrderRsp call() throws Exception {

        String orderId = mCancelOrderRequest.getOrderId();
        SubmitcancelResponse resp = apiClient.post("/v1/order/orders/" + orderId + "/submitcancel", null, new TypeReference<SubmitcancelResponse>() {
        });

        if(!"ok".equals(resp.getStatus())) {
            LOGGER.info("fail to cancelOrder. mCancelOrderRequest:{}", mCancelOrderRequest);
            throw new Exception(mCancelOrderRequest.toString());
        }

        String returnedOrderId = (String) resp.getData();

        MCancelOrderRsp mCancelOrderRsp= new MCancelOrderRsp();
        mCancelOrderRsp.setOrderId(returnedOrderId);

        return mCancelOrderRsp;
    }
}
