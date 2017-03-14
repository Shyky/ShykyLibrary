package com.shyky.library;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.shyky.library.bean.response.base.BaseResponse;
import com.shyky.library.util.JsonUtil;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 基于OkHttp二次封装简化http请求，支持GET、POST、PATCH、DELETE等请求方式，支持上
 * 传、下载文件、多线程断点续传、自动重试
 * <p>
 * <p>使用建造者模式，一行代码发送请求</p>
 * <p>支持多种请求参数，8种基本数据类型，文件类型，json类型，类类型，map类型，json对象类型</p>
 * <p>需要引入：compile 'com.squareup.okhttp3:okhttp:3.5.0'</p>
 * <p>例子：String response = new OkHttpUtil.Builder().url("www.baidu.com").get().execute();</p>
 * <p>例子：String response2 = new OkHttpUtil.Builder().url("www.baidu.com").param("key", "sss").execute(); </p>
 * <p>注意：如果不调用get()、post()、upload()等方法，直接execute()方法，默认调用get()，即
 * <p>new OkHttpUtil.Builder().url("www.baidu.com").get().execute()<br> 等同 <br>new OkHttpUtil.Builder().url("www.baidu.com").execute()</p>
 * </p>
 *
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/12/22
 * @since 1.0
 */
public final class OkHttpUtil {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_FILE = MediaType.parse("application/octet-stream");

    /**
     * 构造方法私有化
     */
    private OkHttpUtil() {

    }

    public static abstract class Callback<RESPONSE> {
        public abstract void onSuccess(int statusCode, @NonNull RESPONSE response);

        public void onLoading(long current, long total, float percent) {

        }

        public abstract void onFailure(int statusCode, @NonNull String message);
    }

    public static final class Builder {
        private static final int STATUS_SUCCESS = 1;
        private static final int STATUS_FAILURE = 2;
        private static final int STATUS_LOADING = 3;
        private static final int TYPE_GET = 11;
        private static final int TYPE_POST = 12;
        private static final int TYPE_UPLOAD = 17;
        private static final int TYPE_DOWNLOAD = 18;
        private Context context;
        private OkHttpClient httpClient;
        private HttpCallback httpCallback;
        private Callback callback;
        private Call call;
        private String url;
        private Class<?> clz;
        private String jsonParam;
        private HashMap<String, Object> params;
        private HashMap<String, Object> headers;
        private HashMap<String, Object> cookies;
        private int requestType;
        private String uploadFileName;
        private String savePath;
        private String saveFileName;
        private boolean isOverrideExists;
        /**
         * 响应内容数据格式，默认为json
         */
        private String responseContentType;
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                final int what = msg.what;
                if (what == STATUS_SUCCESS) {
                    callback.onSuccess(msg.arg1, msg.obj.toString());
                } else if (what == STATUS_LOADING) {
                    final Bundle data = msg.getData();
                    final long current = data.getLong("current");
                    final long total = data.getLong("total");
                    callback.onLoading(current, total, (float) current / total * 100);
                } else if (what == STATUS_FAILURE) {
                    callback.onFailure(-1, msg.obj.toString());
                }
            }
        };

        private class HttpCallback implements okhttp3.Callback {
            @Override
            public void onFailure(Call call, IOException e) {
                KLog.d("------------------------------异步HTTP请求------------------------------");
                KLog.d("请求地址（url）：" + url);
                final boolean hasParam = params.size() > 0;
                KLog.d("请求参数：" + (hasParam ? params.toString() : "无"));
                if (hasParam) {
                    KLog.d("请求参数格式：json");
                }
                final Message msg = Message.obtain();
                switch (requestType) {
                    case TYPE_GET:
                        KLog.d("请求方式：GET");
                        msg.what = STATUS_FAILURE;
                        msg.obj = e.getMessage();
                        break;
                    case TYPE_POST:
                        KLog.d("请求方式：POST");
                        msg.what = STATUS_FAILURE;
                        msg.obj = e.getMessage();
                        break;
                    case TYPE_UPLOAD:
                        KLog.d("请求方式：POST");
                        KLog.e("上传文件失败：文件路径 -> " + uploadFileName);
                        KLog.e("失败原因：" + e.toString());
                        msg.what = STATUS_FAILURE;
                        msg.obj = e.getMessage();
                        break;
                    case TYPE_DOWNLOAD:
                        KLog.e("下载文件失败 -> " + e.getMessage());
                        msg.what = STATUS_FAILURE;
                        msg.obj = e.getMessage();
                        break;
                }
                handler.sendMessage(msg);
                KLog.e("响应状态：请求失败");
                KLog.e("失败原因：" + e.toString());
                e.printStackTrace();
                KLog.d("-------------------------------------------------------------------------");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                KLog.d("------------------------------异步HTTP请求------------------------------");
                KLog.d("请求地址（url）：" + url);
                final boolean hasParam = params.size() > 0;
                KLog.d("请求参数：" + (hasParam ? params.toString() : "无"));
                if (hasParam) {
                    KLog.d("请求参数格式：json");
                }
                final int statusCode = response.code();
                final String downloadFileName = getFileName();
                final Message msg = Message.obtain();

                if (requestType == TYPE_DOWNLOAD) {
                    KLog.d("下载文件名：" + downloadFileName);
                    KLog.d("响应状态：成功");
                    KLog.d("响应状态码：" + statusCode);
                    msg.what = STATUS_LOADING;
                    InputStream is = null;
                    // 写到文件
                    byte[] buffer = new byte[2048];
                    int len;
                    FileOutputStream fos = null;
                    try {
                        final long total = response.body().contentLength();
                        final Bundle data = new Bundle();
                        data.putLong("total", total);
                        long current = 0L;
                        is = response.body().byteStream();
                        //  final String fileName = MD5.encode(fileUrl);
                        // 截取下载文件名
                        final File file = new File(savePath, getFileName());
                        fos = new FileOutputStream(file);
                        final DecimalFormat format = new DecimalFormat("#.0");
                        KLog.i("------------------------------开始下载文件------------------------------");
                        while ((len = is.read(buffer)) != -1) {
                            current += len;
                            fos.write(buffer, 0, len);
                            KLog.d("下载进度：" + (format.format(((float) current / total) * 100)) + "%  当前：" + convertAndFormatFileSize(current) + "（" + current + " byte）" + " / 总共：" + convertAndFormatFileSize(total) + "（" + total + " byte）");
                            data.putLong("current", current);
                        }
                        fos.flush();
                        msg.setData(data);
                        msg.obj = file.getAbsolutePath();
                        KLog.i("------------------------------下载文件结束------------------------------");
                        KLog.d("下载状态：完成");
                        KLog.d("下载文件保存路径：" + savePath);
                        KLog.d("本地保存文件名：" + file.getAbsolutePath());
                        msg.what = STATUS_SUCCESS;
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        KLog.e("下载状态：失败");
                        KLog.e("失败原因：" + e.toString());
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (response.isSuccessful()) {
                    final String result = response.body().string();
                    switch (requestType) {
                        case TYPE_GET:
                            KLog.d("请求方式：GET");
                            msg.arg1 = statusCode;
                            msg.what = STATUS_SUCCESS;
                            msg.obj = result;
                            break;
                        case TYPE_POST:
                            KLog.d("请求方式：POST");
                            msg.arg1 = statusCode;
                            msg.what = STATUS_SUCCESS;
                            msg.obj = result;
                            break;
                        case TYPE_UPLOAD:
                            KLog.d("上传文件名：" + uploadFileName);
                            msg.arg1 = statusCode;
                            msg.what = STATUS_SUCCESS;
                            msg.obj = result;
                            break;
                    }
                    handler.sendMessage(msg);
                    KLog.d("响应状态：成功");
                    KLog.d("响应状态码：" + statusCode);
                    KLog.d("响应内容数据格式：" + responseContentType);
                    KLog.d("响应内容：");
                    KLog.json(result);
                } else {

                }
                KLog.d("-------------------------------------------------------------------------");
            }
        }

        public Builder() {
            httpClient = new OkHttpClient();
            params = new HashMap<>();
            headers = new HashMap<>();
            cookies = new HashMap<>();
            // 默认发送get请求
            requestType = TYPE_GET;
            responseContentType = "json";
        }

        public Builder(@NonNull Context context) {
            this.context = context;
            httpClient = new OkHttpClient();
            params = new HashMap<>();
            headers = new HashMap<>();
            cookies = new HashMap<>();
            // 默认发送get请求
            requestType = TYPE_GET;
            responseContentType = "json";
        }

        public Builder url(@NonNull String url) {
            if (TextUtils.isEmpty(url) || url.trim().length() == 0 || url.equals("null"))
                throw new NullPointerException("url不能为空");
            this.url = url;
            // 自动补全http://前缀
            if (url.indexOf("http://") == -1) {
                this.url = "http://" + url;
            }
            return this;
        }

        public Builder header(@NonNull String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder cookie(@NonNull String name, String value) {
            cookies.put(name, value);
            return this;
        }

        /**
         * 请求参数格式为json
         *
         * @param jsonParamOrFileName json格式请求参数
         * @return Builder对象
         */
        public Builder param(@NonNull String jsonParamOrFileName) {
            if (new File(jsonParamOrFileName).exists()) {
                uploadFileName = jsonParamOrFileName;
            } else
                jsonParam = jsonParamOrFileName;
            return this;
        }

        public Builder param(@NonNull File file) {
            if (file == null || !file.exists())
                throw new NullPointerException("指定文件不存在");
            uploadFileName = file.getAbsolutePath();
            return this;
        }

        public Builder param(@NonNull JSONObject params) {
            final Gson gson = new Gson();
            jsonParam = gson.toJson(params);
            return this;
        }

        public <REQUEST> Builder param(@NonNull REQUEST params) {
            final Gson gson = new Gson();
            jsonParam = gson.toJson(params, params.getClass()); // JsonUtil.toJson(params);
            return this;
        }

        public Builder param(@NonNull String name, @NonNull File file) {
            if (file == null || !file.exists())
                throw new NullPointerException("指定文件不存在");
            uploadFileName = file.getAbsolutePath();
            params.put(name, file);
            return this;
        }

        public Builder param(@NonNull String name, char value) {
            params.put(name, value);
            return this;
        }

        public Builder param(@NonNull String name, boolean value) {
            params.put(name, value);
            return this;
        }

        public Builder param(@NonNull String name, int value) {
            params.put(name, value);
            return this;
        }

        public Builder param(@NonNull String name, long value) {
            params.put(name, value);
            return this;
        }

        public Builder param(@NonNull String name, float value) {
            params.put(name, value);
            return this;
        }

        public Builder param(@NonNull String name, double value) {
            params.put(name, value);
            return this;
        }

        public Builder param(@NonNull String name, String valueOrFileName) {
            if (new File(valueOrFileName).exists()) {
                uploadFileName = valueOrFileName;
            } else
                params.put(name, valueOrFileName);
            return this;
        }

        public Builder param(@NonNull String[] names, String[] values) {
            if (names == null || values == null)
                throw new NullPointerException("参数不能为空");
            else if (names.length != values.length)
                throw new IllegalArgumentException("第一个参数与第二个参数的个数不一致");
            for (int j = 0; j < names.length; j++)
                params.put(names[j], values[j]);
            return this;
        }

        public Builder param(@NonNull String[] names, Object[] values) {
            if (names == null || values == null)
                throw new NullPointerException("参数不能为空");
            else if (names.length != values.length)
                throw new IllegalArgumentException("第一个参数与第二个参数的个数不一致");
            for (int j = 0; j < names.length; j++)
                params.put(names[j], values[j]);
            return this;
        }

        public Builder param(@NonNull HashMap<String, Object> params) {
            this.params = null;
            this.params = params;
            return this;
        }

        public Builder get() {
            requestType = TYPE_GET;
            return this;
        }

        public Builder get(Class<?> clz) {
            requestType = TYPE_GET;
            this.clz = clz;
            return this;
        }

        public Builder post() {
            requestType = TYPE_POST;
            return this;
        }

        public Builder post(Class<?> clz) {
            requestType = TYPE_POST;
            this.clz = clz;
            return this;
        }

        public Builder upload() {
            requestType = TYPE_UPLOAD;
            return this;
        }

        /**
         * 下载文件
         *
         * @param savePathOrFileName 文件下载要保存的路径或文件名
         * @return Builder对象
         */
        public Builder download(@NonNull String savePathOrFileName) {
            return download(new File(savePathOrFileName));
        }

        /**
         * 下载文件
         *
         * @param savePathOrFileName 文件下载要保存的路径或文件名
         * @return Builder对象
         */
        public Builder download(@NonNull String savePathOrFileName, boolean isOverrideExists) {
            this.isOverrideExists = isOverrideExists;
            return download(new File(savePathOrFileName));
        }

        /**
         * 下载文件
         *
         * @param savePath 文件下载完成保存的路径
         * @return Builder对象
         */
        public Builder download(@NonNull String savePath, @NonNull String fileName) {
            return download(new File(savePath), fileName);
        }

        /**
         * 下载文件
         *
         * @param file 文件下载完成保存的路径
         * @return Builder对象
         */
        public Builder download(@NonNull File file) {
            if (file == null || !file.exists())
                throw new NullPointerException("指定路径不存在");
            else if (TextUtils.isEmpty(url) || url.trim().length() == 0)
                throw new NullPointerException("保存的文件名不能为空");
            savePath = file.getAbsolutePath();
            requestType = TYPE_DOWNLOAD;
            return this;
        }

        /**
         * 下载文件
         *
         * @param file 文件下载完成保存的路径
         * @return Builder对象
         */
        public Builder download(@NonNull File file, @NonNull String fileName) {
            if (file == null || !file.exists())
                throw new NullPointerException("指定路径不存在");
            else if (TextUtils.isEmpty(url) || url.trim().length() == 0)
                throw new NullPointerException("保存的文件名不能为空");
            savePath = file.getAbsolutePath();
            saveFileName = fileName;
            requestType = TYPE_DOWNLOAD;
            return this;
        }

        /**
         * 执行同步请求
         *
         * @return http响应结果
         */
        public String execute() {
            String result = null;
            switch (requestType) {
                case TYPE_GET:
                    final Request request_get = new Request.Builder().url(url).build();
                    try {
                        final Response response = httpClient.newCall(request_get).execute();
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        result = null;
                    }
                    break;
                case TYPE_POST:
                    final FormBody.Builder builder = new FormBody.Builder();
                    // 将map中保存的参数添加到FormBody.Builder
                    final Set<Map.Entry<String, Object>> entries = params.entrySet();
                    final Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                    while (iterator.hasNext()) {
                        final Map.Entry<String, Object> entry = iterator.next();
                        builder.add(entry.getKey(), entry.getValue().toString());
                    }
                    RequestBody requestBody = builder.build();
                    // 提交post json请求
                    if (jsonParam != null) {
                        requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonParam);
                    }
                    final Request request_post = new Request.Builder().url(url).post(requestBody).build();
                    try {
                        final Response response = httpClient.newCall(request_post).execute();
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TYPE_UPLOAD:
                    break;
                case TYPE_DOWNLOAD:
//                    final String fileName = MD5.encode(fileUrl);
                    String fileName = saveFileName == null ? getFileName() : saveFileName;
                    final File file = new File(savePath, fileName);
                    // 如果开启了覆盖，则先删除已经存在的同名文件
                    if (isOverrideExists) {
                        file.delete();
                    }
                    result = file.getAbsolutePath();
                    if (!file.exists()) {
                        final Request request = new Request.Builder().url(url).build();
                        try {
                            final Response response = httpClient.newCall(request).execute();
                            InputStream is = null;
                            // 写到文件
                            byte[] buffer = new byte[2048];
                            int len;
                            FileOutputStream fos = null;
                            try {
                                final long total = response.body().contentLength();
                                KLog.d("total -> " + total);
                                long current = 0;
                                is = response.body().byteStream();
                                fos = new FileOutputStream(file);
                                while ((len = is.read(buffer)) != -1) {
                                    current += len;
                                    fos.write(buffer, 0, len);
                                    KLog.d("current -> " + current);
                                }
                                fos.flush();
                                KLog.d("下载文件完成...");
                            } catch (IOException e) {
                                KLog.e("下载文件失败 -> " + e.getMessage());
                            } finally {
                                try {
                                    if (is != null) {
                                        is.close();
                                    }
                                    if (fos != null) {
                                        fos.close();
                                    }
                                } catch (IOException e) {
                                    KLog.e("关闭文件流失败 -> " + e.toString());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            return result;
        }

        /**
         * 执行同步请求并把响应结果数据转换成指定的数据类型
         *
         * @param clz        请求成功返回解析对应的实体类的class字节码
         * @param <RESPONSE> 泛型参数，HTTP响应数据解析对应的实体
         * @return 转换之后的类型
         */
        public <RESPONSE> RESPONSE execute(@NonNull Class<RESPONSE> clz) {
            RESPONSE response = null;
            final String result = execute();
            if (clz == null || clz == String.class) {
                return (RESPONSE) result;
            } else if (clz == JSONObject.class) {
                if (JsonUtil.isJsonObject(result)) {
                    KLog.d("转换成JSONObject...");
                    try {
                        JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
                        response = (RESPONSE) object;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (clz == JSONArray.class) {
                if (JsonUtil.isJsonArray(result)) {
                    KLog.d("转换成JSONArray...");
                    try {
                        final JSONArray object = (JSONArray) new JSONTokener(result).nextValue();
                        response = (RESPONSE) object;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // 获取它的父类的class是否是BaseResponse
                try {
                    response = clz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (response != null && response instanceof BaseResponse) {
                    KLog.d("转换成BaseResponse的子类...");
//                    new HttpResponseCallback.DataParserAsyncTask(statusCode).execute(responseString);
                } else {
                    KLog.d("转换成别的数据类型...");
                    KLog.d("使用FastJson库解析...");
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
            return response;
        }

        public void execute(@NonNull Callback callback) {
            this.callback = callback;
            httpCallback = new HttpCallback();
            switch (requestType) {
                case TYPE_GET:
                    final Request request = new Request.Builder().url(url).build();
                    call = httpClient.newCall(request);
                    call.enqueue(httpCallback);
                    break;
                case TYPE_POST:
                    final FormBody.Builder builder = new FormBody.Builder();
                    // 将map中保存的参数添加到FormBody.Builder
                    final Set<Map.Entry<String, Object>> entries = params.entrySet();
                    final Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                    while (iterator.hasNext()) {
                        final Map.Entry<String, Object> entry = iterator.next();
                        builder.add(entry.getKey(), entry.getValue().toString());
                    }
                    RequestBody requestBody = builder.build();
                    // 提交post json请求
                    if (jsonParam != null) {
                        requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonParam);
                    }
                    final Request request_post = new Request.Builder().url(url).post(requestBody).build();
                    call = httpClient.newCall(request_post);
                    call.enqueue(httpCallback);
                    break;
                case TYPE_UPLOAD:
                    RequestBody body;
                    // 不带参数上传文件
                    if (params.size() == 0) {
                        if (uploadFileName == null)
                            throw new NullPointerException("要上传的文件为空");
                        final File file = new File(uploadFileName);
                        body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    } else {
                        // 带参数上传文件
                        final MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                        // 设置类型
                        multipartBuilder.setType(MultipartBody.FORM);
                        // 追加参数
                        for (String key : params.keySet()) {
                            final Object object = params.get(key);
                            if (!(object instanceof File)) {
                                multipartBuilder.addFormDataPart(key, object.toString());
                            } else {
                                final File file = (File) object;
                                multipartBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE_FILE, file));
                            }
                        }
                        // 创建RequestBody
                        body = multipartBuilder.build();
                    }
                    final Request request_upload = new Request.Builder().url(url).post(body).build();
                    call = httpClient.newCall(request_upload);
                    call.enqueue(httpCallback);
                    break;
                case TYPE_DOWNLOAD:
                    final Request request_download = new Request.Builder().url(url).build();
                    call = httpClient.newCall(request_download);
                    call.enqueue(httpCallback);
                    break;
            }
        }

        /**
         * 根据url获取文件名
         *
         * @return
         */
        private String getFileName() {
            // 截取下载文件名
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            return fileName;
        }

        private String convertAndFormatFileSize(long length) {
            // final float block = 1024.0f;
            final DecimalFormat format = new DecimalFormat("#.00");
            if (length >= 1024 && length < 1024 * 1024)
                return length / 1024 + "K";
            else if (length >= 1024 * 1024 && length < 1024 * 1024 * 1024)
                return format.format((double) length / 1024 / 1024) + "M";
            else if (length >= 1024 * 1024 * 1024 && length < 1024 * 1024 * 1024 * 1024)
                return format.format((double) length / 1024 / 1024 / 1024) + "G";
            else if (length >= 1024 * 1024 * 1024 * 1024)
                return format.format((double) length / 1024 / 1024 / 1024 / 1024) + "T";
            else
                return length + "B";
        }
    }

    public static void setTimeout(long timeout, TimeUnit unit) {

    }
}