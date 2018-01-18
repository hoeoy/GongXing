package com.houoy.www.gongxing.model;

import java.io.Serializable;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class DeviceInfo implements Serializable {
    private String DeviceName;
    private String BackColor;
    private String FontColor;
    private String State;//正常 or 失败
    private List<ParaInfo> ParaInfo;
}
