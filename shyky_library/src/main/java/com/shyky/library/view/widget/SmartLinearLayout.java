package com.shyky.library.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author Shyky
 * @version 1.1
 * @date 2016/9/14
 * @since 1.0
 */
public class SmartLinearLayout extends LinearLayout {
    public SmartLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public SmartLinearLayout(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

    }
}