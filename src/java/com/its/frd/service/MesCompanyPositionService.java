package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.MesCompanyPosition;

public interface MesCompanyPositionService extends BaseService<MesCompanyPosition> {
	
	public MesCompanyPosition findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesCompanyPosition> findByCompanyid(Long comapnyid);
	
}
