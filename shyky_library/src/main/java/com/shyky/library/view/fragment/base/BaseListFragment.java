package com.shyky.library.view.fragment.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shyky.library.R;
import com.shyky.library.util.LayoutUtil;
import com.shyky.library.util.NetworkUtil;
import com.shyky.library.util.ResourceUtil;

import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 通用的带有List的Fragment
 *
 * @param <LIST>     泛型参数，列表控件，可以是ListView、GridView、RecyclerView等
 * @param <RESPONSE> 泛型参数，数据源，可以是List或Entity
 * @param <ENTITY>   泛型参数，数据源集合中的实体
 * @param <ADAPTER>  泛型参数，数据适配器
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.6
 * @email sj1510706@163.com
 * @date 2016/4/6
 * @since 1.0
 */
public abstract class BaseListFragment<LIST extends ViewGroup, RESPONSE, ADAPTER, ENTITY> extends BaseFragment {
    /**
     * List滑动方向常量，向上
     */
    public static final int ORIENTATION_UP = 22;
    /**
     * List滑动方向常量，向下
     */
    public static final int ORIENTATION_DOWN = 23;
    /**
     * 请求数据类型，标识是网络数据
     */
    public static final int REQUEST_TYPE_NETWORK = 24;
    /**
     * 请求数据类型，标识是本地数据
     */
    public static final int REQUEST_TYPE_LOCAL = 25;
    /**
     * 列表控件，ListView或RecyclerView等
     */
    protected LIST list;
    /**
     * 没有数据布局
     */
    private RelativeLayout rlNothing;
    /**
     * 加载中布局
     */
    private RelativeLayout rlLoading;
    /**
     * 失败布局
     */
    private RelativeLayout rlFailure;
    /**
     * 没有网络布局
     */
    private RelativeLayout rlNoNetwork;
    /**
     * 包含List的容器控件，用于将具体的列表插入到该容器控件中显示
     */
    private RelativeLayout listContainer;

    /**
     * 发送异步加载数据请求，在这个方法中发送网络请求或读取数据库得到数据
     */
    protected abstract void sendRequest();

    /**
     * 获取请求数据类型，默认为网络请求数据
     *
     * @return 请求数据类型常量
     */
    @NonNull
    protected int getRequestDataType() {
        return REQUEST_TYPE_NETWORK;
    }

    /**
     * 第一次进入页面时是否请求数据，默认是进入页面就请求
     *
     * @return 返回true表示进入页面时就发起请求，否则需要手动调用sendRequest方法
     */
    protected boolean firstInRequest() {
        return true;
    }

    /**
     * 控制是否显示列表当没有数据时，用于控制没有数据时也可以操作列表，默认为显示
     *
     * @return 要显示返回true，否则返回false
     */
    protected boolean showListWhenNothing() {
        return true;
    }

    /**
     * 当显示加载中回调方法
     */
    protected abstract void onShowLoading();

    /**
     * 当请求失败时点击了重试回调方法
     */
    protected abstract void onFailureRetry();

    /**
     * 取消加载数据请求
     */
    protected abstract void cancelRequest();

    /**
     * List数据适配器
     *
     * @return 具体的数据适配器
     */
    @NonNull
    protected abstract ADAPTER getListAdapter();

    /**
     * 获取List控件，用户由于初始化List控件
     *
     * @return 具体的List控件对象
     */
    protected abstract LIST getList(@NonNull RelativeLayout listContainer);

    /**
     * 设置List数据适配器
     *
     * @param list 列表控件
     */
    //protected abstract void setListAdapter(@NonNull LIST list);

    /**
     * 获取List布局文件资源ID，用于将列表插入到容器中显示
     *
     * @return 资源文件ID
     */
    @LayoutRes
    protected abstract int getListLayoutResId();

    /**
     * 有列表数据过滤器，用于控制列表数据展示
     *
     * @param response 请求返回的数据解析对应的实体类
     * @return 返回true表示有数据，否则没有数据
     */
    protected boolean onSuccessFilter(@NonNull RESPONSE response) {
        return true;
    }

    /**
     * 没有列表数据过滤器，用于控制列表数据展示
     *
     * @param response 请求返回的数据解析对应的实体类
     * @return 返回true表示没有数据，否则有数据
     */
    protected abstract boolean onNothingFilter(@NonNull RESPONSE response);

    /**
     * 获取List Adapter的数据源
     *
     * @param response 请求返回的数据解析对应的实体类
     * @return 数据源
     */
    @NonNull
    protected abstract List<ENTITY> getListData(@NonNull RESPONSE response);

    /**
     * List Item 点击事件回调方法，如果加入了headView，
     * 此方法内部做了判断，在使用position时无须再次减去HeaderView的个数，
     * 否则会造成取得索引不正常甚至数组越界
     *
     * @param view      哪个Item View
     * @param viewResId view资源ID
     * @param position  Item的索引
     * @param entity    对应的实体类
     */
    protected void onListItemClick(@NonNull View view, @IdRes int viewResId, final int position, @NonNull final ENTITY entity) {

    }

    /**
     * List Item View里的某个view的点击事件回调方法，如果加入了headView，
     * 此方法内部做了判断，在使用position时无须再次减去HeaderView的个数，
     * 否则会造成取得索引不正常甚至数组越界
     *
     * @param view      哪个Item View下面的view
     * @param viewResId view资源ID
     * @param position  Item的索引位置
     * @param entity    对应的实体类
     */
    protected void onListItemViewClick(@NonNull View view, @IdRes int viewResId, final int position, @NonNull final ENTITY entity) {

    }

    /**
     * List Item 长按点击事件回调方法，如果加入了headView，
     * 此方法内部做了判断，在使用position时无须再次减去HeaderView的个数，
     * 否则会造成取得索引不正常甚至数组越界
     *
     * @param view     哪个Item View
     * @param position Item的索引
     * @param entity   对应的实体类
     */
    protected void onListItemLongClick(@NonNull View view, @IdRes int viewResId, final int position, @NonNull final ENTITY entity) {

    }

    /**
     * List Item View里的某个view的点击事件回调方法，如果加入了headView，
     * 此方法内部做了判断，在使用position时无须再次减去HeaderView的个数，
     * 否则会造成取得索引不正常甚至数组越界
     *
     * @param view      哪个Item View下面的view
     * @param viewResId view资源ID
     * @param position  Item的索引位置
     * @param entity    对应的实体类
     */
    protected void onListItemViewLongClick(@NonNull View view, @IdRes int viewResId, final int position, @NonNull final ENTITY entity) {

    }

    /**
     * 当列表滑动回调方法
     *
     * @param orientation 方向，向上或向下滑动
     */
    protected void onListScrolled(int orientation) {

    }

    /**
     * 当列表加载数据并显示完成时回调方法
     *
     * @param response 请求返回的数据解析对应的实体类
     */
    protected void onListDataFinished(@NonNull RESPONSE response) {

    }

    /**
     * 获取加载中布局文件的资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    protected abstract int getLoadingLayoutResId();

    /**
     * 获取加载中布局显示文字，设置成只能通过资源ID的形式，是为了支持国际化
     *
     * @return 资源ID
     */
    @StringRes
    protected abstract int getLoadingTextResId();

    /**
     * 获取失败布局布局文件的资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    protected abstract int getFailureLayoutResId();

    /**
     * 获取失败布局中显示图片，设置成只能通过资源ID的形式，是为了支持国际化
     *
     * @return 资源文件ID
     */
    @DrawableRes
    protected abstract int getFailureImageResId();

    /**
     * 获取失败布局中显示文字，设置成只能通过资源ID的形式，是为了支持国际化
     *
     * @return 资源ID
     */
    @StringRes
    protected abstract int getFailureTextResId();

    /**
     * 获取失败布局中重试显示文字，设置成只能通过资源ID的形式，是为了支持国际化
     *
     * @return 字符串资源ID
     */
    @StringRes
    protected abstract int getFailureRetryTextResId();

    /**
     * 获取没有数据中布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    protected abstract int getNothingLayoutResId();

    /**
     * 获取没有数据中布局显示文字，设置成只能通过资源ID的形式，是为了支持国际化
     *
     * @return 资源文件ID
     */
    @DrawableRes
    protected abstract int getNothingImageResId();

    /**
     * 获取没有数据布局中显示文字，设置成只能通过资源ID的形式，是为了支持国际化
     *
     * @return 字符串资源ID
     */
    @StringRes
    protected abstract int getNothingTextResId();

    /**
     * 获取没有网络布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    protected abstract int getNoNetworkLayoutResId();

    /**
     * 滑动到列表顶部
     */
    protected abstract void scrollToTop();

    /**
     * 滑动到列表顶部带有动画效果
     */
    protected abstract void smoothScrollToTop();

    /**
     * 获取List中的数据源
     *
     * @return List的数据源
     */
    @NonNull
    protected abstract List<ENTITY> getListData();

    /**
     * 列表数据是否为空
     *
     * @return
     */
    protected abstract boolean isListEmpty();

    /**
     * 清空列表数据
     */
    public abstract void clearListData();

    protected abstract int getListItemCount();

    @NonNull
    protected abstract ENTITY getListLastItem();

    @NonNull
    protected abstract ENTITY getListItem(int position);

    @NonNull
    protected abstract ENTITY getListFirstItem();

    protected abstract void removeItemFromList(@NonNull ENTITY entity);

    protected abstract void removeItemFromList(int index);

    protected abstract void updateListItem(int index, @NonNull ENTITY entity);

    protected abstract void insertItemToList(int index, @NonNull ENTITY entity);

    protected abstract void addDataToList(@NonNull List<ENTITY> data);

    protected abstract void notifyAdapterDataSetChanged();

    /**
     * 显示加载中布局
     */
    public final void showLoading() {
        onShowLoading(); // 通知子类正在加载
        // 如果列表没有数据显示加载中
        if (isListEmpty()) {
            rlLoading.setVisibility(View.VISIBLE);
            rlFailure.setVisibility(View.GONE);
            rlNothing.setVisibility(View.GONE);
            if (getRequestDataType() == REQUEST_TYPE_NETWORK)
                rlNoNetwork.setVisibility(View.GONE);
            if (list != null)
                list.setVisibility(View.GONE);
        }
    }

    /**
     * 显示没有数据布局
     */
    public final void showNothing() {
        // 无数据显示
        if (isListEmpty()) {
            rlLoading.setVisibility(View.GONE);
            rlFailure.setVisibility(View.GONE);
            rlNothing.setVisibility(View.VISIBLE);
            if (getRequestDataType() == REQUEST_TYPE_NETWORK)
                rlNoNetwork.setVisibility(View.GONE);
            // 默认没有数据时会显示列表
            if (showListWhenNothing()) {
                list.setBackgroundColor(ResourceUtil.getColor(android.R.color.transparent));
                list.setVisibility(View.VISIBLE);
            } else
                listContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 显示失败布局
     */
    public final void showFailure(String message) {
        rlLoading.setVisibility(View.GONE);
        rlNothing.setVisibility(View.GONE);
        if (getRequestDataType() == REQUEST_TYPE_NETWORK)
            rlNoNetwork.setVisibility(View.GONE);
        if (isListEmpty()) {
            // 隐藏列表，因为列表在最顶层，点击事件被它接收了，解决无法点击加载失败页面重试问题
            if (list != null)
                list.setVisibility(View.GONE);
            rlFailure.setVisibility(View.VISIBLE);
        } else {
            // 加载数据失败，隐藏列表，因为列表布局实在最上面
            if (list != null)
                list.setVisibility(View.GONE);
            rlFailure.setVisibility(View.GONE);
            showToast(message);
        }
    }

    /**
     * 显示没有网络
     */
    public final void showNetworkError() {
        if (getRequestDataType() == REQUEST_TYPE_NETWORK) {
            rlLoading.setVisibility(View.GONE);
            rlNothing.setVisibility(View.GONE);
            rlFailure.setVisibility(View.GONE);
            if (isListEmpty()) {
                // 隐藏列表，因为列表在最顶层，点击事件被它接收了，解决无法点击加载失败页面重试问题
                //                if (list != null)
                //                    list.setVisibility(View.GONE);
                if (listContainer != null)
                    listContainer.setVisibility(View.GONE);
                rlNoNetwork.setVisibility(View.VISIBLE);
            } else {
                rlNoNetwork.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示列表
     */
    public final void showList() {
        rlNothing.setVisibility(View.GONE);
        rlLoading.setVisibility(View.GONE);
        rlFailure.setVisibility(View.GONE);
        if (getRequestDataType() == REQUEST_TYPE_NETWORK)
            rlNoNetwork.setVisibility(View.GONE);
        if (list != null)
            list.setVisibility(View.VISIBLE);
        if (listContainer != null)
            listContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 请求失败或没有网络时重试
     */
    public final void failureRetry() {
        d("点击重试...");
        // 检查当前设备网络是否可用
        if (!NetworkUtil.isNetworkConnected()) {
            d("当前设备网络不可用...");
            showNetworkError();
            return;
        }
        // 重新请求
        showLoading();
        onFailureRetry(); // 通知子类点击了失败重试
        sendRequest();
    }

    /**
     * 检测网络是否可用
     *
     * @return 是返回true，否返回false
     */
    private boolean isNetworkAvailable() {
        return NetworkUtil.isNetworkAvailable(getActivity());
    }

    @Override
    public final int getCreateViewLayoutId() {
        return R.layout.template_list; // 默认显示列表模板
    }

    @CallSuper
    @Override
    public void findView(View inflateView, Bundle savedInstanceState) {
        super.findView(inflateView, savedInstanceState);
        listContainer = (RelativeLayout) inflateView.findViewById(R.id.listContainer);
        d("listContainer", listContainer);

        // 初始化三种List状态布局
        if (rlLoading == null && rlNothing == null && rlFailure == null) {
            rlLoading = (RelativeLayout) inflateView.findViewById(R.id.rl_loading);
            rlNothing = (RelativeLayout) inflateView.findViewById(R.id.rl_nothing);
            rlFailure = (RelativeLayout) inflateView.findViewById(R.id.rl_failure);
            rlNoNetwork = (RelativeLayout) inflateView.findViewById(R.id.rl_network);
        }
    }

    /**
     * 设置四种状态布局中显示的图片和文字
     */
    private void setImageAndText() {
        final TextView loadingText = (TextView) rlLoading.findViewById(R.id.tv_loading_text);
        final ImageView failureImage = (ImageView) rlFailure.findViewById(R.id.iv_failure_img);
        final TextView failureText = (TextView) rlFailure.findViewById(R.id.tv_failure_text);
        final TextView failureRetryText = (TextView) rlFailure.findViewById(R.id.tv_failure_retry);
        final ImageView nothingImage = (ImageView) rlNothing.findViewById(R.id.iv_nothing_img);
        final TextView nothingText = (TextView) rlNothing.findViewById(R.id.tv_nothing_text);

        if (getLoadingTextResId() > 0)
            loadingText.setText(getLoadingTextResId());
        if (getNothingImageResId() > 0)
            nothingImage.setImageResource(getNothingImageResId());
        if (getNothingTextResId() > 0)
            nothingText.setText(getNothingTextResId());
        if (getFailureImageResId() > 0)
            failureImage.setImageResource(getFailureImageResId());
        if (getFailureTextResId() > 0)
            failureText.setText(getFailureTextResId());
        if (getFailureRetryTextResId() > 0)
            failureRetryText.setText(getFailureRetryTextResId());
    }

    @CallSuper
    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        super.initView(inflateView, savedInstanceState);

        // 添加指定的List到模板列表布局中
        if (getListLayoutResId() > 0) {
            // LayoutUtil.inflate(getActivity())使用全局的Context对象会造成inflate xml布局文件错误
            final View view = LayoutUtil.inflate(getActivity(), getListLayoutResId());
            listContainer.addView(view);
            // 获取List控件对象
            list = getList(listContainer);
        }

        // 是否使用默认的四种状态布局
        boolean useDefaultLayout = false;
        // 设置四种状态布局的默认显示图片和文字
        final int loadingLayoutResId = getLoadingLayoutResId();
        if (loadingLayoutResId > 0)
            rlLoading.addView(LayoutUtil.inflate(getContext(), loadingLayoutResId));
        else {
            rlLoading.addView(LayoutUtil.inflate(getContext(), R.layout.include_layout_loading));
            useDefaultLayout = true;
        }
        // 设置四种状态布局的默认显示图片和文字
        final int nothingLayoutResId = getNothingLayoutResId();
        if (nothingLayoutResId > 0)
            rlNothing.addView(LayoutUtil.inflate(nothingLayoutResId));
        else {
            rlNothing.addView(LayoutUtil.inflate(R.layout.include_layout_nothing));
            useDefaultLayout = true;
        }
        // 设置四种状态布局的默认显示图片和文字
        final int failureLayoutResId = getFailureLayoutResId();
        if (failureLayoutResId > 0)
            rlFailure.addView(LayoutUtil.inflate(failureLayoutResId));
        else {
            rlFailure.addView(LayoutUtil.inflate(R.layout.include_layout_failure));
            useDefaultLayout = true;
        }
        // 设置四种状态布局的默认显示图片和文字
        final int noNetworkLayoutResId = getNoNetworkLayoutResId();
        if (noNetworkLayoutResId > 0) {
            View view = LayoutInflater.from(getActivity()).inflate(noNetworkLayoutResId, rlNoNetwork, false);
            rlNoNetwork.addView(view);
        } else {
            rlNoNetwork.addView(LayoutUtil.inflate(R.layout.include_layout_network));
            useDefaultLayout = true;
        }

        // 使用默认的四种状态布局
        if (useDefaultLayout)
            setImageAndText(); // 设置四种状态布局的默认显示图片和文字

        // 设置List数据适配器
        // setListAdapter(list);

        // 检查当前设备网络是否可用
        if (getRequestDataType() == REQUEST_TYPE_NETWORK) {
            if (!isNetworkAvailable()) {
                d("当前设备网络不可用...");
                // showFailure("当前网络不可用");
                showNetworkError();
                return;
            }
        }

        // 进入页面是否发送数据请求
        if (firstInRequest()) {
            sendRequest();
        }
    }

    @CallSuper
    @Override
    public void initListener() {
        super.initListener();
        // 点击重试
        rlFailure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d("rlFailure click...");
                // 检查当前设备网络是否可用
                if (!isNetworkAvailable()) {
                    d("当前设备网络不可用...");
                    showFailure("当前网络不可用");
                    return;
                }
                // 重新请求
                showLoading();
                onFailureRetry(); // 通知子类点击了失败重试
                sendRequest();
            }
        });
    }

    /**
     * 加载列表数据成功
     *
     * @param response 列表显示数据解析对应的实体类
     */
    public void onSuccess(@NonNull RESPONSE response) {
        clearListData();
        addDataToList(getListData(response));
        showList();
        onListDataFinished(response);
    }

    /**
     * 加载列表数据失败
     *
     * @param message 消息文本
     */
    public void onFailure(@NonNull String message) {
        showFailure(message);
    }

    @CallSuper
    @Override
    public void onDetach() {
        super.onDetach();
        cancelRequest();
        d("请求已经取消...");
        // 解决Fragment被移除一直显示加载中问题
        if (!isDetached() && isListEmpty())
            showNothing();
    }
}