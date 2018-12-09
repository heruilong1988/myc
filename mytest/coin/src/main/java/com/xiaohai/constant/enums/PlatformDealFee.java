package com.xiaohai.constant.enums;

public enum PlatformDealFee {

	Polo("polo", 0.9975, 0.9975, "poloniex"),
	Bitz("bitz", 0.999, 0.999, "bit-z"),
	Bithumb("bithumb", 0.998, 0.998, "bithumb"),
	Okex("okex", 0.998, 0.998, "okex"),
	Bitfinex("bitfinex", 0.998, 0.998, "bitfinex"),
	Hitbtc("hitbtc", 0.999, 0.999, "hitbtc"),
	Binance("binance", 0.999, 0.999, "binance"),
	Huobi("huobi", 0.998, 0.998, "huobi"),
	
	;
	
	private String platform;
	private double sellFee;
	private double buyFee;
	private String remark;
	
	PlatformDealFee(String platform, double sellFee, double buyFee, String remark) {
		this.platform = platform;
		this.sellFee = sellFee;
		this.buyFee = buyFee;
		this.remark = remark;
	}
	
	public static PlatformDealFee getPlatformDealFee(String platform) {
		PlatformDealFee[] values = values();
		
		for (PlatformDealFee e : values) {
			if (e.platform.equals(platform)) {
				return e;
			}
		}
		return null;
	}

	public String getPlatform() {
		return platform;
	}
	
	public double getSellFee() {
		return sellFee;
	}
	
	public double getBuyFee() {
		return buyFee;
	}

	public String getRemark() {
		return remark;
	}
}
