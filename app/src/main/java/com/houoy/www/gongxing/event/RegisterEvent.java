package com.houoy.www.gongxing.event;

/**
 * eventBus的
 */
public class RegisterEvent {
    public static final String Next = "Next";
    public static final String Register = "Register";
    public static final String DentifyingCode = "DentifyingCode";
    public static final String Begin_Register = "Begin_Register";
    public static final String Begin_DentifyingCode = "Begin_DentifyingCode";

    private String type;
    private Object data;

    public RegisterEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
