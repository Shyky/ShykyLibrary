package com.shyky.library.view.activity.impl;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * 基础的Activity接口
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/3/21
 * @since 1.0
 */
public interface IBaseActivity {
    /**
     * 获取内容布局文件资源文件ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    int getContentViewLayoutResId();

    /**
     * 此方法用于初始化非View的成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     *
     * @param savedInstanceState Activity保存状态
     */
    void initData(@NonNull Bundle savedInstanceState);

    /**
     * 此方法用于初始化布局中所有的View，如果使用了View注入框架则不需要调用此方法
     *
     * @param savedInstanceState Activity保存状态
     */
    void findView(@NonNull Bundle savedInstanceState);

    /**
     * 此方法用于设置View
     *
     * @param savedInstanceState Activity保存状态
     */
    void initView(@NonNull Bundle savedInstanceState);

    /**
     * 此方法用于设置View的事件
     *
     * @param savedInstanceState Activity保存状态
     */
    void initListener(@NonNull Bundle savedInstanceState);

    /**
     * 此方法用于初始化对话框
     *
     * @param savedInstanceState Activity保存状态
     */
    void initDialog(Bundle savedInstanceState);

    /**
     * 获取要添加的Fragment容器控件ID
     *
     * @return 要添加的Fragment容器控件ID，默认返回-1
     */
    @IdRes
    int getFragmentContainerViewId();

    /**
     * 获取要添加的Fragment
     *
     * @return 要添加的Fragment，默认返回null
     */
    @NonNull
    Fragment getFragment();

    /**
     * 得到要添加的Fragment
     *
     * @return 要添加的Fragment，默认返回null
     */
    @NonNull
    Fragment[] getFragments();

    /**
     * 得到要添加的Fragment
     *
     * @return 要添加的Fragment，默认返回null
     */
    @NonNull
    List<Fragment> getFragmentList();

    /**
     * 得到Fragment标签
     *
     * @return Fragment标签名，默认返回null
     */
    @NonNull
    String getFragmentTag();

    /**
     * 获取Toolbar控件资源ID
     *
     * @return 控件资源ID
     */
    @IdRes
    int getToolbarResId();

    @LayoutRes
    int getToolbarMenuLayoutResId();

    @IdRes
    int getToolbarMenuResId();

    @MenuRes
    int getToolbarMenuId();

    void onToolbarNavigationClick();
}