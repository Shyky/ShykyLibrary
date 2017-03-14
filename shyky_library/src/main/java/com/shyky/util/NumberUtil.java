package com.shyky.util;

import android.support.annotation.NonNull;

/**
 * 数字操作工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/7/7
 * @since 1.0
 */
public final class NumberUtil {
    /**
     * 构造方法私有化
     */
    private NumberUtil() {

    }

    /**
     * String类型值转换成int类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回整数类型值，否则返回int数据类型的默认值：0
     */
    public static int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * String类型值转化成long类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回长整数类型值，否则返回0L
     */
    public static long toLong(@NonNull String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * String类型值转换成float类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回浮点类型值，否则返回float数据类型的默认值：0.0f
     */
    public static float toFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    /**
     * String类型值转换成double类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回双精度类型值，否则返回double数据类型的默认值：0.0
     */
    public static double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}