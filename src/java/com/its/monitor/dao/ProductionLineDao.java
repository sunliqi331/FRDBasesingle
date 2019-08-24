package com.its.monitor.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.ProductionLine;


public interface ProductionLineDao extends JpaRepository<ProductionLine, Long>, JpaSpecificationExecutor<ProductionLine> {
	
	//public void insert();
}
