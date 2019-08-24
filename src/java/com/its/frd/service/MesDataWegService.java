package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDataWeg;

public interface MesDataWegService extends BaseService<MesDataWeg> {
    
    public void save(MesDataWeg MesDataWeg);
	
	public MesDataWeg findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesDataWeg> findPage(Specification<MesDataWeg> specification,Page page);
	
	public List<MesDataWeg> findAll(Specification<MesDataWeg> specification);
}
