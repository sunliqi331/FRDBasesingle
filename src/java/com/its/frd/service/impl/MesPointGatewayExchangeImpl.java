package com.its.frd.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.service.RedisService;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesPointGatewayExchangeDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPointGatewayExchange;
import com.its.frd.entity.MesPoints;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesPointGatewayExchangeService;
import com.its.frd.service.MesPointGatewayService;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;

@Service
public class MesPointGatewayExchangeImpl implements MesPointGatewayExchangeService {

	@Autowired
	private MesPointGatewayExchangeDao mesPointGatewayExchangeDao;
	@Autowired
	private CompanyinfoService cpinfoServ;
	@Autowired
	private MesPointGatewayService mesPointGatewayService;
	@Autowired
	private  MesDriverService mesdriverServ;
	@Resource
	private RedisService redisServ;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;
	@Override
	public List<MesPointGatewayExchange> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPointGatewayExchange> springDataPage = mesPointGatewayExchangeDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesPointGatewayExchange> findPage(Specification<MesPointGatewayExchange> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPointGatewayExchange> springDataPage = mesPointGatewayExchangeDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesPointGatewayExchange saveOrUpdate(MesPointGatewayExchange t) {
		// TODO Auto-generated method stub
		return mesPointGatewayExchangeDao.save(t);
	}

	public MesPointGatewayExchange getSourceMesPointGatewayExchange(String sourceMac){
		List<MesPointGatewayExchange> gatewayExchanges = mesPointGatewayExchangeDao.findByCurrentMacOrderByChangeDateDesc(sourceMac);
		if(gatewayExchanges != null && gatewayExchanges.size() > 0){
			return gatewayExchanges.get(0);
		}
		return null;
	}

	/**
	 * 网关验证逻辑
	 */
	@Override
	public int saveExchangeGateway(MesPointGatewayExchange mesPointGatewayExchange) {
		/**begin
		 * 查询新替换的网关是否存在，并且验证网关的准确性
		 */
		MesPointGateway pointGateway = mesPointGatewayService.findByMac(mesPointGatewayExchange.getOriginalMac());
		
		Companyinfo companyinfo = pointGateway.getCompanyinfo();
		if(null == companyinfo){
			return MesPointGatewayExchangeService.EXCHANGE_ERROR_NO_COMPANYINFO;
		}
		
		MesPointGateway checkGateway = new MesPointGateway();
		checkGateway.setMac(mesPointGatewayExchange.getCurrentMac());
		checkGateway.setMacCode(mesPointGatewayExchange.getMacCode());
		//待替换的网关
		MesPointGateway gateway = mesPointGatewayService.findAsSingle(checkGateway);
		if(null == gateway){
			return MesPointGatewayExchangeService.EXCHANGE_ERROR_NO_MAC_CODE;
		}
		if(gateway.getIsActive().equals(MesPointGateway.ACTIVE_YES)){
			return MesPointGatewayExchangeService.EXCHANGE_ERROR_BEEN_VERIFIED;
		}
		
		/*gateway.setIsActive(MesPointGateway.ACTIVE_YES);
		gateway.setCompanyinfo(companyinfo);
		*//**
		 * end
		 *//*
		//Companyinfo companyinfo = cpinfoServ.findById(SecurityUtils.getShiroUser().getCompanyid());
		
		gateway.setCompanyinfo(companyinfo);
		gateway.setIsActive(MesPointGateway.ACTIVE_YES);
		mesPointGatewayService.saveOrUpdate(gateway);
*/
		//更新本网关在设备表里的状态
		MesDriver driver = mesdriverServ.findBySn(gateway.getMac());
		if (driver==null) {
			driver = new MesDriver();
			driver.setSn(gateway.getMac());
			driver.setName(gateway.getName());
		}
		Timestamp createTime_driver = driver.getCreatetime();
		MesDriver originalDriver = mesdriverServ.findBySn(mesPointGatewayExchange.getOriginalMac());
		if(null != originalDriver){
			Timestamp createTime_driver_original = originalDriver.getCreatetime();
			String originalDriverName = originalDriver.getName();
			originalDriver.setSn(mesPointGatewayExchange.getCurrentMac());
			originalDriver.setName(driver.getName());
			originalDriver.setCreatetime(createTime_driver);
			driver.setCreatetime(createTime_driver_original);
			driver.setSn(mesPointGatewayExchange.getOriginalMac());
			driver.setName(originalDriverName);
		}
		//driver.setCompanyinfo(companyinfo);
		//mesdriverServ.saveOrUpdate(driver);

		//修改原网关中的mac信息和原网关设备的sn信息
		//获取原网关信息
		MesPointGateway mesPointGateway = mesPointGatewayService.findByMac(mesPointGatewayExchange.getOriginalMac());
		String originalMacCode = mesPointGateway.getMacCode();
		mesPointGateway.setMac(mesPointGatewayExchange.getCurrentMac());
		String macName = mesPointGateway.getName();
		mesPointGateway.setName(gateway.getName());
		mesPointGateway.setMacCode(mesPointGatewayExchange.getMacCode());
		Timestamp createTime = mesPointGateway.getCreateTime();
		mesPointGateway.setCreateTime(gateway.getCreateTime());
		gateway.setMac(mesPointGatewayExchange.getOriginalMac());
		gateway.setMacCode(originalMacCode);
		gateway.setCreateTime(createTime);
		gateway.setName(macName);
		//进行网关mac地址的替换
		/*mesPointGateway.setMac(mesPointGatewayExchange.getCurrentMac());
		gateway.setMac(mesPointGatewayExchange.getOriginalMac());
		//获取原设备网关信息
		MesDriver mesDriver = mesdriverServ.findBySn(mesPointGatewayExchange.getOriginalMac());
		mesDriver.setSn(mesPointGatewayExchange.getCurrentMac());
		driver.setSn(mesPointGatewayExchange.getOriginalMac());
		//解绑原设备表中网关的公司信息
		mesDriver.setCompanyinfo(null);
		pointGateway.setCompanyinfo(null);
		pointGateway.setIsActive(MesPointGateway.ACTIVE_NO);*/
		//新增网关替换记录
		MesPointGatewayExchange sourceMesPointGatewayExchange = this.getSourceMesPointGatewayExchange(mesPointGatewayExchange.getOriginalMac());
		mesPointGatewayExchange.setChangeDate(new Date());
		mesPointGatewayExchange.setCompanyinfo(companyinfo);
		if(null != sourceMesPointGatewayExchange){
			mesPointGatewayExchange.setSourceId(sourceMesPointGatewayExchange);
		}
		MesPointGatewayExchange pointGatewayExchange = this.saveOrUpdate(mesPointGatewayExchange);
		
		//保存出错了。。。。。
		if(null == pointGatewayExchange || pointGatewayExchange.getId() == 0){
			return -7;
		}
		//同步redis中的mesPoint模板
		Map<String, Object> map = redisServ.getHash(MesPointsTemplate.class.getSimpleName()+"_"+mesPointGatewayExchange.getOriginalMac());
		redisServ.del(MesPointsTemplate.class.getSimpleName()+"_"+mesPointGatewayExchange.getOriginalMac());
		redisServ.setHash(MesPointsTemplate.class.getSimpleName()+"_"+mesPointGatewayExchange.getCurrentMac(), map);
		/*for(MesPoints mesPoints : mesPointGateway.getMesPointses()){
			String template = mesPointsTemplateService.getTemplate(mesPoints);
			MesPointGateway mac = mesPoints.getMesPointGateway();
			String key = MesPointsTemplate.class.getSimpleName()+"_"+mac.getMac();
			Map<String, Object> hash = redisServ.getHash(key);
			if(hash == null){
				hash = new HashMap<String,Object>();
			}
			hash.put(mesPoints.getCodekey(), template);
			redisServ.setHash(key, hash);
		}*/
		mesPointsTemplateService.sendTemplate(mesPointGateway.getMac());
		//替换redis中原mac地址的数据为现mac地址
		for(MesPoints mesPoints : mesPointGateway.getMesPointses()){
			String key = pointGatewayExchange.getOriginalMac()+":"+mesPoints.getCodekey();
			//获取原mac地址数据
			Map<String, Object> hash = redisServ.getHash(key);
			if(hash != null){
				String newKey = mesPointGateway.getMac()+":"+mesPoints.getCodekey();
				redisServ.setHash(newKey, hash);
			}
		}
		return MesPointGatewayExchangeService.EXCHANGE_SUCCESS;

	}

}
