package com.its.frd.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="mes_spc_monitor")
public class MesSpcMonitor implements Serializable {
	private long id;
	
	private long userId;
	
	private List<MesSpcMonitorMonitorConfig> spcMonitorMonitorConfigs = new ArrayList<>();

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	
	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,mappedBy="mesSpcMonitor")
	@JsonIgnore
    public List<MesSpcMonitorMonitorConfig> getSpcMonitorMonitorConfigs() {
		return spcMonitorMonitorConfigs;
	}

	public void setSpcMonitorMonitorConfigs(List<MesSpcMonitorMonitorConfig> spcMonitorMonitorConfigs) {
		this.spcMonitorMonitorConfigs = spcMonitorMonitorConfigs;
	}





	private long mesDriverId;
    
    private String mesDriverName;
	
    private int isMonitor;
    
	private long productLineId;
	
	private String productLineName;
	
	private long productId;
	
	private String productName;
	
	private long productProcedureId;
	
	private String productProcedureName;
	
	private long procedurePropertyId;
	
	private String procedurePropertyName;
	
	private int subRange;
	
	private int subNum;

	public long getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(long mesDriverId) {
		this.mesDriverId = mesDriverId;
	}

	public long getProductLineId() {
		return productLineId;
	}

	public void setProductLineId(long productLineId) {
		this.productLineId = productLineId;
	}

	public String getProductLineName() {
		return productLineName;
	}

	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getProductProcedureId() {
		return productProcedureId;
	}

	public void setProductProcedureId(long productProcedureId) {
		this.productProcedureId = productProcedureId;
	}

	public String getProductProcedureName() {
		return productProcedureName;
	}

	public void setProductProcedureName(String productProcedureName) {
		this.productProcedureName = productProcedureName;
	}

	public long getProcedurePropertyId() {
		return procedurePropertyId;
	}

	public void setProcedurePropertyId(long procedurePropertyId) {
		this.procedurePropertyId = procedurePropertyId;
	}

	public String getProcedurePropertyName() {
		return procedurePropertyName;
	}

	public void setProcedurePropertyName(String procedurePropertyName) {
		this.procedurePropertyName = procedurePropertyName;
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

	public int getIsMonitor() {
		return isMonitor;
	}

	public void setIsMonitor(int isMonitor) {
		this.isMonitor = isMonitor;
	}

	public String getMesDriverName() {
		return mesDriverName;
	}

	public void setMesDriverName(String mesDriverName) {
		this.mesDriverName = mesDriverName;
	}
	
	
}
