package com.its.frd.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MonitorPainter;

public interface MonitorPainterDao extends JpaRepository<MonitorPainter, Long>,JpaSpecificationExecutor<MonitorPainter> {
	public List<MonitorPainter> findByUserId(long id);

	public List<MonitorPainter> findByIdIn(Collection<Long> list);
	
	public List<MonitorPainter> findByNameAndCompanyId(String name, long companyId);
 
	public List<MonitorPainter> findByIdAndCompanyId(String id, long companyId);
	
	@Query("FROM MonitorPainter M order by M.id desc")
	public List<MonitorPainter> findLastRecordId();

	@Query("FROM MonitorPainter where spcAnalysisData is not null and spcAnalysisData <>'' and spcAnalysisData <>'null' ")
	public List<MonitorPainter> findBySpcAnalysisDataNotNull();
}
