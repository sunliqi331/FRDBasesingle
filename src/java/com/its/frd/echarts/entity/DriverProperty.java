package com.its.frd.echarts.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DriverProperty {
	private Double pointValue;
	private Date updateTime;
	
	public Double getPointValue() {
		return pointValue;
	}
	public void setPointValue(Double pointValue) {
		this.pointValue = pointValue;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
