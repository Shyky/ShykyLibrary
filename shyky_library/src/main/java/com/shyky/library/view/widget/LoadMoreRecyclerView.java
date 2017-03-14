package com.shyky.library.view.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shyky.library.R;

import static com.socks.library.KLog.d;
import static com.socks.library.KLog.e;

/**
 * 实现上拉加载更多的RecyclerView
 *
 * @author Shyky
 * @version 1.1
 * @date 2016/9/26
 * @since 1.0
 */
public class LoadMoreRecyclerView extends RecyclerView {
    /**
     * 记录是否正在加载更多
     */
    private boolean isLoadingMore;
    private OnRecycleViewLoadMoreListener onRecycleViewLoadMoreListener;

    /**
     * 加载更多监听
     */
    public interface OnRecycleViewLoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    protected class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected class LoadMoreAdapter extends Adapter<FooterViewHolder> {
        private Context context;
        private int layoutResId;

        protected LoadMoreAdapter(@NonNull Context context) {
            this.context = context;
        }

        protected LoadMoreAdapter(@NonNull Context context, @LayoutRes int layoutResId) {
            this(context);
            this.layoutResId = layoutResId;
        }

        @Override
        public FooterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View inflateView;
            if (layoutResId > 0)
                inflateView = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            else
                inflateView = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new FooterViewHolder(inflateView);
        }

        @Override
        public void onBindViewHolder(FooterViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return getChildCount();
        }
    }

    public LoadMoreRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                d("onScrollStateChanged -> newState" + newState);
                if (newState == SCROLL_STATE_IDLE) {
                    final LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager == null)
                        e("RecyclerView's layoutManager is null.");
                    else {
                        // item总数
                        final int itemCount = layoutManager.getItemCount();
                        // 可见的item个数
                        final int visibleChildCount = layoutManager.getChildCount();
                        if (visibleChildCount > 0) {
                            final View lastVisibleView = recyclerView.getChildAt(visibleChildCount - 1);
                            int lastVisiblePosition = recyclerView.getChildLayoutPosition(lastVisibleView);
                            if (lastVisiblePosition >= itemCount - 1) {
//                                    footerView.setVisibility(VISIBLE);
                                // 标识正在加载更多
                                isLoadingMore = true;
                                if (onRecycleViewLoadMoreListener != null) {
                                    onRecycleViewLoadMoreListener.onLoadMore();
                                }
                            }
                            if (layoutManager instanceof LinearLayoutManager) {

                            } else if (layoutManager instanceof GridLayoutManager) {

                            } else if (layoutManager instanceof StaggeredGridLayoutManager) {

                            } else {
                                // 自定义LayoutManager，直接继承LayoutManager类实现
                            }
                        } else {

                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                d("onScrolled -> dx", dx + " , dy = " + dy);
            }
        });
    }
}