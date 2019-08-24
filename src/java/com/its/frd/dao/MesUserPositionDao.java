package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesUserPosition;
public interface MesUserPositionDao extends JpaRepository<MesUserPosition, Long>,JpaSpecificationExecutor<MesUserPosition>{
	
	@Query("FROM MesUserPosition M WHERE M.userid=?1 AND M.mesCompanyPosition.companyinfo.id=?2")
	public List<MesUserPosition> findByUseridAndCompanyId(Long userid,Long companyId);
}
