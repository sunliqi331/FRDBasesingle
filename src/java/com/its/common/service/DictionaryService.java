package com.its.common.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.Dictionary;
import com.its.common.util.dwz.Page;

public interface DictionaryService {
	Dictionary get(Long id);

	void saveOrUpdate(Dictionary dictionary);

	void delete(Long id);
	
	List<Dictionary> findAll(Page page);
	
	List<Dictionary> findByExample(Specification<Dictionary> specification, Page page);
	
	List<Dictionary> findAll(Specification<Dictionary> specification);
	
	List<Dictionary> findByThemeName(String themeName, Page page);
	
	Dictionary findById(Long id);
}
