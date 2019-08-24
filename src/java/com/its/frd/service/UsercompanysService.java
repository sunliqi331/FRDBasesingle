package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.Usercompanys;

public interface UsercompanysService {
	public List<Usercompanys> findPage(Specification<Usercompanys> specification,Page page); 
	public void saveOrUpdateInfo(Usercompanys info);
	public void deleteinfoByUserIdAndCompanyId(Long userid,Long companyid);
	public Usercompanys findById(Long id);
	public List<Usercompanys> findPage(Page page);
	
	public List<Usercompanys> findByUserid(Long userid);
	public List<Usercompanys> findByCompanyid(Long companyid);
	
	public void deleteCompanyInfoByUserId(Long userId);
}
