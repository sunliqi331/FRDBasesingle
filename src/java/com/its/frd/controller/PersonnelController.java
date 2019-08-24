package com.its.frd.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.entity.main.User;
import com.its.common.log.Log;
import com.its.common.service.UserService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Department;
import com.its.frd.entity.MesCompanyPosition;
import com.its.frd.entity.MesCompanyRole;
import com.its.frd.entity.MesPermissionTransfer;
import com.its.frd.entity.MesUserPosition;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.entity.Usercompanys;
import com.its.frd.entity.Userdepartment;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.DepartmentService;
import com.its.frd.service.MesCompanyPositionService;
import com.its.frd.service.MesCompanyRoleService;
import com.its.frd.service.MesPermissionTransferService;
import com.its.frd.service.MesUserPositionService;
import com.its.frd.service.UserCompanyroleService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.service.UserdepartmentService;

@Controller
@RequestMapping("/personnel")
public class PersonnelController {

    private static final String TRANSFERPERMISSIONRECORD = "personnel/transferPermissionRecord";
    private static final String INPUTPHONE = "personnel/transferPermission";
	private static final String LOOK_UP_COMPANYROLE = "personnel/assign_user_companyRole";
	private static final String LOOK_USER_COMPANYROLE = "personnel/delete_user_companyRole";
	private static final String LIST = "personnel/personnelList";
	private static final String TREE = "personnel/tree";
	private static final String TREE_LIST = "personnel/tree_list";

	@Resource
	private CompanyinfoService cpService;
	@Resource
	private UserService userServ;
	@Resource
	private UsercompanysService ucServ;
	@Resource
	private UserdepartmentService udServ;
	@Resource 
	private DepartmentService deparServ;
	@Resource
	private MesCompanyPositionService mcpServ;
	@Resource
	private MesUserPositionService mupServ;
	@Resource 
	private UserCompanyroleService userCompanyroleService;
	@Resource 
	private MesCompanyRoleService mesCompanyRoleService;
	@Resource 
	private UserdepartmentService userdepartmentService;
	@Resource 
	private MesUserPositionService mesUserPositionService;
	@Resource 
	private MesPermissionTransferService mesPermissionTransferService;
	@Resource
    private JavaMailSender mailSender;

	//	@RequestMapping("/personnellist")
	//	public String personnellist() {
	//		return "personnel/personnelList";
	//	}

	@RequiresPermissions("Personnel:viewPosition")
	@RequestMapping("/positionList/{userId}")
	public String positionList(@PathVariable Long userId,Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();

		map.put("companyId", companyId);
		map.put("userId", userId);
		map.put("user", userServ.findById(userId));
		return "personnel/positionList";
	}

	@RequiresPermissions("Personnel:viewDepartment")
	@RequestMapping("/departmentList/{userId}")
	public String departmentList(@PathVariable Long userId,Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		map.put("companyId", companyId);
		map.put("userId", userId);
		map.put("user", userServ.findById(userId));
		return "personnel/departmentList";
	}

	@RequiresPermissions("Personnel:save")
	@RequestMapping("/personnelInvite")
	public String personnelInvite(HttpServletRequest request,Page page,Map<String, Object> map) {
//		map.put("parentDepartmentId", parentDepartmentId);
		List<Department> departments = deparServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		map.put("departments", departments);
		
		return "personnel/personnelInvite";
	}

	@RequiresPermissions("Personnel:savePosition")
	@RequestMapping("/addPosition/{userId}")
	public String addPosition(@PathVariable Long userId,Map<String, Object> map) {
		List<MesCompanyPosition> mesCompanyPosition = mcpServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		map.put("cposition", mesCompanyPosition);
		map.put("userId", userId);
		return "personnel/addPosition";
	}

	@RequiresPermissions("Personnel:saveDepartment")
	@RequestMapping("/addDepartment/{userId}")
	public String addDepartment(@PathVariable Long userId, HttpServletRequest request,Page page,Map<String, Object> map) {
//		Department parentDepar = deparServ.findByDepartmentIdAndCompanyid(SecurityUtils.getShiroUser().getCompanyid());
//		List<Department> department = new ArrayList<>();
//		if(parentDepar != null) {
//			Specification<Department> specification = DynamicSpecifications.bySearchFilter(request, Department.class,
//					new SearchFilter("companyid", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
//					new SearchFilter("department.id", Operator.EQ, parentDepar.getId()));
//			department = deparServ.findPage(specification, page);
//		}
		List<Companyinfo> companyinfos = new ArrayList<>();
		
		Companyinfo company = cpService.findById(SecurityUtils.getShiroUser().getCompanyid());
		
		List<Companyinfo> currentFactorys = cpService.findByParentidAndCompanytype(SecurityUtils.getShiroUser().getCompanyid(), "factory");
		List<Companyinfo> factorys = new ArrayList<>();
		for (Companyinfo factory : currentFactorys) {
			factorys.add(factory);
			factorys.addAll(findChildFactory(factory,1));
		}	
		
		companyinfos.addAll(factorys);
		
		List<Department> department = deparServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		map.put("page", page);
		map.put("companyinfos", companyinfos);
		map.put("currentCompanyName", company.getCompanyname());
//		map.put("department", department);
		map.put("userId", userId);
		return "personnel/addDepartment";
	}
	
	private List<Companyinfo> findChildFactory(Companyinfo parentFactory, int offset) {
		List<Companyinfo> result = new ArrayList<>();
		List<Companyinfo> factorys = cpService.findByParentidAndCompanytype(parentFactory.getId(), "factory");
		if (!factorys.isEmpty()) {
			for (Companyinfo factory : factorys) {
				factory.setCompanyname(spacing(offset)+factory.getCompanyname());
				result.add(factory);
				result.addAll(findChildFactory(factory,offset+1));
			}
		}
		return result;
	}
	
	@RequestMapping("/findDepartment/{factoryId}")
	@ResponseBody
	public String findDepartment(@PathVariable Long factoryId) throws JsonProcessingException{
		List<Department> departments;
		List<Department> results = new ArrayList<>();

		if (factoryId==0) {
			departments = deparServ.findForCompany(SecurityUtils.getShiroUser().getCompanyid());
		}else {
			departments = deparServ.findForFactory(factoryId);
		}
		int length = departments.size();
		for (int i = 0; i < length; i++) {
			results.add(departments.get(i));
			results.addAll(findChildDept(departments.get(i), 1));
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(results);
	}
	private List<Department> findChildDept(Department parentDepartment,int offset) {//FIXME
		List<Department> list = deparServ.findByParentid(parentDepartment.getId());
		List<Department> result = new ArrayList<>();
		
		//按照offset添加空格，形成树形结构
		if (!list.isEmpty()) {
			int length = list.size();
			for(int i = 0; i < length; i++) {
				Department department = list.get(i);
				department.setName(spacing(offset)+department.getName());
				result.add(department);
				result.addAll(findChildDept(department, offset+1));
			}
		}
		return result;
	}
	private String spacing(int offset) {
		String space = "";
		for (int i = 0; i < offset; i++) {
			space+="　";
		}
		return space;
	}

	@RequestMapping("/data")
	@ResponseBody //业务逻辑：寻找本公司所有员工，并标记各员工的所属部门
	public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Long companyid = SecurityUtils.getShiroUser().getCompanyid();
		//判断当前登录公司是否为空
		if(companyid == null){
			return AjaxObject.newError("公司不存在！").setCallbackType("").toString();
		}
		List<User> users = new ArrayList<>();
		//找出当前公司的全部员工
		List<Usercompanys> usercompanys = ucServ.findByCompanyid(companyid);
		List<Long> ids = new ArrayList<Long>();
		if(usercompanys.size() > 0){
			for(Usercompanys uc : usercompanys){
				//将找出的userid存入数组ids里
				ids.add(uc.getUserid());
			}
			ids.remove(SecurityUtils.getShiroUser().getUser().getId());
			//ids.remove(cpService.findById(SecurityUtils.getShiroUser().getCompanyid()).getUserid());
			List<User> users1 = new ArrayList<User>();
			if(ids.size() > 0){
			    //找出所有userid在ids里的user
			    Specification<User> specification2 = DynamicSpecifications.bySearchFilter(request, User.class,
			            new SearchFilter("id", SearchFilter.Operator.IN, ids.toArray())
			            );
			    users = userServ.findByExample(specification2, page);
			}
		    for(User user : users){
		        //查找出人员在当前登录公司下的职位
		        if(user != null) {
		            List<MesUserPosition> mesUserPositions = mesUserPositionService.findByUseridAndCompanyId(user.getId(), SecurityUtils.getShiroUser().getCompanyid());
		            StringBuilder position = new StringBuilder();
		            for(MesUserPosition mesUserPosition : mesUserPositions){
		                position.append(mesUserPosition.getMesCompanyPosition().getPositionname() + " ");
		            }
		            user.setPosition(position.toString());
		            //找出当前公司的全部部门
		    		List<Department> departments = deparServ.findByCompanyid(companyid);
		    		List<Long> departmentIds = new ArrayList<>();
		    		for (Department department : departments) {
		    			departmentIds.add(department.getId());
		    		}
		            
		    		//设置员工所属部门显示字符串
		            String depts = "";
		            List<Userdepartment> userdepartments =  udServ.findByUserid(user.getId());
		            for (Userdepartment userdepartment : userdepartments) {
		            	if (departmentIds.contains(userdepartment.getDepartment().getId())) {
			            	depts+=userdepartment.getDepartment().getName()+",";
						}
					}
		            if (!depts.isEmpty()) {
			            user.setDepartment(depts.substring(0, depts.length()-1));

					}
		        }
		    }
			
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("user", users);
//		map.put("parentDepartmentId", parentDepartmentId);
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/data2")
	@ResponseBody
	public String data2(HttpServletRequest request,User user,Page page) throws JsonProcessingException {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Usercompanys> uc = ucServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		List<Long> ids = new ArrayList<>();
		for(Usercompanys ucs : uc) {
			ids.add(ucs.getUserid());
		}
		Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class
				,new SearchFilter("username", Operator.LIKE, user.getUsername())
				,new SearchFilter("realname", Operator.LIKE, user.getRealname())
				,new SearchFilter("registerstate", Operator.EQ, "1")
				,new SearchFilter("phone", Operator.LIKE, user.getPhone())
				,new SearchFilter("id", Operator.NOTEQ, SecurityUtils.getShiroUser().getUser().getId()));
		List<User> users = userServ.findByExample(specification);
		List<Long> uids = new ArrayList<>();
		for(User u : users) {
			uids.add(u.getId());
		}
		for(int i=uids.size()-1;i>=0;i--){
			if(ids.contains(uids.get(i)))
				uids.remove(i);
		}
		Specification<User> specification1 = null;
		if(uids.size() > 0){
			specification1 = DynamicSpecifications.bySearchFilter(request, User.class
					,new SearchFilter("id", Operator.IN,uids.toArray()));
		}
		List<User> user1 = new ArrayList<>();
		if(specification1 != null)
			user1 = userServ.findByExample(specification1, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("user", user1);
		return mapper.writeValueAsString(map);
	}
	
	@RequestMapping("/data3")
	@ResponseBody
	public String data3(HttpServletRequest request,User user,Page page) throws JsonProcessingException {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Usercompanys> uc = ucServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		List<Long> ids = new ArrayList<>();
		for(Usercompanys ucs : uc) {
			ids.add(ucs.getUserid());
		}
		Specification<User> specification1 = null;
		if(ids.size() > 0){
			specification1 = DynamicSpecifications.bySearchFilter(request, User.class
					,new SearchFilter("id", Operator.IN,ids.toArray()));
		}
		List<User> user1 = new ArrayList<>();
		if(specification1 != null)
			user1 = userServ.findByExample(specification1, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("user", user1);
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/positionData/{userId}")
	@ResponseBody
	public String positionData(@PathVariable Long userId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesUserPosition> specification = DynamicSpecifications.bySearchFilter(request, MesUserPosition.class
				,new SearchFilter("userid", Operator.EQ, userId)
				,new SearchFilter("mesCompanyPosition.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid())
				);
		List<MesUserPosition> mesUserPositions = mupServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("position", mesUserPositions);
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/departmentData/{userId}")
	@ResponseBody
	public String departmentData(@PathVariable Long userId,HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<Userdepartment> specification = DynamicSpecifications.bySearchFilter(request, Userdepartment.class
				,new SearchFilter("userid", Operator.EQ, userId)
				,new SearchFilter("department.companyid", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid())
				);
		List<Userdepartment> userdepartments = udServ.findPage(specification, page);
		
		for (Userdepartment userdepartment : userdepartments) {
			Department department = userdepartment.getDepartment();
			if (department.getDepartment()!=null&&department.getDepartment().getId()!=null) {
				userdepartment.getDepartment().setParentInfo("[部门]"+deparServ.findById(department.getDepartment().getId()).getName());
			}else if (department.getFactoryid()!=null) {
				userdepartment.getDepartment().setParentInfo("[工厂]"+cpService.findById(department.getFactoryid()).getCompanyname());
			}else {
				userdepartment.getDepartment().setParentInfo("[公司]"+cpService.findById(SecurityUtils.getShiroUser().getCompanyid()).getCompanyname());
			}
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("department", userdepartments);
		return mapper.writeValueAsString(map);
	}

	/*
	 * 删除人员
	 */
	@RequiresPermissions("Personnel:delete")
	@RequestMapping(value="/deleteUserById", method = RequestMethod.POST)
	@ResponseBody
	public String deleteUserById(Long[] ids) {
		try{
			if(SecurityUtils.getShiroUser().getUser().getId().compareTo(cpService.findById(SecurityUtils.getShiroUser().getCompanyid()).getUserid()) != 0)
				return AjaxObject.newError("无删除人员权限！").setCallbackType("").toString();
			for (int i = 0; i < ids.length; i++) {
				//删除user与company关联
				ucServ.deleteinfoByUserIdAndCompanyId(ids[i], SecurityUtils.getShiroUser().getCompanyid());
				//删除user与本公司position关联
				mesUserPositionService.deleteinfoByUserIdAndCompanyId(ids[i], SecurityUtils.getShiroUser().getCompanyid());
				//删除user与本公司department关联
				List<Userdepartment> ud = udServ.findByUserid(ids[i]);
				for(Userdepartment list : ud) {
					if(list.getDepartment().getCompanyid().compareTo(SecurityUtils.getShiroUser().getCompanyid()) == 0)
						udServ.deleteById(list.getId());
				}
				//删除user与本公司role关联
				List<UserCompanyrole> ucr = userCompanyroleService.findByUserIdAndMesCompanyRoleCompanyid(ids[i], SecurityUtils.getShiroUser().getCompanyid());
				for(UserCompanyrole list : ucr) {
					userCompanyroleService.delete(list.getId());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return AjaxObject.newError("删除人员失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除人员成功!").toString();
	}


	@RequiresPermissions("Personnel:deletePosition")
	@RequestMapping("/deletePositionById")
	@ResponseBody
	public String deletePositionById(Long[] ids) {
		try{
			for (int i = 0; i < ids.length; i++) {
				mupServ.deleteById(ids[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
			return AjaxObject.newError("删除职位失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除职位成功!").toString();
	}

	/*
	 * 删除部门
	 */
	@RequiresPermissions("Personnel:deleteDepartment")
	@RequestMapping("/deleteDepartmentById")
	@ResponseBody
	public String deleteDepartmentById(Long[] ids) {
		try{
			for (int i = 0; i < ids.length; i++) {
				udServ.deleteById(ids[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
			return AjaxObject.newError("删除部门失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除部门成功!").toString();
	}

	/*
	 * 未做邀请方式处理，目前是直接添加用户到公司部门
	 */
	@RequiresPermissions("Personnel:save")
	@RequestMapping("/invite")
	@ResponseBody
	public String invite(HttpServletRequest request) {
		//Long departmentId = Long.valueOf(request.getParameter("inviteDept"));
		//Department deparment = deparServ.findByDepartmentIdAndCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		try {
			List<Usercompanys> uc = ucServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
			List<Long> ids = new ArrayList<>();
			for(Usercompanys ucs : uc) {
				ids.add(ucs.getUserid());
			}

			String userids = request.getParameter("userids");
			if (userids.length() > 0) {
				String[] arr = userids.split(",");
				for(int i=0;i<arr.length;i++){
					//					boolean bool = ids.contains(Long.valueOf(arr[i]));
					if(!ids.contains(Long.valueOf(arr[i]))) {//页面选择的ID不在员工公司表里的时候
						Usercompanys usercompanys = new Usercompanys();
						usercompanys.setUserid(Long.valueOf(arr[i]));
						usercompanys.setCompanyinfo(cpService.findById(SecurityUtils.getShiroUser().getCompanyid()));
						ucServ.saveOrUpdateInfo(usercompanys);
						//邀请人员只是进入公司，不进入部门
//						if(parentDepartmentId.compareTo(deparment.getId()) != 0){
//							Userdepartment userdepartments = new Userdepartment();
//							userdepartments.setDepartment(deparServ.findById(departmentId));
//							userdepartments.setUserid(Long.valueOf(arr[i]));
//							udServ.saveOrUpdate(userdepartments);
//						}
					}
				}
			}else {
				return AjaxObject.newError("未选择人员！").setCallbackType("").toString();
			}
		}catch(Exception e) {
			return AjaxObject.newError("邀请失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("邀请成功！").toString();
	}

	@RequiresPermissions("Personnel:savePosition")
	@RequestMapping("/savePosition/{userId}")
	@ResponseBody
	public String savePosition(@PathVariable Long userId,MesUserPosition mesUserPosition) {
		String msg = "修改";
		if(mesUserPosition.getId() == null)
			msg = "添加";
		if(userId == null)
			return AjaxObject.newError("无人员!").setCallbackType("").toString();
		if(mesUserPosition.getMesCompanyPosition().getId() == 0)
			return AjaxObject.newError("无职位！").setCallbackType("").toString();
		List<MesUserPosition> mesUserPositions = mupServ.findByUseridAndCompanyId(userId, SecurityUtils.getShiroUser().getCompanyid());
		for(MesUserPosition mup : mesUserPositions) {
			if(mup.getMesCompanyPosition().getId() == mesUserPosition.getMesCompanyPosition().getId())
				return AjaxObject.newError("该人员已拥有该职位！").setCallbackType("").toString();
		}
		try {
			mesUserPosition.setUserid(userId);
			mesUserPosition.setMesCompanyPosition(mcpServ.findById(mesUserPosition.getMesCompanyPosition().getId()));
			mupServ.saveOrUpdate(mesUserPosition);
		}catch(Exception e) {
			return AjaxObject.newError(msg + "职位失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "职位成功！").toString();
	}

	@RequiresPermissions("Personnel:saveDepartment")
	@RequestMapping("/saveDepartment/{userId}")
	@ResponseBody
	public String saveDepartment(@PathVariable Long userId,Userdepartment userdepartment) {
		String msg = "修改";
		if(userdepartment.getId() == null)
			msg = "添加";
		if(userId == null)
			return AjaxObject.newError("无人员!").setCallbackType("").toString();
		if(userdepartment.getDepartment()==null||userdepartment.getDepartment().getId() == 0)
			return AjaxObject.newError("无部门！").setCallbackType("").toString();
		List<Userdepartment> ud = udServ.findByUserid(userId);
		for(Userdepartment uds : ud) {
			if(uds.getDepartment().getId().equals(userdepartment.getDepartment().getId()))
				return AjaxObject.newError("该人员已在该部门！").setCallbackType("").toString();
		}
		try {
			userdepartment.setUserid(userId);
			udServ.saveOrUpdate(userdepartment);
		}catch(Exception e) {
			return AjaxObject.newError(msg + "部门失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "部门成功！").toString();
	}

	/**
	 * 添加用户和公司信息
	 */
	@Log(message = "向{0}用户分配了{1}的公司角色。")
	@RequiresPermissions("Personnel:assign")
	@RequestMapping(value = "/create/userCompanyRole", method = { RequestMethod.POST })
	public @ResponseBody void assignCompanyRole(UserCompanyrole userCompanyrole) {
		userCompanyroleService.saveOrUpdate(userCompanyrole);
	}

	@RequiresPermissions("Personnel:assign")
	@RequestMapping(value = "/lookup2create/userCompanyRole/{userId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String listUnassignCompanyRole(Map<String, Object> map, @PathVariable Long userId,HttpServletRequest request) {
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByUserIdAndMesCompanyRoleCompanyid(userId,SecurityUtils.getShiroUser().getCompanyid());
		Specification<MesCompanyRole> specification = DynamicSpecifications.bySearchFilter(request, MesCompanyRole.class,
				new SearchFilter("companyid", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("changeable", Operator.EQ, "0")
				);
		List<MesCompanyRole> mesCompanyRoles = mesCompanyRoleService.findByExample(specification, page);
		List<MesCompanyRole> rentList = new ArrayList<MesCompanyRole>();
		// 删除已分配roles
		for (MesCompanyRole mesCompanyRole : mesCompanyRoles) {
			boolean isHas = false;
			for (UserCompanyrole or : userCompanyroles) {
				if (or.getMesCompanyRole().getId().equals(mesCompanyRole.getId())) {
					isHas = true;
					break;
				}
			}
			if (isHas == false) {
				rentList.add(mesCompanyRole);
			}
		}
		map.put("mesCompanyRoles", rentList);
		map.put("userCompanyroles", userCompanyroles);
		map.put("userId", userId);
		return LOOK_UP_COMPANYROLE;
	}

	@RequiresPermissions("Personnel:assign")
	@RequestMapping(value = "/lookup2delete/userCompanyRole/{userId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String listUserCompanyRole(Map<String, Object> map, @PathVariable Long userId, HttpServletRequest request, Page page) {
	    Specification<UserCompanyrole> specification = DynamicSpecifications.bySearchFilter(request, UserCompanyrole.class,
                new SearchFilter("mesCompanyRole.companyid", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()),
                new SearchFilter("mesCompanyRole.changeable", Operator.EQ, "0"),
                new SearchFilter("userId", Operator.EQ, userId)
                );
		List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByExample(specification, page);
		map.put("userCompanyroles", userCompanyroles);
		return LOOK_USER_COMPANYROLE;
	}

	@RequiresPermissions("Personnel:assign")
	@Log(message = "撤销了{0}用户的{1}角色。")
	@RequestMapping(value = "/delete/userCompanyRole/{userCompanyRoleId}", method = { RequestMethod.POST })
	public @ResponseBody void deleteUserCompanyRole(@PathVariable Long userCompanyRoleId) {
		userCompanyroleService.delete(userCompanyRoleId);
	}

	@RequestMapping("/transferPermissionRecord")
	public String transferPermissionRecord() {
	    return TRANSFERPERMISSIONRECORD;
	}
	

    @RequestMapping("/transferPermissionRecordData")
    @ResponseBody
    public String transferPermissionRecordData(HttpServletRequest request, Page page) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();
        Specification<MesPermissionTransfer> specification = DynamicSpecifications.bySearchFilter(request, MesPermissionTransfer.class
                ,new SearchFilter("companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid())
                );
        List<MesPermissionTransfer> mesPermissionTransfers = mesPermissionTransferService.findByPage(specification, page);
//        if(mesPermissionTransfers.size() > 0){
//            for(MesPermissionTransfer mesPermissionTransfer : mesPermissionTransfers){
//                mesPermissionTransfer.setUserByFromuserid(mesPermissionTransfer.getUserByFromuserid());
//                mesPermissionTransfer.setUserByTouserid(mesPermissionTransfer.getUserByTouserid());
//            }
//        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        map.put("mesPermissionTransfer", mesPermissionTransfers);
        return mapper.writeValueAsString(map);
    }


    @RequestMapping("/checkPhone/{phone}")
    @ResponseBody
    public String checkPhone(@PathVariable String phone,HttpServletRequest request) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        String oldPhone = SecurityUtils.getShiroUser().getUser().getPhone();
        try {
            if(phone.equals(oldPhone)){
                map.put("0", 0);
            }else {
                map.put("1", 1);
            }
        } catch (Exception e) {

            return null;
        }
        return mapper.writeValueAsString(map);
    }
	
	@RequestMapping("/inputPhone/{userId}")
    public String inputPhone(@PathVariable Long userId,Map<String,Object> map) {
        User user = userServ.findById(userId);
        map.put("user", user);
        return INPUTPHONE;
    }
	
	@RequiresPermissions("Personnel:transfer")
	@RequestMapping(value = "/transferPermission/{userId}", method = { RequestMethod.POST })
	public @ResponseBody String transferPermission(@PathVariable Long userId, HttpServletRequest request) {
	    String code = request.getParameter("code");
	    String password = request.getParameter("password");
	    SimpleHash sh = new SimpleHash("md5", password);
	    String passwod=SecurityUtils.getShiroUser().getUser().getPassword();
        HttpSession session = request.getSession();
        String vsgineCode = (String) session.getAttribute("vsgineCode");
        if(!(sh.toString()).equals(passwod) && !(sh.toString()).equals(""))
        	return AjaxObject.newError("密码不正确！").setCallbackType("").toString();
        if(!code.equals(vsgineCode) && !code.equals(""))
            return AjaxObject.newError("验证码不正确！").setCallbackType("").toString();
		List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByUserIdAndMesCompanyRoleCompanyid(SecurityUtils.getShiroUser().getUser().getId(), SecurityUtils.getShiroUser().getCompanyid());
		for(UserCompanyrole userCompanyrole : userCompanyroles){
			userCompanyrole.setUserId(userId);
			userCompanyroleService.saveOrUpdate(userCompanyrole);
		}
		ucServ.deleteinfoByUserIdAndCompanyId(SecurityUtils.getShiroUser().getUser().getId(), SecurityUtils.getShiroUser().getCompanyid());
		Companyinfo companyinfo =  cpService.findById(SecurityUtils.getShiroUser().getCompanyid());
		companyinfo.setUserid(userId);
		cpService.saveOrUpdate(companyinfo);
		MesPermissionTransfer mesPermissionTransfer = new MesPermissionTransfer();
		mesPermissionTransfer.setUpdatetime(new Timestamp((new Date()).getTime()));
		mesPermissionTransfer.setUserByFromuserid(SecurityUtils.getShiroUser().getUser());
		mesPermissionTransfer.setUserByTouserid(userServ.findById(userId));
		mesPermissionTransfer.setCompanyinfo(companyinfo);
		mesPermissionTransferService.saveOrUpdate(mesPermissionTransfer);
		this.sendMail(mesPermissionTransfer);
		return AjaxObject.newOk("转移权限成功!").toString();
	}

    private void sendMail(MesPermissionTransfer mesPermissionTransfer){
         SimpleMailMessage mailMessage = new SimpleMailMessage(); 
         //邮件标题
         String subject = "转移权限";
         //邮件内容
         String text = "公司管理员权限已转移!转移前:"+mesPermissionTransfer.getUserByFromuserid().getUsername()+",转移后"+mesPermissionTransfer.getUserByTouserid().getUsername()+"";
         try {
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
         mailMessage.setSubject(subject);  
         mailMessage.setText(text); 
         Companyinfo companyinfo = cpService.findById(SecurityUtils.getShiroUser().getCompanyid());
         String email = companyinfo.getCompanyemail();
         if(email == null)
             return;
         mailMessage.setTo(email);  
         //发送邮件
         try {
             mailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("邮件发送异常!");
        }
    }
	
	@RequestMapping(value="/tree_list", method={RequestMethod.GET, RequestMethod.POST})
	public String tree_list(Map<String, Object> map) {
//		Department department = deparServ.findByDepartmentIdAndCompanyid(SecurityUtils.getShiroUser().getCompanyid());
//		map.put("id", department.getId());
		return TREE_LIST;
	}

	@RequestMapping(value="/tree", method=RequestMethod.GET)
	public String tree(Map<String, Object> map) {
		Department department = deparServ.getTree();

		map.put("department", department);
		return TREE;
	}


	@RequestMapping(value="/list", method={RequestMethod.GET, RequestMethod.POST})
	public String list(ServletRequest request, Page page, Map<String, Object> map) {
//		Specification<Department> specification = DynamicSpecifications.bySearchFilter(request, Department.class,
//				new SearchFilter("department.id", Operator.EQ, parentDepartmentId));
//		List<Department> departments = deparServ.findPage(specification, page);

		map.put("page", page);
//		map.put("departments", departments);
//		map.put("parentDepartmentId", parentDepartmentId);

		return LIST;
	}
}
