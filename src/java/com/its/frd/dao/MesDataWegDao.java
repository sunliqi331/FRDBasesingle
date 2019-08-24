package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesDataWeg;

public interface MesDataWegDao
        extends JpaRepository<MesDataWeg, Long>, JpaSpecificationExecutor<MesDataWeg> {

}