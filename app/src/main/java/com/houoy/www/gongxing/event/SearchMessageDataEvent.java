package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class SearchMessageDataEvent {
    private String type;
    private Object data;

    public SearchMessageDataEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
