package com.shyky.library.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决横向滑动和纵向滑动明显冲突、控制滚动的ViewPager
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/6/3
 * @since 1.0
 */
public class SmartViewPager extends ViewPager {
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    public SmartViewPager(@NonNull Context context) {
        this(context, null);
    }

    public SmartViewPager(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            x2 = event.getX();
            y2 = event.getY();
            if (Math.abs(y1 - y2) > 5 && Math.abs(x2 - x1) < 10) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.dispatchTouchEvent(event);
    }
}