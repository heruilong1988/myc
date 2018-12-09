package com.xiaohai.jaxrs.pojo.stratefgy;

/**
 * 三角套利交易对对象
 * @version 
 * @author xiaohai
 * @date Sep 2, 2018 5:29:30 PM
 *
 */
public class DealCoinPair {
	private String coinName1;
	private String coinName2;
	private double rio;
	private int dealSleepMs;

	public String getCoinName1() {
		return coinName1;
	}

	public void setCoinName1(String coinName1) {
		this.coinName1 = coinName1;
	}

	public String getCoinName2() {
		return coinName2;
	}

	public void setCoinName2(String coinName2) {
		this.coinName2 = coinName2;
	}

	public double getRio() {
		return rio;
	}

	public void setRio(double rio) {
		this.rio = rio;
	}

	public int getDealSleepMs() {
		return dealSleepMs;
	}

	public void setDealSleepMs(int dealSleepMs) {
		this.dealSleepMs = dealSleepMs;
	}
	
}
