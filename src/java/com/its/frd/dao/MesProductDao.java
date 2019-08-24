package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesProduct;

public interface MesProductDao extends 
				JpaRepository<MesProduct, Long>,
				JpaSpecificationExecutor<MesProduct>{
	
	public List<MesProduct> findByCompanyinfo(Companyinfo companyinfo);

	public MesProduct findByProductnum(String productnum);
	
	public MesProduct findByModelnumAndCompanyinfoId(String modelnum,Long companyId);
	
	public List<MesProduct> findByCompanyinfoAndName(Companyinfo companyinfo, String productName);
	
	public MesProduct findBycompanyinfoIdAndModelnum(Long companyId, String modelnum);
}
