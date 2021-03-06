package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class LogoutEvent {
	private String type;
	private Object data;

	public LogoutEvent(String type, Object data) {
		super();
		this.data = data;
		this.type = type;
	}
}
