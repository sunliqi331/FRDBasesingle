package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesUserPositionDao;
import com.its.frd.entity.MesUserPosition;
import com.its.frd.service.MesUserPositionService;
@Service
public class MesUserPositionServiceImp implements MesUserPositionService {
	@Resource
	private MesUserPositionDao dao;
	
	
	@Override
	public MesUserPosition findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesUserPosition> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesUserPosition> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesUserPosition> findPage(Specification<MesUserPosition> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesUserPosition> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesUserPosition saveOrUpdate(MesUserPosition t) {
		return dao.save(t);
	}

	@Override
	public List<MesUserPosition> findByUseridAndCompanyId(Long userid, Long companyId) {
		return dao.findByUseridAndCompanyId(userid, companyId);
	}

	@Override
	public void deleteinfoByUserIdAndCompanyId(Long userid, Long companyid) {
		List<MesUserPosition> list = dao.findByUseridAndCompanyId(userid, companyid);
		dao.delete(list);
	}

}
