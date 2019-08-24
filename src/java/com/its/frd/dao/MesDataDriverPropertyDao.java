package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesDataDriverProperty;

public interface MesDataDriverPropertyDao
        extends JpaRepository<MesDataDriverProperty, Long>, JpaSpecificationExecutor<MesDataDriverProperty> {

}