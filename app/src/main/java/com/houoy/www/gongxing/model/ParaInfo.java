package com.houoy.www.gongxing.model;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class ParaInfo implements Serializable {
    private ParaName ParaName;
    private ParaValue ParaValue;
    private ParaState ParaState;
}
