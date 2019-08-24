package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesCompanyRole;

public interface MesCompanyRoleService {
	MesCompanyRole get(Long id);

	void saveOrUpdate(MesCompanyRole role);

	void delete(Long id);
	
	List<MesCompanyRole> findAll(Page page);
	
	List<MesCompanyRole> findByExample(Specification<MesCompanyRole> specification, Page page);
	
	public abstract MesCompanyRole findById(Long id);
	
	List<MesCompanyRole> findByCompanyid(Long companyid);
}
