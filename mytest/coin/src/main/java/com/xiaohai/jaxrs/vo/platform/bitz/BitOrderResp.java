package com.xiaohai.jaxrs.vo.platform.bitz;

public class BitOrderResp {

	private int code; // status code
	private String msg; // message
	private BitOrderRespData data; // order_id

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public BitOrderRespData getData() {
		return data;
	}

	public void setData(BitOrderRespData data) {
		this.data = data;
	}

}
