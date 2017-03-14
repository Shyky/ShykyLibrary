package com.shyky.library.view.fragment.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.ViewGroup;

import com.shyky.library.R;
import com.shyky.library.constant.Constant;
import com.shyky.library.util.NetworkUtil;
import com.shyky.util.TimeUtil;

import static com.socks.library.KLog.d;

/**
 * 通用的可下拉刷新，可上拉加载更多带有List的Fragment
 *
 * @param <LIST>     泛型参数，列表控件，可以是ListView、GridView、RecyclerView等
 * @param <RESPONSE> 泛型参数，数据源，可以是List或Entity
 * @param <ENTITY>   泛型参数，数据源集合中的实体
 * @param <ADAPTER>  泛型参数，数据适配器
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/4/6
 * @since 1.0
 */
public abstract class BaseRefreshAndLoadMoreListFragment<LIST extends ViewGroup, ADAPTER, RESPONSE, ENTITY> extends BaseListFragment<LIST, RESPONSE, ADAPTER, ENTITY> {
    /**
     * List分页显示条数，默认为15条
     */
    protected static int PAGE_SIZE = Constant.PAGE_SIZE._15;
    protected static final int ACTION_REFRESH = 55;
    protected static final int ACTION_LOAD_MORE = 66;
    /**
     * 请求列表页索引,默认从0开始
     */
    protected int start;
    private int action;
    private boolean isLoadingData;

    protected abstract void enableLoadMore(@NonNull LIST list);

    protected abstract void stopRefresh(@NonNull LIST list);

    protected abstract void setRefreshTime(@NonNull LIST list, @NonNull String refreshTime);

    protected abstract void setLoadMoreSuccess(@NonNull LIST list);

    protected abstract void stopLoadMore(@NonNull LIST list);

    @StringRes
    protected int getNoNetworkShowMessageResId() {
        return 0;
    }

    /**
     * 获取列表加载更多时没有数据显示消息文本字符串资源ID
     *
     * @return
     */
    @StringRes
    protected int getListNoMoreDataShowMessageResId() {
        return 0;
    }

    /**
     * 当列表没有更多数据时回调
     */
    protected void onListNoMoreData() {

    }

    @Override
    protected void onShowLoading() {
        start = 1; // 第一页从1开始
        action = ACTION_REFRESH; // 标识为刷新
    }

    @Override
    protected void onFailureRetry() {
        action = ACTION_REFRESH; // 标识为刷新
    }

    @Override
    public void initData() {
        super.initData();
        // 默认标识为刷新
        action = ACTION_REFRESH;
        // 第一页从1开始
        start = 1;
    }

    public final void refresh() {
        d("refresh --> action = " + action + " , start = " + start);
        if (!NetworkUtil.isNetworkAvailable()) {
            if (getNoNetworkShowMessageResId() > 0)
                showToast(getNoNetworkShowMessageResId());
            else
                showToast(R.string.text_network_error);
            stopRefresh(list);
            setRefreshTime(list, TimeUtil.getCurrentTime("MM-dd HH:mm"));
            return;
        }
        if (!isLoadingData) {
            // 控制显示加载中
            // showLoading();
            // 大于指定的分页条数才开启加载更多
            start = 1;
            isLoadingData = true;
            action = ACTION_REFRESH; // 标识为加载更多
            if (getListItemCount() > PAGE_SIZE) {
                enableLoadMore(list);
            }
            // 标识为正在刷新
            //isRefreshing = true;
            // 显示加载中布局
            // showLoading();
            sendRequest();
        }
    }

    protected final void loadMore() {
        if (!isLoadingData) {
            d("loadMore --> action = " + action + " , start = " + start);
            start++;
            action = ACTION_LOAD_MORE; // 标识为加载更多
            isLoadingData = true;
            sendRequest();
        }
    }

    private String printAction() {
        String str = null;
        switch (action) {
            case ACTION_REFRESH:
                str = "ACTION_REFRESH";
                break;
            case ACTION_LOAD_MORE:
                str = "ACTION_LOAD_MORE";
                break;
        }
        return str;
    }

    @Override
    public void onSuccess(@NonNull RESPONSE response) {
        switch (action) {
            case ACTION_REFRESH:
                stopRefresh(list);
                setRefreshTime(list, TimeUtil.getCurrentTime("MM-dd HH:mm"));

                // 清空原来的数据
                clearListData();

                // 无数据显示
                if (response == null || onNothingFilter(response)) {
                    showNothing();
                } else {
                    // 刷新成功，有数据处理
                    if (onSuccessFilter(response)) {
                        // 应该叠加
                        addDataToList(getListData(response));
                        d("刷新成功，返回的数据集 -> size", getListData(response).size());
                        showList();
//                        start = items.size(); // 请求成功才加
//                        start ++; // 请求成功才加
                        if (getListItemCount() > PAGE_SIZE) {
                            enableLoadMore(list);
                        }
                    } else {
                        showNothing();
                    }
                }
//                // 滑动到顶部
//                scrollToListTop();

                break;
            case ACTION_LOAD_MORE:
                stopRefresh(list);
//                stopLoadMore(list);

                // 无数据显示
                if (response == null || onNothingFilter(response)) {
                    enableLoadMore(list); // 没有下一页数据停止加载更多
                    final int messageResId = getListNoMoreDataShowMessageResId();
                    if (messageResId > 0)
                        showToast(messageResId);
                    else
                        showToast(R.string.tip_no_more_list_data);
                    // 列表没有更多数据回调
                    onListNoMoreData();
                    stopLoadMore(list);
                } else {
                    // 加载成功，有数据
                    if (onSuccessFilter(response)) {
                        // 应该叠加

                        addDataToList(getListData(response));

                        d("加载更多成功，返回的数据集 -> size", getListData(response).size());
                        //                        start = items.size(); // 加载更多成功才加
                    } else {
//                        showToast(getString(R.string.text_list_load_more_nothing));
                        stopLoadMore(list);
                    }
                    setLoadMoreSuccess(list);
                }
                break;
        }
        d("list data size", getListItemCount());
        isLoadingData = false;
        // 防止报错
        notifyAdapterDataSetChanged();
        onListDataFinished(response);
    }

    @Override
    public void onFailure(@NonNull String message) {
        d("onFailure --> action = " + printAction() + " , start = " + start);
        // 防止报错：java.lang.IllegalStateException: Fragment  not attached to Activity
        if (getActivity() != null && isAdded()) {
            switch (action) {
                case ACTION_REFRESH:
                    stopRefresh(list);
                    setRefreshTime(list, TimeUtil.getCurrentTime("MM-dd HH:mm"));
                    if (isListEmpty())
                        showFailure(getString(R.string.text_list_refresh_failure));
                    else
                        showToast(getString(R.string.text_list_refresh_failure));
                    break;
                case ACTION_LOAD_MORE:
                    stopRefresh(list);
                    setLoadMoreSuccess(list);
                    showToast(R.string.text_list_load_more_failure);
                    break;
            }
            notifyAdapterDataSetChanged();
        }
    }
}