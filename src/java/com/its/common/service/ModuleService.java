package com.its.common.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.Module;
import com.its.common.util.dwz.Page;

public interface ModuleService {
	Module get(Long id);

	void saveOrUpdate(Module module);

	void delete(Long id);
	
	List<Module> findAll(Page page);
	
	List<Module> findByExample(Specification<Module> specification, Page page);
	
	Module getTree();
	
	List<Module> findAll();
	
	/**
	 * 根据userId查询相关数据
	 * @param userId
	 * @return
	 */
	public List<Module> findAllByUser(Long userId);
	
	public List<Module> findByName(String name);
}
