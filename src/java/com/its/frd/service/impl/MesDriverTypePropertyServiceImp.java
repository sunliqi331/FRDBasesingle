package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesDriverTypePropertyDao;
import com.its.frd.entity.MesDrivertypeProperty;
import com.its.frd.service.MesDriverTypePropertyService;
@Service
public class MesDriverTypePropertyServiceImp implements MesDriverTypePropertyService {
	@Resource
	private MesDriverTypePropertyDao mpDao;
	
	
	@Override
	public MesDrivertypeProperty findById(Long id) {
		return mpDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		mpDao.delete(id);
	}

	@Override
	public List<MesDrivertypeProperty> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDrivertypeProperty> springDataPage = mpDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDrivertypeProperty> findPage(Specification<MesDrivertypeProperty> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDrivertypeProperty> springDataPage = mpDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDrivertypeProperty saveOrUpdate(MesDrivertypeProperty t) {
		return mpDao.save(t);
	}

    @Override
    public List<MesDrivertypeProperty> findByMesDrivertypeId(Long MesDrivertypeId) {
        return mpDao.findByMesDrivertypeId(MesDrivertypeId);
    }

}
