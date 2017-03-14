package com.shyky.library.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.shyky.library.R;
import com.shyky.library.constant.Constant;
import com.shyky.library.event.MessageEvent;
import com.shyky.library.view.activity.base.BaseActivity;
import com.shyky.library.view.fragment.BrowserFragment;
import com.shyky.util.ObjectUtil;

import static com.socks.library.KLog.d;

/**
 * 带有WebView浏览网页的Activity
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.6
 * @email sj1510706@163.com
 * @date 2016/8/9
 * @since 1.0
 */
public abstract class BrowserActivity extends BaseActivity {
    /**
     * WebView要加载的url
     */
    public static final String URL = "url";
    /**
     * Toolbar显示标题
     */
    public static final String TITLE = "title";
    /**
     * 要拦截的url
     */
    public static final String INTERCEPT_URL = "interceptRequestUrl";
    /**
     * 要拦截的标题
     */
    public static final String INTERCEPT_TITLE = "interceptTitle";
    public static final String CATCH_TITLE = "catchTitle";
    /**
     * 拦截url成功标识码
     */
    public static final int INTERCEPT_URL_SUCCESS = 18;
    /**
     * 拦截url失败标识码
     */
    public static final int INTERCEPT_URL_FAILURE = 14;
    /**
     * 是否屏蔽按下返回键
     */
    public static final String EXTRA_IS_SHIELD_KEY_BACK = "shieldKeyBack";
    /**
     * 提示对话框消息文本资源ID
     */
    public static final String EXTRA_DIALOG_MESSAGE_RESOURCE_ID = "dialogMessageResId";
    /**
     * 提示对话框
     */
    private AlertDialog alertDialog;
    /**
     * 带有WebView的Fragment
     */
    private BrowserFragment browserFragment;
    /**
     * WebView要加载的URL
     */
    private String url;
    /**
     * Toolbar显示标题
     */
    protected String title;
    /**
     * 要拦截的URL
     */
    private String interceptRequestUrl;
    /**
     * 是否屏蔽返回键
     */
    private boolean shieldKeyBack;
    /**
     * 对话框提示信息string资源ID
     */
    @StringRes
    private int dialogMessageResId;

    @Override
    public final int getFragmentContainerViewId() {
        return R.id.fragmentContainer;
    }

    @Override
    public final Fragment getFragment() {
        browserFragment = BrowserFragment.getInstance(url, interceptRequestUrl);
        return browserFragment;
    }

    @CallSuper
    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        url = getStringExtra(URL);
        title = getStringExtra(TITLE);
        interceptRequestUrl = getStringExtra(INTERCEPT_URL);
        shieldKeyBack = getBooleanExtra(EXTRA_IS_SHIELD_KEY_BACK);
        dialogMessageResId = getIntExtra(EXTRA_DIALOG_MESSAGE_RESOURCE_ID);
        d("url = " + url);
        d("title = " + title);
        d("shieldKeyBack = " + shieldKeyBack);
        d("dialogMessageResId = " + dialogMessageResId);
    }

    @CallSuper
    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (ObjectUtil.isNull(url))
            throw new NullPointerException("请通过Intent对象的putString方法传递Url，ExtraName取BrowserActivity.URL");
    }

    @CallSuper
    @Override
    public void initDialog(@NonNull Bundle savedInstanceState) {
        super.initDialog(savedInstanceState);
        if (dialogMessageResId > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(dialogMessageResId)
                    .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 发消息通知别的界面按下了返回键
                            sendMessage(new MessageEvent(Constant.EVENT_TYPE.ON_KEY_BACK_DOWN));
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.text_no, null);
            alertDialog = builder.create();
        }
    }

    @Override
    public void onToolbarNavigationClick() {
//        if (browserFragment != null) {
//            if (browserFragment.canGoBack()) {
//                browserFragment.goBack();
//                return;
//            }
//        }
        if (shieldKeyBack && dialogMessageResId > 0) {
            alertDialog.show();
        } else
            finish();
    }

    @Override
    public void onReceiveMessage(@NonNull MessageEvent event) {
        super.onReceiveMessage(event);
        switch (event.what) {
            case 886:
                // 拦截url成功消息，将数据回传给别的Activity
                final Intent intent = new Intent();
                intent.putExtra(Constant.EXTRA_NAME.RESULT, event.message);
                setResult(INTERCEPT_URL_SUCCESS, intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (shieldKeyBack && dialogMessageResId > 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // 发消息通知别的界面按下了返回键
//                sendMessage(new MessageEvent(Constant.EVENT_TYPE.ON_KEY_BACK_DOWN));
//                if (shieldKeyBack) {
//                    return super.onKeyDown(keyCode, event);
//                } else {
//                    browserFragment.goBack();
//                    return true;
//                }

                alertDialog.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}