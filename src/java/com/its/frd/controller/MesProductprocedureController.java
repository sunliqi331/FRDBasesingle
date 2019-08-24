package com.its.frd.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.its.frd.util.ExcelUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.controller.BaseController;
import com.its.common.entity.main.Dictionary;
import com.its.common.service.DictionaryService;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPointType;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProcedurePropertyPoint;
import com.its.frd.entity.MesProcedurePropertyPointLog;
import com.its.frd.entity.MesProductAlarm;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.MesDriverPointService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesPointGatewayService;
import com.its.frd.service.MesPointsService;
import com.its.frd.service.MesProcedurePropertyPointLogService;
import com.its.frd.service.MesProcedurePropertyPointService;
import com.its.frd.service.MesProcedurePropertyService;
import com.its.frd.service.MesProductAlarmService;
import com.its.frd.service.MesProductProcedureService;
import com.its.frd.service.MesProductService;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/procedure")
public class MesProductprocedureController extends BaseController{
	private final String PRE_PAGE = "product/";

	@Resource
	private MesProductService productServ;
	@Resource
	private MesProductProcedureService procedureServ;
	@Resource
	private MesProcedurePropertyService propertyServ;
	@Resource
	private MesPointGatewayService gatewayServ;
	@Resource
	private MesPointsService pointsServ;
	@Resource
	private MesDriverService driverServ;
	@Resource
	private MesDriverPointService mdpServ;
	@Resource
	private MesProcedurePropertyPointService mesPropertyPointServ;
	@Resource
	private MesProcedurePropertyPointLogService pointLogServ;
	@Resource
	private MesProductAlarmService alarmServ;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;
	@Resource
	private DictionaryService dictionaryService;

	@Resource
	private RedisService redisServ;

	//返回工序列表
	@RequiresPermissions("Product:viewProcedure")
	@RequestMapping("/procedureList/{id}")
	public String procedureList (@PathVariable Long id,Map<String,Object> map) {
		map.put("productId", id);
		map.put("product", productServ.findById(id));
		return PRE_PAGE + "procedureList";
	}

	//返回工序属性列表
	@RequiresPermissions("Product:viewProperty")
	@RequestMapping("/ProcedurePropertyList/{id}")
	public String ProcedurePropertyList (@PathVariable Long id,Map<String,Object> map) {
		map.put("ProcedureId", id);
		map.put("procedure",procedureServ.findById(id));
		return PRE_PAGE + "ProcedurePropertyList";
	}

	/**
	 * 根据productId添加工序
	 * @param productId
	 * @return
	 */
	@RequiresPermissions("Product:saveProcedure")
	@RequestMapping("/addprocedure/{productId}")
	public String addprocedure(@PathVariable Long productId,Map<String,Object> map,HttpServletRequest request, Page page) {
		Specification<MesProductProcedure> specification = DynamicSpecifications.bySearchFilter(request, MesProductProcedure.class,
				new SearchFilter("mesProduct.id",Operator.EQ,productId));
		List<MesProductProcedure> mesProductProcedure = procedureServ.findPage(specification, page);
		map.put("mesProductProcedure", mesProductProcedure);
		map.put("productId", productId);
		return PRE_PAGE + "addProcedure";
	}
	/**
	 * 添加工序
	 * 根据Id查找工序
	 * @param id
	 * @return
	 */
	@RequiresPermissions(value={"Product:editProcedure","Product:showProcedure"},logical=Logical.OR)
	@RequestMapping("/findProcedurebyId/{id}")
	public String findProcedurebyId(@PathVariable Long id,Model model,Map<String, Object> map,String pagename){
		model.addAttribute("mesDriverprocedure",procedureServ.findById(id));
		Long ProductId = procedureServ.findById(id).getMesProduct().getId();
		map.put("productId", ProductId);
		return PRE_PAGE + pagename;
	}

	/*
	 * 根据工序号查找工序
	 *
	 * */

	@RequestMapping("/procedureFindby")
	@ResponseBody
	public String findprocedurebyprocedureNum(String procedurenum,Map<String, Object> map) throws JsonProcessingException{
		MesProductProcedure procedure =procedureServ.findByprocedurenum(procedurenum);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(procedure);
	}
	/**
	 * 根据procedureId添加工序属性
	 * @param procedureId
	 * @return
	 */
	@RequiresPermissions("Product:saveProperty")
	@RequestMapping("/addProcedureProperty/{procedureId}")
	public String addProcedureProperty(@PathVariable Long procedureId,Map<String,Object> map,HttpServletRequest request,Page page) {
		/*Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class
				,new SearchFilter("differencetype", Operator.NOTEQ,"0")
				,new SearchFilter("companyinfo.id",Operator.EQ,companyId));
		List<MesDriver> mesDriver = driverServ.findPage(specification, page);*/
		//数据类型
		Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
				new SearchFilter("parent.id", Operator.EQ,89));
		List<Dictionary> datatype = dictionaryService.findByExample(specification1, page);
		//单位
		Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
				new SearchFilter("parent.id", Operator.EQ,90));
		List<Dictionary> units = dictionaryService.findByExample(specification2, page);
		map.put("datatype", datatype);
		map.put("units", units);
		map.put("procedureId", procedureId);
		/*map.put("mesDriver", mesDriver);*/
		return PRE_PAGE + "addProcedureProperty";
	}

	@RequestMapping("/initActiveProcedurePoints")
	@ResponseBody
	public String initActiveProcedurePoints(HttpServletRequest request,Page page) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Map<String, Object> map = new HashMap<String, Object>();
		page.setNumPerPage(5); //设置每页显示5条数据

		//搜索当前工厂下所有设备的可用工序测点
		Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class
				,new SearchFilter("differencetype", Operator.NOTEQ,"0")
				,new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				);
		List<MesDriver> mesDrivers = driverServ.findPage(specification, new Page());
		List<Long> pointIds = new ArrayList<Long>(); //所有可用工序测点的id集合
		//遍历查找设备下可用的的工序测点
		for(MesDriver m : mesDrivers){
			Specification<MesDriverPoints> specify = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class
					,new SearchFilter("mesDriver.id", Operator.EQ, m.getId()));
			List<MesDriverPoints> mdp = mdpServ.findPage(specify, new Page());
			List<Long> ids = new ArrayList<>();
			for(MesDriverPoints mdps : mdp) {
				ids.add(mdps.getMesPoints().getId());
			}
			List<MesPoints> mesPoints = new ArrayList<>();
			if(ids.size() >0){
				Specification<MesPoints> specify1 = DynamicSpecifications.bySearchFilter(request, MesPoints.class
						,new SearchFilter("id", Operator.IN, ids.toArray())
						,new SearchFilter("status", Operator.EQ, MesPoints.BINDING)
						,new SearchFilter("mesPointType.pointtypekey", Operator.EQ,MesPointType.TYPE_PRODUCT_PROCEDURE));
				mesPoints = pointsServ.findPage(specify1, new Page());
				if(mesPoints.size()>0){
					for(MesPoints mp : mesPoints){
						pointIds.add(mp.getId());
					}
				}
			}
		}
		if(pointIds.size()<1){
			return null;
		}
		Specification<MesDriverPoints> specify2 = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class
				,new SearchFilter("mesPoints.id", Operator.IN, pointIds.toArray()));
		List<MesDriverPoints> mesDriverPoints = mdpServ.findPage(specify2, page);
		map.put("mesDriverPoints", mesDriverPoints);
		map.put("page", page);
		return mapper.writeValueAsString(map);
	}


	//工序Data
	@RequestMapping("/procedureData/{productId}")
	@ResponseBody
	public String procedureData(@PathVariable Long productId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		page.setOrderDirection(Page.ORDER_DIRECTION_ASC);
		page.setOrderField("nextprocedurename");
		Specification<MesProductProcedure> specification = DynamicSpecifications.bySearchFilter(request, MesProductProcedure.class,
				new SearchFilter("mesProduct.id",Operator.EQ,productId));
		List<MesProductProcedure> mesProductProcedure = procedureServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesProductProcedure", mesProductProcedure);
		return mapper.writeValueAsString(map);
	}

	//工序属性Data
	@RequestMapping("/procedurePropertyData/{ProcedureId}")
	@ResponseBody
	public String procedurePropertyData(@PathVariable Long ProcedureId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesProcedureProperty> specification = DynamicSpecifications.bySearchFilter(request, MesProcedureProperty.class,
				new SearchFilter("mesProductProcedure.id",Operator.EQ,ProcedureId));
		List<MesProcedureProperty> mesProcedureProperty = propertyServ.findPage(specification, page);
		List<Dictionary> unitList = dictionaryService.findAll(new Specification<Dictionary>() {
			@Override
			public Predicate toPredicate(Root<Dictionary> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				Expression<Long> expression = root.get("parent").get("id").as(Long.class);
				Predicate predicate = builder.equal(expression, 90);
				return query.where(predicate).getRestriction();
			}
		});
		Map<Long,String> unitsMap = new HashMap<>();
		for (Dictionary d : unitList){
			unitsMap.put(d.getId(), d.getName());
		}
		for(MesProcedureProperty procedureProperty : mesProcedureProperty){
			MesPoints mesPoints = procedureProperty.getMesPoints();
			if(null != mesPoints){
				mesPoints.setUnits(unitsMap.get(mesPoints.getUnitsId()));
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesProcedureProperty", mesProcedureProperty);
		return mapper.writeValueAsString(map);
	}

	/**
	 * 保存工序
	 * @param productId
	 * @param MesDriverprocedure
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"Product:saveProcedure","Product:editProcedure"},logical=Logical.OR)
	@RequestMapping("/saveProcedure/{productId}")
	@ResponseBody
	public String saveProcedure(@PathVariable Long productId,MesProductProcedure mesProductProcedure,HttpServletRequest request,Page page){
		String msg = "修改";
		if(mesProductProcedure.getId() == null)
			msg = "添加";
		try {
			Set<String> macs = new HashSet<>();
			if(productId == null)
				return AjaxObject.newError("产品不存在！").setCallbackType("").toString();
			Specification<MesProductProcedure> specification = DynamicSpecifications.bySearchFilter(request, MesProductProcedure.class,
					new SearchFilter("mesProduct.id", Operator.EQ, productId));
			List<MesProductProcedure> mesProductProcedures = procedureServ.findPage(specification, page);
			for(MesProductProcedure procedure : mesProductProcedures) {
				if(procedure.getProcedurenum().equals(mesProductProcedure.getProcedurenum())) {
					if(mesProductProcedure.getId() == null || procedure.getId().compareTo(mesProductProcedure.getId()) != 0)
						return AjaxObject.newError("工序号已存在！").setCallbackType("").toString();
				}
			}
			mesProductProcedure.setMesProduct(productServ.findById(productId));
			procedureServ.saveOrUpdate(mesProductProcedure);
			for(MesProcedureProperty mesProcedureProperty : mesProductProcedure.getMesProcedureProperties()){
				if(mesProcedureProperty == null){
					continue;
				}
				MesProcedureProperty mesProcedureProperty_ = propertyServ.findById(mesProcedureProperty.getId());
				MesPoints mesPoints = mesProcedureProperty_.getMesPoints();
				if(null != mesPoints){
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

			}
			for (String mac : macs) {
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "工序失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "工序成功!").toString();
	}

	/**
	 * 保存工序属性
	 * @param ProcedureId
	 * @param MesProcedureProperty
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"Product:saveProperty","Product:editProperty"},logical=Logical.OR)
	@RequestMapping("/saveProcedureProperty/{procedureId}")
	@ResponseBody
	public String saveProcedureProperty(@PathVariable Long procedureId,@Valid MesProcedureProperty mesProcedureProperty,HttpServletRequest request,Page page){
		String msg = "修改";
		if(mesProcedureProperty.getId() == null)
			msg = "添加";
		try {
			if(procedureId == null)
				return AjaxObject.newError("工序不存在!").setCallbackType("").toString();
			Specification<MesProcedureProperty> specification = DynamicSpecifications.bySearchFilter(request, MesProcedureProperty.class,
					new SearchFilter("mesProductProcedure.id", Operator.EQ, procedureId));
			List<MesProcedureProperty> mesProcedurePropertys = propertyServ.findPage(specification, page);
			for(MesProcedureProperty property : mesProcedurePropertys) {
				if(mesProcedureProperty.getId() == null ||property.getId().compareTo(mesProcedureProperty.getId()) !=0) {
					if(property.getPropertyname().equals(mesProcedureProperty.getPropertyname()))
						return AjaxObject.newError("工序属性名已存在！").setCallbackType("").toString();
				}
			}
			mesProcedureProperty.setMesProductProcedure(procedureServ.findById(procedureId));
			if(mesProcedureProperty.getId() != null){
				MesProcedureProperty editMesProcedureProperty = propertyServ.findById(mesProcedureProperty.getId());
				mesProcedureProperty.setMesPoints(editMesProcedureProperty.getMesPoints());
				mesProcedureProperty.setMesProcedurePropertyPoint(editMesProcedureProperty.getMesProcedurePropertyPoint());
			}
			//			MesPoints mesPoint = pointsServ.findById(mesProcedureProperty.getMesPoints().getId());
			//			mesPoint.setStatus(MesPoints.BINDING_PROCE);
			propertyServ.saveOrUpdate(mesProcedureProperty);
			MesProcedureProperty mesProcedureProperty_ = propertyServ.findById(mesProcedureProperty.getId());
			MesPoints mesPoints = mesProcedureProperty_.getMesPoints();
			if(null != mesPoints){
				MesPointGateway gateway = mesPoints.getMesPointGateway();
				String result = mesPointsTemplateService.getTemplate(mesPoints);
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), result);
				redisServ.setHash(key, hash);
				mesPointsTemplateService.sendTemplate(gateway.getMac());

			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "工序属性失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "工序属性成功!").toString();
	}

	/**
	 * 根据Id删除工序
	 * @param id
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("Product:deleteProcedure")
	@RequestMapping("/deleteProcedureById")
	@ResponseBody
	public String deleteProcedureById(Long[] ids){
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				MesProductProcedure mesProductProcedure = procedureServ.findById(ids[i]);
				//procedureServ.deleteById(ids[i]);
				for(MesProcedureProperty mesProcedureProperty : mesProductProcedure.getMesProcedureProperties()){
					if(mesProcedureProperty == null){
						continue;
					}
					MesProcedureProperty mesProcedureProperty_ = propertyServ.findById(mesProcedureProperty.getId());
					MesPoints mesPoints = mesProcedureProperty_.getMesPoints();
					if(null != mesPoints){
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

				}
				procedureServ.deleteById(ids[i]);
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("删除工序失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除工序成功!").setCallbackType("").toString();
	}

	/**
	 * 根据Id删除工序属性
	 * @param id
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("Product:deleteProperty")
	@RequestMapping("/deletePropertyById")
	@ResponseBody
	@Transactional
	public String deletePropertyById(Long[] ids){
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				List<MesProductAlarm> alarm = alarmServ.findByMesProcedurePropertyId(ids[i]);
				if(alarm.size() > 0){
					String name = propertyServ.findById(ids[i]).getPropertyname();
					return AjaxObject.newError(name + "属性有数据告警历史数据,无法删除！").setCallbackType("").toString();
				}
				MesProcedureProperty mpp = propertyServ.findById(ids[i]);
				if(mesPropertyPointServ.findById(ids[i]) != null){
					//mesPropertyPointServ.deleteById(ids[i]);
					MesPoints point =  pointsServ.findById(mpp.getMesPoints().getId());
					if(point != null){
						point.setStatus(MesPoints.BINDING);
						pointsServ.saveOrUpdate(point);
						MesPointGateway gateway = point.getMesPointGateway();
						macs.add(gateway.getMac());
						String result = mesPointsTemplateService.getTemplate(point);
						String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
						Map<String, Object> hash = redisServ.getHash(key);
						if(hash == null){
							hash = new HashMap<String,Object>();
						}
						hash.put(point.getCodekey(), result);
						redisServ.setHash(key, hash);
					}
				}
				propertyServ.deleteById(ids[i]);

			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return AjaxObject.newError("删除工序属性失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除工序属性成功!").setCallbackType("").toString();
	}

	/**
	 * 根据Id查找工序
	 * @param id
	 * @return
	 */
	@RequiresPermissions(value={"Product:editProcedure","Product:showProcedure"},logical=Logical.OR)
	@RequestMapping("/findProcedureById/{id}")
	public String findProcedureById(@PathVariable Long id,Model model,Map<String, Object> map,String pagename){
		model.addAttribute("mesDriverprocedure",procedureServ.findById(id));
		Long ProductId = procedureServ.findById(id).getMesProduct().getId();
		map.put("productId", ProductId);
		return PRE_PAGE + pagename;
	}

	/**
	 * 根据Id查找工序属性
	 * @param id
	 * @return
	 */
	@RequiresPermissions("Product:editProperty")
	@RequestMapping("/findPropertyById/{id}")
	public String findPropertyById(@PathVariable Long id,Model model,Map<String, Object> map,HttpServletRequest request,Page page){
		MesProcedureProperty mesProcedureProperty = propertyServ.findById(id);
		Long procedureId = mesProcedureProperty.getMesProductProcedure().getId();
		model.addAttribute("mesProcedureProperty",mesProcedureProperty);
		//数据类型
		Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
				new SearchFilter("parent.id", Operator.EQ,89));
		List<Dictionary> datatype = dictionaryService.findByExample(specification1, page);
		//单位
		Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
				new SearchFilter("parent.id", Operator.EQ,90));
		List<Dictionary> units = dictionaryService.findByExample(specification2, page);
		map.put("datatype", datatype);
		map.put("units", units);
		map.put("procedureId", procedureId);
		return PRE_PAGE + "editProcedureProperty";
	}
	@RequiresPermissions("Product:savePoint")
	@RequestMapping("/addPoint/{propertyId}")
	public String addPoint(@PathVariable Long propertyId,HttpServletRequest request,Map<String, Object> map){
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		MesProcedureProperty property = propertyServ.findById(propertyId);
		MesDriver md = null;
		List<MesDriver> mesDriver = new ArrayList<MesDriver>();
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(property.getMesPoints() != null){
			MesDriverPoints mdp = mdpServ.findByMesPointsId(property.getMesPoints().getId());
			if(md != null) {
		         md = driverServ.findById(mdp.getMesDriver().getId());
	            Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class
	                    ,new SearchFilter("differencetype", Operator.NOTEQ,"0")
	                    ,new SearchFilter("companyinfo.id",Operator.EQ,companyId)
	                    ,new SearchFilter("id", Operator.NOTEQ,md.getId())
	                    );
	            mesDriver = driverServ.findPage(specification, page);
			}

		}else{
			Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class
					,new SearchFilter("differencetype", Operator.NOTEQ,"0")
					,new SearchFilter("companyinfo.id",Operator.EQ,companyId)
					);
			mesDriver = driverServ.findPage(specification, page);
		}
		//当前测点绑定的设备上的其他测点
		List<MesDriverPoints> mdp = new ArrayList<MesDriverPoints>();
		if(md != null)
			mdp = mdpServ.findByMesDriverId(md.getId());
		List<Long> ids = new ArrayList<>();
		if(mdp != null){
			for(MesDriverPoints mdps : mdp) {
				ids.add(mdps.getMesPoints().getId());
			}
		}
		List<MesPoints> mesPoints = new ArrayList<>();
		if(ids.size() >0){
			Specification<MesPoints> specification1 = DynamicSpecifications.bySearchFilter(request, MesPoints.class
					,new SearchFilter("id", Operator.IN, ids.toArray())
					,new SearchFilter("status", Operator.EQ, MesPoints.BINDING)
					,new SearchFilter("mesPointType.pointtypekey", Operator.EQ,MesPointType.TYPE_PRODUCT_PROCEDURE)
					,new SearchFilter("id", Operator.NOTEQ,property.getMesPoints().getId())
					);
			mesPoints = pointsServ.findPage(specification1, page);
		}
		boolean bool = false;
		if(mesPropertyPointServ.findById(propertyId) != null)
			bool = true;
		map.put("md", md);
		map.put("property",property);
		map.put("bool", bool);
		map.put("propertyId", propertyId);
		map.put("mesDriver", mesDriver);
		map.put("mesPoints", mesPoints);
		return PRE_PAGE + "addPoint";
	}

	//保存测点与属性的关联信息
	@SendTemplate
	@RequiresPermissions("Product:savePoint")
	@RequestMapping("/savePoint/{propertyId}")
	@ResponseBody
	@Transactional
	public String savePoint(@PathVariable Long propertyId,HttpServletRequest request){
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);;
		try{
			String checkbox = request.getParameter("checkbox");
			String pId = request.getParameter("pointid");
			Long pointId = null;
			if(pId.length() > 0)
				pointId = Long.valueOf(pId);
			if(propertyId == null)
				return AjaxObject.newError("该工序属性不存在!").setCallbackType("").toString();
			if(pointId == null)
				return AjaxObject.newError("未选择测点!").setCallbackType("").toString();
			if(checkbox != null){
				if(checkbox.equals("yes")){
					Specification<MesProcedurePropertyPoint> specification = DynamicSpecifications.bySearchFilter(request, MesProcedurePropertyPoint.class
							,new SearchFilter("propertyid", Operator.EQ, propertyId));
					List<MesProcedurePropertyPoint> list = mesPropertyPointServ.findPage(specification, page);
					if(list.size() > 0){
						for(MesProcedurePropertyPoint mesPropoint : list){
							//删除工序属性和测点的关联
							mesPropertyPointServ.deleteById(mesPropoint.getPropertyid());
							//修改测点状态
							MesPoints point = pointsServ.findById(mesPropoint.getMesPoints().getId());
							point.setStatus(MesPoints.BINDING);
							//point.setUnits("");
							pointsServ.saveOrUpdate(point);
							//设置工序属性中的测点为空
							MesProcedureProperty mpp = propertyServ.findById(mesPropoint.getPropertyid());
							mpp.setMesPoints(null);
							propertyServ.saveOrUpdate(mpp);
							MesPointGateway gateway = point.getMesPointGateway();
							String result = mesPointsTemplateService.getTemplate(point);
							String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
							Map<String, Object> hash = redisServ.getHash(key);
							if(hash == null){
								hash = new HashMap<String,Object>();
							}
							hash.put(point.getCodekey(), result);
							redisServ.setHash(key, hash);
						}
					}
				}
			}else{
				//查看要绑定的测点所属设备是否已分配产线
				MesPoints mesPoint = pointsServ.findById(pointId);
				if(null == mesPoint.getCurrentMesProductline() || StringUtils.isEmpty(mesPoint.getCurrentMesProductline().getLinename())){
					return AjaxObject.newError("该测点所属设备没有分配产线").setCallbackType("").toString();
				}
				//查询数据库中propertyId对应的pointId是否存在
				Specification<MesProcedurePropertyPoint> specification = DynamicSpecifications.bySearchFilter(request, MesProcedurePropertyPoint.class
						,new SearchFilter("propertyid", Operator.EQ, propertyId));
				List<MesProcedurePropertyPoint> list = mesPropertyPointServ.findPage(specification, page);
				if(list.size() > 0){
					for(MesProcedurePropertyPoint mesPropoint : list){
						//删除工序属性和测点的关联
						//	mesPropertyPointServ.deleteById(mesPropoint.getPropertyid());
						//修改测点状态
						MesPoints point = pointsServ.findById(mesPropoint.getMesPoints().getId());
						mesPropoint.setMesPoints(mesPoint);
						mesPropertyPointServ.saveOrUpdate(mesPropoint);
						//MesProcedureProperty mpp = propertyServ.findById(mesPropoint.getPropertyid());
						point.setStatus(MesPoints.BINDING);
						//point.setUnits(mpp.getUnits());
						pointsServ.saveOrUpdate(point);
						//设置工序属性中的测点为空
						//mpp.setMesPoints(null);
						//propertyServ.saveOrUpdate(mpp);
					}
				}else{
					//保存工序属性和测点的关系到mes_procedure_property_point
					MesProcedurePropertyPoint propertyPoint = new MesProcedurePropertyPoint();
					propertyPoint.setPropertyid(propertyId);
					propertyPoint.setMesPoints(mesPoint);
					propertyPoint.setMesProcedureProperty(propertyServ.findById(propertyId));
					propertyPoint.setCreatedate(new java.sql.Timestamp(new Date().getTime()));
					mesPropertyPointServ.saveOrUpdate(propertyPoint);
				}
				//保存工序属性和测点关联变更log
				MesProcedurePropertyPointLog pointLog = new MesProcedurePropertyPointLog();
				pointLog.setPropertyid(propertyId);
				pointLog.setPointid(pointId);
				pointLog.setCodeKey(mesPoint.getCodekey());
				pointLog.setCreatedate(new java.sql.Timestamp(new Date().getTime()));
				pointLogServ.saveOrUpdate(pointLog);
				//修改测点的status
				MesPoints point = pointsServ.findById(pointId);
				point.setStatus(MesPoints.BINDING_PROCE);
				pointsServ.saveOrUpdate(point);
				List<MesPoints> mesPoints = new ArrayList<>();
				mesPoints.add(point);
				//mesPointsTemplateService.sendTemplate(mesPoints);
				//保存测点信息到mes_procedure_property
				MesProcedureProperty property = propertyServ.findById(propertyId);
				property.setMesPoints(point);
				propertyServ.saveOrUpdate(property);
				MesPointGateway gateway = point.getMesPointGateway();
				String result = mesPointsTemplateService.getTemplate(point);
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(point.getCodekey(), result);
				redisServ.setHash(key, hash);
				mesPointsTemplateService.sendTemplate(gateway.getMac());

			}
		}catch(Exception e){
			e.printStackTrace();
			return AjaxObject.newError("配置测点失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("配置测点成功!").toString();
	}

	/**
	 * 根据设备Id查询工序测点类型的测点数据
	 */
	@RequestMapping("/getMesPointsById/{mesDriverId}")
	@ResponseBody
	public String mesPointsData(@PathVariable Long mesDriverId,HttpServletRequest request,Page page) throws JsonProcessingException {
		page.setNumPerPage(Integer.MAX_VALUE);
		//		Map<String,Object> map = new HashMap<String,Object>();
		Specification<MesDriverPoints> specification = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class
				,new SearchFilter("mesDriver.id", Operator.EQ, mesDriverId));
		List<MesDriverPoints> mdp = mdpServ.findPage(specification, page);
		List<Long> ids = new ArrayList<>();
		for(MesDriverPoints mdps : mdp) {
			ids.add(mdps.getMesPoints().getId());
		}
		List<MesPoints> mesPoints = new ArrayList<>();
		if(ids.size() >0){
			Specification<MesPoints> specification1 = DynamicSpecifications.bySearchFilter(request, MesPoints.class
					,new SearchFilter("id", Operator.IN, ids.toArray())
					,new SearchFilter("status", Operator.EQ, MesPoints.BINDING)
					,new SearchFilter("mesPointType.pointtypekey", Operator.EQ,MesPointType.TYPE_PRODUCT_PROCEDURE));
			mesPoints = pointsServ.findPage(specification1, page);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesPoints);
	}

	/**
	 * 根据测点Id查询测点名字
	 */
	@RequestMapping("/getMesPointsNameById/{mesPoints}")
	@ResponseBody
	public String mesPointsName(@PathVariable Long mesPoints,HttpServletRequest request,Page page) throws JsonProcessingException {
		MesPoints mesPoints1 = this.pointsServ.findById(mesPoints);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesPoints1.getName());
	}



	@RequestMapping("/checkNext/{nextnumber}/{productId}")
	@ResponseBody
	public String checkName(@PathVariable String nextnumber, @PathVariable String productId,HttpServletRequest request) throws JsonProcessingException{
		Page page = new Page();
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			Specification<MesProductProcedure> specification = DynamicSpecifications.bySearchFilter(request, MesProductProcedure.class
					,new SearchFilter("nextprocedurename", Operator.EQ, nextnumber)
					,new SearchFilter("mesProduct.id", Operator.EQ, productId)
					);
			List<MesProductProcedure> procedure = procedureServ.findPage(specification, page);
			if(procedure.size() > 0){
				map.put("0", 0);
			}else {
				map.put("1", 1);
			}
		} catch (Exception e) {
			return null;
		}
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/read_gongxu_excel")
	@ResponseBody
	public String readGongXuExcel(MultipartFile alarmRelationFile, Long ProcedureId) throws JsonProcessingException {
		Map<String, Object> map_result = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		Workbook wb =null;
		Sheet sheet = null;
		Row row = null;
		List<Map<String,String>> list = null;
		String cellData = null;
		String columns[] = {"工序属性名称", "工序属性别名", "测量方法", "上公差", "下公差", "标准值"};
		wb = ExcelUtils.readExcel(alarmRelationFile);
		try {
		if(wb != null){
			//用来存放表中数据
			list = new ArrayList<Map<String,String>>();
			//获取第一个sheet
			sheet = wb.getSheetAt(0);
			//获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			//获取第一行
			row = sheet.getRow(0);
			//获取最大列数
			int colnum = row.getPhysicalNumberOfCells();
			for (int i = 1; i<rownum; i++) {
				Map<String,String> map = new LinkedHashMap<String,String>();
				row = sheet.getRow(i);
				if(row !=null){
					for (int j=0;j<colnum;j++){
						cellData = (String) ExcelUtils.getCellFormatValue(row.getCell(j));
						map.put(columns[j], cellData);
					}
				}else{
					break;
				}
				list.add(map);
			}
		}
		//遍历解析出来的list
		List<MesPoints> mesPointsList = new ArrayList<MesPoints>();
		for (Map<String,String> map : list) {
			MesProcedureProperty mesProcedureProperty = new MesProcedureProperty();
			for (Map.Entry<String,String> entry : map.entrySet()) {
//                System.out.print(entry.getKey()+":"+entry.getValue()+",");
//				if (entry.getKey().equals("工序ID")){
//					MesProductProcedure mesProductProcedure = new MesProductProcedure();
//					mesProductProcedure.setId(Long.valueOf(entry.getValue().replace(".0","")));
//					mesProcedureProperty.setMesProductProcedure(mesProductProcedure);
//				}
				if (entry.getKey().equals("工序属性名称")){
					mesProcedureProperty.setPropertyname(entry.getValue());
				} else if (entry.getKey().equals("工序属性别名")){
					mesProcedureProperty.setSpcpropertyname(entry.getValue());
				} else if (entry.getKey().equals("测量方法")){
					mesProcedureProperty.setControlWay(entry.getValue());
				} else if (entry.getKey().equals("上公差")){
					String value = Transform(entry.getValue());
					mesProcedureProperty.setUppervalues(value);
				} else if (entry.getKey().equals("下公差")){
					String value = Transform(entry.getValue());
					mesProcedureProperty.setLowervalues(value);
				} else if (entry.getKey().equals("标准值")){
					String value = Transform(entry.getValue());
					mesProcedureProperty.setStandardvalues(value);
				}
			}
			MesProductProcedure mesProductProcedure = new MesProductProcedure();
			mesProductProcedure.setId(ProcedureId);
			mesProcedureProperty.setMesProductProcedure(mesProductProcedure);
			propertyServ.saveOrUpdate(mesProcedureProperty);
		}
			map_result.put("result", "success");
			return mapper.writeValueAsString(map_result);
		} catch (Exception e) {
			map_result.put("result", "fail");
			return mapper.writeValueAsString(map_result);
		}

	}

	public String Transform(String value){
		String [] strs = value.split("[.]");
		if (strs[1].length() == 1){
			return value.replace(".0","");
		} else {
			return value;
		}
	}
}
