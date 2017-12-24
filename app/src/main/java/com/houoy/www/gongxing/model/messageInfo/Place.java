package com.houoy.www.gongxing.model.messageInfo;

import java.util.List;

/**
 * Created by andyzhao on 2017/12/23.
 */

public class Place {
    private String PlaceName;
    private String PlaceId;
    private List<DeviceInfo> DeviceInfo;

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public List<DeviceInfo> getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(List<DeviceInfo> deviceInfo) {
        DeviceInfo = deviceInfo;
    }
}
