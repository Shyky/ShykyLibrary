package com.shyky.library.model;

import android.support.annotation.NonNull;

import com.shyky.library.constant.Constant;
import com.shyky.library.model.impl.IUserModel;
import com.shyky.library.util.SharedPreferencesUtil;
import com.shyky.util.TextUtil;

import static com.socks.library.KLog.d;

/**
 * 用户相关的基础业务，本类不提供所有的用户业务（也无法提供），只提供一些基础的用户业务，如判断是否登录、注销登录、获取用户ID等基础功能
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/5/10
 * @since 1.0
 */
public class UserModel implements IUserModel {
    @Override
    public boolean isLogin() {
        final String userName = SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_NAME);
        final String email = SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_EMAIL);
        final String phone = SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_PHONE);
        final boolean isLogin = SharedPreferencesUtil.getBoolean(Constant.SHARED_KEY.IS_LOGIN);
        d("email = " + email);
        d("phone = " + phone);
        d("userName = " + userName);
        d("isLogin = " + isLogin);
        // 邮箱（用户名）不为空（不能为字符串null）且isLogin为true表示已经登录
        if (!TextUtil.isEmptyAndNull(email) && isLogin)
            return true;
        else if (!TextUtil.isEmptyAndNull(phone) && isLogin)
            return true;
        else if (!TextUtil.isEmptyAndNull(userName) && isLogin)
            return true;
        return false;
    }

    @Override
    public boolean writeLogin() {
        return SharedPreferencesUtil.save(Constant.SHARED_KEY.IS_LOGIN, true);
    }

    @Override
    public boolean logout(boolean isClearUserSavedData) {
        if (isClearUserSavedData) {
            // 先取出登录过的用户名、邮箱或手机号及登录类型（区别第三方登录）
            final String userName = SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_NAME);
            final String phone = SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_PHONE);
            final String email = SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_EMAIL);
            final String loginType = SharedPreferencesUtil.getString(Constant.SHARED_KEY.LOGIN_TYPE);
            // 清除所有的配置数据
            final boolean status = SharedPreferencesUtil.clear();
            // 解决注销登录之后，不会显示登录过的用户名问题
            SharedPreferencesUtil.save(Constant.SHARED_KEY.USER_NAME, userName);
            SharedPreferencesUtil.save(Constant.SHARED_KEY.USER_PHONE, phone);
            SharedPreferencesUtil.save(Constant.SHARED_KEY.USER_EMAIL, email);
            SharedPreferencesUtil.save(Constant.SHARED_KEY.LOGIN_TYPE, loginType); // 保存登录类型，是否APP正常登录还是第三方登录
            SharedPreferencesUtil.save(Constant.SHARED_KEY.IS_LOGIN, false);// 标识为没有登录
            return status;
        } else
            return SharedPreferencesUtil.save(Constant.SHARED_KEY.IS_LOGIN, false);
    }

    @NonNull
    @Override
    public String readUserId() {
        return SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_ID);
    }

    @NonNull
    @Override
    public String readUserName() {
        return SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_NAME);
    }

    @NonNull
    @Override
    public String readUserNickName() {
        return SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_NICKNAME);
    }

    @Override
    public String readUserHeaderImage() {
        return SharedPreferencesUtil.getString(Constant.SHARED_KEY.USER_HEADER);
    }
}