package com.its.frd.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.Companyfile;

public interface CompanyfileDao extends 
				JpaRepository<Companyfile, Long>,
				JpaSpecificationExecutor<Companyfile>{
	
	public List<Companyfile> findByParentidAndParenttypeOrderByIdAsc(Long companyid,String type);
	
}
