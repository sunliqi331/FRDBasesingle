package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProductProcedure;

public interface MesProcedurePropertyService extends BaseService<MesProcedureProperty> {
	
	public MesProcedureProperty findById(Long id);
	
	public void deleteById(Long id);

	public List<MesProcedureProperty> findByMesProductProcedure(MesProductProcedure mesProductProcedure);
}
