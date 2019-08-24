package com.its.frd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "mes_procedure_property")
public class MesProcedureProperty implements java.io.Serializable {

	// Fields

	private Long id;
	private MesProductProcedure mesProductProcedure;
	private String controlWay;
	private String keyid;
	private String lowervalues;
	private String propertyname;
	private String spcpropertyname;
	private String standardvalues;
	private String uppervalues;
	private String valueUnit;
	private String units;
	private String datatype;
	private MesPoints mesPoints;
	private MesProcedurePropertyPoint mesProcedurePropertyPoint;
	private String checkValue;
	private String checkTime;

	// Constructors

	/** default constructor */
	public MesProcedureProperty() {
	}

	/** full constructor */
	public MesProcedureProperty(MesProductProcedure mesProductProcedure,
			String controlWay, String keyid, String lowervalues,
			String propertyname, String standardvalues, String uppervalues,
			String valueUnit) {
		this.mesProductProcedure = mesProductProcedure;
		this.controlWay = controlWay;
		this.keyid = keyid;
		this.lowervalues = lowervalues;
		this.propertyname = propertyname;
		this.standardvalues = standardvalues;
		this.uppervalues = uppervalues;
		this.valueUnit = valueUnit;
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
	@JoinColumn(name = "procedureid")
	public MesProductProcedure getMesProductProcedure() {
		return this.mesProductProcedure;
	}

	public void setMesProductProcedure(MesProductProcedure mesProductProcedure) {
		this.mesProductProcedure = mesProductProcedure;
	}

	@Column(name = "control_way")
	public String getControlWay() {
		return this.controlWay;
	}

	public void setControlWay(String controlWay) {
		this.controlWay = controlWay;
	}

	@Column(name = "keyid", length = 64)
	public String getKeyid() {
		return this.keyid;
	}

	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}
	
	@Size(min=0,max=30,message="下限值长度在0-30之间!")
	@Column(name = "lowervalues", length = 30)
	public String getLowervalues() {
		return this.lowervalues;
	}

	public void setLowervalues(String lowervalues) {
		this.lowervalues = lowervalues;
	}

	@Column(name = "propertyname", length = 30)
	public String getPropertyname() {
		return this.propertyname;
	}
    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    @Column(name = "spcpropertyname", length = 30)
    public String getSpcpropertyname() {
        return spcpropertyname;
    }
    public void setSpcpropertyname(String spcpropertyname) {
        this.spcpropertyname = spcpropertyname;
    }
	@Size(min=0,max=30,message="标准值长度在0-30之间!")
	@Column(name = "standardvalues", length = 30)
	public String getStandardvalues() {
		return this.standardvalues;
	}

	public void setStandardvalues(String standardvalues) {
		this.standardvalues = standardvalues;
	}
	@Size(min=0,max=30,message="上限值长度在0-30之间!")
	@Column(name = "uppervalues", length = 30)
	public String getUppervalues() {
		return this.uppervalues;
	}

	public void setUppervalues(String uppervalues) {
		this.uppervalues = uppervalues;
	}

	@Column(name = "value_unit")
	public String getValueUnit() {
		return this.valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pointid")
	public MesPoints getMesPoints() {
		return this.mesPoints;
	}

	public void setMesPoints(MesPoints mesPoints) {
		this.mesPoints = mesPoints;
	}
	@OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType. ALL}, mappedBy = "mesProcedureProperty")
	@JsonIgnore
	public MesProcedurePropertyPoint getMesProcedurePropertyPoint() {
		return this.mesProcedurePropertyPoint;
	}

	public void setMesProcedurePropertyPoint(
			MesProcedurePropertyPoint mesProcedurePropertyPoint) {
		this.mesProcedurePropertyPoint = mesProcedurePropertyPoint;
	}

	@Transient
	public String getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
	@Transient
	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	@Column(name = "units", length = 30)
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	@Column(name = "datatype", length = 30)
	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
}