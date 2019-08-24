package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesCompanyRole;

public interface MesCompanyRoleDao extends JpaRepository<MesCompanyRole, Long>,
			JpaSpecificationExecutor<MesCompanyRole>{
    
    List<MesCompanyRole> findByCompanyid(Long companyid);

}
