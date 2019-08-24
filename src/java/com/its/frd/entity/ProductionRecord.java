package com.its.frd.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "production_record")
public class ProductionRecord implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDriver mesDriver;
	private Integer currentStatus;
	private Timestamp updatetime;
	private Long productnum;
	private Long failureCount;
	private Long passCount;
	private Long totalCount;

	// Constructors

	/** default constructor */
	public ProductionRecord() {
	}

	/** minimal constructor */
	public ProductionRecord(Integer currentStatus) {
		this.currentStatus = currentStatus;
	}

	/** full constructor */
	public ProductionRecord(MesDriver mesDriver, Integer currentStatus,
			Timestamp updatetime, Long productnum, Long failureCount,
			Long passCount, Long totalCount) {
		this.mesDriver = mesDriver;
		this.currentStatus = currentStatus;
		this.updatetime = updatetime;
		this.productnum = productnum;
		this.failureCount = failureCount;
		this.passCount = passCount;
		this.totalCount = totalCount;
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
	@JoinColumn(name = "driverid")
	public MesDriver getMesDriver() {
		return this.mesDriver;
	}

	public void setMesDriver(MesDriver mesDriver) {
		this.mesDriver = mesDriver;
	}

	@Column(name = "current_status", nullable = false)
	public Integer getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(Integer currentStatus) {
		this.currentStatus = currentStatus;
	}

	@Column(name = "updatetime", length = 19)
	public Timestamp getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "productnum")
	public Long getProductnum() {
		return this.productnum;
	}

	public void setProductnum(Long productnum) {
		this.productnum = productnum;
	}

	@Column(name = "failure_count")
	public Long getFailureCount() {
		return this.failureCount;
	}

	public void setFailureCount(Long failureCount) {
		this.failureCount = failureCount;
	}

	@Column(name = "pass_count")
	public Long getPassCount() {
		return this.passCount;
	}

	public void setPassCount(Long passCount) {
		this.passCount = passCount;
	}

	@Column(name = "total_count")
	public Long getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

}