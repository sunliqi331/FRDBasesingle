package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "user_companyrole")
public class UserCompanyrole implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer priority;
	private MesCompanyRole mesCompanyRole;
	private Long userId;

	// Constructors

	/** default constructor */
	public UserCompanyrole() {
	}

	/** full constructor */
	public UserCompanyrole(Integer priority, Long companyRoleId, Long userId) {
		this.priority = priority;
		this.userId = userId;
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

	@Column(name = "priority")
	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_role_id")
	public MesCompanyRole getMesCompanyRole() {
		return this.mesCompanyRole;
	}

	public void setMesCompanyRole(MesCompanyRole mesCompanyRole) {
		this.mesCompanyRole = mesCompanyRole;
	}
	@Column(name = "userId")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}