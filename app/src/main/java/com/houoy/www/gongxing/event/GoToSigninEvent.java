package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * 返回到登陆页面
 */
@lombok.Data
@NoArgsConstructor
public class GoToSigninEvent {
    private String type;
    private Object data;

    public GoToSigninEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
