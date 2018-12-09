package com.xiaohai.constant.enums;

public enum AllDealType {
	HuobiBuy(1, "huobi", "buyLimit", "buy-limit", "火币限价买入"),
	HuobiSell(2, "huobi", "sellLimit", "sell-limit", "火币限价卖出"),
	HuobiBuyMarket(3, "huobi", "buyMarket", "buy-market", "火币市价买入"),
	HuobiSellMarket(4, "huobi", "sellMarket", "sell-market", "火币市价卖出"),
	BitBuy(5, "bitz", "buyLimit", "1", "bitz限价买入"),
	BitSell(6, "bitz", "sellLimit", "2", "bitz限价卖出"),

	;
	
	private int index;
	private String platform;
	private String type;
	private String value;
	private String remark;
	
	AllDealType(int index, String platform, String type, String value, String remark) {
		this.index = index;
		this.platform = platform;
		this.type = type;
		this.value = value;
		this.remark = remark;
	}
	
	static AllDealType[] values = values();
	public static AllDealType getEnum(String platform, String type) {
		
		for (AllDealType e : values) {
			if (e.platform.equals(platform) && e.type.equals(type)) {
				return e;
			}
		}
		return null;
	}

	public int getIndex() {
		return index;
	}

	public String getPlatform() {
		return platform;
	}
	
	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public String getRemark() {
		return remark;
	}
}
