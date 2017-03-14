package com.shyky.library.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.shyky.library.event.MessageEvent;
import com.shyky.library.exception.StringIsEmptyException;
import com.shyky.library.util.ToastUtil;
import com.shyky.library.view.fragment.impl.IBaseFragment;
import com.shyky.util.ObjectUtil;
import com.shyky.util.TextUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;

import static com.socks.library.KLog.d;

/**
 * 基础的Fragment，该类简化了原始类的生命周期，并增加了一些常用方法，强烈建议Fragment继承该类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/3/19
 * @since 1.0
 */
public abstract class BaseFragment extends Fragment implements IBaseFragment {
    /**
     * Fragment content view
     */
    private View inflateView;
    /**
     * 所属Activity
     */
    private Activity activity;
    /**
     * 记录是否已经创建了,防止重复创建
     */
    private boolean viewCreated;

    /**
     * 是否要在销毁时取消注册EventBus
     *
     * @return 返回true表示要取消，否则不取消，默认为不取消
     */
    public boolean unregisterEventBusOnDestroy() {
        return false;
    }

    /**
     * 显示Toast消息
     *
     * @param message 消息文本字符串
     */
    public final void showToast(@NonNull String message) {
        if (TextUtil.isEmpty(message))
            throw new StringIsEmptyException();
        ToastUtil.showShortMessage(message);
    }

    /**
     * 显示Toast消息
     *
     * @param resId 消息文本字符串资源ID
     */
    public final void showToast(@StringRes int resId) {
        ToastUtil.showShortMessage(resId);
    }

    /**
     * 通过ID查找控件
     *
     * @param viewId 控件资源ID
     * @param <VIEW> 泛型参数，查找得到的View
     * @return 找到了返回控件对象，否则返回null
     */
    final public <VIEW extends View> VIEW findViewById(@IdRes int viewId) {
        return (VIEW) inflateView.findViewById(viewId);
    }

    /**
     * 发送消息，用于各个组件之间通信，所有的消息事件只能通过MessageEvent发送
     *
     * @param event 消息事件对象
     */
    protected final void sendMessage(@NonNull MessageEvent event) {
        // 发布事件
        EventBus.getDefault().post(event);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(@NonNull Class<?> targetActivity) {
        startActivity(new Intent(getContext(), targetActivity));
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的String类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull String extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtil.isEmptyAndNull(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的int类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull int extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtil.isEmptyAndNull(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    private Bundle getArgument() {
        Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            data = new Bundle();
        return data;
    }

    public final String getStringArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return null;
        return data.getString(key);
    }

    public final String getStringArgument(@NonNull String key, String defaultValue) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return null;
        final String value = data.getString(key);
        return ObjectUtil.isNull(value) ? defaultValue : value;
    }

    public final char getCharArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return '\u0000';
        return data.getChar(key);
    }

    public final boolean getBooleanArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return false;
        return data.getBoolean(key);
    }

    public final boolean getBooleanArgument(@NonNull String key, boolean defaultValue) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return false;
        return data.getBoolean(key, defaultValue);
    }

    public final int getIntArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return 0;
        return data.getInt(key);
    }

    public final int getIntArgument(@NonNull String key, int defaultValue) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return 0;
        return data.getInt(key, defaultValue);
    }

    public final float getFloatArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return 0.0f;
        return data.getFloat(key);
    }

    public final float getFloatArgument(@NonNull String key, float defaultValue) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return 0.0f;
        return data.getFloat(key, defaultValue);
    }

    public final double getDoubleArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return 0.0;
        return data.getDouble(key);
    }

    public final double getDoubleArgument(@NonNull String key, double defaultValue) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return 0.0;
        return data.getDouble(key, defaultValue);
    }

    public final String[] getStringArrayArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return null;
        return data.getStringArray(key);
    }

    public final ArrayList<String> getStringArrayListArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return null;
        return data.getStringArrayList(key);
    }

    public final Parcelable getParcelableArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return null;
        return data.getParcelable(key);
    }

    public final Serializable getSerializableArgument(@NonNull String key) {
        final Bundle data = getArguments();
        if (ObjectUtil.isNull(data))
            return null;
        return data.getSerializable(key);
    }

    public final void setArgument(@NonNull String key, @NonNull String value) {
        final Bundle data = getArgument();
        data.putString(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull byte value) {
        final Bundle data = getArgument();
        data.putByte(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull char value) {
        final Bundle data = getArgument();
        data.putChar(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull boolean value) {
        final Bundle data = getArgument();
        data.putBoolean(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull int value) {
        final Bundle data = getArgument();
        data.putInt(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull short value) {
        final Bundle data = getArgument();
        data.putShort(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull long value) {
        final Bundle data = getArgument();
        data.putLong(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull float value) {
        final Bundle data = getArgument();
        data.putFloat(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull double value) {
        final Bundle data = getArgument();
        data.putDouble(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull Parcelable value) {
        final Bundle data = getArgument();
        data.putParcelable(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull Serializable value) {
        final Bundle data = getArgument();
        data.putSerializable(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull Bundle value) {
        final Bundle data = getArgument();
        data.putBundle(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull ArrayList<String> value) {
        final Bundle data = getArgument();
        data.putStringArrayList(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    public final void setArgument(@NonNull String key, @NonNull String[] value) {
        final Bundle data = getArgument();
        data.putStringArray(key, value);
        // 防止报错：java.lang.IllegalStateException: Fragment already active
        if (!isAdded())
            setArguments(data);
    }

    /**
     * 接收到EventBus发布的消息事件
     *
     * @param event 消息事件
     */
    @CallSuper
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(@NonNull MessageEvent event) {
        d("onReceiveMessage..." + (event == null ? "null" : event));
    }

    @CallSuper
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        d("onActivityCreated...");
    }

    @CallSuper
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        d("onActivityResult...requestCode" + requestCode + ",resultCode=" + resultCode);
        d("onActivityResult...data", data);
    }

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        d("onAttach...");
    }

    @CallSuper
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        d("onAttach...");
        this.activity = activity;
    }

    @Override
    final public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d("onCreate...");
        // 防止重复调用onCreate方法，造成在initData方法中adapter重复初始化问题
        if (!viewCreated) {
            viewCreated = true;
            initData();
            // 注册事件总线，不要在onStart和onResume中注册，会有问题：xx already registered to event class xxx
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        }
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        d("onCreateView...");
        if (null == inflateView) {
            // 强制竖屏显示
            if (activity != null)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int layoutResId = getCreateViewLayoutId();
            if (layoutResId > 0)
                inflateView = inflater.inflate(getCreateViewLayoutId(), container, false);

            // 解决点击穿透问题
            inflateView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        return inflateView;
    }

    @Override
    final public void onViewCreated(View view, Bundle savedInstanceState) {
        d("onViewCreated.....");
        if (viewCreated) {
            findView(view, savedInstanceState);
            initView(view, savedInstanceState);
            initDialog();
            initListener();
            viewCreated = false;
        }
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        d("onStart...");
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        d("onDestroyView...");
        // 解决ViewPager中的问题
        if (null != inflateView) {
            final ViewParent parent = inflateView.getParent();
            if (parent != null)
                ((ViewGroup) parent).removeView(inflateView);
        }
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();
        d("onPause...");
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
        d("onStop...");
    }

    @CallSuper
    @Override
    public void onDetach() {
        super.onDetach();
        d("onDetach...");
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        d("onResume...");
    }

    @CallSuper
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        d("onLowMemory...");
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        d("onDestroy...");
        // 取消注册事件总线，不要在onStop中取消注册，当Activity被别的页面遮住了就会有问题
        if (unregisterEventBusOnDestroy())
            EventBus.getDefault().unregister(this);
    }

    @CallSuper
    @Override
    public void initData() {
        d("initData...");
    }

    @CallSuper
    @Override
    public void findView(View inflateView, Bundle savedInstanceState) {
        d("findView...");
    }

    @CallSuper
    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        d("initView...");
    }

    @CallSuper
    @Override
    public void initListener() {
        d("initListener...");
    }

    @CallSuper
    @Override
    public void initDialog() {
        d("initDialog...");
    }
}