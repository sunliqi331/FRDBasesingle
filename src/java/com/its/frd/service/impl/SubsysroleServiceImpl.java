package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.SubsysroleDao;
import com.its.frd.entity.Subsysrole;
import com.its.frd.service.SubsysroleService;
@Service
public class SubsysroleServiceImpl implements SubsysroleService {
	@Resource
	private SubsysroleDao siDao;
	
	@Override
	public List<Subsysrole> findPage(Specification<Subsysrole> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Subsysrole> pageData = siDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(pageData.getTotalElements());
		return pageData.getContent();
	}

	@Override
	public void saveOrUpdateInfo(Subsysrole info) {
		siDao.save(info);
	}

	@Override
	public void deleteInfoById(Long id) {
		siDao.delete(id);
	}

	@Override
	public Subsysrole findOneById(Long id) {
		return siDao.findOne(id);
	}

}
