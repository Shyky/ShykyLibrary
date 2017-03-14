package com.shyky.library.view.widget.decoration;

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.shyky.library.R;
import com.shyky.library.util.ResourceUtil;
import com.shyky.library.view.widget.SmartRecyclerView;
import com.shyky.util.ObjectUtil;

import static com.socks.library.KLog.d;

/**
 * 可以设置item上下左右间距的RecyclerView ItemDecoration
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/8/13
 * @since 1.0
 */
public final class MarginItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * item间距
     */
    private int margin;

    /**
     * 构造方法，默认间距为10dp
     */
    public MarginItemDecoration() {
        margin = -1;
    }

    /**
     * 构造方法
     *
     * @param margin item间距
     */
    public MarginItemDecoration(@DimenRes int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int offset;
        SmartRecyclerView recyclerView = null;
        if (parent instanceof SmartRecyclerView) {
            recyclerView = (SmartRecyclerView) parent;
        }
        final int itemPosition = parent.getChildAdapterPosition(view);
        final int itemCount = parent.getAdapter().getItemCount();
        d("itemCount = " + itemCount + " , itemPosition = " + itemPosition);
        if (margin == -1)
            offset = ResourceUtil.getDimens(R.dimen.dp_10);
        else
            offset = ResourceUtil.getDimens(margin);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        d("layoutManager", layoutManager);
        // RecyclerView的布局管理器是线性布局
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            final int orientation = linearLayoutManager.getOrientation();
            // 如果是垂直方向，则除了最后一个item没有下边距，其他的item都有上、下、左、右边距
            if (orientation == LinearLayoutManager.VERTICAL) {
                if (itemPosition >= 0 && itemPosition != itemCount - 1) {
                    // 设置左、上、右、下边距
                    outRect.set(offset, offset, offset, 0);
                } else {
                    outRect.set(offset, offset, offset, offset);
                }
            } else {
                outRect.set(offset, offset, offset, offset);
            }
        } else if ("android.support.v7.widget.GridLayoutManager".equals(layoutManager.getClass().getName())) {
            // layoutManager.getClass() == GridLayoutManager.class 竟然无效？
            // layoutManager instanceof GridLayoutManager 竟然无效？
            // RecyclerView的布局管理器是网格布局
            d("RecyclerView's layout manager is GridLayoutManager.");
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int orientation = gridLayoutManager.getOrientation();
            // 是垂直方向
            if (orientation == GridLayoutManager.VERTICAL) {
                d("垂直方向...");
                // item position为偶数的没有左边距，itemPosition从0开始
                if ((itemPosition + 1) % 2 == 0) {
                    outRect.set(offset, offset, offset, 0);
                } else {
                    outRect.set(offset, offset, 0, 0);
                }
            } else {

            }
        } else if ("android.support.v7.widget.StaggeredGridLayoutManager".equals(layoutManager.getClass().getName())) {
            // layoutManager.getClass() == GridLayoutManager.class 竟然无效？
            // layoutManager instanceof GridLayoutManager 竟然无效？
            // RecyclerView的布局管理器是网格布局
            d("RecyclerView's layout manager is GridLayoutManager.");
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            final int orientation = staggeredGridLayoutManager.getOrientation();
            // 是垂直方向
            if (orientation == GridLayoutManager.VERTICAL) {
                d("垂直方向...");
                // item position为偶数的没有左边距，itemPosition从0开始
                if ((itemPosition + 1) % 2 == 0) {
                    if (ObjectUtil.notNull(recyclerView)) {
                        if (itemPosition == itemCount - 2)
                            outRect.set(offset, offset, offset, offset);
                        else
                            outRect.set(offset, offset, offset, 0);
                    } else {
                        if (itemPosition == itemCount - 1)
                            outRect.set(offset, offset, offset, offset);
                        else
                            outRect.set(offset, offset, offset, 0);
                    }
                } else {
                    if (ObjectUtil.notNull(recyclerView)) {
                        if (itemPosition == itemCount - 2)
                            outRect.set(offset, offset, 0, offset);
                        else
                            outRect.set(offset, offset, 0, 0);
                    } else {
                        if (itemPosition == itemCount - 1)
                            outRect.set(offset, offset, 0, offset);
                        else
                            outRect.set(offset, offset, 0, 0);
                    }
                }
            } else {

            }
        }
    }
}