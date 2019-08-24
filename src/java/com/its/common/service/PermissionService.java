package com.its.common.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.Permission;
import com.its.common.util.dwz.Page;

public interface PermissionService {
	Permission get(Long id);

	void saveOrUpdate(Permission permission);

	void delete(Long id);
	
	List<Permission> findAll(Page page);
	
	List<Permission> findByExample(Specification<Permission> specification, Page page);
	
	public List<Permission> findBySnAndModuleId(String sn,Long moduleId);
}
