package com.its.frd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.QdasDao;
import com.its.frd.entity.MesQdas;
import com.its.frd.service.QdasService;

@Service("qdasService")
public class QdasServiceImpl implements QdasService {

	@Autowired
	private QdasDao qDasDao;
	@Override
	public List<MesQdas> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesQdas> springDataPage = qDasDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesQdas> findPage(Specification<MesQdas> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesQdas> springDataPage = qDasDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesQdas saveOrUpdate(MesQdas t) {
		// TODO Auto-generated method stub
		return qDasDao.saveAndFlush(t);
	}

	@Override
	public MesQdas findById(long id) {
		// TODO Auto-generated method stub
		return qDasDao.findOne(id);
	}

	@Override
	public List<MesQdas> findAll() {
		// TODO Auto-generated method stub
		return qDasDao.findAll();
	}


}
