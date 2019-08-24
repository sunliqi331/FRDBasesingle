package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesProcedurePropertyPointLogDao;
import com.its.frd.entity.MesProcedurePropertyPointLog;
import com.its.frd.service.MesProcedurePropertyPointLogService;
@Service
public class MesProcedurePropertyPointLogImp implements MesProcedurePropertyPointLogService {
	@Resource
	private MesProcedurePropertyPointLogDao dao;
	
	@Override
	public List<MesProcedurePropertyPointLog> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProcedurePropertyPointLog> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesProcedurePropertyPointLog> findPage(Specification<MesProcedurePropertyPointLog> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProcedurePropertyPointLog> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesProcedurePropertyPointLog saveOrUpdate(MesProcedurePropertyPointLog t) {
		return dao.save(t);
	}

	@Override
	public MesProcedurePropertyPointLog findLastedLogByPropertyId(long id) {
		// TODO Auto-generated method stub
		return dao.findLastedLogByPropertyId(id);
	}

	@Override
	public MesProcedurePropertyPointLog findLastedLogByPointId(long id) {
		// TODO Auto-generated method stub
		return dao.findLastedLogByPointId(id);
	}
}
