package com.its.frd.entity;

import java.io.Serializable;
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
@Table(name="mes_spc_monitor_config")
public class MesSpcMonitorConfig implements Serializable {
	private long id;
	
	//规则名称，如连续xxx个点在xxx附近区外
	private String ruleName;
	//规则别名
	private String alias;

	private List<MesSpcMonitorMonitorConfig> spcMonitorMonitorConfigs = new ArrayList<>();
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,mappedBy="mesSpcMonitorConfig")
	@JsonIgnore
	public List<MesSpcMonitorMonitorConfig> getSpcMonitorMonitorConfigs() {
		return spcMonitorMonitorConfigs;
	}

	public void setSpcMonitorMonitorConfigs(List<MesSpcMonitorMonitorConfig> spcMonitorMonitorConfigs) {
		this.spcMonitorMonitorConfigs = spcMonitorMonitorConfigs;
	}
	
	
	
}
