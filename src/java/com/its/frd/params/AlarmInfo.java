package com.its.frd.params;

import java.io.Serializable;

import org.json.JSONObject;

/**
 *  发送告警信息模型
 */
public class AlarmInfo<T extends BaseAlarm> implements Serializable{
	
	public enum InfoType{
		DRIVER, //设备异常
		PRODUCT //产品异常
	}
	
	private InfoType infoType;
	private T alarm;
	
	public AlarmInfo(){}
	
	public AlarmInfo(InfoType infoType){
		this.infoType = infoType;
	}
	
	public InfoType getInfoType() {
		return infoType;
	}
	public void setInfoType(InfoType infoType) {
		this.infoType = infoType;
	}

	public T getAlarm() {
		return alarm;
	}

	public void setAlarm(T alarm) {
		this.alarm = alarm;
	}
	
}
