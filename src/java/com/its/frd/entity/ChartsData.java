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
 * 数据表(echarts)存放定义
 * @author Administrator
 *
 */
@Entity
@Table(name="chartsData")
public class ChartsData implements Serializable {

	private static final long serialVersionUID = 6195835600512342306L;

	private long id;
	
	private String chartId;
	
	private String elementDiv;
	
	private ProductionLine productionLine;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "elementDiv", nullable = false,length=10000)
	public String getElementDiv() {
		return elementDiv;
	}

	public void setElementDiv(String elementDiv) {
		this.elementDiv = elementDiv;
	}
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,targetEntity=ProductionLine.class)
	public ProductionLine getProductionLine() {
		return productionLine;
	}

	public void setProductionLine(ProductionLine productionLine) {
		this.productionLine = productionLine;
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}
	
	
}
