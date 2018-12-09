package com.xiaohai.jaxrs.vo.stratefgy;

import com.xiaohai.jaxrs.vo.base.OrderDataVO;

public class CircleRioOrderInfo {

	private String platformName;
	private double rio;
	private int direct;
	private OrderDataVO orderData1;
	private OrderDataVO orderData2;
	private OrderDataVO orderData3;
	
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public double getRio() {
		return rio;
	}

	public void setRio(double rio) {
		this.rio = rio;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}
	
	public OrderDataVO getOrderData1() {
		return orderData1;
	}

	public void setOrderData1(OrderDataVO orderData1) {
		this.orderData1 = orderData1;
	}

	public OrderDataVO getOrderData2() {
		return orderData2;
	}

	public void setOrderData2(OrderDataVO orderData2) {
		this.orderData2 = orderData2;
	}

	public OrderDataVO getOrderData3() {
		return orderData3;
	}

	public void setOrderData3(OrderDataVO orderData3) {
		this.orderData3 = orderData3;
	}

	public CircleRioOrderInfo () {
		
	}
	
	public CircleRioOrderInfo (String platformName, double rio, int direct, OrderDataVO orderData1, OrderDataVO orderData2, OrderDataVO orderData3) {
		this.platformName = platformName;
		this.rio = rio;
		this.direct = direct;
		this.orderData1 = orderData1;
		this.orderData2 = orderData2;
		this.orderData3 = orderData3;
	}

}
