package com.shyky.library.callback;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.shyky.library.bean.response.base.BaseResponse;
import com.shyky.library.model.base.HttpModel;
import com.shyky.library.util.JsonUtil;
import com.shyky.util.HtmlUtil;
import com.shyky.util.TextUtil;
import com.shyky.util.XmlUtil;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import static com.socks.library.KLog.d;

/**
 * http请求回调监听类
 *
 * @param <RESPONSE> 泛型参数，HTTP响应数据解析对应的实体
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.7
 * @email sj1510706@163.com
 * @date 2016/4/7
 * @since 1.0
 */
public abstract class HttpResponseCallback<RESPONSE> extends HttpCallback {
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求参数字符串
     */
    private String request;
    /**
     * 请求成功返回解析对应的实体类的class字节码
     */
    private Class<RESPONSE> responseEntityClass;
    /**
     * 安全策略，用于加解密请求
     */
    private HttpModel.SecurityPolicy securityPolicy;

    /**
     * 设置请求的URL
     *
     * @param url 统一资源定位符
     */
    public final void setUrl(@NonNull String url) {
        this.url = url;
    }

    /**
     * 设置请求参数
     *
     * @param request 请求参数
     */
    public final void setRequest(@NonNull String request) {
        this.request = request;
    }

    /**
     * 设置HTTP响应数据解析对应的实体
     *
     * @param responseEntityClass HTTP响应数据解析对应的实体
     */
    public final void setResponseEntityClass(@NonNull Class<RESPONSE> responseEntityClass) {
        this.responseEntityClass = responseEntityClass;
    }

    /**
     * 设置安全策略
     *
     * @param securityPolicy 安全策略接口
     */
    public final void setSecurityPolicy(@NonNull HttpModel.SecurityPolicy securityPolicy) {
        this.securityPolicy = securityPolicy;
    }

    /**
     * 请求成功，指服务端成功响应并返回正确的数据
     *
     * @param url
     * @param request
     * @param statusCode
     * @param response
     */
    public abstract void onSuccess(@NonNull String url, @NonNull String request, int statusCode, @NonNull RESPONSE response);

    /**
     * 请求失败，指网络问题，如没有网络或网络慢等问题
     *
     * @param url        请求的网络地址
     * @param request    请求参数文本
     * @param statusCode 状态码，如果没有网络返回-1，否则返回其它正常的网络响应码
     * @param message    响应消息文本
     * @param response   响应实体泛型
     */
    public abstract void onFailure(@NonNull String url, @NonNull String request, int statusCode, @NonNull String message, @NonNull RESPONSE response);

    /**
     * 请求进度
     *
     * @param current
     * @param total
     * @param percent
     */
    public void onLoading(long current, long total, float percent) {

    }

    /**
     * 请求完成，可以在这个方法中处理不管请求成功还是失败，还是没有数据，还是网络错误都会执行的动作
     */
    public void onFinish() {

    }

    /**
     * 数据解析（将返回的数据转成实体类）处理异步任务
     */
    private class DataParserAsyncTask extends AsyncTask<String, Void, Boolean> {
        private int statusCode;
        private RESPONSE responseEntity;

        public DataParserAsyncTask(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        protected Boolean doInBackground(final String... params) {
            try {
                // 实例化对象
                responseEntity = responseEntityClass.newInstance();
                ((BaseResponse) responseEntity).convert(params[0]);
                return true;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                onSuccess(url, request.trim(), statusCode, responseEntity);
                KLog.d("onSuccess(onPostExecute) --> url = " + url + " , request = " + request.trim() + " , statusCode = " + statusCode + " , response = " + responseEntity);
                handleFinish();
            } else
                handleFailure(url, request.trim(), statusCode, "数据解析处理失败。", null);
        }
    }

    /**
     * 处理请求成功回调
     *
     * @param url
     * @param request
     * @param statusCode
     * @param response
     */
    protected final void handleSuccess(@NonNull String url, @NonNull String request, int statusCode, @NonNull String response) {
        String responseString = response;
        // 是否添加了安全策略，如果添加了，则需要对应返回的数据进行解密
        if (securityPolicy != null) {
            // 解密返回数据
            responseString = securityPolicy.decrypt(responseString);
        }

        // 数据非空判断
        if (TextUtil.isEmptyAndNull(responseString)) {
            handleFailure(url, request.trim(), statusCode, "返回的数据是空或null", null);
        } else {
            // 网络请求成功后另外开启一个异步任务去解析数据，防止造成UI卡死
            if (JsonUtil.isJson(responseString)) {
                if (responseEntityClass == JSONObject.class) {
                    if (JsonUtil.isJsonObject(responseString)) {
                        d("转换成JSONObject...");
                        try {
                            JSONObject object = (JSONObject) new JSONTokener(responseString).nextValue();
                            onSuccess(url, request.trim(), statusCode, (RESPONSE) object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (responseEntityClass == JSONArray.class) {
                    if (JsonUtil.isJsonArray(responseString)) {
                        d("转换成JSONArray...");
                        try {
                            JSONArray object = (JSONArray) new JSONTokener(responseString).nextValue();
                            onSuccess(url, request.trim(), statusCode, (RESPONSE) object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (responseEntityClass == String.class || responseEntityClass == null) {
                    onSuccess(url, request.trim(), statusCode, (RESPONSE) responseString);
                } else {
                    // 获取它的父类的class是否是BaseResponse
                    RESPONSE resp = null;
                    try {
                        resp = responseEntityClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (resp != null && resp instanceof BaseResponse) {
                        d("转换成BaseResponse的子类...");
                        new DataParserAsyncTask(statusCode).execute(responseString);
                        return;
                    } else {
                        d("转换成别的数据类型...");
                        d("使用FastJson库解析...");
//                        try {
////                            final RESPONSE respEntity = JsonUtil.deserialize(responseString, responseEntityClass);
//                            final RESPONSE respEntity = JSON.parseObject(responseString, responseEntityClass);
//                            onSuccess(url, request.trim(), statusCode, respEntity);
//                        } catch (Exception e) {
//                            d("json转换对象时发生错误", e.getMessage());
//                            onFailure(url, request.trim(), statusCode, "Json转换失败", null);
//                        }
                        //  throw new ClassCastException(responseEntityClass.toString() + "必须要继承BaseResponse这个类或者它的子类才能支持解析成实体类。");
                        // 否则用反射去解析，不建议使用反射
                        //  response_success_entity =(RESPONSE_SUCCESS_ENTITY) JsonUtil.json2Obj(responseString, response_success_entityClass);
                    }
                }
            } else if (XmlUtil.isXml(responseString)) {

            } else if (HtmlUtil.isHtml(responseString)) {

            } else {
                // 别的数据格式
                // response_failure_entity = (RESPONSE_FAILURE_ENTITY) responseString;
                // 解析数据失败
                handleFailure(url, request.trim(), statusCode, "未知的返回数据格式.", null);
            }
        }
        handleFinish();
    }

    protected final void handleLoading(long current, long total) {
        onLoading(current, total, (current / total) * 100.0f);
    }

    protected final void handleFailure(@NonNull String url, @NonNull String request, int statusCode, @NonNull String message, @NonNull String response) {
        KLog.e("onFailure --> url = " + url + " , request = " + (request == null ? "null" : request.trim()) + " , statusCode = " + statusCode + " , message = " + message);
        onFailure(url, request == null ? null : request.trim(), statusCode, message, response == null ? null : (RESPONSE) response);
        handleFinish();
    }

    protected final void handleFinish() {
        d("onFinish...");
        onFinish();
    }

    @Override
    public void onSuccess(int statusCode, @NonNull String response) {
        handleSuccess(url, request, statusCode, response);
    }

    @Override
    public void onFailure(int statusCode, @NonNull String message) {
        handleFailure(url, request, statusCode, message, null);
    }
}