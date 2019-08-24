package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesProcedurePropertyPoint;

public interface MesProcedurePropertyPointDao extends 
				JpaRepository<MesProcedurePropertyPoint, Long>,
				JpaSpecificationExecutor<MesProcedurePropertyPoint>{
	
	//public void delete(MesProcedurePropertyPoint mesProcedurePropertyPoint);
	@Query(value = "delete from mes_procedure_property_point where propertyid=?1 ", nativeQuery = true)
	@Modifying
	public void deleteMesProcedurePropertyPointById(Long propertyId);
	
	@Query("FROM MesProcedurePropertyPoint M WHERE M.mesPoints.id =?1")
	public List<MesProcedurePropertyPoint> findByMesPointId(Long pointId);
	
}
