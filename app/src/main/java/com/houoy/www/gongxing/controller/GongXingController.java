package com.houoy.www.gongxing.controller;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.dao.GongXingDao;
import com.houoy.www.gongxing.event.LoginEvent;
import com.houoy.www.gongxing.event.LogoutEvent;
import com.houoy.www.gongxing.event.RegisterEvent;
import com.houoy.www.gongxing.event.SearchMessageDataEvent;
import com.houoy.www.gongxing.mock.MockData;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.model.Data;
import com.houoy.www.gongxing.util.Constants;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;
import com.houoy.www.gongxing.vo.RequestVO;
import com.houoy.www.gongxing.vo.ResultVO;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andyzhao on 1/14/2018.
 */

public class GongXingController {

    private static GongXingController gongXingController = null;
    private GongXingDao gongXingDao;

    private GongXingController() {
        gongXingDao = GongXingDao.getInstant();
    }

    public static GongXingController getInstant() {
        if (gongXingController == null) {
            gongXingController = new GongXingController();
        }
        return gongXingController;
    }

    public void signin(final String userid, final String password) {
        String url = Constants.url + "CloudWeChatPlatServer/Login";
        Map<String, String> params = new HashMap();
        params.put("UserID", userid);
        params.put("Password", password);
        RequestVO requestVO = new RequestVO(Constants.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                if (resultVO.getCode().equals("success")) {
                    try {
                        ClientInfo clientInfo = new ClientInfo();
                        clientInfo.setUserID(userid);
                        clientInfo.setPassword(password);
                        gongXingDao.setUser(clientInfo);
                        EventBus.getDefault().post(new LoginEvent("login", resultVO));
                    } catch (DbException e) {
                        Log.e(e.getMessage(), e.getLocalizedMessage());
                        Toast.makeText(x.app(), "本地缓存用户信息失败，所以无法登录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logout() {
        //登出
        String url = Constants.url + "CloudWeChatPlatServer/Logout";
        try {
            ClientInfo clientInfo = gongXingDao.findUser();
            Map<String, String> params = new HashMap();
            params.put("UserID", clientInfo.getUserID());
            params.put("openid", clientInfo.getOpenid());
            RequestVO requestVO = new RequestVO(Constants.sign, params);
            String paramStr = JSON.toJSONString(requestVO);

            XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                    if (resultVO.getCode().equals("success")) {
                        try {
                            gongXingDao.clearUser();
                        } catch (DbException e) {
                            Log.e(e.getMessage(), e.getLocalizedMessage());
                            Toast.makeText(x.app(), "删除本地缓存用户信息失败", Toast.LENGTH_SHORT).show();
                        }
                        EventBus.getDefault().post(new LogoutEvent("logout", resultVO));
                    } else {
                        Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (DbException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
        }
    }

    public void getDentifyingCode(String mobile) {
        //获取验证码
        String url = Constants.url + "CloudWeChatPlatServer/PhoneDentifyingCode";
        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        RequestVO requestVO = new RequestVO(Constants.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new RegisterEvent(RegisterEvent.DentifyingCode, resultVO.getData()));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void register(final ClientInfo clientInfo) {
        //识别码验证
        String url = Constants.url + "CloudWeChatPlatServer/CheckProjectID";
        Map<String, String> params = new HashMap();
        params.put("IDENTIFYINGCODE", clientInfo.getVerification());
        params.put("IDCode", clientInfo.getIDCode());
        params.put("PhoneNum", clientInfo.getPhoneNum());
        final RequestVO requestVO = new RequestVO(Constants.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                if (resultVO.getCode().equals("success")) {
                    //注册
                    String url = Constants.url + "CloudWeChatPlatServer/Register";
                    Map<String, String> params = new HashMap();
                    params.put("UserID", clientInfo.getUserID());
                    params.put("Password", clientInfo.getPassword());
                    params.put("IDCode", clientInfo.getIDCode());
                    params.put("PhoneNum", clientInfo.getPhoneNum());
                    params.put("openid", clientInfo.getOpenid());
                    params.put("verification", clientInfo.getVerification());//手机验证码
                    RequestVO requestVO = new RequestVO(Constants.sign, params);
                    String paramStr = JSON.toJSONString(requestVO);

                    XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                            if (resultVO.getCode().equals("success")) {
                                EventBus.getDefault().post(new RegisterEvent(RegisterEvent.Register, resultVO.getData()));
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

    public void queryData() {
        String url = Constants.url + "/CloudWeChatPlatServer/QueryData";
        Map<String, String> params = new HashMap();
        params.put("touser", "zhaozhao");
        RequestVO requestVO = new RequestVO(Constants.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
//                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
//                if (resultVO.getCode().equals("success")) {
//                    EventBus.getDefault().post(new SearchMessageDataEvent("data", resultVO.getData()));
//                } else {
//                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
//                }

                //mock data
                Data data = MockData.getInfoData();
                EventBus.getDefault().post(new SearchMessageDataEvent("data", data));
            }
        });
    }

}
