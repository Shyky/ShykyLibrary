package com.shyky.library.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shyky.library.R;
import com.shyky.library.view.fragment.base.BaseFragment;

/**
 * 包含一个RecyclerView的Fragment，用于展示列表数据
 *
 * @author Shyky
 * @version 1.2
 * @date 2016/9/12
 * @since 1.0
 */
public class ListFragment<ADAPTER> extends BaseFragment {
    private RecyclerView recyclerView;
    private ADAPTER adapter;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.include_list;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void findView(View inflateView, Bundle savedInstanceState) {
        super.findView(inflateView, savedInstanceState);
        recyclerView = findViewById(R.id.recyclerView);

    }

    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        super.initView(inflateView, savedInstanceState);
    }
}