package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesProcedurePropertyPointLog;

public interface MesProcedurePropertyPointLogDao extends 
				JpaRepository<MesProcedurePropertyPointLog, Long>,
				JpaSpecificationExecutor<MesProcedurePropertyPointLog>{
	
	@Query(value="SELECT t1.* FROM mes_procedure_property_point_log AS t1 ,"
			+ "(SELECT t.id, MAX(t.createdate) FROM mes_procedure_property_point_log t WHERE t.propertyid = ?1 ) AS t2 "
			+ "WHERE t1.id = t2.id",nativeQuery=true)
	public MesProcedurePropertyPointLog findLastedLogByPropertyId(long id);
	
	@Query(value="SELECT t1.* FROM mes_procedure_property_point_log AS t1 ,"
			+ "(SELECT t.id, MAX(t.createdate) FROM mes_procedure_property_point_log t WHERE t.pointid = ?1 ) AS t2 "
			+ "WHERE t1.id = t2.id",nativeQuery=true)
	public MesProcedurePropertyPointLog findLastedLogByPointId(long id);
}
