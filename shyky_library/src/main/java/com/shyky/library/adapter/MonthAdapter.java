package com.shyky.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.shyky.library.R;
import com.shyky.library.adapter.base.BaseListViewAdapter;
import com.shyky.library.bean.MonthBean;
import com.shyky.library.util.ResourceUtil;
import com.shyky.util.TextUtil;

/**
 * 日期选择器的数据适配器
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/6/23
 * @since 1.0
 */
public final class MonthAdapter extends BaseListViewAdapter<MonthAdapter.ViewHolder, MonthBean> {
    /**
     * 构造方法
     *
     * @param context Log日志标签
     */
    public MonthAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getViewLayoutResId() {
        return R.layout.item_date_picker;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewHolder viewHolder) {
        viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_day);
        final MonthBean monthBean = getItem(position);
        if (TextUtil.isEmptyAndNull(monthBean.getDayOfMonth())) {
            viewHolder.textView.setText(TextUtil.toString(monthBean.getDay()));
        } else
            viewHolder.textView.setText(TextUtil.toString(monthBean.getDayOfMonth()));
        if (monthBean.isChecked())
            viewHolder.textView.setBackgroundResource(R.drawable.shape_circle_yellow);
        else
            viewHolder.textView.setBackgroundColor(ResourceUtil.getColor(R.color.white));
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
    }
}