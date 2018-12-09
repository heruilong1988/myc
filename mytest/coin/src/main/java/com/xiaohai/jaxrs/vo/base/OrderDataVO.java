package com.xiaohai.jaxrs.vo.base;

import java.io.Serializable;
import java.util.Date;

public class OrderDataVO implements Serializable {
	
	private Integer id;

    private Long timestamp;

    private String coinName;

    private String baseCoinName;

    private String platform;
    
    // 买单数据，double[price][amount]
    private double[][] buyData;
    
    // 卖单数据，double[price][amount]
    private double[][] sellData;
    
    private double actualBuyPrice;
    
    private double actualBuyAmount;
    
    private double actualSellPrice;
    
    private double actualSellAmount;

    private Date createTime;

    private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public double[][] getBuyData() {
		return buyData;
	}

	public void setBuyData(double[][] buyData) {
		this.buyData = buyData;
	}

	public double[][] getSellData() {
		return sellData;
	}

	public void setSellData(double[][] sellData) {
		this.sellData = sellData;
	}

	public double getActualBuyPrice() {
		return actualBuyPrice;
	}

	public void setActualBuyPrice(double actualBuyPrice) {
		this.actualBuyPrice = actualBuyPrice;
	}

	public double getActualBuyAmount() {
		return actualBuyAmount;
	}

	public void setActualBuyAmount(double actualBuyAmount) {
		this.actualBuyAmount = actualBuyAmount;
	}

	public double getActualSellPrice() {
		return actualSellPrice;
	}

	public void setActualSellPrice(double actualSellPrice) {
		this.actualSellPrice = actualSellPrice;
	}

	public double getActualSellAmount() {
		return actualSellAmount;
	}

	public void setActualSellAmount(double actualSellAmount) {
		this.actualSellAmount = actualSellAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}