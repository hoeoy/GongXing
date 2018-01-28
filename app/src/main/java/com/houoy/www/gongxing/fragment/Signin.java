package com.houoy.www.gongxing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.dao.MessagePushDao;
import com.houoy.www.gongxing.element.ClearEditText;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Jay on 2015/8/28 0028.
 */
@ContentView(R.layout.signin_content)
public class Signin extends Fragment {
    private boolean injected = false;

    @ViewInject(R.id.etxtEmail)
    private ClearEditText etxtEmail;
    @ViewInject(R.id.etxtPwd)
    private ClearEditText etxtPwd;
    private MessagePushDao messagePushDao;
    private GongXingController gongXingController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        messagePushDao = MessagePushDao.getInstant();
        gongXingController = GongXingController.getInstant();
    }

    /**
     * 1. 方法必须私有限定,
     * 2. 方法参数形式必须和type对应的Listener接口一致.
     * 3. 注解参数value支持数组: value={id1, id2, id3}
     * 4. 其它参数说明见{ org.xutils.event.annotation.Event}类的说明.
     * 长按事件 type = View.OnLongClickListener.class
     **/
    @Event(value = {R.id.btnLogin})
    private void onLoginClick(View view) {
        String userid = etxtEmail.getText().toString();
        String password = etxtPwd.getText().toString();
        if (StringUtil.isEmpty(userid)) {
            Toast.makeText(x.app(), "请输入用户名", Toast.LENGTH_LONG).show();
            return;
        }
        if (StringUtil.isEmpty(password)) {
            Toast.makeText(x.app(), "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }

        gongXingController.signin(userid, password);
    }
}
