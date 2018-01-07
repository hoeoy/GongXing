package com.houoy.www.gongxing.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日报消息
 * Created by andyzhao on 2018/1/1.
 */
@Data
@NoArgsConstructor
public class MessageDailyPush implements Serializable {
    private String touser;//接收信息用户在微信端的唯一标识
    private String title_value;//推送消息标题
    private String title_color;//标题颜色，默认为#000000
    private String temperature_value;//当前温度
    private String temperature_color;//当前温度颜色，默认为#000000
    private String humidity_value;//当前湿度
    private String humidity_color;//当前湿度颜色，默认为#000000
    private String state_value;//空调状态
    private String state_color;//空调状态色，默认为#000000
    private String alarm_num_value;//今日告警
    private String alarm_num_color;//今日告警颜色，默认为#000000
    private String remark_value;//备注
    private String remark_color;//备注颜色，默认为#000000
    private String RelationID;//消息id
    private String date;//消息到达日期
}
