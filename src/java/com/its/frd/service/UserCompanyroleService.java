package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.UserCompanyrole;

public interface UserCompanyroleService {
	UserCompanyrole get(Long id);

	void saveOrUpdate(UserCompanyrole userRole);

	void delete(Long id);
	
	List<UserCompanyrole> findAll(Page page);
	
	List<UserCompanyrole> findByExample(Specification<UserCompanyrole> specification, Page page);
	
	List<UserCompanyrole> findByUserId(Long userId);
	
	void deleteByUserId(Long userId);

	List<UserCompanyrole> findByUserIdAndMesCompanyRoleCompanyid(Long id, Long companyid);

}
