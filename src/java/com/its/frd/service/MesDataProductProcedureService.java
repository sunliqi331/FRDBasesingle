package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDataMultiKey;
import com.its.frd.entity.MesDataProductProcedure;

public interface MesDataProductProcedureService extends BaseService<MesDataProductProcedure> {
    
    public void save(MesDataProductProcedure MesDataProductProcedure);
	
	public MesDataProductProcedure findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesDataProductProcedure> findPage(Specification<MesDataProductProcedure> specification,Page page);
	
	public List<MesDataProductProcedure> findAll(Specification<MesDataProductProcedure> specification);
	
	public void delByRowKey(MesDataMultiKey mesDataMultiKey);

	List<Object[]> findDataProductByPRODUCT_MODE(String modelnum);
}
