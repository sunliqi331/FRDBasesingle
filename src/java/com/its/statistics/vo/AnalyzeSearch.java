package com.its.statistics.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.its.common.util.dwz.Page;

public class AnalyzeSearch implements Serializable{
	/**
	 * 
	 */


	protected long mesDriverId;
	
	protected long productLineId;
	
	protected String productLineName;
	
	protected long productId;
	
	protected String productBatchid;
	
	protected String productName;
	
	protected long productProcedureId;
	
	protected String productProcedureName;
	
	protected long procedurePropertyId;
	
	protected String procedurePropertyName;
	
	private String driver_count;
	
	protected String monitorFlg;

	protected String dataPackage;
	
	protected String rowKeyList;

	protected String rowKeyDelFlg;

	//测量类型
	private Integer meastype;

	public Integer getMeastype() {
		return meastype;
	}

	public void setMeastype(Integer meastype) {
		this.meastype = meastype;
	}

	public String getDriver_count() {
		return driver_count;
	}

	public void setDriver_count(String driver_count) {
		this.driver_count = driver_count;
	}

	public String getProductLineName() {
		return productLineName;
	}

	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	protected String productionSn;
	
	protected Date begin;
	
	protected Date end;
	
	protected Page page;
	
	protected int scale;
	
	protected int subSeq = -1;
	
	protected int subRange;
	
	protected int subNum;
	
	private String suffix;
	
	protected int searchType;

	protected List<Double> values = new ArrayList<>();
	
	public Long getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(long mesDriverId) {
		this.mesDriverId = mesDriverId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getProductProcedureId() {
		return productProcedureId;
	}

	public void setProductProcedureId(long productProcedureId) {
		this.productProcedureId = productProcedureId;
	}

	public long getProcedurePropertyId() {
		return procedurePropertyId;
	}

	public void setProcedurePropertyId(long procedurePropertyId) {
		this.procedurePropertyId = procedurePropertyId;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getProductionSn() {
		return productionSn;
	}

	public void setProductionSn(String productionSn) {
		this.productionSn = productionSn;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getSubSeq() {
		return subSeq;
	}

	public void setSubSeq(int subSeq) {
		this.subSeq = subSeq;
	}

	public int getSubRange() {
		return subRange;
	}

	public void setSubRange(int subRange) {
		this.subRange = subRange;
	}

	public int getSubNum() {
		return subNum;
	}

	public void setSubNum(int subNum) {
		this.subNum = subNum;
	}

	public long getProductLineId() {
		return productLineId;
	}

	public void setProductLineId(long productLineId) {
		this.productLineId = productLineId;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

    public String getMonitorFlg() {
        return monitorFlg;
    }

    public void setMonitorFlg(String monitorFlg) {
        this.monitorFlg = monitorFlg;
    }

    public String getDataPackage() {
        return dataPackage;
    }

    public void setDataPackage(String dataPackage) {
        this.dataPackage = dataPackage;
    }

    public String getRowKeyList() {
        return rowKeyList;
    }

    public void setRowKeyList(String rowKeyList) {
        this.rowKeyList = rowKeyList;
    }

    public String getRowKeyDelFlg() {
        return rowKeyDelFlg;
    }

    public void setRowKeyDelFlg(String rowKeyDelFlg) {
        this.rowKeyDelFlg = rowKeyDelFlg;
    }

    public String getProductBatchid() {
        return productBatchid;
    }

    public void setProductBatchid(String productBatchid) {
        this.productBatchid = productBatchid;
    }

	/*2019-05-17 slq*/
	protected String upper;
	protected String lower;
	protected String queryname;
	protected String chartId;
	protected Long monitorId;

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

	public String getQueryname() {
		return queryname;
	}

	public void setQueryname(String queryname) {
		this.queryname = queryname;
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public Long getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(Long monitorId) {
		this.monitorId = monitorId;
	}
}
