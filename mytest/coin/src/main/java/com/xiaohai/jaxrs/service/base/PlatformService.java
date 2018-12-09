package com.xiaohai.jaxrs.service.base;

import java.util.List;
import java.util.Map;

import com.xiaohai.constant.enums.OrderDealType;
import com.xiaohai.jaxrs.vo.base.CancelOrderVO;
import com.xiaohai.jaxrs.vo.base.MyOrderVO;
import com.xiaohai.jaxrs.vo.base.OrderDataVO;

public abstract class PlatformService {

	/**
	 * 根据交易对获取当前交易对的平台挂单信息
	 * @param coinName
	 * @param baseCoinName
	 * @return
	 */
	public abstract OrderDataVO getCoinInfoOrderData(String coinName, String baseCoinName);
	
	/**
	 * 根据交易对币名获取是否有订单信息(orderIds为空则查询所有订单)
	 * @param coinName
	 * @param baseCoinName
	 * @param orderIds
	 * @return
	 */
	public abstract boolean getOrderIsExsit(String coinName, String baseCoinName, List<String> orderIds);

	/**
	 * 根据币名获取是否有订单信息(orderIds为空则查询所有订单)
	 * @param coinName
	 * @param baseCoinName
	 * @param orderIds
	 * @return
	 */
	public abstract List<MyOrderVO> getOrders(String coinName, String baseCoinName, List<String> orderIds);
	
	/**
	 * 根据币名获取是否有订单信息
	 * @param coinName
	 * @return
	 */
	public abstract List<MyOrderVO> getOrders(List<String> orderIds);
	
	/**
	 * 根据币获取余额
	 * @param coinName
	 * @return
	 */
	public abstract Map<String, Double> getBalance(List<String> coinNames);

	/**
	 * 根据订单号列表取消订单
	 * @param orderNum
	 * @return
	 */
	public abstract boolean cancelOrder(List<CancelOrderVO> cancelOrderVOList);
	
	/**
	 * 下单（交易对的价格和数量，实现现价和市价）
	 * @param allDealType
	 * @param coinName
	 * @param baseCoinName
	 * @param price
	 * @param amount
	 * @param needLimitAmount
	 * @return
	 */
	public abstract String order(OrderDealType orderDealType, String coinName, String baseCoinName, double price, double amount, boolean needLimitAmount);

	/**
	 * 获取平台名称
	 * @return
	 */
	public abstract String getServiceName();

}
