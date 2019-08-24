package com.its.frd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_drivertype_property")
public class MesDrivertypeProperty implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDrivertype mesDrivertype;
	private String propertyname;
	private String propertykeyid;
	private String propertytypecode;
	private String datatype;
	private String units;

	// Constructors

	/** default constructor */
	public MesDrivertypeProperty() {
	}

	/** full constructor */
	public MesDrivertypeProperty(MesDrivertype mesDrivertype,
			String propertyname, String propertykeyid) {
		this.mesDrivertype = mesDrivertype;
		this.propertyname = propertyname;
		this.propertykeyid = propertykeyid;
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
	@JoinColumn(name = "drivertypeid")
	@JsonIgnore
	public MesDrivertype getMesDrivertype() {
		return this.mesDrivertype;
	}

	public void setMesDrivertype(MesDrivertype mesDrivertype) {
		this.mesDrivertype = mesDrivertype;
	}

	@Column(name = "propertyname", length = 30)
	public String getPropertyname() {
		return this.propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	@Column(name = "propertykeyid", length = 30)
	public String getPropertykeyid() {
		return this.propertykeyid;
	}

	public void setPropertykeyid(String propertykeyid) {
		this.propertykeyid = propertykeyid;
	}
	
	
	public String getDatatype() {
		return datatype;
	}
	@Column(name = "datatype", length = 30)
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Column(name = "units", length = 30)
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
}