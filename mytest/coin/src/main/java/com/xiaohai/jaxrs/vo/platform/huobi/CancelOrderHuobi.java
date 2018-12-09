/** 
* @version 
* @author xiaohai
* @date Aug 22, 2018 10:48:23 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.huobi;

/** 
 * @version 取消订单响应对象
 * @author xiaohai
 * @date Aug 22, 2018 10:48:23 PM
 * 
 */
public class CancelOrderHuobi {

	private String status;
	
	private CancelOrderHuobiData data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CancelOrderHuobiData getData() {
		return data;
	}

	public void setData(CancelOrderHuobiData data) {
		this.data = data;
	}
	
}


