package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;

public interface CompanyinfoService extends BaseService<Companyinfo>{
	public List<Companyinfo> findPage(Page page);
	
	public void deleteById(Long id);
	
	public void updateStatus(Long id,String status);
	
	public List<Companyinfo> findAll();
	
	public Companyinfo findById(Long id);
	
	public Companyinfo saveAndFile(Companyinfo companyinfo,List<Companyfile> companyfilelist);
	
	public List<Companyinfo> findByUserid(Long userid);
	
	public Companyinfo findByCompanyname(String companyname);
	
	public List<Companyinfo> findPage2();
	
	public List<Companyinfo> findByParentidAndCompanytype(Long parentid,String companytype);
	
	public List<Companyinfo> findByCompanytypeAndCompanystatus(String companytype,String companystatus);
	
//	public Companyinfo getTree();
	
	public List<Companyinfo> getTreeFactory();
	
	public Companyinfo getCompanyTree();
	
	public List<Companyinfo> findByParentId(Long parentId,String companystatus,String companytype);
	
	public void saveOrUpdateForfile(Companyinfo info,List<Companyfile> files,String[] pictureids);
	
	public List<Companyinfo> findByIdInAndCompanystatusAndCompanytype(List<Long> ids,String status,String type);
	
}
