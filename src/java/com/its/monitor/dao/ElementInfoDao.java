package com.its.monitor.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.ElementInfo;


public interface ElementInfoDao extends JpaRepository<ElementInfo, Long>, JpaSpecificationExecutor<ElementInfo> {

}
