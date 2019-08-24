package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.MesSpcMonitorConfig;

public interface MesSpcMonitorConfigService extends BaseService<MesSpcMonitorConfig> {
	public List<MesSpcMonitorConfig> findAll();

	public MesSpcMonitorConfig findById(long id);
}
