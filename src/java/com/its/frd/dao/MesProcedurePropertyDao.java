package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProductProcedure;

public interface MesProcedurePropertyDao extends 
				JpaRepository<MesProcedureProperty, Long>,
				JpaSpecificationExecutor<MesProcedureProperty>{

   /* @Query("FROM MesProcedureProperty M WHERE M.mesDriverprocedure.id=?1 AND M.propertyname=?2")
    public MesProcedureProperty findByMesDriverprocedureIdAndPropertyname(Long mesDriverprocedureId,String propertyname);*/
	
	public List<MesProcedureProperty> findByMesProductProcedure(MesProductProcedure mesProductProcedure);
}
