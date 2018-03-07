package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 *
 */
@lombok.Data
@NoArgsConstructor
public class RefreshChatEvent {
	private String type;
	private Object data;

	public RefreshChatEvent(String type, Object data) {
		super();
		this.data = data;
		this.type = type;
	}
}
