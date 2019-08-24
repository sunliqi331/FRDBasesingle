package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;

public interface CompanyinfoDao extends 
				JpaRepository<Companyinfo, Long>,
				JpaSpecificationExecutor<Companyinfo>{
	
	public List<Companyinfo> findByUserid(Long userid);
	
	public Companyinfo findByCompanyname(String companyname);
	
	public List<Companyinfo> findByParentidAndCompanytype(Long parentid,String companytype);
	
	public List<Companyinfo> findByIdAndCompanytype(Long id,String companytype);

	public List<Companyinfo> findByCompanytypeAndCompanystatus(String companytype, String companystatus);
	
	public List<Companyinfo> findByParentid(Long parentid);
	
	@Query("FROM Companyinfo C WHERE C.parentid=?1 AND C.companystatus!=?2 AND C.companytype=?3")
	public List<Companyinfo> findByParentId(Long parentId,String companystatus,String companytype);

	public List<Companyinfo> findByIdInAndCompanystatusAndCompanytype(List<Long> ids,String status,String type);
}
