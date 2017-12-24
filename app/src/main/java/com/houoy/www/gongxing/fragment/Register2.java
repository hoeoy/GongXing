package com.houoy.www.gongxing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.element.ClearEditText;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;
import com.houoy.www.gongxing.vo.RequestVO;
import com.houoy.www.gongxing.vo.ResultVO;

import org.eclipse.paho.client.mqttv3.util.Strings;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jay on 2015/8/28 0028.
 */
@ContentView(R.layout.register2_content)
public class Register2 extends Fragment {
    private boolean injected = false;

    @ViewInject(R.id.etxtPhone)
    private ClearEditText etxtPhone;
    @ViewInject(R.id.etxtPwd)
    private ClearEditText etxtPwd;
    @ViewInject(R.id.etxtPwd2)
    private ClearEditText etxtPwd2;

    public Register2() {
    }

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
    }

    @Event(value = {R.id.btnSure})
    private void onRegisterClick(View view) {
        //识别码验证
        String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/CheckProjectID";
        Map<String, String> params = new HashMap();
        params.put("IDENTIFYINGCODE", "zhangsan1");
        params.put("IDCode", "ceshihuanjing");
        params.put("PhoneNum", "15811111111");
        final RequestVO requestVO = new RequestVO("dff687bbfd840d3484e2091b09c8c424", params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                if (resultVO.getCode().equals("success")) {
                    //注册
                    String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/Register";
                    Map<String, String> params = new HashMap();
                    params.put("UserID", "zhangsan1");
                    params.put("Password", "p123456");
                    params.put("IDCode", "ceshihuanjing");
                    params.put("PhoneNum", "15811111111");
                    params.put("openid", "oSnZ8w5YmoNfZM4Fpix1gYLvGigs");
                    params.put("verification", "1234");//手机验证码
                    RequestVO requestVO = new RequestVO("dff687bbfd840d3484e2091b09c8c424", params);
                    String paramStr = JSON.toJSONString(requestVO);

                    XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                            if (resultVO.getCode().equals("success")) {

                            } else {
                                Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Event(value = {R.id.btnDentifyingCode})
    private void onbtnDentifyingCodeClick(View view) {
        String mobile = etxtPhone.getText().toString();
        //输入验证
        if (Strings.isEmpty(mobile) || mobile.length() < 11 || mobile.length() > 11) {
            Toast.makeText(view.getContext(), "请输入正确手机号", Toast.LENGTH_LONG).show();
        } else {
            //获取验证码
            String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/PhoneDentifyingCode";
            Map<String, String> params = new HashMap();
            params.put("mobile", mobile);
            RequestVO requestVO = new RequestVO("dff687bbfd840d3484e2091b09c8c424", params);
            String paramStr = JSON.toJSONString(requestVO);

            XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                    if (resultVO.getCode().equals("success")) {

                    } else {
                        Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}



