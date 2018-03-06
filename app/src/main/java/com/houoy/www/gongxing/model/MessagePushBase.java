package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统消息公共属性
 * Created by andyzhao on 2018/1/1.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class MessagePushBase extends ChatMessage implements Serializable {
    //公共属性
//    @Column(name = "id", isId = true)
//    private int id;
    @Column(name = "RelationID")
    private String RelationID;//消息id
    @Column(name = "touser")
    private String touser;//接收信息用户在微信端的唯一标识
    @Column(name = "title_value")
    private String title_value;//推送消息标题
    @Column(name = "title_color")
    private String title_color;//标题颜色，默认为#000000
    @Column(name = "remark_value")
    private String remark_value;//备注
    @Column(name = "remark_color")
    private String remark_color;//备注颜色，默认为#000000
    @Column(name = "time")
    private String time;//消息到达日期
    @Column(name = "type")
    private String type;//消息类型 1 日报， 2 报警
}
