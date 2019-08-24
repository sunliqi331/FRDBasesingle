package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesProductAlarm;

public interface MesProductAlarmDao extends 
				JpaRepository<MesProductAlarm, Long>,
				JpaSpecificationExecutor<MesProductAlarm>{
    
	public List<MesProductAlarm> findByMesProcedurePropertyId(Long propertyId);
	
	@Query(value="select * from mes_product_alarm where procedurePropertyid in ?1 order by id desc limit 10",nativeQuery=true)			 
	public List<MesProductAlarm> findLimitTen(List<Long> ids2);
	
	public List<MesProductAlarm> findByPointid(Long pointid);
}	