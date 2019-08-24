package com.its.frd.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesProduction;

public interface MesProductionDao extends JpaRepository<MesProduction, Long> ,JpaSpecificationExecutor<MesProduction>{

	
	public List<MesProduction> findByUpdatetimeBetween(Timestamp startDate,Timestamp endDate);
	
	public List<MesProduction> findByProductnumAndUpdatetimeBetween(String productnum,Timestamp startDate,Timestamp endDate);
	
	public List<MesProduction> findByMesDriverId(Long MesDriverid);
	
	/**
	 * 根据设备id，产品类型，查找指定时间段内的生产记录
	 * @param startDate
	 * @param endDate
	 * @param driverId
	 * @param productnum
	 * @return
	 */
	@Query("FROM MesProduction M WHERE M.updatetime>=?1 AND M.updatetime<=?2 AND M.mesDriver.id=?3 AND M.productnum=?4 order by M.id asc")
	public List<MesProduction> findByTimeSpan(Timestamp startDate, Timestamp endDate, Long driverId, String productnum);
	
}
