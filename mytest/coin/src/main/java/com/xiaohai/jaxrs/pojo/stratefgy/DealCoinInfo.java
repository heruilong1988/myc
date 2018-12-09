package com.xiaohai.jaxrs.pojo.stratefgy;

import java.util.List;

/**
 * 三角套利交易对信息对象
 * @version 
 * @author xiaohai
 * @date Sep 2, 2018 5:29:12 PM
 *
 */
public class DealCoinInfo {
	private String platformName;

	private List<DealCoinPair> dealCoinPairs;

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public List<DealCoinPair> getDealCoinPairs() {
		return dealCoinPairs;
	}

	public void setDealCoinPairs(List<DealCoinPair> dealCoinPairs) {
		this.dealCoinPairs = dealCoinPairs;
	}
	
}

