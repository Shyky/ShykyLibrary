package com.shyky.library.exception;

/**
 * 没有重写toString方法异常,用于检查子类是否重写了父类的toString方法
 * Author: Created by Shyky on 2016/4/12.
 * Email: sj1510706@163.com
 */
public class NotOverrideToStringMethodException extends RuntimeException {
    public NotOverrideToStringMethodException() {
        this("没有重写toString方法,请使用IDE自动生成覆盖父类的toString方法");
    }

    public NotOverrideToStringMethodException(String detailMessage) {
        super(detailMessage);
    }
}