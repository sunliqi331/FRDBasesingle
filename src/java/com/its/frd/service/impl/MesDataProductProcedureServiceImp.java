package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.MesDataProductProcedureDao;
import com.its.frd.entity.MesDataMultiKey;
import com.its.frd.entity.MesDataProductProcedure;
import com.its.frd.service.MesDataProductProcedureService;
@Service
public class MesDataProductProcedureServiceImp implements MesDataProductProcedureService {
    @Resource
    private MesDataProductProcedureDao mdDao;
	@Resource
    private CompanyfileDao cfDao;
	
	
	@Override
	public MesDataProductProcedure findById(Long id) {
		return mdDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
	    mdDao.delete(id);

	}

	@Override
	public List<MesDataProductProcedure> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDataProductProcedure> springDataPage = mdDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDataProductProcedure> findPage(Specification<MesDataProductProcedure> specification, Page page) {
		org.springframework.data.domain.Page<MesDataProductProcedure> springDataPage = mdDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDataProductProcedure saveOrUpdate(MesDataProductProcedure t) {
		return mdDao.save(t);
	}

	@Override
	public List<MesDataProductProcedure> findAll(Specification<MesDataProductProcedure> specification) {
		return mdDao.findAll(specification);
	}

    @Override
    public void save(MesDataProductProcedure MesDataProductProcedure) {
        mdDao.save(MesDataProductProcedure);
    }

    @Override
    public void delByRowKey(MesDataMultiKey mesDataMultiKey) {
        mdDao.deleteByRowKey(mesDataMultiKey.getFactoryId(), mesDataMultiKey.getProductLineId(),
                mesDataMultiKey.getDriverId(), mesDataMultiKey.getPointId(), mesDataMultiKey.getInsertTimestamp());

    }

	@Override
	public List<Object[]> findDataProductByPRODUCT_MODE(String modelnum) {
		return mdDao.findDataProductByPRODUCT_MODE(modelnum);
	}

}
