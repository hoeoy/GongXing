package com.houoy.www.gongxing.vo;

import java.util.Map;

/**
 * Created by andyzhao on 2017/12/24.
 */

public class RequestVO {
    private String sign;
    private Map<String, String> params;

    public RequestVO(){

    }

    public RequestVO(String sign, Map params) {
        this.sign = sign;
        this.params = params;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
