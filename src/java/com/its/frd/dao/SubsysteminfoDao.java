package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.Subsysteminfo;

public interface SubsysteminfoDao extends 
				JpaRepository<Subsysteminfo, Long>,
				JpaSpecificationExecutor<Subsysteminfo>{
}
