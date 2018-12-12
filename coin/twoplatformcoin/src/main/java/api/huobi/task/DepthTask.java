package api.huobi.task;

import api.huobi.client.ApiClient;
import api.huobi.response.Depth;
import api.huobi.response.DepthResponse;
import api.huobi.util.Utils;
import api.rsp.MDepth;
import api.rsp.PriceQtyPair;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 */
public class DepthTask implements Callable<MDepth> {

    Logger LOGGER = LoggerFactory.getLogger(DepthTask.class);

    ApiClient apiClient;
    String symbol;

    public DepthTask(ApiClient apiClient, String baseCoin, String quoteCoin) {
        this.apiClient = apiClient;
        this.symbol = Utils.toSymbol(baseCoin, quoteCoin);
    }

    @Override
    public MDepth call() throws Exception {

        HashMap map = new HashMap();
        map.put("symbol", this.symbol);
        map.put("type", "step0");

        DepthResponse resp = apiClient.get("/market/depth", map, new TypeReference<DepthResponse<List<Depth>>>() {
        });

        if(!"ok".equals(resp.getStatus())) {
            LOGGER.info("depth request error.errCode:{},errMsg:{}", resp.getStatus(), resp.getErrMsg());
            throw new Exception("depth request error.errMsg:"+ resp.getErrMsg());
        }

        Depth depth = resp.getTick();

        MDepth mDepth = toMDepth(depth);

        return mDepth;
    }

    private MDepth toMDepth(Depth depth) {

        List<PriceQtyPair> bidsList = new ArrayList<>();
        List<PriceQtyPair> askList  = new ArrayList<>();

        List<List<BigDecimal>> bids = depth.getBids();

        List<List<BigDecimal>> asks = depth.getAsks();

        for(List<BigDecimal> bid : bids) {
            if(bid.size() == 2) {
                bidsList.add(new PriceQtyPair(bid.get(0).doubleValue(), bid.get(1).doubleValue()));
            }
        }

        for(List<BigDecimal> ask : asks) {
            if(ask.size() == 2) {
                askList.add(new PriceQtyPair(ask.get(0).doubleValue(), ask.get(1).doubleValue()));
            }
        }

        MDepth mDepth = new MDepth(bidsList, askList);

        return mDepth;

    }
}
