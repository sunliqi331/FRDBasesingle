package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesAlarmSendRelation;

public interface MesAlarmSendRelationService {
    
	public List<MesAlarmSendRelation> findByCompanyid(Long companyid);
	
	public List<MesAlarmSendRelation> findPage(Specification<MesAlarmSendRelation> specification,Page page);
    
	public List<MesAlarmSendRelation> findPage(Page page);
   
	public MesAlarmSendRelation findById(Long id);
    
    public void saveOrUpdate(MesAlarmSendRelation mesAlarmSendRelation);
    
    public void deleteById(Long id);
}
