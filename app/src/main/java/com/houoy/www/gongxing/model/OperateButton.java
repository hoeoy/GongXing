package com.houoy.www.gongxing.model;

import java.io.Serializable;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * 报警类和日报类消息的操作按钮
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class OperateButton implements Serializable {
    private String OperateName;//确认收到或已确认  按钮名称
    private String OperateTypeID;//1或2   操作类型（1：可点击 2：不可点击）
}
