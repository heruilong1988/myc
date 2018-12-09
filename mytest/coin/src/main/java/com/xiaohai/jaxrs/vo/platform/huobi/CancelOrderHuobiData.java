/** 
* @version 
* @author xiaohai
* @date Aug 22, 2018 10:48:23 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.huobi;

import java.util.List;

/** 
 * @version 取消订单结果数据
 * @author xiaohai
 * @date Aug 22, 2018 10:48:23 PM
 * 
 */
public class CancelOrderHuobiData {

	private List<String> success;
	
	private List<CancelOrderHuobiDataFailed> failed;

	public List<String> getSuccess() {
		return success;
	}

	public void setSuccess(List<String> success) {
		this.success = success;
	}

	public List<CancelOrderHuobiDataFailed> getFailed() {
		return failed;
	}

	public void setFailed(List<CancelOrderHuobiDataFailed> failed) {
		this.failed = failed;
	}
	
}


