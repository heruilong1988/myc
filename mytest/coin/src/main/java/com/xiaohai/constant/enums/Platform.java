package com.xiaohai.constant.enums;

/**
 * 
 * @version 平台枚举 
 * @author xiaohai
 * @date Jul 28, 2018 12:51:00 PM
 *
 */
public enum Platform {

	Polo("polo", "poloniex"),
	Bitz("bitz", "bit-z"),
	Bithumb("bithumb", "bithumb"),
	Okex("okex", "okex"),
	Bitfinex("bitfinex", "bitfinex"),
	Hitbtc("hitbtc", "hitbtc"),
	Binance("binance", "binance"),
	Huobi("huobi", "huobi"),
	
	;
	
	private String platform;
	private String remark;
	
	Platform(String platform, String remark) {
		this.platform = platform;
		this.remark = remark;
	}
	
	public static Platform getPlatform(String platform) {
		Platform[] values = values();
		
		for (Platform e : values) {
			if (e.platform.equals(platform)) {
				return e;
			}
		}
		return null;
	}

	public String getPlatform() {
		return platform;
	}

	public String getRemark() {
		return remark;
	}
	
}
