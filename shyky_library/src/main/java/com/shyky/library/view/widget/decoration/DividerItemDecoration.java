package com.shyky.library.view.widget.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shyky.library.util.ResourceUtil;

import static com.socks.library.KLog.d;

/**
 * 可以设置item间分隔线的RecyclerView ItemDecoration
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/9/6
 * @since 1.0
 */
public final class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable divider;
    /**
     * 分隔线颜色
     */
    private int color;
    /**
     * 分隔线高度
     */
    private int height;
    private int startPosition;
    private int itemCount;

    /**
     * 构造方法，默认分隔线颜色为灰色（#f1f1f1）且高度为1dp
     */
    public DividerItemDecoration(@NonNull Context context) {
        init(context);
        startPosition = -1;
//        color = ResourceUtil.getColor(R.color.color_f1f1f1);
//        height = ResourceUtil.getDimens(R.dimen.dp_10);
    }

    /**
     * 构造方法，默认分隔线颜色为灰色（#f1f1f1）且高度为1dp
     *
     * @param context       应用程序上下文
     * @param startPosition 起始索引位置
     * @param itemCount     要绘制的item个数
     */
    public DividerItemDecoration(@NonNull Context context, int startPosition, int itemCount) {
        init(context);
        this.startPosition = startPosition;
        this.itemCount = itemCount;
    }

    /**
     * 初始化分隔线Drawable
     *
     * @param context 应用程序上下文
     */
    private void init(Context context) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    /**
     * 构造方法
     *
     * @param height item分隔线的高度
     */
    public DividerItemDecoration(@ColorRes int colorResId, @DimenRes int height) {
        color = ResourceUtil.getColor(colorResId);
        this.height = height;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        d("onDraw...");
//        int offset;
//        final int itemPosition = parent.getChildAdapterPosition(view);
//        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
//        LogUtil.d(TAG, "itemCount = " + itemCount + " , itemPosition = " + itemPosition);
//        if (color == -1)
//            offset = ResourceUtil.getDimens(R.dimen.dp_10);
//        else
//            offset = ResourceUtil.getDimens(margin);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        d("layoutManager ", layoutManager);
        // RecyclerView的布局管理器是线性布局
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            final int orientation = linearLayoutManager.getOrientation();
            // 如果是垂直方向，则除了最后一个item没有下边距，其他的item都有上、下、左、右边距
            if (orientation == LinearLayoutManager.VERTICAL) {
                int start = 0;
                int count = childCount;
                // 指定position绘制范围
                if (startPosition != -1 && itemCount > 0) {
                    start = startPosition;
                    count = itemCount;
                } else if (itemCount < 0) {
                    count = 0;
                }
                d("startPosition", startPosition);
                d("itemCount", itemCount);
                // 在指定的position范围绘制分隔线
                for (int i = start; i < count; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    final int top = child.getBottom() + params.bottomMargin;
                    final int bottom = top + divider.getIntrinsicHeight();
                    divider.setBounds(left, top, right, bottom);
                    divider.draw(c);
                }
            } else {

            }
        } else if ("android.support.v7.widget.GridLayoutManager".equals(layoutManager.getClass().getName())) {

        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        d("getItemOffsets...");
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        d("layoutManager", layoutManager);
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            final int orientation = linearLayoutManager.getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, 0, divider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
            }
        }
    }
}