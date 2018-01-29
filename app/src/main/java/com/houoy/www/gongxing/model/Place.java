package com.houoy.www.gongxing.model;

import java.io.Serializable;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class Place implements Serializable {
    private String PlaceName;
    private String PlaceId;

    private String AlarmNum;
    private String BackColor;
    private String FontColor;
    private String Time;
    private String Remark;
    private List<DeviceInfo> DeviceInfo;

    //前端冗余字段
    public Boolean isExpanded = false;
}
