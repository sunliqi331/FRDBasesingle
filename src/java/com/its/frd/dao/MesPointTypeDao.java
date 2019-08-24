package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesPointType;

public interface MesPointTypeDao extends 
				JpaRepository<MesPointType, Long>,
				JpaSpecificationExecutor<MesPointType>{

	

	
}
