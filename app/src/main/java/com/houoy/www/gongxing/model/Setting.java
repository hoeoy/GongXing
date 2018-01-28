package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * 用户自定义设置表
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
@Table(name = "setting")
public class Setting implements Serializable {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "notificationSound")
    private String notificationSound;//通知声音,1 系统声音  2 3 4 其他


}
