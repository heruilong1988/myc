package com.xiaohai.utils.base;

import java.util.HashMap;
import java.util.Map;

public class StrUtil {

	/**
	 * http get 请求的参数转换为map形式
	 * @param params
	 * @return
	 */
	public static Map<String, String> getParamsToMap(String params) {
		Map<String, String> map = new HashMap<String, String>();
		
		String[] paramPair = params.split("&");
		
		String[] paramArr;
		for (String param : paramPair) {
			paramArr = param.split("=");
			if (paramArr == null || paramArr.length != 2) {
				continue;
			}
			
			map.put(paramArr[0], paramArr[1]);
		}
		
		return map;
	}
}
