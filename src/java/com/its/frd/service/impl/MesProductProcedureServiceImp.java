package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesProductProcedureDao;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.service.MesProductProcedureService;
@Service
public class MesProductProcedureServiceImp implements MesProductProcedureService {
	@Resource
	private MesProductProcedureDao dao;
	
	
	@Override
	public MesProductProcedure findById(Long id) {
		return dao.findOne(id);
		
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
		public List<MesProductProcedure> findPage(Page page) {
	    if(StringUtils.isEmpty(page.getOrderField())) {
	        page.setOrderField("id");
	    }
		org.springframework.data.domain.Page<MesProductProcedure> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesProductProcedure> findPage(Specification<MesProductProcedure> specification, Page page) {
        if(StringUtils.isEmpty(page.getOrderField())) {
            page.setOrderField("id");
        }
		org.springframework.data.domain.Page<MesProductProcedure> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesProductProcedure saveOrUpdate(MesProductProcedure t) {
		return dao.save(t);
	}

	@Override
	public MesProductProcedure findByprocedurenum(String procedurenum) {
		return dao.findByprocedurenum(procedurenum);
	}

	@Override
	public List<MesProductProcedure> findByMesProductIdIn(List<Long> mesProductListIds) {
		return dao.findByMesProductIdIn(mesProductListIds);
	}

	@Override
	public List<MesProductProcedure> findAll() {
		return dao.findAll();
	}


}
