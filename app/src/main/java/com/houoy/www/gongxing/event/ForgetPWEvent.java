package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class ForgetPWEvent {
    public static final String PhoneDentifyingCode = "PhoneDentifyingCode";
    public static final String VoiceDentifyingCode = "VoiceDentifyingCode";
    //修改密码成功
    public static final String ResetPWSuccess = "ResetPWSuccess";
    //修改密码失败
    public static final String ResetPWFail = "ResetPWFail";
    //正在请求后台修改密码
    public static final String OnResetPW = "OnResetPW";

    private String type;
    private Object data;

    public ForgetPWEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
