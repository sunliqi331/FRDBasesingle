package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesPointGatewayExchange;

public interface MesPointGatewayExchangeDao extends JpaRepository<MesPointGatewayExchange, Long>,JpaSpecificationExecutor<MesPointGatewayExchange>{

	List<MesPointGatewayExchange> findByCurrentMacOrderByChangeDateDesc(String sourceMac);
	

}
