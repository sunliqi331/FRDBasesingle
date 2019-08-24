package com.its.frd.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.its.frd.entity.SpcData;

public interface SpcDataDao extends 
				JpaRepository<SpcData, Long>,
				JpaSpecificationExecutor<SpcData>{
    @QueryHints(value={
            @QueryHint(name="org.hibernate.cacheable",value="true"),
            @QueryHint(name="org.hibernate.cacheRegion",value="com.its.frd.entity.SpcData")
        }
    )

//    @Query("FROM SpcData S ORDER BY S.updateTime DESC LIMIT 1")
    @Query("from SpcData m ORDER BY m.updateTime DESC")
    public List<SpcData> findNewSpcData();
}
