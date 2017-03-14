package com.shyky.library.view.dialog.base;

import android.content.DialogInterface;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shyky.library.view.fragment.base.BaseDialogFragment;

import static com.socks.library.KLog.d;

/**
 * 基础的Dialog（DialogFragment实现）
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/6/10
 * @since 1.0
 */
public abstract class BaseDialog extends BaseDialogFragment {
    @CallSuper
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        d("onDismiss...");
    }

    @CallSuper
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        d("onCancel...");
    }

    @CallSuper
    @Override
    public void dismiss() {
        // 捕获异常不至于造成崩溃
        try {
            super.dismiss();
        } catch (Exception e) {
            d("message", e.getMessage());
        }
    }

    @CallSuper
    @Override
    public void dismissAllowingStateLoss() {
        // 捕获异常不至于造成崩溃
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            d("message", e.getMessage());
        }
    }

    @Override
    public final void show(FragmentManager manager, String tag) {
        showAllowingStateLoss(manager, tag);
    }

    @Override
    public final int show(FragmentTransaction transaction, String tag) {
        throw new IllegalArgumentException("只能调用show(FragmentManager manager, String tag)这个方法");
    }

    /**
     * 显示对话框
     *
     * @param manager fragment manager
     * @param tag     fragment tag
     */
    private void showAllowingStateLoss(@NonNull FragmentManager manager, String tag) {
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(this);
        transaction.add(this, tag);
        transaction.commitAllowingStateLoss();
    }
}