package com.shyky.library.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shyky.library.R;
import com.shyky.util.NumberUtil;
import com.shyky.util.TextUtil;

import static com.socks.library.KLog.d;

/**
 * 自定义数量选择器控件
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/6/13
 * @since 1.0
 */
public class NumberSelectView extends LinearLayout {
    private Context context;
    private TextView leftButton;
    private TextView rightButton;
    private EditText editText;
    /**
     * 是否是设置Text造成的数字改变而触发OnNumChangeListener
     */
    private boolean flag;
    /**
     * 数字改变监听事件
     */
    private OnNumberChangeListener onNumberChangeListener;
    /**
     * 左边的控件
     */
    private RelativeLayout leftRelativeLayout;
    /**
     * 右边的控件
     */
    private RelativeLayout rightRelativeLayout;
    /**
     * 中间文本输入框显示的数量
     */
    private int count;
    /**
     * 中间文本输入框显示的最大数量
     */
    private int maxCount;
    /**
     * 中间文本输入框显示的最小数量
     */
    private int minCount = 1;
    /**
     * 当前输入的类型：为1可以输入，否则不可以输入
     */
    private int type = 1;
    private int leftViewBackground;
    private int rightViewBackground;

    public interface OnNumberChangeListener {
        /**
         * 输入框中的数值改变事件
         *
         * @param view 整个NumberSelectView
         * @param num  输入框的数值
         */
        void onNumChange(@NonNull View view, int num);
    }

    public NumberSelectView(@NonNull Context context) {
        this(context, null);
    }

    public NumberSelectView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 获取配置属性
        initAttribute(attrs);
        // 初始化View
        initView();
        if (type != 1) {
            editText.setFocusable(false);
            editText.setEnabled(false);
        }
    }

    private void initAttribute(AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberSelectView);
        count = typedArray.getInteger(R.styleable.NumberSelectView_count, 1);
        type = typedArray.getInteger(R.styleable.NumberSelectView_type, 1);
        maxCount = typedArray.getInteger(R.styleable.NumberSelectView_maxCount, 1000);
        minCount = typedArray.getInteger(R.styleable.NumberSelectView_minCount, 1);
        leftViewBackground = typedArray.getResourceId(R.styleable.NumberSelectView_leftViewBackground, 0);
        rightViewBackground = typedArray.getResourceId(R.styleable.NumberSelectView_rightViewBackground, 0);
        typedArray.recycle();
    }

    /**
     * 初始化View
     */
    private void initView() {
        initialise();
        setViewsLayoutParam();
        setViewListener();
        //如果有自定义背景，设置控件背景
        if (leftViewBackground > 0)
            leftRelativeLayout.setBackgroundResource(leftViewBackground);
        else if (rightViewBackground > 0)
            rightRelativeLayout.setBackgroundResource(leftViewBackground);
    }

    /**
     * 实例化内部View
     */
    private void initialise() {
        View view = View.inflate(context, R.layout.widget_number_select_view, null);
        if (view != null) {
            leftRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_number_left);
            rightRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_number_right);
            leftButton = (TextView) view.findViewById(R.id.tv_number_left);
            rightButton = (TextView) view.findViewById(R.id.tv_number_right);
            editText = (EditText) view.findViewById(R.id.et_number_center);
            addView(view);
        }
    }

    /**
     * 设置布局参数
     */
    private void setViewsLayoutParam() {
        leftRelativeLayout.setTag("-");
        rightRelativeLayout.setTag("+");
        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * 设置文本变化相关监听事件
     */
    private void setViewListener() {
        //左边控件点击事件：
        leftRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                d("left view click...");
                final String text = editText.getText().toString().trim();
                int number = TextUtil.isEmptyAndNull(text) || "0".equals(text) ? 1 : NumberUtil.toInt(text);
                if (number < 1) {
                    number = 1;
                    editText.setText("1");
                } else if (number > 1000) {
                    number = 1000;
                    editText.setText("1000");
                } else {
                    number--;
                }
                if (number < 1) {
                    number = 1;
                } else {
                    if (onNumberChangeListener != null) {
                        onNumberChangeListener.onNumChange(NumberSelectView.this, number);
                    }
                }
                editText.setText(TextUtil.toString(number));
            }
        });
        rightRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                d("right view click...");
                final String text = editText.getText().toString().trim();
                int number = TextUtil.isEmptyAndNull(text) ? 1 : NumberUtil.toInt(text);
                if (number < 1) {
                    number = 1;
                } else if (number > 1000) {
                    number = 1000;
                } else {
                    number++;
                }
                editText.setText(TextUtil.toString(number));
                if (onNumberChangeListener != null) {
                    onNumberChangeListener.onNumChange(NumberSelectView.this, number);
                }
            }
        });
        // 在xml布局中设置无效，需要代码设置
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) v;
                if (!edit.isFocusable()) {
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                numberDone();
            }
        });
        // 软键盘完成按钮点击事件
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 这个判断不行
//                 if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    if (event != null) {
                        if (event.getAction() == 1) {
                            numberDone();
                        }
                    } else {
                        numberDone();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 设置左边的控件是否可用
     *
     * @param enabled 返回true表示可用，否则禁用
     */
    public void setLeftViewEnabled(boolean enabled) {
        leftRelativeLayout.setEnabled(enabled);
    }

    /**
     * 设置右边的控件是否可用
     *
     * @param enabled 返回true表示可用，否则禁用
     */
    public void setRightViewEnabled(boolean enabled) {
        rightRelativeLayout.setEnabled(enabled);
    }

    /**
     * 获取editText中的值
     *
     * @return
     */
    public int getNum() {
        if (TextUtil.notEmptyAndNull(editText.getText().toString())) {
            return Integer.parseInt(editText.getText().toString());
        } else {
            return 0;
        }
    }

    /**
     * 设置EditText文本变化监听
     *
     * @param onNumberChangeListener
     */
    public void setOnNumberChangeListener(@NonNull OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }

    /**
     * 设置输入的值
     *
     * @param number 输入的值
     */
    public void setNumber(int number) {
//        flag = true;
        editText.setText(TextUtil.toString(number));
        editText.clearFocus();
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }

    private int number;

    public final int getNumber() {
        return number == 0 ? 1 : number;
    }

    private void numberDone() {
        final int numInt = TextUtil.isEmpty(editText.getText().toString()) ? -1 : NumberUtil.toInt(editText.getText().toString());
        if (numInt < minCount) {
            //如果输入的数小于最小值，那么显示最小值
            count = minCount;
            editText.setText(String.valueOf(minCount));
        } else if (numInt == minCount) {
            //如果输入的数等于最小值，那么显示不可点击图标
            count = minCount;
            leftButton.setBackgroundResource(R.mipmap.less_no);
        } else if (numInt > maxCount) {
            //如果输入的数大于最大值，那么显示最大值
            count = maxCount;
            editText.setText(String.valueOf(maxCount));
        } else if (numInt == maxCount) {
            //如果输入的数等于最大值，那么显示不可点击图标
            count = maxCount;
            rightButton.setBackgroundResource(R.mipmap.add_no);
        } else {
            leftButton.setBackgroundResource(R.mipmap.less);
            rightButton.setBackgroundResource(R.mipmap.add);
            count = numInt;
        }
        //设置回调
        if (onNumberChangeListener != null) {
            onNumberChangeListener.onNumChange(NumberSelectView.this, count);
        }
        editText.setSelection(String.valueOf(count).length());
    }

    /**
     * 加减按钮事件监听器
     */
    private class OnButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            final String text = editText.getText().toString().trim();
            int number = TextUtil.isEmpty(text) ? -1 : NumberUtil.toInt(text);
            if (number != -1) {
                flag = false;
                if ("+".equals(v.getTag())) {
                    editText.setText(String.valueOf(number++));
                } else if ("-".equals(v.getTag())) {
                    editText.setText(String.valueOf(number--));
                }
            } else {
                editText.setText("1");
            }
        }
    }
}