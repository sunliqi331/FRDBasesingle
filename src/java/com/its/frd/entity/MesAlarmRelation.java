package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 设备告警展示类型对应关系 实体
 * @author pactera
 *
 */

@Entity
@Table(name = "mes_alarm_relation")
public class MesAlarmRelation implements java.io.Serializable{

	//Fields
	private Long id;
	private MesPoints mesPoints;
	private String alarmCode;
	private String alarmType;
	private String info;

	public MesAlarmRelation() {
		super();
	}

	//Constructors
	public MesAlarmRelation(MesPoints mesPoints, String alarmCode, String alarmType, String info) {
		super();
		this.mesPoints = mesPoints;
		this.alarmCode = alarmCode;
		this.alarmType = alarmType;
		this.info = info;
	}
	
	//Full Constructors
	public MesAlarmRelation(Long id, MesPoints mesPoints, String alarmCode, String alarmType, String info) {
		super();
		this.id = id;
		this.mesPoints = mesPoints;
		this.alarmCode = alarmCode;
		this.alarmType = alarmType;
		this.info = info;
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
	@ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY,targetEntity=MesPoints.class)
	@JoinColumn(name = "pointid")
	public MesPoints getMesPoints() {
		return mesPoints;
	}
	
	public void setMesPoints(MesPoints mesPoints) {
		this.mesPoints = mesPoints;
	}
	
	@Column(name = "alarmcode")
	public String getAlarmCode() {
		return alarmCode;
	}
	
	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}
	
	@Column(name = "alarmtype")
	public String getAlarmType() {
		return alarmType;
	}
	
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	@Column(name = "info")
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
}
