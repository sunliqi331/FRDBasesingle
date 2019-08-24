package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDataDriverStatus;

public interface MesDataDriverStatusDao
        extends JpaRepository<MesDataDriverStatus, Long>, JpaSpecificationExecutor<MesDataDriverStatus> {

    // @Query("SELECT count(0) FROM (SELECT count(DRIVER_STATUS) AS 'co' FROM data_driver_status WHERE FACTORY_ID = ?1 AND PRODUCT_LINE_ID = ?2 AND DRIVER_ID = ?3 AND DRIVER_STATUS = ?4 GROUP BY INSERT_TIMESTAMP ) t1")
    @Query("FROM MesDataDriverStatus WHERE FACTORY_ID = ?1 AND PRODUCT_LINE_ID = ?2 AND DRIVER_ID = ?3 AND DRIVER_STATUS = ?4 GROUP BY INSERT_TIMESTAMP")
    public List<MesDataDriverStatus> getDriverStatusCount(Long factoryId, Long productLineId, Long driverid, String driverStatus);

}