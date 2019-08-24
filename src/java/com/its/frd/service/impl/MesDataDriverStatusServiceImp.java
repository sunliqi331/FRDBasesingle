package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.MesDataDriverStatusDao;
import com.its.frd.entity.MesDataDriverStatus;
import com.its.frd.service.MesDataDriverStatusService;
@Service
public class MesDataDriverStatusServiceImp implements MesDataDriverStatusService {
    @Resource
    private MesDataDriverStatusDao mdDao;
	@Resource
    private CompanyfileDao cfDao;
	
	
	@Override
	public MesDataDriverStatus findById(Long id) {
		return mdDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
	    mdDao.delete(id);

	}

	@Override
	public List<MesDataDriverStatus> findPage(Page page) {
//		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDataDriverStatus> springDataPage = mdDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDataDriverStatus> findPage(Specification<MesDataDriverStatus> specification, Page page) {
//		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDataDriverStatus> springDataPage = mdDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDataDriverStatus saveOrUpdate(MesDataDriverStatus t) {
		return mdDao.save(t);
	}

	@Override
	public List<MesDataDriverStatus> findAll(Specification<MesDataDriverStatus> specification) {
		return mdDao.findAll(specification);
	}

    @Override
    public void save(MesDataDriverStatus MesDataDriverStatus) {
        mdDao.save(MesDataDriverStatus);
    }

    @Override
    public List<MesDataDriverStatus> findAll(Specification<MesDataDriverStatus> specification,
            Sort arg) {
        return mdDao.findAll(specification, arg);
    }

}
