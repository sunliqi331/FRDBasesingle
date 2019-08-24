package com.its.monitor.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.its.frd.entity.MesPointCheckData;

public class QdasDataDetail implements Serializable {
	private String mesPointKey;

	private String mesDriverPointsName;

	private String mesProcedureName;

	private String procedurePropertyName;

	private String maxValue;

	private String minValue;

	private String value;

	private String standardValue;

	private String unit;

	private List<MesPointCheckData> mesPointCheckDataList = new ArrayList<>();

	public String getMesPointKey() {
		return mesPointKey;
	}

	public void setMesPointKey(String mesPointKey) {
		this.mesPointKey = mesPointKey;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getStandardValue() {
		return standardValue;
	}

	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}

	public String getMesDriverPointsName() {
		return mesDriverPointsName;
	}

	public void setMesDriverPointsName(String mesDriverPointsName) {
		this.mesDriverPointsName = mesDriverPointsName;
	}

	public String getMesProcedureName() {
		return mesProcedureName;
	}

	public void setMesProcedureName(String mesProcedureName) {
		this.mesProcedureName = mesProcedureName;
	}

	public String getProcedurePropertyName() {
		return procedurePropertyName;
	}

	public void setProcedurePropertyName(String procedurePropertyName) {
		this.procedurePropertyName = procedurePropertyName;
	}

	public List<MesPointCheckData> getMesPointCheckDataList() {
		return mesPointCheckDataList;
	}

	public void setMesPointCheckDataList(List<MesPointCheckData> mesPointCheckDataList) {
		this.mesPointCheckDataList = mesPointCheckDataList;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
