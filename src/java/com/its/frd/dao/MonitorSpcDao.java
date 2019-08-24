package com.its.frd.dao;

import com.its.frd.entity.MonitorSpc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonitorSpcDao extends JpaRepository<MonitorSpc, Long>, JpaSpecificationExecutor<MonitorSpc> {

//	public List<MonitorSpc> findBymonitorPainterIdAndChartId(Long monitorPainterId, String chartId);

	@Query(nativeQuery=true, value="select * from (select * FROM monitor_spc where monitor_painter_id=?1 and chart_id=?2  order by id desc limit 50) n order by id asc")
	public List<MonitorSpc> findBymonitorPainterIdAndChartId(Long monitorPainterId, String chartId);

//	@Query(nativeQuery=true, value="select * from (select * FROM monitor_spc where monitor_painter_id=?1 order by id desc limit 50) n order by id asc")
//	public List<MonitorSpc> findBymonitorPainterIdAndChartId(Long monitorPainterId);

	@Query(nativeQuery=true, value="select * FROM monitor_spc where mes_driver_id=?1 and product_procedure_id=?2 and driver_property_id=?3 and product_id=?4 order by id desc")
	List<MonitorSpc> findMonitorSpc(Long mesDriverId, long productProcedureId, long procedurePropertyId, long productId);
}
