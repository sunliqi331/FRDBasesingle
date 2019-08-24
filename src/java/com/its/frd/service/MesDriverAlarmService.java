package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDriverAlarm;

public interface MesDriverAlarmService {
    
	public List<MesDriverAlarm> findPage(Specification<MesDriverAlarm> specification,Page page);
	
	public MesDriverAlarm findById(Long id);
	
	public void saveOrUpdate(MesDriverAlarm mesDriverAlarm);
	
	public void deleteById(Long id);
	
	public List<MesDriverAlarm> findByMesPointsId(Long mesPointsId);
	
	public List<MesDriverAlarm> findByMesDriverId(Long mesDriverId);
	
	void delete(List<MesDriverAlarm> mesDriverAlarms);

	List<MesDriverAlarm> findTen(List<Long> driverId);
	
}
