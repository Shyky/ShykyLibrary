package com.shyky.library.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.shyky.library.util.KeyboardUtil;
import com.shyky.util.TextUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 带有一个输入框的对话框
 *
 * @author Shyky
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/6/8
 * @since 1.0
 */
public abstract class InputDialog extends ButtonDialog {
    private EditText editText;
    private String inputText;
    private OnButtonClickListener onButtonClickListener;

    /**
     * 对话框按钮点击事件回调监听类
     */
    public interface OnButtonClickListener {
        /**
         * 输入框验证
         *
         * @param editText 输入框
         * @return 验证通过返回true, 否则返回false
         */
        boolean validate(EditText editText);

        /**
         * 对话框按钮点击事件
         *
         * @param which     哪个按钮
         * @param inputText 输入框输入的内容
         */
        void onClick(int which, String inputText);
    }

    /**
     * 用于弹出输入法,子线程不能操作UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1)
                showKeyboard(editText);
        }
    };

    @IdRes
    public abstract int getEditTextResId();

    /**
     * 输入框获取焦点并弹出输入法键盘
     *
     * @param editText 输入框
     */
    public final void showKeyboard(EditText editText) {
        //设置可获得焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        //请求获得焦点
        editText.requestFocus();
        //调用系统输入法
        InputMethodManager inputManager = (InputMethodManager) editText
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public final void setInputText(@NonNull String inputText) {
        this.inputText = inputText;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = (EditText) view.findViewById(getEditTextResId());

        //
        if (inputText != null) {
            editText.setText(inputText.trim());
            editText.setSelection(inputText.length());
        }

        // 输入框是空时弹出软键盘
        if (TextUtil.isEmpty(editText.getText().toString().trim())) {
            // 延时弹出输入法，解决在自定义的对话框中的EditText无法弹出输入法问题
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }, 300);
        }

        setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public void onClick(View view, int which) {
                switch (which) {
                    case BUTTON_OK:
                        if (onButtonClickListener != null) {
                            if (onButtonClickListener.validate(editText)) {
                                // 输入合法,隐藏软键盘
                                KeyboardUtil.hideSoftKeyboard(getActivity(), editText);
                                onButtonClickListener.onClick(BUTTON_OK, editText.getText().toString().trim());
                                dismiss();
                            }
                        }
                        break;
                    case BUTTON_CANCEL:
                        KeyboardUtil.hideSoftKeyboard(getActivity(), editText);
//                        dismiss();
//                        if (onDialogButtonClickListener != null)
//                            onDialogButtonClickListener.onClick(BUTTON_CANCEL, etEmail.getText().toString().trim());
                        break;
                }
            }
        });
    }
}