package com.its.frd.service;

import java.sql.Timestamp;
import java.util.List;

import com.its.frd.entity.MesProduction;

public interface MesProductionService {
	
	/**
	 * 根据设备id，产品类型，查找指定时间段内的生产记录
	 * @param startDate //记录开始时间
	 * @param endDate  //记录结束时间
	 * @param driverId //设备id
	 * @param productnum  //产品类型
	 * @return
	 */
	public List<MesProduction> findByTimeSpan(Timestamp startDate,Timestamp endDate,Long driverId,String productnum);
	
}
