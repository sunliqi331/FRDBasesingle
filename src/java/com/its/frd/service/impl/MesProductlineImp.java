package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.its.frd.entity.Companyinfo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesProductlineDao;
import com.its.frd.entity.MesProductline;
import com.its.frd.service.MesProductlineService;
@Service
public class MesProductlineImp implements MesProductlineService {
	@Resource
	private MesProductlineDao dao;
	
	@Override
	public MesProductline findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesProductline> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProductline> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesProductline> findPage(Specification<MesProductline> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProductline> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesProductline saveOrUpdate(MesProductline t) {
		return dao.save(t);
	}

    @Override
    public List<MesProductline> findByCompanyid(Long comapnyid) {
        return dao.findByCompanyid(comapnyid);
    }

    @Override
    public MesProductline findByCompanyinfoidAndLinen(Long companyid, String linesn) {
        return dao.findByCompanyinfoidAndLinen(companyid, linesn);
    }

	@Override
	public MesProductline findByLinename(String linename) {
		return dao.findByLinename(linename);
	}

	@Override
	public List<MesProductline> findByCompanyinfoIdIn(List<Long> companyinfoIds) {
		return dao.findByCompanyinfoIdIn(companyinfoIds);
	}


}
