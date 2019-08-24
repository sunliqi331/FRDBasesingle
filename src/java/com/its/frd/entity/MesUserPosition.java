package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.its.common.entity.main.User;

@Entity
@Table(name = "mes_user_position")
public class MesUserPosition implements java.io.Serializable {

	// Fields

	private Long id;
	private MesCompanyPosition mesCompanyPosition;
	private Long userid;

	// Constructors

	/** default constructor */
	public MesUserPosition() {
	}

	/** full constructor */
	public MesUserPosition(MesCompanyPosition mesCompanyPosition,
			Long userid) {
		this.mesCompanyPosition = mesCompanyPosition;
		this.userid = userid;
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
	@JoinColumn(name = "positionid")
	public MesCompanyPosition getMesCompanyPosition() {
		return this.mesCompanyPosition;
	}

	public void setMesCompanyPosition(MesCompanyPosition mesCompanyPosition) {
		this.mesCompanyPosition = mesCompanyPosition;
	}

	@Column(name = "userid")
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

}