package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesCompanyPosition;

public interface MesCompanyPositionDao extends JpaRepository<MesCompanyPosition, Long>,JpaSpecificationExecutor<MesCompanyPosition>{
	
	 @Query("FROM MesCompanyPosition M WHERE M.companyinfo.id=?1")
	public List<MesCompanyPosition> findByCompanyid(Long companyid);
}
