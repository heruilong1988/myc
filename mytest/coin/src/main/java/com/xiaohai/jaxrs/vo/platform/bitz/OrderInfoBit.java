package com.xiaohai.jaxrs.vo.platform.bitz;

import java.io.Serializable;

public class OrderInfoBit implements Serializable {
	
	private static final long serialVersionUID = -7375756816915322929L;

	private int status;
	
	private String msg;
	
	private long time;
	
	private String microtime;
	
	private String source;
	
	private OrderInfoBitData data;

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

	public OrderInfoBitData getData() {
		return data;
	}

	public void setData(OrderInfoBitData data) {
		this.data = data;
	}

}
