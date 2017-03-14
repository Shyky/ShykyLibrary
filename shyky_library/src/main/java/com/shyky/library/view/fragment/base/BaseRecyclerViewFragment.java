package com.shyky.library.view.fragment.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.shyky.library.R;
import com.shyky.library.adapter.base.BaseRecyclerViewAdapter;
import com.shyky.util.ObjectUtil;

import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 通用的带有RecyclerView的Fragment
 *
 * @param <RESPONSE> 列表中要展示的数据
 * @param <ENTITY>   泛型参数，数据源集合中的实体
 * @param <ADAPTER>  泛型参数，数据适配器
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/5/28
 * @since 1.0
 */
public abstract class BaseRecyclerViewFragment<ADAPTER extends RecyclerView.Adapter, RESPONSE, ENTITY> extends BaseListFragment<RecyclerView, RESPONSE, ADAPTER, ENTITY> {
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
    protected RecyclerView getList(@NonNull RelativeLayout listContainer) {
        return (RecyclerView) listContainer.findViewById(R.id.recyclerView);
    }

    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        super.initView(inflateView, savedInstanceState);
        if (list == null)
            throw new NullPointerException("RecyclerView的ID必须是com.shyky.library.R.id.recyclerView");
        list.setHasFixedSize(true);
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

        // RecyclerView item和item view点击事件
        adapter = (BaseRecyclerViewAdapter) getListAdapter();
        if (ObjectUtil.notNull(adapter)) {
            list.setAdapter(adapter);
//        } else if (/*adapter != null && */adapter instanceof BaseRecyclerViewAdapter) {
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
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                d("dx", dx);
                d("dy", dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItem = 0;
                if (layoutManager != null) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                    } else if (layoutManager instanceof GridLayoutManager) {
                        firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] mFirstVisible = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
                        firstVisibleItem = mFirstVisible[0];
                    }
                }
                if (dy > 0) {
                    // 列表向上拉动
//                    hideOrShowScrollTopButton(false);

                    // 向上滚动
                    onListScrolled(ORIENTATION_UP);
                } else {
                    // 向下滚动
                    if (firstVisibleItem == 0) {
                        onListScrolled(ORIENTATION_UP);
//                        hideOrShowScrollTopButton(false);
                    } else {
//                        hideOrShowScrollTopButton(true);
                        // 向下滚动
                        onListScrolled(ORIENTATION_DOWN);
                    }
                }
            }

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
//                if (isSignificantDelta) {
//                    if (dy > 0) {
//                        // 向上滚动
//                        sendMessage(new ListScrollStateEvent(ListScrollStateEvent.Orientation.UP));
//                    } else {
//                        // 向下滚动
//                        sendMessage(new ListScrollStateEvent(ListScrollStateEvent.Orientation.DOWN));
//                    }
//                }
//            }
        });
    }

//    @Override
//    protected final void setListAdapter(@NonNull RecyclerView recyclerView) {
//        if (ObjectUtil.notNull(adapter)) {
//            recyclerView.setAdapter(adapter);
//        }
//    }

    @Override
    protected void onShowLoading() {

    }

    @Override
    protected void onFailureRetry() {

    }

    @Override
    protected void notifyAdapterDataSetChanged() {
        if (ObjectUtil.notNull(adapter)) {
            adapter.notifyDataSetChanged();
        }
    }

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

    @Override
    protected void addDataToList(@NonNull List<ENTITY> data) {
        // if (adapter instanceof BaseRecyclerViewAdapter)
        adapter.addAll(data);
    }

    @Override
    protected void insertItemToList(int index, @NonNull ENTITY entity) {
        adapter.insert(index, entity);
    }

    @NonNull
    @Override
    protected List<ENTITY> getListData() {
        return adapter.getData();
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
    protected int getFailureLayoutResId() {
        return 0;
    }

    @Override
    protected int getLoadingTextResId() {
        return 0;
    }

    @Override
    protected int getLoadingLayoutResId() {
        return 0;
    }

    @Override
    protected int getNothingLayoutResId() {
        return 0;
    }

    @Override
    protected int getFailureImageResId() {
        return 0;
    }

    @Override
    protected int getFailureRetryTextResId() {
        return 0;
    }

    @Override
    protected int getFailureTextResId() {
        return 0;
    }
}