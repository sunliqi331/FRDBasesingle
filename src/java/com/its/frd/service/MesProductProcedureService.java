package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesProductProcedure;

public interface MesProductProcedureService extends BaseService<MesProductProcedure> {
	
	public MesProductProcedure findById(Long id);
	
	public MesProductProcedure findByprocedurenum(String procedurenum);
	
	public void deleteById(Long id);
	
	public List<MesProductProcedure> findPage(Specification<MesProductProcedure> specification, Page page);

	List<MesProductProcedure> findByMesProductIdIn(List<Long> mesProductListIds);

	List<MesProductProcedure> findAll();
}
