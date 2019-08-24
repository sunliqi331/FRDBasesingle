package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesSpcMonitorConfig;
import com.its.frd.entity.MesSpcMonitorMonitorConfig;

public interface MesSpcMonitorMonitorConfigDao extends JpaRepository<MesSpcMonitorMonitorConfig, Long>, JpaSpecificationExecutor<MesSpcMonitorMonitorConfig>{

}
