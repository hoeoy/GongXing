package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的验证码倒计时
 */
@lombok.Data
@NoArgsConstructor
public class RegisterTimerEvent {

    private String type;
    private Object data;

    public RegisterTimerEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
