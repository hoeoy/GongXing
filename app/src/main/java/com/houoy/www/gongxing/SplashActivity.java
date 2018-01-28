package com.houoy.www.gongxing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.event.GoToSigninEvent;
import com.houoy.www.gongxing.event.LoginEvent;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.x;

public class SplashActivity extends AppCompatActivity {

    private UserDao userDao;
    private GongXingController gongXingController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gongXingController = GongXingController.getInstant();
        userDao = UserDao.getInstant();

        /**
         * 延迟进入主界面
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientInfo clientInfo = userDao.findUser();
                    if (clientInfo != null && !StringUtil.isEmpty(clientInfo.getUserID()) && !StringUtil.isEmpty(clientInfo.getPassword())) {
                        gongXingController.signin(clientInfo.getUserID(), clientInfo.getPassword());
                    } else {
                        EventBus.getDefault().post(new GoToSigninEvent(null, null));
                    }
                } catch (DbException e) {
                    Log.e(e.getMessage(), e.getLocalizedMessage());
                    Toast.makeText(x.app(), "获取本地缓存用户信息失败，所以无法登录", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500 * 3);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToSigninEvent(GoToSigninEvent event) {
        Intent intent = new Intent(this, RegisterAndSignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
