package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesPoints;

public interface MesPointsDao extends 
				JpaRepository<MesPoints, Long>,
				JpaSpecificationExecutor<MesPoints>{
	
    @Query("FROM MesPoints WHERE mesPointType.id=12 AND id IN (SELECT mesPoints.id FROM MesDriverPoints WHERE mesDriver.id In(?1))")
    public List<MesPoints> findPointsByDefineParam(List<Long> driverIds);
    
    @Query("FROM MesPoints WHERE mesPointGateway.id=?1 AND codekey=?2 ")
    public List<MesPoints> checkCodekeyNew(Long id,String codekey);
}
