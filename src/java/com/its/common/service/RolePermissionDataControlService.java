package com.its.common.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.RolePermissionDataControl;
import com.its.common.util.dwz.Page;

public interface RolePermissionDataControlService {
	RolePermissionDataControl get(Long id);

	void saveOrUpdate(RolePermissionDataControl rolePermissionDataControl);

	void delete(Long id);
	
	List<RolePermissionDataControl> findAll(Page page);
	
	List<RolePermissionDataControl> findByExample(Specification<RolePermissionDataControl> specification, Page page);

	/**
	 * @param newRList
	 */
	void save(List<RolePermissionDataControl> newRList);

	/**
	 * @param delRList
	 */
	void delete(List<RolePermissionDataControl> delRList);

	/**
	 * @param id
	 * @return
	 */
	List<RolePermissionDataControl> findByRolePermissionRoleId(Long id);
}
