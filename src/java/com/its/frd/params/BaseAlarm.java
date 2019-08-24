package com.its.frd.params;

import java.io.Serializable;

public class BaseAlarm implements Serializable{
	private static final long serialVersionUID = 8125540910460614135L;
	private Info info;
	
	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
	
}
