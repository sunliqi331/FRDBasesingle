package com.its.statistics.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductionRecord1 implements Serializable {

	private static final long serialVersionUID = 8470107445348570092L;

	private String productName;

	private long factoryId;

	private String factoryName;

	private long companyId;

	private String companyName;

	private long productlineId;

	private String productlineName;

	private long productId;

	private String productSn;

	private String datetime;

	private long mesDriverId;

	private String mesDriverName;

	private String rowkey;

	private String gateway;

	private String status = "NG";
	
	private long procedureId;
	
	private String procedureName;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rowkey == null) ? 0 : rowkey.hashCode());
		return result;
	}


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(long factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getProductlineId() {
		return productlineId;
	}

	public void setProductlineId(long productlineId) {
		this.productlineId = productlineId;
	}

	public String getProductlineName() {
		return productlineName;
	}

	public void setProductlineName(String productlineName) {
		this.productlineName = productlineName;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public long getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(long mesDriverId) {
		this.mesDriverId = mesDriverId;
	}

	public String getMesDriverName() {
		return mesDriverName;
	}

	public void setMesDriverName(String mesDriverName) {
		this.mesDriverName = mesDriverName;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getProcedureId() {
		return procedureId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureId(long procedureId) {
		this.procedureId = procedureId;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

}
