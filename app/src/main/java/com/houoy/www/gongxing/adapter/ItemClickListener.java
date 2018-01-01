package com.houoy.www.gongxing.adapter;

import com.houoy.www.gongxing.model.DeviceInfo;

/**
 * Created by lenovo on 2/23/2016.
 */
public interface ItemClickListener {
    void itemClicked(DeviceInfo deviceInfo);

    void itemClicked(Section section);
}
