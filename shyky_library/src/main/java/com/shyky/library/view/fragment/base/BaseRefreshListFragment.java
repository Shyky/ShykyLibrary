package com.shyky.library.view.fragment.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.ViewGroup;

import com.shyky.library.R;
import com.shyky.library.util.NetworkUtil;
import com.shyky.util.TimeUtil;

import static com.socks.library.KLog.d;

/**
 * 通用的可下拉刷新带有List的Fragment
 *
 * @param <RESPONSE> 列表中要展示的数据
 * @param <ENTITY>   泛型参数，数据源集合中的实体
 * @param <ADAPTER>  泛型参数，数据适配器
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/4/6
 * @since 1.0
 */
public abstract class BaseRefreshListFragment<LIST extends ViewGroup, ADAPTER, RESPONSE, ENTITY> extends BaseListFragment<LIST, RESPONSE, ADAPTER, ENTITY> {
    protected abstract void stopRefresh(@NonNull LIST list);

    protected abstract void setRefreshTime(@NonNull LIST list, @NonNull String refreshTime);

    @StringRes
    protected int getNoNetworkShowMessageResId() {
        return 0;
    }

    @Override
    protected void onShowLoading() {

    }

    @Override
    protected void onFailureRetry() {

    }

    public final void refresh() {
        d("refresh...");
        if (!NetworkUtil.isNetworkAvailable()) {
            if (getNoNetworkShowMessageResId() > 0)
                showToast(getNoNetworkShowMessageResId());
            else
                showToast(R.string.text_network_error);
            stopRefresh(list);
            setRefreshTime(list, TimeUtil.getCurrentTime("MM-dd HH:mm"));
            return;
        }
        // 显示加载中布局
        // showLoading();
        // 控制显示加载中
        // showLoading();
        // 请求
        sendRequest();
    }

    @Override
    public void onSuccess(@NonNull RESPONSE response) {
        stopRefresh(list);
        setRefreshTime(list, TimeUtil.getCurrentTime("MM-dd HH:mm"));

        // 先清空原来的数据
        clearListData();

        // 无数据显示
        if (response == null || onNothingFilter(response)) {
            showNothing();
        } else {
            // 刷新成功，有数据处理
            if (onSuccessFilter(response)) {
                // 应该叠加
                addDataToList(getListData(response));
                showList();
                d("item Size", getListData(response).size());
            } else {
                showNothing();
            }
        }
        d("after setData2Adapter... item Size", getListItemCount());
//                // 滑动到顶部
//                scrollToListTop();
        d("total count", getListItemCount());
        // 防止报错
        notifyAdapterDataSetChanged();
        onListDataFinished(response);
    }

    @Override
    public void onFailure(@NonNull String message) {
        d("onFailure...");
        stopRefresh(list);
        setRefreshTime(list, TimeUtil.getCurrentTime("MM-dd HH:mm"));
        // 防止报错：java.lang.IllegalStateException: Fragment  not attached to Activity
        if (getActivity() != null && isAdded()) {
            showFailure(getString(R.string.text_list_refresh_failure));
        }
        notifyAdapterDataSetChanged();
    }
}