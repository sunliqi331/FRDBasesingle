package com.its.common.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.LogInfo;
import com.its.common.util.dwz.Page;

public interface LogInfoService {
	LogInfo get(Long id);

	void saveOrUpdate(LogInfo logInfo);

	void delete(Long id);
	
	List<LogInfo> findAll(Page page);
	
	List<LogInfo> findByExample(Specification<LogInfo> specification, Page page);
}
