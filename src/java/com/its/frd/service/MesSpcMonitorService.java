package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesSpcMonitor;

public interface MesSpcMonitorService extends BaseService<MesSpcMonitor>{
	public List<MesSpcMonitor> findAll();
	
	public MesSpcMonitor findById(long id);
	
}
