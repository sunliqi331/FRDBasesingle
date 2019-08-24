package com.its.frd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.QdasCategoryDao;
import com.its.frd.entity.MesQdasCategory;
import com.its.frd.service.QdasCategoryService;

@Service
public class QdasCategoryServiceImpl implements QdasCategoryService {

	@Autowired
	private QdasCategoryDao qdasCategoryDao;
	@Override
	public List<MesQdasCategory> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesQdasCategory> springDataPage = qdasCategoryDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesQdasCategory> findPage(Specification<MesQdasCategory> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesQdasCategory> springDataPage = qdasCategoryDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesQdasCategory saveOrUpdate(MesQdasCategory t) {
		return qdasCategoryDao.saveAndFlush(t);
	}

	@Override
	public List<MesQdasCategory> findAll() {
		// TODO Auto-generated method stub
		return qdasCategoryDao.findAll();
	}

	@Override
	public MesQdasCategory findById(long id) {
		// TODO Auto-generated method stub
		return qdasCategoryDao.findOne(id);
	}

	@Override
	public List<MesQdasCategory> findAll(Specification<MesQdasCategory> specification) {
		
		return qdasCategoryDao.findAll(specification);
	}

}
