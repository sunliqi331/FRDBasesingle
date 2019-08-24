package com.its.frd.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPointType;
import com.its.frd.entity.MesPoints;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverPointService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesPointGatewayService;
import com.its.frd.service.MesPointTypeService;
import com.its.frd.service.MesPointsService;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/mesPointGateway")
public class MesPointGatewayController {
	private final String PAGEPRE = "mesPointGateway/"; 

	@Resource
	private MesPointGatewayService pointGatewayServ;
	@Resource
	private CompanyinfoService cpinfoServ;
	@Resource
	private  MesDriverService mesdriverServ;
	@Resource
	private MesPointTypeService pointTypeServ;
	@Resource
	private MesPointsService mesPointsServ;
	@Resource
	private MesDriverPointService mdpointServ;
	@Resource
	private RedisService redisServ;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;

	@Autowired
	protected SimpMessagingTemplate simTemplate;

	@RequestMapping("/gatewayStatus")
	@ResponseBody
	public String getGatewayStatus(String mac){
		return this.getGatewayStatus(mac);
	}

	@RequiresPermissions("gateway:view")
	@RequestMapping("/pointGatewayPage")
	public String pointGatewayList() {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		/*if(companyId == null)
			return "error/403";*/
		return PAGEPRE + "pointGatewaylist";
	}

	@RequiresPermissions("gateway:viewPoint")
	@RequestMapping("/mesPointList/{pointGatewayId}")
	public String mesPointList (@PathVariable Long pointGatewayId,Map<String,Object> map) {
		map.put("pointGatewayId", pointGatewayId);
		map.put("pointGaqteway", pointGatewayServ.findById(pointGatewayId));
		return  PAGEPRE + "mesPointList";
	}

	/**
	 * 网关data
	 */
	@RequestMapping("/data")
	@ResponseBody
    public String pointGatewayListData(HttpServletRequest request, Page page) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<SearchFilter> filters = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getParameter("verify"))) {
            filters.add(new SearchFilter("companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()));
        }
        Specification<MesPointGateway> specification = DynamicSpecifications.bySearchFilter(request,
                MesPointGateway.class, filters);
        List<MesPointGateway> mesPoints = pointGatewayServ.findPage(specification, page);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        mesPoints = this.fillGatewayStatus(mesPoints);

        for (MesPointGateway gateway : mesPoints) {
            gateway.setGateWayStatus(this.getGatewayStatusByMacNew(gateway.getMac()));
        }

        map.put("pointGateways", mesPoints);
        return mapper.writeValueAsString(map);
    }

    /**
     * 通过mac值来获取网关状态
     * @param mac
     * @return
     */
    private String getGatewayStatusByMacNew(String mac){
        if(mac == null || "".equals(mac.trim()))
            return null;
        /*if(!redisServ.checkConnection()){
            return null;
        }*/
        String conStats = String.valueOf(redisServ.getHash("mac_status_result", mac));
        if(StringUtils.isNotEmpty(conStats)) {
            if("0".equals(conStats)) {
                return "正常";
            } else {
                return "停止";
            }
        } else {
            return "中继异常";
        }
        
//        JSONObject macConStatus = JSONObject.fromObject(redisServ.getHash("mac_status_result", mac));
//        if (null != macConStatus && 0 < macConStatus.size()) {
//            // 最后check的时间点，当前作为保留字段
//            // String lastCheckTime = String.valueOf(macConStatus.get("time"));
//            String conStats = String.valueOf(macConStatus.get("status"));
//            if(StringUtils.isNotEmpty(conStats)) {
//                if("0".equals(conStats)) {
//                    return "正常";
//                } else {
//                    return "停止";
//                }
//            } else {
//                return "中继异常";
//            }
//        } else {
//            return "中继异常";
//        }
    }

	/**
	 * 遍历网关列表,并设置网关状态
	 * @param gateways
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	private List<MesPointGateway> fillGatewayStatus(List<MesPointGateway> gateways){
		for(MesPointGateway gateway : gateways){
			gateway.setGateWayStatus(this.getGatewayStatusByMac(gateway.getMac()));
		}
		return gateways;
	}

	/**
	 * 通过mac值来获取网关状态
	 * @param mac
	 * @return
	 */
	private String getGatewayStatusByMac(String mac){
		if(mac == null || "".equals(mac.trim()))
			return null;
		/*if(!redisServ.checkConnection()){
			return null;
		}*/
		boolean flag = true;
		Map<String,Object> map = redisServ.getHash(mac);
		if(map != null && map.size() > 0){
			for(String key : map.keySet()){
				Object obj = map.get(key);
				if(obj != null){
					if("0".equals((String)obj)){
						flag = false;
						if(key.equals("0")){
							return "网关异常";
						}
						return "中继:"+key+"异常";
					}
				}else{
					if(key.equals("0")){
						return "网关状态数据获取失败";
					}else{
						return "中继:"+key+"状态数据获取失败";
					}
				}
			}
		}else{
			flag = false;
		}
		if(flag){
			return "正常";
		}
		return "停止";
	}

	/**
	 * 网关测点data
	 */
	@RequestMapping("/mesPointsData/{pointGatewayId}")
	@ResponseBody
	public String mesPointsData(@PathVariable Long pointGatewayId,HttpServletRequest request, Page page) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesDriverPoints> specification = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class,
				new SearchFilter("mesDriver.sn",Operator.EQ,pointGatewayServ.findById(pointGatewayId).getMac()),
				new SearchFilter("mesDriver.differencetype", Operator.EQ,"0" ),
				new SearchFilter("mesPoints.mesPointGateway.id",Operator.EQ,pointGatewayId)
				);
		List<MesDriverPoints> mesDriverPoints = mdpointServ.findPage(specification, page);

		/*Specification<MesPoints> specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
				new SearchFilter("mesPointGateway.id", Operator.EQ, pointGatewayId)
				);
		List<MesPoints> mesPoints = mesPointsServ.findPage(specification, page);*/
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesDriverPoints", mesDriverPoints);
		return mapper.writeValueAsString(map);
	}

	/**
	 * 删除网关
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("gateway:delete")
	@RequestMapping("/delById")
	@ResponseBody
	public String delById(Long[] ids){
		try {
			String macS = "";
			for (int i = 0; i < ids.length; i++) {
				String mac = pointGatewayServ.findById(ids[i]).getMac();
				MesDriver driver = mesdriverServ.findBySn(mac);
				if(driver!=null){
					mesdriverServ.deleteById(driver.getId());
				}
				pointGatewayServ.deleteById(ids[i]);
				redisServ.del(mac);
				// mesPointsTemplateService.sendTemplate(mac);
				
				macS += (mac + ",");
			}
			macS = macS.substring(0, macS.lastIndexOf(","));
			JSONObject obj = new JSONObject();
			obj.put("type", "1");
			obj.put("mac", macS);
			obj.put("pointId", "");
			simTemplate.convertAndSend( "/showMonitor/advise/template", obj);
		} catch (Exception e) {
			return AjaxObject.newError("删除网关失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除网关成功！").toString();
	}

	/**
	 * 删除网关测点
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("gateway:deletePoint")
	@RequestMapping("/deleteMesPointById")
	@ResponseBody
	public String deleteMesPointById( Long[] ids){
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				MesDriverPoints mesDriverPoints = mdpointServ.findById(ids[i]);
				MesPoints mesPoints = mesPointsServ.findById(mesDriverPoints.getMesPoints().getId());
				mdpointServ.deleteById(ids[i]);
				mesPointsServ.deleteById(mesPoints.getId());
				String template = mesPointsTemplateService.getTemplate(mesPoints);
				MesPointGateway gateway = mesPoints.getMesPointGateway();
				macs.add(gateway.getMac());
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), template);
				redisServ.setHash(key, hash);
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("删除网关测点失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除网关测点成功！").toString();
	}
	@RequiresPermissions(value={"verifyGateway:save","verifyGateway:edit"},logical=Logical.OR)
	@RequestMapping("/verifyGateway")
	@ResponseBody
	public String verifyGateway(MesPointGateway pointGateway){
		if(SecurityUtils.getShiroUser().getCompanyid() == null)
			return AjaxObject.newError("公司不存在！").setCallbackType("").toString();
		MesPointGateway gateway = pointGatewayServ.findAsSingle(pointGateway);
		if(null == gateway){
			return AjaxObject.newError("您输入的编码不存在！").setCallbackType("").toString();
		}
		if(gateway.getIsActive().equals(MesPointGateway.ACTIVE_YES)){
			return AjaxObject.newError("该网关已验证过！").setCallbackType("").toString();
		}
		gateway.setCompanyinfo(cpinfoServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
		gateway.setIsActive(MesPointGateway.ACTIVE_YES);
		pointGatewayServ.saveOrUpdate(gateway);
		//更新本网关在设备表里的状态
		MesDriver driver = mesdriverServ.findBySn(gateway.getMac());
		if (driver==null) {
			driver = new MesDriver();
			driver.setSn(gateway.getMac());
			driver.setName(gateway.getName());
		}
		driver.setCompanyinfo(cpinfoServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
		mesdriverServ.saveOrUpdate(driver);
		return AjaxObject.newOk("验证成功！").toString();
	}

	@RequiresPermissions(value={"gateway:save","gateway:edit"},logical=Logical.OR)
	@RequestMapping("/unbindGateway")
	@ResponseBody
	@SendTemplate
	public String unbindGateway(Long[] ids){
		Set<String> macs = new HashSet<>();
		for (int i = 0; i < ids.length; i++) {
			MesPointGateway gateway = pointGatewayServ.findById(ids[i]);
			if(null == gateway){
				return AjaxObject.newError("您所选的网关中有已被删除的网关！").setCallbackType("").toString();
			}
			gateway.setCompanyinfo(null);
			gateway.setIsActive(MesPointGateway.ACTIVE_NO);
			pointGatewayServ.saveOrUpdate(gateway);
			macs.add(gateway.getMac());
			for(MesPoints mesPoints : gateway.getMesPointses()){
				String template = mesPointsTemplateService.getTemplate(mesPoints);
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), template);
				redisServ.setHash(key, hash);
			}
			//更新本网关在设备表里的状态
			MesDriver driver = mesdriverServ.findBySn(gateway.getMac());
			if (driver==null) {
				driver = new MesDriver();
				driver.setSn(gateway.getMac());
				driver.setName(gateway.getName());
			}
			driver.setCompanyinfo(null);
			mesdriverServ.saveOrUpdate(driver);

		}
		for(String mac : macs){
			mesPointsTemplateService.sendTemplate(mac);
		}
		return AjaxObject.newOk("解绑成功！").toString();
	}

	/**
	 * 增或改网关
	 * @param pointGateway
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"gateway:save","gateway:edit"},logical=Logical.OR)
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(MesPointGateway pointGateway){
		String msg = "修改";
		if(pointGateway.getId() == null)
			msg = "添加";
		try {
			//if(SecurityUtils.getShiroUser().getCompanyid() == null)
			//return AjaxObject.newError("公司不存在！").setCallbackType("").toString();
			List<MesPointGateway> mpg = pointGatewayServ.findAll(null);
			List<String> macList = new ArrayList<String>();
			List<String> macNameList = new ArrayList<String>();
			for(MesPointGateway mpgs : mpg) {
				macList.add(mpgs.getMac());
				/*


				if(mpgs.getMac().equals(pointGateway.getMac())) {
					if(pointGateway.getId() == null)
						return AjaxObject.newError("MAC地址已存在！").setCallbackType("").toString();
					if(mpgs.getId().compareTo(pointGateway.getId()) != 0)
						return AjaxObject.newError("MAC地址已存在！").setCallbackType("").toString();
				}*/

			}




			List<MesPointGateway> mpg1 = pointGatewayServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
			for(MesPointGateway mpgs : mpg1) {
				macNameList.add(mpgs.getName());
				/*if(mpgs.getName().equals(pointGateway.getName())) {
					if(pointGateway.getId() == null)
						return AjaxObject.newError("网关名称已存在！").setCallbackType("").toString();
					if(mpgs.getId().compareTo(pointGateway.getId()) != 0)
						return AjaxObject.newError("网关名称已存在！").setCallbackType("").toString();
				}*/
			}
			if(pointGateway.getId() != null){
				macList.remove(pointGateway.getMac());
				macNameList.remove(pointGateway.getName());
			}
			if(macList.contains(pointGateway.getMac())){
				return AjaxObject.newError("MAC地址已存在！").setCallbackType("").toString();
			}
			if(macNameList.contains(pointGateway.getName())){
				return AjaxObject.newError("网关名称已存在！").setCallbackType("").toString();
			}
			/*
			 * 在添加网关的同时,需要做如何工作.
			 * 1.添加网关时需要选择测点.
			 * 2.在mes_driver中加入网关数据(即把网关当做一类设备看待)
			 * 3.在mes_driver中加入网关后,需要在mes_driver_points表中加入 一条和该网关关联的属性数据.
			 * 	
			 */if(pointGateway.getId() == null) {
				 //1 存数据到网关表
				 //pointGateway.setCompanyinfo(cpinfoServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
				 pointGateway.setCreateTime(new Timestamp(new Date().getTime()));
				 pointGatewayServ.saveOrUpdate(pointGateway);
				 //2.存数据到设备表
				 MesDriver driver = new MesDriver();
				 driver.setSn(pointGateway.getMac());
				 driver.setName(pointGateway.getName());
				 //5-12 客户邮件对应：网关创建时不分配给任何企业
				 //driver.setCompanyinfo(cpinfoServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
				 driver.setDifferencetype("0");
				 mesdriverServ.saveOrUpdate(driver);
				 //3.存数据到设备测点属性表mes_driver_points
			 }else {
				 // pointGateway.setCompanyinfo(cpinfoServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
				 MesPointGateway oldMesPointGateway = pointGatewayServ.findById(pointGateway.getId());
				 //保存到driver表
				 List<MesDriver> driver = mesdriverServ.findByCompanyinfoidAndSnAndDif(SecurityUtils.getShiroUser().getCompanyid(),
						 oldMesPointGateway.getMac(),"0");
				 if(driver.size() > 0){
					 for(MesDriver dr : driver){
						 dr.setSn(pointGateway.getMac());
						 dr.setName(pointGateway.getName());
						 dr.setCompanyinfo(cpinfoServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
						 dr.setDifferencetype("0");
						 mesdriverServ.saveOrUpdate(dr);
					 }
				 }
				 pointGatewayServ.saveOrUpdate(pointGateway);
				 for(MesPoints mesPoints : pointGateway.getMesPointses()){
					 String template = mesPointsTemplateService.getTemplate(mesPoints);
					 String key = MesPointsTemplate.class.getSimpleName()+"_"+pointGateway.getMac();
					 Map<String, Object> hash = redisServ.getHash(key);
					 if(hash == null){
						 hash = new HashMap<String,Object>();
					 }
					 hash.put(mesPoints.getCodekey(), template);
					 redisServ.setHash(key, hash);
				 }
//			   pointGateway.setCreateTime(new Timestamp(new Date().getTime()));
	           JSONObject obj = new JSONObject();
	            obj.put("type", "1");
	            obj.put("mac", pointGateway.getMac());
	            obj.put("pointId", "");
	            simTemplate.convertAndSend( "/showMonitor/advise/template", obj);
			 }

			// mesPointsTemplateService.sendTemplate(pointGateway.getMac());
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError( msg + "网关失败！").toString();
		}		
		return AjaxObject.newOk(msg + "网关成功！").toString();
	}

	/**
	 * 增或改网关测点
	 * @param mesPoints
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"gateway:savePoint","gateway:editPoint"},logical=Logical.OR)
	@RequestMapping("/saveMesPoint")
	@ResponseBody
	public String saveMesPoint(MesPoints mesPoints,HttpServletRequest request,Page page) {
		String msg = "修改";
		if(mesPoints.getId() == null)
			msg = "添加";
		Specification<MesPoints> specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class
				,new SearchFilter("mesPointGateway.id", Operator.EQ, mesPoints.getMesPointGateway().getId())
				);
		List<MesPoints> mp = mesPointsServ.findPage(specification, page);
		for(MesPoints mps : mp) {
			if(mps.getName().equals(mesPoints.getName())) {
				if(mesPoints.getId() == null)
					return AjaxObject.newError("测点名称已存在！").setCallbackType("").toString();
				if(mps.getId().compareTo(mesPoints.getId()) != 0)
					return AjaxObject.newError("测点名称已存在！").setCallbackType("").toString();
			}
		}
		try {
			if(mesPoints.getId() == null) {
				mesPoints.setMesPointType(pointTypeServ.findById(17l));
				mesPoints.setStatus(MesPoints.UNBINDING);
				mesPointsServ.saveOrUpdate(mesPoints);
				MesPointGateway mesPointGateway = pointGatewayServ.findById(mesPoints.getMesPointGateway().getId());
				Long companyId = SecurityUtils.getShiroUser().getCompanyid();
				MesDriverPoints mesDriverPoints = new MesDriverPoints();
				List<MesDriver> driver = mesdriverServ.findByCompanyinfoidAndSnAndDif(companyId
						,mesPointGateway.getMac(),"0");
				if(driver.size() > 0){
					for(MesDriver dr : driver){
						mesDriverPoints.setMesDriver(dr);
					}
				}
				mesDriverPoints.setMesPoints(mesPoints);
				mdpointServ.saveOrUpdate(mesDriverPoints);
			}else {
				mesPoints.setMesPointType(pointTypeServ.findById(17l));
				mesPoints.setStatus(MesPoints.UNBINDING);
				mesPointsServ.saveOrUpdate(mesPoints);

				/*MesPoints oldMesPoint = mesPointsServ.findById(mesPoints.getId());
			MesPointGateway mesPointGateway = pointGatewayServ.findById(oldMesPoint.getMesPointGateway().getId());
			MesDriver driver = mesdriverServ.findByCompanyinfoidAndSn(SecurityUtils.getShiroUser().getCompanyid(),
					mesPointGateway.getMac());
			MesDriverPoints mesDriverPoints = mdpointServ.findByMesPointIdAndMesDriverId(oldMesPoint.getId(), driver.getId());*/
			}
			String template = mesPointsTemplateService.getTemplate(mesPoints);
			MesPointGateway gateway = mesPoints.getMesPointGateway();
			String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
			Map<String, Object> hash = redisServ.getHash(key);
			if(hash == null){
				hash = new HashMap<String,Object>();
			}
			hash.put(mesPoints.getCodekey(), template);
			redisServ.setHash(key, hash);
			mesPointsTemplateService.sendTemplate(gateway.getMac());
		}catch(Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "网关测点失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "网关测点成功!").toString();
	}

	@RequiresPermissions("gateway:save")
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String preCreate(Map<String, Object> map) {
		return PAGEPRE+"create";
	}
	@RequiresPermissions("verifyGateway:save")
	@RequestMapping(value="/createVerify", method=RequestMethod.GET)
	public String createVerify(Map<String, Object> map) {
		return PAGEPRE+"verify";
	}
	@RequiresPermissions("verifyGateway:verify")
	@RequestMapping(value="/verify", method=RequestMethod.GET)
	public String verify(Map<String, Object> map) {
		map.put("verify", "verify");
		return PAGEPRE + "pointGatewaylistVerify";
		//return PAGEPRE+"verify";
	}
	@RequiresPermissions("gateway:savePoint")
	@RequestMapping("/addMesPoint/{pointGatewayId}")
	public String addMesPoint(@PathVariable Long pointGatewayId,HttpServletRequest request,Page page,Map<String,Object> map) {
		Specification<MesPointType> specification =DynamicSpecifications.bySearchFilter(request, MesPointType.class);
		List<MesPointType> mesPointType = pointTypeServ.findPage(specification, page);
		map.put("mesPointType", mesPointType);
		map.put("pointGatewayId", pointGatewayId);
		return PAGEPRE + "addMesPoint";
	}
	/**
	 * 根据id查找网关信息
	 */
	@RequiresPermissions("gateway:edit")
	@RequestMapping("/findById/{id}")
	public String findById(@PathVariable Long id,Model model){
		model.addAttribute("pointGateway",pointGatewayServ.findById(id));
		return PAGEPRE + "update";
	}
	/**
	 * 根据id查找网关测点信息
	 */
	@RequiresPermissions("gateway:editPoint")
	@RequestMapping("/findMesPointById/{id}")
	public String findMesPointById(@PathVariable Long id,Model model,HttpServletRequest request,Page page,Map<String,Object> map) {
		MesDriverPoints mesDriverPoints = mdpointServ.findById(id);
		MesPoints mesPoints = mesPointsServ.findById(mesDriverPoints.getMesPoints().getId());
		model.addAttribute("mesPoints", mesPoints);
		Specification<MesPointType> specification =DynamicSpecifications.bySearchFilter(request, MesPointType.class);
		List<MesPointType> mesPointType = pointTypeServ.findPage(specification, page);
		map.put("mesPointType", mesPointType);
		return PAGEPRE + "editMesPoint";
	}
}
