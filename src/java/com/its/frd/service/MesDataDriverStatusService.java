package com.its.frd.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDataDriverStatus;

public interface MesDataDriverStatusService extends BaseService<MesDataDriverStatus> {
    
    public void save(MesDataDriverStatus MesDataDriverStatus);
	
	public MesDataDriverStatus findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesDataDriverStatus> findPage(Specification<MesDataDriverStatus> specification,Page page);
	
	public List<MesDataDriverStatus> findAll(Specification<MesDataDriverStatus> specification);
	public List<MesDataDriverStatus> findAll(Specification<MesDataDriverStatus> specification, Sort arg);
}
