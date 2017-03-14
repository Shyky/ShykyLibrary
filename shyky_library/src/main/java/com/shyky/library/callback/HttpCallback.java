package com.shyky.library.callback;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 抽象的http请求回调监听类
 *
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/6/3
 * @since 1.0
 */
public abstract class HttpCallback implements Callback {
    /**
     * 请求成功标识
     */
    private static final int STATE_SUCCESS = 11;
    /**
     * 请求失败标识
     */
    private static final int STATE_FAILURE = 22;
    /**
     * 消息处理器
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.obj != null) {
                if (msg.what == STATE_SUCCESS) {
                    final int code = msg.arg1;
                    if (code == 404 || code == 500)
                        onFailure(code, msg.obj.toString());
                    else
                        onSuccess(msg.arg1, msg.obj.toString());
                } else if (msg.what == STATE_FAILURE) {
                    onFailure(msg.arg1, msg.obj.toString());
                }
            }
        }
    };

    /**
     * 当请求成功时会调用此方法
     *
     * @param statusCode HTTP响应状态码
     * @param response   返回的字符串数据
     */
    public abstract void onSuccess(int statusCode, @NonNull String response);

    /**
     * 当请求失败时会调用此方法
     *
     * @param statusCode Http响应状态码，-1表示没有网络
     * @param message    异常或错误消息
     */
    public abstract void onFailure(int statusCode, @NonNull String message);

    @Override
    public final void onFailure(Call call, IOException e) {
        final String message = e.getMessage();
        final Message msg = Message.obtain();
        msg.obj = message;
        msg.arg1 = 1;
        msg.what = STATE_FAILURE;
        handler.sendMessage(msg);
    }

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        // 这个方法不是在主线程中调用，不能在回调中更新UI
        try {
            final String responseString = response.body().string();
            final int code = response.code();
            final Message msg = Message.obtain();
            msg.obj = responseString;
            msg.arg1 = code;
            msg.what = STATE_SUCCESS;
            handler.sendMessage(msg);
        } catch (SocketTimeoutException e) {
            final Message msg = Message.obtain();
            msg.obj = e.getMessage();
            msg.arg1 = 0;
            msg.what = STATE_FAILURE;
            handler.sendMessage(msg);
        }
    }
}