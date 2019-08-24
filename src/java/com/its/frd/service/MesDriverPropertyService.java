package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.MesDriverProperty;

public interface MesDriverPropertyService extends BaseService<MesDriverProperty> {
	
	public MesDriverProperty findById(Long id);
	
	public MesDriverProperty findByPropertyname(String propertyname);
	
	public void deleteById(Long id);
	
    public List<MesDriverProperty> findByMesDriverid(Long mesDriverid);
    
    public MesDriverProperty findByMesDriverAndPropertyname(String mesDriverName,String propertyname);

}
