package com.shyky.library.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.shyky.library.adapter.base.BaseRecyclerViewCheckableAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 仿照ListView的同名类设计的RecyclerView Adapter
 *
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/8/18
 * @since 1.0
 */
public final class ArrayAdapter<ENTITY> extends BaseRecyclerViewCheckableAdapter<ENTITY> {
    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    @LayoutRes
    private int layoutResId;
    /**
     * If the inflated resource is not a TextView, {@link #textViewResId} is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    @IdRes
    private int textViewResId;

    public ArrayAdapter(@NonNull Context context) {
        super(context);
    }

    /**
     * 构造方法
     *
     * @param context  应用程序上下文
     * @param resource item view布局文件资源ID
     */
    public ArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        this(context);
        init(resource, 0, new ArrayList<ENTITY>());
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public ArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        this(context);
        init(resource, textViewResourceId, new ArrayList<ENTITY>());
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ENTITY[] objects) {
        this(context);
        init(resource, 0, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    public ArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull ENTITY[] objects) {
        this(context);
        init(resource, textViewResourceId, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ENTITY> objects) {
        this(context);
        init(resource, 0, objects);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    public ArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<ENTITY> objects) {
        this(context);
        init(resource, textViewResourceId, objects);
    }

    /**
     * 初始化数据
     *
     * @param layoutResourceId
     * @param textViewResourceId
     * @param objects
     */
    private void init(@LayoutRes int layoutResourceId, @IdRes int textViewResourceId, @NonNull List<ENTITY> objects) {
        layoutResId = layoutResourceId;
        setData(objects);
        textViewResId = textViewResourceId;
    }

    @Override
    protected int getCheckableViewResId() {
        return 0;
    }

    @Override
    public void bindViewHolder(@NonNull BaseRecyclerViewHolder viewHolder, int viewType, int position) {
        super.bindViewHolder(viewHolder, viewType, position);
        final ENTITY item = getItem(position);
        viewHolder.setText(viewType, textViewResId, item.toString());
    }

    @Override
    public int getItemLayoutResId(int viewType) {
        return layoutResId;
    }
}