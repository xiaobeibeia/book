package com.tsbg.mis.util.constants;

/**
 * 通用常量类, 单个业务的常量请单开一个类, 方便常量的分类管理
 */
public class Constants {

	public static final String SUCCESS_CODE = "100";
	public static final String SUCCESS_MESSAGE = "请求成功";

	public static final String FAIL_CODE = "500";
	public static final String FAIL_MESSAGE = "请求失败";

	/**
	 * session中存放用户信息的key值
	 */
	public static final String SESSION_USER_INFO = "userInfo";
	public static final String SESSION_USER_PERMISSION = "userPermission";
}
