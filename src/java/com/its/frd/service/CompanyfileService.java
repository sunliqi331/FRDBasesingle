package com.its.frd.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;

public interface CompanyfileService {
	public List<Companyfile> findPage(Page page);
	public List<Companyfile> findPage(Specification<Companyfile> specification,Page page);
	public Companyfile findById(Long id);
	public List<Companyfile> findByParentidAndType(Long id,String type);
	public void deleteCompanyfileByParentidAndType(Long id,String type);
	public Companyfile saveOrUpdate(Companyfile file);
	public void deleteById(Long id);
}
