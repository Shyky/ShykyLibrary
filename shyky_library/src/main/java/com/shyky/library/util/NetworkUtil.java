package com.shyky.library.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.shyky.library.BaseApplication;

import static com.socks.library.KLog.d;

/**
 * 网络操作相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/4/6
 * @since 1.0
 */
public final class NetworkUtil {
    /**
     * 构造方法私有化
     */
    private NetworkUtil() {

    }

    /**
     * 检查当前网络是否可用
     *
     * @return 可用返回true，不可用返回false
     */
    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(BaseApplication.getContext());
    }

    /**
     * 当前网络不可用
     *
     * @return 可用返回true，不可用返回false
     */
    public static boolean networkNotAvailable() {
        return !isNetworkAvailable();
    }

    /**
     * 检查当前网络是否可用
     *
     * @param activity 当前Activity
     * @return 可用返回true，不可用返回false
     */
    public static boolean isNetworkAvailable(@NonNull Activity activity) {
        return activity != null && isNetworkAvailable(activity.getApplicationContext());
    }

    /**
     * 检查当前网络是否可用
     *
     * @return 可用返回true，不可用返回false
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        boolean flag = false;
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            flag = false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    d(i + "===类型===" + networkInfo[i].getTypeName());
                    d(i + "===状态===" + networkInfo[i].getState());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 检测网络是否连接
     *
     * @param activity
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(@NonNull Activity activity) {
        if (activity != null) {
            ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnected();
            d("isConnected = " + isConnected);
            return isConnected;
        } else
            throw new NullPointerException("activity is null");
    }

    /**
     * 检测网络是否连接
     *
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected() {
        return isNetworkConnected(BaseApplication.getContext());
    }

    /**
     * 网络未连接
     *
     * @return 是返回true，否返回false
     */
    public static boolean networkNotConnected() {
        return !isNetworkConnected();
    }

    /**
     * 检测网络是否连接
     *
     * @param context
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(@NonNull Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnected();
            d("isConnected = " + isConnected);
            return isConnected;
        } else
            throw new NullPointerException("context is null");
    }

    /**
     * 检测网络是否连接
     *
     * @param fragment
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(@NonNull Fragment fragment) {
        if (fragment != null) {
            return isNetworkConnected(fragment.getActivity());
        } else
            throw new NullPointerException("fragment is null");
    }
}