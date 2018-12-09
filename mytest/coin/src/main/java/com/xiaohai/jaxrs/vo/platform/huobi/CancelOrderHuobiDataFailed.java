/** 
* @version 
* @author xiaohai
* @date Aug 22, 2018 10:48:23 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.huobi;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * @version 取消订单结果失败数据
 * @author xiaohai
 * @date Aug 22, 2018 10:48:23 PM
 * 
 */
public class CancelOrderHuobiDataFailed {

	@JsonProperty("err-msg")
	private String errMsg;
	
	@JsonProperty("order-id")
	private String orderId;
	
	@JsonProperty("err-code")
	private String errCode;

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
}


