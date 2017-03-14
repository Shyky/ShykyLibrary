package com.shyky.library.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shyky.library.R;
import com.shyky.library.event.MessageEvent;
import com.shyky.library.util.StorageUtil;
import com.shyky.library.view.fragment.base.BaseFragment;
import com.shyky.util.ObjectUtil;
import com.shyky.util.TextUtil;

import static com.socks.library.KLog.d;

/**
 * 带有WebView浏览网页的Fragment
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @email sj1510706@163.com
 * @date 2016/8/30
 * @since 1.0
 */
public final class BrowserFragment extends BaseFragment {
    private LinearLayout llContent;
    private ProgressBar progressBar;
    private WebView webView;
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
            view.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            // 拦截指定的url
            final String interceptRequestUrl = getStringArgument("interceptRequestUrl");
            d("WebView要跳转的url", url);
            d("要拦截的url", interceptRequestUrl);
            if (TextUtil.notEmptyAndNull(interceptRequestUrl)) {
                // 打开的url与要拦截的url进行对比，以它开始就表示拦截成功
                if (url != null && url.trim().startsWith(interceptRequestUrl.trim())) {
                    d("成功拦截到指定url", url);
                    // 拦截成功，发送消息告诉别的页面拦截成功，并把拦截的url发送出去
                    final MessageEvent messageEvent = new MessageEvent();
                    messageEvent.what = 886;
                    messageEvent.message = interceptRequestUrl.trim();
                    sendMessage(messageEvent);
                }
            }
            return super.shouldInterceptRequest(view, url);
        }
    };
    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // 获得网页的加载进度
            if (newProgress < 100) {
                progressBar.setProgress(newProgress);
            } else {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            // HTML5定位
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            // 多窗口的问题
            WebView.HitTestResult result = view.getHitTestResult();
            // 解决在有些设备上报空指针问题
            if (ObjectUtil.notNull(result)) {
                String data = result.getExtra();
                webView.loadUrl(data);
            }
            return true;
        }
    };

    public final static BrowserFragment getInstance(@NonNull String url) {
        final BrowserFragment instance = new BrowserFragment();
        instance.setArgument("url", url);
        return instance;
    }

    public final static BrowserFragment getInstance(@NonNull String url, @NonNull String interceptRequestUrl) {
        final BrowserFragment instance = new BrowserFragment();
        instance.setArgument("url", url);
        instance.setArgument("interceptRequestUrl", interceptRequestUrl);
        return instance;
    }

    public void setUrl(@NonNull String url) {
        setArgument("url", url);
    }

    public String getUrl() {
        return getStringArgument("url");
    }

    public void setInterceptRequestUrl(@NonNull String interceptRequestUrl) {
        setArgument("interceptRequestUrl", interceptRequestUrl);
    }

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.aaaaaa;
    }

    @Override
    public void findView(View inflateView, Bundle savedInstanceState) {
        super.findView(inflateView, savedInstanceState);
        llContent = findViewById(R.id.layout_content);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        super.initView(inflateView, savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getContext().getApplicationContext());
        webView.setLayoutParams(params);
        llContent.addView(webView);
        initWebView();
    }

    private void initWebView() {
        final WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);
        // 调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        webSettings.setJavaScriptEnabled(true);
        // HTML5数据存储
        // 有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        StorageUtil.getSDcardRootPath();
        String appCachePath = StorageUtil.getCachePath();
        webSettings.setAppCachePath(appCachePath);
        // 支持缩放
        webSettings.setSupportZoom(true);
        // 缩放按钮
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        // 不用保存表单上的填入的密码，解决在有些内存小的设备上因为Activity被回收造成show Dialog报错问题
        webSettings.setSavePassword(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 多窗口的问题
        // html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        // 需要重写WebChromeClient的onCreateWindow方法
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        final String url = getStringArgument("url");
        d("url", url);
        if (TextUtil.notEmptyAndNull(url))
            webView.loadUrl(url);
    }

    public void goBack() {
        if (webView.canGoBack())
            webView.goBack();
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}