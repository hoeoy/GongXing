package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 告警消息
 * Created by andyzhao on 2018/1/1.
 */
@Data
@NoArgsConstructor
@Table(name = "message_push")
public class MessagePush implements Serializable {
    //公共属性
    @Column(name = "id", isId = true)
    private int id;
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
    @Column(name = "RelationID")
    private String RelationID;//消息id
    @Column(name = "time")
    private String time;//消息到达日期
    @Column(name = "type")
    private String type;//消息类型 1 日报， 2 报警

    //报警类型属性
    @Column(name = "rule_name_value")
    private String rule_name_value;//规则名称
    @Column(name = "rule_name_color")
    private String rule_name_color;//规则名称颜色，默认为#000000
    @Column(name = "trigger_time_value")
    private String trigger_time_value;//触发时间，值格式自定
    @Column(name = "trigger_time_color")
    private String trigger_time_color;//触发时间颜色，默认为#000000
    @Column(name = "device_name_value")
    private String device_name_value;//设备名称
    @Column(name = "device_name_color")
    private String device_name_color;//设备名称颜色，默认为#000000
    @Column(name = "subkey_name_value")
    private String subkey_name_value;//子项名称
    @Column(name = "subkey_name_color")
    private String subkey_name_color;//子项名称颜色，默认为#000000
    @Column(name = "current_parameter_value")
    private String current_parameter_value;//当前参数，值格式自定
    @Column(name = "current_parameter_color")
    private String current_parameter_color;//当前参数颜色，默认为#000000


    //以下是日报类型消息的属性
    @Column(name = "temperature_value")
    private String temperature_value;//当前温度
    @Column(name = "temperature_color")
    private String temperature_color;//当前温度颜色，默认为#000000
    @Column(name = "humidity_value")
    private String humidity_value;//当前湿度
    @Column(name = "humidity_color")
    private String humidity_color;//当前湿度颜色，默认为#000000
    @Column(name = "state_value")
    private String state_value;//空调状态
    @Column(name = "state_color")
    private String state_color;//空调状态色，默认为#000000
    @Column(name = "alarm_num_value")
    private String alarm_num_value;//今日告警
    @Column(name = "alarm_num_color")
    private String alarm_num_color;//今日告警颜色，默认为#000000
}
