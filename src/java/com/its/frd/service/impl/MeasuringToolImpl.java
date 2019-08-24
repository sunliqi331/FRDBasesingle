package com.its.frd.service.impl;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MeasuringToolDao;
import com.its.frd.entity.MeasuringTool;
import com.its.frd.service.MeasuringToolService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MeasuringToolImpl implements MeasuringToolService {

	@Resource
	private MeasuringToolDao dao;


	@Override
	public List<MeasuringTool> findPage(Page page) {
		return null;
	}

	@Override
	public List<MeasuringTool> findPage(Specification<MeasuringTool> specification, Page page) {
		if(StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderField("id");
		}
		org.springframework.data.domain.Page<MeasuringTool> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MeasuringTool saveOrUpdate(MeasuringTool measuringTool) {
		return dao.save(measuringTool);
	}


	@Override
	public List<MeasuringTool> findAllByIsdelete(Integer isdelete) {
		return dao.findAllByIsdelete(isdelete);
	}

	@Override
	public MeasuringTool findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public List<MeasuringTool> findBySnAndIsdelete(String sn, int i) {
		return dao.findBySnAndIsdelete(sn,i);
	}

	@Override
	public void deleteAll() {
		dao.deleteAll();
	}

	@Override
	public void deleteAllBySpcsite(Integer spcsite) {
		dao.deleteAllBySpcsite(spcsite);
	}
}
