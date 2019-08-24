package com.its.frd.service;


import java.util.List;

import com.its.frd.entity.MesUserPosition;

public interface MesUserPositionService extends BaseService<MesUserPosition> {
	
	public MesUserPosition findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesUserPosition> findByUseridAndCompanyId(Long userid,Long companyId);
	
	public void deleteinfoByUserIdAndCompanyId(Long userid,Long companyid);
	
}
