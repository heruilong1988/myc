/*
 * Copyright (c) 2018, TP-Link Co.,Ltd.
 * Author: heruilong <heruilong@tp-link.com.cn>
 * Created: 2018/12/11
 */

package api.binance.task;

import api.binance.BinanceApiRestClient;
import api.binance.domain.market.OrderBook;
import api.binance.domain.market.OrderBookEntry;
import api.binance.util.BinanceUtils;
import api.rsp.MDepth;
import api.rsp.PriceQtyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BinanceDepthTask implements Callable<MDepth> {

    private BinanceApiRestClient binanceApiRestClient;
    private String symbol;

    public BinanceDepthTask(BinanceApiRestClient binanceApiRestClient, String baseCoin, String quoteCoin) {
        this.binanceApiRestClient = binanceApiRestClient;
        this.symbol = BinanceUtils.toSymbol(baseCoin, quoteCoin);
    }

    @Override
    public MDepth call() throws Exception {
        Integer limit = 100;
        OrderBook orderBook = binanceApiRestClient.getOrderBook(symbol,limit);
        //List of bids (price/qty).
        List<OrderBookEntry> bids = orderBook.getBids();
        List<OrderBookEntry> asks = orderBook.getAsks();

        List<PriceQtyPair> bidsList = new ArrayList<>();
        List<PriceQtyPair> askList  = new ArrayList<>();

        for(OrderBookEntry bid : bids) {

            PriceQtyPair priceQtyPair = new PriceQtyPair();
            priceQtyPair.setPrice(Double.parseDouble(bid.getPrice()));
            priceQtyPair.setQty(Double.parseDouble(bid.getQty()));

            bidsList.add(priceQtyPair);
        }

        for(OrderBookEntry ask : asks) {
            PriceQtyPair priceQtyPair = new PriceQtyPair();
            priceQtyPair.setPrice(Double.parseDouble(ask.getPrice()));
            priceQtyPair.setQty(Double.parseDouble(ask.getQty()));

            askList.add(priceQtyPair);
        }

        MDepth mDepth = new MDepth(bidsList, askList);
        return mDepth;
    }
}
