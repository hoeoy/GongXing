package com.houoy.www.gongxing.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统消息接收vo
 * Created by andyzhao on 2018/1/1.
 */
@Data
@NoArgsConstructor
public class MessageVO implements Serializable {
    private int id;
    private String RelationID;//消息id
    private String touser;//接收信息用户在微信端的唯一标识
    private String title_value;//推送消息标题
    private String title_color;//标题颜色，默认为#000000
    private String remark_value;//备注
    private String remark_color;//备注颜色，默认为#000000
    private String time;//消息到达日期
    private Integer type;//消息类型 1 日报， 2 报警

    //报警类型属性
    private String rule_name_value;//规则名称
    private String rule_name_color;//规则名称颜色，默认为#000000
    private String trigger_time_value;//触发时间，值格式自定
    private String trigger_time_color;//触发时间颜色，默认为#000000
    private String device_name_value;//设备名称
    private String device_name_color;//设备名称颜色，默认为#000000
    private String subkey_name_value;//子项名称
    private String subkey_name_color;//子项名称颜色，默认为#000000
    private String current_parameter_value;//当前参数，值格式自定
    private String current_parameter_color;//当前参数颜色，默认为#000000

    //以下是日报类型消息的属性
    private String temperature_value;//当前温度
    private String temperature_color;//当前温度颜色，默认为#000000
    private String humidity_value;//当前湿度
    private String humidity_color;//当前湿度颜色，默认为#000000
    private String state_value;//空调状态
    private String state_color;//空调状态色，默认为#000000
    private String alarm_num_value;//今日告警
    private String alarm_num_color;//今日告警颜色，默认为#000000
}
