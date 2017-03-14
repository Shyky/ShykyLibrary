package com.shyky.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.shyky.library.adapter.base.BaseRecyclerViewCheckableAdapter;

/**
 * @author Shyky
 * @version 1.1
 * @date 2016/8/18
 * @since 1.0
 */
public class SimpleAdapter<ENTITY> extends BaseRecyclerViewCheckableAdapter<ENTITY> {
    public SimpleAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getCheckableViewResId() {
        return 0;
    }

    @Override
    public int getItemLayoutResId(int viewType) {
        return 0;
    }
}