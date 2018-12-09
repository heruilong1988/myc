package com.xiaohai.constant;

public class CacheKey {

	/**
	 * 用于新增ADMIN用户，redis防并发处理
	 */
	public static final String ADMIN_ADD_PREFIX = "ConcurrentAdminAdd-";
	
}
