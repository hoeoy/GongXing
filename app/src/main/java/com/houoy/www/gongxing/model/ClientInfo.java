package com.houoy.www.gongxing.model;

/**
 * Created by andyzhao on 2017/12/23.
 */

public class ClientInfo {
    private String UserID;
    private String MsgType;
    private String RelationID;
    private String IDCode;
    private String Logourl;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getRelationID() {
        return RelationID;
    }

    public void setRelationID(String relationID) {
        RelationID = relationID;
    }

    public String getIDCode() {
        return IDCode;
    }

    public void setIDCode(String IDCode) {
        this.IDCode = IDCode;
    }

    public String getLogourl() {
        return Logourl;
    }

    public void setLogourl(String logourl) {
        Logourl = logourl;
    }
}
