package com.xiaohai.jaxrs.vo.platform.huobi;

public class HuobiBalanceList {

	private String balance;
	private String currency;
	//trade: 交易余额，frozen: 冻结余额
	private String type;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
