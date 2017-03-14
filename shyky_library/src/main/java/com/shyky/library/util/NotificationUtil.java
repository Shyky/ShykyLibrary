package com.shyky.library.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.shyky.library.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification相关工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/5/2
 * @since 1.0
 */
public final class NotificationUtil {
    private static NotificationManager notificationManager;
    //    private static Map<Integer, Notification> notifications;
    private static List<Notification> notifications;

    /**
     * 静态内部类实现单例模式
     */
    public static class Holder {
        public static final NotificationUtil INSTANCE = new NotificationUtil();
    }

    /**
     * 构造方法私有化
     */
    private NotificationUtil() {
        notificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 获取实例对象
     *
     * @return NotificationUtil实例对象
     */
    private static NotificationUtil getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 检查第一次使用有没有调用getInstance方法
     */
    public static void check() {
        if (notificationManager == null)
            throw new NullPointerException("这是第一次使用NotificationUtil，NotificationManager还没有初始化，请先调用getInstance方法");
    }

    public synchronized static void add(Notification notification) {
        getInstance();
        if (notifications == null)
            notifications = new ArrayList<>();

        if (!notifications.contains(notification))
            notifications.add(notification);
    }

    public static void show() {
        getInstance();
    }
}