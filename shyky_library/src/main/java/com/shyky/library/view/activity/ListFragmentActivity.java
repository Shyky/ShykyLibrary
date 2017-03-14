package com.shyky.library.view.activity;

import android.support.v4.app.Fragment;

/**
 * 基础的带有ListFragment的Activity
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/12/10
 * @since 1.0
 */
public abstract class ListFragmentActivity extends ToolbarFragmentActivity {
    /**
     * 获取Fragment中要展示的ListFragment
     *
     * @return 带有列表的Fragment
     */
    protected abstract Fragment getListFragment();

    @Override
    public final Fragment getFragment() {
        return getListFragment();
    }
}