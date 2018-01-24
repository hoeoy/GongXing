package com.houoy.www.gongxing.adapter;

import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.model.OperatePart;
import com.houoy.www.gongxing.model.RemarkPart;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 1/18/2018.
 */
@lombok.Data
@NoArgsConstructor
public class SectiondFooter {
    private OperatePart operatePart;
    private RemarkPart remarkPart;
    private ClientInfo clientInfo;

    private String type;//1 查询  2 报警  3 日报
}
