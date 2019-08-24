package com.its.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.common.entity.main.LogInfo;

public interface LogInfoDAO extends JpaRepository<LogInfo, Long>, JpaSpecificationExecutor<LogInfo> {

}