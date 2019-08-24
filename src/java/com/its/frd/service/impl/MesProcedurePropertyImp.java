package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesProcedurePropertyDao;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.service.MesProcedurePropertyService;
@Service
public class MesProcedurePropertyImp implements MesProcedurePropertyService {
	@Resource
	private MesProcedurePropertyDao dao;
	
	
	@Override
	public MesProcedureProperty findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesProcedureProperty> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProcedureProperty> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesProcedureProperty> findPage(Specification<MesProcedureProperty> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProcedureProperty> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesProcedureProperty saveOrUpdate(MesProcedureProperty t) {
		return dao.save(t);
	}

	@Override
	public List<MesProcedureProperty> findByMesProductProcedure(MesProductProcedure mesProductProcedure){
		return dao.findByMesProductProcedure(mesProductProcedure);
	}

}
