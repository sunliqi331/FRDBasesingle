package com.its.frd.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDataDriverProperty;

public interface MesDataDriverPropertyService extends BaseService<MesDataDriverProperty> {
    
    public void save(MesDataDriverProperty MesDataDriverProperty);
	
	public MesDataDriverProperty findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesDataDriverProperty> findPage(Specification<MesDataDriverProperty> specification,Page page);
	
	public List<MesDataDriverProperty> findAll(Specification<MesDataDriverProperty> specification);
	
	   public List<MesDataDriverProperty> findAll(Specification<MesDataDriverProperty> specification, Sort args);
	
	public List<MesDataDriverProperty> findAllOrderByInsertTimestamp(Specification<MesDataDriverProperty> specification);
}
