package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "chat_talker")
public class ChatTalker extends BaseModel {
    @Column(name = "talker_name")
    private String talker_name;
//    @Column(name = "talker_type")
//    private Integer talker_type;//聊天者的类型
}
