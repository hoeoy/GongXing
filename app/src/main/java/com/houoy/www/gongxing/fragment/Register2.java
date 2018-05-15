package com.houoy.www.gongxing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.RegisterAndSignInActivity;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.element.ClearEditText;
import com.houoy.www.gongxing.event.RegisterEvent;
import com.houoy.www.gongxing.event.RegisterTimerEvent;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.StringUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * Created by Jay on 2015/8/28 0028.
 */
@ContentView(R.layout.register2_content)
public class Register2 extends Fragment {
    private boolean injected = false;

    @ViewInject(R.id.etxtPhone)
    private ClearEditText etxtPhone;
    @ViewInject(R.id.dentifyingCode)
    private ClearEditText dentifyingCode;
    @ViewInject(R.id.lastTimeTextView)
    private TextView lastTimeTextView;
    @ViewInject(R.id.btnDentifyingCode)
    private TextView btnDentifyingCode;
    @ViewInject(R.id.idcode)
    private ClearEditText idcode;

    private GongXingController gongXingController;
    RegisterAndSignInActivity activity;

    public Register2() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架
//        EventBus.getDefault().register(this);
        gongXingController = GongXingController.getInstant();

        activity = (RegisterAndSignInActivity) inflater.getContext();
        if (activity.timer > 0) {
            lastTimeTextView.setVisibility(View.VISIBLE);
            btnDentifyingCode.setVisibility(View.GONE);
            lastTimeTextView.setText(60 - activity.timer / 1000 + "秒后重新获取");
        } else {
            lastTimeTextView.setVisibility(View.GONE);
            btnDentifyingCode.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleTImer(RegisterTimerEvent event) {
        Integer type = (Integer) event.getData();
        switch (type) {
            case 0:
                lastTimeTextView.setText(60 - activity.timer / 1000 + "秒后重新获取");
                break;
            case 1:
                lastTimeTextView.setVisibility(View.GONE);
                btnDentifyingCode.setVisibility(View.VISIBLE);
                break;
        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Event(value = {R.id.btnSure})
    private void onRegisterClick(View view) {
        String etxtPhoneStr = etxtPhone.getText().toString();
        String dentifyingCodeStr = dentifyingCode.getText().toString();
        String idcodeStr = idcode.getText().toString();

        if (StringUtil.isEmpty(etxtPhoneStr)) {
            Toast.makeText(view.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(dentifyingCodeStr)) {
            Toast.makeText(view.getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(idcodeStr)) {
            Toast.makeText(view.getContext(), "识别码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        EventBus.getDefault().post(new RegisterEvent(RegisterEvent.Begin_Register, ""));
    }

    @Event(value = {R.id.btnDentifyingCode})
    private void onbtnDentifyingCodeClick(View view) {
        String mobile = etxtPhone.getText().toString();
        //输入验证
        if (StringUtil.isEmpty(mobile) || !StringUtil.isCellphone(mobile)) {
            Toast.makeText(view.getContext(), "请输入正确手机号", Toast.LENGTH_LONG).show();
        } else {
            activity.timer = 0;
            lastTimeTextView.setVisibility(View.VISIBLE);
            btnDentifyingCode.setVisibility(View.GONE);
            activity.countTimer();//开始计时
            EventBus.getDefault().post(new RegisterEvent(RegisterEvent.Begin_DentifyingCode, mobile));
        }
    }

    public ClientInfo getInputClientInfo() {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setPhoneNum(etxtPhone.getText().toString());
        clientInfo.setVerification(dentifyingCode.getText().toString());
        clientInfo.setIDCode(idcode.getText().toString());
        return clientInfo;
    }

}



