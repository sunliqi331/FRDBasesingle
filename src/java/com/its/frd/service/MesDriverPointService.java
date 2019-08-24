package com.its.frd.service;

import java.util.List;

import javax.persistence.Transient;

import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;

public interface MesDriverPointService extends BaseService<MesDriverPoints> {
	
	public MesDriverPoints findById(Long id);
	
	public void deleteById(Long id);
	
	public MesDriverPoints findByMesPointIdAndMesDriverId(Long mesPointId,Long mesDriverId);
	
	public List<MesDriverPoints> findByMesDriverId(Long mesDriverId);
	
	public MesDriverPoints findByMesPointsId(Long mesPointsId);
	
	public List<MesDriverPoints> findByMesDrivertypePropertyAndMesDriver(Long MesDrivertypePropertyId,Long mesDriverId);
	
	public List<MesDriverPoints> findByMesDriverIdAndMesDrivertypePropertyIdNotNull(Long mesDriverId);
	
	public List<MesDriverPoints> findByMesDriver(MesDriver mesDriver);
}
