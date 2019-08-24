package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesSpcMonitorConfig;

public interface MesSpcMonitorConfigDao extends JpaRepository<MesSpcMonitorConfig, Long>, JpaSpecificationExecutor<MesSpcMonitorConfig>{

}
