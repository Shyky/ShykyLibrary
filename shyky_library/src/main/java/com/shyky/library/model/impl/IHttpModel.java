package com.shyky.library.model.impl;

import android.support.annotation.NonNull;

import com.shyky.library.bean.RequestParams;
import com.shyky.library.callback.HttpResponseCallback;
import com.shyky.library.model.base.HttpModel;
import com.shyky.library.model.base.HttpModel.HttpMethod;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * HTTP业务模块接口
 *
 * @param <RESPONSE> 泛型参数，HTTP响应数据解析对应的实体
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/3/10
 * @since 1.0
 */
public interface IHttpModel<RESPONSE> {
    /**
     * 检查当前网络是否可用
     *
     * @param url                  统一资源定位符
     * @param httpResponseCallback 请求响应回调类
     * @return 可用返回true，否则返回false
     */
    boolean checkCurrentNetwork(@NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送同步GET请求
     *
     * @param url 统一资源定位符
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull String url);

    /**
     * 发送同步请求
     *
     * @param url 统一资源定位符
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url);

    /**
     * 发送同步请求
     *
     * @param url   统一资源定位符
     * @param param 请求参数名称
     * @param value 请求参数值
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull File value);

    /**
     * 发送同步请求
     *
     * @param url   统一资源定位符
     * @param param 请求参数名称
     * @param value 请求参数值
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull String value);

    /**
     * 发送同步请求
     *
     * @param url   统一资源定位符
     * @param param 请求参数名称
     * @param value 请求参数值
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull int value);

    /**
     * 发送同步请求
     *
     * @param url   统一资源定位符
     * @param param 请求参数名称
     * @param value 请求参数值
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull float value);

    /**
     * 发送同步请求
     *
     * @param url   统一资源定位符
     * @param param 请求参数名称
     * @param value 请求参数值
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull double value);

    /**
     * 发送同步请求
     *
     * @param url   统一资源定位符
     * @param param 请求参数名称
     * @param value 请求参数值
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull boolean value);

    /**
     * 发送同步请求
     *
     * @param url   统一资源定位符
     * @param param 请求参数名称
     * @param value 请求参数值
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull char value);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称字符串数组
     * @param values 请求参数值数组
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull String[] values);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称字符串数组
     * @param values 请求参数值数组
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull int[] values);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称字符串数组
     * @param values 请求参数值数组
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull double[] values);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称字符串数组
     * @param values 请求参数值数组
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull float[] values);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称字符串数组
     * @param values 请求参数值数组
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull char[] values);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称字符串数组
     * @param values 请求参数值数组
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull boolean[] values);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称和值数组
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[][] params);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称和值Map键值对
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> params);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数json对象
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull JSONObject params);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数json字符串
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String params);

    /**
     * 发送同步请求
     *
     * @param url    统一资源定位符
     * @param params 请求参数对象
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull RequestParams params);

    /**
     * 发送异步GET请求
     *
     * @param url                  统一资源定位符
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称
     * @param value                请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull String value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称
     * @param value                请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull int value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称
     * @param value                请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull float value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称
     * @param value                请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull double value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称
     * @param value                请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpModel.HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull char value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称
     * @param value                请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull boolean value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param params               请求参数名称
     * @param values               请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull String[] values, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param params               请求参数名称
     * @param values               请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull int[] values, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param params               请求参数名称
     * @param values               请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull float[] values, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param params               请求参数名称
     * @param values               请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull double[] values, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param params               请求参数名称
     * @param values               请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull char[] values, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param params               请求参数名称
     * @param values               请求参数值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull boolean[] values, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param params               请求参数名称和值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[][] params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称和值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数为json字符串
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param httpMethod           HTTP请求方法
     * @param url                  统一资源定位符
     * @param param                请求参数为json对象
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull JSONObject param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param httpMethod           HTTP请求方法
     * @param url                  统一资源定位符
     * @param param                请求参数为RequestParams对象，这个类需要自己去扩展用于拼接请求参数
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull RequestParams param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步GET请求
     *
     * @param url                  统一资源定位符
     * @param param                请求参数为实体对象，要求实体继承BaseRequest
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    <REQUEST> void sendAsyncRequest(@NonNull String url, @NonNull REQUEST param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求
     *
     * @param httpMethod           HTTP请求方法
     * @param url                  统一资源定位符
     * @param param                请求参数名称和值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    <REQUEST> void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull REQUEST param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 取消所有的HTTP请求
     */
    void cancelRequests();

    /**
     * 取消当前正在执行的HTTP请求
     */
    void cancelRequest();

    /**
     * 取消指定tag的HTTP请求
     *
     * @param tag 请求标识
     */
    void cancelRequest(@NonNull String tag);
}