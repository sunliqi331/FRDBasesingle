package com.its.frd.service.impl;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MeasuringOperationlogDao;
import com.its.frd.entity.MeasuringOperationlog;
import com.its.frd.entity.MeasuringTool;
import com.its.frd.service.MeasuringOperationlogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MeasuringOperationlogImpl implements MeasuringOperationlogService {

	@Resource
	private MeasuringOperationlogDao dao;


	@Override
	public List<MeasuringOperationlog> findPage(Page page) {
		return null;
	}

	@Override
	public List<MeasuringOperationlog> findPage(Specification<MeasuringOperationlog> specification, Page page) {
		if(StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderField("id");
		}
		org.springframework.data.domain.Page<MeasuringOperationlog> springDataPage = dao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MeasuringOperationlog saveOrUpdate(MeasuringOperationlog measuringOperationlog) {
		return dao.save(measuringOperationlog);
	}
}
