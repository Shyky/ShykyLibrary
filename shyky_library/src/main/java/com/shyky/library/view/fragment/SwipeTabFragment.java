package com.shyky.library.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shyky.library.R;
import com.shyky.library.adapter.FragmentPagerAdapter;
import com.shyky.library.view.fragment.base.BaseFragment;
import com.shyky.util.ListUtil;
import com.shyky.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 滑动切换选项卡Fragment
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/10/10
 * @since 1.0
 */
public class SwipeTabFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private List<String> titles;
    private List<Fragment> fragments;

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.content_swipe_tab;
    }

    @CallSuper
    @Override
    public void initData() {
        super.initData();
        if (ObjectUtil.isNull(titles)) {
            titles = new ArrayList<>();
        } else if (ObjectUtil.isNull(fragments)) {
            fragments = new ArrayList<>();
        }
    }

    @CallSuper
    @Override
    public void findView(View inflateView, Bundle savedInstanceState) {
        super.findView(inflateView, savedInstanceState);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    @CallSuper
    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        super.initView(inflateView, savedInstanceState);
        adapter = new FragmentPagerAdapter(getChildFragmentManager());
        adapter.setFragments(fragments, titles);
        // 让它每次只加载一个Fragment，ViewPager.setOffscreenPageLimit(int limit)，其中参数可以设为0或者1，参数小于1时，会默认用1来作为参数，未设置之前，ViewPager会默认加载两个Fragment。
        // 关闭预加载，默认一次只加载一个Fragment
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setTabTitles(@NonNull String[] titles) {
        if (ObjectUtil.notNull(titles)) {
            setTabTitles(Arrays.asList(titles));
        }
    }

    public void setTabTitles(@NonNull List<String> titles) {
        this.titles = titles;
    }

    public void addTabTitle(@NonNull String title) {
        if (ObjectUtil.notNull(titles) && ObjectUtil.notNull(title)) {
            titles.add(title);
        }
    }

    public void addTabTitle(@StringRes int titleResId) {
        if (titleResId > 0 && isAdded()) {
            addTabTitle(getString(titleResId));
        }
    }

    public void setTabFragments(@NonNull Fragment[] fragments) {
        if (ObjectUtil.notNull(fragments)) {
            setTabFragments(Arrays.asList(fragments));
        }
    }

    public void setTabFragments(@NonNull List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public void addTabFragment(@NonNull Fragment fragment) {
        if (ObjectUtil.notNull(fragments) && ObjectUtil.notNull(fragment)) {
            fragments.add(fragment);
        }
    }

    public void setTab(@NonNull String[] titles, @NonNull Fragment[] fragments) {
        setTabTitles(titles);
        setTabFragments(fragments);
    }

    public void setTab(@NonNull List<String> titles, @NonNull List<Fragment> fragments) {
        setTabTitles(titles);
        setTabFragments(fragments);
    }

    public void addTab(@StringRes int titleResId, @NonNull Fragment fragment) {
        addTabTitle(titleResId);
        addTabFragment(fragment);
    }

    public void addTab(@NonNull String title, @NonNull Fragment fragment) {
        addTabTitle(title);
        addTabFragment(fragment);
    }

    public Fragment getTabFragment(int index) {
        if (index < 0 || index <= fragments.size())
            return null;
        return fragments.get(index);
    }

    public Fragment getCurrentSelectedTab() {
        final int position = tabLayout.getSelectedTabPosition();
        d("tabLayout.getSelectedTabPosition()", position);
        if (ListUtil.isEmpty(fragments))
            return null;
        return fragments.get(position);
    }

    public int getTabCount() {
        return ObjectUtil.isNull(fragments) ? 0 : fragments.size();
    }
}