package com.shyky.library.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shyky.library.BaseApplication;

/**
 * 布局加载及转换工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/6/14
 * @since 1.0
 */
public final class LayoutUtil {
    /**
     * 构造方法
     */
    private LayoutUtil() {

    }

    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@LayoutRes int layoutResId) {
        return LayoutInflater.from(BaseApplication.getContext()).inflate(layoutResId, null, false);
    }

    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@NonNull Context context, @LayoutRes int layoutResId) {
        return LayoutInflater.from(context).inflate(layoutResId, null, false);
    }

    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@NonNull ViewGroup root, @LayoutRes int layoutResId) {
        return LayoutInflater.from(BaseApplication.getContext()).inflate(layoutResId, root, false);
    }

    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@NonNull Context context, @NonNull ViewGroup root, @LayoutRes int layoutResId) {
        return LayoutInflater.from(context).inflate(layoutResId, root, false);
    }
}