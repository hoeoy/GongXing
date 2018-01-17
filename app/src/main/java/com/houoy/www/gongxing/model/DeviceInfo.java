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
    private List<ParaInfo> ParaInfo;
}
