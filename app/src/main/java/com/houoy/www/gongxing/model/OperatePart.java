package com.houoy.www.gongxing.model;

import java.io.Serializable;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * 报警类和日报类消息的操作按钮数组
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
public class OperatePart implements Serializable {
    private List<OperateButton> OperateButton;
}
