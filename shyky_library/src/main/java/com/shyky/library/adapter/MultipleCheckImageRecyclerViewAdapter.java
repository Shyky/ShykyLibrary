package com.shyky.library.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

import com.shyky.library.adapter.base.BaseRecyclerViewImageAdapter;
import com.shyky.library.bean.CheckableBean;
import com.shyky.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

import static com.socks.library.KLog.d;
import static com.socks.library.KLog.e;

/**
 * 实现item全选、反选、多选、记住选中状态等且具有显示、加载及缓存图片功能的RecyclerView Adapter
 *
 * @param <ENTITY> 泛型参数，数据源集合中的实体，实体类需要继承CheckableBean
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/9/7
 * @since 1.0
 */
public abstract class MultipleCheckImageRecyclerViewAdapter<ENTITY extends CheckableBean> extends BaseRecyclerViewImageAdapter<ENTITY> {
    private OnItemCheckChangedListener onItemCheckChangedListener;

    /**
     * 得到可选择控件的资源ID
     *
     * @return 控件资源ID
     */
    @IdRes
    protected abstract int getCheckableViewResId();

    /**
     * item选中状态改变事件回调接口
     */
    public interface OnItemCheckChangedListener {
        boolean onItemCheckChanged(final int position, final boolean checked, final int checkedCount);
    }

    protected MultipleCheckImageRecyclerViewAdapter(@NonNull Context context) {
        super(context);
    }

    public final void setOnItemCheckChangedListener(@NonNull OnItemCheckChangedListener onItemCheckChangedListener) {
        this.onItemCheckChangedListener = onItemCheckChangedListener;
    }

    /**
     * 获取选中的item集合
     *
     * @return 选中的item集合
     */
    public final List<ENTITY> getCheckedItems() {
        List<ENTITY> checkedItems = new ArrayList<>();
        for (int j = 0; j < getItemDataCount(); j++) {
            final ENTITY entity = getItemData(j);
            entity.setChecked(true);
            final boolean checked = entity.isChecked();
            d("getCheckedItems --> entity.isChecked", checked);
            if (checked)
                checkedItems.add(entity);
        }
        if (checkedItems.size() == 0) {
            checkedItems = null;
        }
        d("checkedItems", checkedItems);
        return checkedItems;
    }

    /**
     * 清空所有已选择的item索引集合
     */
    public final void clearCheckedItems() {
        for (int j = 0; j < getItemDataCount(); j++) {
            final ENTITY entity = getItemData(j);
            final boolean checked = entity.isChecked();
            d("entity.isChecked", checked);
            if (checked)
                entity.setChecked(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除指定已选择的item索引
     *
     * @param position item的索引位置
     */
    public final void removeCheckedItem(int position) {
        getItemData(position).setChecked(false);
        notifyItemChanged(position);
    }

    /**
     * item多选
     *
     * @param position item的索引位置
     */
    public final void check(int position) {
        d("选中item，position = " + position);
        if (position >= 0 && position < getItemCount()) {
            final ENTITY entity = getItemData(position);
            entity.setChecked(true);
            set(position, entity);
            notifyItemChanged(position);
        }
    }

    /**
     * item全选
     */
    public final void checkAll() {
        for (int j = 0; j < getItemCount(); j++) {
            getItemData(j).setChecked(true);
        }
        notifyDataSetChanged();
    }

    /**
     * item反选
     */
    public final void inverseAll() {
        for (int j = 0; j < getItemDataCount(); j++) {
            final ENTITY entity = getItemData(j);
            entity.setChecked(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 反选指定索引位置的item
     *
     * @param position item索引位置
     */
    public final void inverse(int position) {
        d("反选item，position = " + position);
        if (position >= 0 && position < getItemCount()) {
            final ENTITY entity = getItemData(position);
            entity.setChecked(false);
            set(position, entity);
            notifyItemChanged(position);
        }
    }

    /**
     * 获取选中的item的个数
     *
     * @return 选中的item的个数
     */
    public final int getCheckedItemCount() {
        int count = 0;
        for (int j = 0; j < getItemDataCount(); j++) {
            final ENTITY entity = getItemData(j);
            final boolean checked = entity.isChecked();
            d("getCheckedItemCount item checked", checked);
            if (checked)
                count++;
        }
        d("getCheckedItemCount", count);
        return count;
    }

    @CallSuper
    @Override
    public void bindViewHolder(@NonNull BaseRecyclerViewHolder viewHolder, int viewType, final int position) {
        final int checkableViewResId = getCheckableViewResId();
        final ENTITY entity = getItem(position);
        if (checkableViewResId > 0) {
            final View checkableView = viewHolder.getView(viewType, checkableViewResId);
            if (ObjectUtil.isNull(checkableView)) {
                e("没有在view type = " + viewType + "中找到选择控件");
            } else {
                d("在view type = " + viewType + "中找到了选择控件：" + checkableView);
                // 判断是CheckBox、RadioButton、ImageView其中一个
                if (checkableView instanceof CheckBox) {
                    final CheckBox checkBox = (CheckBox) checkableView;
                    // 记住每一个item的选择状态
                    if (entity.isChecked()) {
                        d("item position = " + position + "被选中");
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }

                    // 复选框点击事件
                    checkableView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final boolean checked = checkBox.isChecked();
                            d("是否选中 = " + checked + " , 选中的item position = " + position);
                            if (checked)
                                entity.setChecked(true);
                            else
                                entity.setChecked(false);
                            if (ObjectUtil.notNull(onItemCheckChangedListener)) {
                                onItemCheckChangedListener.onItemCheckChanged(position, checked, getCheckedItemCount());
                            }
                        }
                    });
                }
            }
        }
    }
}