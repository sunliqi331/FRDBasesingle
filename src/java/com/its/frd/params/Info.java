package com.its.frd.params;

import java.io.Serializable;

/**
 * 具体异常信息
 */
public class Info implements Serializable{
	public enum ChangeValueType{
		UP,  //超过上限
		DOWN //低于下限
	}
	
	//变化值
	private Double changeValue;
	private ChangeValueType valueType;
	private Long pointId;   //测点主键
	
	public Double getChangeValue() {
		return changeValue;
	}
	public void setChangeValue(Double changeValue) {
		this.changeValue = changeValue;
	}
	public ChangeValueType getValueType() {
		return valueType;
	}
	public void setValueType(ChangeValueType valueType) {
		this.valueType = valueType;
	}
	public Long getPointId() {
		return pointId;
	}
	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}
	
}
