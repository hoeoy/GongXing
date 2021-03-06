package com.houoy.www.gongxing;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_help)
public class HelpActivity extends MyAppCompatActivity {
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
        StringBuilder detail = new StringBuilder("更新2018-1-24：" +
                "</br>1 修改查询页颜色不匹配的问题" +
                "</br>2 验证一些型号手机无法自动填入验证码" +
                "</br>3 增加帮助页面" +
                "</br></br>");

        detail.insert(0, "更新2018-1-25：" +
                "</br>1 修改clientid为topic从而支持接收历史消息" +
                "</br>2 升级数据库版本号" +
                "</br>3 调整message格式增加sign从而解决消息内容为null的Bug" +
                "</br></br>");

        detail.insert(0, "更新2018-1-26：" +
                "</br>1 解决查询页的报警数量不准确的bug" +
                "</br>2 升级数据库版本号" +
                "</br>3 将清楚缓存按钮移动到设置页面，增加清楚缓存界面" +
                "</br></br>");

        detail.insert(0, "更新2018-1-27：" +
                "</br>1 设置页面增加消息通知设置功能" +
                "</br>2 升级数据库版本号" +
                "</br>3 提示音跟随系统或自定义" +
                "</br></br>");

        detail.insert(0, "更新2018-1-28：" +
                "</br>1 增加splash页面" +
                "</br>2 消息页面显示时间" +
                "</br>3 清空缓存后发送事件，清空消息页面的数据" +
                "</br></br>");

        detail.insert(0, "更新2018-1-29：" +
                "</br>1 更新本地sqlite版本" +
                "</br>2 查询页面：只有一个区域则默认展开。多个区域展开所有报警状态的。" +
                "</br>3 报警消息确认收到之后按钮状态改变" +
                "</br></br>");

        detail.insert(0, "更新2018-3-7：" +
                "</br>1 重构消息接收模块" +
                "</br>2 更改一些BUG" +
                "</br></br>");
        detail.insert(0, "更新2018-4-3：" +
                "</br>1 增加更新功能" +
                "</br></br>");

        webView.loadData(detail.toString(), "text/html; charset=UTF-8", null);
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
