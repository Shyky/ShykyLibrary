package com.shyky.library.util;

import android.os.Environment;

import com.shyky.library.BaseApplication;

/**
 * 存储相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/7/7
 * @since 1.0
 */
public final class StorageUtil {
    /**
     * 构造方法私有化
     */
    private StorageUtil() {

    }

    public static boolean hasStorage() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getSDcardRootPath() {
        if (hasStorage())
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        return null;
    }

    public static String getCachePath() {
        return BaseApplication.getContext().getCacheDir().getAbsolutePath();
    }
}