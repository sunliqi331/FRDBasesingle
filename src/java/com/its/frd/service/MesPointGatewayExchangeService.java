package com.its.frd.service;

import com.its.frd.entity.MesPointGatewayExchange;

public interface MesPointGatewayExchangeService extends BaseService<MesPointGatewayExchange> {
	/**
	 * 错误：当前公司不存在
	 */
	public static final int EXCHANGE_ERROR_NO_COMPANYINFO = -1;
	/**
	 * 错误：验证码不正确
	 */
	public static final int EXCHANGE_ERROR_NO_MAC_CODE = -2;
	/**
	 * 错误：网关已被验证过
	 */
	public static final int EXCHANGE_ERROR_BEEN_VERIFIED = -3;
	/**
	 * 正确：验证成功
	 */
	public static final int EXCHANGE_SUCCESS = 0;
	
	
	
	public int saveExchangeGateway(MesPointGatewayExchange mesPointGatewayExchange);
	
}
