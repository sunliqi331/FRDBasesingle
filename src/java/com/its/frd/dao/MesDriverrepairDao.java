package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesDriverrepair;

public interface MesDriverrepairDao extends 
				JpaRepository<MesDriverrepair, Long>,
				JpaSpecificationExecutor<MesDriverrepair>{
	
	
	
}
