package com.shyky.library.util;

import android.os.Build;

/**
 * SDK API相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/6/3
 * @since 1.0
 */
public final class ApiUtil {
    /**
     * 构造方法私有化
     */
    private ApiUtil() {

    }

    /**
     * 是否支持指定的SDK版本
     *
     * @param apiNo SDK版本号
     * @return 返回true为支持，否则不支持
     */
    public static boolean isSupport(int apiNo) {
        return Build.VERSION.SDK_INT >= apiNo;
    }

    /**
     * 当前设备Android系统是否是5.0
     *
     * @return
     */
    public static boolean isApi21() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 当前设备Android系统是否支持MaterialDesign风格界面
     *
     * @return 是返回true，否则返回false
     */
    public static boolean isSupportMaterialDesign() {
        return isSupport(Build.VERSION_CODES.LOLLIPOP);
    }
}