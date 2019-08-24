package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesSubdriver;

public interface MesSubdriverDao extends 
				JpaRepository<MesSubdriver, Long>,
				JpaSpecificationExecutor<MesSubdriver>{
	
	
	
}
