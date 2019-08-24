package com.its.frd.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDataDriverCount;

public interface MesDataDriverCountDao extends JpaRepository<MesDataDriverCount, Long>, 
JpaSpecificationExecutor<MesDataDriverCount> {

    /*
    @Query("FROM MesDataDriverCount M WHERE"
            + " M.factoryId=?1 "
            + "AND M.productLineId=?2 "
            + "AND M.driverId=?3 "
            + "AND M.pointId=?4 "
            + "AND M.insertTimestamp>=?5 "
            + "AND M.insertTimestamp<=?6"
            )
    public List<MesDataDriverCount> findAllMesDataDriverCount(
            Integer companyid
            ,Integer productLineId
            ,Integer driverId
            ,Integer pointId
            ,Timestamp beginDate
            ,Timestamp endDate
            );*/
//
}