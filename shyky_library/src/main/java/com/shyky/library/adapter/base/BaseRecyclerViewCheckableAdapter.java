package com.shyky.library.adapter.base;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;

import com.shyky.library.util.ResourceUtil;
import com.shyky.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 实现item全选、反选、单选、多选等的RecyclerView Adapter
 *
 * @param <ENTITY> 泛型参数，数据源集合中的实体
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/8/9
 * @since 1.0
 */
public abstract class BaseRecyclerViewCheckableAdapter<ENTITY> extends BaseRecyclerViewAdapter<ENTITY> {
    private SparseBooleanArray checkedStates;
    private int checkedPosition;
    @IdRes
    private int checkedViewResId;
    @ColorRes
    private int oldColorResId;
    @ColorRes
    private int newColorResId;
    private OnItemCheckChangedListener onItemCheckChangedListener;

    /**
     * item选中状态改变事件回调接口
     */
    public interface OnItemCheckChangedListener {
        boolean onItemCheckChanged(final int position, final boolean checked, final int checkedCount);
    }

    @IdRes
    protected abstract int getCheckableViewResId();

    protected BaseRecyclerViewCheckableAdapter(@NonNull Context context) {
        super(context);
        checkedStates = new SparseBooleanArray();
        checkedPosition = -1;
    }

    public final void setOnItemCheckChangedListener(@NonNull OnItemCheckChangedListener onItemCheckChangedListener) {
        this.onItemCheckChangedListener = onItemCheckChangedListener;
    }

    /**
     * 清空所有已选择的item索引集合
     */
    public final void clearCheckedIndexes() {
        checkedStates.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除指定已选择的item索引
     *
     * @param position item的索引位置
     */
    public final void removeCheckedIndex(int position) {
        if (checkedStates.size() > 0)
            checkedStates.delete(position);
    }

    /**
     * item单选
     *
     * @param position item的索引位置
     */
    public final void singleCheck(int position) {
        checkedStates.clear();
        checkedStates.put(position, true);
        notifyDataSetChanged();
    }

    /**
     * item单选，并且突出显示当前选中的item
     *
     * @param position      item的索引位置
     * @param viewResId     要突出显示的item中的某个view的资源ID
     * @param oldColorResId item view原来的文本颜色
     * @param newColorResId item view要突出显示的文本颜色
     */
    public final void singleCheck(int position, @IdRes int viewResId, @ColorRes int oldColorResId, @ColorRes int newColorResId) {
        this.checkedPosition = position;
        this.checkedViewResId = viewResId;
        this.oldColorResId = oldColorResId;
        this.newColorResId = newColorResId;
        singleCheck(position);
    }

    /**
     * item多选
     *
     * @param position item的索引位置
     */
    public final void multipleCheck(int position) {
        if (position >= 0 && position < getItemCount()) {
            boolean state = checkedStates.get(position);
            if (state) {
                state = false;
            } else {
                state = true;
            }
            checkedStates.put(position, state);
            notifyItemChanged(position);
        }
    }

    /**
     * item多选
     *
     * @param positions item的索引位置
     */
    public final void multipleCheck(List<Integer> positions) {
        if (positions != null && positions.size() > 0) {
            for (int i = 0; i < positions.size(); i++) {
                boolean state = checkedStates.get(positions.get(i));
                if (state) {
                    state = false;
                } else {
                    state = true;
                }
                checkedStates.put(positions.get(i), state);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * item全选
     */
    public final void checkAll() {
        for (int j = 0; j < getItemCount(); j++) {
            checkedStates.put(j, true);
        }
        notifyDataSetChanged();
    }

    /**
     * item反选
     */
    public final void inverseAll() {
        checkedStates.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取选中的item的个数
     *
     * @return 选中的item的个数
     */
    public final int getCheckedItemCount() {
        final List<Integer> list = getCheckedItemIndexes();
        return ObjectUtil.isNull(list) ? 0 : list.size();
    }

    public final List<Integer> getCheckedItemIndexes() {
        final List<Integer> checkedIndexes = new ArrayList<>();
        final int size = checkedStates.size();
        d("checkedStates.size()", size);
        if (size == 0) {
            return checkedIndexes;
        }
        for (int j = 0; j < size; j++) {
            final int position = checkedStates.keyAt(j);
            if (checkedStates.get(position)) {
                checkedIndexes.add(position);
            }
        }
        return checkedIndexes;
    }

    @CallSuper
    @Override
    public void bindViewHolder(@NonNull BaseRecyclerViewHolder viewHolder, int viewType, final int position) {
        if (position == checkedPosition) {
            if (newColorResId > 0) {
                viewHolder.setTextColor(viewType, checkedViewResId, ResourceUtil.getColor(newColorResId));
            }
        } else {
            if (oldColorResId > 0) {
                viewHolder.setTextColor(viewType, checkedViewResId, ResourceUtil.getColor(oldColorResId));
            }
        }

        final int viewResId = getCheckableViewResId();
        if (viewResId > 0) {
            final View checkableView = viewHolder.getView(viewType, viewResId);
            if (ObjectUtil.isNull(checkableView))
                throw new NullPointerException("没有找到控件");
            if (checkableView instanceof CheckBox) {
                final CheckBox checkBox = (CheckBox) checkableView;
                if (checkedStates.get(position)) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }

                // 如果复选框选中了，则记录item索引位置选中状态
                if (checkBox.isChecked()) {
                    d("选中item position = " + position);
                    checkedStates.put(position, true);
                } else {
                    checkedStates.delete(position);
                }

                checkableView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d("是否选中 = " + checkBox.isChecked() + " , 选中的item position = " + position);
                        if (checkBox.isChecked()) {
                            checkedStates.put(position, true);
                        } else {
                            checkedStates.delete(position);
                        }
                        if (ObjectUtil.notNull(onItemCheckChangedListener)) {
                            onItemCheckChangedListener.onItemCheckChanged(position, checkBox.isChecked(), getCheckedItemCount());
                        }
                    }
                });
            }
        }
    }
}