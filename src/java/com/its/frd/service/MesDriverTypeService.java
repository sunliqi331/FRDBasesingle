package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.MesDrivertype;

public interface MesDriverTypeService extends BaseService<MesDrivertype> {
	
	public MesDrivertype findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesDrivertype> findPage(Specification<MesDrivertype> specification,Page page);
	
	public List<MesDrivertype> findAll(Specification<MesDrivertype> specification);
 	
	public void saveAndFile(MesDrivertype mesDrivertype, List<Companyfile> companyfilelist);
	
	public List<MesDrivertype> findByCompanyinfoidAndTypename(Long companyid,String typename);
}
