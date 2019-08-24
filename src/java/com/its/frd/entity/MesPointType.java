package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_point_type")
public class MesPointType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String pointtypekey;
	//private Companyinfo companyinfo;
	private MesPointType mesPointType;
	private List<MesPoints> mesPointses = new ArrayList<MesPoints>();
	private List<MesPointType> mesPointTypes = new ArrayList<MesPointType>();
	/**
	 * 产品计数测点
	 */
	public static final String TYPE_PRODUCT_COUNT = "P_COUNT";

	/**
	 * 产品合格测点
	 */
	public static final String TYPE_PRODUCT_STATUS = "P_STATUS";

	/**
	 * 设备状态测点
	 */
	public static final String TYPE_DRIVER_STATUS = "D_STATUS";

	/**
	 * 设备计数测点
	 */
	public static final String TYPE_DRIVER_COUNT = "D_COUNT";

	/**
	 * 工序测点
	 */
	public static final String TYPE_PRODUCT_PROCEDURE = "P_PROCEDURE";

	/**
	 * 设备属性测点
	 */
	public static final String TYPE_DRIVER_MONITOR = "D_MONITOR";

	/**
	 * 耗电量
	 */
	public static final String TYPE_ELECTRIC = "ELECTRIC";

	/**
	 * 耗水量
	 */
	public static final String TYPE_WATER = "WATER";

	/**
	 * 耗气量
	 */
	public static final String TYPE_GAS = "GAS";

	/**
	 * 网关测点
	 */
	public static final String TYPE_POINT_GATEWAY = "POINT_GATEWAY";

	/**
	 * 
	 */
	public static final String TYPE_REPEATER = "REPEATER";

	/**
	 * 设备告警测点
	 */
	public static final String TYPE_Alarm_Show = "Alarm_Show";

	// Constructors

	/** default constructor */
	public MesPointType() {
	}

	public MesPointType(Long id) {
		super();
		this.id = id;
	}

	/** full constructor */
	public MesPointType(String name, String key, List<MesPoints> mesPointses) {
		this.name = name;
		this.pointtypekey = key;
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

	@Column(name = "name", length = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "pointtypekey", length = 20)
	public String getPointtypekey() {
		return this.pointtypekey;
	}

	public void setPointtypekey(String key) {
		this.pointtypekey = key;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesPointType")
	public List<MesPoints> getMesPointses() {
		return this.mesPointses;
	}

	public void setMesPointses(List<MesPoints> mesPointses) {
		this.mesPointses = mesPointses;
	}
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyid")
	public Companyinfo getCompanyinfo() {
		return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentid")
	public MesPointType getMesPointType() {
		return this.mesPointType;
	}

	public void setMesPointType(MesPointType mesPointType) {
		this.mesPointType = mesPointType;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesPointType")
	public List<MesPointType> getMesPointTypes() {
		return this.mesPointTypes;
	}

	public void setMesPointTypes(List<MesPointType> mesPointTypes) {
		this.mesPointTypes = mesPointTypes;
	}
	
}