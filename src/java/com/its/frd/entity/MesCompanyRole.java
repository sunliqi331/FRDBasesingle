package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_company_role")
public class MesCompanyRole implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Long companyid;
	private String changeable;
	private List<MesCompanyRolePermission> rolePermissions = new ArrayList<MesCompanyRolePermission>();
	private List<UserCompanyrole> userCompanyroles = new ArrayList<UserCompanyrole>();
	// Constructors

	/** default constructor */
	public MesCompanyRole() {
	}

	/** full constructor */
	public MesCompanyRole(String name, String description, Long companyid,
			List<MesCompanyRolePermission> mesCompanyRolePermissions) {
		this.name = name;
		this.description = description;
		this.companyid = companyid;
		this.rolePermissions = mesCompanyRolePermissions;
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

	@Column(name = "name", length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "changeable", length = 5)
	public String getChangeable() {
		return changeable;
	}

	public void setChangeable(String changeable) {
		this.changeable = changeable;
	}

	@Column(name = "companyid")
	public Long getCompanyid() {
		return this.companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesCompanyRole")
	public List<MesCompanyRolePermission> getRolePermissions() {
		return this.rolePermissions;
	}

	public void setRolePermissions(
			List<MesCompanyRolePermission> mesCompanyRolePermissions) {
		this.rolePermissions = mesCompanyRolePermissions;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesCompanyRole")
	public List<UserCompanyrole> getUserCompanyroles() {
		return this.userCompanyroles;
	}

	public void setUserCompanyroles(List<UserCompanyrole> userCompanyroles) {
		this.userCompanyroles = userCompanyroles;
	}
}