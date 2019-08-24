package com.its.frd.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 监控画面元素(设备)存放定义
 * @author Administrator
 *
 */
@Entity
@Table(name="elementInfo")
public class ElementInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String elementId;
	
	private String elementDiv;
	
	private String flowchartWindow;
	
	private String currentDivId;
	
	private String connector;

	private ProductionLine productionLine;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	@Column(name = "elementDiv", nullable = false,length=10000)
	public String getElementDiv() {
		return elementDiv;
	}

	public void setElementDiv(String elementDiv) {
		this.elementDiv = elementDiv;
	}

	public String getFlowchartWindow() {
		return flowchartWindow;
	}

	public void setFlowchartWindow(String flowchartWindow) {
		this.flowchartWindow = flowchartWindow;
	}

	public String getCurrentDivId() {
		return currentDivId;
	}

	public void setCurrentDivId(String currentDivId) {
		this.currentDivId = currentDivId;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@JsonIgnore
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,targetEntity=ProductionLine.class)
	public ProductionLine getProductionLine() {
		return productionLine;
	}

	public void setProductionLine(ProductionLine productionLine) {
		this.productionLine = productionLine;
	}
	
	
	
}
