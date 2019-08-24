package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.frd.entity.MesQdasCategory;

public interface QdasCategoryService extends BaseService<MesQdasCategory> {
	List<MesQdasCategory> findAll();

	MesQdasCategory findById(long id);
	
	List<MesQdasCategory> findAll(Specification<MesQdasCategory> specification);
}
