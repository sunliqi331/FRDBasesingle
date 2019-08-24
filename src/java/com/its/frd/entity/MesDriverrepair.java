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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_driverrepair")
public class MesDriverrepair implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDriver mesDriver;
	private Timestamp starttime;
	private Timestamp endtime;
	private String repairperson;
	private String repaircycle;
	private String repaircause;
	private String telnum;
	private String status = DRIVER_REPAIRE_NOTSTART;
	
	private String startDate;
	private String endDate;
	
	private final static String DRIVER_REPAIRE_OFF = "已完成";
	private final static String DRIVER_REPAIRE_IN = "维修中";
	private final static String DRIVER_REPAIRE_NOTSTART = "未开始";
	
	// Constructors

	/** default constructor */
	public MesDriverrepair() {
	}

	/** full constructor */
	public MesDriverrepair(MesDriver mesDriver, Timestamp starttime,
			Timestamp endtime, String repairperson, String repaircycle,
			String repaircause, String status) {
		this.mesDriver = mesDriver;
		this.starttime = starttime;
		this.endtime = endtime;
		this.repairperson = repairperson;
		this.repaircycle = repaircycle;
		this.repaircause = repaircause;
		this.status = status;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "driverid")
	//@JsonIgnore
	public MesDriver getMesDriver() {
		return this.mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@Column(name = "starttime", length = 19)
	public Timestamp getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@Column(name = "endtime", length = 19)
	public Timestamp getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	@Column(name = "repairperson", length = 20)
	public String getRepairperson() {
		return this.repairperson;
	}

	public void setRepairperson(String repairperson) {
		this.repairperson = repairperson;
	}

	@Column(name = "repaircycle", length = 10)
	public String getRepaircycle() {
		return this.repaircycle;
	}

	public void setRepaircycle(String repaircycle) {
		this.repaircycle = repaircycle;
	}

	@Column(name = "repaircause", length = 30)
	public String getRepaircause() {
		return this.repaircause;
	}

	public void setRepaircause(String repaircause) {
		this.repaircause = repaircause;
	}

	@Column(name = "status", length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "telnum", length = 11)
	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	@Transient
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Transient
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
}