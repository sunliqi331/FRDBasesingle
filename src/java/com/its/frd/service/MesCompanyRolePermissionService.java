package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesCompanyRolePermission;

public interface MesCompanyRolePermissionService {
	MesCompanyRolePermission get(Long id);

	void saveOrUpdate(MesCompanyRolePermission rolePermission);
	
	public void saveOrUpdates(List<MesCompanyRolePermission> rolePermission);

	void delete(Long id);
	
	List<MesCompanyRolePermission> findAll(Page page);
	
	List<MesCompanyRolePermission> findByExample(Specification<MesCompanyRolePermission> specification, Page page);

	List<MesCompanyRolePermission> findByCompanyRoleId(Long id);

	void save(List<MesCompanyRolePermission> newRList);
	
	void delete(List<MesCompanyRolePermission> delRList);
}
