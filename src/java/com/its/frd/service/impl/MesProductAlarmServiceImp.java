package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesProductAlarmDao;
import com.its.frd.entity.MesProductAlarm;
import com.its.frd.service.MesProductAlarmService;
@Service
public class MesProductAlarmServiceImp implements MesProductAlarmService {

	@Resource
	private MesProductAlarmDao mpaDao;

	@Override
	public List<MesProductAlarm> findPage(Specification<MesProductAlarm> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProductAlarm> springDataPage = mpaDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesProductAlarm findById(Long id) {
		return mpaDao.findOne(id);
	}

	@Override
	public void saveOrUpdate(MesProductAlarm mesProductAlarm) {
	    mpaDao.save(mesProductAlarm);
	}

	@Override
	public void deleteById(Long id) {
	    mpaDao.delete(id);
	}

	@Override
	public List<MesProductAlarm> findByMesProcedurePropertyId(Long propertyId) {
		return mpaDao.findByMesProcedurePropertyId(propertyId);
	}

	@Override
	public List<MesProductAlarm> findAll(Specification<MesProductAlarm> specification) {
		return mpaDao.findAll(specification);
	}

	@Override
	public List<MesProductAlarm> findTen(List<Long> ids2) {
		return mpaDao.findLimitTen(ids2);
	}

	@Override
	public List<MesProductAlarm> findByMesPointsId(Long pointid) {
		return mpaDao.findByPointid(pointid);
	}


}
