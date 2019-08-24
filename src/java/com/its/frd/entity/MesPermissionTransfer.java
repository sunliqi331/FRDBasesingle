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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.its.common.entity.main.User;

@Entity
@Table(name = "mes_permission_transfer")
public class MesPermissionTransfer implements java.io.Serializable {

	// Fields

	private Long id;
	private User UserByTouserid;
	private User UserByFromuserid;
	private Timestamp updatetime;
	private Companyinfo companyinfo;

	// Constructors

	/** default constructor */
	public MesPermissionTransfer() {
	}

	/** minimal constructor */
	public MesPermissionTransfer(Timestamp updatetime, Companyinfo companyinfo) {
		this.updatetime = updatetime;
		this.companyinfo = companyinfo;
	}

	/** full constructor */
	public MesPermissionTransfer(User UserByTouserid,
			User UserByFromuserid, Timestamp updatetime, Companyinfo companyinfo) {
		this.UserByTouserid = UserByTouserid;
		this.UserByFromuserid = UserByFromuserid;
		this.updatetime = updatetime;
		this.companyinfo = companyinfo;
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
	@JoinColumn(name = "touserid")
	public User getUserByTouserid() {
		return this.UserByTouserid;
	}

	public void setUserByTouserid(User UserByTouserid) {
		this.UserByTouserid = UserByTouserid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fromuserid")
	public User getUserByFromuserid() {
		return this.UserByFromuserid;
	}

	public void setUserByFromuserid(User UserByFromuserid) {
		this.UserByFromuserid = UserByFromuserid;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@Column(name = "updatetime", nullable = false, length = 19)
	public Timestamp getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyid", nullable = false)
	//@JsonIgnore
	public Companyinfo getCompanyinfo() {
	    return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
	    this.companyinfo = companyinfo;
	}

}