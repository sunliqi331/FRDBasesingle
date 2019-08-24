package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesPointGatewayDao;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.service.MesPointGatewayService;

@Service
public class MesPointGatewayServiceImp implements MesPointGatewayService {
	@Resource
	private MesPointGatewayDao dao;
	
	@Override
	public List<MesPointGateway> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPointGateway> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesPointGateway> findPage(Specification<MesPointGateway> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPointGateway> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesPointGateway findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void saveOrUpdate(MesPointGateway point) {
		dao.save(point);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesPointGateway> findByCompanyid(Long companyid) {
		return dao.findByCompanyinfoId(companyid);
	}

	@Override
	public List<MesPointGateway> findAll(Specification<MesPointGateway> specification) {
		// TODO Auto-generated method stub
		return dao.findAll(specification);
	}

	@Override
	public MesPointGateway findAsSingle(MesPointGateway gateway) {
		// TODO Auto-generated method stub
		if(!StringUtils.isBlank(gateway.getMac()) && !StringUtils.isBlank(gateway.getMacCode())){
			return dao.findByMacAndMacCode(gateway.getMac(), gateway.getMacCode());
		}
		return null;
	}

	@Override
	public MesPointGateway findByMac(String mac) {
		// TODO Auto-generated method stub
		return dao.findByMac(mac);
	}

	@Override
	public List<MesPointGateway> findAll() {
		return dao.findAll();
	}


}
