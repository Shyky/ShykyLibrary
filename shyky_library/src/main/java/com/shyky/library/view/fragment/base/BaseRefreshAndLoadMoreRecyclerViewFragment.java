package com.shyky.library.view.fragment.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shyky.library.R;
import com.shyky.library.adapter.base.BaseRecyclerViewAdapter;
import com.shyky.library.view.widget.HidingScrollListener;
import com.shyky.library.view.widget.SmartRecyclerView;
import com.shyky.library.view.widget.SmartRecyclerView.OnRecycleViewLoadMoreListener;
import com.shyky.util.ObjectUtil;

import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 基础的带有RecyclerView的Fragment
 *
 * @param <RESPONSE> 泛型参数，数据源，可以是List或Entity
 * @param <ENTITY>   泛型参数，数据源集合中的实体
 * @param <ADAPTER>  泛型参数，数据适配器
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/25
 * @since 1.0
 */
public abstract class BaseRefreshAndLoadMoreRecyclerViewFragment<ADAPTER extends RecyclerView.Adapter, RESPONSE, ENTITY> extends BaseRefreshAndLoadMoreListFragment<SmartRecyclerView, ADAPTER, RESPONSE, ENTITY> implements OnRefreshListener, OnRecycleViewLoadMoreListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private BaseRecyclerViewAdapter adapter;

    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    protected RecyclerView.ItemAnimator getItemAnimator() {
        return null;
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    protected final void enableLoadMore(SmartRecyclerView smartRecyclerView) {
        smartRecyclerView.setAutoLoadMoreEnable(true);
    }

    @Override
    protected final void stopRefresh(SmartRecyclerView smartRecyclerView) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected final void setRefreshTime(SmartRecyclerView smartRecyclerView, String refreshTime) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected final void setLoadMoreSuccess(SmartRecyclerView smartRecyclerView) {
        smartRecyclerView.setLoadMoreFinish(true);
    }

    @Override
    protected final void stopLoadMore(SmartRecyclerView smartRecyclerView) {
        smartRecyclerView.setLoadMoreFinish(false);
    }

    @Override
    protected SmartRecyclerView getList(@NonNull RelativeLayout listContainer) {
        return (SmartRecyclerView) listContainer.findViewById(R.id.recyclerView);
    }

    @CallSuper
    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        super.initView(inflateView, savedInstanceState);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        if (swipeRefreshLayout == null)
            throw new NullPointerException("SwipeRefreshLayout的ID必须是com.shyky.library.R.id.swipeRefreshLayout");
        if (list == null)
            throw new NullPointerException("RecyclerView的ID必须是com.shyky.library.R.id.recyclerView");

//        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
//                R.color.color3, R.color.color4);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
//        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                        .getDisplayMetrics()));

        list.setHasFixedSize(true);
        list.setAutoLoadMoreEnable(true);
        list.setFirstLoadGone(true);
        if (getLayoutManager() == null) {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            list.setLayoutManager(layoutManager);
        } else
            list.setLayoutManager(getLayoutManager());
        if (getItemAnimator() == null)
            list.setItemAnimator(new DefaultItemAnimator());
        else
            list.setItemAnimator(getItemAnimator());
        // Item间距
        if (getItemDecoration() != null)
            list.addItemDecoration(getItemDecoration());

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        list.setOnRecycleViewLoadMoreListener(this);

        // RecyclerView item和item view点击事件
        adapter = (BaseRecyclerViewAdapter) getListAdapter();
        if (ObjectUtil.notNull(adapter)) {
            list.setAdapter(adapter);
//        if (adapter != null /*&& adapter instanceof BaseRecyclerViewAdapter*/) {
            adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull View view, @IdRes int viewResId, int position) {
                    d("position", position);
                    if (!isListEmpty()) {
                        final ENTITY entity = (ENTITY) (adapter).getItem(position);
                        onListItemClick(view, view.getId(), position, entity);
                        d("item entity", entity);
                    } else {
                        d("item entity is null");
                        onListItemClick(view, view.getId(), position, null);
                    }
                }
            });

            adapter.setOnItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
                @Override
                public void onClick(@NonNull View view, @IdRes int viewResId, int position) {
                    d("position", position);
                    if (!isListEmpty()) {
                        final ENTITY entity = (ENTITY) (adapter).getItem(position);
                        onListItemViewClick(view, view.getId(), position, entity);
                        d("item entity", entity);
                    } else {
                        d("item entity is null");
                        onListItemViewClick(view, view.getId(), position, null);
                    }
                }
            });
        }

        // RecyclerView滚动事件
        list.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                onListScrolled(ORIENTATION_UP);
            }

            @Override
            public void onShow() {
                onListScrolled(ORIENTATION_DOWN);
            }
        });
    }

    @Override
    protected void notifyAdapterDataSetChanged() {
        if (ObjectUtil.notNull(adapter)) {
            adapter.notifyDataSetChanged();
        }
    }

//    @Override
//    protected final void setListAdapter(@NonNull SmartRecyclerView smartRecyclerView) {
//        if (ObjectUtil.notNull(adapter)) {
//            smartRecyclerView.setAdapter(adapter);
//        }
//    }

    @Override
    protected void updateListItem(int index, @NonNull ENTITY entity) {
        adapter.set(index, entity);
    }

    @Override
    protected void removeItemFromList(int index) {
        adapter.remove(index);
    }

    @Override
    protected void removeItemFromList(@NonNull ENTITY entity) {
        adapter.remove(entity);
    }

    @NonNull
    @Override
    protected ENTITY getListFirstItem() {
        return getListItem(0);
    }

    @NonNull
    @Override
    protected ENTITY getListItem(int position) {
        return (ENTITY) adapter.getItemData(position);
    }

    @NonNull
    @Override
    protected ENTITY getListLastItem() {
        return getListItem(getListItemCount() - 1);
    }

    @Override
    protected int getListItemCount() {
        return adapter.getItemDataCount();
    }

    @Override
    public final void clearListData() {
        if (ObjectUtil.notNull(adapter))
            adapter.clear();
    }

    @Override
    protected boolean isListEmpty() {
        return ObjectUtil.isNull(adapter) ? true : adapter.isEmpty();
    }

    @NonNull
    @Override
    protected List<ENTITY> getListData() {
        return adapter.getData();
    }

    @Override
    protected void addDataToList(@NonNull List<ENTITY> data) {
        // if (adapter instanceof BaseRecyclerViewAdapter)
        adapter.addAll(data);
    }

    @Override
    protected void insertItemToList(int index, @NonNull ENTITY entity) {
        adapter.insert(index, entity);
    }

    @Override
    protected final void scrollToTop() {
        if (!isListEmpty())
            list.scrollToPosition(0);
    }

    @Override
    protected void smoothScrollToTop() {
        if (!isListEmpty())
            list.smoothScrollToPosition(0);
    }

    @Override
    protected int getLoadingTextResId() {
        return 0;
    }

    @Override
    protected int getFailureImageResId() {
        return 0;
    }

    @Override
    protected int getFailureTextResId() {
        return 0;
    }

    @Override
    protected int getFailureRetryTextResId() {
        return 0;
    }

    @Override
    protected int getNothingImageResId() {
        return 0;
    }

    @Override
    protected int getNothingTextResId() {
        return 0;
    }

    @Override
    protected int getNoNetworkLayoutResId() {
        return 0;
    }

    @Override
    protected int getLoadingLayoutResId() {
        return 0;
    }

    @Override
    protected int getFailureLayoutResId() {
        return 0;
    }

    @Override
    protected int getNothingLayoutResId() {
        return 0;
    }

    @Override
    public final void onRefresh() {
        refresh();
    }

    @Override
    public final void onLoadMore() {
        loadMore();
    }
}