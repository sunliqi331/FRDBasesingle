package com.its.frd.controller;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.exception.ServiceException;
import com.its.common.service.DictionaryService;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverAlarm;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesDrivertype;
import com.its.frd.entity.MesDrivertypeProperty;
import com.its.frd.entity.MesPointCheckData;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedurePropertyPoint;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverAlarmService;
import com.its.frd.service.MesDriverPointService;
import com.its.frd.service.MesDriverPropertyService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesDriverTypePropertyService;
import com.its.frd.service.MesDriverTypeService;
import com.its.frd.service.MesPointCheckDataService;
import com.its.frd.service.MesPointGatewayService;
import com.its.frd.service.MesPointsService;
import com.its.frd.service.MesProcedurePropertyPointService;
import com.its.frd.service.MesProductlineService;
import com.its.frd.service.MesSubdriverService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService;
import com.its.frd.util.DateUtils;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;

import io.netty.handler.codec.http.HttpRequest;
import reactor.core.dynamic.annotation.Notify;


@Controller
@RequestMapping("/driver")
public class MesDriverController {
	private final String PAGEPRE = "driver/";
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MesDriverController.class);

	@Resource
	private MesDriverService driverServ;
	@Resource
	private MesDriverPropertyService driverProServ;
	@Resource
	private CompanyinfoService cpService;
	@Resource
	private MesSubdriverService subDriServ;
	@Resource
	private MesDriverTypeService mdtServ;
	@Resource
	private MesDriverTypePropertyService mdtpServ;
	@Resource
	private MesPointGatewayService mesPointGatewayService;
	@Resource
	private MesPointsService mesPointsService;
	@Resource
	private MesDriverPointService mesDriverPointService;
	@Resource
	private MesProductlineService mesProductlineService;
	@Resource
	private MesPointCheckDataService mesPointCheckDataService;
	@Resource
	private RedisService redisServ;
	@Resource
	private MesDriverAlarmService mesDriverAlarmService;
	@Resource
	private ProductAndEnergyAndDriverChartService productChartServ;
	@Resource
	private CompanyfileService companyfileService;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;
	@Resource
	private MesProcedurePropertyPointService mesProcedurePropertyPointService;
	@Resource
	private DictionaryService dictionaryService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parse(text));
			}
		});
	}

	@RequestMapping("/driverStatus/{driverId}")
	@ResponseBody
	public String driverStatus(@PathVariable Long driverId){
		JSONObject jsonObject = new JSONObject();
		//客户要求变更：状态显示背景颜色
		return jsonObject.put("status", this.getDriverStatusCodeAndColor(driverId)).toString();
	}

	@RequiresPermissions("driverList:view")
	@RequestMapping("/driverList")
	public String driverList (HttpServletRequest request,Page page,Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
		Specification<MesDrivertype> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertype.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				);
		List<MesDrivertype> mesDrivertypes = mdtServ.findPage(specification,page);
		map.put("mesDrivertypes", mesDrivertypes);
		map.put("companyinfos", cpService.getTreeFactory());
		return PAGEPRE + "driverList";
	}

	@RequiresPermissions("driverList:viewPoint")
	@RequestMapping("/driverPointList/{driverId}")
	public String driverPointList (@PathVariable Long driverId,HttpServletRequest request,Page page,Map<String, Object> map) {
		map.put("driverId", driverId);
		map.put("driver", driverServ.findById(driverId));
		return PAGEPRE + "driverPointList";
	}

	@RequiresPermissions("driverList:show")
	@RequestMapping("/viewDriver/{driverId}")
	public String viewDriver (@PathVariable Long driverId,Map<String, Object> map) {
		map.put("driverId", driverId);
		map.put("driver", driverServ.findById(driverId));
		return PAGEPRE + "viewDriver";
	}


	@RequiresPermissions("driverList:save")
	@RequestMapping("/addDriver")
	public String addDriver (Page page,Map<String,Object> map,HttpServletRequest request) {
		Specification<MesDrivertype> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertype.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
		List<MesDrivertype> mesDrivertypes = mdtServ.findAll(specification);
//		List<MesDrivertype> mesDrivertypes = mdtServ.findPage(specification,page);
		map.put("companyid", SecurityUtils.getShiroUser().getCompanyid());
		map.put("mesDrivertypes", mesDrivertypes);
		return PAGEPRE + "addDriver";
	}

	@RequiresPermissions("driverList:savePoint")
	@RequestMapping("/addDriverPoint/{driverId}/{pointtypekey}")
	public String addDriverPoint(@PathVariable Long driverId,@PathVariable Long pointtypekey,Map<String,Object> map,HttpServletRequest request,Page page) {
		MesDriver mesDriver = driverServ.findById(driverId);
		List<MesDrivertypeProperty> mesDrivertypeProperties = mdtpServ.findByMesDrivertypeId(mesDriver.getMesDrivertype().getId());
		Specification<MesPointGateway> specification = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
		List<MesPointGateway> mesPointGateways = mesPointGatewayService.findPage(specification, page);
		List<MesDrivertypeProperty> mesDrivertypeProperties2 = new ArrayList<MesDrivertypeProperty>();
		for(MesDrivertypeProperty mesDrivertypeProperty : mesDrivertypeProperties){
			List<MesDriverPoints> mesDriverPoints = mesDriverPointService.findByMesDrivertypePropertyAndMesDriver(mesDrivertypeProperty.getId(), driverId);
			if(mesDriverPoints.size()==0){
				mesDrivertypeProperties2.add(mesDrivertypeProperty);
			}
		}
		boolean HPK = false;
		boolean HMK = false;
		if(pointtypekey==4){
			HPK = true;
		}
		if(pointtypekey==4){
			HMK = true;
		}
		map.put("MesDrivertypeProperties", mesDrivertypeProperties2 );
		map.put("mesPointGateways", mesPointGateways);
		map.put("companyid", SecurityUtils.getShiroUser().getCompanyid());
		map.put("driverId", driverId);
		map.put("pointtypekey", pointtypekey);
		map.put("HPK", HPK);
		map.put("HMK", HMK);
		return PAGEPRE + "addDriverPoint";
	}

	@RequiresPermissions("driverList:editPoint")
	@RequestMapping("/editDriverPoint/{driverPointId}/{driverId}")
	public String editDriverPoint(@PathVariable Long driverPointId,@PathVariable Long driverId,Map<String,Object> map,Page page) {
		MesDriver mesDriver = driverServ.findById(driverId);
		MesDriverPoints mesDriverPoints = mesDriverPointService.findById(driverPointId);
		List<MesDrivertypeProperty> mesDrivertypeProperties = mdtpServ.findByMesDrivertypeId(mesDriver.getMesDrivertype().getId());
		List<MesDrivertypeProperty> mesDrivertypeProperties2 = new ArrayList<MesDrivertypeProperty>();
		for(MesDrivertypeProperty mesDrivertypeProperty : mesDrivertypeProperties){
			List<MesDriverPoints> mesDriverPoints2 = mesDriverPointService.findByMesDrivertypePropertyAndMesDriver(mesDrivertypeProperty.getId(), driverId);
			if(mesDriverPoints2.size()==0){
				mesDrivertypeProperties2.add(mesDrivertypeProperty);
			}
		}
		mesDrivertypeProperties2.add(mesDriverPoints.getMesDrivertypeProperty());
		map.put("MesDrivertypeProperties", mesDrivertypeProperties2 );
		map.put("mesDriverPoints", mesDriverPoints);
		return PAGEPRE + "editDriverPoint";
	}

	@RequiresPermissions("driverList:editPoint")
	@RequestMapping("/configureDriverPoint/{MesDriverPointId}/{pointtypekey}")
	public String configureDriverPoint(@PathVariable Long MesDriverPointId,@PathVariable Long pointtypekey,Map<String,Object> map,HttpServletRequest request,Page page) {
		map.put("mesDriverPoint", mesDriverPointService.findById(MesDriverPointId));
		return PAGEPRE + "configureDriverPoint";
	}

	/**
	 * 设备分页
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverData")
	@ResponseBody
	public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
		List<MesDriver> list = new ArrayList<MesDriver>();
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
				new SearchFilter("companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid())
				,new SearchFilter("differencetype",Operator.NOTEQ,"0")
				);
		list = driverServ.findPage(specification, page);
		list = this.getDriverListForStatus(list);
		for(MesDriver mesDriver : list){
			mesDriver.setButton("<a id='DriverData"+mesDriver.getId()+"' refresh='true' onclick='searchDriverData(" + mesDriver.getId() + ",this)'><i class='fa fa-eye'></i>&nbsp;查看数据</a>");
			mesDriver.setConfigPoint("<a id='DriverData"+mesDriver.getId()+"' refersh='true' onclick='configPoint(" + mesDriver.getId() + ",this)'><i class='fa fa-cogs'></i>&nbsp;配置测点</a>");
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		map.put("page", page);
		map.put("mesDrivers", list);

		return mapper.writeValueAsString(map);
	}

	/**
	 * 设备分页
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/MesPointCheckDataData/{MesDriverPointId}")
	@ResponseBody
	public String MesPointCheckDataData(@PathVariable Long MesDriverPointId,HttpServletRequest request, Page page) throws JsonProcessingException {
		List<MesPointCheckData> list = new ArrayList<MesPointCheckData>();
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesPointCheckData> specification = DynamicSpecifications.bySearchFilter(request, MesPointCheckData.class,
				new SearchFilter("mesDriverPoints.id", Operator.EQ, MesDriverPointId)
				);
		list = mesPointCheckDataService.findPage(specification, page);
		for(MesPointCheckData mesPointCheckData : list){
			mesPointCheckData.setColor("<span style='width:16px; height：16px; display:block; background:"+mesPointCheckData.getColorcode()+"'>&nbsp;</span>");
			if (mesPointCheckData.getCompanyfileId()!=null) {
				mesPointCheckData.setCompanyfilePath("<img style='height:30px; width:40px' src="+request.getServletContext().getContextPath()+"/company/showPic/" + mesPointCheckData.getCompanyfileId() +">");
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		map.put("page", page);
		map.put("mesPointCheckDatas", list);

		return mapper.writeValueAsString(map);
	}

	
	//updatedBy:xsq 7.11
	
	
	/**
	 * 设备属性分页-设备状态测点
	 * @param request
	 * @param page	
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverPointData/{driverId}")
	@ResponseBody
	public String driverPointData(@PathVariable Long driverId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		//updatedBy:xsq 7.7
		//out.ckear();
		
		Specification<MesDriverPoints> specification1 = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class,
				new SearchFilter("mesDriver.id",Operator.EQ,driverId),
				new SearchFilter("mesPoints.mesPointGateway.companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.EQ,"D_STATUS")
				);
		//设备状态测点
		List<MesDriverPoints> statusMesDriverPoints = mesDriverPointService.findPage(specification1, page);
		//System.out.println("设备状态测点记录条数："+page.getTotalCount()+"；页数："+page.getTotalPage());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("statusMesDriverPoints", statusMesDriverPoints);
		return mapper.writeValueAsString(map);
	}
	
	//设备属性测点
	
	
	/**
	 * 设备属性分页-设备属性测点
	 * @param request
	 * @param page	
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverPointData1/{driverId}")
	@ResponseBody
	public String driverPointData1(@PathVariable Long driverId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		//updatedBy:xsq 7.7
		Specification<MesDriverPoints> specification2 = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class,
				new SearchFilter("mesDriver.id",Operator.EQ,driverId),
				new SearchFilter("mesPoints.mesPointGateway.companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.EQ,"D_MONITOR")
				);
		//设备属性测点
		List<MesDriverPoints> propertyMesDriverPoints = mesDriverPointService.findPage(specification2, page);
		//System.out.println("产品属性测点"+page.getTotalCount()+"；页数："+page.getTotalPage());
		for(MesDriverPoints mesDriverPoints : propertyMesDriverPoints){
			if(mesDriverPoints.getValidation()!=null){
				if(mesDriverPoints.getValidation().equals("1")){
					mesDriverPoints.setTvalidation("是");
				}else{
					mesDriverPoints.setTvalidation("否");
				}
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("propertyMesDriverPoints", propertyMesDriverPoints);
		return mapper.writeValueAsString(map);
	}
	
	
	//设备工序测点
	
	
	/**
	 * 设备属性分页-设备工序测点
	 * @param request
	 * @param page	
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverPointData2/{driverId}")
	@ResponseBody
	public String driverPointData2(@PathVariable Long driverId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		//updatedBy:xsq 7.7
		Specification<MesDriverPoints> specification3 = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class,
				new SearchFilter("mesDriver.id",Operator.EQ,driverId),
				new SearchFilter("mesPoints.mesPointGateway.companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.EQ,"P_PROCEDURE")
				);
		//产品工序测点
		List<MesDriverPoints> procedureMesDriverPoints = mesDriverPointService.findPage(specification3, page);
		//System.out.println("产品工序测点"+page.getTotalCount()+"；页数："+page.getTotalPage());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("procedureMesDriverPoints", procedureMesDriverPoints);
		return mapper.writeValueAsString(map);
	}
	
	//统计类测点
	
	
	/**
	 * 设备属性分页-统计类测点
	 * @param request
	 * @param page	
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverPointData3/{driverId}")
	@ResponseBody
	public String driverPointData3(@PathVariable Long driverId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		//updatedBy:xsq 7.7
		Specification<MesDriverPoints> specification4 = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class,
				new SearchFilter("mesDriver.id",Operator.EQ,driverId),
				new SearchFilter("mesPoints.mesPointGateway.companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.NOTEQ,"D_STATUS"),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.NOTEQ,"D_MONITOR"),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.NOTEQ,"P_PROCEDURE"),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.NOTEQ,"POINT_GATEWAY"),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.NOTEQ,"ALARM_SHOW")
				);
		//统计类测点
		List<MesDriverPoints> statisticalMesDriverPoints = mesDriverPointService.findPage(specification4, page);
		//System.out.println("统计类测点"+page.getTotalCount()+"；页数："+page.getTotalPage());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("statisticalMesDriverPoints", statisticalMesDriverPoints);
		return mapper.writeValueAsString(map);
	}
	/**
	 * 设备告警分页-告警类测点
	 * @param request
	 * @param page	
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverPointData4/{driverId}")
	@ResponseBody
	public String driverPointData4(@PathVariable Long driverId,HttpServletRequest request,Page page) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesDriverPoints> specification5 = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class,
				new SearchFilter("mesDriver.id",Operator.EQ,driverId),
				new SearchFilter("mesPoints.mesPointGateway.companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.EQ,"Alarm_Show")
				//new SearchFilter("mesPoints.mesPointType.pointtypekey",Operator.EQ,"Alarm_Show")
				);
		List<MesDriverPoints> alarmMesDriverPoints = mesDriverPointService.findPage(specification5, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("alarmMesDriverPoints", alarmMesDriverPoints);
		return mapper.writeValueAsString(map);		
	}
	
	
	
	
	/**
	 * 添加数据，保存设备
	 * @param MesProductline
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"driverList:save","driverList:edit"},logical=Logical.OR)
	@RequestMapping("/saveEditDriver")
	@ResponseBody
	public String saveEditDriver(@Valid MesDriver mesDriver, HttpServletRequest request) {
		MesDriver oldMesDriver = new MesDriver();
		String msg = "修改";
		if (mesDriver.getId() == null) {
			msg = "添加";
		}
		try {
			if (mesDriver != null) {
				mesDriver.setCompanyinfo(cpService.findById(mesDriver.getCompanyinfo().getId()));
			}
			if(mesDriver.getId()!=null){
				oldMesDriver = driverServ.findById(mesDriver.getId());
				oldMesDriver.setSn(mesDriver.getSn());
				oldMesDriver.setName(mesDriver.getName());
				oldMesDriver.setDescription(mesDriver.getDescription());
				oldMesDriver.setLeavefactorydate(mesDriver.getLeavefactorydate());
				oldMesDriver.setModelnumber(mesDriver.getModelnumber());
				oldMesDriver.setMesDrivertype(mesDriver.getMesDrivertype());
				oldMesDriver.setBrand(mesDriver.getBrand());
				/* mesDriver.setMesProductline(oldMesDriver.getMesProductline());
                if(mesDriver.getMesDrivertype()!=oldMesDriver.getMesDrivertype()){
                    List<MesDriverPoints> mesDriverPoints1 = mesDriverPointService.findByMesDriverId(mesDriver.getId());
                    List<Long> ids = new ArrayList<Long>();
                    for(MesDriverPoints mesDriverPoint : mesDriverPoints1){
                        ids.add(mesDriverPoint.getId());
                    }
                    for (int i = 0; i < ids.size(); i++) {
                        MesDriverPoints mesDriverPoints = mesDriverPointService.findById(ids.get(i));
                        MesPoints mesPoints = mesDriverPoints.getMesPoints();
                        mesPoints.setStatus(MesPoints.UNBINDING);
                        mesPointsService.saveOrUpdate(mesPoints);
                        mesDriverPointService.deleteById(mesDriverPoints.getId());
                    }
                }*/
			}
			driverServ.saveOrUpdate(oldMesDriver);
			List<MesDriverPoints> mesDriverPointses = oldMesDriver.getMesDriverPointses();
			Set<String> macs = new HashSet<>();
			for (MesDriverPoints mesDriverPoints : mesDriverPointses) {
				MesDriverPoints mesDriverPoints_ = mesDriverPointService.findById(mesDriverPoints.getId());
				MesPoints mesPoints = mesDriverPoints_.getMesPoints();
				MesPointGateway gateway = mesPoints.getMesPointGateway();
				macs.add(gateway.getMac());
				String result = mesPointsTemplateService.getTemplate(mesPoints);
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), result);
				redisServ.setHash(key, hash);
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "设备失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "设备成功!").toString();
	}
	/**
	 * 添加数据，保存设备
	 * @param MesProductline
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"driverList:save","driverList:edit"},logical=Logical.OR)
	@RequestMapping("/saveDriver")
	@ResponseBody
	public String saveDriver(@Valid MesDriver mesDriver, HttpServletRequest request) {
		MesDriver oldMesDriver = new MesDriver();
		String msg = "修改";
		if (mesDriver.getId() == null) {
			msg = "添加";
		}
		try {
			if (mesDriver != null) {
				mesDriver.setCompanyinfo(cpService.findById(mesDriver.getCompanyinfo().getId()));
			}
			if(mesDriver.getId()!=null){
				oldMesDriver = driverServ.findById(mesDriver.getId());
				mesDriver.setMesProductline(oldMesDriver.getMesProductline());
				if(mesDriver.getMesDrivertype()!=oldMesDriver.getMesDrivertype()){
					List<MesDriverPoints> mesDriverPoints1 = mesDriverPointService.findByMesDriverId(mesDriver.getId());
					List<Long> ids = new ArrayList<Long>();
					for(MesDriverPoints mesDriverPoint : mesDriverPoints1){
						ids.add(mesDriverPoint.getId());
					}
					for (int i = 0; i < ids.size(); i++) {
						MesDriverPoints mesDriverPoints = mesDriverPointService.findById(ids.get(i));
						MesPoints mesPoints = mesDriverPoints.getMesPoints();
						mesPoints.setStatus(MesPoints.UNBINDING);
						mesPointsService.saveOrUpdate(mesPoints);
						mesDriverPointService.deleteById(mesDriverPoints.getId());
					}
				}
			}
			MesDriver mesDriver_ = driverServ.saveOrUpdate(mesDriver);
			List<MesDriverPoints> mesDriverPointses = mesDriver_.getMesDriverPointses();
			Set<String> macs = new HashSet<>();
			for (MesDriverPoints mesDriverPoints : mesDriverPointses) {
				MesPoints mesPoints = mesDriverPoints.getMesPoints();
				MesPointGateway gateway = mesPoints.getMesPointGateway();
				macs.add(gateway.getMac());
				String result = mesPointsTemplateService.getTemplate(mesPoints);
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), result);
				redisServ.setHash(key, hash);
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "设备失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "设备成功!").toString();
	}

	/**
	 * 添加数据，保存设备
	 * @param MesProductline
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("driverList:savePoint")
	@RequestMapping("/saveDriverPoint/{driverId}")
	@ResponseBody
	public String saveDriverPoint(@Valid MesDriverPoints MesDriverPoint,@PathVariable Long driverId,HttpServletRequest request){
		MesDriver mesDriver = driverServ.findById(driverId);
		try {
			if(mesDriver != null){
				mesDriver.setCompanyinfo(cpService.findById(mesDriver.getCompanyinfo().getId()));
			}
			//从前台获得添加的测点,类型为  测点-类型属性,测点-类型属性
			String str = request.getParameter("mesPointIds");
			String[] arr1 = str.split("[,\\-]");
			Map<String, String> Pointmap = new HashMap<String, String>();
			for(int i = 1;i< arr1.length;){
				//将 测点-类型属性存入map
				Pointmap.put(arr1[i-1], arr1[i]);
				i+=2;
			}
			//判断测点是否为空
			if (!Pointmap.isEmpty()) {
				List<MesDriverPoints> mesDriverPoints = new ArrayList<MesDriverPoints>();
				List<MesPoints> mesPoints = new ArrayList<MesPoints>();
				//获取当前设备已添加的测点
				for(MesDriverPoints mesDriverPoint : mesDriverPointService.findByMesDriverId(mesDriver.getId())){
					mesPoints.add(mesDriverPoint.getMesPoints());
				}
				//迭代要添加的测点map
				Iterator<String> iter = Pointmap.keySet().iterator();
				Set<String> macs = new HashSet<>();
				while(iter.hasNext()){
					String key=iter.next();
					String value = Pointmap.get(key);
					//判断是否是新增的
					if(!mesPoints.contains(mesPointsService.findById(Long.valueOf(key)))){
						MesDriverPoints mesDriverPoint = new MesDriverPoints();
						MesPoints mesPoint = mesPointsService.findById(Long.valueOf(key));
						mesPoint.setStatus(MesPoints.BINDING);
						mesDriverPoint.setMesPoints(mesPoint);
						if(!value.equals("undefined")){
							MesDrivertypeProperty mesDrivertypeProperty = mdtpServ.findById(Long.valueOf(value));
							mesDriverPoint.setMesDrivertypeProperty(mesDrivertypeProperty);
							mesPoint.setUnits(mesDrivertypeProperty.getUnits());
						}
						mesDriverPoint.setMesDriver(mesDriver);
						mesDriverPoint.setMaxValue(MesDriverPoint.getMaxValue());
						mesDriverPoint.setMinValue(MesDriverPoint.getMinValue());
						mesDriverPoint.setValidation(MesDriverPoint.getValidation());
						mesDriverPointService.saveOrUpdate(mesDriverPoint);
						String template = mesPointsTemplateService.getTemplate(mesPoint);
						MesPointGateway gateway = mesPoint.getMesPointGateway();
						
						String key_ = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
						Map<String, Object> hash = redisServ.getHash(key_);
						if(hash == null){
							hash = new HashMap<String,Object>();
						}
						hash.put(mesPoint.getCodekey(), template);
						redisServ.setHash(key, hash);
						macs.add(gateway.getMac());
						mesDriverPoints.add(mesDriverPoint);
						//	                  }else{
						//	                      //不是新增的
						//	                      for(MesPoints mesPoint : mesPoints){
						//	                          if(mesPoint.getId()!=Long.valueOf(key)){
						//	                              MesDriverPoints mesDriverPoint = mesDriverPointService.findByMesPointIdAndMesDriverId(mesPoint.getId(), mesDriver.getId());
						//	                              MesPoints mesPoints1 = mesDriverPoint.getMesPoints();
						//	                              mesPoints1.setStatus(MesPoints.UNBINDING);
						//	                              mesPointsService.saveOrUpdate(mesPoints1);
						//	                              mesDriverPointService.deleteById(mesDriverPoint.getId());
						//	                          }
						//	                      }
					}
				}
				mesDriver.setMesDriverPointses(mesDriverPoints);
				for(String mac : macs){
					mesPointsTemplateService.sendTemplate(mac);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("添加失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("添加成功!").toString();
	}

	/**
	 * 添加数据，保存设备
	 * @param MesProductline
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("driverList:savePoint")
	@RequestMapping("/saveEditDriverPoint")
	@ResponseBody
	public String saveEditDriverPoint(@Valid MesDriverPoints mesDriverPoint,HttpServletRequest request){
		try {
			MesDriverPoints mesDriverPoints_ = mesDriverPointService.saveOrUpdate(mesDriverPoint);
			MesPoints mesPoints = mesDriverPoints_.getMesPoints();
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
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("修改失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("修改成功!").toString();
	}


	/**
	 * 添加数据，保存设备
	 * @param MesProductline
	 * @return 
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("driverList:editPoint")
	@RequestMapping("/saveMesPointCheckData")
	@ResponseBody
	public String saveMesPointCheckData(@Valid MesPointCheckData mesPointCheckData,HttpServletRequest request, MultipartFile statusimg){
		try {
			if (statusimg.getOriginalFilename().isEmpty()) {
				mesPointCheckData.setCompanyfileId(null);
			}else {
				Companyfile companyfile = fileSave(statusimg);
				mesPointCheckData.setCompanyfileId(companyfile.getId());
			}
			mesPointCheckDataService.saveOrUpdate(mesPointCheckData);
			MesDriverPoints mesDriverPoints = mesPointCheckData.getMesDriverPoints();
			MesDriverPoints driverPoints = mesDriverPointService.findById(mesDriverPoints.getId());
			if(null != driverPoints){
				MesPoints mesPoints = driverPoints.getMesPoints();
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("添加失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("添加成功!").toString();
	}

	private Companyfile fileSave(MultipartFile file) throws IOException {
		Companyfile cpfile;
		String fileBaseName = file.getOriginalFilename();
		String newFileName = UUID.randomUUID().toString()
				+ fileBaseName.substring(fileBaseName.lastIndexOf("."));
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE/";
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new java.io.File(filePath + File.separator + newFileName);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		file.transferTo(uploadFile);
		cpfile = new Companyfile();
		cpfile.setfilebasename(fileBaseName);
		cpfile.setfilelength(file.getSize());
		cpfile.setfilenewname(newFileName);
		cpfile.setfilepath(filePath);
		return companyfileService.saveOrUpdate(cpfile);
	}

	@SendTemplate
	@RequestMapping(value = "/deleteMesPointCheckData", method = RequestMethod.POST)
	public @ResponseBody String deleteMesPointCheckData(Long[] ids) {
		try {
			Set<String> macs = new HashSet<>();
			for (Long id : ids) {
				MesPointCheckData mesPointCheckData = mesPointCheckDataService.findOne(id);
				if (mesPointCheckData!=null) {
					mesPointCheckDataService.deleteById(id);
					if (mesPointCheckData.getCompanyfileId()!=null) {
						companyfileService.deleteById(mesPointCheckData.getCompanyfileId());
					}
					MesDriverPoints mesDriverPoints = mesPointCheckData.getMesDriverPoints();
					MesDriverPoints driverPoints = mesDriverPointService.findById(mesDriverPoints.getId());
					if(null != driverPoints){
						MesPoints mesPoints = driverPoints.getMesPoints();
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
				}
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (ServiceException e) {
			return AjaxObject.newError("删除失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除成功！").setCallbackType("").toString();
	}

	@SendTemplate
	@RequiresPermissions("driverList:deletePoint")
	@RequestMapping(value = "/deleteDriverPoint", method = RequestMethod.POST)
	public @ResponseBody String deleteMany(Long[] ids) {
		String[] name = new String[ids.length];
		if(ids.length>0){
			boolean key = false;
			MesDriverPoints mDriverPoints = mesDriverPointService.findById(ids[0]);
			MesPoints mPoints = mDriverPoints.getMesPoints();
			String prodedurePointsName = "";
			if("P_PROCEDURE".equals(mPoints.getMesPointType().getPointtypekey())){
				for(int i = 0; i < ids.length; i++){
					MesDriverPoints mesDriverPoints = mesDriverPointService.findById(ids[i]);
					MesPoints mesPoints = mesDriverPoints.getMesPoints();
					List<MesProcedurePropertyPoint> findByMesPointId = new ArrayList<>();
					findByMesPointId = mesProcedurePropertyPointService.findByMesPointId(mesPoints.getId());
					if(findByMesPointId.size()>0){
						prodedurePointsName = mesPoints.getName();
						key = true;
						break;
					}
				}
			}
			if(key){
				return AjaxObject.newError(prodedurePointsName+"上绑定的有产品工序属性，无法删除!").setCallbackType("").toString();
			}
		}
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				MesDriverPoints mesDriverPoints = mesDriverPointService.findById(ids[i]);
				MesPoints mesPoints = mesDriverPoints.getMesPoints();
				mesPoints.setStatus(MesPoints.UNBINDING);
				mesPointsService.saveOrUpdate(mesPoints);
				mesDriverPointService.deleteById(mesDriverPoints.getId());
				String template = mesPointsTemplateService.getTemplate(mesPoints);
				MesPointGateway gateway = mesPoints.getMesPointGateway();
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), template);
				redisServ.setHash(key, hash);
				
				macs.add(gateway.getMac());
				name[i] = mesDriverPoints.getMesPoints().getName();
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (ServiceException e) {
			return AjaxObject.newError("删除设备测点失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除设备测点成功！").setCallbackType("").toString();
	}
	/**
	 * 根据id查找
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("driverList:edit")
	@RequestMapping("/findById/{id}")
	public String findByID(@PathVariable Long id,Model model,Map<String,Object> map,Page page,HttpServletRequest request){
		MesDriver mesDriver = driverServ.findById(id);
		List<MesDriverPoints> mesDriverPoints = mesDriver.getMesDriverPointses();
		StringBuffer sb = new StringBuffer();
		for(MesDriverPoints mesDriverPoint : mesDriverPoints){
			if(mesDriverPoint.getMesPoints()!=null){
				if(sb.length()<=0){
					sb.append(mesDriverPoint.getMesPoints().getId());
				}else{
					sb.append(","+mesDriverPoint.getMesPoints().getId());
				}
			}
		}
		Specification<MesDrivertype> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertype.class
				,new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				,new SearchFilter("id",Operator.NOTEQ,mesDriver.getMesDrivertype().getId())
				);
		List<MesDrivertype> MDt = mdtServ.findPage(specification, page);
		map.put("companyid", mesDriver.getCompanyinfo().getId());
		map.put("mdt", mesDriver.getMesDrivertype());
		map.put("MesDrivertype", MDt);
		map.put("selectedMesPoints", sb);
		model.addAttribute("mesDriver",mesDriver);
		return PAGEPRE + "editDriver";
	}

	@SendTemplate
	@RequiresPermissions("driverList:delete")
	@RequestMapping(value = "/deleteDriver", method = RequestMethod.POST)
	public @ResponseBody String deleteManyDriver(Long[] ids) {
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				MesDriver mesDriver = driverServ.findById(ids[i]);
				List<MesDriverAlarm> mesDriverAlarms = mesDriverAlarmService.findByMesDriverId(mesDriver.getId());
				mesDriverAlarmService.delete(mesDriverAlarms);
				List<MesDriverPoints> mesDriverPoints = mesDriverPointService.findByMesDriverId(ids[i]);
				for(MesDriverPoints mesDriverPoints2 : mesDriverPoints){
					mesDriverPoints2.getMesPoints().setStatus("0");
				}
				driverServ.deleteById(mesDriver.getId());
				for(MesDriverPoints mesDriverPoints2 : mesDriver.getMesDriverPointses()){
					MesPoints mesPoints = mesDriverPoints2.getMesPoints();
					//MesDriverPoints driverPoints = mesDriverPointService.findById(mesDriverPoints2.getId());
					//MesPoints mesPoints = driverPoints.getMesPoints();
					if(null != mesPoints){
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
				}

			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			return AjaxObject.newError("删除设备失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除设备成功！").setCallbackType("").toString();
	}

	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findAlarm/{ids}", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> findAlarm(@PathVariable Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (int i = 0; i < ids.length; i++) {
				List<MesDriverAlarm> mesDriverAlarms = mesDriverAlarmService.findByMesDriverId(ids[i]);
				if (mesDriverAlarms.size() > 0) {
					map.put("1", 1);
					return map;
				}
				map.put("0", 0);
			}
		} catch (Exception e) {
			return null;
		}
		return map;
	}

	@RequestMapping("/checkSn/{companyid}/{sn}")
	@ResponseBody
	public String checkSn(@PathVariable Long companyid,@PathVariable String sn) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesDriver> mesDrivers = driverServ.findByCompanyinfoidAndSnAndDif(companyid, sn, "1");
		try {
			if(mesDrivers.size()>0){
				map.put("0", 0);
			}else {
				map.put("1", 1);
			}
		} catch (Exception e) {
			return null;
		}
		return mapper.writeValueAsString(map);

	}


	@RequestMapping("/checkCheckvalue/{mesDriverPointId}/{checkvalue}")
	@ResponseBody
	public String checkCheckvalue(@PathVariable Long mesDriverPointId,@PathVariable Long checkvalue) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			if(mesPointCheckDataService.findByMesDriverPointsIdAndCheckvalue(mesDriverPointId, checkvalue).size()<1){
				map.put("1", 1);
			}else {
				map.put("0", 0);
			}
		} catch (Exception e) {
			return null;
		}
		return mapper.writeValueAsString(map);

	}

	@RequestMapping("/checkName/{mesDriverPointId}/{name}")
	@ResponseBody
	public String checkName(@PathVariable Long mesDriverPointId,@PathVariable String name) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			if(mesPointCheckDataService.findByMesDriverPointsIdAndName(mesDriverPointId, name).size()<1){
				map.put("1", 1);
			}else {
				map.put("0", 0);
			}
		} catch (Exception e) {
			return null;
		}
		return mapper.writeValueAsString(map);

	}

	/**
	 * 根据设备id添加测点
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/MesPointData/{MesPointGatewayId}/{pointtypekey}/{driverId}")
	@ResponseBody
	public String MesDriverData(@PathVariable Long MesPointGatewayId,@PathVariable Long pointtypekey,@PathVariable Long driverId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MesDriverPoints> mesDriverPoints = mesDriverPointService.findByMesDriverId(driverId);
		Specification<MesPoints> specification = null;
		if(pointtypekey==1){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.id",Operator.EQ,MesPointGatewayId),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"D_STATUS"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==4){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.id",Operator.EQ,MesPointGatewayId),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"D_MONITOR"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==6){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.id",Operator.EQ,MesPointGatewayId),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"P_PROCEDURE"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==7){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.id",Operator.EQ,MesPointGatewayId),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"D_STATUS"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"P_PROCEDURE"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"POINT_GATEWAY"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"D_MONITOR"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"ALARM_SHOW"),
					
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==8){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.id",Operator.EQ,MesPointGatewayId),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"POINT_GATEWAY"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey == 21){
			specification =DynamicSpecifications.bySearchFilter(request, MesPoints.class, 
					new SearchFilter("mesPointGateway.id",Operator.EQ,MesPointGatewayId),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"ALARM_SHOW"),
					new SearchFilter("status",Operator.EQ,"0")				
					);
		}
		List<MesPoints> mesPoints = mesPointsService.findPage(specification, page);
		List<Long> ids = new ArrayList<Long>();
		for(MesPoints mesPoints2 : mesPoints){
			ids.add(mesPoints2.getId());
		}
		for(MesDriverPoints mesDriverPoint : mesDriverPoints){
			if(ids.contains(mesDriverPoint.getMesPoints().getId())){
				ids.remove(mesDriverPoint.getMesPoints().getId());
			}
		}
		List<MesPoints> mesPoints2 = new ArrayList<MesPoints>();
		for(Long id : ids){
			mesPoints2.add(mesPointsService.findById(id));
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesPoints", mesPoints2);
		return mapper.writeValueAsString(map);
	}

	/**
	 * 根据设备id添加测点
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/MesPointData2/{name}/{pointtypekey}/{driverId}")
	@ResponseBody
	public String MesDriverData2(@PathVariable String name,@PathVariable Long pointtypekey,@PathVariable Long driverId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MesDriverPoints> mesDriverPoints = mesDriverPointService.findByMesDriverId(driverId);
		Specification<MesPoints> specification = null;
		if(pointtypekey==1){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"D_STATUS"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==4){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"D_MONITOR"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==6){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"P_PROCEDURE"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==7){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"D_STATUS"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"P_PROCEDURE"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"D_MONITOR"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"POINT_GATEWAY"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==8){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"POINT_GATEWAY"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==21){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"ALARM_SHOW"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}
		// unknown code
		/*if(pointtypekey==1){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"D_STATUS"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else{
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("name", Operator.LIKE, name),
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"D_STATUS"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"P_PROCEDURE"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}*/
		List<MesPoints> mesPoints = mesPointsService.findPage(specification, page);
		List<Long> ids = new ArrayList<Long>();
		for(MesPoints mesPoints2 : mesPoints){
			ids.add(mesPoints2.getId());
		}
		for(MesDriverPoints mesDriverPoint : mesDriverPoints){
			if(ids.contains(mesDriverPoint.getMesPoints().getId())){
				ids.remove(mesDriverPoint.getMesPoints().getId());
			}
		}
		
		List<MesPoints> mesPoints2 = new ArrayList<MesPoints>();
		for(Long id : ids){
			mesPoints2.add(mesPointsService.findById(id));
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesPoints", mesPoints2);
		return mapper.writeValueAsString(map);
	}
	
	/**
	 * 初始化展示所有可用测点
	 * @param pointtypekey
	 * @param driverId
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/MesPointDataInit/{pointtypekey}/{driverId}")
	@ResponseBody
	public String MesPointDataInit(@PathVariable Long pointtypekey,@PathVariable Long driverId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MesDriverPoints> mesDriverPoints = mesDriverPointService.findByMesDriverId(driverId);
		Specification<MesPoints> specification = null;
		if(pointtypekey==1){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"D_STATUS"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==4){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"D_MONITOR"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==6){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"P_PROCEDURE"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==7){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"D_STATUS"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"P_PROCEDURE"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"D_MONITOR"),
					new SearchFilter("mesPointType.pointtypekey",Operator.NOTEQ,"POINT_GATEWAY"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==8){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"POINT_GATEWAY"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}else if(pointtypekey==21){
			specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class,
					new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("mesPointType.pointtypekey",Operator.EQ,"ALARM_SHOW"),
					new SearchFilter("status",Operator.EQ,"0")
					);
		}
		List<MesPoints> mesPoints = mesPointsService.findPage(specification, page);
		List<Long> ids = new ArrayList<Long>();
		for(MesPoints mesPoints2 : mesPoints){
			ids.add(mesPoints2.getId());
		}
		for(MesDriverPoints mesDriverPoint : mesDriverPoints){
			if(ids.contains(mesDriverPoint.getMesPoints().getId())){
				ids.remove(mesDriverPoint.getMesPoints().getId());
			}
		}
		List<MesPoints> mesPoints2 = new ArrayList<MesPoints>();
		for(Long id : ids){
			mesPoints2.add(mesPointsService.findById(id));
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesPoints", mesPoints2);
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/getDriverStatus/{id}")
	public String getDriverStatus(@PathVariable Long id, Map<String, Object> map){
		Map<String,Object> resultMap = this.getDriverPropertyValues(id);
		if(resultMap == null ){
			System.out.println("最后查到的设备map集合元素个数为0");
		}else{
			System.out.println("最后查到的设备map集合元素个数"+resultMap.size());
		}
		boolean have = true;
		if(resultMap!=null){
			if(resultMap.size() > 0){
				map.put("have", have);
			}else{
				have = false;
				map.put("have", have);
			}
		}
		map.put("xxx", resultMap);
		return PAGEPRE + "getDriverStatus";
	}

	/**
	 * 根据设备id获取该设备属性的实时数据
	 * @param driverId
	 * @return
	 */
	public Map<String,Object> getDriverPropertyValues(Long driverId){
		if(driverId == null)
			return null;
		//获取设备属性测点map
		Map<String,String> pointsMap = this.getDriverPointsForPropertyPoint(driverId);
		System.out.println("map集合的key:"+pointsMap.keySet());
		System.out.println("map集合value:"+pointsMap.values());
		if(pointsMap == null || pointsMap.size() <= 0)
			return null;
		Map<String,Object> resultMap = new HashMap<>();
		//key:网关+测点key  value:为Json字符串,包含value只
		Map<String,Object> driverMap = redisServ.getHash("DeviceInfo_"+String.valueOf(driverId));//218可测
		
		Set<String> keyPointsMap = pointsMap.keySet();
		log.info("redis拿到的driverMap的元素个数:"+driverMap.size());
		if(driverMap != null && driverMap.size() > 0){
			String pointKey = null;
			for(String idKey : pointsMap.keySet()){
				pointKey = idKey.substring(idKey.indexOf("-")+1, idKey.length());
				for(String macKey : driverMap.keySet()){
					if(macKey.indexOf(pointKey) != -1){
						JSONObject jsonObj = new JSONObject(driverMap.get(macKey).toString());
						log.info(driverMap.get(macKey).toString());
						String electricPre = driverMap.get(macKey).toString();
						if(electricPre != null && electricPre != "" ){
							resultMap.put(pointsMap.get(idKey), jsonObj.getDouble("value"));
						}
					}
				}
			}
		}
		//    	  resultMap.put("高度(个)", "5003.153");
		return resultMap;
	}

	/**
	 * 获取设备属性测点及对应的属性名称
	 * 包含测点id+"-"+key,属性名称+单位即可.
	 * @param driverId
	 * @return
	 */
	private Map<String,String> getDriverPointsForPropertyPoint(Long driverId){
		/**
		 * 1.查询出driver_point中有propertyid的数据;
		 * 2.根据propertyid查询出属性名称;
		 */
		Map<String,String> map = new HashMap<>();
		List<MesDriverPoints> propertyPoints = mesDriverPointService.findByMesDriverIdAndMesDrivertypePropertyIdNotNull(driverId);
		for(MesDriverPoints dpoint : propertyPoints){
			System.out.println(dpoint.getId());
			System.out.println(dpoint.getMesPoints().getCodekey());
			MesDrivertypeProperty driverTypeProperty = mdtpServ.findById(dpoint.getMesDrivertypeProperty().getId());
			map.put(dpoint.getId()+"-"+dpoint.getMesPoints().getCodekey(), driverTypeProperty.getPropertyname()+"("+dictionaryService.findById(dpoint.getMesPoints().getUnitsId()).getName()+")");
			log.info("************得到数据："+dpoint.getId()+"-"+dpoint.getMesPoints().getCodekey(),"********"+ driverTypeProperty.getPropertyname()+"("+dpoint.getMesPoints().getUnits()+")");
		}
		return map;
	}

	/**
	 * 根据Id获取设备状态
	 * @param driverId
	 * @return
	 */
	private String getDriverStatusCode(Long driverId){
		/*
		 * 1.先查询出该设备关联的状态测点;
		 * 2.如果有多个状态测点,则最终结果结合多个测点的结果拼接;
		 * 2.查询出状态值及对应的名称;
		 */
		if(driverId == null)
			return "";
		String result = "";
		String id = String.valueOf(driverId);
		Map<String,Object> map = redisServ.getHash(id);
		//给设备的状态测点
		List<MesPoints> points = this.getStatusPoints(driverId);
		if(map != null && points.size() != 0){
			Set<String> keySets = map.keySet();
			//5月11日客户要求变更：每个设备上最多只会有一个状态测点
			//for(MesPoints point : points){
			MesPoints point = points.get(0);
			for(String key : keySets){
				//包含该测点
				if(key.indexOf(point.getCodekey()) != -1){
					try{
						Object obj = map.get(key);
						JSONObject jsObj = new JSONObject(obj.toString());
						Long value = jsObj.getLong("value");
						//根据value的值查看pointData中对应的是什么名称
						result += this.getPointDataValueName(this.getDriverPointId(point.getId()), value);
					}catch(Exception e){

					}
				}
			}
			//}
			return result;
		}
		return "未定义";
	}

	/**
	 * 根据Id获取设备状态对应颜色
	 * @param driverId
	 * @return
	 */
	private String getDriverStatusCodeAndColor(Long driverId){
		/*
		 * 1.先查询出该设备关联的状态测点;
		 * 2.查询出状态值及对应的颜色;
		 */
		if(driverId == null)
			return "";
		String result = "";
		String id = String.valueOf(driverId);
		Map<String,Object> map = redisServ.getHash("DeviceInfo_"+id);
		//给设备的状态测点
		List<MesPoints> points = this.getStatusPoints(driverId);
		if(map != null && points.size() != 0){
			Set<String> keySets = map.keySet();
			//5月11日客户要求变更：每个设备上最多只会有一个状态测点
			MesPoints point = points.get(0);
			for(String key : keySets){
				//包含该测点
				if(key.indexOf(point.getCodekey()) != -1){
					try{
						Object obj = map.get(key);
						JSONObject jsObj = new JSONObject(obj.toString());
						Long value = jsObj.getLong("value");
						//根据value的值查看pointData中对应的是什么值，并配置颜色
						String valueName = this.getPointDataValueName(this.getDriverPointId(point.getId()), value);
						String valueColor = this.getPointDataValueColor(this.getDriverPointId(point.getId()), value);
						result += "<span style=\"background-color:"+valueColor+";\">　　</span>&nbsp;"+valueName;
					}catch(Exception e){

					}
				}
			}
			return result;
		}
		return "未定义";
	}

	private String getPointDataValueName(Long driverPointId,Long value){
		if(driverPointId != null && value != null){
			List<MesPointCheckData> list = mesPointCheckDataService.findByMesDriverPointsIdAndCheckvalue(driverPointId, value);
			if(list != null)
				return list.get(0).getName();
		}
		return "";
	}

	private String getPointDataValueColor(Long driverPointId,Long value){
		if(driverPointId != null && value != null){
			List<MesPointCheckData> list = mesPointCheckDataService.findByMesDriverPointsIdAndCheckvalue(driverPointId, value);
			if(list != null)
				return list.get(0).getColorcode();
		}
		return "";
	}

	private Long getDriverPointId(Long pointId){
		MesDriverPoints mp = mesDriverPointService.findByMesPointsId(pointId);
		if(mp != null)
			return mp.getId();
		return null;
	}

	/**
	 * 根据设备id查询出该设备的所有状态测点
	 * @param driverId
	 * @return
	 */
	private List<MesPoints> getStatusPoints(Long driverId){
		if(driverId == null)
			return null;
		return mesPointsService.findPointsByPointsId(Arrays.asList(new Long[]{driverId}));
	}


	/**
	 * 变更设备的状态
	 * @param list
	 * @return
	 */
	private List<MesDriver> getDriverListForStatus(List<MesDriver> list){
		for(MesDriver driver : list){
			//客户要求变更：状态显示背景颜色
			driver.setDriverStatus(this.getDriverStatusCodeAndColor(driver.getId()));
		}
		return list;
	}
	/**
	 * 根据产线id查找设备
	 * @param id
	 * @param model
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/getmesDriverbyproductlineId/{productlineId}")
	public @ResponseBody String getmesDriverbyproductlineId(@PathVariable Long productlineId,Page page,HttpServletRequest request) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
				new SearchFilter("mesProductline.id",Operator.EQ,productlineId)
				
				);
		List<MesDriver> mesDrivers = driverServ.findPage(specification, page);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesDrivers);
		
	}
	/**
	 * 根据设备id查找设备
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/MesDrivertypePropertyData/{mesDriverId}")
	@ResponseBody
	public String MesDriverData(@PathVariable Long mesDriverId) throws JsonProcessingException {
		List<MesDriverPoints> mesDriverPoints = new ArrayList<MesDriverPoints>();
		List<MesDrivertypeProperty> mesDrivertypeProperties = new ArrayList<MesDrivertypeProperty>();
		mesDriverPoints = mesDriverPointService.findByMesDriverId(mesDriverId);
		for(MesDriverPoints mesDriverPoint : mesDriverPoints){
			if(mesDriverPoint.getMesDrivertypeProperty()!=null){
				mesDrivertypeProperties.add(mesDriverPoint.getMesDrivertypeProperty());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesDrivertypeProperties);
	}
}
