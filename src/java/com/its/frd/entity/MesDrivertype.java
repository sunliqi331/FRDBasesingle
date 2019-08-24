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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_drivertype")
public class MesDrivertype implements java.io.Serializable {

	// Fields

	private Long id;
	private String typename;
	@JsonIgnore
	private Companyinfo companyinfo;
	private List<MesDrivertypeProperty> mesDrivertypeProperties = new ArrayList<MesDrivertypeProperty>();
	private List<MesDriver> mesDrivers = new ArrayList<MesDriver>();
	
	private String showPic;
	// Constructors

	/** default constructor */
	public MesDrivertype() {
	}

	/** full constructor */
	public MesDrivertype(String typename,
			List<MesDrivertypeProperty> mesDrivertypeProperties,
			List<MesDriver> mesDrivers) {
		this.typename = typename;
		this.mesDrivertypeProperties = mesDrivertypeProperties;
		this.mesDrivers = mesDrivers;
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

	@Column(name = "typename", length = 30)
	public String getTypename() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDrivertype")
	public List<MesDrivertypeProperty> getMesDrivertypeProperties() {
		return this.mesDrivertypeProperties;
	}

	public void setMesDrivertypeProperties(
			List<MesDrivertypeProperty> mesDrivertypeProperties) {
		this.mesDrivertypeProperties = mesDrivertypeProperties;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDrivertype")
	public List<MesDriver> getMesDrivers() {
		return this.mesDrivers;
	}

	public void setMesDrivers(List<MesDriver> mesDrivers) {
		this.mesDrivers = mesDrivers;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyid")
	public Companyinfo getCompanyinfo() {
		return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}
	@Transient
	public String getShowPic() {
		return showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}
	
}