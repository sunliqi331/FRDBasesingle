package com.its.frd.dao;

import com.its.frd.entity.MeasuringTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MeasuringToolDao extends
				JpaRepository<MeasuringTool, Long>,
				JpaSpecificationExecutor<MeasuringTool>{

	List<MeasuringTool> findAllByIsdelete(Integer isdelete);

	List<MeasuringTool> findBySnAndIsdelete(String sn, int i);

	@Modifying
	@Query(value="DELETE m FROM measuringtool m WHERE  m.spcsite = ?1",nativeQuery=true)
	void deleteAllBySpcsite(Integer spcsite);
}
