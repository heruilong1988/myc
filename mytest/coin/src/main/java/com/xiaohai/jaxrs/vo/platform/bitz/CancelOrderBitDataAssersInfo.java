/** 
* @version 
* @author xiaohai
* @date Aug 13, 2018 9:45:11 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.bitz;

/** 
 * @version 
 * @author xiaohai
 * @date Aug 13, 2018 9:45:11 PM
 * 
 */
public class CancelOrderBitDataAssersInfo {
	
	private String uid;
	
	private String bz_over;
	
	private String bz_lock;
	
	private String created;
	
	private String updated;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getBz_over() {
		return bz_over;
	}

	public void setBz_over(String bz_over) {
		this.bz_over = bz_over;
	}

	public String getBz_lock() {
		return bz_lock;
	}

	public void setBz_lock(String bz_lock) {
		this.bz_lock = bz_lock;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}
	
}


