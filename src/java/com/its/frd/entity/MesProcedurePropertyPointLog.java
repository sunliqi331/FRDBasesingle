package com.its.frd.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "mes_procedure_property_point_log")
public class MesProcedurePropertyPointLog implements java.io.Serializable {

	// Fields

	private Long id;
	private Long propertyid;
	private Long pointid;
	private String codeKey;
	private Timestamp createdate;

	// Constructors

	/** default constructor */
	public MesProcedurePropertyPointLog() {
	}

	/** minimal constructor */
	public MesProcedurePropertyPointLog(Timestamp createdate) {
		this.createdate = createdate;
	}

	/** full constructor */
	public MesProcedurePropertyPointLog(Long propertyid, Long pointid,
			Timestamp createdate) {
		this.propertyid = propertyid;
		this.pointid = pointid;
		this.createdate = createdate;
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

	@Column(name = "propertyid")
	public Long getPropertyid() {
		return this.propertyid;
	}

	public void setPropertyid(Long propertyid) {
		this.propertyid = propertyid;
	}

	@Column(name = "pointid")
	public Long getPointid() {
		return this.pointid;
	}

	public void setPointid(Long pointid) {
		this.pointid = pointid;
	}

	@Column(name = "createdate", nullable = false, length = 19)
	public Timestamp getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}
	@Column(name = "codekey")
	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	
}