package com.shyky.library;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.shyky.library.constant.Constant;
import com.shyky.library.util.SharedPreferencesUtil;
import com.shyky.util.ObjectUtil;
import com.socks.library.KLog;

import static com.socks.library.KLog.d;

/**
 * 基础的Application，所有的Context都是从这里统一获取的
 * 注意：如果引用了当前库一定要继承这个类，不然使用了这个库中的一些类会报错
 *
 * @author Copyright(C)2011-2017 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/3/18
 * @since 1.0
 */
public abstract class BaseApplication extends MultiDexApplication {
    /**
     * 本类实体对象
     */
    private static BaseApplication instance;
    /**
     * 设备屏幕宽度
     */
    private static int width;
    /**
     * 设备屏幕高度
     */
    private static int height;

    /**
     * 获取APP当前版本号
     *
     * @return APP版本号
     */
    @NonNull
    protected abstract String getAppVersion();

    /**
     * 获取应用构建类型
     * {@link BuildConfig.BUILD_TYPE}
     *
     * @return 构建类型字符串
     */
    @NonNull
    protected abstract String getBuildType();

    /**
     * 获取本类实例
     *
     * @return 实例对象
     */
    public static final BaseApplication getInstance() {
        return instance;
    }

    /**
     * 获取应用程序上下文
     *
     * @return 应用程序上下文
     */
    public static final Context getContext() {
        final BaseApplication instance = getInstance();
        if (instance == null)
            throw new NullPointerException("如果使用了shyky_library中的类，必须自定义Application继承com.shyky.library.BaseApplication，并在清单文件中配置你的Application类");
        return instance.getApplicationContext();
    }

    /**
     * 初始化Log
     */
    protected void initLog() {
        // 控制KLog日志打印
        final String buildType = getBuildType();
        if (ObjectUtil.notNull(buildType)) {
            if (buildType.equals("debug")) {
                KLog.init(true, "测试版，测试服务器环境");
            } else if (buildType.equals("develop")) {
                KLog.init(true, "开发版，正式服务器环境");
            } else if (buildType.equals("deploy")) {
                KLog.init(true, "预发布版，预发布服务器环境");
            } else if (buildType.equals("release")) {
                // 正式版不要打印Log日志
                KLog.init(false);
            }
        }
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        d("onCreate...");
        instance = this;

        // 初始化Log日志打印
        initLog();
        // 初始化APP崩溃处理器
        CrashHandler.getInstance().init(this);
        // 获取设备分辨率并写入到本地配置文件中
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        d("width", width);
        d("height", height);
        SharedPreferencesUtil.save(Constant.SHARED_KEY.SCREEN_WIDTH, width);
        SharedPreferencesUtil.save(Constant.SHARED_KEY.SCREEN_HEIGHT, height);
    }

    /**
     * 获取设备屏幕宽度
     *
     * @return 设备屏幕宽度
     */
    public static final int getScreenWidth() {
        return width;
    }

    /**
     * 获取设备屏幕高度
     *
     * @return 设备屏幕高度
     */
    public static final int getScreenHeight() {
        return height;
    }
}