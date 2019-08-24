package com.its.common.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.OrganizationRole;
import com.its.common.util.dwz.Page;

public interface OrganizationRoleService {
	OrganizationRole get(Long id);

	void saveOrUpdate(OrganizationRole organizationRole);

	void delete(Long id);
	
	List<OrganizationRole> findAll(Page page);
	
	List<OrganizationRole> findByExample(Specification<OrganizationRole> specification, Page page);
	
	/**
	 * 根据organizationId，找到已分配的角色。
	 * 描述
	 * @param organizationId
	 * @return
	 */
	List<OrganizationRole> findByOrganizationId(Long organizationId);
}
