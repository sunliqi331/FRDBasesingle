package com.its.common.dao.component;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.common.entity.component.Resource;

public interface ResourceDAO extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource>{
	Resource getByUuid(String uuid);
}
