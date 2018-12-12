package api.huobi.task;

import api.huobi.client.ApiClient;
import api.huobi.response.OrdersDetailResponse;
import api.huobi.util.Utils;
import api.req.MQueryOrderRequest;
import api.rsp.MOrderSide;
import api.rsp.MOrderType;
import api.rsp.MQueryOrderRsp;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;
import java.util.concurrent.Callable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryOrderTask implements Callable<MQueryOrderRsp> {

    Logger LOGGER = LoggerFactory.getLogger(QueryOrderTask.class);

    static String accountId = "account-id";
    static String amount = "amount";
    static String canceledAt = "canceled-at";
    static String createdAt = "created-at";
    static String fieldAmount = "field-amount";
    static String fieldCashAmount = "field-cash-amount";
    static String fieldFees = "field-fees";
    static String finishedAt = "finished-at";
    static String id = "id";
    static String price = "price";
    static String source = "source";
    static String state = "state";
    static String symbol = "symbol";
    static String type = "type";

    private ApiClient apiClient;
    private MQueryOrderRequest mQueryOrderRequest;

    public QueryOrderTask(ApiClient apiClient, MQueryOrderRequest mQueryOrderRequest) {
        this.apiClient = apiClient;
        this.mQueryOrderRequest = mQueryOrderRequest;
    }

    @Override
    public MQueryOrderRsp call() throws Exception {
        String orderId = mQueryOrderRequest.getOrderId();

        OrdersDetailResponse resp = apiClient.get("/v1/order/orders/" + orderId, null, new TypeReference<OrdersDetailResponse>() {
        });

        if(!"ok".equals(resp.getStatus())) {
            LOGGER.info("queryOrderRequest failed. rsp:{}", resp);
            throw new Exception(resp.toString());
        }

//        JSONObject json = new JSONObject();
        Map<String,Object> orderMap = (Map) resp.getData();
        //json  = (JSONObject) resp.getData();

        MQueryOrderRsp mQueryOrderRsp = new MQueryOrderRsp();
        mQueryOrderRsp.setAccountId(String.valueOf(orderMap.get(accountId).toString()));
        mQueryOrderRsp.setQuantity(orderMap.get(amount).toString());
        mQueryOrderRsp.setCanceledAtTimestamp(Long.parseLong(orderMap.get(canceledAt).toString()));
        mQueryOrderRsp.setCreatedAtTimestamp(Long.parseLong(orderMap.get(createdAt).toString()));
        mQueryOrderRsp.setFieldQuantity(orderMap.get(fieldAmount).toString());
        mQueryOrderRsp.setFieldCashQuantity(orderMap.get(fieldCashAmount).toString());
        mQueryOrderRsp.setFieldFees(orderMap.get(fieldFees).toString());
        mQueryOrderRsp.setFinishedAtTimestamp(Long.parseLong(orderMap.get(finishedAt).toString()));
        mQueryOrderRsp.setOrderId(String.valueOf(orderMap.get(orderId)));
        mQueryOrderRsp.setPrice(orderMap.get(price).toString());
        mQueryOrderRsp.setSource(orderMap.get(source).toString());
        mQueryOrderRsp.setSymbol(orderMap.get(symbol).toString());

        String status = orderMap.get(state).toString();
        mQueryOrderRsp.setState(Utils.toStatus(status));

        String huoBiSideType  = orderMap.get(type).toString();
        String[] sideType = huoBiSideType.split("-");
        mQueryOrderRsp.setSide(sideType[0].equalsIgnoreCase("buy") ? MOrderSide.buy : MOrderSide.sell);
        mQueryOrderRsp.setType(sideType[1].equalsIgnoreCase("limit") ? MOrderType.limit : MOrderType.market);

        return mQueryOrderRsp;
    }





}
