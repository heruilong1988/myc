package com.xiaohai.constant.enums;

/**
 * 交易类型
 * @version 
 * @author xiaohai
 * @date Jul 28, 2018 3:30:46 PM
 *
 */
public enum DealType {
	Buy(1, "buy", "买入"),
	Sell(2, "sell", "卖出"),

	;
	
	private int index;
	private String value;
	private String remark;
	
	DealType(int index, String value, String remark) {
		this.index = index;
		this.value = value;
		this.remark = remark;
	}
	
	public static DealType getEnum(String value) {
		DealType[] values = values();
		
		for (DealType e : values) {
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
