package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesProcedurePropertyPointDao;
import com.its.frd.entity.MesProcedurePropertyPoint;
import com.its.frd.service.MesProcedurePropertyPointService;
@Service
public class MesProcedurePropertyPointImp implements MesProcedurePropertyPointService {
	@Resource
	private MesProcedurePropertyPointDao dao;
	
	@Override
	public List<MesProcedurePropertyPoint> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProcedurePropertyPoint> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesProcedurePropertyPoint> findPage(Specification<MesProcedurePropertyPoint> specification, Page page) {
		page.setOrderField("propertyid");
		org.springframework.data.domain.Page<MesProcedurePropertyPoint> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesProcedurePropertyPoint saveOrUpdate(MesProcedurePropertyPoint t) {
		return dao.save(t);
	}

	@Override
	public MesProcedurePropertyPoint findById(Long propertyId) {
		return dao.findOne(propertyId);
	}

	@Override
	public void deleteById(Long propertyId) {
		//dao.delete(propertyId);
		dao.deleteMesProcedurePropertyPointById(propertyId);
	}

	@Override
	public List<MesProcedurePropertyPoint> findByMesPointId(Long pointId) {
		return dao.findByMesPointId(pointId);
	}
}
