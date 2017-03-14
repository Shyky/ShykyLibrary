package com.shyky.library.exception;

/**
 * 不支持的toString返回格式异常,用于检查toString方法返回的字符串格式是否是指定的格式
 * Author: Created by Shyky on 2016/4/12.
 * Email: sj1510706@163.com
 */
public class NotSupportToStringFormatException extends RuntimeException {
    public NotSupportToStringFormatException() {
        this("不支持的toString返回格式,格式为 : 类名称{\n" +
                "成员变量1(变量数据类型)= xx\n" +
                ", 成员变量2(short)= xx\n" +
                ", 成员变量3(boolean)= xx\n" +
                ", 成员变量4(String)='xx'\n" +
                ", 成员变量5(如果自定义的类类型,则需要使用包名+类名,如com.demo.bean.Person)='xx'\n" +
                "...\n" +
                "'}\n'");
    }

    public NotSupportToStringFormatException(String detailMessage) {
        super(detailMessage);
    }
}