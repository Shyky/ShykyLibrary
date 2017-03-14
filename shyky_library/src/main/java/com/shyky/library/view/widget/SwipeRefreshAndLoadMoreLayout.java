package com.shyky.library.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.shyky.library.R;
import com.shyky.library.util.ApiUtil;
import com.shyky.library.util.LayoutUtil;
import com.shyky.util.ObjectUtil;

import static com.socks.library.KLog.d;

/**
 * 扩展官方的SwipeRefreshLayout实现上拉加载更多功能，可以用于ListView、GridView、RecyclerView、ScrollView等控件。
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/9/28
 * @since 1.0
 */
public class SwipeRefreshAndLoadMoreLayout extends SwipeRefreshLayout {
    private ScrollView scrollView;
    private ListView listView;
    private GridView gridView;
    private RecyclerView recyclerView;
    /**
     * 上拉加载更多回调接口
     */
    private OnLoadMoreListener onLoadMoreListener;
    /**
     * 触摸屏幕按下的Y坐标
     */
    private float downY, upY;
    /**
     * 是否可以上拉加载更多
     */
    private boolean canLoadMore;
    /**
     * 控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
     */
    private int scaledTouchSlop;
    /**
     * 加载更多显示布局
     */
    private View loadingMoreView;
    /**
     * 记录是否正在加载更多
     */
    private boolean isLoadingMore;

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public SwipeRefreshAndLoadMoreLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeRefreshAndLoadMoreLayout(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        canLoadMore = true;
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        d("scaledTouchSlop", scaledTouchSlop);
        if (ObjectUtil.isNull(loadingMoreView)) {
            // 如果没有设置加载更多布局就给一个默认的布局显示
            loadingMoreView = LayoutUtil.inflate(context, R.layout.item_loading);
        } else {

        }
    }

    public void setLoadMoreEnabled(boolean enabled) {
        canLoadMore = enabled;
    }

    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void setListViewOnScrollListener() {
        if (ObjectUtil.notNull(listView)) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    d("ListView onScrollStateChanged -> scrollState", scrollState);
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    d("ListView onScroll -> firstVisibleItem", firstVisibleItem + " , visibleItemCount = " + visibleItemCount + " , totalItemCount = " + totalItemCount);
                }
            });
        }
    }

    private void setRecyclerViewOnScrollListener() {
        if (ObjectUtil.notNull(recyclerView)) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    d("onScrollStateChanged -> newState", newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    d("onScrolled -> dx", dx + " , dy = " + dy);
                }
            });
        }
    }

    private void addListViewLoadMoreView() {
        if (ObjectUtil.notNull(onLoadMoreListener)) {
            // 标识当前正在加载更多
            isLoadingMore = true;
            if (isLoadingMore) {
                // 如果子view是ListView，则给ListView添加底部item布局
                if (ObjectUtil.notNull(listView)) {
                    listView.addFooterView(loadingMoreView);
                } else if (ObjectUtil.notNull(recyclerView)) {
                    // 如果子view是RecyclerView，通过设置Adapter实现footerView
                }
            } else {
                // 如果子view是ListView，则移除ListView底部添加的item布局
                if (ObjectUtil.notNull(listView)) {
                    listView.removeFooterView(loadingMoreView);
                } else if (ObjectUtil.notNull(recyclerView)) {

                }
                // 重置滑动的坐标
                downY = 0;
                upY = 0;
            }
            onLoadMoreListener.onLoadMore();
        }
    }

    private void removeListViewLoadMoreView() {

    }

    private void addRecyclerViewLoadMoreView() {
        if (isLoadingMore) {
            if (ObjectUtil.notNull(recyclerView)) {
                // 如果子view是RecyclerView，通过设置Adapter实现footerView
            }
        } else {
            if (ObjectUtil.notNull(recyclerView)) {

            }
            // 重置滑动的坐标
            downY = 0;
            upY = 0;
        }
    }

    private void removeRecyclerViewLoadMoreView() {

    }

    /**
     * 判断是否能上拉加载更多
     *
     * @return 是返回true，否则返回false
     */
    private boolean canLoadMore() {
        if (canLoadMore)
            return true;
        else if (ObjectUtil.notNull(listView) && ObjectUtil.notNull(listView.getAdapter())) {
            return listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1;
        } else if (ObjectUtil.notNull(recyclerView) && ObjectUtil.notNull(recyclerView.getAdapter())) {
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            // 获取最后一个可见的item的位置
            int lastVisibleItemPosition = 0;
            if (ObjectUtil.notNull(layoutManager)) {
                // 针对不同的布局管理器处理
                if (layoutManager instanceof LinearLayoutManager) {
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()]);
                    // 获得最大的位置
                    final int maxPosition = Integer.MIN_VALUE;
                    for (int position : lastPositions) {
                        lastVisibleItemPosition = Math.max(maxPosition, position);
                    }
                } else {
                    lastVisibleItemPosition = layoutManager.getItemCount() - 1;
                }
            }
            return lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    private void loadMore() {
        // 正在移动，移动过程中判断是否能下拉加载更多
        if (canLoadMore()) {
            d("触发上拉加载更多数据...");
            if (ObjectUtil.notNull(onLoadMoreListener)) {
                // 标识当前正在加载更多
                isLoadingMore = true;
                if (isLoadingMore) {
                    addListViewLoadMoreView();
                    addRecyclerViewLoadMoreView();
                } else {
                    removeListViewLoadMoreView();
                    removeRecyclerViewLoadMoreView();
                    // 重置滑动的坐标
                    downY = 0;
                    upY = 0;
                }
                onLoadMoreListener.onLoadMore();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        d("onLayout -> changed", changed + " , left = " + left + " , top = " + top + " , right = " + right + " , bottom = " + bottom);
        // 获取包含的子控件，只能是一个容器控件
        final int childCount = getChildCount();
        final View firstChildView = getChildAt(0);
        d("childCount", childCount);
        d("firstChildView", firstChildView);
        // 判断容器下有多少个子view
        if (childCount > 0) {
            // 判断第一个子view是否是ListView
            if (firstChildView instanceof ListView) {
                d("第一个子view是ListView...");
                listView = (ListView) firstChildView;
                // 设置ListView的滑动监听
                setListViewOnScrollListener();
            } else if (firstChildView instanceof RecyclerView) {
                d("第一个子view是RecyclerView...");
                // 判断第一个子view是否是RecyclerView
                recyclerView = (RecyclerView) firstChildView;
                // 设置RecyclerView的滑动监听
                setRecyclerViewOnScrollListener();
            } else if (firstChildView instanceof GridView) {
                // 判断第一个子view是否是GridView
                gridView = (GridView) firstChildView;
                // 设置GridView的滑动监听
                setListViewOnScrollListener();
            } else if (firstChildView instanceof ScrollView) {
                // 判断第一个子view是否是ScrollView
                scrollView = (ScrollView) firstChildView;
                // 设置ScrollView的滑动监听
                setListViewOnScrollListener();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        d("dispatchTouchEvent -> ev.getAction()", ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                // API兼容处理
                if (ApiUtil.isSupport(11)) {
                    downY = getY();
                } else {
                    downY = ev.getY();
                }
                d("MotionEvent.ACTION_DOWN -> downY", downY);
                break;
            case MotionEvent.ACTION_MOVE:
                // 处理加载更多逻辑
                loadMore();
                d("MotionEvent.ACTION_MOVE...");
                break;
            case MotionEvent.ACTION_UP:
                // 停止移动，移动的终点
                // API兼容处理
                if (ApiUtil.isSupport(11)) {
                    upY = getY();
                } else {
                    upY = ev.getY();
                }
                d("MotionEvent.ACTION_UP -> upY", upY);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}