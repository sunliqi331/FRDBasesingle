package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesAlarmSendRelation;

public interface MesAlarmSendRelationDao extends JpaRepository<MesAlarmSendRelation, Long>,
	JpaSpecificationExecutor<MesAlarmSendRelation>{
	
	public List<MesAlarmSendRelation> findByCompanyid(Long companyid);
}
