package com.shyky.util;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author Shyky
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/4/18
 * @since 1.0
 */
public final class RegExpUtil {
	/**
	 * This pattern is intended for searching for things that look like they
	 * might be phone numbers in arbitrary text, not for validating whether
	 * something is in fact a phone number. It will miss many things that are
	 * legitimate phone numbers.
	 *
	 * <p>
	 * The pattern matches the following:
	 * <ul>
	 * <li>Optionally, a + sign followed immediately by one or more digits.
	 * Spaces, dots, or dashes may follow.
	 * <li>Optionally, sets of digits in parentheses, separated by spaces, dots,
	 * or dashes.
	 * <li>A string starting and ending with a digit, containing digits, spaces,
	 * dots, and/or dashes.
	 * </ul>
	 */
	public static final Pattern PHONE = Pattern.compile( // sdd = space, dot, or
															// dash
			"(\\+[0-9]+[\\- \\.]*)?" // +<digits><sdd>*
					+ "(\\([0-9]+\\)[\\- \\.]*)?" // (<digits>)<sdd>*
					+ "([0-9][0-9\\- \\.]+[0-9])"); // <digit><digit|sdd>+<digit>

	// matching as foo.su

	public static final Pattern EMAIL_ADDRESS = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	/**
	 * 构造方法私有化
	 */
	private RegExpUtil() {

	}

	/**
	 * 验证是否是邮箱
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isEmail(String text) {
		return !TextUtil.isEmptyAndNull(text) && EMAIL_ADDRESS.matcher(text).matches();
	}

	/**
	 * 验证是否是手机号码
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isPhone(String text) {
		return !TextUtil.isEmptyAndNull(text) && PHONE.matcher(text).matches();
	}

	/**
	 * 验证文本是否是数字
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isNumber(String text) {
		return !TextUtil.isEmptyAndNull(text) && text.matches("^[0-9]*$");
	}

	/**
	 * 验证文本是否是整数
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isInteger(String text) {
		return !TextUtil.isEmptyAndNull(text) && text.matches("^-?[1-9]d*$");
	}

	/**
	 * 验证文本是否是正整数
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isPositiveInteger(String text) {
		return !TextUtil.isEmptyAndNull(text) && text.matches("^[1-9]d*$");
	}

	/**
	 * 验证文本是否是负整数
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isNegativeInteger(String text) {
		return !TextUtil.isEmptyAndNull(text) && text.matches("^-[1-9]d*$");
	}

	/**
	 * 验证文本是否是浮点数
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isFloatNumber(String text) {
		return !TextUtil.isEmptyAndNull(text) && text.matches("^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$");
	}

	/**
	 * 验证文本是否是双精度浮点数
	 *
	 * @param text
	 *            要验证的文本
	 * @return 是返回true, 否则返回false
	 */
	public static boolean isDoubleNumber(String text) {
		return !TextUtil.isEmptyAndNull(text) && text.matches("^\\d{0,8}\\.{0,1}(\\d{1,2})?$");
	}
}