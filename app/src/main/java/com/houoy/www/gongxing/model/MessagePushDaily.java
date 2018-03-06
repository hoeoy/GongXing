package com.houoy.www.gongxing.model;

import com.houoy.www.gongxing.vo.MessageVO;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 日报消息
 * Created by andyzhao on 2018/1/1.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "message_daily")
public class MessagePushDaily extends MessagePushBase implements Serializable {
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

    public MessagePushDaily(MessageVO messageVO) {
        setRelationID(messageVO.getRelationID());//消息id
        setTouser(messageVO.getTouser());
        setTitle_value(messageVO.getTitle_value());
        setTitle_color(messageVO.getTitle_color());
        setRemark_value(messageVO.getRemark_value());
        setRemark_color(messageVO.getRemark_color());
        setTime(messageVO.getTime());
        setType(messageVO.getType());

        temperature_value = messageVO.getTemperature_value();//当前温度
        temperature_color = messageVO.getTemperature_color();//当前温度颜色，默认为#000000
        humidity_value = messageVO.getHumidity_value();//当前湿度
        humidity_color = messageVO.getHumidity_color();//当前湿度颜色，默认为#000000
        state_value = messageVO.getState_value();//空调状态
        state_color = messageVO.getState_color();//空调状态色，默认为#000000
        alarm_num_value = messageVO.getAlarm_num_value();//今日告警
        alarm_num_color = messageVO.getAlarm_num_color();//今日告警颜色，默认为#000000
    }
}
