package com.its.frd.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.its.frd.entity.MesDataProductProcedure;

public interface MesDataProductProcedureDao
        extends JpaRepository<MesDataProductProcedure, Long>, JpaSpecificationExecutor<MesDataProductProcedure> {
    @Modifying
    @Transactional
    @Query("delete from MesDataProductProcedure where"
            + " mesDataMultiKey.factoryId = ?1 "
            + "and mesDataMultiKey.productLineId = ?2 "
            + "and mesDataMultiKey.driverId = ?3 "
            + "and mesDataMultiKey.pointId = ?4 "
            + "and mesDataMultiKey.insertTimestamp = ?5 "
            )
    int deleteByRowKey(Integer factoryId, Integer productLineId, Integer driverId, Integer pointId, BigInteger insertTimestamp);


    @Query(value="select d.INSERT_TIMESTAMP,d.QUALIFIED from data_product_procedure d where d.PRODUCT_MODE = ?1 ORDER BY d.INSERT_TIMESTAMP LIMIT 1",nativeQuery=true)
    List<Object[]> findDataProductByPRODUCT_MODE(String modelnum);
}