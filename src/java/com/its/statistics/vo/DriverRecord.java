package com.its.statistics.vo;

import java.io.Serializable;
import java.math.BigInteger;
import com.its.frd.util.DateUtils;

public class DriverRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2679160790699959808L;

	private BigInteger id;
	
	private BigInteger factoryId;
	
	private String factoryName;
	
	private BigInteger companyId;
	
	private String companyName;
	
	private BigInteger productlineId;
	
	private String productlineName;
	
	private BigInteger datetime;
	
	private BigInteger mesDriverId;
	
	private String mesDriverName;
	
	private String rowkey;
	
	private String gateway;
	
	private String status = "NG";
	
	private String driver_status = "-1";
	
	private String driver_count = "0";
	
	private boolean insertable = false;
	
	//private List<MesProductProcedure> list = new ArrayList<>();

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(BigInteger factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public BigInteger getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigInteger companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigInteger getProductlineId() {
		return productlineId;
	}

	public void setProductlineId(BigInteger productlineId) {
		this.productlineId = productlineId;
	}

	public String getProductlineName() {
		return productlineName;
	}

	public void setProductlineName(String productlineName) {
		this.productlineName = productlineName;
	}

	public String getDatetime() {
		String date = DateUtils.unixTimestampToDate(Long.parseLong(String.valueOf(datetime)+"000")).replaceAll(" 00:00:00", "");
		return date;
	}

	public void setDatetime(BigInteger datetime) {
		this.datetime = datetime;
	}

	public BigInteger getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(BigInteger mesDriverId) {
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

	public String getDriver_status() {
		return driver_status;
	}

	public void setDriver_status(String driver_status) {
		this.driver_status = driver_status;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public String getDriver_count() {
		return driver_count;
	}

	public void setDriver_count(String driver_count) {
		this.driver_count = driver_count;
	}
	
	
}


