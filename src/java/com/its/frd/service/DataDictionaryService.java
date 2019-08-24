package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.DataDictionary;

public interface DataDictionaryService {
	DataDictionary get(Long id);

	void saveOrUpdate(DataDictionary dictionary);

	void delete(Long id);
	
	List<DataDictionary> findAll(Page page);
	
	List<DataDictionary> findByExample(Specification<DataDictionary> specification, Page page);
	
	List<DataDictionary> findByThemeName(String themeName, Page page);
}
