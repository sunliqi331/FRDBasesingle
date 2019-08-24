package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesSubdriverDao;
import com.its.frd.entity.MesSubdriver;
import com.its.frd.service.MesSubdriverService;
@Service
public class MesSubdriverImp implements MesSubdriverService {
	@Resource
	private MesSubdriverDao dao;
	
	
	@Override
	public MesSubdriver findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesSubdriver> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesSubdriver> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesSubdriver> findPage(Specification<MesSubdriver> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesSubdriver> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesSubdriver saveOrUpdate(MesSubdriver t) {
		return dao.save(t);
	}

}
