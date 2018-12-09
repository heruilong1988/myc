package com.xiaohai.jaxrs.vo.platform.huobi;

public class OrderInfoHuobi {
	private String status;
	
	private long ts;
	
	private OrderInofHuobiTick tick;
	
	private String ch;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public OrderInofHuobiTick getTick() {
		return tick;
	}

	public void setTick(OrderInofHuobiTick tick) {
		this.tick = tick;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}
	
}
