package com.its.frd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * MesDriverProperty entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mes_driver_property")
public class MesDriverProperty implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDriver mesDriver;
	private String mesDriverSn;
	private String propertyname;
	private String propertykey;
	private double minValue;
	private double maxValue;
	private double standardValue;
	private String unit;
	private String monitor;

	// Constructors

	/** default constructor */
	public MesDriverProperty() {
	}

	/** full constructor */
	public MesDriverProperty(MesDriver mesDriver, String propertyname,
			String propertykey, String monitor) {
		this.mesDriver = mesDriver;
		this.propertyname = propertyname;
		this.propertykey = propertykey;
		this.monitor = monitor;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driverid")
	public MesDriver getMesDriver() {
		return this.mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}

	@Column(name = "propertyname", length = 20)
	public String getPropertyname() {
		return this.propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	@Column(name = "propertykey", length = 30)
	public String getPropertykey() {
		return this.propertykey;
	}

	public void setPropertykey(String propertykey) {
		this.propertykey = propertykey;
	}

	@Column(name = "monitor", length = 5)
	public String getMonitor() {
		return this.monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getStandardValue() {
		return standardValue;
	}

	public void setStandardValue(double standardValue) {
		this.standardValue = standardValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMesDriverSn() {
		return mesDriverSn;
	}

	public void setMesDriverSn(String mesDriverSn) {
		this.mesDriverSn = mesDriverSn;
	}

	
}