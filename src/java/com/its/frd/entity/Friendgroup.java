package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Friendgroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "friendgroup")
public class Friendgroup implements java.io.Serializable {

	// Fields

	private Long id;
	private String groupname;
	private String createTime;
	private String createUser;
	private Long userid;
	private Long groupid;

	// Constructors

	/** default constructor */
	public Friendgroup() {
	}

	/** minimal constructor */
	public Friendgroup(Long userid) {
		this.userid = userid;
	}

	/** full constructor */
	public Friendgroup(String groupname, Long userid) {
		this.groupname = groupname;
		this.userid = userid;
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

	@Column(name = "groupname", length = 20)
	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	@Column(name = "userid", nullable = false)
	public Long getUserid() {
		return this.userid;
	}

	@Column(name = "groupid", nullable = false)
	public Long getGroupid() {
		return groupid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Transient
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Transient
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	
	
}