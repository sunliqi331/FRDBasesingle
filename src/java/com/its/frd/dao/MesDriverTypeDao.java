package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDrivertype;

public interface MesDriverTypeDao extends 
				JpaRepository<MesDrivertype, Long>,
				JpaSpecificationExecutor<MesDrivertype>{
	
    @Query("FROM MesDrivertype M WHERE M.companyinfo.id=?1 AND M.typename=?2")
   public List< MesDrivertype> findByCompanyinfoidAndTypename(Long companyid,String typename);
}
