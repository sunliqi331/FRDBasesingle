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
@Table(name = "mes_driver_points")
public class MesDriverPoints implements java.io.Serializable {

	// Fields

	private Long id;
	private MesPoints mesPoints;
	private MesDriver mesDriver;
	private String maxValue;
	private String minValue;
	private String standardValue;
	private String validation;
	private String Tvalidation;
	private MesDrivertypeProperty mesDrivertypeProperty;
	private List<MesPointCheckData> mesPointCheckDatas = new ArrayList<MesPointCheckData>();
	// Constructors

	/** default constructor */
	public MesDriverPoints() {
	}

	/** full constructor */
	public MesDriverPoints(MesPoints mesPoints, MesDriver mesDriver,
			String maxValue, String minValue, String standardValue) {
		this.mesPoints = mesPoints;
		this.mesDriver = mesDriver;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.standardValue = standardValue;
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
	@JoinColumn(name = "point_id")
	public MesPoints getMesPoints() {
		return this.mesPoints;
	}

	public void setMesPoints(MesPoints mesPoints) {
		this.mesPoints = mesPoints;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id")
	public MesDriver getMesDriver() {
		return this.mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}

	@Column(name = "max_value", length = 30)
	public String getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "min_value", length = 30)
	public String getMinValue() {
		return this.minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	@Column(name = "standard_value", length = 30)
	public String getStandardValue() {
		return this.standardValue;
	}

	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}
	@Column(name = "validation", length = 5)
	public String getValidation() {
		return this.validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}
	
	@Transient
	public String getTvalidation() {
        return Tvalidation;
    }

    public void setTvalidation(String tvalidation) {
        Tvalidation = tvalidation;
    }

    @JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDriverPoints")
	public List<MesPointCheckData> getMesPointCheckDatas() {
		return this.mesPointCheckDatas;
	}

	public void setMesPointCheckDatas(List<MesPointCheckData> mesPointCheckDatas) {
		this.mesPointCheckDatas = mesPointCheckDatas;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drivertypepropertyid")
	@JsonIgnore
	public MesDrivertypeProperty getMesDrivertypeProperty() {
		return this.mesDrivertypeProperty;
	}

	public void setMesDrivertypeProperty(
			MesDrivertypeProperty mesDrivertypeProperty) {
		this.mesDrivertypeProperty = mesDrivertypeProperty;
	}
}