/** 
 * @version 
 * @author xiaohai
 * @date Aug 16, 2018 9:32:39 PM 
 * 
 */
package com.xiaohai.jaxrs.vo.platform.bitz;

/**
 * @version
 * @author xiaohai
 * @date Aug 16, 2018 9:32:39 PM
 * 
 */
public class OpenOrderBitDataData {
	private String id;

	private String uid;

	private String price;

	private String number;

	private String total;

	private String numberOver;

	private String numberDeal;

	private String flag;

	// 0:未成交, 1:部分成交, 2:全部成交, 3:已经撤销
	private String status;

	private String coinForm;

	private String coinTo;

	private String created;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getNumberOver() {
		return numberOver;
	}

	public void setNumberOver(String numberOver) {
		this.numberOver = numberOver;
	}

	public String getNumberDeal() {
		return numberDeal;
	}

	public void setNumberDeal(String numberDeal) {
		this.numberDeal = numberDeal;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCoinForm() {
		return coinForm;
	}

	public void setCoinForm(String coinForm) {
		this.coinForm = coinForm;
	}

	public String getCoinTo() {
		return coinTo;
	}

	public void setCoinTo(String coinTo) {
		this.coinTo = coinTo;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}
