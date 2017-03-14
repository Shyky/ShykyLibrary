package com.shyky.library.view.activity;

/**
 * 具有返回功能的带有Toolbar和Fragment的Activity
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/12/10
 * @since 1.0
 */
public class BackToolbarFragmentActivity extends ToolbarFragmentActivity {
    @Override
    public final void onToolbarNavigationClick() {
        finish();
    }
}