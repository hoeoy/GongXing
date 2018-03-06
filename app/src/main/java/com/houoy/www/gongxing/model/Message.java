package com.houoy.www.gongxing.model;

import com.houoy.www.gongxing.vo.MessageVO;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 1/25/2018.
 */
@lombok.Data
@NoArgsConstructor
public class Message {
    private String sign;
    private MessageVO Params;
}
