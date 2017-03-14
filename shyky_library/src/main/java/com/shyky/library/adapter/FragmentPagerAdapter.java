package com.shyky.library.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.shyky.library.BaseApplication;
import com.shyky.library.util.LayoutUtil;
import com.shyky.library.util.ResourceUtil;
import com.shyky.util.ObjectUtil;
import com.shyky.util.TextUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用的Fragment ViewPager适配器
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/6/3
 * @since 1.0
 */
public final class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    /**
     * 反射的方法名
     */
    private static final String GET_ITEM_TITLE_TEXT = "getItemTitleText";
    /**
     * 应用程序上下文
     */
    private Context context;
    /**
     * ViewPager中要显示的Fragment集合
     */
    private List<Fragment> fragments;
    /**
     * Tab显示标题集合
     */
    private List<String> titles;
    /**
     * Tab显示图片或自定义布局资源ID集合
     */
    private List<Integer> tabDrawableOrLayoutResIds;

    /**
     * 构造方法
     *
     * @param fragmentManager Fragment管理器
     */
    public FragmentPagerAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new ArrayList<>();
    }

    /**
     * 构造方法
     *
     * @param fragmentManager Fragment管理器
     * @param context         应用程序上下文
     */
    public FragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Context context) {
        this(fragmentManager);
        this.context = context.getApplicationContext();
    }

    /**
     * 构造方法
     *
     * @param fragmentManager Fragment管理器
     * @param fragments       要添加显示的Fragment集合
     */
    public FragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull List<Fragment> fragments) {
        this(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                return fragments.get(position);
            } else {
                return fragments.get(0);
            }
        }
        return null;
    }

    @DrawableRes
    public int getPageDrawable(int position) {
        if (tabDrawableOrLayoutResIds != null && !tabDrawableOrLayoutResIds.isEmpty())
            return tabDrawableOrLayoutResIds.get(position);
        return 0;
    }

    public View getPageView(int position) {
        if (tabDrawableOrLayoutResIds != null && !tabDrawableOrLayoutResIds.isEmpty())
            return context == null ? LayoutUtil.inflate(BaseApplication.getContext(), tabDrawableOrLayoutResIds.get(position)) : LayoutUtil.inflate(context, tabDrawableOrLayoutResIds.get(position));
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && !titles.isEmpty())
            return titles.get(position);
        return fragments != null && position < fragments.size() ? getContentText(fragments.get(position)) : "";
    }

    public void setFragments(@NonNull List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public Fragment getFragmentByPosition(int position) {
        return fragments.get(position);
    }

    public void setFragments(@NonNull List<Fragment> fragments, List<String> titles) {
        this.fragments = fragments;
        this.titles = titles;
    }

    public void addFragment(@NonNull Fragment fragment) {
        if (fragments != null && fragment != null) {
            fragments.add(fragment);
        }
    }

    public void addFragment(@NonNull Fragment fragment, @NonNull String title) {
        addFragment(fragment);
        if (titles == null)
            titles = new ArrayList<>();
        if (titles != null && !TextUtil.isEmptyAndNull(title)) {
            // 不用判断是否重复，会造成get的时候造成长度不一致问题
            //  if (!fragmentTitles.contains(title))
            titles.add(title);
        }
    }

    public void addFragment(@NonNull Fragment fragment, @StringRes int resId) {
        addFragment(fragment);
        if (ObjectUtil.isNull(titles))
            titles = new ArrayList<>();
        if (resId > 0) {
            // 不用判断是否重复，会造成get的时候造成长度不一致问题
            //  if (!fragmentTitles.contains(title))
            titles.add(ResourceUtil.getString(resId));
        }
    }
//    public void addFragment(@NonNull Fragment fragment, @IdRes @DrawableRes int drawableOrLayoutResId) {
//        addFragment(fragment, drawableOrLayoutResId, null);
//    }

    public void addFragment(@NonNull Fragment fragment, @IdRes @DrawableRes int drawableOrLayoutResId, @NonNull String title) {
        addFragment(fragment);
        if (tabDrawableOrLayoutResIds == null)
            tabDrawableOrLayoutResIds = new ArrayList<>();
        if (drawableOrLayoutResId > 0) {
            tabDrawableOrLayoutResIds.add(drawableOrLayoutResId);
        }
        if (title != null) {
            if (titles == null)
                titles = new ArrayList<>();
            if (titles != null && !TextUtil.isEmptyAndNull(title)) {
                titles.add(title);
            }
        }
    }

    /**
     * 清空所有的标题和Fragment
     */
    public void clear() {
        if (titles != null)
            titles.clear();
        if (fragments != null)
            fragments.clear();
        if (tabDrawableOrLayoutResIds != null)
            tabDrawableOrLayoutResIds.clear();
    }

    /**
     * 根据传进来的对象反射出getItemViewText()方法，来获取需要显示的值
     *
     * @param item
     * @return
     */
    private String getContentText(Object item) {
        String contentText = item.toString();
        try {
            if (item instanceof Fragment) {
                contentText = item.getClass().getSimpleName();
            }
            Class<?> clz = item.getClass();
            Method m = clz.getMethod(GET_ITEM_TITLE_TEXT);
            contentText = m.invoke(item, new Object[0]).toString();
        } catch (Exception e) {
        }
        return contentText;
    }
}