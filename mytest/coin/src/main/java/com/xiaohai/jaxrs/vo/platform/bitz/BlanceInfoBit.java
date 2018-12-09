/** 
* @version 
* @author xiaohai
* @date Aug 13, 2018 9:59:44 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.bitz;

/** 
 * @version 
 * @author xiaohai
 * @date Aug 13, 2018 9:59:44 PM
 * 
 */
public class BlanceInfoBit {

	private int status;
	
	private String msg;
	
	private long time;
	
	private String microtime;
	
	private String source;
	
	private BlanceInfoBitData data;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMicrotime() {
		return microtime;
	}

	public void setMicrotime(String microtime) {
		this.microtime = microtime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public BlanceInfoBitData getData() {
		return data;
	}

	public void setData(BlanceInfoBitData data) {
		this.data = data;
	}
	
}