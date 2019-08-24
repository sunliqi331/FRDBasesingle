package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
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
import com.its.frd.entity.MesDrivertype;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.MesProductline;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesDriverTypeService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.MesProductlineService;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;


@Controller
@RequestMapping("/productline")
public class MesProductlineController {
	private final String PRE_PAGE = "product/";
	private static final String LIST = "productLine/lineList";
	private static final String TREE = "productLine/tree";
	private static final String TREE_LIST = "productLine/tree_list";

	@Resource
	private MesProductlineService lineServ;
	@Resource
	private MesDriverService driServ;
	@Resource
	private CompanyinfoService cpServ;
	@Resource
	private MesProductService proServ;
	@Resource
	private MesDriverTypeService mesDriverTypeService;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;

	@Resource
	private RedisService redisServ;

	/**
	 * 添加产线上的设备
	 * @param id
	 * @param model
	 * @return
	 */

	@RequiresPermissions("Productline:saveDriver")
	@RequestMapping("/addLineDriver/{lineId}")
	public String addLineDriver (@PathVariable Long lineId, HttpServletRequest request,Page page,Map<String,Object> map) {
		Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class
				,new SearchFilter("companyinfo.id", Operator.EQ, lineServ.findById(lineId).getCompanyinfo().getId())
				,new SearchFilter("id", Operator.NOTEQ, lineId));
		List<MesProductline> mesProductlines = lineServ.findPage(specification, page);
		map.put("companyId", SecurityUtils.getShiroUser().getCompanyid());
		map.put("lineId", lineId);
		map.put("mesProductlines", mesProductlines);
		map.put("page", page);
		return PRE_PAGE + "addLineDriver";
	}

	/**
	 * 添加产线
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("Productline:save")
	@RequestMapping("/addLine/{parentId}")
	public String addLine(@PathVariable Long parentId,Page page,HttpServletRequest request,Map<String, Object> map) {
		if(SecurityUtils.getShiroUser().getCompanyid().compareTo(parentId) == 0) {
			Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
					new SearchFilter("companytype",Operator.EQ,"factory"),
					new SearchFilter("parentid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
					new SearchFilter("companystatus", Operator.NOTEQ,"3"));
			List<Companyinfo> company = cpServ.findPage(specification, page);
			map.put("companyid", SecurityUtils.getShiroUser().getCompanyid());
			map.put("company", company);
			return PRE_PAGE + "addLine";
		}else {
			map.put("companyid", SecurityUtils.getShiroUser().getCompanyid());
			map.put("parentId",parentId);
			return PRE_PAGE + "addLine2";
		}
	}

	/**
	 * 根据id查找
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("Productline:edit")
	@RequestMapping("/findById/{id}")
	public String findById(@PathVariable Long id,Model model,Map<String,Object> map,Page page,HttpServletRequest request){
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
				new SearchFilter("companytype",Operator.EQ,"factory"),
				new SearchFilter("parentid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				,new SearchFilter("companystatus", Operator.EQ,"1"));
		List<Companyinfo> companys = cpServ.findPage(specification, page);
		/*List<MesDriver> drivers = lineServ.findById(id).getMesDrivers();
		StringBuffer sb = new StringBuffer();
		for(MesDriver mesDriver : drivers){
			if(sb.length()<=0){
				sb.append(mesDriver.getId());
			}else{
				sb.append(","+mesDriver.getId());
			}
		}
		map.put("selectedMesdrivers", sb);*/
		map.put("companys", companys);
		map.put("companyid", SecurityUtils.getShiroUser().getCompanyid());
		map.put("company", lineServ.findById(id).getCompanyinfo());
		model.addAttribute("MesProductline",lineServ.findById(id));
		return PRE_PAGE + "editLine";
	}
	/**
	 * 根据id查找设备
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("Productline:viewDriver")
	@RequestMapping("/driverList/{id}")
	public String findDrivers(@PathVariable Long id,Map<String, Object> map,HttpServletRequest request,Page page,String parentid){
		MesProductline line = lineServ.findById(id);
		page.setNumPerPage(Integer.MAX_VALUE);
		Specification<MesDrivertype> specification1 = DynamicSpecifications.bySearchFilter(request, MesDrivertype.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
		List<MesDrivertype> mesDrivertypes = mesDriverTypeService.findPage(specification1, page);
		map.put("lineId", id);
		map.put("line", line);
		map.put("companyId", line.getCompanyinfo().getId());
		map.put("mdt", mesDrivertypes);
		return PRE_PAGE + "driverlist";
	}

	/**
	 * 返回所选产线下的所有设备
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/DriverData/{id}")
	@ResponseBody
	public String DriverData(@PathVariable Long id,Page page,HttpServletRequest request) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String,Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
				new SearchFilter("mesProductline.id",Operator.EQ,id)
				,new SearchFilter("differencetype", Operator.NOTEQ, "0")
				);
		List<MesDriver> mesDrivers = driServ.findPage(specification, page);
		map.put("page", page);
		map.put("mesDrivers", mesDrivers);
		return mapper.writeValueAsString(map);

	}

	/**
	 * 根据id返回所选产线下的所有设备
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/DriverData2/{id}")
	@ResponseBody
	public String DriverData2(@PathVariable Long id,Page page,HttpServletRequest request) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
				new SearchFilter("mesProductline.id",Operator.EQ,id)
				,new SearchFilter("differencetype", Operator.NOTEQ, "0")
				);
		List<MesDriver> mesDrivers = driServ.findPage(specification, page);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesDrivers);
	}


	/**
	 * 返回所有产线
	 * @param request
	 * @param page
	 * @return
	 */
	@RequiresPermissions("Productline:view")
	@RequestMapping("/productlineData/{parentid}")
	@ResponseBody
	public String data(@PathVariable Long parentid,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MesProductline> mesProductlines = new ArrayList<>();
		if(SecurityUtils.getShiroUser().getCompanyid().compareTo(parentid) != 0) {
			Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class,
					new SearchFilter("companyinfo.id", Operator.EQ,parentid));
			mesProductlines = lineServ.findPage(specification, page);
		}else {
			Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class,
					new SearchFilter("companyinfo.parentid", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()));
			mesProductlines = lineServ.findPage(specification, page);
		}
		StringBuilder mesdrivers = null;
		for(MesProductline mProductline : mesProductlines){
			mesdrivers = new StringBuilder();
			for(MesDriver md : mProductline.getMesDrivers()){
				mesdrivers.append(md.getName() + "   ");
			}
			mProductline.setMesdriver(mesdrivers.toString());
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("MesProductlines", mesProductlines);
		map.put("parentid", parentid);
		return  mapper.writeValueAsString(map);
	}

	/**
	 * 添加数据，保存产线
	 * @param MesProductline
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"Productline:save","Productline:edit"},logical=Logical.OR)
	@RequestMapping("/saveProductline")
	@ResponseBody
	public String saveDriver(@Valid MesProductline mesProductline,HttpServletRequest request){
		//		MesDriver mesDriver = new MesDriver();
		try {
			if(lineServ.findByCompanyinfoidAndLinen(mesProductline.getCompanyinfo().getId(), mesProductline.getLinesn()) != null)
				return AjaxObject.newError("该产线编号已存在！").setCallbackType("").toString();
			/*String str = request.getParameter("selectedMesDriverIds");
			if (str.length() > 0) {
				String[] arr = str.split(",");
				for(int i=0;i<arr.length;i++){
					mesDriver = driServ.findById(Long.valueOf(arr[i]));
					mesDriver.setMesProductline(mesProductline);
				}
			}*/
			//        mesProductline.setCompanyinfo(cpServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
			lineServ.saveOrUpdate(mesProductline);
			Set<String> macs = new HashSet<>();
			for(MesDriver mesDriver : mesProductline.getMesDrivers()){
				if(mesDriver == null){
					continue;
				}
				for (MesDriverPoints mesDriverPoints : mesDriver.getMesDriverPointses()) {
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
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("添加产线失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("添加产线成功!").toString();
	}

	/**
	 * 修改产线
	 * @param id
	 * @param model
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("Productline:edit")
	@RequestMapping(value = "/editMesProductline", method = RequestMethod.POST)
	public @ResponseBody String editMesProductline(@Valid MesProductline mesProductline,HttpServletRequest request) {
		MesProductline mpl = lineServ.findByCompanyinfoidAndLinen(mesProductline.getCompanyinfo().getId(), mesProductline.getLinesn());
		try {
			if(mpl != null) {
				if(mpl.getId().compareTo(mesProductline.getId()) != 0)
					return AjaxObject.newError("该产线编号已存在！").setCallbackType("").toString();
			}
			MesProductline oldMesProductline = lineServ.findById(mesProductline.getId());
			//oldMesProductline.setLinename(mesProductline.getLinename());
			//oldMesProductline.setCompanyinfo(mesProductline.getCompanyinfo());
			//oldMesProductline.setLinesn(mesProductline.getLinesn());
			//oldMesProductline.setMesdriver(mesProductline.getMesdriver());
			//oldMesProductline.setMesProduct(mesProductline.getMesProduct());
			/*MesDriver mesDriver = new MesDriver();
			String str = request.getParameter("selectedEditMesDriverIds");
			if(str.length() > 0){
				String[] arr = str.split(",");
				for (MesDriver oldMesdriver : oldMesProductline.getMesDrivers()) {
					oldMesdriver.setMesProductline(null);
					mesProductline.getMesDrivers().remove(oldMesdriver);
				}
				for(int i = 0;i < arr.length;i++){
					mesDriver = driServ.findById(Long.valueOf(arr[i]));
					mesDriver.setMesProductline(mesProductline);
					mesProductline.getMesDrivers().add(mesDriver);
				}
			}else {
				for (MesDriver oldMesdriver : oldMesProductline.getMesDrivers()) {
					oldMesdriver.setMesProductline(null);
					oldMesdriver.setCompanyinfo(cpServ.findById(SecurityUtils.getShiroUser().getCompanyid()));
					driServ.saveOrUpdate(oldMesdriver);
				}
				mesProductline.setMesDrivers(null);
			}*/
			mesProductline.setCompanyinfo(oldMesProductline.getCompanyinfo());
			lineServ.saveOrUpdate(mesProductline);
			Set<String> macs = new HashSet<>();
			for(MesDriver mesDriver : mesProductline.getMesDrivers()){
				if(mesDriver == null){
					continue;
				}
				for (MesDriverPoints mesDriverPoints : mesDriver.getMesDriverPointses()) {
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
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("修改产线失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("修改产线成功！").toString();
	}

	/**
	 * 保存产线设备
	 * @param lineId
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("Productline:saveDriver")
	@RequestMapping("/saveDriver/{lineId}")
	@ResponseBody
	public String saveDriver(@PathVariable Long lineId,HttpServletRequest request) {
		try{
			MesDriver mesDriver = new MesDriver();
			String str = request.getParameter("selectedMesDriverIds");
			Set<String> macs = new HashSet<>();
			if (str.length() > 0) {
				String[] arr = str.split(",");
				for(int i=0;i<arr.length;i++){
					mesDriver = driServ.findById(Long.valueOf(arr[i]));
					mesDriver.setMesProductline(lineServ.findById(lineId));
					driServ.saveOrUpdate(mesDriver);
					for (MesDriverPoints mesDriverPoints : mesDriver.getMesDriverPointses()) {
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
				}
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("添加产线设备失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("添加产线设备成功！").toString();
	}
	/**
	 * 根据id删除产线
	 * @param id
	 * @param model
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("Productline:delete")
	@RequestMapping("/deleteMesProductline")
	@ResponseBody
	public String deleteSubDriver(Long[] ids){
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				MesProductline mesProductline = lineServ.findById(ids[i]);
				
				List<MesDriver> productLineDrivers = driServ.findByMesProductlineid(ids[i]);
				if (!productLineDrivers.isEmpty()) {
					return AjaxObject.newError("产线仍有关联设备，删除失败!").setCallbackType("").toString();
				}
				
				for(MesDriver mesDriver : productLineDrivers){
					mesDriver.setMesProductline(null);
				}
				//lineServ.deleteById(ids[i]);
				for(MesDriver mesDriver : mesProductline.getMesDrivers()){
					if(mesDriver == null){
						continue;
					}
					for (MesDriverPoints mesDriverPoints : mesDriver.getMesDriverPointses()) {
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
				}
				lineServ.deleteById(ids[i]);
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("删除产线失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除产线成功!").toString();
	}

	/**
	 * 根据id移除设备
	 * @param id
	 * @param model
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("Productline:deleteDriver")
	@RequestMapping("/deleteDriver")
	@ResponseBody
	public String deleteDriver(Long[] ids){
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				MesDriver mesDriver = driServ.findById(ids[i]);
				mesDriver.setMesProductline(null);
				driServ.saveOrUpdate(mesDriver);
				for (MesDriverPoints mesDriverPoints : mesDriver.getMesDriverPointses()) {
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
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("删除产线设备失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除产线设备成功!").toString();
	}

	/**
	 * 根据公司id获取公司下的所有产线
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/getProductlineByCompanyid/{companyid}")
	public @ResponseBody String getProductlineByCompanyid(@PathVariable Long companyid,HttpServletRequest request,Page page) throws JsonProcessingException{
		Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class,
				new SearchFilter("companyinfo.id",Operator.EQ,companyid)
				/*,new SearchFilter("companyinfo.companytype", Operator.EQ, "factory")*/
				);
		List<MesProductline> mesProductlines = lineServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesProductlines);
	}

	/**
	 * 获取当前公司下的所有产线
	 * @param model
	 * @return
	 */
	@RequestMapping("/getProductlineByCurrentCompanyid")
	public @ResponseBody String getProductlineByCurrentCompanyid(HttpServletRequest request,Page page) throws JsonProcessingException{
		Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class,
				new SearchFilter("companyinfo.parentid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				,new SearchFilter("companyinfo.companytype", Operator.EQ, "factory")
				);
		List<MesProductline> mesProductlines = lineServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesProductlines);
	}


	/**
	 * 根据产线id查找设备
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/MesDriverData/{factoryId}/{productlineId}")
	@ResponseBody
	public String MesDriverData(@PathVariable Long productlineId,@PathVariable Long factoryId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesDriver> specification = null;
		List<MesDriver> mesDrivers = new ArrayList<MesDriver>();
		if(productlineId == 0){
			mesDrivers = driServ.findByMesProductline(factoryId);
		}else{
			specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
					new SearchFilter("mesProductline.id",Operator.EQ,productlineId)
					,new SearchFilter("differencetype", Operator.NOTEQ, "0"));
			mesDrivers = driServ.findPage(specification, page);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesDrivers", mesDrivers);
		return mapper.writeValueAsString(map);
	}
	/**
	 * 根据产线id查找设备
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/MesDriverData2/{factoryId}/{productlineId}")
	@ResponseBody
	public String MesDriverData2(@PathVariable Long productlineId,@PathVariable Long factoryId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Specification<MesDriver> specification = null;
		List<MesDriver> mesDrivers = new ArrayList<MesDriver>();
		if(productlineId == 0){
			mesDrivers = driServ.findByMesProductline(factoryId);
		}else{
			specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
					new SearchFilter("mesProductline.id",Operator.EQ,productlineId)
					,new SearchFilter("differencetype", Operator.NOTEQ, "0"));
			mesDrivers = driServ.findPage(specification, page);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesDrivers);
	}

	/**
	 * 已选择添加的设备列表
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/selectedMesDriverData/{STmesdrivers}")
	@ResponseBody
	public String selectedMesDriverData(@PathVariable String STmesdrivers,HttpServletRequest request,Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		List<MesDriver> mesDriver = new ArrayList<MesDriver>();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		String[] arr1 = STmesdrivers.split(",");
		/*Arrays.asList(arr1);*/
		//array转list
		if(arr1.equals(null)){
			Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
					new SearchFilter("id",Operator.IN, arr1));
			mesDriver = driServ.findPage(specification, page);
		}
		map.put("page", page);
		map.put("selectedMesDrivers", mesDriver);
		return mapper.writeValueAsString(map);
	}

	@RequiresPermissions("Productline:view")
	@RequestMapping(value="/tree_list", method={RequestMethod.GET, RequestMethod.POST})
	public String tree_list(Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
		map.put("id",companyId );
		return TREE_LIST;
	}

	@RequiresPermissions("Productline:view")
	@RequestMapping(value="/tree", method=RequestMethod.GET)
	public String tree(Map<String, Object> map) {
		Companyinfo companyinfo = cpServ.getCompanyTree();//cpService.getTreeFactory();
		map.put("companyinfo", companyinfo);
		return TREE;
	}

	@RequiresPermissions("Productline:view")
	@RequestMapping(value="/list/{parentid}", method={RequestMethod.GET, RequestMethod.POST})
	public String list(ServletRequest request, Page page, @PathVariable Long parentid, Map<String, Object> map) {
		Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class,
				new SearchFilter("companyinfo.id", Operator.EQ, parentid));
		List<MesProductline> mesProductline = lineServ.findPage(specification, page);

		map.put("page", page);
		map.put("mesProductline", mesProductline);
		map.put("parentid", parentid);

		return LIST;
	}
}
