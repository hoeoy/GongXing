package com.houoy.www.gongxing.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by andyzhao on 2017/12/23.
 */

public class DataPart implements Serializable {
    private List<Place> Place;

    public List<Place> getPlace() {
        return Place;
    }

    public void setPlace(List<Place> place) {
        Place = place;
    }
}
