package com.its.frd.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="mes_point_gateway_exchange")
public class MesPointGatewayExchange implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1472946876351258436L;
	
	private Long id;
	
	private String originalMac;
	
	private String currentMac;
	
	private MesPointGatewayExchange sourceId;
	
	private List<MesPointGatewayExchange> currentGatewayLogs;
	
	private String macCode;
	
	private Date changeDate;
	
	private Companyinfo companyinfo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriginalMac() {
		return originalMac;
	}

	public void setOriginalMac(String originalMac) {
		this.originalMac = originalMac;
	}

	public String getCurrentMac() {
		return currentMac;
	}

	public void setCurrentMac(String currentMac) {
		this.currentMac = currentMac;
	}

	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,targetEntity=MesPointGatewayExchange.class)
	public MesPointGatewayExchange getSourceId() {
		return sourceId;
	}

	public void setSourceId(MesPointGatewayExchange sourceId) {
		this.sourceId = sourceId;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sourceId",targetEntity=MesPointGatewayExchange.class)
	@JsonIgnore
	public List<MesPointGatewayExchange> getCurrentGatewayLogs() {
		return currentGatewayLogs;
	}

	public void setCurrentGatewayLogs(List<MesPointGatewayExchange> currentGatewayLogs) {
		this.currentGatewayLogs = currentGatewayLogs;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,targetEntity=Companyinfo.class)
	@JoinColumn(name = "companyid")
	public Companyinfo getCompanyinfo() {
		return companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}

	@Transient
	public String getMacCode() {
		return macCode;
	}

	public void setMacCode(String macCode) {
		this.macCode = macCode;
	}
	
	

}
