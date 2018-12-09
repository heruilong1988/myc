package com.xiaohai.jaxrs.vo.platform.bitz;

public class OrderInfoBitData {
	
	// 交易对
	private String coinPair;
	
	//[卖盘[price, amount, totalPrice]]
	private String[][] asks;
	
	//[买盘[price, amount, totalPrice]]
	private String[][] bids;

	public String getCoinPair() {
		return coinPair;
	}

	public void setCoinPair(String coinPair) {
		this.coinPair = coinPair;
	}

	public String[][] getAsks() {
		return asks;
	}

	public void setAsks(String[][] asks) {
		this.asks = asks;
	}

	public String[][] getBids() {
		return bids;
	}

	public void setBids(String[][] bids) {
		this.bids = bids;
	}

}
