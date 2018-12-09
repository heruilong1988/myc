package com.xiaohai.jaxrs.vo.platform.huobi;

import java.util.List;

public class HuobiBalanceData {

	// 账户 ID	
	private long id;
	
	// 账户状态	working：正常 lock：账户被锁定
	private String state;
	
	// 账户类型	spot：现货账户
	private String type;
	
	private List<HuobiBalanceList> list;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<HuobiBalanceList> getList() {
		return list;
	}

	public void setList(List<HuobiBalanceList> list) {
		this.list = list;
	}
	
}
