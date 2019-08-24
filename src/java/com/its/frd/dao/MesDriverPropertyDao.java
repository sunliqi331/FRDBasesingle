package com.its.frd.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDriverProperty;

public interface MesDriverPropertyDao extends 
				JpaRepository<MesDriverProperty, Long>,
				JpaSpecificationExecutor<MesDriverProperty>{
	
    public MesDriverProperty findByPropertyname(String propertyname);
	
    @Query("FROM MesDriverProperty M WHERE M.mesDriver.id=?1")
    public List<MesDriverProperty> findByMesDriverid(Long mesDriverid);
    
    @Query("FROM MesDriverProperty M WHERE M.mesDriver.name=?1 AND M.propertyname=?2")
    public MesDriverProperty findByMesDriverAndPropertyname(String mesDriverName,String propertyname);
}
