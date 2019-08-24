package com.its.frd.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.its.frd.entity.MesQualityImg;

public interface MesQualityImgDao extends JpaRepository<MesQualityImg, Long>, 
JpaSpecificationExecutor<MesQualityImg> {

    @Query("FROM MesQualityImg M WHERE M.factoryid=?1 AND M.qualityimageid=?2")
    public List<MesQualityImg> findByCompanyinfoidAndTypename(Long companyid, String qualityimageid);

    @Query("FROM MesQualityImg M WHERE M.factoryid=?1 AND M.qualityimageid=?2")
    public List<MesQualityImg> findByQualityFileByTime(Date start, Date end);

    /* @Modifying
//    @Query(value ="UPDATE MesQualityImg M set M.qualityimagenm =?1 WHERE M.qualityimageid=?2", nativeQuery = true)
    @Query("UPDATE MesQualityImg M set M.qualityimagenm =?1 WHERE M.qualityimageid=?2")
    @Transactional
    public void updateMesQualityImg(String qualityimagenm, String qualityimageidOld); */
    
//    @Modifying
//    @Query(value ="UPDATE MesQualityImg M set qualityimagenm =?4 WHERE M.factoryid=?1 AND M.qualityimageid=?2", nativeQuery = true)
//    public void updateMesQualityImg(Long companyid, String qualityimageidOld,String qualityimageidNew, String qualityimagenm, String status);
}