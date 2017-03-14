package com.shyky.library.exception;

/**
 * 字符串为空运行时异常
 *
 * @author Shyky
 * @version 1.0
 * @date 2016/6/8
 * @since 1.0
 */
public class StringIsEmptyException extends RuntimeException {
    public StringIsEmptyException() {
        this("字符串对象为null或长度为0");
    }

    public StringIsEmptyException(String detailMessage) {
        super(detailMessage);
    }
}