package com.its.frd.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDriverOEE;

/**
 * 设备OEE数据访问层
 * @author pactera
 *
 */
public interface MesDriverOEEDao extends 
				JpaRepository<MesDriverOEE, Long>,
				JpaSpecificationExecutor<MesDriverOEE> {
	
	/**
	 * 根据设备id，时间间隔，班次，检索设备oee历史记录
	 * @param start
	 * @param end
	 * @param driverId
	 * @param classes
	 * @return
	 */
	@Query("FROM MesDriverOEE M WHERE M.createDate>?1 AND M.createDate<?2 AND M.mesDriver.id=?3 AND M.classes=?4 order by M.id desc")
	List<MesDriverOEE> findByTimeAndClasses(Date start, Date end, Long driverId ,String classes);
	
	/**
	 * 根据设备id，时间间隔检索设备oee历史记录
	 * @param start
	 * @param end
	 * @param driverId
	 * @return
	 */
	@Query("FROM MesDriverOEE M WHERE M.createDate>?1 AND M.createDate<?2 AND M.mesDriver.id=?3 order by M.id desc")
	List<MesDriverOEE> findByTime(Date start, Date end, Long driverId);
	
}
