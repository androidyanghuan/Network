package com.example.classroom.activitys;

import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.classroom.R;
import com.example.classroom.databinding.ActivityBroswerBinding;
import com.example.classroom.utils.MyLog;
import com.example.classroom.views.CustomProgressDialog;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class BroswerActivity extends BaseActivity {
    private static final String TAG = "BroswerActivity";

    private ActivityBroswerBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_broswer);
        CustomProgressDialog.createDefaultLoadingDialog(this, true);
        mBinding.setTitle(String.format("%s", getIntent().getStringExtra("title")));
        WebSettings settings = mBinding.activityBroswerWv.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        // 5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(0);
        }
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        //不允许访问本地文件（不影响assets和resources资源的加载）
        settings.setAllowFileAccess(true); //是否可访问本地文件，默认值 true
        settings.setAllowContentAccess(true); //是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        mBinding.activityBroswerWv.loadUrl(String.format("%s", getIntent().getStringExtra("url")));
        mBinding.activityBroswerWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                MyLog.log(TAG, "*********onPageStarted********" + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                MyLog.log(TAG, "*********shouldOverrideUrlLoading********" + request);
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CustomProgressDialog.cancel();
                MyLog.log(TAG, "*********onPageFinished********" + view.isActivated());
                //  Toast.makeText(view.getContext(), "onPageFinished", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                CustomProgressDialog.cancel();
                MyLog.log(TAG, "s:" + s + ",s1:" + s1);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                CustomProgressDialog.cancel();
                MyLog.log(TAG, "web resource error:" + webResourceError.toString());
            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBinding.activityBroswerWv.canGoBack()) {
                mBinding.activityBroswerWv.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}