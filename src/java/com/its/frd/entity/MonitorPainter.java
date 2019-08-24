package com.its.frd.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class MonitorPainter implements Serializable {
	private long id;
	
	private String name;
	
	private String bindingData;
	
	private String connections;
	
	private String components;
	
	private int winId;
	
	private String chartsData;
	
	private String monitorTableData;
	
	private String elementsInfo;
	
	private String domContent;
	
	private long userId;
	
	private String isActive;
	
	private String driverSns;
	
	private String background;
	
	private long companyId;
	
	private String container;

	private String spcData;

	private String spcAnalysisData;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(length=10000)
	public String getBindingData() {
		return bindingData;
	}

	public void setBindingData(String bindingData) {
		this.bindingData = bindingData;
	}
	@Column(length=10000)
	public String getConnections() {
		return connections;
	}

	public void setConnections(String connections) {
		this.connections = connections;
	}
	@Column(length=20000)
	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public int getWinId() {
		return winId;
	}

	public void setWinId(int winId) {
		this.winId = winId;
	}
	@Column(length=20000)
	public String getChartsData() {
		return chartsData;
	}

	public void setChartsData(String chartsData) {
		this.chartsData = chartsData;
	}
	@Column(length=20000)
	public String getElementsInfo() {
		return elementsInfo;
	}

	public void setElementsInfo(String elementsInfo) {
		this.elementsInfo = elementsInfo;
	}

	@Column(length=100000)
	public String getDomContent() {
		return domContent;
	}

	public void setDomContent(String domContent) {
		this.domContent = domContent;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getDriverSns() {
		return driverSns;
	}

	public void setDriverSns(String driverSns) {
		this.driverSns = driverSns;
	}
	@Column(length=100000)
	public String getMonitorTableData() {
		return monitorTableData;
	}

	public void setMonitorTableData(String monitorTableData) {
		this.monitorTableData = monitorTableData;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	@Column(length=20000)
	public String getSpcData() {
		return spcData;
	}

	public void setSpcData(String spcData) {
		this.spcData = spcData;
	}

	@Column(length=20000)
	public String getSpcAnalysisData() {
		return spcAnalysisData;
	}

	public void setSpcAnalysisData(String spcAnalysisData) {
		this.spcAnalysisData = spcAnalysisData;
	}
}
