package com.houoy.www.gongxing.model;

import java.io.Serializable;

/**
 * Created by andyzhao on 2017/12/23.
 */

public class Data implements Serializable {
    private ClientInfo ClientInfo;
    private DataPart DataPart;

    public ClientInfo getClientInfo() {
        return ClientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        ClientInfo = clientInfo;
    }

    public DataPart getDataPart() {
        return DataPart;
    }

    public void setDataPart(DataPart dataPart) {
        DataPart = dataPart;
    }
}
