package com.shyky.util;

import java.util.List;

/**
 * cue文件（.cue）相关工具类
 *
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2017/1/7
 * @since 1.0
 */
public final class CueUtil {
    /**
     * 构造方法私有化
     */
    private CueUtil() {

    }

    public static String getPerformer() {
        return "";
    }

    public static List<String> getPerformers() {
        return null;
    }

    public static String getDate() {
        return "";
    }

    public static String getIndex() {
        return "";
    }

    public static List<String> getIndexes() {
        return null;
    }

    public static String getTitle() {
        return getTitle(0);
    }

    public static String getTitle(int index) {
        return "";
    }

    public static List<String> getTitles() {
        return null;
    }
}