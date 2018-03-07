package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 用户
 * Created by andyzhao on 2017/12/23.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class ChatMessage extends BaseModel {
    @Column(name = "talker_id")
    private String talker_id;//此消息是谁发出的
    @Column(name = "house_id")
    private Integer house_id;//此消息属于哪个聊天室

}
