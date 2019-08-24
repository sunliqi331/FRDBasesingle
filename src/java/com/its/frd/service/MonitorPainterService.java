package com.its.frd.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.its.frd.entity.MonitorPainter;

@Service
public interface MonitorPainterService {

	public List<MonitorPainter> findByUserId(long id);
	
	public MonitorPainter findById(long id);
	
	public List<MonitorPainter> findByIdIn(Collection<Long> list,long companyId);
	
	public void saveMonitorPainter(MonitorPainter monitorPainter);

	public MonitorPainter findByNameAndCompanyId(String name, long companyId);
	
	public MonitorPainter findByIdAndCompanyId(String name, long companyId);
	
	/**
	 * 获取数据库最后一条记录的id
	 * @return
	 */
	public String findLastRecordId();
}
