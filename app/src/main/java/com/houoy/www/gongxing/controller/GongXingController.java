package com.houoy.www.gongxing.controller;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.houoy.www.gongxing.GongXingApplication;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.event.AffirmOperateEvent;
import com.houoy.www.gongxing.event.GoToSigninEvent;
import com.houoy.www.gongxing.event.LoginEvent;
import com.houoy.www.gongxing.event.LogoutEvent;
import com.houoy.www.gongxing.event.RegisterEvent;
import com.houoy.www.gongxing.event.SearchDailyMessageDataEvent;
import com.houoy.www.gongxing.event.SearchMessageDataEvent;
import com.houoy.www.gongxing.event.SearchWarningMessageDataEvent;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.model.Data;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;
import com.houoy.www.gongxing.vo.MessageVO;
import com.houoy.www.gongxing.vo.RequestVO;
import com.houoy.www.gongxing.vo.ResultVO;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用接口的Congroller
 * Created by andyzhao on 1/14/2018.
 */
public class GongXingController {

    private static GongXingController gongXingController = null;
    private UserDao userDao;

    private GongXingController() {
        userDao = UserDao.getInstant();
    }

    public static GongXingController getInstant() {
        if (gongXingController == null) {
            gongXingController = new GongXingController();
        }
        return gongXingController;
    }

    public void signin(final String userid, final String password) {
        String url = GongXingApplication.url + "CloudWeChatPlatServer/AppLogin";
        Map<String, String> params = new HashMap();
        params.put("UserID", userid);
        params.put("Password", password);
        final RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO<ClientInfo> resultVO = JSON.parseObject(result, new TypeReference<ResultVO<ClientInfo>>() {
                });
                if (resultVO.getCode().equals("success")) {
                    try {
                        ClientInfo clientInfo = resultVO.getData();
                        clientInfo.setPassword(password);
                        clientInfo.setIDCode(clientInfo.getIDENTIFYINGCODE());
                        clientInfo.setOpenid(clientInfo.getWeChatID());
                        userDao.setUser(clientInfo);
                        EventBus.getDefault().post(new LoginEvent("login", resultVO));
                    } catch (DbException e) {
                        Log.e(e.getMessage(), e.getLocalizedMessage());
                        Toast.makeText(x.app(), "本地缓存用户信息失败，所以无法登录", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new GoToSigninEvent(null, null));
                    }
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new GoToSigninEvent(null, null));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                EventBus.getDefault().post(new GoToSigninEvent(null, null));
            }
        });
    }

    public void logout() {
        //登出
        String url = GongXingApplication.url + "CloudWeChatPlatServer/APPLogout";
        try {
            ClientInfo clientInfo = userDao.findUser();
            Map<String, String> params = new HashMap();
            params.put("UserID", clientInfo.getUserID());
            params.put("openid", clientInfo.getOpenid());
            RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
            String paramStr = JSON.toJSONString(requestVO);

            XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                    if (resultVO.getCode().equals("success")) {
                        try {
                            userDao.clearUser();
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
        String url = GongXingApplication.url + "CloudWeChatPlatServer/APPPhoneDentifyingCode";
        Map<String, String> params = new HashMap();
        params.put("mobile", mobile);
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
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
//        String url = GongXingApplication.url + "CloudWeChatPlatServer/CheckProjectID";
//        Map<String, String> params = new HashMap();
//        params.put("IDENTIFYINGCODE", clientInfo.getVerification());
//        params.put("IDCode", clientInfo.getIDCode());
//        params.put("PhoneNum", clientInfo.getPhoneNum());
//        final RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
//        String paramStr = JSON.toJSONString(requestVO);
//
//        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
//            @Override
//            public void onSuccess(String result) {
//                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
//                if (resultVO.getCode().equals("success")) {
//                    //注册
//                    String url = GongXingApplication.url + "CloudWeChatPlatServer/Register";
//                    Map<String, String> params = new HashMap();
//                    params.put("UserID", clientInfo.getUserID());
//                    params.put("Password", clientInfo.getPassword());
//                    params.put("IDCode", clientInfo.getIDCode());
//                    params.put("PhoneNum", clientInfo.getPhoneNum());
//                    params.put("openid", clientInfo.getOpenid());
//                    params.put("verification", clientInfo.getVerification());//手机验证码
//                    RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
//                    String paramStr = JSON.toJSONString(requestVO);
//
//                    XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
//                        @Override
//                        public void onSuccess(String result) {
//                            ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
//                            if (resultVO.getCode().equals("success")) {
//                                EventBus.getDefault().post(new RegisterEvent(RegisterEvent.Register, resultVO.getData()));
//                            } else {
//                                Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                } else {
//                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        //注册
        String url = GongXingApplication.url + "CloudWeChatPlatServer/APPRegister";
        Map<String, String> params = new HashMap();
        params.put("UserID", clientInfo.getUserID());
        params.put("Password", clientInfo.getPassword());
        params.put("IDCode", clientInfo.getIDCode());
        params.put("PhoneNum", clientInfo.getPhoneNum());
        params.put("openid", "");
        params.put("verification", clientInfo.getVerification());//手机验证码
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
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

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    //查询消息
    public void queryData() throws DbException {
        String url = GongXingApplication.url + "/CloudWeChatPlatServer/QueryData";
        Map<String, String> params = new HashMap();
        ClientInfo clientInfo = userDao.findUser();
        params.put("touser", clientInfo.getOpenid());
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO<Data> resultVO = JSON.parseObject(result, new TypeReference<ResultVO<Data>>() {
                });
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new SearchMessageDataEvent("data", resultVO.getData()));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //查询报警消息详细
    public void queryWarningData(MessageVO messageVO) throws DbException {
        String url = GongXingApplication.url + "/CloudWeChatPlatServer/MessageDetail";
        Map<String, String> params = new HashMap();
        params.put("touser", messageVO.getTouser());
        params.put("RelationID", messageVO.getRelationID());
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO<Data> resultVO = JSON.parseObject(result, new TypeReference<ResultVO<Data>>() {
                });
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new SearchWarningMessageDataEvent("data", resultVO.getData()));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }

//                Data data = MockData.getPushData();
//                EventBus.getDefault().post(new SearchWarningMessageDataEvent("data", data));
            }
        });
    }

    //查询日报消息详细
    public void queryDailyData(MessageVO messageVO) throws DbException {
        String url = GongXingApplication.url + "/CloudWeChatPlatServer/MessageDetail";
        Map<String, String> params = new HashMap();
        params.put("touser", messageVO.getTouser());
        params.put("RelationID", messageVO.getRelationID());
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO<Data> resultVO = JSON.parseObject(result, new TypeReference<ResultVO<Data>>() {
                });
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new SearchDailyMessageDataEvent("data", resultVO.getData()));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }

//                Data data = MockData.getPushData();
//                EventBus.getDefault().post(new SearchDailyMessageDataEvent("data", data));
            }
        });
    }

    //确认报警
    public void affirmOperate(ClientInfo clientInfo) throws DbException {
        String url = GongXingApplication.url + "/CloudWeChatPlatServer/AffirmOperate";
        Map<String, String> params = new HashMap();
        ClientInfo user = userDao.findUser();
        params.put("touser", user.getOpenid());
        params.put("RelationID", clientInfo.getRelationID());
        RequestVO requestVO = new RequestVO(GongXingApplication.sign, params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO<Data> resultVO = JSON.parseObject(result, new TypeReference<ResultVO<Data>>() {
                });
                if (resultVO.getCode().equals("success")) {
                    EventBus.getDefault().post(new AffirmOperateEvent("data", ""));
                } else {
                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
