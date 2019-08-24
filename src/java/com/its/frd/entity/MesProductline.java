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
@Table(name = "mes_productline")
public class MesProductline implements java.io.Serializable {

	// Fields

	private Long id;
	private Companyinfo companyinfo;
	//private MesProduct mesProduct;
	private String linesn;
	private String linename;
	private List<MesDriver> mesDrivers = new ArrayList<MesDriver>();
	
	private String mesdriver;

	// Constructors

	/** default constructor */
	public MesProductline() {
	}
	
	public MesProductline(Long id) {
		super();
		this.id = id;
	}

	/** full constructor */
	public MesProductline(Companyinfo companyinfo, //MesProduct mesProduct,
			 String linesn, String linename,
			List<MesDriver> mesDrivers) {
		this.companyinfo = companyinfo;
		//this.mesProduct = mesProduct;
		this.linesn = linesn;
		this.linename = linename;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "factoryid")
//	@JsonIgnore
	public Companyinfo getCompanyinfo() {
		return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productid")
	@JsonIgnore
	public MesProduct getMesProduct() {
		return this.mesProduct;
	}

	public void setMesProduct(MesProduct mesProduct) {
		this.mesProduct = mesProduct;
	}*/


	@Column(name = "linesn", length = 30)
	public String getLinesn() {
		return this.linesn;
	}

	public void setLinesn(String linesn) {
		this.linesn = linesn;
	}

	@Column(name = "linename", length = 20)
	public String getLinename() {
		return this.linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	@OneToMany(cascade={CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "mesProductline")
	@JsonIgnore
	public List<MesDriver> getMesDrivers() {
		return this.mesDrivers;
	}
	
	public void setMesDrivers(List<MesDriver> mesDrivers) {
		this.mesDrivers = mesDrivers;
	}

	@Transient
    public String getMesdriver() {
        return mesdriver;
    }

    public void setMesdriver(String mesdriver) {
        this.mesdriver = mesdriver;
    }

}