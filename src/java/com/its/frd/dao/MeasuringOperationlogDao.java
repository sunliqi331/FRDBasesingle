package com.its.frd.dao;

import com.its.frd.entity.MeasuringOperationlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface MeasuringOperationlogDao extends
				JpaRepository<MeasuringOperationlog, Long>,
				JpaSpecificationExecutor<MeasuringOperationlog>{

}
