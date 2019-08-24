package com.its.frd.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "mes_procedure_property_point")
public class MesProcedurePropertyPoint implements java.io.Serializable {

	// Fields

	private Long propertyid;
	private MesPoints mesPoints;
	private MesProcedureProperty mesProcedureProperty;
	private Timestamp createdate;

	// Constructors

	/** default constructor */
	public MesProcedurePropertyPoint() {
	}

	/** full constructor */
	public MesProcedurePropertyPoint(Long propertyid, MesPoints mesPoints,
			MesProcedureProperty mesProcedureProperty, Timestamp createdate) {
		this.propertyid = propertyid;
		this.mesPoints = mesPoints;
		this.mesProcedureProperty = mesProcedureProperty;
		this.createdate = createdate;
	}

	// Property accessors
	@Id
	@Column(name = "propertyid", unique = true, nullable = false)
	public Long getPropertyid() {
		return this.propertyid;
	}

	public void setPropertyid(Long propertyid) {
		this.propertyid = propertyid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pointid", nullable = false)
	public MesPoints getMesPoints() {
		return this.mesPoints;
	}

	public void setMesPoints(MesPoints mesPoints) {
		this.mesPoints = mesPoints;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public MesProcedureProperty getMesProcedureProperty() {
		return this.mesProcedureProperty;
	}

	public void setMesProcedureProperty(
			MesProcedureProperty mesProcedureProperty) {
		this.mesProcedureProperty = mesProcedureProperty;
	}

	@Column(name = "createdate", nullable = false, length = 19)
	public Timestamp getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}

}