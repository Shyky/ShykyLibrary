package com.shyky.library.view.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.shyky.library.BaseApplication;

import static com.socks.library.KLog.d;

/**
 * 自定义ViewPager Tab指示器
 *
 * @author Shyky
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/6/16
 * @since 1.0
 */
public class SmartViewPagerIndicator extends HorizontalScrollView {
    /**
     * 构造方法
     */
    public SmartViewPagerIndicator() {
        this(BaseApplication.getContext());
    }

    public SmartViewPagerIndicator(@NonNull Context context) {
        this(context, null);
    }

    public SmartViewPagerIndicator(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartViewPagerIndicator(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    /**
     * 初始化SmartEditText
     *
     * @param attrs        属性集
     * @param defStyleAttr 默认样式属性
     */
    private void initView(@NonNull AttributeSet attrs, int defStyleAttr) {
        d("initView...");
    }

    public void setupWithViewPager(@NonNull ViewPager viewPager) {

    }

    public void setSelectedTabIndicatorColor(@ColorInt int color) {
//        mTabStrip.setSelectedIndicatorColor(color);
    }

    public void setSelectedTabIndicatorWidth(int width) {
//        mTabStrip.setSelectedIndicatorHeight(height);
    }

    public void setSelectedTabIndicatorHeight(int height) {
//        mTabStrip.setSelectedIndicatorHeight(height);
    }
}