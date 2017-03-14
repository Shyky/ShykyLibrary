package com.shyky.library.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.shyky.library.view.dialog.base.BaseDialog;

/**
 * 自定义日期选择对话框
 *
 * @author Shyky
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/6/23
 * @since 1.0
 */
public final class DatePickerDialog extends BaseDialog {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 没有标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DateDialog(getActivity());
    }
}