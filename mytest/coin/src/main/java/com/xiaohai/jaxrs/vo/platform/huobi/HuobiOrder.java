package com.xiaohai.jaxrs.vo.platform.huobi;

import java.util.List;

public class HuobiOrder {
	
	private String status;
	
	private List<HuobiOrderData> data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<HuobiOrderData> getData() {
		return data;
	}

	public void setData(List<HuobiOrderData> data) {
		this.data = data;
	}
	
}
