package com.shyky.library.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import android.util.StateSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.shyky.library.R;
import com.shyky.library.adapter.ArrayAdapter;
import com.shyky.library.util.DensityUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.socks.library.KLog.d;
import static com.socks.library.KLog.e;

/**
 * 扩展系统自带的RecyclerView，实现下拉刷新，下拉加载更多，侧滑菜单，可拖拽等功能
 *
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.6
 * @date 2016/5/18
 * @email sj1510706@163.com
 * @since 1.0
 */
public class SmartRecyclerView extends RecyclerView {
    /**
     * Indicates that we are not in the middle of a touch gesture
     */
    public static final int TOUCH_MODE_REST = -1;
    /**
     * Indicates we just received the touch event and we are waiting to see if the it is a tap or a
     * scroll gesture.
     */
    public static final int TOUCH_MODE_DOWN = 1;
    /**
     * Indicates the touch has been recognized as a tap and we are now waiting to see if the touch
     * is a longpress
     */
    public static final int TOUCH_MODE_TAP = 2;
    /**
     * Indicates we have waited for everything we can wait for, but the user's finger is still down
     */
    public static final int TOUCH_MODE_DONE_WAITING = 3;
    /**
     * Indicates the touch gesture is a scroll
     */
    public static final int TOUCH_MODE_SCROLL = 4;
    /**
     * Indicates the view is in the process of being flung
     */
    public static final int TOUCH_MODE_FLING = 5;
    /**
     * Indicates the touch gesture is an overscroll - a scroll beyond the beginning or end.
     */
    public static final int TOUCH_MODE_OVERSCROLL = 6;
    /**
     * Indicates the view is being flung outside of normal content bounds
     * and will spring back.
     */
    public static final int TOUCH_MODE_OVERFLING = 7;
    /**
     * Normal list that does not indicate choices
     */
    public static final int CHOICE_MODE_NONE = 11;
    /**
     * The list allows up to one choice
     */
    public static final int CHOICE_MODE_SINGLE = 12;
    /**
     * The list allows multiple choices
     */
    public static final int CHOICE_MODE_MULTIPLE = 13;
    /**
     * The list allows multiple choices in a modal selection mode
     */
    public static final int CHOICE_MODE_MULTIPLE_MODAL = 14;
    /**
     * item view类型，头部
     */
    private final static int ITEM_TYPE_HEADER = 21;
    /**
     * item view类型，底部
     */
    private final static int ITEM_TYPE_FOOTER = 22;
    /**
     * 是否允许添加头部view，默认可以添加
     */
    private boolean isHeaderEnable;
    /**
     * 是否允许添加底部view，默认可以添加
     */
    private boolean isFooterEnable;
    /**
     * 实现了头部和底部的adapter
     */
    private InternalAdapter internalAdapter;
    /**
     * Our height after the last layout
     */
    private int layoutHeight;
    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private boolean isLoadingMore;
    /**
     * 是否第一次加载显示Loading
     */
    private boolean isFirstLoadGone;
    /**
     * 标记加载更多的position
     */
    private int loadMorePosition;
    /**
     * 加载更多的监听-业务需要实现加载数据
     */
    private OnRecycleViewLoadMoreListener onRecycleViewLoadMoreListener;
    /**
     * 是否预加载
     */
    private boolean isPreLoading;
    //    private boolean loadMoreFinished = true;
    /**
     * 是否能加载更多,仅跟踪RecycleView滚动方向
     */
    private boolean canLoadMore;
    /**
     * The adapter containing the data to be displayed by this view
     */
    private Adapter adapter;
    private InternalAdapterDataObserver adapterDataObserver;
    private ArrayList<FixedViewInfo> headerViews;
    private ArrayList<FixedViewInfo> footerViews;
    private boolean headerDividersEnabled;
    private boolean footerDividersEnabled;
    /**
     * The select child's view (from the adapter's getView) is enabled.
     */
    private boolean isChildViewEnabled;
    /**
     * 分隔线颜色或图片资源
     */
    private Drawable divider;
    /**
     * 分隔线颜色
     */
    private int dividerColor;
    /**
     * 分隔线高度
     */
    private int dividerHeight;
    /**
     * 分隔线宽度
     */
    private float dividerWidth;
    /**
     * 分隔线是否不透明
     */
    private boolean dividerIsOpaque;
    /**
     * The drawable used to draw the selector
     */
    private Drawable selector;
    /**
     * The selection's left padding
     */
    private int selectionLeftPadding;
    /**
     * The selection's top padding
     */
    private int selectionTopPadding;
    /**
     * The selection's right padding
     */
    private int selectionRightPadding;
    /**
     * The selection's bottom padding
     */
    private int selectionBottomPadding;
    private int requestedColumnWidth;
    private int columnWidth;
    /**
     * Controls if/how the user may choose/check items in the list
     */
    /**
     * 默认为{@link #CHOICE_MODE_NONE}
     */
    private int choiceMode;
    /**
     * One of TOUCH_MODE_REST, TOUCH_MODE_DOWN, TOUCH_MODE_TAP, TOUCH_MODE_SCROLL, or
     * TOUCH_MODE_DONE_WAITING
     */
    private int touchMode = TOUCH_MODE_REST;
    /**
     * Running count of how many items are currently checked
     */
    private int checkedItemCount;
    /**
     * Controls CHOICE_MODE_MULTIPLE_MODAL. null when inactive.
     */
    private ActionMode choiceActionMode;
    /**
     * Running state of which positions are currently checked
     */
    private SparseBooleanArray checkStates;
    /**
     * Running state of which IDs are currently checked.
     * If there is a value for a given key, the checked state for that ID is true
     * and the value holds the last known position in the adapter for that id.
     */
    private LongSparseArray<Integer> checkedIdStates;
    /**
     * 默认为true
     */
    private boolean areAllItemsSelectable;
    /**
     * True if the data has changed since the last layout
     */
    private boolean dataChanged;
    /**
     * RecyclerView第一次加载数据时显示的View
     */
    private View loadingView;
    /**
     * View to show if there are no items to show.
     */
    /**
     * RecyclerView没有item时显示的View
     */
    private View emptyView;
    /**
     * RecyclerView加载数据错误时显示的View
     */
    private View errorView;
    /**
     * RecyclerView加载数据没有网络时显示的View
     */
    private View noNetworkView;
    private List<Integer> ignorePositions;
    /**
     * The listener that receives notifications when an item is clicked.
     */
    private OnItemClickListener onItemClickListener;
    private OnItemSlideListener onItemSlideListener;
    private ItemTouchHelper.Callback itemTouchCallback;
    private RecyclerView.ItemDecoration dividerItemDecoration = new ItemDecoration() {
        @Override
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            super.onDraw(c, parent, state);
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() + parent.getPaddingLeft();
            Paint paint = new Paint();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                paint.setStrokeWidth(DensityUtil.dip2px(dividerHeight));
                paint.setColor(getContext().getResources().getColor(R.color.color_e1e1e1));
                c.drawLine(left, top, right, top, paint);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, DensityUtil.dip2px(dividerHeight));
        }
    };

    /**
     * A class that represents a fixed view in a list, for example a header at the top
     * or a footer at the bottom.
     */
    public class FixedViewInfo {
        /**
         * The view to add to the list
         */
        public View view;
        /**
         * The data backing the view. This is returned from {@link ListAdapter#getItem(int)}.
         */
        public Object data;
        /**
         * <code>true</code> if the fixed view should be selectable in the list
         */
        public boolean isSelectable;
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent   The AdapterView where the click happened.
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been slided.
     */
    public interface OnItemSlideListener {
        void onItemSlide(int orientation, int position, long id);
    }

    /**
     * 加载更多监听
     */
    public interface OnRecycleViewLoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    private class InternalAdapterDataObserver extends AdapterDataObserver {
        private AdapterDataObserver viewDataObserver;

        public Field getDeclaredField(Class clazz, String fieldName) {
            Field field;
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field;
                } catch (Exception e) {
                    e(e.toString());
                }
            }
            return null;
        }

        public InternalAdapterDataObserver() {
            try {
                //                //通过反射获取父类观察者对象
                //                Field declaredField = RecyclerView.class.getDeclaredField("mObserver");
                //                declaredField.setAccessible(true);

                Field declaredField = getDeclaredField(RecyclerView.class, "mObserver");
                if (declaredField != null) {
                    viewDataObserver = (AdapterDataObserver) declaredField.get(SmartRecyclerView.this);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onChanged() {
            if (viewDataObserver != null) {
                viewDataObserver.onChanged();
            }
            setLoadMoreFinish(isFooterEnable);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (viewDataObserver != null) {
                viewDataObserver.onItemRangeChanged(positionStart, itemCount);
            }
            setLoadMoreFinish(isFooterEnable);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (viewDataObserver != null) {
                viewDataObserver.onItemRangeChanged(positionStart, itemCount, payload);
            }
            setLoadMoreFinish(isFooterEnable);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (viewDataObserver != null) {
                viewDataObserver.onItemRangeInserted(positionStart, itemCount);
            }
            setLoadMoreFinish(isFooterEnable);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (viewDataObserver != null) {
                viewDataObserver.onItemRangeRemoved(positionStart, itemCount);
            }
            setLoadMoreFinish(isFooterEnable);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (viewDataObserver != null) {
                viewDataObserver.onItemRangeMoved(fromPosition, toPosition, itemCount);
            }
            setLoadMoreFinish(isFooterEnable);
        }
    }

    private class InternalAdapter extends Adapter<ViewHolder> {
        /**
         * 数据adapter
         */
        private Adapter internalAdapter;
        private HeaderViewHolder headerViewHolder;
        private FooterViewHolder footerViewHolder;

        private class HeaderViewHolder extends ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                    params.setFullSpan(true);
                    itemView.setLayoutParams(layoutParams);
                }
            }
        }

        private class FooterViewHolder extends ViewHolder {
//            public final View loadMoreView;

            public FooterViewHolder(@NonNull View itemView) {
                super(itemView);
//                loadMoreView = itemView.findViewById(resId);
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                    params.setFullSpan(true);
                    itemView.setLayoutParams(layoutParams);
                }
                if (isFirstLoadGone) {
                    itemView.setVisibility(GONE);
                } else {
                    itemView.setVisibility(VISIBLE);
                }
            }
        }

        public InternalAdapter(Adapter adapter) {
            internalAdapter = adapter;
            isHeaderEnable = false;
        }

        @Override
        public int getItemViewType(int position) {
            int headerPosition = 0;
            int footerPosition = getItemCount() - 1;
            if (headerPosition == position && isHeaderEnable && headerViewHolder != null) {
                return ITEM_TYPE_HEADER;
            } else if (footerPosition == position && isFooterEnable) {
                return ITEM_TYPE_FOOTER;
            } else {
                return internalAdapter.getItemViewType(position);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case ITEM_TYPE_HEADER:
                    return headerViewHolder;
                case ITEM_TYPE_FOOTER:
                    if (footerViewHolder == null) {
                        footerViewHolder = new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                    }
                    return footerViewHolder;
                default:
                    return internalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type != ITEM_TYPE_FOOTER && type != ITEM_TYPE_HEADER) {
                if (isHeaderEnable) {
                    --position;
                }
                internalAdapter.onBindViewHolder(holder, position);
            }
        }

        public Adapter getInternalAdapter() {
            return internalAdapter;
        }

        @Override
        public int getItemCount() {
            // 需要计算上加载更多和添加的头部俩个
            int count = internalAdapter.getItemCount();
            if (isFooterEnable)
                count++;
            else if (isHeaderEnable)
                count++;
            return count;
        }

        //        public void setHeaderEnable(boolean enable) {
//            mIsHeaderEnable = enable;
//        }
//
        public void addHeaderView(@LayoutRes int resId) {
            headerViewHolder = new HeaderViewHolder(LayoutInflater.from(getContext()).inflate(resId, null));
        }

        public void addHeaderView(@NonNull View view) {
            headerViewHolder = new HeaderViewHolder(view);
        }

        public void addFooterView(@LayoutRes int resId) {
            footerViewHolder = new FooterViewHolder(View.inflate(getContext(), resId, null));
        }

        public void addFooterView(@NonNull View view) {
            footerViewHolder = new FooterViewHolder(view);
        }

        public void showFooterView() {
//            if (footerViewHolder != null && footerViewHolder.loadMoreLayout != null) {
//                footerViewHolder.loadMoreLayout.setVisibility(VISIBLE);
//            }
        }

        public void hideFooterView() {
//            if (footerViewHolder != null && footerViewHolder.loadMoreLayout != null /*&& mFooterViewHolder.llLoadMore.getVisibility() == VISIBLE*/) {
//                footerViewHolder.loadMoreLayout.setVisibility(GONE);
//            }
        }
    }

    public SmartRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public SmartRecyclerView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartRecyclerView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartRecyclerView, defStyle, defStyle);
        final CharSequence[] entries = typedArray.getTextArray(R.styleable.SmartRecyclerView_android_entries);
        if (entries != null) {
            setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, entries));
        }

        dividerWidth = typedArray.getDimension(R.styleable.SmartRecyclerView_dividerWidth, 0.0f);
        final Drawable selector = typedArray.getDrawable(R.styleable.SmartRecyclerView_android_listSelector);
        if (selector != null) {
            setSelector(selector);
        }
        setChoiceMode(typedArray.getInt(R.styleable.SmartRecyclerView_android_choiceMode, CHOICE_MODE_NONE));
        final Drawable divider = typedArray.getDrawable(R.styleable.SmartRecyclerView_android_divider);
        if (divider != null) {
            // Use an implicit divider height which may be explicitly
            // overridden by android:dividerHeight further down.
            setDivider(divider);
        }
        // Use an explicit divider height, if specified.
//        if (typedArray.hasValueOrEmpty(R.styleable.SmartRecyclerView_android_dividerHeight)) {
            /*final int dividerHeight =*/
        dividerHeight = typedArray.getDimensionPixelSize(R.styleable.SmartRecyclerView_android_dividerHeight, 0);
        if (dividerHeight != 0) {
            setDividerHeight(dividerHeight);
        }
//        }
        headerDividersEnabled = typedArray.getBoolean(R.styleable.SmartRecyclerView_android_headerDividersEnabled, true);
        footerDividersEnabled = typedArray.getBoolean(R.styleable.SmartRecyclerView_android_footerDividersEnabled, true);
        // 回收
        typedArray.recycle();

        // 初始化成员变量及事件监听处理
        init();
    }

    /**
     * 初始化成员变量及事件监听处理
     */
    private void init() {
        headerViews = new ArrayList<>();
        footerViews = new ArrayList<>();
        ignorePositions = new ArrayList<>();
        areAllItemsSelectable = true;
        choiceMode = CHOICE_MODE_NONE;
        adapterDataObserver = new InternalAdapterDataObserver();

        addItemDecoration(dividerItemDecoration);

        // 添加滚动事件监听
        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //                //防止第一行到顶部有空白区域
                //                if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                //                    ((StaggeredGridLayoutManager) getLayoutManager()).invalidateSpanAssignments();
                //
                //                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                d("onScrolled -> dx", dx + " , dy = " + dy);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    if (!recyclerView.canScrollVertically(-1)) {
                        internalAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                if (null != onRecycleViewLoadMoreListener && isFooterEnable && !isLoadingMore && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition();
                    d("lastVisiblePosition", lastVisiblePosition);
                    if (lastVisiblePosition + 2 == internalAdapter.getItemCount()) {
                        d("提前显示Loading，只显示，不加载数据...");
                        internalAdapter.showFooterView();
                    }
                    if (lastVisiblePosition + 1 == internalAdapter.getItemCount() || isPreLoading && lastVisiblePosition == internalAdapter.getItemCount() - 10) {
                        setLoadingMore(true);
                        loadMorePosition = lastVisiblePosition;
                        onRecycleViewLoadMoreListener.onLoadMore();
                    }
                }
            }
        });

        // item触摸事件回调
        itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (!ignorePositions.isEmpty()) {
                    for (int i : ignorePositions) {
                        if (i == viewHolder.getAdapterPosition()) {
                            return;
                        }
                    }
                }
                if (onItemSlideListener != null) {
                    onItemSlideListener.onItemSlide(ItemTouchHelper.LEFT, viewHolder.getAdapterPosition(), 0L);
                } else {
                    e("you forget the initialize OnLoadMoreListener");
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (!ignorePositions.isEmpty()) {
                    for (int i : ignorePositions) {
                        if (i == viewHolder.getAdapterPosition()) {
                            return;
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }
        };
    }

    public void setPreLoading(boolean isPreLoading) {
        this.isPreLoading = isPreLoading;
    }

    public void setFirstLoadGone(boolean firstLoadGone) {
        isFirstLoadGone = firstLoadGone;
    }

    public void setSelector(@NonNull Drawable selector) {
        if (selector != null) {
            selector.setCallback(null);
            unscheduleDrawable(selector);
        }
        this.selector = selector;
        Rect padding = new Rect();
        selector.getPadding(padding);
        selectionLeftPadding = padding.left;
        selectionTopPadding = padding.top;
        selectionRightPadding = padding.right;
        selectionBottomPadding = padding.bottom;
        selector.setCallback(this);
        updateSelectorState();
    }

    /**
     * Indicates whether this view is in a state where the selector should be drawn. This will
     * happen if we have focus but are not in touch mode, or we are in the middle of displaying
     * the pressed state for an item.
     *
     * @return True if the selector should be shown
     */
    private boolean shouldShowSelector() {
        return (isFocused() && !isInTouchMode()) || (touchModeDrawsInPressedState() && isPressed());
    }

    /**
     * @return True if the current touch mode requires that we draw the selector in the pressed
     * state.
     */
    boolean touchModeDrawsInPressedState() {
        // FIXME use isPressed for this
        switch (touchMode) {
            case TOUCH_MODE_TAP:
            case TOUCH_MODE_DONE_WAITING:
                return true;
            default:
                return false;
        }
    }

    private void updateSelectorState() {
        final Drawable selector = this.selector;
        if (selector != null && selector.isStateful()) {
            if (shouldShowSelector()) {
                if (selector.setState(getDrawableStateForSelector())) {
                    invalidateDrawable(selector);
                }
            } else {
                selector.setState(StateSet.NOTHING);
            }
        }
    }

    private int[] getDrawableStateForSelector() {
        // If the child view is enabled then do the default behavior.
        if (isChildViewEnabled) {
            // Common case
            return super.getDrawableState();
        }

        // The selector uses this View's drawable state. The selected child view
        // is disabled, so we need to remove the enabled state from the drawable
        // states.
        final int enabledState = ENABLED_STATE_SET[0];

        // If we don't have any extra space, it will return one of the static
        // state arrays, and clearing the enabled state on those arrays is a
        // bad thing! If we specify we need extra space, it will create+copy
        // into a new array that is safely mutable.
        final int[] state = onCreateDrawableState(1);

        int enabledPos = -1;
        for (int i = state.length - 1; i >= 0; i--) {
            if (state[i] == enabledState) {
                enabledPos = i;
                break;
            }
        }

        // Remove the enabled state
        if (enabledPos >= 0) {
            System.arraycopy(state, enabledPos + 1, state, enabledPos,
                    state.length - enabledPos - 1);
        }

        return state;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            internalAdapter = new InternalAdapter(adapter);
            try {
                adapter.unregisterAdapterDataObserver(adapterDataObserver);
            } catch (Exception e) {
                e(e.toString());
            }
            adapter.registerAdapterDataObserver(adapterDataObserver);
        }
        super.swapAdapter(internalAdapter, true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutHeight = getHeight();
    }

    public Adapter getInternalAdapter() {
        return internalAdapter != null ? internalAdapter.getInternalAdapter() : null;
    }

    public int getHeaderViewsCount() {
        return headerViews.size();
    }

    public void addHeaderView(@LayoutRes int resId) {
        addHeaderView(View.inflate(getContext(), resId, null));
    }

    public void addHeaderView(int position, @LayoutRes int resId) {
        addHeaderView(View.inflate(getContext(), resId, null));
    }

    public void addHeaderView(@NonNull View view) {
        addHeaderView(view, null, true);
    }

    public void addHeaderView(int position, @NonNull View view) {
        addHeaderView(view, null, true);
    }

    public void addHeaderView(@LayoutRes int resId, Object data, boolean isSelectable) {
        addHeaderView(View.inflate(getContext(), resId, null), null, true);
    }

    public void addHeaderView(@NonNull View view, Object data, boolean isSelectable) {
        final FixedViewInfo fixedView = new FixedViewInfo();
        fixedView.view = view;
        fixedView.data = data;
        fixedView.isSelectable = isSelectable;
        headerViews.add(fixedView);
        areAllItemsSelectable &= isSelectable;

        // Wrap the adapter if it wasn't already wrapped.
//        if (mAdapter != null) {
//            if (!(mAdapter instanceof HeaderViewListAdapter)) {
//                wrapHeaderListAdapterInternal();
//            }
//
//            // In the case of re-adding a header view, or adding one later on,
//            // we need to notify the observer.
//            if (mDataSetObserver != null) {
//                mDataSetObserver.onChanged();
//            }
//       }
    }

    public boolean removeHeaderView(@LayoutRes int resId) {
        return removeHeaderView(View.inflate(getContext(), resId, null));
    }

    /**
     * Removes a previously-added header view.
     *
     * @param view The view to remove
     * @return true if the view was removed, false if the view was not a header
     * view
     */
    public boolean removeHeaderView(@NonNull View view) {
        if (headerViews.size() > 0) {
            boolean result = false;
//            if (mAdapter != null && ((HeaderViewListAdapter) mAdapter).removeHeader(view)) {
//                if (mDataSetObserver != null) {
//                    mDataSetObserver.onChanged();
//                }
//                result = true;
//            }
            removeFixedViewInfo(view, headerViews);
            return result;
        }
        return false;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            FixedViewInfo info = where.get(i);
            if (info.view == v) {
                where.remove(i);
                break;
            }
        }
    }

    public int getFooterViewsCount() {
        return footerViews.size();
    }

    /**
     * Add a fixed view to appear at the bottom of the list. If addFooterView is
     * called more than once, the views will appear in the order they were
     * added. Views added using this call can take focus if they want.
     * <p>
     * Note: When first introduced, this method could only be called before
     * setting the adapter with {@link #setAdapter(android.support.v7.widget.RecyclerView.Adapter)}. Starting with
     * {@link android.os.Build.VERSION_CODES#KITKAT}, this method may be
     * called at any time. If the ListView's adapter does not extend
     * {@link HeaderViewListAdapter}, it will be wrapped with a supporting
     * instance of {@link WrapperListAdapter}.
     *
     * @param view The view to add.
     */
    public void addFooterView(@NonNull View view) {
        addFooterView(view, null, true);
    }

    /**
     * Add a fixed view to appear at the bottom of the list. If addFooterView is
     * called more than once, the views will appear in the order they were
     * added. Views added using this call can take focus if they want.
     * <p>
     * Note: When first introduced, this method could only be called before
     * setting the adapter with {@link #setAdapter(android.support.v7.widget.RecyclerView.Adapter)}. Starting with
     * {@link android.os.Build.VERSION_CODES#KITKAT}, this method may be
     * called at any time. If the ListView's adapter does not extend
     * {@link HeaderViewListAdapter}, it will be wrapped with a supporting
     * instance of {@link WrapperListAdapter}.
     *
     * @param v            The view to add.
     * @param data         Data to associate with this view
     * @param isSelectable true if the footer view can be selected
     */
    public void addFooterView(View v, Object data, boolean isSelectable) {
        final FixedViewInfo fixedView = new FixedViewInfo();
        fixedView.view = v;
        fixedView.data = data;
        fixedView.isSelectable = isSelectable;
        footerViews.add(fixedView);
        areAllItemsSelectable &= isSelectable;

//        autoLoadAdapter.addFooterView(fixedView);

        // Wrap the adapter if it wasn't already wrapped.
//        if (mAdapter != null) {
//            if (!(mAdapter instanceof HeaderViewListAdapter)) {
//                wrapHeaderListAdapterInternal();
//            }
//
//            // In the case of re-adding a footer view, or adding one later on,
//            // we need to notify the observer.
//            if (dataSetObserver != null) {
//                mDataSetObserver.onChanged();
//            }
//        }
    }

    /**
     * Sets the drawable that will be drawn between each item in the list.
     * <p>
     * <strong>Note:</strong> If the drawable does not have an intrinsic
     * height, you should also call {@link #setDividerHeight(int)}.
     *
     * @param divider the drawable to use
     * @attr ref R.styleable#ListView_divider
     */
    public void setDivider(@Nullable Drawable divider) {
        if (divider != null) {
            dividerHeight = divider.getIntrinsicHeight();
        } else {
            dividerHeight = 0;
        }
        this.divider = divider;
        dividerIsOpaque = divider == null || divider.getOpacity() == PixelFormat.OPAQUE;
        requestLayout();
        invalidate();
    }

    /**
     * Sets the height of the divider that will be drawn between each item in the list. Calling
     * this will override the intrinsic height as set by {@link #setDivider(Drawable)}
     *
     * @param height The new height of the divider in pixels.
     */
    public void setDividerHeight(int height) {
        dividerHeight = height;
        requestLayout();
        invalidate();
    }

    /**
     * @return The current choice mode
     * @see #setChoiceMode(int)
     */
    public int getChoiceMode() {
        return choiceMode;
    }

    /**
     * Defines the choice behavior for the List. By default, Lists do not have any choice behavior
     * ({@link #CHOICE_MODE_NONE}). By setting the choiceMode to {@link #CHOICE_MODE_SINGLE}, the
     * List allows up to one item to  be in a chosen state. By setting the choiceMode to
     * {@link #CHOICE_MODE_MULTIPLE}, the list allows any number of items to be chosen.
     *
     * @param choiceMode One of {@link #CHOICE_MODE_NONE}, {@link #CHOICE_MODE_SINGLE}, or
     *                   {@link #CHOICE_MODE_MULTIPLE}
     */
    public void setChoiceMode(int choiceMode) {
        this.choiceMode = choiceMode;
        if (choiceActionMode != null) {
            choiceActionMode.finish();
            choiceActionMode = null;
        }
        if (choiceMode != CHOICE_MODE_NONE) {
            if (checkStates == null) {
                checkStates = new SparseBooleanArray(0);
            }
            if (checkedIdStates == null && adapter != null && adapter.hasStableIds()) {
                checkedIdStates = new LongSparseArray<>(0);// new LongSparseArray<Integer>(0);
            }
            // Modal multi-choice mode only has choices when the mode is active. Clear them.
            if (choiceMode == CHOICE_MODE_MULTIPLE_MODAL) {
                clearChoices();
                setLongClickable(true);
            }
        }
    }

    /**
     * Clear any choices previously set
     */
    public void clearChoices() {
        if (checkStates != null) {
            checkStates.clear();
        }
        if (checkedIdStates != null) {
            checkedIdStates.clear();
        }
        checkedItemCount = 0;
    }

    /**
     * Return the requested width of a column in the grid.
     * <p>
     * <p>This may not be the actual column width used. Use {@link #getColumnWidth()}
     * to retrieve the current real width of a column.</p>
     *
     * @return The requested column width in pixels
     * @attr ref android.R.styleable#GridView_columnWidth
     * @see #setColumnWidth(int)
     * @see #getColumnWidth()
     */
    public int getRequestedColumnWidth() {
        return requestedColumnWidth;
    }

    /**
     * Set the width of columns in the grid.
     *
     * @param columnWidth The column width, in pixels.
     * @attr ref android.R.styleable#GridView_columnWidth
     */
    public void setColumnWidth(int columnWidth) {
        if (columnWidth != requestedColumnWidth) {
            requestedColumnWidth = columnWidth;
            //requestLayoutIfNecessary();
        }
    }

    /**
     * Return the width of a column in the grid.
     * <p>
     * <p>This may not be valid yet if a layout is pending.</p>
     *
     * @return The column width in pixels
     * @attr ref android.R.styleable#GridView_columnWidth
     * @see #setColumnWidth(int)
     * @see #getRequestedColumnWidth()
     */
    public int getColumnWidth() {
        return columnWidth;
    }

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been slided.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemSlideListener(@NonNull @Nullable OnItemSlideListener listener) {
        onItemSlideListener = listener;
    }

    /**
     * Indicates whether this view is in filter mode. Filter mode can for instance
     * be enabled by a user when typing on the keyboard.
     *
     * @return True if the view is in filter mode, false otherwise.
     */
    boolean isInFilterMode() {
        return false;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public <VIEW extends View> VIEW findViewByIdFromLoadingView(@IdRes int resId) {
        return (VIEW) loadingView.findViewById(resId);
    }

    public void setLoadingView(@LayoutRes int resId) {
        setLoadingView(View.inflate(getContext(), resId, null));
    }

    public void setLoadingView(@NonNull View loadingView) {
        this.loadingView = loadingView;
    }

    /**
     * When the current adapter is empty, the AdapterView can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return emptyView;
    }

    public <VIEW extends View> VIEW findViewByIdFromEmptyView(@IdRes int resId) {
        return (VIEW) emptyView.findViewById(resId);
    }

    public void setEmptyView(@LayoutRes int resId) {
        setEmptyView(View.inflate(getContext(), resId, null));
    }

    /**
     * Sets the view to show if the adapter is empty
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setEmptyView(@NonNull View emptyView) {
        this.emptyView = emptyView;

        // If not explicitly specified this view is important for accessibility.
        if (emptyView != null
                && emptyView.getImportantForAccessibility() == IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            emptyView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

        final Adapter adapter = getAdapter();
        final boolean empty = ((adapter == null) || adapter.getItemCount() == 0);
        updateEmptyStatus(empty);
    }

    /**
     * Update the status of the list based on the empty parameter.  If empty is true and
     * we have an empty view, display it.  In all the other cases, make sure that the listview
     * is VISIBLE and that the empty view is GONE (if it's not null).
     */
    private void updateEmptyStatus(boolean empty) {
        if (isInFilterMode()) {
            empty = false;
        }

        if (empty) {
            if (emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                // If the caller just removed our empty view, make sure the list view is visible
                setVisibility(View.VISIBLE);
            }

            // We are now GONE, so pending layouts will not be dispatched.
            // Force one here to make sure that the state of the list matches
            // the state of the adapter.
            if (dataChanged) {
//                this.onLayout(false, mLeft, mTop, mRight, mBottom);
                this.layout(0, 0, 0, 0);
            }
        } else {
            if (emptyView != null) emptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    public void setErrorView(@LayoutRes int resId) {
        setErrorView(View.inflate(getContext(), resId, null));
    }

    public void setErrorView(@NonNull View errorView) {
        this.errorView = errorView;
    }

    public void setNoNetworkView(@LayoutRes int resId) {
        setNoNetworkView(View.inflate(getContext(), resId, null));
    }

    public void setNoNetworkView(@NonNull View noNetworkView) {
        this.noNetworkView = noNetworkView;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        //        if (layout instanceof StaggeredGridLayoutManager) {
        //            ((StaggeredGridLayoutManager) layout).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        //        }
        super.setLayoutManager(layout);
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    int firstPosition = -1;

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    private int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] firstPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPosition(firstPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最小的位置
     *
     * @param positions
     * @return
     */
    private int getMinPosition(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int position : positions) {
            minPosition = Math.min(minPosition, position);
        }
        return minPosition;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

//    /**
//     * 设置头部view是否展示
//     *
//     * @param enable
//     */
//    public void setHeaderEnable(boolean enable) {
//        mAutoLoadAdapter.setHeaderEnable(enable);
//    }
//

    /**
     * @return Whether the drawing of the divider for header views is enabled
     * @see #setHeaderDividersEnabled(boolean)
     */
    public boolean areHeaderDividersEnabled() {
        return headerDividersEnabled;
    }

    /**
     * Enables or disables the drawing of the divider for header views.
     *
     * @param headerDividersEnabled True to draw the headers, false otherwise.
     * @see #setFooterDividersEnabled(boolean)
     * @see #areHeaderDividersEnabled()
     * @see #addHeaderView(android.view.View)
     */
    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        this.headerDividersEnabled = headerDividersEnabled;
        invalidate();
    }

    /**
     * @return Whether the drawing of the divider for footer views is enabled
     * @see #setFooterDividersEnabled(boolean)
     */
    public boolean areFooterDividersEnabled() {
        return footerDividersEnabled;
    }

    /**
     * Enables or disables the drawing of the divider for footer views.
     *
     * @param footerDividersEnabled True to draw the footers, false otherwise.
     * @see #setHeaderDividersEnabled(boolean)
     * @see #areFooterDividersEnabled()
     * @see #addFooterView(android.view.View)
     */
    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        this.footerDividersEnabled = footerDividersEnabled;
        invalidate();
    }

    /**
     * 设置是否支持自动加载更多
     *
     * @param autoLoadMore
     */
    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
        isFooterEnable = autoLoadMore;
    }

    /**
     * 设置正在加载更多
     *
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        this.isLoadingMore = loadingMore;
    }

    /**
     * 设置加载更多的监听
     *
     * @param onRecycleViewLoadMoreListener
     */
    public void setOnRecycleViewLoadMoreListener(@NonNull OnRecycleViewLoadMoreListener onRecycleViewLoadMoreListener) {
        this.onRecycleViewLoadMoreListener = onRecycleViewLoadMoreListener;
    }

    public void setLoadMoreFinish(boolean hasMore) {
        setAutoLoadMoreEnable(hasMore);
        if (internalAdapter != null) {
            //            loadMoreFinished = false;
            internalAdapter.hideFooterView();
        }
        isLoadingMore = false;
    }

    public boolean isIsFooterEnable() {
        return isFooterEnable;
    }

    public boolean canViewScrollUp() {
        try {
            if (getAdapter() != null) {
                int count = 0;
                count += (isIsFooterEnable() ? 1 : 0);
                count += ((isHeaderEnable) ? 1 : 0);
                if (getAdapter().getItemCount() - count <= 0) {
                    return false;
                }
            }
            final RecyclerView.LayoutManager layoutManager = getLayoutManager();
            firstPosition = 0;
            if (layoutManager instanceof LinearLayoutManager) {
                firstPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                firstPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] mFirstVisible = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
                firstPosition = mFirstVisible[0];
            }
            final int firstPosition = this.firstPosition;// getFirstVisiblePosition();
            return firstPosition != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}