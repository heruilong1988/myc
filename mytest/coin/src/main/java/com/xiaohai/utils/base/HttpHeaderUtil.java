/** 
* @version 
* @author xiaohai
* @date Aug 16, 2018 9:09:17 PM 
* 
*/ 
package com.xiaohai.utils.base;

import org.springframework.http.HttpHeaders;

/** 
 * @version 
 * @author xiaohai
 * @date Aug 16, 2018 9:09:17 PM
 * 
 */
public class HttpHeaderUtil {

	/**
	 * 获取bitz请求头部
	 * @return
	 */
	public static HttpHeaders getBitzHttpHeader() {
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/x-www-form-urlencoded");
		
		return header;
	}
	
}


