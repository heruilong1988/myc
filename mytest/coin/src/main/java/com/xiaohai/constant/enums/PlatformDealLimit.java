package com.xiaohai.constant.enums;

/**
 * 
 * @described 平台交易最大最小值限制
 * @author qiliang.chen
 * @createDate 2018年2月28日
 *
 */
public enum PlatformDealLimit {
	
	PoloUsdt("polo", "usdt", 30, 300, "poloniex"),
	BitzUsdt("bitz", "usdt", 60, 100, "bit-z"),
	BithumbUsdt("bithumb", "usdt", 30, 300, "bithumb"),
	OkexUsdt("okex", "usdt", 30, 300, "okex"),
	BitfinexUsdt("bitfinex", "usdt", 30, 300, "bitfinex"),
	HitbtcUsdt("hitbtc", "usdt", 30, 300, "hitbtc"),
	BinanceUsdt("binance", "usdt", 30, 300, "binance"),
	HuobiUsdt("huobi", "usdt", 5, 20, "huobi"),

	PoloBtc("polo", "btc", 0.003, 0.03, "poloniex"),
	BitzBtc("bitz", "btc", 0.009, 0.02, "bit-z"),
	BithumbBtc("bithumb", "btc", 0.003, 0.03, "bithumb"),
	OkexBtc("okex", "btc", 0.003, 0.03, "okex"),
	BitfinexBtc("bitfinex", "btc", 0.003, 0.03, "bitfinex"),
	HitbtcBtc("hitbtc", "btc", 0.003, 0.03, "hitbtc"),
	BinanceBtc("binance", "btc", 0.003, 0.03, "binance"),
	HuobiBtc("huobi", "btc", 0.0006, 0.0025, "huobi"),
	
	PoloEth("polo", "eth", 0.04, 0.4, "poloniex"),
	BitzEth("bitz", "eth", 0.11, 0.4, "bit-z"),
	BithumbEth("bithumb", "eth", 0.04, 0.4, "bithumb"),
	OkexEth("okex", "eth", 0.04, 0.4, "okex"),
	BitfinexEth("bitfinex", "eth", 0.04, 0.4, "bitfinex"),
	HitbtcEth("hitbtc", "eth", 0.04, 0.4, "hitbtc"),
	BinanceEth("binance", "eth", 0.04, 0.4, "binance"),
	HuobiEth("huobi", "eth", 0.01, 0.037, "huobi"),
	
	;
	
	private String platform;
	private String baseCoinName;
	private double minLimit;
	private double maxLimit;
	private String remark;
	
	PlatformDealLimit(String platform, String baseCoinName, double minLimit, double maxLimit, String remark) {
		this.platform = platform;
		this.baseCoinName = baseCoinName;
		this.minLimit = minLimit;
		this.maxLimit = maxLimit;
		this.remark = remark;
	}
	
	public static PlatformDealLimit getPlatformDealLimit(String platform, String baseCoinName) {
		PlatformDealLimit[] values = values();
		
		for (PlatformDealLimit limit : values) {
			if (limit.platform.equals(platform) && limit.baseCoinName.equals(baseCoinName)) {
				return limit;
			}
		}
		return null;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getBaseCoinName() {
		return baseCoinName;
	}

	public void setBaseCoinName(String baseCoinName) {
		this.baseCoinName = baseCoinName;
	}

	public double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(double minLimit) {
		this.minLimit = minLimit;
	}

	public double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(double maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
