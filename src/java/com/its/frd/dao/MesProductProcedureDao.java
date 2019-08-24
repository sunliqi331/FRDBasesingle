package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductProcedure;

public interface MesProductProcedureDao extends 
				JpaRepository<MesProductProcedure, Long>,
				JpaSpecificationExecutor<MesProductProcedure>{
	
	public MesProductProcedure findByprocedurenum(String procedurenum);
	
	public List<MesProductProcedure> findByMesProduct(MesProduct mesProduct);

    List<MesProductProcedure> findByMesProductIdIn(List<Long> mesProductListIds);
}
