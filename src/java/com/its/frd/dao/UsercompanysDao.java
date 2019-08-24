package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.Usercompanys;

public interface UsercompanysDao extends JpaRepository<Usercompanys, Long>,JpaSpecificationExecutor<Usercompanys>{
	public List<Usercompanys> findByUseridAndCompanyinfoId(Long userid,Long companyid);
	
	public List<Usercompanys> findByUserid(Long userid);
	
	public List<Usercompanys> findByCompanyinfoId(Long companyinfoId);
	
	
	@Query("delete from Usercompanys s where s.userid = ?1")  
    void deleteByUserid(Long id);
}
