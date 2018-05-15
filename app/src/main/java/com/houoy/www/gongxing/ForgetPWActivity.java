package com.houoy.www.gongxing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.houoy.www.gongxing.controller.AuthenticationController;
import com.houoy.www.gongxing.dao.AuthenticationDao;
import com.houoy.www.gongxing.element.ClearEditText;
import com.houoy.www.gongxing.event.ForgetPWEvent;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;

@ContentView(R.layout.activity_forget_pw)
public class ForgetPWActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private AuthenticationDao authenticationDao;

    @ViewInject(R.id.forget_userid)
    private ClearEditText forget_userid;
    @ViewInject(R.id.forget_phone)
    private ClearEditText forget_phone;

    @ViewInject(R.id.forgetBtn)
    private Button forgetBtn;
    @ViewInject(R.id.forget_again)
    private ClearEditText forget_again;
    @ViewInject(R.id.forget_new)
    private ClearEditText forget_new;
    @ViewInject(R.id.forget_check)
    private ClearEditText forget_check;

    @ViewInject(R.id.ForgetLastTimeSound)
    private TextView ForgetLastTimeSound;
    @ViewInject(R.id.ForgetLastTime)
    private TextView ForgetLastTime;

    @ViewInject(R.id.btnForgetCheckSoundId)
    private Button btnForgetCheckSoundId;
    @ViewInject(R.id.btnForgetCheckId)
    private Button btnForgetCheckId;

    private AuthenticationController authenticationController;

    public long timer = 0;
    public Handler mHandler = new Handler();
    AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.forgetPW));

        authenticationController = AuthenticationController.getInstant();
        authenticationDao = AuthenticationDao.getInstant();

        long diffNow = 0;
        try {
            diffNow = authenticationDao.diffNow();
        } catch (DbException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (diffNow > 0 && diffNow < 60 * 1000) {
            //还在一个计时周期中
            timer = diffNow;
            changeState(true);
            countTimer();
        }
    }

    private void changeState(boolean hide) {
        if (hide) {
            ForgetLastTime.setVisibility(View.VISIBLE);
            ForgetLastTimeSound.setVisibility(View.VISIBLE);
            btnForgetCheckId.setVisibility(View.GONE);
            btnForgetCheckSoundId.setVisibility(View.GONE);
            ForgetLastTime.setText("获取验证码" + (60 - timer / 1000) + "秒");
            ForgetLastTimeSound.setText("获取语音验证码" + (60 - timer / 1000) + "秒");
        } else {
            ForgetLastTime.setVisibility(View.GONE);
            ForgetLastTimeSound.setVisibility(View.GONE);
            btnForgetCheckId.setVisibility(View.VISIBLE);
            btnForgetCheckSoundId.setVisibility(View.VISIBLE);
        }
    }

    private void checkDentifyCode(String phone) {
        if (StringUtil.isEmpty(phone) || !StringUtil.isCellphone(phone)) {
            Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        timer = 0;
        changeState(true);
        countTimer();//开始计时
        try {
            authenticationDao.refresh();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.btnForgetCheckId})
    private void onBtnForgetCheckIdClick(View view) {
        String phone = forget_phone.getText().toString();
        checkDentifyCode(phone);
        authenticationController.getPhoneDentifyingCode(phone);
    }

    @Event(value = {R.id.btnForgetCheckSoundId})
    private void onBtnForgetCheckSoundIdClick(View view) {
        String phone = forget_phone.getText().toString();
        checkDentifyCode(phone);
        authenticationController.getVoiceDentifyingCode(phone);
    }

    @Event(value = {R.id.forgetBtn})
    private void onForgetBtnClick(View view) {
        String userid = forget_userid.getText().toString();
        if (StringUtil.isEmpty(userid)) {
            Toast.makeText(view.getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String check = forget_check.getText().toString();
        if (StringUtil.isEmpty(check)) {
            Toast.makeText(view.getContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = forget_phone.getText().toString();
        if (StringUtil.isEmpty(phone) || !StringUtil.isCellphone(phone)) {
            Toast.makeText(view.getContext(), "请输入正确手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String pw = forget_new.getText().toString();
        String cpw = forget_again.getText().toString();
        if (StringUtil.isEmpty(pw) || StringUtil.isEmpty(cpw)) {
            Toast.makeText(view.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pw.equals(cpw)) {
            Toast.makeText(view.getContext(), "两次输入密码不同", Toast.LENGTH_SHORT).show();
            return;
        }

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUserID(userid);
        clientInfo.setVerification(check);
        clientInfo.setPhoneNum(phone);
        clientInfo.setPassword(pw);
        authenticationController.resetPW(clientInfo);
        showMsgDialog("正在重置密码...", null, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleConnect(ForgetPWEvent event) {
        //调用获取验证码服务成功
        switch (event.getType()) {
            case ForgetPWEvent.ResetPWSuccess:
                //修改密码成功,返回登录页面
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                finish();
                break;
            case ForgetPWEvent.ResetPWFail:
                //修改密码失败
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                break;
            case ForgetPWEvent.OnResetPW:
                //正在修改密码

                break;
            case ForgetPWEvent.PhoneDentifyingCode:

                break;
            case ForgetPWEvent.VoiceDentifyingCode:

                break;
        }
    }

    private Runnable TimerRunnable = new Runnable() {
        @Override
        public void run() {
            timer += 1000;
            if (timer > 60 * 1000) {//倒计时结束
                timer = 0;
                changeState(false);
            } else {
                ForgetLastTime.setText("获取验证码" + (60 - timer / 1000) + "秒");
                ForgetLastTimeSound.setText("获取语音验证码" + (60 - timer / 1000) + "秒");
                countTimer();//下一次计时
            }
        }
    };

    public void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mHandler.removeCallbacks(TimerRunnable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //显示基本的AlertDialog
    public AlertDialog showMsgDialog(String message, DialogInterface.OnClickListener sure, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统提示");
        builder.setMessage(message);
        if (sure != null) {
            builder.setPositiveButton("确定", sure);
        }
        if (cancel != null) {
            builder.setNegativeButton("取消", cancel);
        }
        dialog = builder.show();
        return dialog;
    }
}
