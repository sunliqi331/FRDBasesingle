package com.its.frd.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.MesDataDriverCountDao;
import com.its.frd.entity.MesDataDriverCount;
import com.its.frd.service.MesDataDriverCountService;
@Service
public class MesDataDriverCountServiceImp implements MesDataDriverCountService {
    @Resource
    private MesDataDriverCountDao mdDao;
	@Resource
    private CompanyfileDao cfDao;
	
	
	@Override
	public MesDataDriverCount findById(Long id) {
		return mdDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
	    mdDao.delete(id);

	}

	@Override
	public List<MesDataDriverCount> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDataDriverCount> springDataPage = mdDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDataDriverCount> findPage(Specification<MesDataDriverCount> specification, Page page) {
		org.springframework.data.domain.Page<MesDataDriverCount> springDataPage = mdDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDataDriverCount saveOrUpdate(MesDataDriverCount t) {
		return mdDao.save(t);
	}

	@Override
	public List<MesDataDriverCount> findAll(Specification<MesDataDriverCount> specification) {
		return mdDao.findAll(specification);
	}

    @Override
    public void save(MesDataDriverCount MesDataDriverCount) {
        mdDao.save(MesDataDriverCount);
    }

    @Override
    public List<MesDataDriverCount> findAllMesDataDriverCount(Integer companyid, Integer productLineId,
            Integer driverId, Integer pointId, Timestamp beginDate, Timestamp endDate) {
        return null;
    }
	
}
