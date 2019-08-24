package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.its.frd.entity.MesAlarmRelation;

public interface MesAlarmRelationDao extends 
				JpaRepository<MesAlarmRelation, Long>,
				JpaSpecificationExecutor<MesAlarmRelation>{

	public List<MesAlarmRelation> findByMesPointsId(Long id);
	
}
