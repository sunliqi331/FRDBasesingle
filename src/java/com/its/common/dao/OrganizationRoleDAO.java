package com.its.common.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.common.entity.main.OrganizationRole;

public interface OrganizationRoleDAO extends JpaRepository<OrganizationRole, Long>, JpaSpecificationExecutor<OrganizationRole> {
	List<OrganizationRole> findByOrganizationId(Long organizationId);
}