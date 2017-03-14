package com.shyky.util;

import android.support.annotation.NonNull;

/**
 * 文本操作工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/7/7
 * @since 1.0
 */
public final class TextUtil {
    /**
     * 构造方法私有化
     */
    private TextUtil() {

    }

    public static int length(CharSequence str) {
        return ObjectUtil.isNull(str) ? 0 : str.length();
    }

    public static int length(String str) {
        return ObjectUtil.isNull(str) ? 0 : str.length();
    }

    /**
     * 判断文本是否为空
     *
     * @param str 要判断的文本
     * @return 如果文本对象为null或文本的长度为0返回true，否则返回false
     */
    public static boolean isEmpty(CharSequence str) {
        return length(str) == 0;
    }

    /**
     * 判断文本是否为空
     *
     * @param str 要判断的文本
     * @return 如果文本对象为null或文本的长度为0返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return ObjectUtil.isNull(str) || str.trim().length() == 0;
    }

    public static boolean isBlank(String str) {
        return (ObjectUtil.isNull(str) || str.trim().equals(""));
    }

    public static boolean isBlank(CharSequence str) {
        return (ObjectUtil.isNull(str) || str.equals(""));
    }

    public static boolean isEmptyAndNull(CharSequence str) {
        return isEmpty(str) || ObjectUtil.isNull(str);
    }

    public static boolean notEmptyAndNull(CharSequence str) {
        return !isEmptyAndNull(str);
    }

    public static boolean isEmptyAndNull(String str) {
        return isEmpty(str) || ObjectUtil.isNull(str);
    }

    public static boolean notEmptyAndNull(String str) {
        return !isEmptyAndNull(str);
    }

    public static boolean isUpperCase(char word) {
        final int code = word;
        // 整数值65~90为大写英文字母
        return code >= 65 && code <= 90;
    }

    public static boolean isLowerCase(char word) {
        final int code = word;
        // 整数值97~122为小写英文字母
        return code >= 97 && code <= 122;
    }

    /**
     * 将英文字母转换成大写字母
     *
     * @param word 要转换的英文字符
     * @return 成功返回true，失败返回false
     */
    public static char toLowerCase(char word) {
        if (isUpperCase(word)) {
            return word;
        }
        final int code = word;
        final char upCase = (char) (code + 26 + 5); // 26是26个英文字母，中间还有5个别的符合，参照ASCII码
        return upCase;
    }

    public static String toString(boolean data) {
        return String.valueOf(data);
    }

    public static String toString(char data) {
        return String.valueOf(data);
    }

    public static String toString(byte data) {
        return String.valueOf(data);
    }

    public static String toString(int data) {
        return String.valueOf(data);
    }

    public static String toString(short data) {
        return String.valueOf(data);
    }

    public static String toString(long data) {
        return String.valueOf(data);
    }

    public static String toString(float data) {
        return String.valueOf(data);
    }

    public static String toString(double data) {
        return String.valueOf(data);
    }

    public static String toString(char[] data) {
        return String.valueOf(data);
    }

    public static String toString(byte[] data) {
        return String.valueOf(data);
    }

    public static String toString(Object data) {
        if (!ObjectUtil.isNull(data))
            return data.toString().trim();
        return null;
    }

    /**
     * 截取指定字符在原始字符串出现的位置后面的所有字符
     *
     * @param str 要截取的原始字符串
     * @param spl 用作截取的字符
     * @return 如果在原始字符串中能找到截取字符则返回截取后的字符串，失败返回null
     */
    public static String substring(String str, char spl) {
        return substring(str, toString(spl));
    }

    /**
     * 截取指定字符在原始字符串出现的位置后面的所有字符
     *
     * @param str 要截取的原始字符串
     * @param spl 用作截取的字符串
     * @return 如果在原始字符串中能找到截取字符串则返回截取后的字符串，失败返回null
     */
    public static String substring(@NonNull String str, @NonNull String spl) {
        if (TextUtil.isEmptyAndNull(str) || TextUtil.isEmptyAndNull(spl))
            return null;
        final int start = str.indexOf(spl);
        if (start == -1)
            return null;
//        throw new IllegalArgumentException("传入的第二个方法参数是分割字符，没有找到字符：" + spl);
        return str.substring(start + 1, str.length());
    }

    /**
     * 截取某两个区间的字符串
     *
     * @param str   要截取的原始字符串
     * @param start 用作截取的开始字符串
     * @param end   用作截取的结束字符串
     * @return 如果在原始字符串中能找到截取字符串则返回截取后的字符串，失败返回null
     */
    public static String substring(@NonNull String str, @NonNull String start, @NonNull String end) {
        if (isEmptyAndNull(str))
            return null;
        else if (isEmptyAndNull(start) || isEmptyAndNull(end))
            return str;
        final int beginIndex = str.indexOf(start);
        final int endIndex = str.lastIndexOf(end);
        if (beginIndex == -1 && endIndex == -1)
            return null;
        return str.substring(beginIndex, endIndex);
    }

    /**
     * 比较两个文本是否相等
     *
     * @param str1 文本1
     * @param str2 文本2
     * @return 相等返回true，不相等返回false
     */
    public static boolean equals(CharSequence str1, CharSequence str2) {
        if (ObjectUtil.isNull(str1) || ObjectUtil.isNull(str2))
            throw new NullPointerException("字符串不能为null");
        if (str1 == str2)
            return true;
        int len = str1.length();
        if (len == str2.length()) {
            if (str1.getClass() == String.class && str2.getClass() == String.class)
                return str1.equals(str2);
            else {
                for (int j = 0; j < len; j++) {
                    if (str1.charAt(j) != str2.charAt(j))
                        return false;
                }
                return true;
            }
        }
        return false;
    }
}