package com.shyky.library.view.activity;

import com.shyky.library.R;
import com.shyky.library.view.activity.base.BaseActivity;

/**
 * 带有Toolbar和Fragment的Activity
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/12/10
 * @since 1.0
 */
public class ToolbarFragmentActivity extends BaseActivity {
    @Override
    public final int getContentViewLayoutResId() {
        return R.layout.activity_toolbar_fragment;
    }

    @Override
    public final int getFragmentContainerViewId() {
        return R.id.fragmentContainer;
    }
}