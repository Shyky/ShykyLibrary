package com.shyky.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shyky.library.BaseApplication;
import com.shyky.library.R;

import static com.socks.library.KLog.d;

/**
 * 自定义TextView，该TextView除了系统基本的功能以外，还加入了防止连续点击处理、部分文字点击处理、跑马灯效果等功能
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/4/28
 * @since 1.0
 */
public class SmartTextView extends TextView {
    /**
     * 构造方法
     */
    public SmartTextView() {
        this(BaseApplication.getContext());
    }

    public SmartTextView(@NonNull Context context) {
        this(context, null);
    }

    public SmartTextView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public SmartTextView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    /**
     * 初始化SmartTextView
     *
     * @param context      应用程序上下文
     * @param attrs        属性集
     * @param defStyleAttr 默认样式属性
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        d("initView...");
        // 获取自定义的样式属性
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SmartTextView, defStyleAttr, 0);
        final int n = typedArray.getIndexCount();
        for (int j = 0; j < n; j++) {
            int attr = typedArray.getIndex(j);
            // 用switch判断会报错,改用if else
//            if (attr == R.styleable.SmartTextView_requestFocus) {
//                boolean requestFocus = typedArray.getBoolean(attr, false);
//
//                // 获取焦点
//                if (requestFocus)
//                    requestFocus();
//                LogUtil.d(TAG, "attrRequestFocus = " + requestFocus);
//            }
        }
    }
}