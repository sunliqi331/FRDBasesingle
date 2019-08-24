package com.its.frd.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.its.frd.entity.MesDriverAlarm;

public interface MesDriverAlarmDao extends 
				JpaRepository<MesDriverAlarm, Long>,
				JpaSpecificationExecutor<MesDriverAlarm>{
    
    @Query("from MesDriverAlarm M order by M.updatetime DESC")
    List<MesDriverAlarm> findAllWithCache();
    
    public List<MesDriverAlarm> findByMesPointsId(Long mesPointsId);
    
    public List<MesDriverAlarm> findByMesDriverId(Long mesDriverId);
    
    @Query(value="select * from mes_driver_alarm where driverid in ?1 order by id desc limit 10",nativeQuery=true)
    public List<MesDriverAlarm> findLimitTen(List<Long> driverId);
}
