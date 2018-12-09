package com.xiaohai.constant.enums;

/**
 * 下单类型
 * @version 
 * @author xiaohai
 * @date Jul 28, 2018 3:49:43 PM
 *
 */
public enum OrderDealType {

	Buy(1, "buyLimit", "限价买入"),
	Sell(2, "sellLimit", "限价卖出"),
	BuyMarket(3, "buyMarket", "市价买入"),
	SellMarket(4, "sellMarket", "市价卖出"),

	;
	
	private int index;
	private String value;
	private String remark;
	
	OrderDealType(int index, String value, String remark) {
		this.index = index;
		this.value = value;
		this.remark = remark;
	}
	
	static OrderDealType[] values = values();
	public static OrderDealType getEnum(String value) {
		
		for (OrderDealType e : values) {
			if (e.value.equals(value)) {
				return e;
			}
		}
		return null;
	}

	public int getIndex() {
		return index;
	}

	public String getValue() {
		return value;
	}

	public String getRemark() {
		return remark;
	}
}
