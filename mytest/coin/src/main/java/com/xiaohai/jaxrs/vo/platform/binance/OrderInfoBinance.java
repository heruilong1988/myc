/** 
 * @version 
 * @author xiaohai
 * @date Aug 25, 2018 9:06:59 PM 
 * 
 */
package com.xiaohai.jaxrs.vo.platform.binance;

/**
 * @version 获取binance行情信息响应对象
 * @author xiaohai
 * @date Aug 25, 2018 9:06:59 PM
 * 
 */
public class OrderInfoBinance {

	private int lastUpdateId;
	private Object[][] asks;
	private Object[][] bids;

	public int getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(int lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	public Object[][] getAsks() {
		return asks;
	}

	public void setAsks(Object[][] asks) {
		this.asks = asks;
	}

	public Object[][] getBids() {
		return bids;
	}

	public void setBids(Object[][] bids) {
		this.bids = bids;
	}

}
