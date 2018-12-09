/** 
 * @version 
 * @author xiaohai
 * @date Aug 23, 2018 12:40:51 AM 
 * 
 */
package com.xiaohai.jaxrs.vo.platform.huobi;

import java.util.List;

/**
 * @version 获取交易对下单精度响应对象
 * @author xiaohai
 * @date Aug 23, 2018 12:40:51 AM
 * 
 */
public class SymbolPrecision {

	private String status;

	private List<SymbolPrecisionData> data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SymbolPrecisionData> getData() {
		return data;
	}

	public void setData(List<SymbolPrecisionData> data) {
		this.data = data;
	}

}
