package com.shyky.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.shyky.library.R;

import static com.socks.library.KLog.d;

/**
 * 自定义Button，该ImageView除了系统基本的功能以外，还加入了防止连续点击处理等功能
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/4/24
 * @since 1.0
 */
public class SmartButton extends Button {
    private Context context;

    public SmartButton(Context context) {
        super(context, null);
    }

    public SmartButton(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.buttonStyle);
    }

    public SmartButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 初始化SmartButton
     *
     * @param attrs        属性集
     * @param defStyleAttr 默认样式属性
     */
    private void initView(AttributeSet attrs, int defStyleAttr) {
        d("initView...");
        // 获取自定义的样式属性
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SmartButton, defStyleAttr, 0);
        final int n = typedArray.getIndexCount();
        for (int j = 0; j < n; j++) {
            int attr = typedArray.getIndex(j);
            // 用switch判断会报错,改用if else
//            if (attr == R.styleable.ShykyEditText_requestFocus) {
//                boolean requestFocus = typedArray.getBoolean(attr, false);
//
//                // 获取焦点
//                if (requestFocus)
//                    requestFocus();
//                LogUtil.d(TAG, "attrRequestFocus = " + requestFocus);
//            } else if (attr == R.styleable.ShykyEditText_validate) {
//
//            }
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }
}