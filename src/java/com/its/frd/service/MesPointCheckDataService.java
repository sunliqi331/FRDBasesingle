package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.MesPointCheckData;

public interface MesPointCheckDataService extends BaseService<MesPointCheckData>{
	
    public void deleteById(Long id);
    
    public List<MesPointCheckData> findByMesDriverPointsIdAndCheckvalue(Long mesDriverPointsId,Long checkvalue);
    
    public List<MesPointCheckData> findByMesDriverPointsIdAndName(Long mesDriverPointsId,String name);
    
    public List<MesPointCheckData> findByPointsId(List<Long> ids);
    
    public MesPointCheckData findOne(Long id);
}
