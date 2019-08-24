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

import com.its.common.entity.main.Permission;

@Entity
@Table(name = "mes_company_role_permission")
public class MesCompanyRolePermission implements java.io.Serializable {

	// Fields

	private Long id;
	private Permission permission;
	private MesCompanyRole mesCompanyRole;

	// Constructors

	/** default constructor */
	public MesCompanyRolePermission() {
	}

	/** full constructor */
	public MesCompanyRolePermission(Permission Permission,
			MesCompanyRole mesCompanyRole) {
		this.permission = Permission;
		this.mesCompanyRole = mesCompanyRole;
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
	@JoinColumn(name = "permission_id")
	public Permission getPermission() {
		return this.permission;
	}

	public void setPermission(Permission Permission) {
		this.permission = Permission;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_role_id")
	public MesCompanyRole getMesCompanyRole() {
		return this.mesCompanyRole;
	}

	public void setMesCompanyRole(MesCompanyRole mesCompanyRole) {
		this.mesCompanyRole = mesCompanyRole;
	}

}