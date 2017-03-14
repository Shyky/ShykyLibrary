package com.shyky.util;

import java.util.List;

/**
 * 数组、集合工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/7/18
 * @since 1.0
 */
public final class ListUtil {
    /**
     * 构造方法私有化
     */
    private ListUtil() {

    }

    public static int length(int[] array) {
        return ObjectUtil.isNull(array) ? 0 : array.length;
    }

    public static <T> int length(T[] array) {
        return ObjectUtil.isNull(array) ? 0 : array.length;
    }

    public static <T> int size(List<T> list) {
        return ObjectUtil.isNull(list) ? 0 : list.size();
    }

    public static <T> boolean isEmpty(T[] arrays) {
        return ObjectUtil.isNull(arrays) || arrays.length == 0;
    }

    public static <T> boolean notEmpty(T[] arrays) {
        return !isEmpty(arrays);
    }

    public static <T> boolean isEmpty(List<T> list) {
        return size(list) == 0;
    }

    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> String foreach(T[] array) {
        if (!ObjectUtil.isNull(array)) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < array.length; j++) {
                sb.append(array[j]).append("=");
            }
        }
        return null;
    }

    public static <T> String foreach(T[] array1, T[] array2) {
        if (!ObjectUtil.isNull(array1) && !ObjectUtil.isNull(array2)) {
            for (int j = 0; j < array1.length; j++) {
                for (int k = 0; k < array2.length; k++) {
                }
            }
        }
        return null;
    }

    /**
     * 整数数组转成String输出
     *
     * @param array 整数数组
     * @return 如果集合不为空返回输出字符串，否则返回null
     */
    public static String string(int[] array) {
        if (length(array) > 0) {
            final StringBuilder sb = new StringBuilder();
            for (int item : array) {
                sb.append(item).append(",");
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return null;
    }

    /**
     * 数组转成String输出
     *
     * @param array 数组
     * @param <T>   泛型参数，数组中放置的元素数据类型
     * @return 如果集合不为空返回输出字符串，否则返回null
     */
    public static <T> String string(T[] array) {
        if (length(array) > 0) {
            final StringBuilder sb = new StringBuilder();
            for (T item : array) {
                sb.append(item).append(",");
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return null;
    }

    /**
     * 集合转成String输出
     *
     * @param list 集合
     * @param <T>  泛型参数，集合中放置的元素数据类型
     * @return 如果集合不为空返回输出字符串，否则返回"null"
     */
    public static <T> String toString(List<T> list) {
        return toString(list, ",");
    }

    /**
     * 集合转成String输出
     *
     * @param list      集合
     * @param <T>       泛型参数，集合中放置的元素数据类型
     * @param separator 分隔符
     * @return 如果集合不为空返回输出字符串，否则返回"null"
     */
    public static <T> String toString(List<T> list, String separator) {
        if (ObjectUtil.isNull(separator))
            throw new NullPointerException("分隔符不能为null");
        if (size(list) > 0) {
            final StringBuilder sb = new StringBuilder();
            for (T item : list) {
                sb.append(item).append(separator);
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return "null";
    }
}