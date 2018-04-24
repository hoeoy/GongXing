package com.houoy.www.gongxing.event;

import android.content.Context;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class UpdateEvent {
    public static final String checkVersion = "checkVersion";
    public static final String newVersion = "newVersion";
    public static final String begin = "begin";
    public static final String downloading = "downloading";
    public static final String finish = "finish";
    public static final String error = "error";

    private String type;
    private Object data;

    private Long total;
    private Long current;
    private Boolean isDownloading;

    private Context context;
    public UpdateEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
