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
@Table(name = "mes_product")
public class MesProduct implements java.io.Serializable {

	// Fields

	private Long id;
	private String modelnum;
	private String name;
	private String productnum;
	private Companyinfo companyinfo;
	//private List<MesProductline> mesProductlines = new ArrayList<MesProductline>();
	//private List<MesProduction> productionRecords = new ArrayList<MesProduction>();
	private List<MesProductProcedure> mesProductProcedures = new ArrayList<MesProductProcedure>();

	// Constructors

	/** default constructor */
	public MesProduct() {
	}


	/** full constructor */
	public MesProduct(String modelnum, String name, Long companyid,
			//List<MesProductline> mesProductlines,
			List<MesProduction> productionRecords,String productionstatscode,
			List<MesProductProcedure> mesProductProcedures) {
		this.modelnum = modelnum;
		this.name = name;
		//this.companyid = companyid;
		//this.mesProductlines = mesProductlines;
		//this.productionRecords = productionRecords;
		this.mesProductProcedures = mesProductProcedures;
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

	@Column(name = "modelnum", length = 30)
	public String getModelnum() {
		return this.modelnum;
	}

	public void setModelnum(String modelnum) {
		this.modelnum = modelnum;
	}

	@Column(name = "name", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesProduct")
	public List<MesProductline> getMesProductlines() {
		return this.mesProductlines;
	}

	public void setMesProductlines(List<MesProductline> mesProductlines) {
		this.mesProductlines = mesProductlines;
	}
*/
	/*@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesProduct")
	public List<MesProduction> getProductionRecords() {
		return this.productionRecords;
	}

	public void setProductionRecords(List<MesProduction> productionRecords) {
		this.productionRecords = productionRecords;
	}*/

    @JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesProduct")
	public List<MesProductProcedure> getMesProductProcedures() {
		return this.mesProductProcedures;
	}

	public void setMesProductProcedures(
			List<MesProductProcedure> mesProductProcedures) {
		this.mesProductProcedures = mesProductProcedures;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyid", nullable = false)
	public Companyinfo getCompanyinfo() {
		return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}

	@Column(name = "productnum", length = 50)
	public String getProductnum() {
		return productnum;
	}
	public void setProductnum(String productnum) {
		this.productnum = productnum;
	}

}