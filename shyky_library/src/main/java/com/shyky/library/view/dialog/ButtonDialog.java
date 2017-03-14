package com.shyky.library.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.shyky.library.BaseApplication;
import com.shyky.library.R;
import com.shyky.library.util.DensityUtil;
import com.shyky.library.view.dialog.base.BaseDialog;
import com.shyky.util.TextUtil;

/**
 * 带有按钮的对话框（AlertDialog实现）
 *
 * @author Shyky
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/6/8
 * @since 1.0
 */
public abstract class ButtonDialog extends BaseDialog {
    public static final int BUTTON_OK = 11;
    public static final int BUTTON_CANCEL = 22;
    private OnDialogButtonClickListener onDialogButtonClickListener;
    private AlertDialog alertDialog;
    private String title;
    private int titleResId;
    private String message;
    private int messageResId;

    /**
     * 对话框按钮点击事件回调监听类
     */
    public interface OnDialogButtonClickListener {
        /**
         * 对话框按钮点击事件
         *
         * @param view  按钮控件
         * @param which 哪个按钮
         */
        void onClick(View view, int which);
    }

    @LayoutRes
    public abstract int getDialogViewLayoutResId();

    public final void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setTitle(@StringRes int resId) {
        titleResId = resId;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public void setMessage(@StringRes int resId) {
        messageResId = resId;
    }

    @StringRes
    public final int getPositiveButtonTextResId() {
        return -1;
    }

    @StringRes
    public final int getNegativeButtonTextResId() {
        return -1;
    }

    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final int positiveBtnTextResId = getPositiveButtonTextResId() < 0 ? R.string.text_ok : getPositiveButtonTextResId();
        final int negativeBtnTextResId = getNegativeButtonTextResId() < 0 ? R.string.text_cancel : getNegativeButtonTextResId();
        final View inflateView = View.inflate(BaseApplication.getContext(), getDialogViewLayoutResId(), null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(inflateView, DensityUtil.dip2px(60), DensityUtil.dip2px(10),
                        DensityUtil.dip2px(60), DensityUtil.dip2px(10)).
                        setPositiveButton(positiveBtnTextResId, null).
                        setNegativeButton(negativeBtnTextResId, null);
        if (!TextUtil.isEmpty(title))
            builder.setTitle(title);
        else if (titleResId > 0)
            builder.setTitle(titleResId);
        else if (!TextUtil.isEmpty(message))
            builder.setMessage(message);
        else if (messageResId > 0)
            builder.setMessage(messageResId);

//        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//                .setView(inflateView, DensityUtil.dip2px(10), DensityUtil.dip2px(20),
//                        DensityUtil.dip2px(10), DensityUtil.dip2px(10)).
//                        setPositiveButton(positiveBtnTextResId, null).
//                        setNegativeButton(negativeBtnTextResId, null).
//        create();
        alertDialog = builder.create();
        alertDialog.show();
        // 解决默认点击了按钮会关闭对话框问题
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDialogButtonClickListener != null) {
                    onDialogButtonClickListener.onClick(view, BUTTON_OK);
//                    if (onDialogButtonClickListener.validate(etEmail)) {
//                        // 输入合法,隐藏软键盘
//                        KeyboardUtil.hideSoftKeyboard(getActivity(), etEmail);
//                        onDialogButtonClickListener.onClick(BUTTON_OK, etEmail.getText().toString().trim());
//                        alertDialog.dismiss();
//                    }
                }
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                KeyboardUtil.hideSoftKeyboard(getActivity(), etEmail);
                if (onDialogButtonClickListener != null)
                    onDialogButtonClickListener.onClick(view, BUTTON_CANCEL);
                alertDialog.dismiss();
//                if (onDialogButtonClickListener != null)
//                    onDialogButtonClickListener.onClick(BUTTON_CANCEL, etEmail.getText().toString().trim());
            }
        });
        return alertDialog;
    }

    @Override
    public final void dismiss() {
        super.dismiss();
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
//        super.dismiss();
    }
}