package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesPointGateway;

public interface MesPointGatewayService {
	public List<MesPointGateway> findPage(Page page);
	public List<MesPointGateway> findPage(Specification<MesPointGateway> specification,Page page);
	public List<MesPointGateway> findAll(Specification<MesPointGateway> specification);
	public List<MesPointGateway> findAll();
	public MesPointGateway findById(Long id);
	public void saveOrUpdate(MesPointGateway point);
	public void deleteById(Long id);
	public List<MesPointGateway> findByCompanyid(Long companyid);
	public MesPointGateway findByMac(String mac);
	public MesPointGateway findAsSingle(MesPointGateway gateway);
}
