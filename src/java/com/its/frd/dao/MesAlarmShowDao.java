package com.its.frd.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.its.frd.entity.MesAlarmShow;

public interface MesAlarmShowDao extends 
				JpaRepository<MesAlarmShow, Long>,
				JpaSpecificationExecutor<MesAlarmShow>{
	
	@Query(value="select * from mes_alarm_show where driverid in ?1 order by id desc limit 2000",nativeQuery=true)
	public List<MesAlarmShow> findNewTwoThousand(List<Long> driverIds);
	
	/**
	 * 查询最新的设备告警数据
	 * @param driverIds  设备id
	 * @param line  告警数据条数
	 * @return
	 */
	@Query(value="select * from mes_alarm_show where driverid in ?1 order by id desc limit ?2",nativeQuery=true)
	public List<MesAlarmShow> findNewData(List<Long> driverIds,Long line);
}