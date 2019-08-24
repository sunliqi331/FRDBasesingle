package com.its.frd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "mes_qdas")
public class MesQdas implements java.io.Serializable {

	private Long id;
	private String name;
	private String parameter;
	private String classtype;
	private String keyfield;
	private MesQdasCategory mesqdascategory;

	public MesQdas() {
	}

	public MesQdas(String name, String parameter, String keyfield) {
		this.name = name;
		this.parameter = parameter;
		this.keyfield = keyfield;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "parameter", length = 50)
	public String getParameter() {
		return this.parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Column(name = "keyfield", length = 50)
	public String getKeyfield() {
		return this.keyfield;
	}

	public void setKeyfield(String keyfield) {
		this.keyfield = keyfield;
	}

	@Column(name = "classtype", length = 50)
	public String getClasstype() {
		return classtype;
	}

	public void setClasstype(String classtype) {
		this.classtype = classtype;
	}

	@JoinColumn(name = "qdascategoryid")
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,targetEntity=MesQdasCategory.class)
	public MesQdasCategory getMesqdascategory() {
		return mesqdascategory;
	}

	public void setMesqdascategory(MesQdasCategory mesqdascategory) {
		this.mesqdascategory = mesqdascategory;
	}

	
}