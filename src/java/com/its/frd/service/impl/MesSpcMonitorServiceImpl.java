package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesSpcMonitorDao;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesSpcMonitor;
import com.its.frd.entity.MesSpcMonitorConfig;
import com.its.frd.entity.MesSpcMonitorMonitorConfig;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesSpcMonitorConfigService;
import com.its.frd.service.MesSpcMonitorMonitorConfigService;
import com.its.frd.service.MesSpcMonitorService;
@Service("mesSpcMonitorService")
public class MesSpcMonitorServiceImpl implements MesSpcMonitorService {

	@Resource
	private MesSpcMonitorDao mesSpcMonitorDao;
	@Resource
	private MesSpcMonitorConfigService mesSpcMonitorConfigService;
	@Resource
	private MesSpcMonitorMonitorConfigService mesSpcMonitorMonitorConfigService;
	@Resource
	private MesDriverService mesDriverService;
	@Override
	public List<MesSpcMonitor> findPage(Page page) {
		org.springframework.data.domain.Page<MesSpcMonitor> springDataPage = mesSpcMonitorDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesSpcMonitor> findPage(Specification<MesSpcMonitor> specification, Page page) {
		org.springframework.data.domain.Page<MesSpcMonitor> springDataPage = mesSpcMonitorDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesSpcMonitor saveOrUpdate(MesSpcMonitor mesSpcMonitor) {
		// TODO Auto-generated method stub
		MesSpcMonitor editMesSpcMonitor = null;
		try {

			if (mesSpcMonitor.getId() != 0) {
				editMesSpcMonitor = mesSpcMonitorDao.findOne(mesSpcMonitor.getId());
				BeanUtils.copyProperties(editMesSpcMonitor, mesSpcMonitor);

			}else{
				editMesSpcMonitor = mesSpcMonitor;
			}
			editMesSpcMonitor.setUserId(SecurityUtils.getShiroUser().getUser().getId());
			MesDriver mesDriver = mesDriverService.findById(editMesSpcMonitor.getMesDriverId());
			editMesSpcMonitor.setProductLineId(mesDriver.getMesProductline().getId());
			editMesSpcMonitor.setProductLineName(mesDriver.getMesProductline().getLinename());
			editMesSpcMonitor = mesSpcMonitorDao.saveAndFlush(editMesSpcMonitor);
			MesSpcMonitorMonitorConfig mesSpcMonitorMonitorConfig = new MesSpcMonitorMonitorConfig();
			mesSpcMonitorMonitorConfig.setMesSpcMonitor(editMesSpcMonitor);
			MesSpcMonitorConfig mesSpcMonitorConfig = mesSpcMonitorConfigService.findById(1);
			mesSpcMonitorMonitorConfig.setMesSpcMonitorConfig(mesSpcMonitorConfig);
			mesSpcMonitorMonitorConfigService.saveOrUpdate(mesSpcMonitorMonitorConfig);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return editMesSpcMonitor;
	}

	@Override
	public List<MesSpcMonitor> findAll() {
		// TODO Auto-generated method stub
		return mesSpcMonitorDao.findAll();
	}

	@Override
	public MesSpcMonitor findById(long id) {
		// TODO Auto-generated method stub
		return mesSpcMonitorDao.findOne(id);
	}

}
