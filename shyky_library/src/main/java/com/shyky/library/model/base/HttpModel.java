package com.shyky.library.model.base;

import android.support.annotation.NonNull;

import com.shyky.library.callback.HttpResponseCallback;
import com.shyky.library.model.impl.IHttpModel;
import com.shyky.library.util.NetworkUtil;

import static com.socks.library.KLog.d;
import static com.socks.library.KLog.w;

/**
 * HTTP业务模块
 *
 * @param <RESPONSE> 泛型参数，HTTP响应数据解析对应的实体
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/27
 * @since 1.0
 */
public abstract class HttpModel<RESPONSE> implements IHttpModel<RESPONSE> {
    /**
     * 请求超时时间
     */
    private static final int TIMEOUT_SECOND = 15;
    /**
     * 时间单位为毫秒
     */
    private static final int TIME_UNIT = 1000;
    /**
     * 安全策略，用于加解密请求
     */
    protected SecurityPolicy securityPolicy;

    /**
     * HTTP请求谓词枚举
     */
    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    /**
     * 安全策略
     */
    public interface SecurityPolicy {
        /**
         * 解密文本
         *
         * @param response 请求成功返回的字符串数据
         * @return 解密后的字符串数据
         */
        String decrypt(String response);
    }

    protected final int getTimeout() {
        return TIMEOUT_SECOND * TIME_UNIT;
    }

    /**
     * 添加安全策略，是否需要加密请求参数和解密响应数据，需要服务端配合
     *
     * @param securityPolicy 安全策略
     */
    public final void addSecurityPolicy(SecurityPolicy securityPolicy) {
        this.securityPolicy = securityPolicy;
    }

    @Override
    public boolean checkCurrentNetwork(@NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        d("正在检测当前设备网络连接状态...");
        if (!NetworkUtil.isNetworkConnected()) {
            w("没有可连接的网络，直接回调httpResponseCallback.onFailure方法...");
            httpResponseCallback.onFailure(url, null, -1, "无网络连接，没有可用的网络，请检查是否连接了网络。", null);
            return true;
        }
        return false;
    }
}