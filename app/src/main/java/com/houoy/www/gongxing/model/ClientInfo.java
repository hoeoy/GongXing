package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * 用户，消息 等统一model
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
    @Column(name = "name")
    private String name;//用户名
    @Column(name = "MsgType")
    private String MsgType;//数据类型:（0：错误；1：查询；2：报警（主动推送）；3：日报(主动推送)；4：应答；）
    @Column(name = "RelationID")
    private String RelationID;
    @Column(name = "IDCode")
    private String IDCode;//识别码
    @Column(name = "IDENTIFYINGCODE")
    private String IDENTIFYINGCODE;//识别码的另一个名字，与识别码应该相同
    @Column(name = "Logourl")
    private String Logourl;
    @Column(name = "Password")
    private String Password;//密码
    @Column(name = "PhoneNum")
    private String PhoneNum;//手机号
    @Column(name = "openid")
    private String openid;//用户微信唯一标识
    @Column(name = "WeChatID")
    private String WeChatID;//用户的微信端唯一码,与openid一样
    @Column(name = "verification")
    private String verification;//手机验证码

    @Column(name = "Topic")
    private String Topic;//用于用户接受消息推送需要订阅的topic
    @Column(name = "headimgurl")
    private String headimgurl;//用户头像url


//    //冗余字段clientid
//    @Column(name = "clientId")
//    private String clientId;

}
