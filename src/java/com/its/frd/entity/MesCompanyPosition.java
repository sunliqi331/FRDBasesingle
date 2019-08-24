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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_company_position")
public class MesCompanyPosition implements java.io.Serializable {

	// Fields

	private Long id;
	private Companyinfo companyinfo;
	private String positionname;
	private List<MesUserPosition> mesUserPositions = new ArrayList<MesUserPosition>();

	// Constructors

	/** default constructor */
	public MesCompanyPosition() {
	}

	/** full constructor */
	public MesCompanyPosition(Companyinfo companyinfo, String positionname,
			List<MesUserPosition> mesUserPositions) {
		this.companyinfo = companyinfo;
		this.positionname = positionname;
		this.mesUserPositions = mesUserPositions;
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
	@JoinColumn(name = "companyid")
	public Companyinfo getCompanyinfo() {
		return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}

	@Column(name = "positionname", length = 20)
	public String getPositionname() {
		return this.positionname;
	}

	public void setPositionname(String positionname) {
		this.positionname = positionname;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesCompanyPosition")
	public List<MesUserPosition> getMesUserPositions() {
		return this.mesUserPositions;
	}

	public void setMesUserPositions(List<MesUserPosition> mesUserPositions) {
		this.mesUserPositions = mesUserPositions;
	}

}