package com.houoy.www.gongxing.adapter;

/**
 * Created by bpncool on 2/24/2016.
 */
/**
 * interface to listen changes in state of sections
 */
public interface SectionStateChangeListener {
    void onSectionStateChanged(Section section, boolean isOpen);
}