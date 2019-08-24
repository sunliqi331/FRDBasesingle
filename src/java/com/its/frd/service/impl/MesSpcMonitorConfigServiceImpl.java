package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.frd.dao.MesSpcMonitorConfigDao;
import com.its.frd.entity.MesSpcMonitorConfig;
import com.its.frd.service.MesSpcMonitorConfigService;

@Service("mesSpcMonitorConfigService")
public class MesSpcMonitorConfigServiceImpl implements MesSpcMonitorConfigService{

	@Resource
	private MesSpcMonitorConfigDao mesSpcMonitorConfigDao;
	@Override
	public List<MesSpcMonitorConfig> findPage(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MesSpcMonitorConfig> findPage(Specification<MesSpcMonitorConfig> specification, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MesSpcMonitorConfig saveOrUpdate(MesSpcMonitorConfig t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MesSpcMonitorConfig> findAll() {
		// TODO Auto-generated method stub
		return mesSpcMonitorConfigDao.findAll();
	}

	@Override
	public MesSpcMonitorConfig findById(long id) {
		// TODO Auto-generated method stub
		return mesSpcMonitorConfigDao.findOne(id);
	}

}
