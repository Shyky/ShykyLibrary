package com.shyky.library.view.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.shyky.library.R;
import com.shyky.library.util.LayoutUtil;
import com.shyky.library.view.dialog.base.BaseDialog;

import static com.socks.library.KLog.d;

/**
 * 加载中对话框
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/4/18
 * @since 1.0
 */
public final class LoadingDialog extends BaseDialog {
    private TextView tvMessage;
    private String message;
    private int messageResId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // 没有标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设置对话框显示布局
        final View inflateView = LayoutUtil.inflate(getContext(), R.layout.dialog_loading);
        tvMessage = (TextView) inflateView.findViewById(R.id.tv_message);
        if (message != null) {
            d("message", message);
            tvMessage.setText(message);
        } else if (messageResId > 0) {
            d("messageResId", messageResId);
            tvMessage.setText(messageResId);
        } else {
            tvMessage.setText(R.string.text_loading);
        }
        dialog.setContentView(R.layout.dialog_loading);

        // 背景透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public void setMessage(@StringRes int resId) {
        messageResId = resId;
    }

    public final void show(FragmentManager manager) {
        show(manager, "loadingDialog");
    }
}