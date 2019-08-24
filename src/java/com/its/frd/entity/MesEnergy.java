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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "mes_energy")
public class MesEnergy implements java.io.Serializable {

	// Fields

	private Long id;
	private MesDriver mesDriver;
	private Double value;
	private Timestamp updatetime;
	private String energytype;
	private Double totalValue;

	// Constructors

	/** default constructor */
	public MesEnergy() {
	}

	/** minimal constructor */
	public MesEnergy(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	/** full constructor */
	public MesEnergy(MesDriver mesDriver, Double value, Timestamp updatetime,
			String energytype) {
		this.mesDriver = mesDriver;
		this.value = value;
		this.updatetime = updatetime;
		this.energytype = energytype;
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

	@Column(name = "value", precision = 22, scale = 0)
	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@Column(name = "updatetime", nullable = false, length = 19)
	public Timestamp getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "energytype", length = 30)
	public String getEnergytype() {
		return this.energytype;
	}

	public void setEnergytype(String energytype) {
		this.energytype = energytype;
	}

	@Column(name = "totalvalue", precision = 22, scale = 0)
	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

}