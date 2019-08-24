package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesDriverPointsDao;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.service.MesDriverPointService;
@Service
public class MesDriverPointImp implements MesDriverPointService {
	@Resource
	private MesDriverPointsDao dao;
	
	
	@Override
	public MesDriverPoints findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesDriverPoints> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverPoints> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDriverPoints> findPage(Specification<MesDriverPoints> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverPoints> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDriverPoints saveOrUpdate(MesDriverPoints t) {
		return dao.save(t);
	}

    @Override
    public MesDriverPoints findByMesPointIdAndMesDriverId(Long mesPointId, Long mesDriverId) {
        return dao.findByMesPointIdAndMesDriverId(mesPointId, mesDriverId);
    }

    @Override
    public List<MesDriverPoints> findByMesDriverId(Long mesDriverId) {
        return dao.findByMesDriverId(mesDriverId);
    }

    @Override
    public List<MesDriverPoints> findByMesDrivertypePropertyAndMesDriver(Long MesDrivertypePropertyId, Long mesDriverId) {
        return dao.findByMesDrivertypePropertyAndMesDriver(MesDrivertypePropertyId, mesDriverId);
    }

	@Override
	public MesDriverPoints findByMesPointsId(Long mesPointsId) {
		return dao.findByMesPointsId(mesPointsId);
	}

	@Override
	public List<MesDriverPoints> findByMesDriverIdAndMesDrivertypePropertyIdNotNull(Long mesDriverId) {
		if(mesDriverId == null)
			return null;
		return dao.findByMesDriverIdAndMesDrivertypePropertyIdNotNull(mesDriverId);
	}

	@Override
	public List<MesDriverPoints> findByMesDriver(MesDriver mesDriver) {
		return dao.findByMesDriver(mesDriver);
	}

}
