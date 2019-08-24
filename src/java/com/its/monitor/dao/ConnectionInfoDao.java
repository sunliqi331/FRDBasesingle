package com.its.monitor.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.ConnectionInfo;


public interface ConnectionInfoDao extends JpaRepository<ConnectionInfo, Long>, JpaSpecificationExecutor<ConnectionInfo> {
	
	//public void insert();
}
