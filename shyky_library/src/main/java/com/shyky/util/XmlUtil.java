package com.shyky.util;

/**
 * XML操作相关工具类
 *
 * @author Shyky
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/4/13
 * @since 1.0
 */
public final class XmlUtil {
	/**
	 * 构造方法私有化
	 */
	private XmlUtil() {

	}

	/**
	 * 判断是否是XML数据格式
	 *
	 * @param str
	 *            String字符串
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isXml(final String str) {
		if (TextUtil.isEmptyAndNull(str))
			return false;
		return true;
	}

	/**
	 * 判断是否是XML数据格式
	 *
	 * @param str
	 *            CharSequence字符串
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isXml(final CharSequence str) {
		if (TextUtil.isEmptyAndNull(str))
			return false;
		return true;
	}
}