/** 
* @version 
* @author xiaohai
* @date Jul 28, 2018 9:42:54 PM 
* 
*/ 
package com.xiaohai.jaxrs.pojo.stratefgy;

import java.util.List;

/**
 * 三角套利请求对象
 * @version 
 * @author xiaohai
 * @date Sep 2, 2018 5:28:41 PM
 *
 */
public class TriangleReq {
	private int count;
	
	private int orderNumMax;
	
	private int sleepMs;
	
	private int dealSleepMs;

	private List<DealCoinInfo> dealCoinInfos;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOrderNumMax() {
		return orderNumMax;
	}

	public void setOrderNumMax(int orderNumMax) {
		this.orderNumMax = orderNumMax;
	}

	public int getSleepMs() {
		return sleepMs;
	}

	public void setSleepMs(int sleepMs) {
		this.sleepMs = sleepMs;
	}

	public int getDealSleepMs() {
		return dealSleepMs;
	}

	public void setDealSleepMs(int dealSleepMs) {
		this.dealSleepMs = dealSleepMs;
	}

	public List<DealCoinInfo> getDealCoinInfos() {
		return dealCoinInfos;
	}

	public void setDealCoinInfos(List<DealCoinInfo> dealCoinInfos) {
		this.dealCoinInfos = dealCoinInfos;
	}

}


