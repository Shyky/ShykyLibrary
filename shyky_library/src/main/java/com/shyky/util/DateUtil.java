package com.shyky.util;

import java.util.Calendar;

/**
 * 日期、日历相关工具类
 * 
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/4/6
 * @since 1.0
 */
public final class DateUtil {
	/**
	 * 构造方法私有化
	 */
	private DateUtil() {

	}

	/**
	 * 获取当前系统时间的年份
	 *
	 * @return 年份
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获取当前系统时间的月份
	 *
	 * @return 月份
	 */
	public static int getCurrentMonth() {
		// 取到的月份要加1
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前系统时间的年份和月份
	 * 
	 * @param pattern
	 *            日期格式
	 * @return 当前的年份和月份
	 */
	public static String getCurrentYearAndMonth(String pattern) {
		return getCurrentYearAndMonth(pattern, false);
	}

	/**
	 * 获取当前系统时间的年份和月份
	 * 
	 * @param pattern
	 *            日期格式
	 * @param flag
	 *            月份是否使用两位数表示
	 * @return 当前的年份和月份
	 */
	public static String getCurrentYearAndMonth(String pattern, boolean flag) {
		int month = getCurrentMonth();
		String result = TextUtil.toString(month);
		if (flag) {
			if (month < 10)
				result = "0" + month;
		}
		return String.format("%s%s%s", getCurrentYear(), pattern, result);
	}

	/**
	 * 获取当前系统时间的年、月、日
	 * 
	 * @param pattern
	 *            日期格式
	 * @param flag
	 *            月和日是否使用两位数表示
	 * @return 当前的年份和月份
	 */
	public static String getCurrentYearMonthDay(String pattern, boolean flag) {
		int day = getCurrentDay();
		String result = getCurrentYearAndMonth(pattern, flag);
		if (flag) {
			if (day < 10)
				return result + pattern + "0" + day;
		}
		return result + pattern + day;
	}

	/**
	 * 获取当前系统时间的年、月、日
	 * 
	 * @param pattern
	 *            日期格式
	 * @return 当前的年份和月份
	 */
	public static String getCurrentYearMonthDay(String pattern) {
		return getCurrentYearMonthDay(pattern, false);
	}

	/**
	 * 获取当前系统时间的日期
	 *
	 * @return 日期
	 */
	public static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/***
	 * 获取指定年份下的月份下的天数
	 *
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return 天数
	 */
	public static int getDayOfMonth(int year, int month) {
		if (month < 1 || month > 12)
			return 0;
		int day = 0;
		// 判断大月份
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			day = 31;
		}
		// 判断小月
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			day = 30;
		}
		// 判断平年与闰年
		if (month == 2) {
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
				day = 29;
			} else {
				day = 28;
			}
		}
		return day;
	}
}