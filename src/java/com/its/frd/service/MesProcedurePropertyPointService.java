package com.its.frd.service;

import java.util.List;
import com.its.frd.entity.MesProcedurePropertyPoint;

public interface MesProcedurePropertyPointService extends BaseService<MesProcedurePropertyPoint> {
	
	public MesProcedurePropertyPoint findById(Long propertyId);
	
	public void deleteById(Long propertyId);
	
	public List<MesProcedurePropertyPoint> findByMesPointId(Long pointId);
}
