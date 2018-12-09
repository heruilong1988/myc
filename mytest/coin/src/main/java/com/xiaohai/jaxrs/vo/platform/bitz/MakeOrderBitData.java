/** 
* @version 
* @author xiaohai
* @date Aug 13, 2018 10:38:49 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.bitz;

/** 
 * @version 
 * @author xiaohai
 * @date Aug 13, 2018 10:38:49 PM
 * 
 */
public class MakeOrderBitData {
	
	/** 委托单id */
	private int id;
	
	/** 用户ID */
	private String uId;
	
	private String price;
	
	private String number;
	
	/** 剩余数量 */
	private String numberover;
	
	/** 交易类型 */
	private String flag;
	
	/** 状态未成交 */
	private int status;
	
	private String coinForm;
	
	private String coinTo;
	
	/** 交易成功数量 */
	private String numberDeal;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
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

	public String getNumberover() {
		return numberover;
	}

	public void setNumberover(String numberover) {
		this.numberover = numberover;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public String getNumberDeal() {
		return numberDeal;
	}

	public void setNumberDeal(String numberDeal) {
		this.numberDeal = numberDeal;
	}
	
}


