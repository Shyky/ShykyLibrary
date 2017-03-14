package com.shyky.library.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shyky.library.util.LayoutUtil;
import com.shyky.library.util.ResourceUtil;
import com.shyky.util.ListUtil;
import com.shyky.util.ObjectUtil;
import com.shyky.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

import static com.socks.library.KLog.d;
import static com.socks.library.KLog.e;

/**
 * 通用的RecyclerView Adapter
 *
 * @param <ENTITY> 泛型参数，数据源集合中的实体
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.6
 * @email sj1510706@163.com
 * @date 2016/3/31
 * @since 1.0
 */
public abstract class BaseRecyclerViewAdapter<ENTITY> extends RecyclerView.Adapter {
    /**
     * 应用程序上下文
     */
    private Context context;
    /**
     * 数据源
     */
    private List<ENTITY> data;
    /**
     * 保存Item view type类型集合
     */
    private List<Integer> viewTypes;
    /**
     * Item点击事件回调
     */
    private OnItemClickListener onItemClickListener;
    /**
     * Item View里的某个view的点击事件回调
     */
    private OnItemViewClickListener onItemViewClickListener;
    /**
     * Item长按事件回调
     */
    private OnItemLongClickListener onItemLongClickListener;
    /**
     * Item View里的某个view的长按事件回调
     */
    private OnItemViewLongClickListener onItemViewLongClickListener;
    /**
     * Lock used to modify the content of {@link #data}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see  to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();
    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private ArrayList<ENTITY> mOriginalValues;
    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #data} is modified.
     */
    private boolean mNotifyOnChange = true;
    /**
     * 当前的item view布局类型
     */
    private int viewType;

    /**
     * Item点击事件监听器
     */
    public interface OnItemClickListener {
        /**
         * 当Item View点击回调方法
         *
         * @param view      被点击的item view
         * @param viewResId item view控件资源ID
         * @param position  item的位置
         */
        void onItemClick(@NonNull View view, @IdRes int viewResId, int position);
    }

    /**
     * Item View中的view点击事件监听器
     */
    public interface OnItemViewClickListener {
        /**
         * 当Item View点击回调方法
         *
         * @param view     被点击的view控件
         * @param position item的位置
         */
        void onClick(@NonNull View view, @IdRes int viewResId, int position);
    }

    /**
     * Item View长按事件监听器
     */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(@NonNull View view, @IdRes int viewResId, int position);
    }

    /**
     * Item View长按事件监听器
     */
    public interface OnItemViewLongClickListener {
        boolean onLongClick(@NonNull View view, @IdRes int viewResId, int position);
    }

    /**
     * 构造方法
     *
     * @param context 应用程序上下文
     */
    protected BaseRecyclerViewAdapter(@NonNull Context context) {
        this.context = context;
        viewTypes = new ArrayList<>();
        data = new ArrayList<>();
    }

    /**
     * 获取Item布局资源文件ID
     *
     * @param viewType item布局类型
     * @return 资源文件ID
     */
    public abstract int getItemLayoutResId(int viewType);

    /**
     * 绑定Item view及设置显示数据
     *
     * @param viewHolder
     * @param viewType
     * @param position
     */
    public abstract void bindViewHolder(@NonNull BaseRecyclerViewHolder viewHolder, int viewType, int position);

    /**
     * 获取item view的布局参数，用于调整item view的布局
     *
     * @param layoutParams 布局参数
     * @param viewType     item view布局类型
     * @return 调整后的布局参数
     */
    public ViewGroup.LayoutParams getItemViewLayoutParams(ViewGroup.LayoutParams layoutParams, int viewType) {
        return null;
    }

    /**
     * 获取要点击的item view里的view的资源ID
     *
     * @return view的资源ID数组
     */
    public int[] getClickItemViewResId() {
        return null;
    }

    /**
     * 内置一个ViewHolder用于包装各个item view控件
     */
    public final class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        /**
         * 保存ViewHolder中的各个View
         */
        private SparseArray<View> views;

        /**
         * 构造方法
         *
         * @param view item view布局
         */
        public BaseRecyclerViewHolder(@NonNull View view) {
            super(view);
            views = new SparseArray<>();
            this.rootView = view;
            rootView.setTag(this);
        }

        protected View getRootView() {
            return rootView;
        }

        public <VIEW extends View> VIEW getView(int viewType, int viewId) {
            // 通过viewType+viewId区分View
            View view = views.get(viewType + viewId);
            if (view == null) {
                view = rootView.findViewById(viewId);
                views.put(viewType + viewId, view);
            }
            d("getView view", view);
            return (VIEW) view;
        }

        public BaseRecyclerViewHolder setVisibility(int viewType, int viewResId, int visibility) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setVisibility(visibility);
            else
                e("找不到id:" + viewResId + "的控件，请检查是否在item view中定义了或有无设置控件ID");
            return this;
        }

        public BaseRecyclerViewHolder setBackgroundColor(int viewType, int viewResId, int color) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setBackgroundColor(color);
            return this;
        }

        public BaseRecyclerViewHolder setBackgroundResource(int viewType, int viewResId, int resId) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setBackgroundResource(resId);
            return this;
        }

        public BaseRecyclerViewHolder setTag(int viewType, @IdRes int viewResId, int key, Object tag) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setTag(key, tag);
            return this;
        }

        public BaseRecyclerViewHolder setLayoutParams(int viewType, @IdRes int viewResId, @NonNull RadioGroup.LayoutParams params) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setLayoutParams(params);
            return this;
        }

        public BaseRecyclerViewHolder setLayoutParams(int viewType, @IdRes int viewResId, @NonNull LinearLayout.LayoutParams params) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setLayoutParams(params);
            return this;
        }

        public BaseRecyclerViewHolder setLayoutParams(int viewType, @IdRes int viewResId, @NonNull RelativeLayout.LayoutParams params) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setLayoutParams(params);
            return this;
        }

        public BaseRecyclerViewHolder setLayoutParams(int viewType, @IdRes int viewResId, @NonNull FrameLayout.LayoutParams params) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setLayoutParams(params);
            return this;
        }

        public BaseRecyclerViewHolder setTag(int viewType, @IdRes int viewResId, @NonNull Object tag) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setTag(tag);
            return this;
        }

        public BaseRecyclerViewHolder setText(@NonNull TextView textView, @NonNull String text) {
            if (textView != null)
                textView.setText(text);
            return this;
        }

        public BaseRecyclerViewHolder setText(@NonNull TextView textView, @StringRes int resId) {
            if (textView != null)
                textView.setText(resId);
            return this;
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, byte value) {
            return setText(viewType, viewResId, TextUtil.toString(value));
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, boolean value) {
            return setText(viewType, viewResId, TextUtil.toString(value));
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, short value) {
            return setText(viewType, viewResId, TextUtil.toString(value));
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, long value) {
            return setText(viewType, viewResId, TextUtil.toString(value));
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, float value) {
            return setText(viewType, viewResId, TextUtil.toString(value));
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, double value) {
            return setText(viewType, viewResId, TextUtil.toString(value));
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, @NonNull String text) {
            final TextView textView = getView(viewType, viewResId);
            if (textView != null)
                textView.setText(text);
            else
                e("找不到id:" + viewResId + "的控件，请检查是否在item view中定义了或有无设置控件ID");
            return this;
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, int resIdOrIntValue) {
            final TextView textView = getView(viewType, viewResId);
            if (textView != null) {
                if (ResourceUtil.exists(resIdOrIntValue))
                    textView.setText(resIdOrIntValue);
                else
                    textView.setText(TextUtil.toString(resIdOrIntValue));
            }
            return this;
        }

        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, @NonNull Spanned html) {
            final TextView textView = getView(viewType, viewResId);
            if (textView != null)
                textView.setText(html);
            return this;
        }

        /**
         * 设置TextView的显示文本及Flags文本效果
         *
         * @param viewType  Item的布局类型
         * @param viewResId 要设置的TextView控件的View ID
         * @param resId     string字符串资源ID
         * @param flags     TextView的Flags，从Paint这个类中取
         * @return BaseRecyclerViewHolder实例对象
         */
        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, @NonNull int resId, int flags) {
            return setText(viewType, viewResId, ResourceUtil.getString(resId), flags);
        }

        /**
         * 设置TextView的显示文本及Flags文本效果
         *
         * @param viewType  Item的布局类型
         * @param viewResId 要设置的TextView控件的View ID
         * @param text      要显示文本
         * @param flags     TextView的Flags，从Paint这个类中取
         * @return BaseRecyclerViewHolder实例对象
         */
        public BaseRecyclerViewHolder setText(int viewType, @IdRes int viewResId, @NonNull String text, int flags) {
            final TextView textView = getView(viewType, viewResId);
            if (textView != null) {
                // textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
                textView.getPaint().setAntiAlias(true); // 抗锯齿
                // Paint.STRIKE_THRU_TEXT_FLAG 中划线
                textView.getPaint().setFlags(flags | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                textView.setText(text);
            }
            return this;
        }

        public BaseRecyclerViewHolder setTextColor(int viewType, @IdRes int viewResId, int color) {
            final TextView textView = getView(viewType, viewResId);
            if (textView != null)
                textView.setTextColor(color);
            return this;
        }

        public BaseRecyclerViewHolder setImageResource(int viewType, @IdRes int viewResId, @DrawableRes int drawableResId) {
            final ImageView imageView = getView(viewType, viewResId);
            if (imageView != null)
                imageView.setImageResource(drawableResId);
            return this;
        }

        public BaseRecyclerViewHolder setImageDrawable(int viewType, @IdRes int viewResId, @NonNull Drawable drawable) {
            final ImageView imageView = getView(viewType, viewResId);
            if (imageView != null)
                imageView.setImageDrawable(drawable);
            return this;
        }

        public BaseRecyclerViewHolder setImageDrawable(int viewType, int viewId, int resId) {
            final ImageView imageView = getView(viewType, viewId);
            if (imageView != null)
                imageView.setImageResource(resId);
            return this;
        }

        public BaseRecyclerViewHolder setImageBitmap(int viewType, int viewId, Bitmap bitmap) {
            final ImageView imageView = getView(viewType, viewId);
            if (imageView != null)
                imageView.setImageBitmap(bitmap);
            return this;
        }

        public BaseRecyclerViewHolder setProgress(int viewType, int viewId, int progress) {
            final ProgressBar progressBar = getView(viewType, viewId);
            if (progressBar != null)
                progressBar.setProgress(progress);
            return this;
        }

        public BaseRecyclerViewHolder setMax(int viewType, int viewId, int max) {
            final ProgressBar progressBar = getView(viewType, viewId);
            if (progressBar != null)
                progressBar.setMax(max);
            return this;
        }

        public BaseRecyclerViewHolder setChecked(int viewType, @IdRes int viewResId, boolean checked) {
            final View view = getView(viewType, viewResId);
            if (ObjectUtil.notNull(view)) {
                if (view instanceof CheckBox) {
                    final CheckBox checkBox = (CheckBox) view;
                    if (ObjectUtil.notNull(checkBox))
                        checkBox.setChecked(checked);
                } else if (view instanceof RadioButton) {
                    final RadioButton radioButton = (RadioButton) view;
                    if (ObjectUtil.notNull(radioButton))
                        radioButton.setChecked(checked);
                }
            }
            return this;
        }

        public final BaseRecyclerViewHolder setRating(int viewType, int viewResId, float rating) {
            final RatingBar ratingBar = getView(viewType, viewResId);
            if (ratingBar != null)
                ratingBar.setRating(rating);
            return this;
        }

        public BaseRecyclerViewHolder setOnClickListener(int viewType, int viewResId, OnClickListener onClickListener) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setOnClickListener(onClickListener);
            return this;
        }

        public BaseRecyclerViewHolder setOnLongClickListener(int viewType, int viewResId, OnLongClickListener onLongClickListener) {
            final View view = getView(viewType, viewResId);
            if (view != null)
                view.setOnLongClickListener(onLongClickListener);
            return this;
        }

        public BaseRecyclerViewHolder setOnCheckedChangeListener(int viewType, int viewResId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            final CheckBox checkBox = getView(viewType, viewResId);
            if (checkBox != null)
                checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
            return this;
        }
    }

    public final void setOnItemClickListener(@NonNull OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public final void setOnItemViewClickListener(@NonNull OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public final void setOnItemLongClickListener(@NonNull OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public final void setOnItemViewLongClickListener(@NonNull OnItemViewLongClickListener onItemViewLongClickListener) {
        this.onItemViewLongClickListener = onItemViewLongClickListener;
    }

    /**
     * 设置数据源
     *
     * @param data 数据源集合
     */
    public final void setData(@NonNull List<ENTITY> data) {
        this.data = data;
    }

    /**
     * 获取指定位置的Item数据源
     *
     * @param position Item的位置
     * @return 如果位置正确返回数据源中的实体，否则返回null
     */
    public ENTITY getItem(int position) {
        int viewTypeCount = viewTypes.size();
        // 如果viewTypeCount大于1说明还有别的item view
        if (viewTypeCount > 1)
            position = (position - viewTypeCount) + 1; // 取出正确的索引，防止越界
        if (data == null || position < 0 || position >= data.size())
            return null;
        return data.get(position);
    }

    /**
     * 获取数据源集合中的item元素
     *
     * @param position item元素索引位置
     * @return 如果位置正确返回数据源中的实体，否则返回null
     */
    public ENTITY getItemData(int position) {
        if (ListUtil.isEmpty(data) || position < 0 && position >= getItemDataCount())
            return null;
        return data.get(position);
    }

    /**
     * 获取Item View type的个数
     *
     * @return item viewType的个数
     */
    public final int getItemViewTypeCount() {
        return viewTypes.size();
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.viewType = viewType;
        // 保存不同的Item view布局类型，需要去重判断，这个方法会多次调用
        if (!viewTypes.contains(viewType))
            viewTypes.add(viewType);
        View inflateView;
        final int layoutResId = getItemLayoutResId(viewType);
        // 使用全局的Context对象会造成UI显示不正常问题
        if (layoutResId > 0) {
            inflateView = LayoutUtil.inflate(context, parent, layoutResId);
            // 用于控制瀑布流布局时，有些Item View需要横向显示，需要子类去根据需求设置参数，让Item View横向全屏显示
            final ViewGroup.LayoutParams layoutParams = getItemViewLayoutParams(inflateView.getLayoutParams(), viewType);
            if (layoutParams != null)
                inflateView.setLayoutParams(layoutParams);
            return new BaseRecyclerViewHolder(inflateView);
        }
        return null;
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final View rootView = ((BaseRecyclerViewHolder) holder).getRootView();
        if (rootView != null) {
            rootView.setTag(position);
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, v.getId(), (int) v.getTag());
                    }
                }
            });
            rootView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, v.getId(), (int) v.getTag());
                    return false;
                }
            });

            // 处理item view中的view的点击事件
            final int[] viewResIds = getClickItemViewResId();
            if (ObjectUtil.notNull(viewResIds)) {
                final ClickListener clickListener = new ClickListener(position);
                for (int resId : viewResIds)
                    ((BaseRecyclerViewHolder) holder).setOnClickListener(viewType, resId, clickListener);
            }
        }
        bindViewHolder((BaseRecyclerViewHolder) holder, getItemViewType(position), position);
    }

    private class ClickListener implements OnClickListener {
        private int position;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            onItemViewClickListener.onClick(view, view.getId(), position);
        }
    }

    /**
     * 获取数据源大小
     *
     * @return 数据源大小
     */
    public final int getItemDataCount() {
        return data == null ? 0 : data.size();
    }

    /**
     * 获取数据源里的数据
     *
     * @param position 位置
     * @return 位置正确返回实体，否则返回null
     */
    public final ENTITY getDataItem(int position) {
        if (position >= 0 && position < getItemDataCount())
            return data.get(position);
        return null;
    }

    public final boolean isEmpty() {
        return ObjectUtil.isNull(data) ? true : data.isEmpty();
    }


    /**
     * @return true if this adapter doesn't contain any data.  This is used to determine
     * whether the empty view should be displayed.  A typical implementation will return
     * getCount() == 0 but since getCount() includes the headers and footers, specialized
     * adapters might want a different behavior.
     */

    public final void add(@NonNull ENTITY entity) {
        if (!isEmpty() && ObjectUtil.notNull(entity)) {
            data.add(entity);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取数据源集合
     *
     * @return 数据源集合
     */
    @NonNull
    public List<ENTITY> getData() {
        return data;
    }

    /**
     * 设置指定位置的item实体
     *
     * @param position
     * @param entity
     */
    public final void set(int position, @NonNull ENTITY entity) {
        if (position >= 0 && position < getItemDataCount() && ObjectUtil.notNull(entity)) {
            data.set(position, entity);
            notifyItemChanged(position);
        }
    }

    public final void addAll(@NonNull List<ENTITY> list) {
        if (ObjectUtil.notNull(data) && ObjectUtil.notNull(list)) {
            data.addAll(list);
            notifyDataSetChanged();
        }
    }

    public final void insert(int index, @NonNull ENTITY entity) {
        if (index >= 0 && index < getItemDataCount() && ObjectUtil.notNull(entity)) {
            data.add(index, entity);
            notifyItemInserted(index);
        }
    }

    public final void remove(@NonNull ENTITY entity) {
        if (!isEmpty() && ObjectUtil.notNull(entity)) {
            data.remove(entity);
            notifyDataSetChanged();
        }
    }

    public final void remove(int position) {
        if (position >= 0 && position < getItemDataCount()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public final void clear() {
        if (ObjectUtil.notNull(data)) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        final int count = viewTypes.size();
        // count为1说明只有列表Item，否则需要减去1，因为列表类型也算进去了
        // 解决count为0时不会显示item view问题
        return ((count == 1 || count == 0) ? 0 : count - 1) + (data == null || data.isEmpty() ? 0 : data.size());
    }
}