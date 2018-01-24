package com.houoy.www.gongxing.adapter;

import lombok.NoArgsConstructor;

/**
 * Created by bpncool on 2/23/2016.
 */
@lombok.Data
@NoArgsConstructor
public class Section {

    private String name;
    private String Time;
    private String state;
    public boolean isExpanded;

    public Section(String name) {
        this.name = name;
        isExpanded = true;
    }

    public Section(String name, String Time,String state) {
        this.name = name;
        this.Time = Time;
        this.state = state;
        isExpanded = true;
    }
}
