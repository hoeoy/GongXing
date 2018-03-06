package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 聊天室
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "chat_house")
public class ChatHouse extends BaseModel {
    public static final int HouseTypeSystemAlert = 100;
    public static final int HouseTypeSystemDaily = 101;
    public static final int HouseTypeUser = 200;

    @Column(name = "house_name")

    private String house_name;

    @Column(name = "unread_num")
    private Integer unread_num;

    @Column(name = "last_essage")
    private String last_essage;//最新的消息

    @Column(name = "house_type")
    private Integer house_type;//聊天室类别

    public void addUnreadNum() {
        if (unread_num == null) {
            unread_num = 0;
        }
        unread_num++;
    }
}
