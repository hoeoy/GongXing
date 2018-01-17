package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class SearchWarningMessageDataEvent {
    private String type;
    private Object data;

    public SearchWarningMessageDataEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
