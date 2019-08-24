package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;
import com.its.frd.params.CompanyfileType;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.CompanyinfoService;

@Service
public class CompanyfileServiceImp implements CompanyfileService {
	@Resource
	private CompanyfileDao cfDao;

	@Override
	public List<Companyfile> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Companyfile> springDataPage = cfDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());

		return springDataPage.getContent();
	}

	@Override
	public List<Companyfile> findPage(Specification<Companyfile> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Companyfile> springDataPage = cfDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public Companyfile findById(Long id) {
		return cfDao.findOne(id);
	}

	@Override
	public List<Companyfile> findByParentidAndType(Long id,String type) {
		return cfDao.findByParentidAndParenttypeOrderByIdAsc(id,type);
	}

	@Override
	public void deleteCompanyfileByParentidAndType(Long id,String type) {
		List<Companyfile> filelist = cfDao.findByParentidAndParenttypeOrderByIdAsc(id,type);
		cfDao.deleteInBatch(filelist);
	}

	@Override
	public Companyfile saveOrUpdate(Companyfile file) {
		return cfDao.save(file);
	}
	
	@Override
	public void deleteById(Long id){
		cfDao.delete(id);
	}


}
