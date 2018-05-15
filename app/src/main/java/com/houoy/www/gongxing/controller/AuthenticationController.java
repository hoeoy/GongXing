package com.houoy.www.gongxing.controller;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.GongXingApplication;
import com.houoy.www.gongxing.event.ForgetPWEvent;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;
import com.houoy.www.gongxing.vo.RequestVO;
import com.houoy.www.gongxing.vo.ResultVO;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册和登录等鉴权相关接口
 * Created by andyzhao on 1/14/2018.
 */
public class AuthenticationController {

    private static AuthenticationController authenticationController = null;

    private String defaultUrl = "http://app.51jfjk.com:9017/CloudAPPServer/";

    private AuthenticationController() {

    }

    public static AuthenticationController getInstant() {
        if (authenticationController == null) {
            authenticationController = new AuthenticationController();
        }
        return authenticationController;
    }

    public void getPhoneDentifyingCode(String mobile) {
        String url = defaultUrl + "/APPPHONEDENTIFYINGCODE";
        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new ForgetPWEvent(ForgetPWEvent.PhoneDentifyingCode, resultVO.getData()));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getVoiceDentifyingCode(String mobile) {
        String url = defaultUrl + "/APPVOICEDENTIFYINGCODE";
        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new ForgetPWEvent(ForgetPWEvent.VoiceDentifyingCode, resultVO.getData()));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void resetPW(final ClientInfo clientInfo) {
        //注册
        String url = defaultUrl + "APPRESETPASSWORDE";
        Map<String, String> params = new HashMap();
        params.put("userid", clientInfo.getUserID());
        params.put("PhoneNum", clientInfo.getPhoneNum());
        params.put("newPassword", clientInfo.getPassword());
        params.put("Verification", clientInfo.getVerification());//手机验证码
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new ForgetPWEvent(ForgetPWEvent.ResetPWSuccess, resultVO.getData()));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new ForgetPWEvent(ForgetPWEvent.ResetPWFail, null));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                EventBus.getDefault().post(new ForgetPWEvent(ForgetPWEvent.ResetPWFail, null));
            }
        });
    }
}
