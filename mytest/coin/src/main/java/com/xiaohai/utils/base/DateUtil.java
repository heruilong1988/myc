package com.xiaohai.utils.base;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	/**
	 * 获取当前的UTC时间
	 * @return
	 */
	public static String getUTCTimeStr() {
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		try {

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
			return URLEncoder.encode(format.format(cal.getTime()).replace(" ", "T"), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
