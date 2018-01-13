package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 2017/12/23.
 */
@lombok.Data
@NoArgsConstructor
@Table(name = "user")
public class ClientInfo implements Serializable {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "UserID")
    private String UserID;//用户名
    @Column(name = "MsgType")
    private String MsgType;
    @Column(name = "RelationID")
    private String RelationID;
    @Column(name = "IDCode")
    private String IDCode;//识别码
    @Column(name = "Logourl")
    private String Logourl;
    @Column(name = "Password")
    private String Password;//密码
    @Column(name = "PhoneNum")
    private String PhoneNum;//手机号
    @Column(name = "openid")
    private String openid;//用户微信唯一标识
    @Column(name = "verification")
    private String verification;//手机验证码
}
