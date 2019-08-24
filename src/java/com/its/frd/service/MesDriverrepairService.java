package com.its.frd.service;

import com.its.frd.entity.MesDriverrepair;

public interface MesDriverrepairService extends BaseService<MesDriverrepair> {
	
	public MesDriverrepair findById(Long id);
	
	public void deleteById(Long id);
}
