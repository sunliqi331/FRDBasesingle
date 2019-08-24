package com.its.frd.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "mes_driver_alarm")
public class MesDriverAlarm implements java.io.Serializable {

	// Fields

	private Long id;
	private MesPoints mesPoints;
	private MesDriver mesDriver;
	private Date updatetime;
	private String statecode;
	private String Tstatecode;
	private Double errorchangevalue;
	private String companyname;

	// Constructors

	/** default constructor */
	public MesDriverAlarm() {
	}

	/** minimal constructor */
	public MesDriverAlarm(MesPoints mesPoints, MesDriver mesDriver,
	        Date updatetime) {
		this.mesPoints = mesPoints;
		this.mesDriver = mesDriver;
		this.updatetime = updatetime;
	}

	/** full constructor */
	public MesDriverAlarm(MesPoints mesPoints, MesDriver mesDriver,
	        Date updatetime, String statecode, String Tstatecode, Double errorchangevalue) {
		this.mesPoints = mesPoints;
		this.mesDriver = mesDriver;
		this.updatetime = updatetime;
		this.statecode = statecode;
		this.Tstatecode = Tstatecode;
		this.errorchangevalue = errorchangevalue;
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
	@JoinColumn(name = "pointid", nullable = false)
	public MesPoints getMesPoints() {
		return this.mesPoints;
	}

	public void setMesPoints(MesPoints mesPoints) {
		this.mesPoints = mesPoints;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driverid", nullable = false)
	public MesDriver getMesDriver() {
		return this.mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatetime", nullable = false, length = 19)
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "statecode", length = 5)
	public String getStatecode() {
		return this.statecode;
	}

	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}

	@Transient
	public String getTstatecode() {
        return Tstatecode;
    }

    public void setTstatecode(String tstatecode) {
        Tstatecode = tstatecode;
    }

    @Column(name = "errorchangevalue", precision = 22, scale = 0)
	public Double getErrorchangevalue() {
		return this.errorchangevalue;
	}

	public void setErrorchangevalue(Double errorchangevalue) {
		this.errorchangevalue = errorchangevalue;
	}

	@Transient
	public String getCompanyname() {
		return mesDriver != null ? mesDriver.getCompanyinfo().getCompanyname() : "";
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

}