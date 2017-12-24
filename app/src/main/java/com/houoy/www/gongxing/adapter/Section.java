package com.houoy.www.gongxing.adapter;

/**
 * Created by bpncool on 2/23/2016.
 */
public class Section {

    private String name;

    public boolean isExpanded;

    public Section(String name) {
        this.name = name;
        isExpanded = true;
    }

    public String getName() {
        return name;
    }
}
