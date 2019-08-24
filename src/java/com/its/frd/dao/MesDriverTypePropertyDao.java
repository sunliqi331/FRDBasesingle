package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesDrivertypeProperty;

public interface MesDriverTypePropertyDao extends 
				JpaRepository<MesDrivertypeProperty, Long>,
				JpaSpecificationExecutor<MesDrivertypeProperty>{
	
    public List<MesDrivertypeProperty> findByMesDrivertypeId(Long MesDrivertypeId);
	
}
