package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesDriverAlarmDao;
import com.its.frd.entity.MesDriverAlarm;
import com.its.frd.service.MesDriverAlarmService;
@Service
public class MesDriverAlarmServiceImp implements MesDriverAlarmService {

	@Resource
	private MesDriverAlarmDao mdaDao;

	@Override
	public List<MesDriverAlarm> findPage(Specification<MesDriverAlarm> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverAlarm> springDataPage = mdaDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDriverAlarm findById(Long id) {
		return mdaDao.findOne(id);
	}

	@Override
	public void saveOrUpdate(MesDriverAlarm mesDriverAlarm) {
	    mdaDao.save(mesDriverAlarm);
	}

	@Override
	public void deleteById(Long id) {
	    mdaDao.delete(id);
	}

    @Override
    public List<MesDriverAlarm> findByMesPointsId(Long mesPointsId) {
        return mdaDao.findByMesPointsId(mesPointsId);
    }

    @Override
    public List<MesDriverAlarm> findByMesDriverId(Long mesDriverId) {
        return mdaDao.findByMesDriverId(mesDriverId);
    }

    @Override
    public void delete(List<MesDriverAlarm> mesDriverAlarms) {
    	List<MesDriverAlarm> sublist = null;
    	//大量数据要删除，1000件分割删除
    	int i;
    	for (i = 0; i < mesDriverAlarms.size()/1000; i++) {
    		sublist = mesDriverAlarms.subList(1000*i, 1000*(i+1));
            mdaDao.deleteInBatch(sublist);
            //System.out.println(1000*i+" , "+1000*(i+1));
		}
    	//最后不足1000件的一次删除
    	sublist =  mesDriverAlarms.subList(1000*i, mesDriverAlarms.size());
        mdaDao.deleteInBatch(sublist);
        mdaDao.flush();
        //System.out.println(1000*i+" , "+mesDriverAlarms.size());
    }
    
    @Override
    public List<MesDriverAlarm> findTen(List<Long> ids){
    	return mdaDao.findLimitTen(ids);
    }

}
