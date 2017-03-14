package com.shyky.library.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.shyky.library.R;
import com.shyky.library.bean.RequestParams;
import com.shyky.library.callback.HttpResponseCallback;
import com.shyky.library.util.ResourceUtil;
import com.shyky.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.socks.library.KLog.d;

/**
 * 使用okHttp进行HTTP请求
 *
 * @param <RESPONSE> 泛型参数，HTTP响应数据解析对应的实体
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.6
 * @email sj1510706@163.com
 * @date 2016/5/10
 * @since 1.0
 */
public final class HttpModel<RESPONSE> extends com.shyky.library.model.base.HttpModel<RESPONSE> {
    /**
     * 请求超时时间，默认为5秒
     */
    public static final int REQUEST_TIME_OUT_SECOND = 5;

    private static class Holder {
        public static final HttpModel instance = new HttpModel();

        private Holder() {

        }
    }

    /**
     * OKHttp客户端
     */
    private static final OkHttpClient httpClient = HttpClient.getInstance();
    /**
     * 保存正在执行的请求
     */
    private List<Call> requests;
    /**
     * 当前正在执行的请求
     */
    private Call call;
    /**
     * 请求参数类型为JSON
     */
    public static final MediaType PARAM_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * 请求参数类型为表单
     */
    public static final MediaType PARAM_TYPE_FORM = MediaType.parse("application/json; charset=utf-8");

    /**
     * 单例模式获取HTTP客户端
     */
    private static final class HttpClient {
        private static OkHttpClient instance;

        /**
         * 构造方法私有化
         */
        private HttpClient() {

        }

        /**
         * 获取OkHttpClient实例
         *
         * @return OkHttpClient实例对象
         */
        public static OkHttpClient getInstance() {
            if (instance == null) {
                synchronized (HttpClient.class) {
                    if (instance == null) {
                        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                        instance = builder.addInterceptor(new LogInterceptor())
                                .connectTimeout(REQUEST_TIME_OUT_SECOND, TimeUnit.SECONDS)
                                .writeTimeout(REQUEST_TIME_OUT_SECOND, TimeUnit.SECONDS)
                                .readTimeout(REQUEST_TIME_OUT_SECOND, TimeUnit.SECONDS).build();
                    }
                }
            }
            return instance;
        }
    }

    /**
     * 构造方法
     */
    public HttpModel() {
        requests = new ArrayList<>();
    }

    public synchronized static HttpModel getInstance() {
        return Holder.instance;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull String url) {
        // 发送同步GET请求
        final Request request = new Request.Builder().url(url).build();
        try {
            final Response response = httpClient.newCall(request).execute();
            return (RESPONSE) response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url) {
        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request = new Request.Builder().url(url).build();
                try {
                    final Response response = httpClient.newCall(request).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case POST:
                // 提交post表单请求
                final RequestBody formBody = new FormBody.Builder().build();
                final Request request_post = new Request.Builder().url(url).post(formBody).build();
                try {
                    final Response response = httpClient.newCall(request_post).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull File value) {
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull String value) {
        final String req = "?" + param + "=" + value;

        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request = new Request.Builder().url(url + req).build();
                try {
                    final Response response = httpClient.newCall(request).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case POST:
                // 提交post表单请求
                final RequestBody formBody = new FormBody.Builder().add(param, value).build();
                final Request request_post = new Request.Builder().url(url).post(formBody).build();
                try {
                    final Response response = httpClient.newCall(request_post).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull int value) {
        return sendSyncRequest(httpMethod, url, param, TextUtil.toString(value));
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull float value) {
        return sendSyncRequest(httpMethod, url, param, TextUtil.toString(value));
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull double value) {
        return sendSyncRequest(httpMethod, url, param, TextUtil.toString(value));
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull boolean value) {
        return sendSyncRequest(httpMethod, url, param, TextUtil.toString(value));
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull char value) {
        return sendSyncRequest(httpMethod, url, param, TextUtil.toString(value));
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull String[] values) {
        if (params.length == 0 && values.length == 0 && params.length != values.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < params.length; index++) {
            map.put(params[index], values[index]);
        }
        return sendSyncRequest(httpMethod, url, map);
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull int[] values) {
        if (params.length == 0 && values.length == 0 && params.length != values.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < params.length; index++) {
            map.put(params[index], values[index]);
        }
        return sendSyncRequest(httpMethod, url, map);
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull double[] values) {
        if (params.length == 0 && values.length == 0 && params.length != values.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < params.length; index++) {
            map.put(params[index], values[index]);
        }
        return sendSyncRequest(httpMethod, url, map);
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull float[] values) {
        if (params.length == 0 && values.length == 0 && params.length != values.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < params.length; index++) {
            map.put(params[index], values[index]);
        }
        return sendSyncRequest(httpMethod, url, map);
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull char[] values) {
        if (params.length == 0 && values.length == 0 && params.length != values.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < params.length; index++) {
            map.put(params[index], values[index]);
        }
        return sendSyncRequest(httpMethod, url, map);
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull boolean[] values) {
        if (params.length == 0 && values.length == 0 && params.length != values.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < params.length; index++) {
            map.put(params[index], values[index]);
        }
        return sendSyncRequest(httpMethod, url, map);
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[][] params) {
        HashMap<String, Object> map = new HashMap<>();
        for (String[] param : params) {
            map.put(param[0], param[1]);
        }
        return sendSyncRequest(httpMethod, url, map);
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> params) {
        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request = new Request.Builder().url(buildGetUrl(url, params)).build();
                try {
                    final Response response = httpClient.newCall(request).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case POST:
                // 提交post表单请求
                FormBody.Builder builder = new FormBody.Builder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    builder.add(param.getKey(), param.getValue().toString());
                }
                FormBody formBody = builder.build();
                final Request request_post = new Request.Builder().url(url).post(formBody).build();
                try {
                    final Response response = httpClient.newCall(request_post).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull JSONObject params) {
        return sendSyncRequest(httpMethod, url, params.toString());
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String params) {
        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                try {
                    final Request request = new Request.Builder().url(buildGetUrl(url, jsonToMap(params))).build();
                    final Response response = httpClient.newCall(request).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case POST:
                RequestBody body = RequestBody.create(PARAM_TYPE_JSON, params);
                final Request request_post = new Request.Builder().url(url).post(body).build();
                try {
                    final Response response = httpClient.newCall(request_post).execute();
                    return (RESPONSE) response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull RequestParams params) {
        return null;
    }

    /**
     * 设置HttpResponseCallback相关参数
     *
     * @param url
     * @param request
     * @param responseEntityClass
     * @param httpResponseCallback
     */
    private void setHttpResponseCallback(@NonNull String url, String request, Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        httpResponseCallback.setUrl(url);
        httpResponseCallback.setRequest(TextUtil.isEmptyAndNull(request) ? ResourceUtil.getString(R.string.msg_request_nothing) : request);
        if (responseEntityClass != null)
            httpResponseCallback.setResponseEntityClass(responseEntityClass);
    }

    @Override
    public void sendAsyncRequest(@NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        setHttpResponseCallback(url, null, null, httpResponseCallback);
        final Request request = new Request.Builder().url(url).build();
        call = httpClient.newCall(request);
        addRequest(call);
        call.enqueue(httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        setHttpResponseCallback(url, null, null, httpResponseCallback);

        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request_get = new Request.Builder().url(url).build();
                call = httpClient.newCall(request_get);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
            case POST:
                // 提交post表单请求
                final RequestBody formBody = new FormBody.Builder().build();
                final Request request_post = new Request.Builder().url(url).post(formBody).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        setHttpResponseCallback(url, null, responseEntityClass, httpResponseCallback);

        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request_get = new Request.Builder().url(url).build();
                call = httpClient.newCall(request_get);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
            case POST:
                // 提交post表单请求
                final RequestBody formBody = new FormBody.Builder().build();
                final Request request_post = new Request.Builder().url(url).post(formBody).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull String value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        final String request = "?" + param + "=" + value;
        d("拼接的请求参数：" + request);
        setHttpResponseCallback(url, request, responseEntityClass, httpResponseCallback);

        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request_get = new Request.Builder().url(url + request).build();
                call = httpClient.newCall(request_get);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
            case POST:
                // 提交post表单请求
                final RequestBody formBody = new FormBody.Builder().add(param, value).build();
                final Request request_post = new Request.Builder().url(url).post(formBody).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull int value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        sendAsyncRequest(httpMethod, url, param, TextUtil.toString(value), responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull float value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        sendAsyncRequest(httpMethod, url, param, TextUtil.toString(value), responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull double value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        sendAsyncRequest(httpMethod, url, param, TextUtil.toString(value), responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull char value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        sendAsyncRequest(httpMethod, url, param, TextUtil.toString(value), responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String param, @NonNull boolean value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        sendAsyncRequest(httpMethod, url, param, TextUtil.toString(value), responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] param, @NonNull String[] value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (param.length == 0 && value.length == 0 && param.length != value.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < param.length; index++) {
            map.put(param[index], value[index]);
        }
        sendAsyncRequest(httpMethod, url, map, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] param, @NonNull int[] value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (param.length == 0 && value.length == 0 && param.length != value.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < param.length; index++) {
            map.put(param[index], value[index]);
        }
        sendAsyncRequest(httpMethod, url, map, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] params, @NonNull float[] values, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (params.length == 0 && values.length == 0 && params.length != values.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < params.length; index++) {
            map.put(params[index], values[index]);
        }
        sendAsyncRequest(httpMethod, url, map, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] param, @NonNull double[] value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (param.length == 0 && value.length == 0 && param.length != value.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < param.length; index++) {
            map.put(param[index], value[index]);
        }
        sendAsyncRequest(httpMethod, url, map, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] param, @NonNull char[] value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (param.length == 0 && value.length == 0 && param.length != value.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < param.length; index++) {
            map.put(param[index], value[index]);
        }
        sendAsyncRequest(httpMethod, url, map, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[] param, @NonNull boolean[] value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (param.length == 0 && value.length == 0 && param.length != value.length) {
            throw new IllegalArgumentException("2个数组长度不一致或者长度为0");
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int index = 0; index < param.length; index++) {
            map.put(param[index], value[index]);
        }
        sendAsyncRequest(httpMethod, url, map, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String[][] params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        HashMap<String, Object> map = new HashMap<>();
        for (String[] param : params) {
            map.put(param[0], param[1]);
        }
        sendAsyncRequest(httpMethod, url, map, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (!checkCurrentNetwork(url, httpResponseCallback))
            return;
        final String request = buildParams(params);
        setHttpResponseCallback(url, request, responseEntityClass, httpResponseCallback);

        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request_get = new Request.Builder().url(buildGetUrl(url, params)).build();
                call = httpClient.newCall(request_get);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
            case POST:
                final FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue().toString());
                }
                // 提交post表单请求
                final RequestBody formBody = formBodyBuilder.build();
                final Request request_post = new Request.Builder().url(url).post(formBody).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String json, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (checkCurrentNetwork(url, httpResponseCallback))
            return;
        setHttpResponseCallback(url, json, responseEntityClass, httpResponseCallback);

        switch (httpMethod) {
            case GET:
                try {
                    final Request request_get = new Request.Builder().url(buildGetUrl(url, jsonToMap(json))).build();
                    call = httpClient.newCall(request_get);
                    addRequest(call);
                    call.enqueue(httpResponseCallback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case POST:
                final RequestBody body = RequestBody.create(PARAM_TYPE_JSON, json);
                final Request request_post = new Request.Builder().url(url).post(body).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull JSONObject params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        sendAsyncRequest(httpMethod, url, params.toString(), responseEntityClass, httpResponseCallback);
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull RequestParams param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        sendAsyncRequest(httpMethod, url, param.toString(), responseEntityClass, httpResponseCallback);
    }

    @Override
    public <REQUEST> void sendAsyncRequest(@NonNull String url, @NonNull REQUEST params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {

    }

    @Override
    public <REQUEST> void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull REQUEST params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        d("sendAsyncRequest...");
        d("request param bean", params);
        final Gson gson = new Gson();
        final String jsonParam = gson.toJson(params, params.getClass()); // JsonUtil.toJson(params);
        d("json request param", jsonParam);
        sendAsyncRequest(httpMethod, url, jsonParam, responseEntityClass, httpResponseCallback);
    }

    @Override
    public void cancelRequests() {
        for (Call call : requests) {
            if (!call.isCanceled())
                call.cancel();
        }
        requests.clear();
        requests = null;
    }

    @Override
    public void cancelRequest() {
        if (!call.isCanceled())
            call.cancel();
        call = null;
    }

    @Override
    public void cancelRequest(@NonNull String tag) {

    }

    private void addRequest(@NonNull Call call) {
        if (call != null && !requests.contains(call)) {
            requests.add(call);
            d("发起了一个HTTP请求，当前一共有" + requests.size() + "个请求。");
        }
    }

    /**
     * json转成Map
     *
     * @param json json字符串
     * @return 封装了请求参数及其值的Map
     */
    private static HashMap<String, Object> jsonToMap(String json) throws JSONException {
        HashMap<String, Object> data = new HashMap<>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = new JSONObject(json);
        Iterator it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            String value = TextUtil.toString(jsonObject.get(key));
            data.put(key, value);
        }
        return data;
    }

    /**
     * 针对get请求，通过url和参数构建带参数的url
     *
     * @param url    最基本的url
     * @param params 请求的参数
     * @return 添加了参数的url
     */
    private String buildGetUrl(String url, Map<String, Object> params) {
        return url + buildParams(params);
    }

    /**
     * 构建GET请求参数
     *
     * @param params 请求参数Map键值对
     * @return 拼接的GET请求参数
     */
    private String buildParams(@NonNull Map<String, Object> params) {
        final StringBuilder sb = new StringBuilder("?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}