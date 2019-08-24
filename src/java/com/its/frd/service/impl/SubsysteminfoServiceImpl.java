package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.SubsysteminfoDao;
import com.its.frd.entity.Subsysteminfo;
import com.its.frd.service.SubsysteminfoService;
@Service
public class SubsysteminfoServiceImpl implements SubsysteminfoService {
	@Resource
	private SubsysteminfoDao siDao;
	
	@Override
	public List<Subsysteminfo> findPage(Specification<Subsysteminfo> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Subsysteminfo> pageData = siDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(pageData.getTotalElements());
		return pageData.getContent();
	}

	@Override
	public void saveOrUpdateInfo(Subsysteminfo info) {
		siDao.save(info);
	}

	@Override
	public void deleteInfoById(Long id) {
		siDao.delete(id);
	}

	@Override
	public Subsysteminfo findOneById(Long id) {
		return siDao.findOne(id);
	}

}
