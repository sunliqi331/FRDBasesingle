package com.its.frd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "companyinvitation")
public class Companyinvitation implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userid;
	private Long companyid;
	private String msg;

	// Constructors

	/** default constructor */
	public Companyinvitation() {
	}

	/** full constructor */
	public Companyinvitation(Long userid, Long companyid, String msg) {
		this.userid = userid;
		this.companyid = companyid;
		this.msg = msg;
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

	@Column(name = "userid")
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "companyid")
	public Long getCompanyid() {
		return this.companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	@Column(name = "msg", length = 30)
	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}