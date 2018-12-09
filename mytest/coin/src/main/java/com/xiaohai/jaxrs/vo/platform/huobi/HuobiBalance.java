package com.xiaohai.jaxrs.vo.platform.huobi;

public class HuobiBalance {

	private String status;
	
	private HuobiBalanceData data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HuobiBalanceData getData() {
		return data;
	}

	public void setData(HuobiBalanceData data) {
		this.data = data;
	}
	
}
