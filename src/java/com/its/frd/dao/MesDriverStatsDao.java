package com.its.frd.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDriverStats;

public interface MesDriverStatsDao extends 
				JpaRepository<MesDriverStats, Long>,
				JpaSpecificationExecutor<MesDriverStats>{
	
	public List<MesDriverStats> findByMesDriverIdAndUpdatetimeBetween(Long driverId,Timestamp startDate,Timestamp endDate);
	
	public List<MesDriverStats> findByMesDriverIdInAndUpdatetimeBetween(List<Long> driverIds,Timestamp startDate,Timestamp endDate);
	
	/**
	 * 根据设备id、时间间隔查找设备产量，并按照id降序排列
	 * @param driverId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("FROM MesDriverStats M WHERE M.mesDriver.id=?1 AND M.updatetime>=?2 AND M.updatetime<=?3 order by M.id desc")
	public List<MesDriverStats> findByDriverIdAndTimeSpan(Long driverId,Timestamp startDate,Timestamp endDate);
}
