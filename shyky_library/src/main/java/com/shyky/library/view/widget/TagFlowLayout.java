package com.shyky.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

import com.shyky.library.R;
import com.shyky.library.intf.OnDataChangedListener;
import com.shyky.library.util.DensityUtil;
import com.shyky.util.ListUtil;
import com.shyky.util.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.socks.library.KLog.w;

/**
 * 流式标签布局，支持标签单选、多选、选中和未选中效果设置、显示不了自动换行
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/9/14
 * @since 1.0
 */
public class TagFlowLayout extends FlowLayout implements OnDataChangedListener {
    /**
     * 标签选择模式：不做选择
     */
    private static final int SELECT_MODE_NONE = -1;
    /**
     * 标签选择模式：单选
     */
    private static final int SELECT_MODE_SINGLE = 0;
    /**
     * 标签选择模式：多选
     */
    private static final int SELECT_MODE_MULTIPLE = 1;
    /**
     * 标签选择模式：选中偶数项
     */
    private static final int SELECT_MODE_EVEN = 2;
    /**
     * 标签选择模式：选中奇数项
     */
    private static final int SELECT_MODE_ODD = 3;
    private static final String KEY_CHOOSE_POS = "key_choose_pos";
    private static final String KEY_DEFAULT = "key_default";
    private TagAdapter adapter;
    private boolean autoSelectEffect;
    /**
     * 标签选择模式，默认为-1不做选择
     */
    private int selectMode;
    /**
     * 最多可以选择多少个标签，默认为-1不限制个数，注意：这个属性只针对多选有效
     */
    private int maxSelectCount;
    private MotionEvent motionEvent;
    private Set<Integer> selectedViews;
    private OnSelectListener onSelectListener;
    private OnTagClickListener onTagClickListener;

    public TagFlowLayout(@NonNull Context context) {
        this(context, null);
    }

    public TagFlowLayout(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        autoSelectEffect = true;
        selectMode = SELECT_MODE_NONE;
        maxSelectCount = -1;
        selectedViews = new HashSet<>();

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        autoSelectEffect = typedArray.getBoolean(R.styleable.TagFlowLayout_auto_select_effect, true);
        selectMode = typedArray.getInt(R.styleable.TagFlowLayout_selectMode, SELECT_MODE_NONE);
        maxSelectCount = typedArray.getInt(R.styleable.TagFlowLayout_maxSelectCount, -1);
        typedArray.recycle();
        if (autoSelectEffect) {
            setClickable(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        for (int j = 0; j < count; j++) {
            TagView tagView = (TagView) getChildAt(j);
            if (tagView.getVisibility() == View.GONE) continue;
            if (tagView.getTagView().getVisibility() == View.GONE) {
                tagView.setVisibility(View.GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface OnSelectListener {
        void onSelected(Set<Integer> selectPosSet);
    }

    public void setOnSelectListener(@NonNull OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
        if (onSelectListener != null)
            setClickable(true);
    }

    public interface OnTagClickListener {
        boolean onTagClick(View view, int position, FlowLayout parent);
    }

    public void setOnTagClickListener(@NonNull OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
        if (onTagClickListener != null)
            setClickable(true);
    }

    public void setAdapter(@NonNull TagAdapter adapter) {
        this.adapter = adapter;
        adapter.setOnDataChangedListener(this);
        selectedViews.clear();
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        TagAdapter adapter = this.adapter;
        TagView tagViewContainer = null;
        HashSet preCheckedList = adapter.getPreCheckedList();
        for (int i = 0; i < adapter.getCount(); i++) {
            View tagView = adapter.getView(this, i, adapter.getItem(i));

            tagViewContainer = new TagView(getContext());
//            ViewGroup.MarginLayoutParams clp = (ViewGroup.MarginLayoutParams) tagView.getLayoutParams();
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(clp);
//            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            lp.topMargin = clp.topMargin;
//            lp.bottomMargin = clp.bottomMargin;
//            lp.leftMargin = clp.leftMargin;
//            lp.rightMargin = clp.rightMargin;
            tagView.setDuplicateParentStateEnabled(true);
            if (tagView.getLayoutParams() != null) {
                tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            } else {
                MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(DensityUtil.dip2px(5),
                        DensityUtil.dip2px(5),
                        DensityUtil.dip2px(5),
                        DensityUtil.dip2px(5));
                tagViewContainer.setLayoutParams(lp);
            }
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);


            if (preCheckedList.contains(i)) {
                tagViewContainer.setChecked(true);
            }

            if (adapter.setSelected(i, adapter.getItem(i))) {
                selectedViews.add(i);
                tagViewContainer.setChecked(true);
            }
        }
        selectedViews.addAll(preCheckedList);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            motionEvent = MotionEvent.obtain(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        if (motionEvent == null)
            return super.performClick();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        motionEvent = null;
        TagView child = findChild(x, y);
        int pos = findPosByView(child);
        if (child != null) {
            doSelect(child, pos);
            if (onTagClickListener != null) {
                return onTagClickListener.onTagClick(child.getTagView(), pos, this);
            }
        }
        return true;
    }

    public void setMaxSelectCount(int count) {
        if (selectedViews.size() > count) {
            w("you has already select more than " + count + " views , so it will be clear .");
            selectedViews.clear();
        }
        maxSelectCount = count;
    }

    public Set<Integer> getSelectedList() {
        return new HashSet<Integer>(selectedViews);
    }

    private void doSelect(TagView child, int position) {
        if (autoSelectEffect) {
            if (!child.isChecked()) {
                if (selectMode == SELECT_MODE_NONE) {
                    // 没有就不做处理
                } else if (selectMode == SELECT_MODE_SINGLE && selectedViews.size() == 1) {
                    // 标签单选
                    Iterator<Integer> iterator = selectedViews.iterator();
                    Integer index = iterator.next();
                    final TagView tagView = (TagView) getChildAt(index);
                    tagView.setChecked(false);
                    child.setChecked(true);
                    selectedViews.remove(index);
                    selectedViews.add(position);
                } else if (selectMode == SELECT_MODE_MULTIPLE) {
                    // 标签多选
                    if (maxSelectCount == -1) {
                        return;
                    }
                    if (maxSelectCount > 0 && selectedViews.size() >= maxSelectCount)
                        return;
                    child.setChecked(true);
                    selectedViews.add(position);
                } else if (selectMode == SELECT_MODE_EVEN) {
                    // 标签多选，选中偶数项
                    if (maxSelectCount > 0 && selectedViews.size() >= maxSelectCount)
                        return;
                    child.setChecked(true);
                    selectedViews.add(position);
                } else if (selectMode == SELECT_MODE_ODD) {
                    // 标签多选，选中奇数项
                    if (maxSelectCount > 0 && selectedViews.size() >= maxSelectCount)
                        return;
                    child.setChecked(true);
                    selectedViews.add(position);
                }
            } else {
                child.setChecked(false);
                selectedViews.remove(position);
            }
            if (onSelectListener != null) {
                onSelectListener.onSelected(new HashSet<Integer>(selectedViews));
            }
        }
    }

    public TagAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());

        String selectPos = "";
        if (selectedViews.size() > 0) {
            for (int key : selectedViews) {
                selectPos += key + "|";
            }
            selectPos = selectPos.substring(0, selectPos.length() - 1);
        }
        bundle.putString(KEY_CHOOSE_POS, selectPos);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
            if (!TextUtil.isEmpty(mSelectPos)) {
                String[] split = mSelectPos.split("\\|");
                for (String pos : split) {
                    int index = Integer.parseInt(pos);
                    selectedViews.add(index);

                    TagView tagView = (TagView) getChildAt(index);
                    if (tagView != null)
                        tagView.setChecked(true);
                }

            }
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private int findPosByView(View child) {
        final int count = getChildCount();
        for (int j = 0; j < count; j++) {
            View v = getChildAt(j);
            if (v == child) return j;
        }
        return -1;
    }

    private TagView findChild(int x, int y) {
        final int count = getChildCount();
        for (int j = 0; j < count; j++) {
            TagView v = (TagView) getChildAt(j);
            if (v.getVisibility() == View.GONE) continue;
            Rect outRect = new Rect();
            v.getHitRect(outRect);
            if (outRect.contains(x, y)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public void onChanged() {
        selectedViews.clear();
        changeAdapter();
    }

    public abstract class TagAdapter<T> {
        private List<T> data;
        private OnDataChangedListener onDataChangedListener;
        private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();

        public TagAdapter(@NonNull List<T> data) {
            this.data = data;
        }

        public TagAdapter(@NonNull T[] data) {
            this.data = new ArrayList<T>(Arrays.asList(data));
        }

        public void setOnDataChangedListener(@NonNull OnDataChangedListener onDataChangedListener) {
            this.onDataChangedListener = onDataChangedListener;
        }

        public void setSelectedList(int... positions) {
            Set<Integer> set = new HashSet<>();
            for (int pos : positions) {
                set.add(pos);
            }
            setSelectedList(set);
        }

        public void setSelectedList(Set<Integer> set) {
            mCheckedPosList.clear();
            if (set != null)
                mCheckedPosList.addAll(set);
            notifyDataChanged();
        }

        HashSet<Integer> getPreCheckedList() {
            return mCheckedPosList;
        }

        public int getCount() {
            return ListUtil.isEmpty(data) ? 0 : data.size();
        }

        public void notifyDataChanged() {
            onDataChangedListener.onChanged();
        }

        public T getItem(int position) {
            if (position < 0 || position >= getCount())
                return null;
            return data.get(position);
        }

        public abstract View getView(@NonNull FlowLayout parent, int position, @NonNull T entity);

        public boolean setSelected(int position, @NonNull T entity) {
            return false;
        }
    }

    public final static class TagView extends FrameLayout implements Checkable {
        private boolean isChecked;
        private static final int[] CHECK_STATE = new int[]{android.R.attr.state_checked};

        public TagView(@NonNull Context context) {
            this(context, null);
        }

        public TagView(@NonNull Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TagView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public View getTagView() {
            return getChildAt(0);
        }

        @Override
        public int[] onCreateDrawableState(int extraSpace) {
            int[] states = super.onCreateDrawableState(extraSpace + 1);
            if (isChecked()) {
                mergeDrawableStates(states, CHECK_STATE);
            }
            return states;
        }

        @Override
        public void setChecked(boolean checked) {
            if (this.isChecked != checked) {
                this.isChecked = checked;
                refreshDrawableState();
            }
        }

        @Override
        public boolean isChecked() {
            return isChecked;
        }

        @Override
        public void toggle() {
            setChecked(!isChecked);
        }
    }
}