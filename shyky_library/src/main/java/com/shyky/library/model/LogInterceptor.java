package com.shyky.library.model;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

import static com.socks.library.KLog.d;
import static com.socks.library.KLog.e;
import static com.socks.library.KLog.i;
import static com.socks.library.KLog.json;

/**
 * OkHttp Log日志拦截器
 *
 * @author Shyky
 * @version 1.2
 * @date 2017/1/2
 * @since 1.0
 */
public final class LogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        i("------------------------------异步Http请求开始------------------------------");
        final Request request = chain.request();
        d("请求地址（URL）：" + request.url().toString());
        String method = "GET";
        // 判断Method类型
        if (request.method().equals("GET")) {
            method = "GET";
        } else if (request.method().equals("POST")) {
            method = "POST";
        } else if (request.method().equals("PUT")) {
            method = "PUT";
        } else if (request.method().equals("DELETE")) {
            method = "DELETE";
        }
        d("请求方式：" + method);
        d("请求参数：" + bodyToString(request.body()));
        final Headers headers = request.headers();
        d("请求头：" + (headers.size() == 0 ? "无" : headers.toString()));
        Response response;
        try {
            final long startTime = System.nanoTime();
            response = chain.proceed(request); // OkHttp链式调用
            final long endTime = System.nanoTime();
            long time = (endTime - startTime) / 1000 / 1000 / 1000;   // 用请求后的时间减去请求前的时间得到耗时
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            final Buffer buffer = source.buffer();
            d("请求耗时：" + time + " S");
            d("响应状态：成功");
            d("响应状态码：" + response.code());
            final String result = buffer.clone().readString(UTF8);
            d("响应内容：");
            json(result);
        } catch (Exception e) {
            e("响应状态：失败");
            e("失败原因：" + e.toString());
            throw e;
        }
        i("------------------------------异步Http请求结束------------------------------");
        return response;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "无";
        }
    }
}