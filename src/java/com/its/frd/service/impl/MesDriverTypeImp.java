package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.MesDriverTypeDao;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.MesDrivertype;
import com.its.frd.params.CompanyfileType;
import com.its.frd.service.MesDriverTypeService;
@Service
public class MesDriverTypeImp implements MesDriverTypeService {
	@Resource
	private MesDriverTypeDao mdDao;
	@Resource
    private CompanyfileDao cfDao;
	
	
	@Override
	public MesDrivertype findById(Long id) {
		return mdDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		mdDao.delete(id);
	}

	@Override
	public List<MesDrivertype> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDrivertype> springDataPage = mdDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDrivertype> findPage(Specification<MesDrivertype> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDrivertype> springDataPage = mdDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDrivertype saveOrUpdate(MesDrivertype t) {
		return mdDao.save(t);
	}
	

    @Override
    public void saveAndFile(MesDrivertype mesDrivertype, List<Companyfile> companyfilelist) {
        mdDao.save(mesDrivertype);
        for(Companyfile cpfile : companyfilelist){
            cpfile.setParentid(mesDrivertype.getId());
            cpfile.setParenttype(CompanyfileType.DRIVERTYPEFILE.toString());
            cfDao.save(cpfile);
        }
    }

    @Override
    public List<MesDrivertype> findByCompanyinfoidAndTypename(Long companyid, String typename) {
        return mdDao.findByCompanyinfoidAndTypename(companyid, typename);
    }

	@Override
	public List<MesDrivertype> findAll(Specification<MesDrivertype> specification) {
		// TODO Auto-generated method stub
		return mdDao.findAll(specification);
	}
	
}
