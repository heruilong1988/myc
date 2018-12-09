package com.xiaohai.jaxrs.vo.platform.huobi;

public class OrderInofHuobiTick {
	
	private long id;
	
	private long ts;
	
	//买盘,[price(成交价), amount(成交量)], 按price降序
	private double[][] bids;
	
	//卖盘,[price(成交价), amount(成交量)], 按price升序
	private double[][] asks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public double[][] getBids() {
		return bids;
	}

	public void setBids(double[][] bids) {
		this.bids = bids;
	}

	public double[][] getAsks() {
		return asks;
	}

	public void setAsks(double[][] asks) {
		this.asks = asks;
	}

}
