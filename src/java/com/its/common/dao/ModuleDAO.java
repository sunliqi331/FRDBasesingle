package com.its.common.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.its.common.entity.main.Module;
import com.its.frd.entity.MesDriver;

public interface ModuleDAO extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {
	Page<Module> findByParentId(Long parentId, Pageable pageable);
	
	@QueryHints(value={
			@QueryHint(name="org.hibernate.cacheable",value="true"),
			@QueryHint(name="org.hibernate.cacheRegion",value="com.its.common.entity.main.Module")
		}
	)
	@Query("from Module m order by m.priority ASC")
	List<Module> findAllWithCache();
	
	Module getBySn(String sn);
	
	@Query("FROM Module M WHERE M.name=?1")
    public List<Module> findByName(String name);
}