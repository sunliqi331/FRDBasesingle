package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesAlarmSendRelationDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesAlarmSendRelation;
import com.its.frd.entity.MesDriverAlarm;
import com.its.frd.service.MesAlarmSendRelationService;

@Service
public class MesAlarmSendRelationServiceImp implements MesAlarmSendRelationService {
	
	@Resource
	private MesAlarmSendRelationDao alarRDao;
	
	@Override
	public List<MesAlarmSendRelation> findByCompanyid(Long companyid) {
		return alarRDao.findByCompanyid(companyid);
	}

    @Override
    public List<MesAlarmSendRelation> findPage(Specification<MesAlarmSendRelation> specification, Page page) {
    	page.setOrderField("id");
        org.springframework.data.domain.Page<MesAlarmSendRelation> springDataPage = alarRDao.findAll(specification,PageUtils.createPageable(page));
        page.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public MesAlarmSendRelation findById(Long id) {
        return alarRDao.findOne(id);
    }

    @Override
    public void saveOrUpdate(MesAlarmSendRelation mesAlarmSendRelation) {
        alarRDao.save(mesAlarmSendRelation);
        
    }

    @Override
    public void deleteById(Long id) {
        alarRDao.delete(id);
    }

    @Override
    public List<MesAlarmSendRelation> findPage(Page page) {
    	page.setOrderField("id");
        org.springframework.data.domain.Page<MesAlarmSendRelation> springDataPage = alarRDao.findAll(PageUtils.createPageable(page));
        page.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

}
