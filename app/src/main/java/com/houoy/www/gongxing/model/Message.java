package com.houoy.www.gongxing.model;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 1/25/2018.
 */
@lombok.Data
@NoArgsConstructor
public class Message {
    private String sign;
    private MessagePush Params;
}
