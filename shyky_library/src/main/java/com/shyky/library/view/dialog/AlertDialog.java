package com.shyky.library.view.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.shyky.library.R;
import com.shyky.library.util.LayoutUtil;
import com.shyky.library.view.dialog.base.BaseDialog;
import com.shyky.util.ObjectUtil;
import com.shyky.util.TextUtil;

import static com.socks.library.KLog.d;

/**
 * 警告对话框
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/8/27
 * @since 1.0
 */
public final class AlertDialog extends BaseDialog {
    public static final int BUTTON_LEFT = 34;
    public static final int BUTTON_RIGHT = 33;
    private TextView tvMessage;
    private Button btnLeft;
    private Button btnRight;
    private String message;
    private int messageResId;
    private String btnLeftText;
    private int btnLeftTextResId;
    private String btnRightText;
    private int btnRightTextResId;
    private OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {
        /**
         * button点击事件回调方法
         *
         * @param alertDialog
         * @param buttonResId
         * @param whichButton
         */
        void onClick(@NonNull AlertDialog alertDialog, int buttonResId, int whichButton);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        // 没有标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设置对话框显示布局
        final View inflateView = LayoutUtil.inflate(getContext(), R.layout.dialog_alert);
        tvMessage = (TextView) inflateView.findViewById(R.id.tv_message);
        btnLeft = (Button) inflateView.findViewById(R.id.btn_left);
        btnRight = (Button) inflateView.findViewById(R.id.btn_right);

        if (message != null) {
            d("message", message);
            tvMessage.setText(message);
        } else if (messageResId > 0) {
            d("messageResId", messageResId);
            tvMessage.setText(messageResId);
        }

        if (TextUtil.isEmptyAndNull(btnLeftText))
            btnLeft.setText(btnLeftText);
        else if (btnLeftTextResId > 0)
            btnLeft.setText(btnLeftTextResId);

        if (TextUtil.isEmptyAndNull(btnRightText))
            btnRight.setText(btnRightText);
        else if (btnRightTextResId > 0)
            btnRight.setText(btnRightTextResId);

        if (ObjectUtil.notNull(onButtonClickListener)) {
            btnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClickListener.onClick(AlertDialog.this, btnLeft.getId(), BUTTON_LEFT);
                }
            });

            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClickListener.onClick(AlertDialog.this, btnRight.getId(), BUTTON_RIGHT);
                }
            });
        }

        dialog.setContentView(inflateView);
        // 背景透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    public void setOnButtonClickListener(@NonNull OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public void setMessage(@StringRes int resId) {
        messageResId = resId;
    }

    public void setLeftButtonText(@NonNull String text) {
        btnLeftText = text;
    }

    public void setLeftButtonText(@StringRes int resId) {
        btnLeftTextResId = resId;
    }

    public void setRightButtonText(@NonNull String text) {
        btnRightText = text;
    }

    public void setRightButtonText(@StringRes int resId) {
        btnRightTextResId = resId;
    }

    public final void show(FragmentManager manager) {
        show(manager, "alertDialog");
    }
}