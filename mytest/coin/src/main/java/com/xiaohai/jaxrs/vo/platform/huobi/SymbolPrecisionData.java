/** 
 * @version 
 * @author xiaohai
 * @date Aug 23, 2018 12:41:01 AM 
 * 
 */
package com.xiaohai.jaxrs.vo.platform.huobi;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @version 交易对精度响应对象
 * @author xiaohai
 * @date Aug 23, 2018 12:41:01 AM
 * 
 */
public class SymbolPrecisionData implements Serializable {

	private static final long serialVersionUID = 731728983513665882L;

	@JsonProperty("base-currency")
	private String baseCurrency;

	@JsonProperty("quote-currency")
	private String quoteCurrency;

	@JsonProperty("price-precision")
	private int pricePrecision;

	@JsonProperty("amount-precision")
	private int amountPrecision;

	@JsonProperty("symbol-partition")
	private String symbolPartition;

	@JsonProperty("symbol")
	private String symbol;

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public int getPricePrecision() {
		return pricePrecision;
	}

	public void setPricePrecision(int pricePrecision) {
		this.pricePrecision = pricePrecision;
	}

	public int getAmountPrecision() {
		return amountPrecision;
	}

	public void setAmountPrecision(int amountPrecision) {
		this.amountPrecision = amountPrecision;
	}

	public String getSymbolPartition() {
		return symbolPartition;
	}

	public void setSymbolPartition(String symbolPartition) {
		this.symbolPartition = symbolPartition;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
