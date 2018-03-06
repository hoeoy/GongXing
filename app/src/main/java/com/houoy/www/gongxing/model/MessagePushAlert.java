package com.houoy.www.gongxing.model;

import com.houoy.www.gongxing.vo.MessageVO;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 告警消息
 * Created by andyzhao on 2018/1/1.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "message_alert")
public class MessagePushAlert extends MessagePushBase implements Serializable {
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

    public MessagePushAlert(MessageVO messageVO) {
        setRelationID(messageVO.getRelationID());//消息id
        setTouser(messageVO.getTouser());
        setTitle_value(messageVO.getTitle_value());
        setTitle_color(messageVO.getTitle_color());
        setRemark_value(messageVO.getRemark_value());
        setRemark_color(messageVO.getRemark_color());
        setTime(messageVO.getTime());
        setType(messageVO.getType());

        rule_name_value = messageVO.getRule_name_value();//规则名称
        rule_name_color = messageVO.getRule_name_color();//规则名称颜色，默认为#000000
        trigger_time_value = messageVO.getTrigger_time_value();//触发时间，值格式自定
        trigger_time_color = messageVO.getTrigger_time_color();//触发时间颜色，默认为#000000
        device_name_value = messageVO.getDevice_name_value();//设备名称
        device_name_color = messageVO.getDevice_name_color();//设备名称颜色，默认为#000000
        subkey_name_value = messageVO.getSubkey_name_value();//子项名称
        subkey_name_color = messageVO.getSubkey_name_color();//子项名称颜色，默认为#000000
        current_parameter_value = messageVO.getCurrent_parameter_value();//当前参数，值格式自定
        current_parameter_color = messageVO.getCurrent_parameter_color();//当前参数颜色，默认为#000000
    }
}
