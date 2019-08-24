package com.its.frd.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.entity.main.Dictionary;
import com.its.common.entity.main.RolePermission;
import com.its.common.entity.main.User;
import com.its.common.exception.ServiceException;
import com.its.common.service.DictionaryService;
import com.its.common.service.RedisService;
import com.its.common.service.RoleService;
import com.its.common.service.UserRoleService;
import com.its.common.service.UserService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Department;
import com.its.frd.entity.MesCompanyRole;
import com.its.frd.entity.MesCompanyRolePermission;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProductline;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.entity.Usercompanys;
import com.its.frd.params.CompanyfileType;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.DepartmentService;
import com.its.frd.service.MesCompanyRolePermissionService;
import com.its.frd.service.MesCompanyRoleService;
import com.its.frd.service.MesPointGatewayService;
import com.its.frd.service.MesProductlineService;
import com.its.frd.service.UserCompanyroleService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.util.Constants;
import com.its.frd.util.DateUtils;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;

@Controller
@RequestMapping("/company")
public class CompanyCotroller {
	private final String PAGEPRE = "companyManage/";
	private final String LIST_PAGE = "companyManage/list";
	private static final String LIST = "factory/factoryList";
	private static final String TREE = "factory/tree";
	private static final String TREE_LIST = "factory/tree_list";
	@Resource
	private CompanyinfoService cpService;
	@Resource
	private CompanyfileService cfService;
	@Resource
	private UserService userServ;
	@Resource
	private RoleService roleServ;
	@Resource
	private UsercompanysService ucServ;
	@Resource
	private DepartmentService dpServ;
	@Resource
	UserRoleService userRoleService;
	@Resource
	UserCompanyroleService ucrserv;
	@Resource
	MesCompanyRoleService mcrServ;
	@Resource
	MesCompanyRolePermissionService mcrpServ;
	@Resource
	private DictionaryService dicServ;
	@Resource
	private MesProductlineService lineServ;
	@Resource
	private MesPointGatewayService mesPointGatewayService;
	@Resource
	private RedisService redisService;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;

	//待审核公司列表
	@RequiresPermissions("CompanyCheck:view")
	@RequestMapping("/list")
	public String list() {
		return LIST_PAGE;
	}

	//个人公司列表
	@RequiresPermissions("CompanyList:view")
	@RequestMapping("/companyList")
	public String companyList() {
		return PAGEPRE + "companyList";
	}

	//已审核公司列表
	@RequiresPermissions("CompanyRegisted:view")
	@RequestMapping("/registedlist")
	public String registedlist() {
		return PAGEPRE + "registedlist";
	}

	//添加工厂
	@RequiresPermissions("Factory:save")
	@RequestMapping("/addFactory/{parentid}")
	public String addFactory(@PathVariable Long parentid,Map<String,Object> map) {
		map.put("parentid", parentid);
		return PAGEPRE + "addFactoryinfo";
	}

	//保存工厂信息
	@RequiresPermissions(value={"Factory:save","Factory:edit"},logical=Logical.OR)
	@RequestMapping(value = "/saveOrUpdateFactory/{parentid}",method = RequestMethod.POST)
	@ResponseBody
	@SendTemplate
	public String saveOrUpdateFactory(@PathVariable Long parentid,Companyinfo factoryinfo,HttpServletRequest request,Page page) {
		String msg = "修改";
		if(factoryinfo.getId() == null)
			msg = "添加";
		try {
			//工厂创建子工厂不能超过5级
			if(parentid != null){
				if(cpService.findById(parentid).getParentid() != null){
					Long id2 = cpService.findById(parentid).getParentid();
					if(cpService.findById(id2).getParentid() != null){
						Long id3 = cpService.findById(id2).getParentid();
						if(cpService.findById(id3).getParentid() != null){
							Long id4 = cpService.findById(id3).getParentid();
							if(cpService.findById(id4).getParentid() != null){
								/*Long id5 = cpService.findById(id4).getParentid();
								if(cpService.findById(id5).getParentid() != null)*/
								return AjaxObject.newError("无法创建子工厂！").setCallbackType("").toString();
							}
						}
					}
				}
			}
			
			Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class
					,new SearchFilter("parentid", Operator.EQ, parentid)
					,new SearchFilter("companytype",Operator.EQ , "factory"));
			List<Companyinfo> factory = cpService.findPage(specification, page);
			for(Companyinfo factorys : factory) {
					if(factorys.getCompanyname().equals(factoryinfo.getCompanyname())){
						if(factoryinfo.getId() == null || factorys.getId().compareTo(factoryinfo.getId()) != 0)
						return AjaxObject.newError("该工厂名称已存在！").setCallbackType("").toString();
					}
			}
			//切割时间字符串YY-MM-DD
			String startdate = factoryinfo.getStartdate();
			if(startdate != null)
				startdate = startdate.substring(0, 11);
			factoryinfo.setStartdate(startdate);
			factoryinfo.setParentid(parentid);
			factoryinfo.setUserid(SecurityUtils.getShiroUser().getUser().getId());
			factoryinfo.setCompanystatus(Constants.COMPANYINFOOK);
			factoryinfo.setCompanytype(Constants.FACTORY);
			cpService.saveOrUpdate(factoryinfo);
			updatePointsInfoInRedis(SecurityUtils.getShiroUser().getCompanyid());
			
		}catch(Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "工厂信息失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "工厂信息成功").toString();
	}

	private void updatePointsInfoInRedis(long id){
		List<MesPointGateway> list2 = mesPointGatewayService.findByCompanyid(id);
		for(MesPointGateway mesPointGateway : list2){
			Map<String, Object> map = new HashMap<>();
			List<MesPoints> mesPointses = mesPointGateway.getMesPointses();
			for (MesPoints mesPoints : mesPointses) {
				String result = mesPointsTemplateService.getTemplate(mesPoints);
				map.put(mesPoints.getCodekey(), result);
				
			}
			redisService.setHash(MesPointsTemplate.class.getSimpleName()+"_"+mesPointGateway.getMac(), map);
			mesPointsTemplateService.sendTemplate(mesPointGateway.getMac());
		}
	}
	@RequestMapping("/data")
	@ResponseBody
	public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		page.setOrderField("createtime");
		page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class
				, new SearchFilter("companytype", Operator.EQ, "company"));
		List<Companyinfo> Companyinfos = cpService.findPage(specification, page);
		for(Companyinfo companyinfo : Companyinfos) {
			if(companyinfo.getCompanystatus().equals("1")) {
				companyinfo.setCompanystatus("已审核");
			}else if(companyinfo.getCompanystatus().equals("3")) {
				companyinfo.setCompanystatus("无效");
			}else {
				companyinfo.setCompanystatus("未审核");
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("companyinfos", Companyinfos);
		return mapper.writeValueAsString(map);
	}

	//公司data
	@RequestMapping("/data2")
	@ResponseBody
	public String data2(HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		page.setOrderField("createtime");
		page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
		/* 
		 * 根据User中是否是注册用户来判断怎么显示公司信息
		 */
		List<Usercompanys> usercompanys = new ArrayList<>();
		List<Companyinfo> companys = new ArrayList<>();
		if(SecurityUtils.getShiroUser().getUser().getRegisterstate() != null){
			usercompanys = ucServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
			List<Long> ids = new ArrayList<Long>();
			if(usercompanys != null)
				for(Usercompanys uc : usercompanys){
					ids.add(uc.getCompanyinfo().getId());
				}
			if(ids.size() > 0){
			    Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
			            new SearchFilter("id", SearchFilter.Operator.IN, ids.toArray()),
			            new SearchFilter("companytype",Operator.EQ,"company"),
			            new SearchFilter("companystatus",Operator.NOTEQ,"4"));
			    companys = cpService.findPage(specification, page);
			}
		}else{
			Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
					new SearchFilter("companytype", Operator.EQ,"company"),
					new SearchFilter("companystatus", Operator.NOTEQ, "4"));
			companys = cpService.findPage(specification, page);
		}
		if(companys.size() > 0){
		    for(Companyinfo company : companys) {
		        if(company.getCompanystatus().equals("1")) {
		            company.setCompanystatus("已审核");
		        }else if(company.getCompanystatus().equals("3")) {
		            company.setCompanystatus("无效");
		        }else {
		            company.setCompanystatus("未审核");
		        }
		    }
		}
		for(Companyinfo cc : companys){
			String username =userServ.findById(cc.getUserid()).getUsername();	
			cc.setUsername(username);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("user", SecurityUtils.getShiroUser().getUser());
		map.put("company", companys);
		return mapper.writeValueAsString(map);
	}

	//工厂data
	@RequestMapping("/data3/{parentid}")
	@ResponseBody
	public String data3(@PathVariable Long parentid,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		//		Long parentId = SecurityUtils.getShiroUser().getCompanyid();
		/*		List<Usercompanys> usercompanys = ucServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
		List<Long> ids = new ArrayList<Long>();
		if(usercompanys != null)
			for(Usercompanys uc : usercompanys){
				ids.add(uc.getCompanyinfo().getId());
			}*/
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
				//				new SearchFilter("id", SearchFilter.Operator.IN, ids.toArray()),
				new SearchFilter("companytype",Operator.EQ,"factory"),
				new SearchFilter("companystatus",Operator.NOTEQ,"3"),
				new SearchFilter("parentid", Operator.EQ, parentid));
		List<Companyinfo> companys = cpService.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("user", userServ.findById(SecurityUtils.getShiroUser().getUser().getId()));
		map.put("factory", companys);
		map.put("parentid", parentid);
		return mapper.writeValueAsString(map);
	}


	@RequestMapping("/registeddata")
	@ResponseBody
	public Map<String, Object> registeddata(HttpServletRequest request, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,new SearchFilter("companytype", Operator.EQ, "company"));
		List<Companyinfo> Companyinfos = cpService.findPage(specification, page);

		map.put("page", page);
		map.put("companyinfos", Companyinfos);
		return map;
	}

	//审批公司
	@RequiresPermissions("CompanyCheck:edit")
	@RequestMapping(value = "/updateStatus")
	@ResponseBody
	public String update(Long id, String status) {
		String msg = "操作成功!";
		try {
			//更新公司状态
			cpService.updateStatus(id, status);
			//更改公司的平台角色
			//			UserRole userRole = new UserRole();
			//			userRole.setRole(roleServ.get((long) 18));
			//			userRole.setUser(SecurityUtils.getShiroUser().getUser());
			//			userRoleService.saveOrUpdate(userRole);
			//给公司设role，并赋给该用户
			MesCompanyRole mesCompanyRole = new MesCompanyRole();
			mesCompanyRole.setCompanyid(id);
			mesCompanyRole.setName(cpService.findById(id).getCompanyname()+"公司管理员");
			mesCompanyRole.setChangeable("1");
			mcrServ.saveOrUpdate(mesCompanyRole);
			//给公司角色设置权限
			List<MesCompanyRolePermission> rolePermissions = new ArrayList<MesCompanyRolePermission>();
			List<RolePermission> rolePes =  roleServ.get((long) 16).getRolePermissions();
			for(RolePermission rolePermission : rolePes){
				MesCompanyRolePermission mesCompanyRolePermission = new MesCompanyRolePermission();
				mesCompanyRolePermission.setMesCompanyRole(mesCompanyRole);
				mesCompanyRolePermission.setPermission(rolePermission.getPermission());
				//mcrpServ.saveOrUpdate(mesCompanyRolePermission);
				rolePermissions.add(mesCompanyRolePermission);
			}
			mcrpServ.saveOrUpdates(rolePermissions);
			mcrpServ.save(rolePermissions);
			mesCompanyRole.setRolePermissions(rolePermissions);
			mcrServ.saveOrUpdate(mesCompanyRole);

			UserCompanyrole userCompanyrole = new UserCompanyrole();
			userCompanyrole.setUserId(cpService.findById(id).getUserid());
			userCompanyrole.setPriority(999);
			userCompanyrole.setMesCompanyRole(mesCompanyRole);
			ucrserv.saveOrUpdate(userCompanyrole);
		} catch (ServiceException e) {
			return AjaxObject.newError("操作失败！").setCallbackType("").toString();
		}
		if (status.equals("4")){
			return AjaxObject.newOk(msg).toString();
		}
		return AjaxObject.newOk(msg).toString();
	}

	//修改公司信息、公司资质审批
	@RequiresPermissions(value={"CompanyList:edit","CompanyCheck:edit"},logical=Logical.OR)
	@RequestMapping("/findCompanyByid/{id}")
	public String showCompanyInfo(@PathVariable Long id, Map<String, Object> map,HttpServletRequest request,Page page, String pagename) {
		Companyinfo cpinfo = cpService.findById(id);
		List<Companyfile> fileList = cfService.findByParentidAndType(cpinfo.getId(),CompanyfileType.COMPANYINFOFILE.toString());
		//所属行业
		Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class
				,new SearchFilter("parent.id", Operator.EQ,63)
				,new SearchFilter("value", Operator.NOTEQ,cpinfo.getBusinesstype())
				);
		List<Dictionary> Dictionary1 = dicServ.findByExample(specification1, page);
		//类型
		Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class
				,new SearchFilter("parent.id", Operator.EQ,69)
				,new SearchFilter("value", Operator.NOTEQ,cpinfo.getInfotype())
				);
		List<Dictionary> Dictionary2 = dicServ.findByExample(specification2, page);
		map.put("Dictionary1", Dictionary1);
		map.put("Dictionary2", Dictionary2);
		map.put("companyinfo", cpinfo);
		map.put("companyfiles", fileList);
		return PAGEPRE + pagename; // "showCompanyinfo";"editCompanyinfo"
	}

	//修改工厂 信息
	@RequiresPermissions("Factory:edit")
	@RequestMapping("/findFactoryByid/{id}")
	public String showFactoryInfo(@PathVariable Long id, Map<String, Object> map, String pagename) {
		Companyinfo cpinfo = cpService.findById(id);
		map.put("companyinfo", cpinfo);
		return PAGEPRE + pagename; 
	}

	//注册公司
	@RequestMapping(value="registCompanyInfo",method = RequestMethod.GET)
	public String registCompany(Map<String, Object> map, HttpServletRequest request, Page page) {
		//所属行业
		Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
				new SearchFilter("parent.id", Operator.EQ,63));
		List<Dictionary> Dictionary1 = dicServ.findByExample(specification1, page);
		//类型
		Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
				new SearchFilter("parent.id", Operator.EQ,69));
		List<Dictionary> Dictionary2 = dicServ.findByExample(specification2, page);
		map.put("Dictionary1", Dictionary1);
		map.put("Dictionary2", Dictionary2);
		return PAGEPRE + "registCompanyInfo";
	}

	/**
	 * 用于保存公司信息
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String create(@RequestParam("files") MultipartFile[] files, Companyinfo companyinfo,String[] pictureids) {
		String msg = "修改";
		if(companyinfo.getId() == null)
			msg = "注册"; 
		//      if(!this.createCompanyAble())
		//          return AjaxObject.newOk("不能创建公司！").toString();
		/*if(!this.checkPictureType(files))
			return AjaxObject.newError("不支持该文件格式!").setCallbackType("").toString();*/
		List<Companyfile> companyfilelist = new ArrayList<Companyfile>();
		if (files != null && files.length > 0) {
			companyfilelist = new ArrayList<Companyfile>();
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					try {
						fileSave(companyfilelist, file);
					} catch (Exception e) {
						e.printStackTrace();
						return AjaxObject.newError( msg +"失败:请检查上传文件是否正确!").setCallbackType("").toString();
					}
				}
			}
		}
		if(companyinfo.getId() == null) {
			try {
				if(cpService.findByCompanyname(companyinfo.getCompanyname()) != null)
					return AjaxObject.newError("该公司已存在！").setCallbackType("").toString();
				companyinfo.setCreatetime(new java.sql.Timestamp(new Date().getTime()));
				companyinfo.setUserid(SecurityUtils.getLoginUser().getId());
				companyinfo.setCompanystatus("4");
				//在保存附件时需要删除以前旧的附件(如果有)
				/*if(companyinfo.getId() != null)
				cfService.deleteCompanyfileByParentid(companyinfo.getId());*/
				User user = SecurityUtils.getShiroUser().getUser();
				companyinfo.setCompanytype(Constants.COMPANY);
				Companyinfo companyinfo2 = cpService.saveAndFile(companyinfo, companyfilelist);
				Usercompanys usercp = new Usercompanys();
				usercp.setCompanyinfo(companyinfo);
				usercp.setUserid(user.getId());
				ucServ.saveOrUpdateInfo(usercp);
				//创建公司时不再创建默认部门
//				Department department = new Department();
//				department.setCompanyid(companyinfo.getId());
//				department.setDepartment(null);
//				department.setName(companyinfo.getCompanyname());
//				department.setPrincipal(companyinfo.getLegalperson());
//				dpServ.saveOrUpdate(department);
			} catch (Exception e) {
				e.printStackTrace();
				return AjaxObject.newError(msg +"公司失败：请检查填写信息!").setCallbackType("").toString();
			}
			return AjaxObject.newOk(msg + "公司成功！").toString();
			//return "redirect:/companyManage/companyList";
		}else {
			try {
				Long userId = cpService.findById(companyinfo.getId()).getUserid();
				String companyname = cpService.findById(companyinfo.getId()).getCompanyname();
				if(SecurityUtils.getShiroUser().getUser().getId().compareTo(userId) != 0)
					return AjaxObject.newError("没有修改公司权限！").setCallbackType("").toString();
				if(cpService.findByCompanyname(companyinfo.getCompanyname()) != null) {
					if(cpService.findByCompanyname(companyinfo.getCompanyname()).getId().compareTo(companyinfo.getId()) != 0)
						return AjaxObject.newError("该公司名称已存在！").setCallbackType("").toString();
				}

				List<UserCompanyrole> ucRole = ucrserv.findByUserIdAndMesCompanyRoleCompanyid(userId,companyinfo.getId());
				for(UserCompanyrole ucRoles : ucRole){
					mcrServ.delete(ucRoles.getMesCompanyRole().getId());
				}
				companyinfo.setUserid(SecurityUtils.getLoginUser().getId());
				companyinfo.setCompanytype(Constants.COMPANY);
				companyinfo.setCompanystatus("4");
				/*if(companyfilelist != null && companyfilelist.size() > 0)
					cfService.deleteCompanyfileByParentid(companyinfo.getId());
				cpService.saveAndFile(companyinfo, companyfilelist);*/
				cpService.saveOrUpdateForfile(companyinfo, companyfilelist, pictureids);
				updatePointsInfoInRedis(companyinfo.getId());
			}catch(Exception e) {
				return AjaxObject.newError(msg + "公司失败！").setCallbackType("").toString();
			}
			return AjaxObject.newOk(msg + "公司成功！需管理员审核！").toString();
		}
	}

	//检查图片格式
	private boolean checkPictureType(MultipartFile[] files){
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					try {
						String fileBaseName = file.getOriginalFilename();
						System.out.println(fileBaseName.substring(fileBaseName.lastIndexOf(".")).toLowerCase()+"====================");
						if(Companyfile.picTypeMap.get(fileBaseName.substring(fileBaseName.lastIndexOf(".")).toLowerCase())==null)
							return false;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 用来检查用户是否能创建公司
	 * 当user表中的companyid不为null时,则不能创建公司
	 * @return	
	 */
	private void fileSave(List<Companyfile> companyfilelist, MultipartFile file) throws IOException {
		Companyfile cpfile;
		String fileBaseName = file.getOriginalFilename();
		String newFileName = UUID.randomUUID().toString()
				+ fileBaseName.substring(fileBaseName.lastIndexOf("."));
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		System.out.println("this is a test:"+this.getClass().getClassLoader().getResource("/").getPath());
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
		companyfilelist.add(cpfile);
	}

	//删除公司、工厂
	@RequiresPermissions(value={"CompanyList:delete","Factory:delete","CompanyRegisted:delete"},logical=Logical.OR)
	@RequestMapping("/deleteCompanyinfo")
	@ResponseBody
	public String deleteRegistCompanyinfo(Long[] ids) {
		try {
			if(ids!=null && ids.length>0){
				String key = "ok";
				//判断选择的目标是否都可以删除
				for(int i = 0; i < ids.length; i++){
					Companyinfo info = cpService.findById(ids[i]);
					if(info.getCompanytype().equals("company")){
						if(SecurityUtils.getShiroUser().getUser().getId().compareTo(info.getUserid()) != 0){
							key = "没有删除公司权限！";
							break;
						}
					}else{
						List<MesProductline> mpl = lineServ.findByCompanyid(ids[i]);
						if(mpl.size() != 0){
							key = "有工厂关联产线！无法删除！";
							break;
						}
						List<Department> departments = dpServ.findForFactory(ids[i]);
						departments.addAll(findChildDept(departments));
						if (!departments.isEmpty()){
							key = "有工厂关联部门！无法删除！";
							break;
						}
						if(SecurityUtils.getShiroUser().getUser().getId().compareTo(info.getUserid()) != 0){
							key = "没有删除工厂权限！";
							break;
						}
				    }
				}
				if(!"ok".equals(key)){
					return AjaxObject.newError(key).setCallbackType("").toString();
				}
				for(int i = 0; i < ids.length; i++){
					Companyinfo info = cpService.findById(ids[i]);
					if(info.getCompanytype().equals("company")){
						info.setCompanystatus(Constants.COMPANYINFODEL);
						cpService.saveOrUpdate(info);
					}else{
						cpService.deleteById(ids[i]);
					}
				}
				
			}
		} catch (Exception e) {
			return AjaxObject.newError("错误，删除失败！").setCallbackType("").toString();
		}
    	return AjaxObject.newOk("删除成功！").setCallbackType("").toString();
	}

	private List<Department> findChildDept(List<Department> departments) {
		List <Department> result = new ArrayList<>();
		for (Department department : departments) {
			result.addAll(dpServ.findByParentid(department.getId()));
		}
		if (!result.isEmpty()) {//传来的部门有子部门则继续递归
			result.addAll(findChildDept(result));
		}
		return result;
	}

	//管理员修改公司状态
	@RequiresPermissions("CompanyRegisted:delete")
	@RequestMapping("/editStatus/{companyid}")
	public String updateStatus(@PathVariable Long companyid,Map<String,Object> map){
	    Companyinfo companyinfo = cpService.findById(companyid);
		map.put("companyid", companyid);
		map.put("companyinfo", companyinfo);
		return PAGEPRE + "updateStatus";
	}
	
	@RequiresPermissions("CompanyRegisted:delete")
	@RequestMapping("/updateCompany/{companyid}")
	@ResponseBody
	public String updateCompany(@PathVariable Long companyid,HttpServletRequest request) {
		try {
				String status = request.getParameter("status");
				Companyinfo companyinfo = cpService.findById(companyid);
				companyinfo.setCompanystatus(status);
				if(!status.equals("1")){
				    List<UserCompanyrole> ucRole = ucrserv.findByUserIdAndMesCompanyRoleCompanyid(companyinfo.getUserid(),companyinfo.getId());
				    for(UserCompanyrole ucRoles : ucRole){
				        mcrServ.delete(ucRoles.getMesCompanyRole().getId());
				    }
				}else{
				    this.update(companyid, status);
				}
				cpService.saveOrUpdate(companyinfo);
		} catch (Exception e) {
			return AjaxObject.newError("修改公司状态失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("修改公司状态成功！").toString();
	}

	//查看本公司信息
	@RequiresPermissions(value={"CompanyList:show","CompanyRegisted:show"},logical=Logical.OR)
	@RequestMapping("/myCompany/{id}")
	public String showMyCompanyInfo(Model model,@PathVariable Long id) {
		model.addAttribute("companyinfo", cpService.findById(id));
		model.addAttribute("companyfile", cfService.findByParentidAndType(id, CompanyfileType.COMPANYINFOFILE.toString()));
		return PAGEPRE + "myselfCompany";
	}


	@RequestMapping("/joinCompanypage")
	public String toJoinCompanyPage(){
		return PAGEPRE + "joinCompanys";
	}

	/**
	 * 用戶已加入公司分頁數據
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping("/joinCompanysData")
	@ResponseBody
	public Map<String,Object> findJionCompany(HttpServletRequest request,Page page){
		Map<String, Object> map = new HashMap<String, Object>();
		List<SearchFilter> sfList = new ArrayList<SearchFilter>();
		sfList.add(new SearchFilter("userid",SearchFilter.Operator.EQ,SecurityUtils.getShiroUser().getId()));
		Specification<Usercompanys> specification 
		= DynamicSpecifications.bySearchFilter(request, Usercompanys.class,sfList);
		List<Usercompanys> userCompanys = ucServ.findPage(specification, page);

		map.put("page", page);
		map.put("userCompanys", userCompanys);
		return map;
	}

	@RequestMapping("/joinCompany/{companyid}")
	@ResponseBody
	public String joinCompany(@PathVariable Long companyid){
		try{
			Long userid = SecurityUtils.getShiroUser().getId();
			Usercompanys uc = new Usercompanys();
			uc.setUserid(userid);
			uc.setCompanyinfo(cpService.findById(companyid));
			ucServ.saveOrUpdateInfo(uc);
		}catch(Exception e){
			return AjaxObject.newOk("加入失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("加入成功!").toString();
	}

	@RequestMapping("/outCompany/{companyid}")
	@ResponseBody
	public String outCompany(@PathVariable Long companyid){
		try{
			Long userid = SecurityUtils.getShiroUser().getId();
			ucServ.deleteinfoByUserIdAndCompanyId(userid, companyid);
		}catch(Exception e){
			return AjaxObject.newOk("退出失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("退出成功!").toString();
	}

	/**
	 * 通过图片的id来展示
	 */
	@RequestMapping("/showPic/{id}")
	public void showPic(@PathVariable Long id, HttpServletResponse response) {
		FileInputStream in = null;
		OutputStream os = null;
		response.setContentType("image/jpeg");
		try {
			Companyfile file = cfService.findById(id);
			// 图片读取路径
			// in=new FileInputStream("E:\\picture\\"+photoName);
			in=new FileInputStream(file.getfilepath() + File.separator + file.getfilenewname());
			int i = in.available();
			byte[] data = new byte[i];
			in.read(data);

			// 写图片
			os = new BufferedOutputStream(response.getOutputStream());
			os.write(data);
			os.flush();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			if(os != null)
				try {
					os.close();
				} catch (IOException e) {
				}
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
	}

	@RequestMapping("/upload")
	public String toUploadPage(){

		return PAGEPRE + "fileupload";
	}

	@RequiresPermissions("Factory:view")
	@RequestMapping(value="/tree_list", method={RequestMethod.GET, RequestMethod.POST})
	public String tree_list(Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
		map.put("id", companyId);
		return TREE_LIST;
	}

	@RequiresPermissions("Factory:view")
	@RequestMapping(value="/tree", method=RequestMethod.GET)
	public String tree(Map<String, Object> map) {
		Companyinfo companyinfo = cpService.getCompanyTree();//cpService.getTreeFactory();
		map.put("companyinfo", companyinfo);
		return TREE;
	}

	@RequiresPermissions("Factory:view")
	@RequestMapping(value="/list/{parentid}", method={RequestMethod.GET, RequestMethod.POST})
	public String list(ServletRequest request, Page page, @PathVariable Long parentid, Map<String, Object> map) {
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
				new SearchFilter("companytype",Operator.EQ,"factory"),
				new SearchFilter("companystatus",Operator.NOTEQ,"3"),
				new SearchFilter("parentid", Operator.EQ, parentid));
		List<Companyinfo> companyinfos = cpService.findPage(specification, page);

		map.put("page", page);
		map.put("companyinfos", companyinfos);
		map.put("parentid", parentid);

		return LIST;
	}

	@RequestMapping("/checkName/{companyname}")
	@ResponseBody
	public String checkName(@PathVariable String companyname,HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class
				,new SearchFilter("companyname", Operator.EQ, companyname)
				,new SearchFilter("companystatus", Operator.NOTEQ, "3")
				,new SearchFilter("companytype", Operator.EQ, "company")
				);
		List<Companyinfo> user = cpService.findPage(specification, page);
		try {
			if(user.size() > 0){
				map.put("0", 0);
			}else {
				map.put("1", 1);
			}
		} catch (Exception e) {

			return null;
		}
		return mapper.writeValueAsString(map);
	}
}
