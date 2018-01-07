package com.houoy.www.gongxing.vo;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/24.
 */
@Data
@NoArgsConstructor
public class RequestVO implements Serializable {
    private String sign;
    private Map<String, String> params;

    public RequestVO(String sign, Map params) {
        this.sign = sign;
        this.params = params;
    }
}
