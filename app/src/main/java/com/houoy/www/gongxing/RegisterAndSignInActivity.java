package com.houoy.www.gongxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.houoy.www.gongxing.event.LoginEvent;
import com.houoy.www.gongxing.event.RegisterEvent;
import com.houoy.www.gongxing.fragment.MyFragmentPagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_register_and_sign_in)
public class RegisterAndSignInActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    @ViewInject(R.id.txt_topbar)
    private TextView txt_topbar;
    @ViewInject(R.id.rg_tab_bar)
    private RadioGroup rg_tab_bar;
    @ViewInject(R.id.rb_channel)
    private RadioButton rb_channel;
    @ViewInject(R.id.rb_message)
    private RadioButton rb_message;
    @ViewInject(R.id.vpager)
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_TWO2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注入view和事件
        x.view().inject(this);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_channel.setChecked(true);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void bindViews() {
//        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
//        rb_channel = (RadioButton) findViewById(R.id.rb_channel);
//        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rg_tab_bar.setOnCheckedChangeListener(this);

//        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_channel:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
        }
    }

    //订阅者，后台长时间异步任务
//    @Subscribe(threadMode = ThreadMode.ASYNC)
    //订阅者，将在主进执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleConnect(RegisterEvent event) {
        switch (event.getType()) {
            case "1":
                vpager.setCurrentItem(PAGE_TWO2);
                break;
            case "2":
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_channel.setChecked(true);
                    break;
                case PAGE_TWO:
                case PAGE_TWO2:
                    rb_message.setChecked(true);
                    break;
            }
        }
    }
}

