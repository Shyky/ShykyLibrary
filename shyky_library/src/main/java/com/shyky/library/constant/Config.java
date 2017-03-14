package com.shyky.library.constant;

/**
 * 配置常量类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/4/10
 * @since 1.0
 */
public class Config {
    /**
     * 本地数据库名称
     */
    public static final String DATABASE_NAME = "local_database.db";
    /**
     * 本地数据库版本号，每次更新新版本的时候，版本号自增1
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * Log日志控制开关
     */
    public static boolean LOG = true;

    /**
     * 构造方法私有化
     */
    protected Config() {

    }
}