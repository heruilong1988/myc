package com.xiaohai.jaxrs.vo.base;

import java.util.Date;

import com.xiaohai.constant.enums.DealType;

public class MyOrderVO {

	// 交易对币名
	private String coinName;
	
	// 交易对基数币名
	private String baseCoinName;
	
	// 挂单金额
	private double price;
	
	// 挂单数量
	private double amount;
	
	// 剩余未交易数量
	private double remainAmount;

	// 交易对类型
	private DealType dealType;
	
	// 挂单时间
	private Date time;
	
	// 备注
	private String remark;

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public String getBaseCoinName() {
		return baseCoinName;
	}

	public void setBaseCoinName(String baseCoinName) {
		this.baseCoinName = baseCoinName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(double remainAmount) {
		this.remainAmount = remainAmount;
	}

	public DealType getDealType() {
		return dealType;
	}

	public void setDealType(DealType dealType) {
		this.dealType = dealType;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
