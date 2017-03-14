package com.shyky.library.util;

import android.content.pm.PackageManager;

import com.shyky.library.BaseApplication;

/**
 * APP系统相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/17
 * @since 1.0
 */
public final class AppUtil {
    /**
     * 构造方法私有化
     */
    private AppUtil() {

    }

    /**
     * 获取APP包名称
     *
     * @return APP包名称
     */
    public static String getPackageName() {
        return BaseApplication.getContext().getPackageName();
    }

    /**
     * 获取当前APP版本名称
     *
     * @return APP版本名称
     */
    public static String getVersionName() {
        try {
            return BaseApplication.getContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode() {
        try {
            return BaseApplication.getContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}