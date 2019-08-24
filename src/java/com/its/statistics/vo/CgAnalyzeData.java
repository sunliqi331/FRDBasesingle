package com.its.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CgAnalyzeData {
	private int id;
	
	private String productName;
	
	private String mesDriverName;
	
	private String productProcedureName;
	
	private String procedurePropertyName;
	
	private String productionSn;
	
	private String time;
	
	private String value;

	private String rowKey = "";

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMesDriverName() {
		return mesDriverName;
	}

	public void setMesDriverName(String mesDriverName) {
		this.mesDriverName = mesDriverName;
	}

	public String getProductProcedureName() {
		return productProcedureName;
	}

	public void setProductProcedureName(String productProcedureName) {
		this.productProcedureName = productProcedureName;
	}

	public String getProcedurePropertyName() {
		return procedurePropertyName;
	}

	public void setProcedurePropertyName(String procedurePropertyName) {
		this.procedurePropertyName = procedurePropertyName;
	}

	public String getProductionSn() {
		return productionSn;
	}

	public void setProductionSn(String productionSn) {
		this.productionSn = productionSn;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CgAnalyzeData [productName=" + productName + ", mesDriverName=" + mesDriverName
				+ ", productProcedureName=" + productProcedureName + ", procedurePropertyName=" + procedurePropertyName
				+ ", productionSn=" + productionSn + ", time=" + time + ", value=" + value + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }
	
	
}
