package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class PushMessageEvent {
	private String type;
	private Object data;

	public PushMessageEvent(String type, Object data) {
		super();
		this.data = data;
		this.type = type;
	}
}
