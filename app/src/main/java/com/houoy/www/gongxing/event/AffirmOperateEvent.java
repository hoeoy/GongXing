package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class AffirmOperateEvent {
    private String type;
    private Object data;

    public AffirmOperateEvent(String type, Object data) {
        super();
        this.data = data;
        this.type = type;
    }
}
