package com.houoy.www.gongxing;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_help)
public class HelpActivity extends AppCompatActivity {
    private ActionBar actionBar;

    @ViewInject(R.id.webView)
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.help));
        initWebView();
    }

    private void initWebView() {
        // 设置WebView的客户端
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
//        WebSettings webSettings = webView.getSettings();
//        // 让WebView能够执行javaScript
//        webSettings.setJavaScriptEnabled(true);
//        // 让JavaScript可以自动打开windows
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        // 设置缓存
//        webSettings.setAppCacheEnabled(true);
//        // 设置缓存模式,一共有四种模式
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        // 设置缓存路径
////        webSettings.setAppCachePath("");
//        // 支持缩放(适配到当前屏幕)
//        webSettings.setSupportZoom(true);
//        // 将图片调整到合适的大小
//        webSettings.setUseWideViewPort(true);
//        // 支持内容重新布局,一共有四种方式
//        // 默认的是NARROW_COLUMNS
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        // 设置可以被显示的屏幕控制
//        webSettings.setDisplayZoomControls(true);
//        // 设置默认字体大小
//        webSettings.setDefaultFontSize(12);
//        webView.loadUrl("http://www.baidu.com/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webView.loadData("更新2018-1-24：" +
                        "</br>1 修改查询页颜色不匹配的问题" +
                        "</br>2 验证一些型号手机无法自动填入验证码" +
                        "</br>3 增加帮助页面",
                "text/html; charset=UTF-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
