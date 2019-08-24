package com.its.frd.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Subsysteminfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "subsysteminfo")
public class Subsysteminfo implements java.io.Serializable {

	// Fields

	private Long id;
	private String sysname;
	private Float price;
	private String url;
	private String description;
	private Timestamp createtime;
	private String picname;
	private String status = "0";
	private String valids;
	private String defaultrole;
	private Long defaultroleid;

	// Constructors

	/** default constructor */
	public Subsysteminfo() {
	}

	/** minimal constructor */
	public Subsysteminfo(Timestamp createtime, String limit) {
		this.createtime = createtime;
		this.valids = limit;
	}

	/** full constructor */
	public Subsysteminfo(String sysname, Float price, String url,
			String description, Timestamp createtime, String picname,
			String status, String limit, String defaultrole, Long defaultroleid) {
		this.sysname = sysname;
		this.price = price;
		this.url = url;
		this.description = description;
		this.createtime = createtime;
		this.picname = picname;
		this.status = status;
		this.valids = limit;
		this.defaultrole = defaultrole;
		this.defaultroleid = defaultroleid;
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

	@Column(name = "sysname", length = 30)
	public String getSysname() {
		return this.sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	@Column(name = "price", precision = 12, scale = 0)
	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	
	@Column(name = "url", length = 30)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "description", length = 30)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "createtime", nullable = false, length = 19)
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	@Column(name = "picname", length = 30)
	public String getPicname() {
		return this.picname;
	}

	public void setPicname(String picname) {
		this.picname = picname;
	}

	@Column(name = "status", length = 5)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "defaultrole", length = 20)
	public String getDefaultrole() {
		return this.defaultrole;
	}

	public void setDefaultrole(String defaultrole) {
		this.defaultrole = defaultrole;
	}

	@Column(name = "defaultroleid")
	public Long getDefaultroleid() {
		return this.defaultroleid;
	}

	public void setDefaultroleid(Long defaultroleid) {
		this.defaultroleid = defaultroleid;
	}

	public String getValids() {
		return valids;
	}

	public void setValids(String valids) {
		this.valids = valids;
	}
	
}