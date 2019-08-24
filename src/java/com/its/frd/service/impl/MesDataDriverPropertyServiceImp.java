package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.MesDataDriverPropertyDao;
import com.its.frd.entity.MesDataDriverProperty;
import com.its.frd.service.MesDataDriverPropertyService;
@Service
public class MesDataDriverPropertyServiceImp implements MesDataDriverPropertyService {
    @Resource
    private MesDataDriverPropertyDao mdDao;
	@Resource
    private CompanyfileDao cfDao;
	
	
	@Override
	public MesDataDriverProperty findById(Long id) {
		return mdDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
	    mdDao.delete(id);

	}

	@Override
	public List<MesDataDriverProperty> findPage(Page page) {
		page.setOrderField("insertTimestamp");
		org.springframework.data.domain.Page<MesDataDriverProperty> springDataPage = mdDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDataDriverProperty> findPage(Specification<MesDataDriverProperty> specification, Page page) {
		try {
//            page.setOrderField("insertTimestamp");
            org.springframework.data.domain.Page<MesDataDriverProperty> springDataPage = mdDao.findAll(specification,PageUtils.createPageable(page));
            page.setTotalCount(springDataPage.getTotalElements());
            return springDataPage.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

	@Override
	public MesDataDriverProperty saveOrUpdate(MesDataDriverProperty t) {
		return mdDao.save(t);
	}

	@Override
	public List<MesDataDriverProperty> findAll(Specification<MesDataDriverProperty> specification, Sort args) {
		return mdDao.findAll(specification, args);
	}

    @Override
    public void save(MesDataDriverProperty MesDataDriverProperty) {
        mdDao.save(MesDataDriverProperty);
    }

    @Override
    public List<MesDataDriverProperty> findAllOrderByInsertTimestamp(
            Specification<MesDataDriverProperty> specification) {
        return mdDao.findAll(specification);
    }

    @Override
    public List<MesDataDriverProperty> findAll(Specification<MesDataDriverProperty> specification) {
        return mdDao.findAll(specification);
    }

}
