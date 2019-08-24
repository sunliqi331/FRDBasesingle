package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesDriverrepairDao;
import com.its.frd.entity.MesDriverrepair;
import com.its.frd.service.MesDriverrepairService;
@Service
public class MesDriverrepairImp implements MesDriverrepairService {
	@Resource
	private MesDriverrepairDao dao;
	
	
	@Override
	public MesDriverrepair findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesDriverrepair> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverrepair> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDriverrepair> findPage(Specification<MesDriverrepair> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverrepair> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDriverrepair saveOrUpdate(MesDriverrepair t) {
		return dao.save(t);
	}

}
