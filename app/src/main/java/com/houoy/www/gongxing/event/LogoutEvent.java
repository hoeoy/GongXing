package com.houoy.www.gongxing.event;

/**
 * eventBus的
 */
public class LogoutEvent {
	private String type;
	private Object data;

	public LogoutEvent(String type, Object data) {
		super();
		this.data = data;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
