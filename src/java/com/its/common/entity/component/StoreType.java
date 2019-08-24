package com.its.common.entity.component;

public enum StoreType {
	DB("db"), FILE("file");
	
	private String value;
	
	StoreType(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
