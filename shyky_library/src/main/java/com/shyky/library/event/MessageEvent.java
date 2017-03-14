package com.shyky.library.event;

import android.support.annotation.NonNull;

/**
 * EventBus发送消息事件
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/4/15
 * @since 1.0
 */
public final class MessageEvent {
    public int what;
    public int arg1;
    public int arg2;
    public String message;
    public Object object;

    /**
     * 构造方法
     */
    public MessageEvent() {

    }

    public MessageEvent(int what) {
        this.what = what;
    }

    public MessageEvent(@NonNull String message) {
        this.message = message;
    }

    public MessageEvent(@NonNull Object object) {
        this.object = object;
    }

    public MessageEvent(int what, @NonNull String message) {
        this.what = what;
        this.message = message;
    }

    public MessageEvent(int what, @NonNull Object object) {
        this.what = what;
        this.object = object;
    }

    public MessageEvent(int what, int arg1) {
        this.what = what;
        this.arg1 = arg1;
    }

    public MessageEvent(int what, int arg1, int arg2) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public MessageEvent(int what, int arg1, @NonNull String message) {
        this.what = what;
        this.arg1 = arg1;
        this.message = message;
    }

    public MessageEvent(int what, int arg1, @NonNull Object object) {
        this.what = what;
        this.arg1 = arg1;
        this.object = object;
    }

    public MessageEvent(int what, int arg1, int arg2, @NonNull String message) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.message = message;
    }

    public MessageEvent(int what, int arg1, int arg2, @NonNull String message, @NonNull Object object) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.message = message;
        this.object = object;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "what=" + what +
                ", arg1=" + arg1 +
                ", arg2=" + arg2 +
                ", message='" + message + '\'' +
                ", object=" + object +
                '}';
    }
}