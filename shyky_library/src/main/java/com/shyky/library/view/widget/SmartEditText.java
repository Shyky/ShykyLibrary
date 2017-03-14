package com.shyky.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.shyky.library.R;
import com.shyky.library.constant.Constant;
import com.shyky.library.util.ToastUtil;
import com.shyky.library.view.fragment.base.BaseFragment;
import com.shyky.util.RegExpUtil;
import com.shyky.util.TextUtil;

import static com.socks.library.KLog.d;

/**
 * 自定义EditText，这个EditText除了系统基本的功能以外，还加入了输入验证及输入格式化等功能
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/4/21
 * @since 1.0
 */
public class SmartEditText extends EditText implements OnFocusChangeListener, TextWatcher {
    /**
     * EditText右侧的清空图标
     */
    private Drawable clearDrawable;
    /**
     * 是否获取了焦点
     */
    private boolean hasFocus;
    /**
     * 是否输入非法
     */
//    private boolean inputIllegal;
    private int attrValidate;
    private int attrValidateMethod;
    private int attrValidateWidgetResId;
    private int attrValidateInputLength;
    //    private String attrValidateEmptyTip;
    private int attrValidateEmptyTipResId;
    private int attrValidateEqualsWidgetResId;
    //    private String attrValidateIllegalTip;
    private int attrValidateIllegalTipResId;
    //    private String attrValidateLengthLessTip;
    private int attrValidateLengthLessTipResId;
    //    private String attrValidateNotEqualsTip;
    private int attrValidateNotEqualsTipResId;
    private boolean isAttached;
    private Context context;
    private View attachView;
    private BaseFragment attachFragment;

    public SmartEditText(Context context) {
        this(context, null);
    }

    public SmartEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SmartEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        inputIllegal = true; // 默认为输入合法
        isAttached = true;
        initView(attrs, defStyleAttr);
    }

    /**
     * Attach验证事件到Fragment上
     *
     * @param fragment 包含触发验证控件的Fragment
     */
    public void attach(BaseFragment fragment) {
        if (isAttached) {
            // 获取它的顶级View,得到EditText所在的View,然后在RootView中查找控件
//            View rootView = getRootView();

//            String viewResName = getResources().getResourceName(attrValidateWidgetResId);
//            LogUtil.d(TAG, "在当前的Parent View中找到控件:" + viewResName);
            attachView = fragment.findViewById(attrValidateWidgetResId);
            if (attachView == null || attrValidateWidgetResId == 0)
                throw new NullPointerException("没有在当前的Fragment或Activity中找到触发验证控件,请检查是否在布局文件配置SmartEditText标签的地方加入了validateWidget属性");
            // 控件点击事件
//            attachView.setOnClickListener(onAttachViewClickListener);
            isAttached = false;
        }
    }

    /**
     * 绑定触发验证事件到指定的控件上
     *
     * @param view 要触发验证的控件
     */
//    public void bind(View view) {
//        if (view == null)
//            throw new NullPointerException("触发验证的控件为空");
//        else {
//            if (isAttached) {
//                attachView = view;
//                view.setOnClickListener(onAttachViewClickListener);
//                isAttached = false;
//            }
//        }
//    }

    /**
     * 输入框输入验证
     *
     * @return 验证通过返回true，否则返回false
     */
    public boolean validateInput() {
        final String text = getText().toString().trim();
        d("attach view click input text = " + text);
        d("attrValidate = " + attrValidate);
        String str = Integer.toBinaryString(attrValidate);
        for (int n = 0; n < str.length(); n++) {
            int x = Integer.parseInt(str.charAt(n) + "");
            if (x == 1) {
                int num = str.length() - n;
                d("num:" + num);
                switch (num) {
                    case 1:
                        d("validate email...");
                        if (!RegExpUtil.isEmail(text)) {
                            showIllegalTip();
                            return false;
                        }
                        break;
                    case 2:
                        d("validate phone...");
                        if (!RegExpUtil.isPhone(text)) {
                            showIllegalTip();
                            return false;
                        }
                        break;
                    case 4:
                        d("validate number...");
                        if (!RegExpUtil.isNumber(text)) {
                            showIllegalTip();
                            return false;
                        }
                        break;
                    case 6:
                        d("validate empty");
                        if (TextUtil.isEmpty(text)) {
                            showEmptyTip();
                            return false;
                        }
                        break;
                }
            }
        }

        // 没有配置这个属性,默认是输入少于6个字符提示
        if (attrValidateInputLength == 0) {
            if (length() < 6) {
                showLengthLessTip();
                return false;
            }
        } else if (length() < attrValidateInputLength) {
            showLengthLessTip();
            return false;
        }
        return true;
    }

    private void showEmptyTip() {
//        if (attrValidateEmptyTip != null && attrValidateEmptyTipResId != 0) {
//            if (attrValidateEmptyTip == null)
        // 没有配置这个属性
        if (attrValidateEmptyTipResId == 0)
            throw new IllegalArgumentException("请检查是否在布局文件配置SmartEditText标签的地方加入了validateEmptyTip属性");
        showToast(attrValidateEmptyTipResId);
//            else
//                showToast(attrValidateEmptyTip);
//        }
        setText(Constant.EMPTY_STR);
        requestFocus();
    }

    private void showLengthLessTip() {
//        if (attrValidateLengthLessTip != null && attrValidateLengthLessTipResId != 0) {
//            if (attrValidateLengthLessTip == null)
        // 没有配置这个属性
        if (attrValidateInputLength != 0)
            throw new IllegalArgumentException("请检查是否在布局文件配置SmartEditText标签的地方加入了validateLengthLessTip属性");
        showToast(attrValidateLengthLessTipResId);
//            else
//                showToast(attrValidateLengthLessTip);
//        }
        requestFocus();
    }

    private void showIllegalTip() {
//        if (attrValidateIllegalTip != null && attrValidateIllegalTipResId != 0) {
//            if (attrValidateIllegalTip == null)
        // 没有配置这个属性
        if (attrValidateIllegalTipResId == 0)
            throw new IllegalArgumentException("请检查是否在布局文件配置SmartEditText标签的地方加入了validateIllegalTip属性");
        showToast(attrValidateIllegalTipResId);
//            else
//                showToast(attrValidateIllegalTip);
//        }
        requestFocus();
    }

    private void showNotEqualsTip() {
//        if (attrValidateNotEqualsTip != null && attrValidateNotEqualsTipResId != 0) {
//            if (attrValidateNotEqualsTip == null)
        // 没有配置这两个属性
        if (attrValidateEqualsWidgetResId == 0)
            throw new IllegalArgumentException("请检查是否在布局文件配置SmartEditText标签的地方加入了validateEqualsWidget属性");
        else if (attrValidateNotEqualsTipResId == 0)
            throw new IllegalArgumentException("请检查是否在布局文件配置SmartEditText标签的地方加入了validateNotEqualsTip属性");

        if (attrValidateEqualsWidgetResId != 0) {
            EditText et = attachFragment.findViewById(attrValidateEqualsWidgetResId);
            if (!getText().toString().trim().equals(et.getText().toString().trim()))
                showToast(attrValidateNotEqualsTipResId);
        }
//            else
//                showToast(attrValidateNotEqualsTip);
//        }
        requestFocus();
    }

    private void showValidate(int messageResId) {
        switch (attrValidateMethod) {
            case 0:
                showToast(messageResId);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void showToast(int resId) {
        ToastUtil.showShortMessage(resId);
    }

    private void showToast(String message) {
        ToastUtil.showShortMessage(message);
    }

    private void visible(boolean visibility) {

    }

    /**
     * 初始化SmartEditText
     *
     * @param attrs        属性集
     * @param defStyleAttr 默认样式属性
     */
    private void initView(AttributeSet attrs, int defStyleAttr) {
        d("initView...");
        // 获取自定义的样式属性
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SmartEditText, defStyleAttr, 0);
        final int n = typedArray.getIndexCount();
        for (int j = 0; j < n; j++) {
            int attr = typedArray.getIndex(j);
            // 用switch判断会报错,改用if else
            if (attr == R.styleable.SmartEditText_requestFocus) {
                boolean requestFocus = typedArray.getBoolean(attr, false);

                // 获取焦点
                if (requestFocus)
                    requestFocus();
                d("attrRequestFocus = " + requestFocus);
            } else if (attr == R.styleable.SmartEditText_validate) {
                attrValidate = typedArray.getInt(attr, -1/*4*/); // 默认为验证文本(4为文本类型)
                d("attrValidate = " + attrValidate);
            } else if (attr == R.styleable.SmartEditText_validateMethod) {
                attrValidateMethod = typedArray.getInt(attr, -1); // 默认为Toast提示验证消息
                d("attrValidateMethod = " + attrValidateMethod);
            } else if (attr == R.styleable.SmartEditText_validateInputLength) {
                attrValidateInputLength = typedArray.getInt(attr, -1);
                d("attrValidateInputLength = " + attrValidateInputLength);
            } else if (attr == R.styleable.SmartEditText_validateWidget) {
                // 获取绑定触发验证的控件
                attrValidateWidgetResId = typedArray.getResourceId(attr, -1);
//                 初始化验证控件点击事件
//                onAttachViewClickListener = new OnAttachViewClickListener();
                d("attrValidateWidgetResId = " + attrValidateWidgetResId);
            } else if (attr == R.styleable.SmartEditText_validateEmptyTip) {
//                attrValidateEmptyTip = typedArray.getString(attr); // 默认为输入少于6位字符提示
//                if (attrValidateEmptyTip == null)
                attrValidateEmptyTipResId = typedArray.getResourceId(attr, -1);
                d("attrValidateEmptyTipResId = " + attrValidateEmptyTipResId);
            } else if (attr == R.styleable.SmartEditText_validateIllegalTip) {
//                attrValidateIllegalTip = typedArray.getString(attr);
//                if (attrValidateIllegalTip == null)
                attrValidateIllegalTipResId = typedArray.getResourceId(attr, -1);
                d("attrValidateIllegalTipResId = " + attrValidateIllegalTipResId);
            } else if (attr == R.styleable.SmartEditText_validateLengthLessTip) {
//                attrValidateLengthLessTip = typedArray.getString(attr);
//                if (attrValidateLengthLessTip == null)
                attrValidateLengthLessTipResId = typedArray.getResourceId(attr, -1);
                d("attrValidateLengthLessTipResId = " + attrValidateLengthLessTipResId);
            } else if (attr == R.styleable.SmartEditText_validateNotEqualsTip) {
//                attrValidateNotEqualsTip = typedArray.getString(attr);
//                if (attrValidateNotEqualsTip == null)
                attrValidateNotEqualsTipResId = typedArray.getResourceId(attr, -1);
                d("attrValidateNotEqualsTipResId = " + attrValidateNotEqualsTipResId);
            } else if (attr == R.styleable.SmartEditText_validateEqualsWidget) {
                attrValidateEqualsWidgetResId = typedArray.getResourceId(attr, -1);
                d("attrValidateEqualsWidgetResId = " + attrValidateEqualsWidgetResId);
            }
        }
        // 释放资源
        typedArray.recycle();

        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        clearDrawable = getCompoundDrawables()[2];
        if (clearDrawable == null) {
            clearDrawable = getResources().getDrawable(R.mipmap.cancel);
        }
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
        // 默认隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /* @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     *  getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     *  getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight:
     *  distance 删除图标顶部边缘到控件顶部边缘的距离
     *  distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    setText(Constant.EMPTY_STR); // 清空输入的文本
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? clearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        d("onTextChanged...");
        if (hasFocus)
            setClearIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        d("beforeTextChanged...");
    }

    @Override
    public void afterTextChanged(Editable s) {
        d("afterTextChanged...");
    }
}