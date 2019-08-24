package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesCompanyPositionDao;
import com.its.frd.entity.MesCompanyPosition;
import com.its.frd.service.MesCompanyPositionService;
@Service
public class MesCompanyPositionServiceImp implements MesCompanyPositionService {
	@Resource
	private MesCompanyPositionDao dao;
	
	
	@Override
	public MesCompanyPosition findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesCompanyPosition> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesCompanyPosition> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesCompanyPosition> findPage(Specification<MesCompanyPosition> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesCompanyPosition> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesCompanyPosition saveOrUpdate(MesCompanyPosition t) {
		return dao.save(t);
	}

    @Override
    public List<MesCompanyPosition> findByCompanyid(Long comapnyid) {
        return dao.findByCompanyid(comapnyid);
    }


}
