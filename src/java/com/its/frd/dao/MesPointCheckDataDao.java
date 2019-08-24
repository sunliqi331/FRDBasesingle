package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.Department;
import com.its.frd.entity.MesPointCheckData;

public interface MesPointCheckDataDao extends JpaRepository<MesPointCheckData, Long>,
			JpaSpecificationExecutor<MesPointCheckData>{

    @Query("FROM MesPointCheckData M WHERE M.mesDriverPoints.id=?1 AND M.checkvalue=?2")
    public List<MesPointCheckData> findByMesDriverPointsIdAndCheckvalue(Long mesDriverPointsId,Long checkvalue);
    
    @Query("FROM MesPointCheckData M WHERE M.mesDriverPoints.id=?1 AND M.name=?2")
    public List<MesPointCheckData> findByMesDriverPointsIdAndName(Long mesDriverPointsId,String name);
    
    public List<MesPointCheckData> findByMesDriverPointsIdIn(List<Long> pointIds);
}
