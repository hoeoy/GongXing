package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class RegisterEvent {
    public static final String Next = "Next";
    public static final String Register = "Register";
    public static final String DentifyingCode = "DentifyingCode";
    public static final String Begin_CheckUserId = "Begin_CheckUserId";
    public static final String CheckUserId = "CheckUserId";
    public static final String CheckUserId_Fail = "CheckUserId_Fail";
    public static final String Begin_Register = "Begin_Register";
    public static final String Begin_DentifyingCode = "Begin_DentifyingCode";

    private String type;
    private Object data;

    public RegisterEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
