package com.shyky.library.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/9/12
 * @since 1.0
 */
public final class PermissionUtil {
    /**
     * 申请当前数组中的权限
     *
     * @param activity    申请权限的页面
     * @param requestCode 返回码
     * @param permissions 权限数组
     */
    public static void requestPermissions(Activity activity, int requestCode, String[] permissions) {
        for (int x = 0; x < permissions.length; x++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[x]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{permissions[x]}, requestCode);
            }
        }
    }

    /**
     * 检测当前数组中的权限是否都已经申请
     *
     * @param activity    当前类
     * @param permissions 权限数组
     * @return true：全部都已经申请， false：还有没有申请的权限
     */
    public static boolean checkPermissions(Activity activity, String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}