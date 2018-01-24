package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * eventBus的
 */
@lombok.Data
@NoArgsConstructor
public class LoginEvent {
	private String type;
	private Object data;

	public LoginEvent(String type, Object data) {
		super();
		this.data = data;
		this.type = type;
	}
}
