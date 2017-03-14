package com.shyky.library.util;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shyky.library.BaseApplication;

/**
 * 优化后的不重复显示的Toast
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/4/6
 * @since 1.0
 */
public final class ToastUtil {
    private static Toast toast;
    /**
     * 记录是否已经调用过
     */
    private static boolean isInvoked;

    /**
     * 构造方法私有化
     */
    private ToastUtil() {

    }

    /**
     * 将Toast对象置空
     */
    private static void setToastNull() {
        // 解决先调用了 void showShortToastMessage()，再调用此方法是报空指针错误
        if (isInvoked)
            toast = null;
    }

    /**
     * 显示Toast提示
     *
     * @param message  提示文本
     * @param duration 停留时间
     */
    private static void showToast(String message, int duration) {
        setToastNull();
        if (toast == null)
            toast = Toast.makeText(BaseApplication.getContext(), message, duration);
        else
            toast.setText(message);
        toast.show(); // 显示Toast
        isInvoked = false;
    }

    /**
     * 显示Toast提示
     *
     * @param resId    提示文本资源ID
     * @param duration 停留时间
     */
    private static void showToast(int resId, int duration) {
        setToastNull();
        if (toast == null)
            toast = Toast.makeText(BaseApplication.getContext(), resId, duration);
        else
            toast.setText(resId);
        toast.show(); // 显示Toast提示框
        isInvoked = false;
    }

    /**
     * 短时间Toast提示
     *
     * @param message 要提示的信息
     */
    public static void showShortMessage(@NonNull String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间Toast提示
     *
     * @param resId 资源ID，在res/string.xml中配置的字符ID
     */
    public static void showShortMessage(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间自定义显示布局Toast提示
     *
     * @param layoutId 自定义显示布局文件ID
     * @param resId    字符串资源ID，在res/string.xml中配置的字符串ID
     * @param gravity  弹出显示位置
     */
    public static void showShortMessage(@LayoutRes int layoutId, @StringRes int resId, int gravity) {
        if (toast == null) {
            toast = new Toast(BaseApplication.getContext());
            toast.setGravity(gravity, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        final View view = LayoutInflater.from(BaseApplication.getContext()).inflate(layoutId, null);
        final String name = view.getClass().getName();
        if (name.equals(TextView.class.getName())) {
            final TextView textView = (TextView) view;
            textView.setText(resId);
        }
        toast.setView(view);
        toast.show(); // 显示Toast
        isInvoked = true;
    }

    /**
     * 短时间自定义显示布局Toast提示
     *
     * @param layoutId 自定义显示布局文件ID
     * @param message  显示字符串文本
     * @param gravity  弹出显示位置
     */
    public static void showShortMessage(@LayoutRes int layoutId, @NonNull String message, int gravity) {
        if (toast == null) {
            toast = new Toast(BaseApplication.getContext());
            toast.setGravity(gravity, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        final View view = LayoutInflater.from(BaseApplication.getContext()).inflate(layoutId, null);
        final String name = view.getClass().getName();
        if (name.equals(TextView.class.getName())) {
            final TextView textView = (TextView) view;
            textView.setText(message);
        }
        toast.setView(view);
        toast.show(); // 显示Toast
        isInvoked = true;
    }

    /**
     * 短时间自定义显示布局Toast提示,Toast显示在默认的位置
     *
     * @param layoutId 自定义显示布局文件ID
     * @param message  显示字符串文本
     */
    public static void showShortMessage(@LayoutRes int layoutId, @NonNull String message) {
        if (toast == null) {
            toast = new Toast(BaseApplication.getContext());
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        final View view = LayoutInflater.from(BaseApplication.getContext()).inflate(layoutId, null);
        final String name = view.getClass().getName();
        if (name.equals(TextView.class.getName())) {
            final TextView textView = (TextView) view;
            textView.setText(message);
        }
        toast.setView(view);
        toast.show(); // 显示Toast
        isInvoked = true;
    }


    /**
     * 长时间Toast提示
     *
     * @param message 要提示的信息
     */
    public static void showLongMessage(@NonNull String message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间Toast提示
     *
     * @param resId 资源ID
     */
    public static void showLongMessage(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    /**
     * 自定义Toast提示停留时间
     *
     * @param message  要提示的信息
     * @param duration 停留时间毫秒数，以毫秒为单位
     */
    public static void showMessage(@NonNull String message, int duration) {
        showToast(message, duration);
    }

    /**
     * 自定义Toast提示停留时间
     *
     * @param resId    要提示的信息，字符串资源ID
     * @param duration 停留时间毫秒数，以毫秒为单位
     */
    public static void showMessage(@StringRes int resId, int duration) {
        showToast(resId, duration);
    }

    /**
     * 销毁Toast提示框，解决Activity销毁后还提示问题
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}