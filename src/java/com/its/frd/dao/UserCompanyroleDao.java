package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.UserCompanyrole;

public interface UserCompanyroleDao extends 
				JpaRepository<UserCompanyrole, Long>,
				JpaSpecificationExecutor<UserCompanyrole>{
	
	public List<UserCompanyrole> findByUserId(Long userId);
	
	public List<UserCompanyrole> findByUserIdAndMesCompanyRoleCompanyid(Long userId,Long companyRoleCompanyid);
	
	@Query("delete from UserCompanyrole s where s.userId = ?1")  
	public void deleteByUserId(Long userId);
}
