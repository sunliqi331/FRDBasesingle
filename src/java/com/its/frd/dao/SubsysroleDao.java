package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.Subsysrole;

public interface SubsysroleDao extends 
				JpaRepository<Subsysrole, Long>,
				JpaSpecificationExecutor<Subsysrole>{
}
