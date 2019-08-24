package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 设备报警展示实体类
 * @author pactera
 *
 */

@Entity
@Table(name = "mes_alarm_show")
public class MesAlarmShow implements java.io.Serializable{
	
	//Fields
	private Long id;
	private MesDriver mesDriver;
	private MesAlarmRelation mesAlarmRelation;
	private Date datetime;
	private String status;
	
	private String companyName;
	private String productionLineName;
	
	public MesAlarmShow() {
		super();
	}

	//Constructors
	public MesAlarmShow(MesDriver mesDriver, MesAlarmRelation mesAlarmRelation, Date datetime, String status) {
		super();
		this.mesDriver = mesDriver;
		this.mesAlarmRelation = mesAlarmRelation;
		this.datetime = datetime;
		this.status = status;
	}

	//Full Constructors
	public MesAlarmShow(Long id, MesDriver mesDriver, MesAlarmRelation mesAlarmRelation, Date datetime, String status) {
		super();
		this.id = id;
		this.mesDriver = mesDriver;
		this.mesAlarmRelation = mesAlarmRelation;
		this.datetime = datetime;
		this.status = status;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY,targetEntity=MesDriver.class)
	@JoinColumn(name = "driverid")
	public MesDriver getMesDriver() {
		return mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY,targetEntity=MesAlarmRelation.class)
	@JoinColumn(name = "relationid")
	public MesAlarmRelation getMesAlarmRelation() {
		return mesAlarmRelation;
	}

	public void setMesAlarmRelation(MesAlarmRelation mesAlarmRelation) {
		this.mesAlarmRelation = mesAlarmRelation;
	}
	
	@Column(name = "datetime")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	public String getCompanyName() {
		return this.mesDriver !=null ? (this.mesDriver.getMesProductline() != null ? 
				this.mesDriver.getMesProductline().getCompanyinfo().getCompanyname() : this.companyName) : "" ;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Transient
	public String getProductionLineName() {
		return this.mesDriver !=null ? (this.mesDriver.getMesProductline() != null ? 
				this.mesDriver.getMesProductline().getLinename() : this.productionLineName) : "" ;
	}

	public void setProductionLineName(String productionLineName) {
		this.productionLineName = productionLineName;
	}

}
