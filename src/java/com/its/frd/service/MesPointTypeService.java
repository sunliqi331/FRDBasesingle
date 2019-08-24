package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesPointType;

public interface MesPointTypeService {
	public List<MesPointType> findPage(Page page);
	public List<MesPointType> findPage(Specification<MesPointType> specification,Page page);
	public MesPointType findById(Long id);
	public MesPointType saveOrUpdate(MesPointType point);
	public void deleteById(Long id);
	public List<MesPointType> findAll();
	public List<MesPointType> findAll(Specification<MesPointType> specification);
	public List<MesPointType> findAllOrderByName();
}
