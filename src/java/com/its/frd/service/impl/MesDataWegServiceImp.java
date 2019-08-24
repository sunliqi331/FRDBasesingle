package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.MesDataWegDao;
import com.its.frd.entity.MesDataWeg;
import com.its.frd.service.MesDataWegService;
@Service
public class MesDataWegServiceImp implements MesDataWegService {
    @Resource
    private MesDataWegDao mdDao;
	@Resource
    private CompanyfileDao cfDao;
	
	
	@Override
	public MesDataWeg findById(Long id) {
		return mdDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
	    mdDao.delete(id);

	}

	@Override
	public List<MesDataWeg> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDataWeg> springDataPage = mdDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDataWeg> findPage(Specification<MesDataWeg> specification, Page page) {
		org.springframework.data.domain.Page<MesDataWeg> springDataPage = mdDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDataWeg saveOrUpdate(MesDataWeg t) {
		return mdDao.save(t);
	}

	@Override
	public List<MesDataWeg> findAll(Specification<MesDataWeg> specification) {
		return mdDao.findAll(specification);
	}

    @Override
    public void save(MesDataWeg MesDataWeg) {
        mdDao.save(MesDataWeg);
    }
	
}
