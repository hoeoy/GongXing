package com.houoy.www.gongxing.model;

import java.io.Serializable;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * 报警类和日报类消息的备注
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class RemarkPart implements Serializable {
    private String Remark;//如：请确认报警好即使处理相关故障   备注
}
