package com.shyky.library.presenter;

import com.shyky.library.model.UserModel;

/**
 * 用户业务与界面呈接器
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/10
 * @since 1.0
 */
public class UserPresenter {
    /**
     * 用户业务
     */
    private UserModel userModel;

    /**
     * 构造方法
     */
    public UserPresenter() {
        userModel = new UserModel();
    }

    public final boolean isLogin() {
        return userModel.isLogin();
    }

    public final boolean logout() {
        return logout(false);
    }

    public final boolean logout(boolean isClearUserSavedData) {
        return userModel.logout(isClearUserSavedData);
    }

    public final String readUserId() {
        return userModel.readUserId();
    }

    public final boolean writeLogin() {
        return userModel.writeLogin();
    }
}