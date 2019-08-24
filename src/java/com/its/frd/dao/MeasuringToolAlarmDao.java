package com.its.frd.dao;

import com.its.frd.entity.MeasuringToolAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MeasuringToolAlarmDao extends
				JpaRepository<MeasuringToolAlarm, Long>,
				JpaSpecificationExecutor<MeasuringToolAlarm>{

	@Query(value="SELECT * FROM measuringtoolalarm ma  JOIN measuringtool m ON ma.measuring_tool_id = m.id  WHERE m.mes_product_procedure_id in ?1 order by ma.id desc limit ?2",nativeQuery=true)
	List<MeasuringToolAlarm> getDataWithMesProductProcedureIdsAndLine(List<Long> mesProductProcedureIds, Long line);

	@Modifying
	@Query(value="DELETE t1 FROM measuringtoolalarm t1 LEFT JOIN measuringtool t2 ON t1.measuring_tool_id = t2.id WHERE t2.id = ?1",nativeQuery=true)
	void deleteByMeasuringToolId(Long id);

	@Modifying
	@Query(value="DELETE t1 FROM measuringtoolalarm t1 LEFT JOIN measuringtool t2 ON t1.measuring_tool_id = t2.id WHERE t2.spcsite = ?1",nativeQuery=true)
	void deleteAllBySpcsite(Integer spcsite);

	List<MeasuringToolAlarm> findByMeasuringToolId(Long id);

	@Query(value="SELECT * FROM measuringtoolalarm ma order by ma.id desc limit ?1",nativeQuery=true)
	List<MeasuringToolAlarm> getDataWithByLine(Long line);
}
