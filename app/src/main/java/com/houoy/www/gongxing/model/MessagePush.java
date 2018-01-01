package com.houoy.www.gongxing.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2018/1/1.
 */
@Data
@NoArgsConstructor
public class MessagePush {
    private String touser;//接收信息用户在微信端的唯一标识
    private String title_value;//推送消息标题
    private String title_color;//标题颜色，默认为#000000
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
    private String remark_value;//备注
    private String remark_color;//备注颜色，默认为#000000
    private String RelationID;//消息id


}
