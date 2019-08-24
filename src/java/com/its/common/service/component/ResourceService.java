package com.its.common.service.component;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.component.Resource;
import com.its.common.util.dwz.Page;


public interface ResourceService {
	Resource get(Long id);

	void saveOrUpdate(Resource resource);

	void delete(Long id);
	
	List<Resource> findAll(Page page);
	
	List<Resource> findByExample(Specification<Resource> specification, Page page);
	
	Resource getByUuid(String uuid);
}
