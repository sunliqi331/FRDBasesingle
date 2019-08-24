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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 设备OEE实体类
 * @author pactera
 *
 */
@Entity
@Table(name = "mes_driver_oee")
public class MesDriverOEE implements java.io.Serializable{

	//Fields
	private Long id;
	private Companyinfo companyinfo;
	private MesProductline mesProductline;
	private MesDriver mesDriver;
	private Date createDate;
	private String classes;
	private String timeRate;//时间开动率
	private String propertyRate;//性能开动率
	private String passRate;//合格品率
	private String oeeRate;//设备OEE
	private Date starttime;//班次开始时间
	private Date endtime;//班次结束时间
	private String type;//记录类型，手动，自动
	
	// Constructors
	public MesDriverOEE() {
	}
	
	//Full Constructors
	public MesDriverOEE(Long id, Companyinfo companyinfo, MesProductline mesProductline, MesDriver mesDriver,
			Date createDate, String classes, String timeRate, String propertyRate, String passRate, String oeeRate,
			Date starttime, Date endtime, String type) {
		super();
		this.id = id;
		this.companyinfo = companyinfo;
		this.mesProductline = mesProductline;
		this.mesDriver = mesDriver;
		this.createDate = createDate;
		this.classes = classes;
		this.timeRate = timeRate;
		this.propertyRate = propertyRate;
		this.passRate = passRate;
		this.oeeRate = oeeRate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.type = type;
	}
	
	public MesDriverOEE(Companyinfo companyinfo, MesProductline mesProductline, MesDriver mesDriver, Date createDate,
			String classes, String timeRate, String propertyRate, String passRate, String oeeRate, Date starttime,
			Date endtime, String type) {
		super();
		this.companyinfo = companyinfo;
		this.mesProductline = mesProductline;
		this.mesDriver = mesDriver;
		this.createDate = createDate;
		this.classes = classes;
		this.timeRate = timeRate;
		this.propertyRate = propertyRate;
		this.passRate = passRate;
		this.oeeRate = oeeRate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.type = type;
	}

	// Property accessors
	
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
	@ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY,targetEntity=Companyinfo.class)
	@JoinColumn(name = "companyid")
	public Companyinfo getCompanyinfo() {
		return companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY,targetEntity=MesProductline.class)
	@JoinColumn(name = "productlineid")
	public MesProductline getMesProductline() {
		return mesProductline;
	}

	public void setMesProductline(MesProductline mesProductline) {
		this.mesProductline = mesProductline;
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

	@Column(name = "createdate")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "classes")
	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Column(name = "timerate", length = 30)
	public String getTimeRate() {
		return timeRate;
	}

	public void setTimeRate(String timeRate) {
		this.timeRate = timeRate;
	}

	@Column(name = "propertyrate", length = 30)
	public String getPropertyRate() {
		return propertyRate;
	}

	public void setPropertyRate(String propertyRate) {
		this.propertyRate = propertyRate;
	}

	@Column(name = "passrate", length = 30)
	public String getPassRate() {
		return passRate;
	}

	public void setPassRate(String passRate) {
		this.passRate = passRate;
	}

	@Column(name = "oeerate", length = 30)
	public String getOeeRate() {
		return oeeRate;
	}

	public void setOeeRate(String oeeRate) {
		this.oeeRate = oeeRate;
	}
	
	@Column(name = "starttime")
	@JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	
	@Column(name = "endtime")
	@JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	@Column(name = "type",length = 20)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
