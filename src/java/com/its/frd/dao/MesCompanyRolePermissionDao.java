package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesCompanyRolePermission;

public interface MesCompanyRolePermissionDao extends JpaRepository<MesCompanyRolePermission, Long>,
			JpaSpecificationExecutor<MesCompanyRolePermission>{

	List<MesCompanyRolePermission> findByMesCompanyRoleId(Long companyRoleId);
}
