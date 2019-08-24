package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesPoints;

public interface MesDriverDao extends 
				JpaRepository<MesDriver, Long>,
				JpaSpecificationExecutor<MesDriver>{
    @Query("FROM MesDriver M WHERE M.companyinfo.id=?1 AND M.differencetype=?2")
    public List<MesDriver> findByCompanyidAndDifferencetype(Long companyid,String differencetype);
    
    public List<MesDriver> findByCompanyinfo(Companyinfo companyInfo);
    
    @Query("FROM MesDriver M WHERE M.mesProductline.id is Null AND M.differencetype!='0' AND M.companyinfo.id=?1")
    public List<MesDriver> findByMesProductline(Long companyid);
    
    @Query("FROM MesDriver M WHERE M.mesProductline.id=?1")
    public List<MesDriver> findByMesProductlineid(Long mesProductlineid);
    
    @Query("FROM MesDriver M WHERE M.companyinfo.id=?1 AND M.sn=?2 AND M.differencetype=?3")
    public List<MesDriver> findByCompanyinfoidAndSnAndDif(Long companyid,String sn,String dif);
    
    public List<MesDriver> findByMesDrivertypeId(Long mesDriverTypeId);
    
    public MesDriver findBySn(String sn);
    
}
