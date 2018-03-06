package com.houoy.www.gongxing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.element.ClearEditText;
import com.houoy.www.gongxing.event.RegisterEvent;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Jay on 2015/8/28 0028.
 */
@ContentView(R.layout.register_content)
public class Register extends Fragment {
    private boolean injected = false;

    @ViewInject(R.id.userid)
    private ClearEditText userid;
    @ViewInject(R.id.password)
    private ClearEditText password;
    @ViewInject(R.id.password2)
    private ClearEditText password2;

    public Register() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架
        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Event(value = {R.id.btnNext})
    private void onNextClick(View view) {
        String p = password.getText().toString();
        String p2 = password2.getText().toString();
        if (StringUtil.isEmpty(p)) {
            Toast.makeText(view.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(p2)) {
            Toast.makeText(view.getContext(), "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (p.equals(p2)) {
            EventBus.getDefault().post(new RegisterEvent(RegisterEvent.Next, "a"));
        } else {
            Toast.makeText(view.getContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
        }
    }

    public ClientInfo getInputClientInfo() {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUserID(userid.getText().toString());
        clientInfo.setPassword(password.getText().toString());
        return clientInfo;
    }

    public String getUserid() {
        return userid.getText().toString();
    }

    public String getPassword() {
        return password.getText().toString();
    }
}
