package com.its.frd.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class MesSpcMonitorMonitorConfig implements Serializable {
	private long id;
	
	private MesSpcMonitor mesSpcMonitor;
	
	private MesSpcMonitorConfig mesSpcMonitorConfig;
	
    private int m;
	
	private int n;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
	public MesSpcMonitor getMesSpcMonitor() {
		return mesSpcMonitor;
	}

	public void setMesSpcMonitor(MesSpcMonitor mesSpcMonitor) {
		this.mesSpcMonitor = mesSpcMonitor;
	}
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
	public MesSpcMonitorConfig getMesSpcMonitorConfig() {
		return mesSpcMonitorConfig;
	}

	public void setMesSpcMonitorConfig(MesSpcMonitorConfig mesSpcMonitorConfig) {
		this.mesSpcMonitorConfig = mesSpcMonitorConfig;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}


	
	
}
