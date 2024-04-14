package com.example.autolink031;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Meet {
    private static final String DEFAULT_URL = "https://192.168.0.212/";

    // 获取 WebView 实例并配置基本设置
    @SuppressLint("SetJavaScriptEnabled")
    public static WebView createWebView(Context context, String roomid) {
        WebView webView = new WebView(context);
        WebSettings webSettings = webView.getSettings();

        // 启用 JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 启用本地存储
        webSettings.setDomStorageEnabled(true);
        // 启用 WebView 内置的地理定位功能
        webSettings.setGeolocationEnabled(true);
        // 设置允许访问文件系统
        webSettings.setAllowFileAccess(true);
        // 设置允许通过 JS 打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存模式为不使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置 WebViewClient，以便在 WebView 中加载页面
        webView.setWebViewClient(new WebViewClient());
        // 设置 WebChromeClient，以便在 WebView 中处理 JavaScript 对话框、网站图标、标题等
        webView.setWebChromeClient(new WebChromeClient());

        // 构建URL
        String url = DEFAULT_URL + roomid;
        // 加载指定 URL
        webView.loadUrl(url);

        return webView;
    }
}
