package com.houoy.www.gongxing.model;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class Data implements Serializable {
    private ClientInfo ClientInfo;
    private DataPart DataPart;
    private OperatePart operatePart;
    private RemarkPart remarkPart;
}
