package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesSpcMonitor;

public interface MesSpcMonitorDao extends JpaRepository<MesSpcMonitor, Long>, JpaSpecificationExecutor<MesSpcMonitor> {

}
