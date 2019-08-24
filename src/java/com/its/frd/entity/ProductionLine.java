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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 产品线(监控画面)存放定义
 * @author Administrator
 *
 */
@Entity
@Table(name="productionLine")
public class ProductionLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1397039895408426491L;

	private long id;
	
	private String lineName;
	
	private int elementAmount;
	
	private String domContent;
	
	private List<ElementInfo> elementInfoList = new ArrayList<ElementInfo>();
	
	private List<ConnectionInfo> connectionInfoList = new ArrayList<ConnectionInfo>();
	
	private List<ChartsData> chartsDataList = new ArrayList<ChartsData>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLineName() {
		return this.lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,mappedBy="productionLine",targetEntity=ElementInfo.class)
	public List<ElementInfo> getElementInfoList() {
		return this.elementInfoList;
	}

	public void setElementInfoList(List<ElementInfo> elementInfoList) {
		this.elementInfoList = elementInfoList;
	}
	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,mappedBy="productionLine",targetEntity=ConnectionInfo.class)
	public List<ConnectionInfo> getConnectionInfoList() {
		return this.connectionInfoList;
	}

	public void setConnectionInfoList(List<ConnectionInfo> connectionInfoList) {
		this.connectionInfoList = connectionInfoList;
	}

	public int getElementAmount() {
		return elementAmount;
	}

	public void setElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
	}

	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,mappedBy="productionLine",targetEntity=ChartsData.class)
	public List<ChartsData> getChartsDataList() {
		return this.chartsDataList;
	}

	public void setChartsDataList(List<ChartsData> chartsDataList) {
		this.chartsDataList = chartsDataList;
	}

	@Transient
	public String getDomContent() {
		return domContent;
	}

	public void setDomContent(String domContent) {
		this.domContent = domContent;
	}
	
}
