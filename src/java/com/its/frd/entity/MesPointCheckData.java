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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_point_check_data")
public class MesPointCheckData implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDriverPoints mesDriverPoints;
	private Long checkvalue;
	private String colorcode;
	private String color;
	private String name;
	private Long companyfileId;
	private String companyfilePath;

	// Constructors

	/** default constructor */
	public MesPointCheckData() {
	}

	/** full constructor */
	public MesPointCheckData(MesDriverPoints mesDriverPoints,
			Long checkvalue, String colorcode,String name,String color) {
		this.mesDriverPoints = mesDriverPoints;
		this.checkvalue = checkvalue;
		this.colorcode = colorcode;
		this.name = name;
		this.color = color;
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
	@JoinColumn(name = "driverpointid")
	@JsonIgnore
	public MesDriverPoints getMesDriverPoints() {
		return this.mesDriverPoints;
	}

	public void setMesDriverPoints(MesDriverPoints mesDriverPoints) {
		this.mesDriverPoints = mesDriverPoints;
	}

	@Column(name = "checkvalue")
	public Long getCheckvalue() {
		return this.checkvalue;
	}

	public void setCheckvalue(Long checkvalue) {
		this.checkvalue = checkvalue;
	}

	@Column(name = "colorcode", length = 10)
	public String getColorcode() {
		return this.colorcode;
	}

	public void setColorcode(String colorcode) {
		this.colorcode = colorcode;
	}

	@Column(name = "name", length = 36)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

	public Long getCompanyfileId() {
		return companyfileId;
	}

	public void setCompanyfileId(Long companyfileId) {
		this.companyfileId = companyfileId;
	}

	@Transient
	public String getCompanyfilePath() {
		return companyfilePath;
	}

	public void setCompanyfilePath(String companyfilePath) {
		this.companyfilePath = companyfilePath;
	}

}