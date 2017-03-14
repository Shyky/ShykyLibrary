package com.shyky.util;

/**
 * Created by shyky on 16-4-13.
 */

/**
 * @author Shyky
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/4/13
 * @since 1.0
 */
public final class HtmlUtil {
	/**
	 * 构造方法私有化
	 */
	private HtmlUtil() {

	}

	public static boolean isHtml(final String str) {
		if (TextUtil.isEmptyAndNull(str))
			return false;
		return true;
	}

	public static boolean isHtml(final CharSequence str) {
		if (TextUtil.isEmptyAndNull(str))
			return false;
		return true;
	}
}