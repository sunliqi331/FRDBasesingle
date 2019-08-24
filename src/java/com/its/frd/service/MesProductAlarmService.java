package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesProductAlarm;

public interface MesProductAlarmService {
    
	public List<MesProductAlarm> findPage(Specification<MesProductAlarm> specification,Page page);
	
	public List<MesProductAlarm> findAll(Specification<MesProductAlarm> specification);
	
	public MesProductAlarm findById(Long id);
	
	public void saveOrUpdate(MesProductAlarm mesProductAlarm);
	
	public void deleteById(Long id);
	
	public List<MesProductAlarm> findByMesProcedurePropertyId(Long propertyId);

	public List<MesProductAlarm> findTen(List<Long> ids2);
	
	public List<MesProductAlarm> findByMesPointsId(Long pointid);
	
}
