package com.shyky.library.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shyky.library.BaseApplication;
import com.shyky.library.bean.base.BaseBean;

import java.util.List;

/**
 * 基础的ListView或GridView数据适配器，Google官方已经不建议使用ListView，这里为了兼容以前的代码而设计
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/3/9
 * @since 1.0
 */
public abstract class BaseListViewAdapter<VH, ENTITY extends BaseBean> extends BaseAdapter {
    private List<ENTITY> data;
    private View inflateView;

    @LayoutRes
    public abstract int getViewLayoutResId();

    @NonNull
    public abstract VH getViewHolder();

    //    public abstract View getView(int position, View convertView, View inflateView);
    public abstract View getView(int position, @NonNull View convertView, @NonNull VH viewHolder);

    /**
     * 构造方法
     */
    protected BaseListViewAdapter() {

    }

    /**
     * 构造方法
     */
    protected BaseListViewAdapter(@NonNull Context context) {

    }

    @Override
    public int getCount() {
        if (data != null && !data.isEmpty())
            return data.size();
        return 0;
    }

    @Override
    public ENTITY getItem(int position) {
        return data.get(position);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup viewGroup) {
        if (getViewLayoutResId() > 0) {
            inflateView = LayoutInflater.from(BaseApplication.getContext()).inflate(getViewLayoutResId(), null); // 不能用viewGroup);
            convertView = inflateView;
            if (convertView == null) {
                if (getViewHolder() != null)
                    convertView.setTag(getViewHolder());
            } else {
                convertView.getTag();
            }
            return getView(position, inflateView, getViewHolder());
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final ENTITY remove(int position) {
        ENTITY result;
        try {
            result = data.remove(position);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public final void setData(List<ENTITY> data) {
        this.data = null;
        this.data = data;
        notifyDataSetChanged();
    }

    public final <VIEW> VIEW getItemView(@IdRes int viewId) {
        return (VIEW) inflateView.findViewById(viewId);
    }

    public final void setText(int position, @IdRes int viewId, String text) {
        final TextView textView = getItemView(viewId);
        if (textView != null)
            textView.setText(text);
    }

    public final void setText(int position, @IdRes int viewId, @StringRes int resId) {
        final TextView textView = getItemView(viewId);
        if (textView != null)
            textView.setText(resId);
    }

    public final void setTextColor(int position, @IdRes int viewId, @ColorRes int color) {
        final TextView textView = getItemView(viewId);
        if (textView != null)
            textView.setTextColor(BaseApplication.getContext().getResources().getColor(color));
    }

    public final void setBackgroundResource(int position, @IdRes int viewId, @DrawableRes int resId) {
        final ImageView imageView = getItemView(viewId);
        if (imageView != null)
            imageView.setBackgroundResource(resId);
    }

    public final void setImageBitmap(int position, @IdRes int viewId, @NonNull Bitmap bitmap) {
        final ImageView imageView = getItemView(viewId);
        if (imageView != null)
            imageView.setImageBitmap(bitmap);
    }

    public final void setImageResource(int position, @IdRes int viewId, @DrawableRes int resId) {
        final ImageView imageView = getItemView(viewId);
        if (imageView != null)
            imageView.setImageResource(resId);
    }

    public final void setImageDrawable(int position, @IdRes int viewId, @NonNull Drawable drawable) {
        final ImageView imageView = getItemView(viewId);
        if (imageView != null)
            imageView.setImageDrawable(drawable);
    }
}