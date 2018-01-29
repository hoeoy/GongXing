package com.houoy.www.gongxing.event;

import lombok.NoArgsConstructor;

/**
 * 消息页面刷新
 */
@lombok.Data
@NoArgsConstructor
public class RefreshMessageEvent {
	private String type;
	private Object data;

	public RefreshMessageEvent(String type, Object data) {
		super();
		this.data = data;
		this.type = type;
	}
}
