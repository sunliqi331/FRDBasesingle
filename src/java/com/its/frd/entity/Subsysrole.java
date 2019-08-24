package com.its.frd.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Subsysrole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "subsysrole",catalog="frd_base")
public class Subsysrole implements java.io.Serializable {

	// Fields

	private Long id;
	private String rolename;
	private Long roleid;
	private Long subsysid;
	private Long userid;
	private Timestamp createtime;
	private String isable;

	// Constructors

	/** default constructor */
	public Subsysrole() {
	}

	/** minimal constructor */
	public Subsysrole(Timestamp createtime) {
		this.createtime = createtime;
	}

	/** full constructor */
	public Subsysrole(String rolename, Long roleid, Long subsysid, Long userid,
			Timestamp createtime, String isable) {
		this.rolename = rolename;
		this.roleid = roleid;
		this.subsysid = subsysid;
		this.userid = userid;
		this.createtime = createtime;
		this.isable = isable;
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

	@Column(name = "rolename", length = 30)
	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@Column(name = "roleid")
	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	@Column(name = "subsysid")
	public Long getSubsysid() {
		return this.subsysid;
	}

	public void setSubsysid(Long subsysid) {
		this.subsysid = subsysid;
	}

	@Column(name = "userid")
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "createtime", nullable = false, length = 19)
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	@Column(name = "isable", length = 5)
	public String getIsable() {
		return this.isable;
	}

	public void setIsable(String isable) {
		this.isable = isable;
	}

}