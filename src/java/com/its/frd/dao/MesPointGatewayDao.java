package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesPointGateway;

public interface MesPointGatewayDao extends 
				JpaRepository<MesPointGateway, Long>,
				JpaSpecificationExecutor<MesPointGateway>{
	public List<MesPointGateway> findByCompanyinfoId(Long companyid);
	
	public MesPointGateway findByMacAndMacCode(String mac, String macCode);

	public MesPointGateway findByMac(String mac);
	
}
