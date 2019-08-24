package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.frd.dao.MesSpcMonitorMonitorConfigDao;
import com.its.frd.entity.MesSpcMonitorMonitorConfig;
import com.its.frd.service.MesSpcMonitorMonitorConfigService;

@Service("mesSpcMonitorMonitorConfigService")
public class MesSpcMonitorMonitorConfigServiceImpl implements MesSpcMonitorMonitorConfigService {

	@Resource
	private MesSpcMonitorMonitorConfigDao mesSpcMonitorMonitorConfigDao;
	@Override
	public List<MesSpcMonitorMonitorConfig> findPage(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MesSpcMonitorMonitorConfig> findPage(Specification<MesSpcMonitorMonitorConfig> specification,
			Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MesSpcMonitorMonitorConfig saveOrUpdate(MesSpcMonitorMonitorConfig t) {
		// TODO Auto-generated method stub
		return mesSpcMonitorMonitorConfigDao.saveAndFlush(t);
	}

}
