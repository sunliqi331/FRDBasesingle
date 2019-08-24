package com.its.frd.service.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.frd.dao.MonitorPainterDao;
import com.its.frd.entity.MonitorPainter;
import com.its.frd.service.MonitorPainterService;

@Service("monitorPaintorService")
public class MonitorPainterServiceImpl implements MonitorPainterService {

	@Autowired
	private MonitorPainterDao monitorPainterDao;
	
	@Override
	public List<MonitorPainter> findByUserId(long id) {
		// TODO Auto-generated method stub
		return monitorPainterDao.findByUserId(id);
	}

	@Override
	public List<MonitorPainter> findByIdIn(Collection<Long> list,long companyId) {
		return monitorPainterDao.findAll(new Specification<MonitorPainter>() {
			@Override
			public Predicate toPredicate(Root<MonitorPainter> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				Predicate predicate1 = root.get("id").in(list);
				Predicate predicate2 = builder.equal(root.get("companyId"), companyId);
				return query.where(predicate1,predicate2).getRestriction();
			}
		});
	}

	@Override
	public void saveMonitorPainter(MonitorPainter monitorPainter) {
		monitorPainterDao.save(monitorPainter);
		
	}

	@Override
	public MonitorPainter findByNameAndCompanyId(String name, long companyId) {
		// TODO Auto-generated method stub
		List<MonitorPainter> list =  monitorPainterDao.findByNameAndCompanyId(name, companyId);
		if(list.size() > 0){
			return list.get(0);
		}else
			return null;
	}
	@Override
	public MonitorPainter findByIdAndCompanyId(String name, long companyId) {
		// TODO Auto-generated method stub
		List<MonitorPainter> list =  monitorPainterDao.findByIdAndCompanyId(name, companyId);
		if(list.size() > 0){
			return list.get(0);
		}else
			return null;
	}
	@Override
	public MonitorPainter findById(long id) {
		// TODO Auto-generated method stub
		return monitorPainterDao.findOne(id);
	}

	/**
	 * 找出数据库最新的一条数据的id
	 */
	@Override
	public String findLastRecordId() {
		Long res = null;
		List<MonitorPainter> findLastRecordId = monitorPainterDao.findLastRecordId();
		return findLastRecordId.get(0).getId()+"";
	}

}
