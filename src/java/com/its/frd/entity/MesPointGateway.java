package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_point_gateway")
public class MesPointGateway implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String mac;
	private Companyinfo companyinfo;
	private MesDriver mesDriver;
	private String gateWayStatus = "";
	private String virtualFlag = MesPointGateway.UNVIRTUAL;
	private String macCode;
	private String isActive = MesPointGateway.ACTIVE_NO;
	private Timestamp createTime;
	private List<MesPoints> mesPointses = new ArrayList<MesPoints>();
	private List<MesPointGateway> mesPointGatewaies = new ArrayList<MesPointGateway>();
	
	public static final String VIRTUAL = "YES";
	public static final String UNVIRTUAL = "NO";
	public static final String ACTIVE_YES = "YES";
	public static final String ACTIVE_NO = "NO";

	// Constructors

	/** default constructor */
	public MesPointGateway() {
	}

	public MesPointGateway(Long id) {
		super();
		this.id = id;
	}

	/** full constructor */
	public MesPointGateway(String mac, List<MesPoints> mesPointses) {
		this.mac = mac;
		this.mesPointses = mesPointses;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "mac", length = 30)
	public String getMac() {
		return this.mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyid")
	public Companyinfo getCompanyinfo() {
		return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesPointGateway")
	public List<MesPoints> getMesPointses() {
		return this.mesPointses;
	}

	public void setMesPointses(List<MesPoints> mesPointses) {
		this.mesPointses = mesPointses;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<MesPointGateway> getMesPointGatewaies() {
		return this.mesPointGatewaies;
	}

	public void setMesPointGatewaies(List<MesPointGateway> mesPointGatewaies) {
		this.mesPointGatewaies = mesPointGatewaies;
	}
	@Transient
	public String getGateWayStatus() {
		return gateWayStatus;
	}

	public void setGateWayStatus(String gateWayStatus) {
		this.gateWayStatus = gateWayStatus;
	}
	@Column(name = "virtual_flag", length = 20)
	public String getVirtualFlag() {
		return virtualFlag;
	}

	public void setVirtualFlag(String virtualFlag) {
		this.virtualFlag = virtualFlag;
	}
	@Column(name = "mac_code", length = 255)
	public String getMacCode() {
		return macCode;
	}

	public void setMacCode(String macCode) {
		this.macCode = macCode;
	}
	@Column(name = "is_active", length = 255)
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Transient
	public MesDriver getMesDriver() {
		return mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}
	
	
	
}