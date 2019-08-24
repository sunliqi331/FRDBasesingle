package com.its.frd.dao;

import java.util.List;

import com.its.frd.entity.Companyinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesProductline;

public interface MesProductlineDao extends 
				JpaRepository<MesProductline, Long>,
				JpaSpecificationExecutor<MesProductline>{
	
    @Query("FROM MesProductline M WHERE M.companyinfo.id=?1")
    public List<MesProductline> findByCompanyid(Long companyid);
    
    @Query("FROM MesProductline M WHERE M.companyinfo.id=?1 AND M.linesn=?2")
    public MesProductline findByCompanyinfoidAndLinen(Long companyid,String linesn);

	public MesProductline findByLinename(String linename);

    List<MesProductline> findByCompanyinfoIdIn(List<Long> companyinfoIds);
}
