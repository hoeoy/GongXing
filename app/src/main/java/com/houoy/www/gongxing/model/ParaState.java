package com.houoy.www.gongxing.model;

import java.io.Serializable;

/**
 * Created by andyzhao on 2017/12/23.
 */

public class ParaState implements Serializable {
    private String Name;
    private String BackColor;
    private String FontColor;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBackColor() {
        return BackColor;
    }

    public void setBackColor(String backColor) {
        BackColor = backColor;
    }

    public String getFontColor() {
        return FontColor;
    }

    public void setFontColor(String fontColor) {
        FontColor = fontColor;
    }
}
