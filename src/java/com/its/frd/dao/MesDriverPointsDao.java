package com.its.frd.dao;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;

public interface MesDriverPointsDao extends 
				JpaRepository<MesDriverPoints, Long>,
				JpaSpecificationExecutor<MesDriverPoints>{
	
    @Query("FROM MesDriverPoints M WHERE M.mesPoints.id=?1 AND M.mesDriver.id=?2")
    public MesDriverPoints findByMesPointIdAndMesDriverId(Long mesPointId,Long mesDriverId);
    
    @Query("FROM MesDriverPoints M WHERE M.mesDriver.id=?1")
    public List<MesDriverPoints> findByMesDriverId(Long mesDriverId);
    
    @Query("FROM MesDriverPoints M WHERE M.mesPoints.id=?1")
    public MesDriverPoints findByMesPointsId(Long mesPointsId);
    
    @Query("FROM MesDriverPoints M WHERE M.mesDrivertypeProperty.id=?1 AND M.mesDriver.id=?2")
    public List<MesDriverPoints> findByMesDrivertypePropertyAndMesDriver(Long MesDrivertypePropertyId,Long mesDriverId);
    

    public List<MesDriverPoints> findByMesDriverIdAndMesDrivertypePropertyIdNotNull(Long mesDriverId);
    
    public MesDriverPoints findByMesDrivertypePropertyId(Long propertyId);
    
    public List<MesDriverPoints> findByMesDriver(MesDriver mesDriver);
}
