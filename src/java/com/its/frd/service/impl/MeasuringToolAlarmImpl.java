package com.its.frd.service.impl;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MeasuringToolAlarmDao;
import com.its.frd.entity.MeasuringToolAlarm;
import com.its.frd.service.MeasuringToolAlarmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MeasuringToolAlarmImpl implements MeasuringToolAlarmService {
	@Resource
	private MeasuringToolAlarmDao measuringToolAlarmDao;

	@Override
	public List<MeasuringToolAlarm> findPage(Page page) {
		return null;
	}

	@Override
	public List<MeasuringToolAlarm> findPage(Specification<MeasuringToolAlarm> specification, Page page) {
		if(StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderField("id");
		}
		org.springframework.data.domain.Page<MeasuringToolAlarm> springDataPage = measuringToolAlarmDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MeasuringToolAlarm saveOrUpdate(MeasuringToolAlarm measuringToolAlarm) {
		return measuringToolAlarmDao.save(measuringToolAlarm);
	}

	@Override
	public List<MeasuringToolAlarm> getDataWithMesProductProcedureIdsAndLine(List<Long> mesProductProcedureIds, Long line) {
		return measuringToolAlarmDao.getDataWithMesProductProcedureIdsAndLine(mesProductProcedureIds,line);
	}

	@Override
	public List<MeasuringToolAlarm> getDataWithByLine(Long line) {
		return measuringToolAlarmDao.getDataWithByLine(line);
	}



	@Override
	public List<MeasuringToolAlarm> findByMeasuringToolId(Long id) {
		return measuringToolAlarmDao.findByMeasuringToolId(id);
	}

	@Override
	public void deleteByMeasuringToolId(Long id) {
		measuringToolAlarmDao.deleteByMeasuringToolId(id);
	}

	@Override
	public void deleteAllBySpcsite(Integer spcsite) {
		measuringToolAlarmDao.deleteAllBySpcsite(spcsite);
	}

	@Override
	public void deleteAll() {
		measuringToolAlarmDao.deleteAll();
	}

}
