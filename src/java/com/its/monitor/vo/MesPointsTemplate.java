package com.its.monitor.vo;

import java.util.ArrayList;
import java.util.List;

import com.its.frd.entity.MesPointCheckData;

public class MesPointsTemplate {
	private String mesPointKey = "";
	
	private long mesPointId = 0L;
	
	private String mesDriverPointsName = "";
	
	private String mesPointGateway = "";
	
	private String mesPointTypeKey = "";
	
	private String factoryName = "";
	
	private long factoryId = 0L;
	
	private long companyId = 0L;
	
	private String mesDriverName  = "";
	
	private String productlineName = "";
	
	private String mesProduct = "";
	
	private String mesProductModel = "";
	
	private String mesProductCompanyinfo = "";
	
	private long mesDriverId = 0L;
	
	private long mesDriverProcedureId = 0L;
	
	private String mesProcedureName = "";
	
	private String procedurePropertyName = "";
	
	private Long mesProductLineId = 0L;
	
	private long procedurePropertyId = 0L;
	
	private long productId = 0L;
	
	private String maxValue = "";
	
	private String minValue = "";
	
	private String value = "";
	
	private String standardValue = "";
	
	private String unit = "";
	
	private int isDelete;
	
	private List<MesPointCheckData> mesPointCheckDataList = new ArrayList<>();
	
	public String getMesPointKey() {
		return mesPointKey;
	}

	public void setMesPointKey(String mesPointKey) {
		this.mesPointKey = mesPointKey;
	}

	public String getMesPointGateway() {
		return mesPointGateway;
	}

	public void setMesPointGateway(String mesPointGateway) {
		this.mesPointGateway = mesPointGateway;
	}

	public String getMesPointTypeKey() {
		return mesPointTypeKey;
	}

	public void setMesPointTypeKey(String mesPointTypeKey) {
		this.mesPointTypeKey = mesPointTypeKey;
	}

	public long getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(long mesDriverId) {
		this.mesDriverId = mesDriverId;
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

	public long getMesDriverProcedureId() {
		return mesDriverProcedureId;
	}

	public void setMesDriverProcedureId(long mesDriverProcedureId) {
		this.mesDriverProcedureId = mesDriverProcedureId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getMesProduct() {
		return mesProduct;
	}

	public void setMesProduct(String mesProduct) {
		this.mesProduct = mesProduct;
	}

	public String getMesProductModel() {
		return mesProductModel;
	}

	public void setMesProductModel(String mesProductModel) {
		this.mesProductModel = mesProductModel;
	}

	public String getMesProductCompanyinfo() {
		return mesProductCompanyinfo;
	}

	public void setMesProductCompanyinfo(String mesProductCompanyinfo) {
		this.mesProductCompanyinfo = mesProductCompanyinfo;
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

	public long getProcedurePropertyId() {
		return procedurePropertyId;
	}

	public void setProcedurePropertyId(long procedurePropertyId) {
		this.procedurePropertyId = procedurePropertyId;
	}

	public long getMesPointId() {
		return mesPointId;
	}

	public void setMesPointId(long mesPointId) {
		this.mesPointId = mesPointId;
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

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Long getMesProductLineId() {
		return mesProductLineId;
	}

	public void setMesProductLineId(Long mesProductLineId) {
		this.mesProductLineId = mesProductLineId;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(long factoryId) {
		this.factoryId = factoryId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getMesDriverName() {
		return mesDriverName;
	}

	public void setMesDriverName(String mesDriverName) {
		this.mesDriverName = mesDriverName;
	}

	public String getProductlineName() {
		return productlineName;
	}

	public void setProductlineName(String productlineName) {
		this.productlineName = productlineName;
	}
	
	
}
