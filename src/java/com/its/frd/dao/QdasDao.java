package com.its.frd.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesQdas;

public interface QdasDao extends JpaRepository<MesQdas, Long>, JpaSpecificationExecutor<MesQdas> {

	public MesQdas findByKeyfield(String keyfield);
	
}
