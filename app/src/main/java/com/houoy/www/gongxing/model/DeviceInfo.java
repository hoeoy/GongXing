package com.houoy.www.gongxing.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by andyzhao on 2017/12/23.
 */

public class DeviceInfo implements Serializable {
    private String DeviceName;
    private String BackColor;
    private String FontColor;
    private List<ParaInfo> ParaInfo;

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getBackColor() {
        return BackColor;
    }

    public void setBackColor(String backColor) {
        BackColor = backColor;
    }

    public String getFontColor() {
        return FontColor;
    }

    public void setFontColor(String fontColor) {
        FontColor = fontColor;
    }

    public List<ParaInfo> getParaInfo() {
        return ParaInfo;
    }

    public void setParaInfo(List<ParaInfo> paraInfo) {
        ParaInfo = paraInfo;
    }
}
