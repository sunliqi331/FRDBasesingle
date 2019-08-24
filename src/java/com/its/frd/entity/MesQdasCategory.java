package com.its.frd.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="mes_qdas_category")
public class MesQdasCategory {
	private long id;
	
	private String name;
	
	private int isexport;
	
	private List<MesQdas> mesQdasList = new ArrayList<>();

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,mappedBy="mesqdascategory")
	public List<MesQdas> getMesQdasList() {
		return mesQdasList;
	}

	public void setMesQdasList(List<MesQdas> mesQdasList) {
		this.mesQdasList = mesQdasList;
	}

	public int getIsexport() {
		return isexport;
	}

	public void setIsexport(int isexport) {
		this.isexport = isexport;
	}
	
	
}
