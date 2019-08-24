package com.its.frd.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.its.frd.entity.DataDictionary;
import com.its.frd.entity.DataDictionary.DictionaryType;


public interface DataDictionaryDAO extends JpaRepository<DataDictionary, Long>, JpaSpecificationExecutor<DataDictionary> {
	@QueryHints(value={
			@QueryHint(name="org.hibernate.cacheable",value="true"),
			@QueryHint(name="org.hibernate.cacheRegion",value="com.its.frd.entity.DataDictionary")
		}
	)
	Page<DataDictionary> findByParentNameAndType(String name, DictionaryType dictionaryType, Pageable pageable);
	
	@QueryHints(value={
			@QueryHint(name="org.hibernate.cacheable",value="true"),
			@QueryHint(name="org.hibernate.cacheRegion",value="com.its.frd.entity.DataDictionary")
		}
	)
	@Query("FROM DataDictionary d WHERE d.parent.name=?1 AND d.type='ITEM' ORDER BY d.priority ASC")
	List<DataDictionary> findAllItems(String themeName);
}