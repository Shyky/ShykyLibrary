package com.shyky.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间相关工具类
 *
 * @author Shyky
 * @version 1.2
 * @date 2016/3/25
 * @since 1.0
 */
public final class TimeUtil {
    /**
     * 构造方法
     */
    private TimeUtil() {

    }

    /**
     * 获取当前系统时间
     *
     * @return 格式化的时间戳
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
    }

    /**
     * 获取当前系统时间
     *
     * @return 格式化的时间戳
     */
    public static String getCurrentTime(Locale locale) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).format(new Date());
    }

    /**
     * 获取当前系统时间
     *
     * @return 格式化的时间戳
     */
    public static String getCurrentTime(String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(new Date());
    }

    /**
     * 获取当前系统时间的毫秒数
     *
     * @return 当前时间的毫秒数
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前系统时间
     *
     * @param pattern 时间格式
     * @return 格式化的时间戳
     */
    public static String getCurrentTime(String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date());
    }

    /**
     * 小时数转换成秒数
     *
     * @param hour 小时数
     * @return 秒数
     */
    public static long hour2Second(int hour) {
        return hour * 60 * 60;
    }

    /**
     * 小时数转换成毫秒数
     *
     * @param hour 小时数
     * @return 毫秒数
     */
    public static long hour2Millis(int hour) {
        return hour * 60 * 60 * 1000;
    }

    public static String format(long timeMillis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeMillis));
    }

    public static String format(long timeMillis, Locale locale) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).format(new Date(timeMillis));
    }

    public static String format(long timeMillis, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(timeMillis));
    }

    public static String format(long timeMillis, String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(new Date(timeMillis));
    }
}