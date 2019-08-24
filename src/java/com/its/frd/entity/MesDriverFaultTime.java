package com.its.frd.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "mes_driver_fault_time")
public class MesDriverFaultTime implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDriver mesDriver;
	private Long pointid;
	private Integer changetime;
	private Timestamp createtime;

	// Constructors

	/** default constructor */
	public MesDriverFaultTime() {
	}

	/** full constructor */
	public MesDriverFaultTime(MesDriver mesDriver, Long pointid,
			Integer changetime, Timestamp createtime) {
		this.mesDriver = mesDriver;
		this.pointid = pointid;
		this.changetime = changetime;
		this.createtime = createtime;
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

	@Column(name = "pointid")
	public Long getPointid() {
		return this.pointid;
	}

	public void setPointid(Long pointid) {
		this.pointid = pointid;
	}

	@Column(name = "changetime")
	public Integer getChangetime() {
		return this.changetime;
	}

	public void setChangetime(Integer changetime) {
		this.changetime = changetime;
	}

	@Column(name = "createtime", length = 19)
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

}