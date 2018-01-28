package com.houoy.www.gongxing;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.houoy.www.gongxing.adapter.RegisterAndSignInAdapter;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.event.LoginEvent;
import com.houoy.www.gongxing.event.NetBroadcastReceiver;
import com.houoy.www.gongxing.event.NetworkChangeEvent;
import com.houoy.www.gongxing.event.RegisterEvent;
import com.houoy.www.gongxing.event.SMSContentObserver;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
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

    private RegisterAndSignInAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_TWO2 = 2;

    private UserDao userDao;
    private GongXingController gongXingController;

    NetBroadcastReceiver netWorkStateReceiver;
    SMSContentObserver smsContentObserver;
    //    SMSBroadcastReceiver oSMSBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注入view和事件
        x.view().inject(this);
        smsContentObserver = new SMSContentObserver(this, new Handler(),
                new SMSContentObserver.SmsListener() {
                    @Override
                    public void onResult(String smsContent) {
                        //todo
                    }
                });
        mAdapter = new RegisterAndSignInAdapter(getSupportFragmentManager());
        bindViews();
        rb_channel.setChecked(true);
        EventBus.getDefault().register(this);
        userDao = UserDao.getInstant();
        gongXingController = GongXingController.getInstant();
    }

    @Override
    protected void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetBroadcastReceiver();
        }
//        if (oSMSBroadcastReceiver == null) {
//            oSMSBroadcastReceiver = new SMSBroadcastReceiver();
//        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);

//        IntentFilter iff = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        iff.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        registerReceiver(oSMSBroadcastReceiver, iff);
        super.onResume();
        if (smsContentObserver != null) {
            getContentResolver().registerContentObserver(
                    Uri.parse("content://sms/"), true, smsContentObserver);// 注册监听短信数据库的变化
        }
    }

    private void trySignin() {
        try {
            ClientInfo clientInfo = userDao.findUser();
            if (clientInfo != null && !StringUtil.isEmpty(clientInfo.getUserID()) && !StringUtil.isEmpty(clientInfo.getPassword())) {
                gongXingController.signin(clientInfo.getUserID(), clientInfo.getPassword());
            }
        } catch (DbException e) {
            Log.e(e.getMessage(), e.getLocalizedMessage());
            Toast.makeText(x.app(), "获取本地缓存用户信息失败，所以无法登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(netWorkStateReceiver);
//        unregisterReceiver(oSMSBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (smsContentObserver != null) {
            getContentResolver().unregisterContentObserver(smsContentObserver);// 取消监听短信数据库的变化
        }

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
            case RegisterEvent.Next:
                vpager.setCurrentItem(PAGE_TWO2);
                break;
            case RegisterEvent.Begin_Register:
                ClientInfo clientInfo = mAdapter.getClientInfo();
                gongXingController.register(clientInfo);
                break;
            case RegisterEvent.Begin_DentifyingCode:
                gongXingController.getDentifyingCode((String) event.getData());
                break;
            case RegisterEvent.Register:
                break;
            case RegisterEvent.DentifyingCode:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
//        getApplication().startService(new Intent(getApplicationContext(), MQTTService.class));
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

    /**
     * 网络变化之后的类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetChange(NetworkChangeEvent event) {
        Boolean hasNet = isNetConnect((Integer) event.getData());
        if (hasNet) {
            trySignin();
        }
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect(int netMobile) {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }
}

