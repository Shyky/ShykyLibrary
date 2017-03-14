package com.shyky.library.view.activity.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shyky.library.constant.Constant;
import com.shyky.library.event.MessageEvent;
import com.shyky.library.util.LayoutUtil;
import com.shyky.library.util.ToastUtil;
import com.shyky.library.view.activity.impl.IBaseActivity;
import com.shyky.util.ListUtil;
import com.shyky.util.ObjectUtil;
import com.shyky.util.TextUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 基础的Activity，该类细化了原始类的生命周期，简化和封装了Fragment和Toolbar的管理
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.7
 * @email sj1510706@163.com
 * @date 2016/4/8
 * @since 1.0
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity, OnMenuItemClickListener {
    /**
     * Fragment管理器
     */
    private FragmentManager fragmentManager;
    /**
     * 保存的Activity状态
     */
    private Bundle savedInstanceState;
    /**
     * 记录是否已经调用过onStart方法
     */
    private boolean isStarted;
    /**
     * 保存通过addFragment方法添加的Fragment
     */
    private List<Fragment> addedFragments;
    /**
     * Material Design风格AppBarLayout控件
     */
    private AppBarLayout appBarLayout;
    /**
     * Material Design风格Toolbar控件
     */
    private Toolbar toolbar;
    /**
     * Toolbar添加的自定义布局
     */
    private View toolbarMenuView;

    /**
     * Toolbar Menu item点击事件回调
     *
     * @param menuId menu资源ID
     * @param item   MenuItem对象
     * @return
     */
    public boolean onToolbarMenuItemClick(@IdRes int menuId, @NonNull MenuItem item) {
        return false;
    }

    /**
     * 是否要Activity销毁时取消注册EventBus
     *
     * @return 返回true表示要取消，否则不取消，默认为不取消
     */
    public boolean unregisterEventBusOnDestroy() {
        return false;
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

    /**
     * 显示Toast提示信息
     *
     * @param message 消息文本
     */
    public final void showToast(@NonNull String message) {
        ToastUtil.showShortMessage(message);
    }

    /**
     * 显示Toast提示信息
     *
     * @param resId string.xml中的字符串资源ID
     */
    public final void showToast(@StringRes int resId) {
        ToastUtil.showShortMessage(resId);
    }

    /**
     * 发送消息，用于各个组件之间通信，所有的消息事件只能通过MessageEvent发送
     *
     * @param event 消息事件对象
     */
    protected final void sendMessage(@NonNull MessageEvent event) {
        // 发布EventBus消息事件
        EventBus.getDefault().post(event);
    }

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d("onCreate...");
        if (getContentViewLayoutResId() > 0)
            setContentView(getContentViewLayoutResId());
        this.savedInstanceState = savedInstanceState;
        isStarted = true;
        fragmentManager = getSupportFragmentManager();
        // 解决当APP发生错误Activity被重启时Fragment重叠问题
        if (hasFragment()) {
            d("原来有添加过Fragment...");
            // 清空已经添加的Fragment
            addedFragments.clear();
            // 将已经添加的Fragment从Activity中分离
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment : addedFragments) {
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }
        initData(savedInstanceState);
        // 注册事件总线，不要在onStart和onResume中注册，会有问题：xx already registered to event class xxx
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    /**
     * 添加Fragment到Activity中
     */
    private void addFragments() {
        // 添加Fragment到Activity中
        final Fragment fragment = getFragment();
        final Fragment[] fragments = getFragments();
        final List<Fragment> fragmentList = getFragmentList();
        if (ObjectUtil.notNull(fragment) || (ObjectUtil.notNull(fragments) && fragments.length > 0) || (ObjectUtil.notNull(fragmentList) && !fragmentList.isEmpty())) {
            if (ObjectUtil.isNull(addedFragments))
                addedFragments = new ArrayList<>();
        }
        if (fragment != null) {
            addedFragments.add(fragment);
            d("添加了一个Fragment：" + fragment);
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            if (getFragmentContainerViewId() > 0) {
                fragmentTransaction.add(getFragmentContainerViewId(), fragment);
            } else if (getFragmentTag() != null)
                fragmentTransaction.add(fragment, getFragmentTag());
            else if (getFragmentContainerViewId() > 0 && getFragmentTag() != null)
                fragmentTransaction.add(getFragmentContainerViewId(), fragment, getFragmentTag());
            // 默认显示添加的Fragment
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
        } else if (fragments != null && fragments.length > 0) {
            d("添加了" + fragments.length + "个Fragment");
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            if (getFragmentContainerViewId() > 0) {
                for (Fragment item : fragments) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item);
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentTag() != null) {
                for (Fragment item : fragments) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentContainerViewId() > 0 && getFragmentTag() != null) {
                for (Fragment item : fragments) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            }
            // 默认显示第一个添加的Fragment
            fragmentTransaction.show(fragments[0]);
            fragmentTransaction.commit();
        } else if (fragmentList != null && !fragmentList.isEmpty()) {
            d("添加了" + fragmentList.size() + "个Fragment");
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            if (getFragmentContainerViewId() > 0) {
                for (Fragment item : fragmentList) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item);
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentTag() != null) {
                for (Fragment item : fragmentList) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            } else if (getFragmentContainerViewId() > 0 && getFragmentTag() != null) {
                for (Fragment item : fragmentList) {
                    if (item == null)
                        throw new NullPointerException("当前Fragment为null");
                    addedFragments.add(item);
                    fragmentTransaction.add(getFragmentContainerViewId(), item, getFragmentTag());
                    // 添加后默认隐藏Fragment
                    fragmentTransaction.hide(item);
                }
            }
            // 默认显示第一个添加的Fragment
            fragmentTransaction.show(fragmentList.get(0));
            fragmentTransaction.commit();
        }
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        d("onStart...");

        if (isStarted) {
            findView(savedInstanceState);
            initView(savedInstanceState);

            // 添加Fragment到Activity中
            addFragments();
            initDialog(savedInstanceState);
            initListener(savedInstanceState);
            isStarted = false;
        }
    }

    /**
     * 替换Fragment
     *
     * @param fragment 要替换的Fragment
     */
    protected void replaceFragment(@NonNull Fragment fragment) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // add方法默认会显示添加进去的Fragment
        fragmentTransaction.replace(getFragmentContainerViewId(), fragment);
        fragmentTransaction.commit();
    }

    /**
     * 替换Fragment，允许丢失状态
     *
     * @param fragment 要替换的Fragment
     */
    protected void replaceFragmentAllowingStateLoss(@NonNull Fragment fragment) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // add方法默认会显示添加进去的Fragment
        fragmentTransaction.replace(getFragmentContainerViewId(), fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 添加Fragment到Activity中
     *
     * @param fragment 要添加的Fragment
     */
    protected final void addFragment(@NonNull Fragment fragment) {
        if (getFragmentContainerViewId() <= 0)
            throw new IllegalArgumentException("没有重写getFragmentContainerViewId方法或返回的值不是一个view资源id");

        // 先隐藏所有的Fragment
        hideFragments();
        // 显示Fragment
        if (fragment != null && !fragment.isDetached()) {
            // addedFragments默认是没有初始化的
            if (addedFragments == null)
                addedFragments = new ArrayList<>();
            // 保存添加的Fragment
            if (!addedFragments.contains(fragment))
                addedFragments.add(fragment);
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            // add方法默认会显示添加进去的Fragment
            fragmentTransaction.add(getFragmentContainerViewId(), fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 判断Activity中是否有Fragment
     *
     * @return 有返回true，否则返回false
     */
    protected final boolean hasFragment() {
        return addedFragments != null && !addedFragments.isEmpty();
    }

    /**
     * 显示第一个添加的Fragment
     */
    protected final void showFragment() {
        // 显示Fragment
        if (hasFragment()) {
            final Fragment fragment = addedFragments.get(0);
            if (!fragment.isDetached()) {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.beginTransaction();
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
            }
        }
    }

    /**
     * 隐藏所有添加进去的Fragment
     */
    private void hideFragments() {
        hideFragments(null);
    }

    /**
     * 隐藏除了指定的Fragment之外的所有添加进去的Fragment
     *
     * @param index 添加的Fragment在集合中的索引位置
     */
    private void hideFragments(int index) {
        if (hasFragment())
            hideFragments(addedFragments.get(index));
    }

    /**
     * 隐藏除了指定的Fragment之外的所有添加进去的Fragment
     *
     * @param fragment 指定的Fragment
     */
    private void hideFragments(@NonNull Fragment fragment) {
        // 先判断有没有添加过Fragment
        if (hasFragment()) {
            // 有则循环遍历隐藏所有的Fragment
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            int len = addedFragments.size();
            for (int j = 0; j < len; j++) {
                final Fragment item = addedFragments.get(j);
                if (ObjectUtil.notNull(fragment) && item == fragment)
                    continue;
                fragmentTransaction.hide(item);
            }
            fragmentTransaction.commit();
        }
    }

    /**
     * 显示指定的Fragment
     *
     * @param fragment 指定的Fragment
     */
    protected final void showFragment(@NonNull Fragment fragment) {
        if (getFragmentContainerViewId() <= 0)
            throw new IllegalArgumentException("没有重写getFragmentContainerViewId方法或返回的值不是一个view资源id");
        // 先隐藏已经添加了的Fragment
        hideFragments(fragment);
        // 再显示指定的Fragment
        if (fragment != null && !fragment.isDetached()) {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 显示指定的Fragment
     *
     * @param index 指定的Fragment在数组或集合中的索引
     */
    protected final void showFragment(int index) {
        d("index", index);
        if (hasFragment()) {
            if (index < 0 || index >= addedFragments.size())
                throw new ArrayIndexOutOfBoundsException("index:" + index + "超出了添加的Fragment数组的长度，当前Activity包含的Fragment的个数为" + addedFragments.size());
            // 先隐藏所有的Fragment再显示指定的Fragment
            hideFragments(index);
        }
    }

    /**
     * 隐藏第一个添加进去的Fragment
     */
    protected void hideFragment() {
        if (hasFragment()) {
            final Fragment fragment = addedFragments.get(0);
            if (!fragment.isDetached()) {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragment);
                fragmentTransaction.commit();
            }
        }
    }

    /**
     * 隐藏指定的Fragment
     *
     * @param fragment 指定的Fragment
     */
    protected final void hideFragment(@NonNull Fragment fragment) {
        if (fragment != null && !fragment.isDetached()) {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 获取对应的Fragment实例,当遇到特殊情况重启后，MainActivity可以通过该方法初始化Fragment
     *
     * @param clazz 对应的Fragment字节码
     * @param <T>   对应Fragment实例
     * @return 如果存在则返回对应的实例，如果不存在，则返回null
     */
    protected <T extends Fragment> T getFragment(Class<T> clazz) {
        T result = null;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (ListUtil.notEmpty(fragments)) {
            for (Fragment fragment : fragments) {
                if (fragment.getClass().equals(clazz)) {
                    result = (T) fragment;
                    break;
                }
            }
        }
        return result;
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        d("onActivityResult...requestCode" + requestCode + " , resultCode = " + resultCode);
        d("onActivityResult...data", data);
        // 解决Fragment嵌套onActivityResult不回调问题，这是FragmentActivity的bug
        if (hasFragment()) {
            for (Fragment fragment : addedFragments) {
                if (ObjectUtil.notNull(fragment) && !fragment.isDetached())
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @CallSuper
    @Override
    public void onResume() {
        d("onResume...");
        // 竖屏显示
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        d("onPause...");
    }

    @CallSuper
    @Override
    protected void onRestart() {
        super.onRestart();
        d("onRestart...");
    }

    @CallSuper
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        d("onLowMemory...内存低");
    }

    @CallSuper
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_MODERATE:
                d("onTrimMemory>>>TRIM_MEMORY_MODERATE:" + TRIM_MEMORY_MODERATE);
                break;
            case TRIM_MEMORY_COMPLETE:
                d("onTrimMemory>>>TRIM_MEMORY_COMPLETE:" + TRIM_MEMORY_COMPLETE);
                break;
            case TRIM_MEMORY_BACKGROUND:
                d("onTrimMemory>>>TRIM_MEMORY_BACKGROUND:" + TRIM_MEMORY_BACKGROUND);
                break;
            case TRIM_MEMORY_UI_HIDDEN:
                d("onTrimMemory>>>TRIM_MEMORY_UI_HIDDEN:" + TRIM_MEMORY_UI_HIDDEN);
                break;
            case TRIM_MEMORY_RUNNING_LOW:
                d("onTrimMemory>>>TRIM_MEMORY_RUNNING_LOW:" + TRIM_MEMORY_RUNNING_LOW);
                break;
            case TRIM_MEMORY_RUNNING_MODERATE:
                d("onTrimMemory>>>TRIM_MEMORY_RUNNING_MODERATE:" + TRIM_MEMORY_RUNNING_MODERATE);
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL:
                d("onTrimMemory>>>TRIM_MEMORY_RUNNING_CRITICAL:" + TRIM_MEMORY_RUNNING_CRITICAL);
                break;
        }
    }

    @CallSuper
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        d("onAttachFragment... fragment = " + (fragment == null ? "null" : fragment));
    }

    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
        d("onStop...");
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("onDestroy...");
        // 取消注册事件总线，不要在onStop中取消注册，当Activity被别的页面遮住了就会有问题
        if (unregisterEventBusOnDestroy())
            EventBus.getDefault().unregister(this);
        // 解决当APP发生错误Activity被重启时Fragment重叠问题
        if (hasFragment()) {
            d("原来有添加过Fragment...");
            // 清空已经添加的Fragment
            addedFragments.clear();
            // 将已经添加的Fragment从Activity中分离
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment : addedFragments) {
                fragmentTransaction.detach(fragment);
                fragmentTransaction.commit();
            }
        }
        fragmentManager = null;
        addedFragments = null;
    }

    @CallSuper
    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        d("initData...");
    }

    @CallSuper
    @Override
    public void findView(@NonNull Bundle savedInstanceState) {
        d("findView...");
        final int resId = getToolbarResId();
        if (resId > 0) {
            final View view = findViewById(resId);
            if (view instanceof AppBarLayout) {
                appBarLayout = (AppBarLayout) view;
                final int childCount = appBarLayout.getChildCount();
                if (childCount == 1) {
                    toolbar = (Toolbar) appBarLayout.getChildAt(0);
                }
            } else if (view instanceof Toolbar)
                toolbar = (Toolbar) view;
        }
    }

    @CallSuper
    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        d("initView...");
        if (ObjectUtil.notNull(toolbar)) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onToolbarNavigationClick();
                }
            });
            toolbar.setOnMenuItemClickListener(this);
        }
        // 设置APP logo图标
//        toolbar.setLogo(R.mipmap.logo);
        // Navigation Icon 要設定在 setSupportActionBar 才有作用 否則會出現 back button
        // 这边要留意的是setNavigationIcon需要放在 setSupportActionBar之后才会生效。
        // 替换系统默认的侧边菜单图标，默认是白色图标
//        setToolbarNavigationIcon(R.mipmap.menu);
    }

    @CallSuper
    @Override
    public void initListener(@NonNull Bundle savedInstanceState) {
        d("initListener...");
    }

    @CallSuper
    @Override
    public void initDialog(@NonNull Bundle savedInstanceState) {
        d("initDialog...");
    }

    @Override
    public int getFragmentContainerViewId() {
        return -1;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public Fragment[] getFragments() {
        return null;
    }

    @Override
    public List<Fragment> getFragmentList() {
        return null;
    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getToolbarResId() {
        return 0;
    }

    @Override
    public int getToolbarMenuLayoutResId() {
        return 0;
    }

    @Override
    public int getToolbarMenuResId() {
        return 0;
    }

    @Override
    public int getToolbarMenuId() {
        return 0;
    }

    @Override
    public void onToolbarNavigationClick() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onToolbarMenuItemClick(item.getItemId(), item);
    }

    public void initToolbarMenuActionView(@NonNull View actionView) {

    }

    @CallSuper
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        d("onCreateOptionsMenu...");
        final int menuId = getToolbarMenuId();
        if (menuId > 0) {
            getMenuInflater().inflate(menuId, menu);
            // 自定义ToolBar menu布局
            final int layoutResId = getToolbarMenuLayoutResId();
            if (layoutResId > 0) {
                toolbarMenuView = LayoutUtil.inflate(this, layoutResId);
                // 需要API 11无法兼容低版本
                // menu.findItem(0).setActionView(toolbarMenuView);
                // 解决低版本兼容问题，使用v4包的MenuItemCompat类
                final int menuResId = getToolbarMenuResId();
                MenuItem menuItem = menu.findItem(menuResId);
                MenuItemCompat.setActionView(menuItem, toolbarMenuView);
                initToolbarMenuActionView(toolbarMenuView);
            }
        }
        return true;
    }

    public final Toolbar getToolbar() {
        return toolbar;
    }

    public final void setToolbarMenu(@MenuRes int resId) {
        toolbar.inflateMenu(resId);
    }

    public final void setToolbarNavigationIcon(@DrawableRes int resId) {
        toolbar.setNavigationIcon(resId);
    }

    public final void setToolbarNavigationIcon(@Nullable Drawable icon) {
        toolbar.setNavigationIcon(icon);
    }

    public final void setToolbarLogo(@DrawableRes int resId) {
        toolbar.setLogo(resId);
    }

    public final void setToolbarTitle(@StringRes int resId) {
        toolbar.setTitle(resId);
    }

    public final void setToolbarTitle(@NonNull CharSequence title) {
        toolbar.setTitle(title);
    }

    public final void setToolbarTitleTextColor(@ColorInt int colorResId) {
        toolbar.setTitleTextColor(colorResId);
    }

    public final void setToolbarTitleTextAppearance(@StyleRes int styleResId) {
        toolbar.setTitleTextAppearance(getApplicationContext(), styleResId);
    }

    public final void setToolbarSubtitle(@StringRes int resId) {
        toolbar.setSubtitle(resId);
    }

    public final void setToolbarSubtitle(@NonNull CharSequence subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    public final void hideToolbarLogo() {
        toolbar.setLogo(null);
    }

    public final void hideToolbarTitle() {
        toolbar.setTitle(null);
    }

    public final void hideToolbarMenu(@IdRes int resId) {
        final MenuItem menuItem = findToolbarMenuItem(resId);
        menuItem.setVisible(false);
        // 要调用该方法才能生效
        invalidateOptionsMenu();
    }

    public final void showToolbarMenu(@IdRes int resId) {
        final MenuItem menuItem = findToolbarMenuItem(resId);
        menuItem.setVisible(true);
        // 要调用该方法才能生效
        invalidateOptionsMenu();
    }

    public final Menu getToolbarMenu() {
        return toolbar.getMenu();
    }

    public final MenuItem findToolbarMenuItem(@IdRes int resId) {
        return getToolbarMenu().findItem(resId);
    }

    public final void setToolbarMenuItemVisible(@IdRes int resId, boolean visible) {
        final MenuItem menuItem = findToolbarMenuItem(resId);
        d("Toolbar MenuItem", menuItem);
        if (menuItem != null)
            menuItem.setVisible(visible);
    }

    public final <VIEW extends View> VIEW findToolbarMenuViewById(@IdRes int resId) {
        if (toolbarMenuView == null)
            return null;
        return (VIEW) toolbarMenuView.findViewById(resId);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        try {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                onToolbarNavigationClick();
//                return false;
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(@NonNull Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
    }

    /**
     * 跳转到指定的Activity
     *
     * @param flags          intent flags
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(int flags, @NonNull Class<?> targetActivity) {
        final Intent intent = new Intent(this, targetActivity);
        intent.setFlags(flags);
        startActivity(new Intent(this, targetActivity));
    }

    /**
     * 跳转到指定的Activity
     *
     * @param data           Activity之间传递数据，Intent的Extra key为Constant.EXTRA_NAME.DATA
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(@NonNull Bundle data, @NonNull Class<?> targetActivity) {
        final Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_NAME.DATA, data);
        intent.setClass(this, targetActivity);
        startActivity(intent);
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
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的boolean类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, boolean extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtil.isEmptyAndNull(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
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
    public final void startActivity(@NonNull String extraName, int extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtil.isEmptyAndNull(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的float类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, float extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtil.isEmptyAndNull(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的double类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, double extraValue, @NonNull Class<?> targetActivity) {
        if (TextUtil.isEmptyAndNull(extraName))
            throw new NullPointerException("传递的值的键名称为null或空");
        final Intent intent = new Intent(getApplicationContext(), targetActivity);
        intent.putExtra(extraName, extraValue);
        startActivity(intent);
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回null，否则返回传递过来的参数值
     */
    public final String getStringExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回null
            final String extra = intent.getStringExtra(name);
            if (ObjectUtil.isNull(extra)) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getString(name);
                }
            } else
                return extra;
        }
        return null;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final String getStringExtra(@NonNull String name, String defaultValue) {
        final String extra = getStringExtra(name);
        if (ObjectUtil.isNull(extra))
            return defaultValue;
        return extra;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回null，否则返回传递过来的参数值
     */
    public final boolean getBooleanExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回false
            final boolean extra = intent.getBooleanExtra(name, false);
            if (!extra) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getBoolean(name);
                }
            }
            return extra;
        }
        return false;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final boolean getBooleanExtra(@NonNull String name, boolean defaultValue) {
        final boolean extra = getBooleanExtra(name);
        if (extra)
            return defaultValue;
        return extra;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回null，否则返回传递过来的参数值
     */
    public final int getIntExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回-1
            final int extra = intent.getIntExtra(name, -1);
            if (extra == -1) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getInt(name);
                }
            }
            return extra;
        }
        return -1;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final int getIntExtra(@NonNull String name, int defaultValue) {
        final int extra = getIntExtra(name);
        if (extra == -1)
            return defaultValue;
        return extra;
    }

    public final short getShortExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0;
        return intent.getShortExtra(name, Short.valueOf("0"));
    }

    public final int getShortExtra(@NonNull String name, short defaultValue) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0;
        return intent.getShortExtra(name, defaultValue);
    }

    public final long getLongExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0L;
        return intent.getLongExtra(name, 0L);
    }

    public final long getLongExtra(@NonNull String name, long defaultValue) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return 0L;
        return intent.getLongExtra(name, defaultValue);
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回0.0f，否则返回传递过来的参数值
     */
    public final float getFloatExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回0.0f
            final float extra = intent.getFloatExtra(name, 0.0f);
            if (extra == 0.0f) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getFloat(name);
                }
            }
            return extra;
        }
        return 0.0f;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回设置的默认值，否则返回传递过来的参数值
     */
    public final float getFloatExtra(@NonNull String name, float defaultValue) {
        final float extra = getFloatExtra(name);
        if (extra == 0.0f)
            return defaultValue;
        return extra;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name 参数名称
     * @return 从intent和bundle中都没有取到返回0.0，否则返回传递过来的参数值
     */
    public final double getDoubleExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.notNull(intent)) {
            // 如果intent不为空，则先从intent中取，如果没有再从bundle中取，如果都没有就返回0.0
            final double extra = intent.getDoubleExtra(name, 0.0);
            if (extra == 0.0) {
                final Bundle data = intent.getExtras();
                if (ObjectUtil.notNull(data)) {
                    return data.getDouble(name);
                }
            }
            return extra;
        }
        return 0.0;
    }

    /**
     * 获取通过Intent对象传递过来的参数
     *
     * @param name         Extra参数名称
     * @param defaultValue 没有在intent或bundle对象中找到时设置的默认值
     * @return 从intent和bundle中都没有取到返回0.0，否则返回传递过来的参数值
     */
    public final double getDoubleExtra(@NonNull String name, double defaultValue) {
        final double extra = getDoubleExtra(name);
        if (extra == 0.0)
            return defaultValue;
        return extra;
    }

    public final Parcelable getParcelableExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return null;
        return intent.getParcelableExtra(name);
    }

    public final Parcelable[] getParcelableArrayExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return null;
        return intent.getParcelableArrayExtra(name);
    }

    public final ArrayList<Parcelable> getParcelableArrayListExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return null;
        return intent.getParcelableArrayListExtra(name);
    }

    public final Serializable getSerializableExtra(@NonNull String name) {
        final Intent intent = getIntent();
        if (ObjectUtil.isNull(intent))
            return null;
        return intent.getSerializableExtra(name);
    }
}