package com.its.frd.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesPointsDao;
import com.its.frd.entity.MesPoints;
import com.its.frd.service.MesPointsService;

@Service
public class MesPointsServiceImp implements MesPointsService {
	@Resource
	private MesPointsDao dao;
	
	@Override
	public List<MesPoints> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPoints> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesPoints> findPage(Specification<MesPoints> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPoints> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesPoints findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public MesPoints saveOrUpdate(MesPoints point) {
		return dao.save(point);
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		dao.delete(id);
	}

	@Override
	public List<MesPoints> findPointsByPointsId(List<Long> driverIds) {
		if(driverIds == null || driverIds.size() <= 0)
			return null;
		return dao.findPointsByDefineParam(driverIds);
	}

	@Override
	public List<MesPoints> checkCodekeyNew(Long id,String codekey) {
		List<MesPoints> springDataPage = dao.checkCodekeyNew(id,codekey);
		return springDataPage;
	}

	@Override
	public void batchSaveOrUpdate(List<MesPoints> pointList) {
		// TODO Auto-generated method stub
		dao.save(pointList);
		
	}
}
