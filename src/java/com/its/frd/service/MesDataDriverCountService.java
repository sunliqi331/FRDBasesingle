package com.its.frd.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDataDriverCount;

public interface MesDataDriverCountService extends BaseService<MesDataDriverCount> {
    
    public void save(MesDataDriverCount MesDataDriverCount);
	
	public MesDataDriverCount findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesDataDriverCount> findPage(Specification<MesDataDriverCount> specification,Page page);
	
	public List<MesDataDriverCount> findAll(Specification<MesDataDriverCount> specification);
	
	public List<MesDataDriverCount> findAllMesDataDriverCount(
            Integer companyid,
            Integer productLineId,
            Integer driverId,
            Integer pointId,
            Timestamp beginDate,
            Timestamp endDate);
}
