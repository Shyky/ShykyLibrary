package com.shyky.library.model.impl;

import android.support.annotation.NonNull;

/**
 * 用户业务接口
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/5/10
 * @since 1.0
 */
public interface IUserModel {
    /**
     * 判断用户是否登录
     *
     * @return 已经登录返回true，否则返回false
     */
    boolean isLogin();

    /**
     * 标识用户已登录
     *
     * @return 成功返回true，失败返回false
     */
    boolean writeLogin();

    /**
     * 注销登录
     *
     * @param isClearUserSavedData 是否清除用户已经保存的数据
     * @return 成功返回true，失败返回false
     */
    boolean logout(boolean isClearUserSavedData);

    /**
     * 读取保存在配置文件中的用户ID
     *
     * @return 用户ID
     */
    @NonNull
    String readUserId();

    /**
     * 读取保存在配置文件中的用户名称
     *
     * @return 用户名称
     */
    @NonNull
    String readUserName();

    /**
     * 读取保存在配置文件中的用户昵称
     *
     * @return 用户昵称
     */
    @NonNull
    String readUserNickName();

    /**
     * 读取保存在配置文件中的用户头像网络地址
     *
     * @return 用户头像网络地址
     */
    @NonNull
    String readUserHeaderImage();
}