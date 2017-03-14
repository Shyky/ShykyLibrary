package com.shyky.library.view.fragment.impl;

import android.support.annotation.NonNull;

/**
 * ListFragment列表界面接口
 *
 * @param <RESPONSE> 泛型参数，列表显示数据解析对应的实体类
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/6/1
 * @since 1.0
 */
public interface IListFragment<RESPONSE> {
    /**
     * 加载列表数据成功
     *
     * @param response 列表显示数据解析对应的实体类
     */
    void onSuccess(@NonNull RESPONSE response);

    /**
     * 加载列表数据失败
     *
     * @param message 失败消息
     */
    void onFailure(@NonNull String message);
}