package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.its.frd.entity.MonitorPainterUser;

public interface MonitorPainterUserDao extends JpaRepository<MonitorPainterUser, Long>, JpaSpecificationExecutor<MonitorPainterUser> {
	public List<MonitorPainterUser> findByUserId(Long id);
	public List<MonitorPainterUser> monitorPainterId(Long id);
	@Modifying
	@Query("delete from MonitorPainterUser u where u.monitorPainterId=?1 ")
	public void deleteByMonitorPainterId(long painterId);
	
	@Modifying
	@Query("delete from MonitorPainterUser u where u.monitorPainterId=?1 and u.userId =?2")
	public void deleteMonitorPainterByPainterIdAndUserId(long monitorPainterId,long userId);
}
