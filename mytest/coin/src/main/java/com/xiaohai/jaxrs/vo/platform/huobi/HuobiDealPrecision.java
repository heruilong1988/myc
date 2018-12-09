/** 
 * @version 
 * @author xiaohai
 * @date Aug 23, 2018 9:08:53 PM 
 * 
 */
package com.xiaohai.jaxrs.vo.platform.huobi;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * @version
 * @author xiaohai
 * @date Aug 23, 2018 9:08:53 PM
 * 
 */
public class HuobiDealPrecision {
	
	private static HuobiDealPrecision instance = null;

	private HuobiDealPrecision() {

	}

	public static HuobiDealPrecision getInstance() {  
		if (instance == null) {  
	        synchronized (HuobiDealPrecision.class) {  
		        if (instance == null) {
		        	instance = new HuobiDealPrecision();
		        }
	        }
		}
		
		return instance;
	}

	private static Map<String, String> precision;

	public Map<String, String> getPrecision() {
		return precision;
	}

	public void setPrecision(Map<String, String> precision) {
		this.precision = precision;
	}

}
