package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesQdasCategory;

public interface QdasCategoryDao extends JpaRepository<MesQdasCategory, Long>, JpaSpecificationExecutor<MesQdasCategory> {

}
