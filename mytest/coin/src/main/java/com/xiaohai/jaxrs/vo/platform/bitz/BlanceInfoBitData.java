/** 
* @version 
* @author xiaohai
* @date Aug 13, 2018 10:01:59 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.bitz;

import java.util.List;

/** 
 * @version 
 * @author xiaohai
 * @date Aug 13, 2018 10:01:59 PM
 * 
 */
public class BlanceInfoBitData {

	private String cny;
	
	private String usd;
	
	private String btc_total;
	
	private List<BlanceInfoBitDataInfo> info;

	public String getCny() {
		return cny;
	}

	public void setCny(String cny) {
		this.cny = cny;
	}

	public String getUsd() {
		return usd;
	}

	public void setUsd(String usd) {
		this.usd = usd;
	}

	public String getBtc_total() {
		return btc_total;
	}

	public void setBtc_total(String btc_total) {
		this.btc_total = btc_total;
	}

	public List<BlanceInfoBitDataInfo> getInfo() {
		return info;
	}

	public void setInfo(List<BlanceInfoBitDataInfo> info) {
		this.info = info;
	}
	
}


