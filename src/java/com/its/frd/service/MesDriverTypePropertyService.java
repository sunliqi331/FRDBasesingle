package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.MesDrivertypeProperty;

public interface MesDriverTypePropertyService extends BaseService<MesDrivertypeProperty> {
	
	public MesDrivertypeProperty findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesDrivertypeProperty> findByMesDrivertypeId(Long MesDrivertypeId);
}
