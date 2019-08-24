package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesPointTypeDao;
import com.its.frd.entity.MesPointType;
import com.its.frd.service.MesPointTypeService;

@Service
public class MesPointTypeServiceImp implements MesPointTypeService {
	@Resource
	private MesPointTypeDao dao;
	
	@Override
	public List<MesPointType> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPointType> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesPointType> findPage(Specification<MesPointType> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPointType> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesPointType findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public MesPointType saveOrUpdate(MesPointType point) {
		return dao.save(point);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesPointType> findAll() {
		return dao.findAll();
	}

	@Override
	public List<MesPointType> findAll(Specification<MesPointType> specification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MesPointType> findAllOrderByName() {
		// TODO Auto-generated method stub
		return dao.findAll(new Sort(Direction.DESC,"name"));
	}


}
