package com.shyky.library.view.activity;

import com.shyky.library.R;
import com.shyky.library.view.activity.base.BaseActivity;

/**
 * 包含一个RecyclerView的Activity
 *
 * @author Shyky
 * @version 1.2
 * @date 2016/9/12
 * @since 1.0
 */
public class ListActivity extends BaseActivity {
    @Override
    public int getContentViewLayoutResId() {
        return R.layout.include_list;
    }
}