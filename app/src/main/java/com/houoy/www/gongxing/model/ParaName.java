package com.houoy.www.gongxing.model;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class ParaName implements Serializable {
    private String Name;
    private String BackColor;
    private String FontColor;
}
