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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * MesSubdriver entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mes_subdriver")
public class MesSubdriver implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDriver mesDriver;
	private Long driverid;

	// Constructors

	/** default constructor */
	public MesSubdriver() {
	}

	/** full constructor */
	public MesSubdriver(MesDriver mesDriver, Long driverid) {
		this.mesDriver = mesDriver;
		this.driverid = driverid;
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
	@JoinColumn(name = "driverparentid")
	@JsonIgnore
	public MesDriver getMesDriver() {
		return this.mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}

	@Column(name = "driverid")
	public Long getDriverid() {
		return this.driverid;
	}

	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}

}