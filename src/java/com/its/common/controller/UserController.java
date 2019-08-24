package com.its.common.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.zookeeper.server.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.its.common.entity.main.Role;
import com.its.common.entity.main.User;
import com.its.common.entity.main.UserRole;
import com.its.common.exception.ExistedException;
import com.its.common.exception.ServiceException;
import com.its.common.log.Log;
import com.its.common.log.LogMessageObject;
import com.its.common.log.impl.LogUitls;
import com.its.common.service.RoleService;
import com.its.common.service.UserRoleService;
import com.its.common.service.UserService;
import com.its.common.shiro.ShiroUser;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Friendgroup;
import com.its.frd.entity.Friends;
import com.its.frd.entity.MesCompanyRole;
import com.its.frd.entity.UserApiModel;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.entity.Usercompanys;
import com.its.frd.entity.Userdepartment;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.FriendsService;
import com.its.frd.service.MesCompanyRoleService;
import com.its.frd.service.MesUserPositionService;
import com.its.frd.service.UserCompanyroleService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.service.UserdepartmentService;
import com.its.frd.util.APIHttpClient;
import com.its.frd.util.ResourceUtil;

@Controller
@RequestMapping("/management/security/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	UserCompanyroleService userCompanyroleService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private CompanyinfoService companyinfoService;

	@Autowired
	private MesCompanyRoleService mesCompanyRoleService;
	
	@Autowired
	private UserRoleService userrole;

	@Resource
	private MesUserPositionService mupServ;

	@Resource 
	private UsercompanysService ucServ;


	@Resource
	private UserdepartmentService udServ;

	@Resource
	private FriendsService friendsServ;
	
	@Value("${defaultPwd}")
	private String defaultPwd;

	private static final String CREATE = "management/security/user/create";
	private static final String UPDATE = "management/security/user/update";
	private static final String LIST = "management/security/user/list";
	private static final String LOOK_UP_ROLE = "management/security/user/assign_user_role";
	private static final String LOOK_UP_COMPANYROLE = "management/security/user/assign_user_companyRole";
	private static final String LOOK_USER_ROLE = "management/security/user/delete_user_role";
	private static final String LOOK_USER_COMPANYROLE = "management/security/user/delete_user_companyRole";
	private static final String UPDATE_USER_STATUS = "management/security/user/updateStatus";

	@RequiresPermissions("User:save")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String preCreate() {
		return CREATE;
	}

	@Log(message = "添加了{0}用户。")
	@RequiresPermissions("User:save")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody String create(@Valid User user) {
		user.setCreateTime(new Date());
		user.setRegisterstate("1");
		//测试商城添加成功，mes系统未成功的异常情况
//		user.setPassword(null);
		
		try {
			com.its.frd.util.MD5 md5 = new com.its.frd.util.MD5();
			String url = ResourceUtil.getValueForDefaultProperties("shop.api.save");
			APIHttpClient ac = new APIHttpClient(url);
			Gson gs = new Gson();
			UserApiModel newUser = new UserApiModel(user.getUsername(),user.getPassword(),user.getPhone(),user.getEmail());
			//判断商城是否写入成功
//			String result = ac.post(gs.toJson(newUser));
//			System.out.println(result);
			/*if("true".equals(result) && ac.getStatus() == 0){
				userService.saveOrUpdate(user);
			}else{
				return AjaxObject.newError("添加用户失败!").setCallbackType("").toString();
			}*/
			userService.saveOrUpdate(user);
		} catch (ExistedException e) {
			//删除商城对应user
			new Runnable(){
				@Override
				public void run() {
					boolean isOk = false;
					String url = ResourceUtil.getValueForDefaultProperties("shop.api.delete");
					APIHttpClient ac = new APIHttpClient(url);
					Gson gs = new Gson();
					UserApiModel newUser = new UserApiModel(user.getUsername(),null,null,null);
					String result = "";
					String userJson = gs.toJson(newUser);
					while(!isOk){
						//调用删除Http，如果返回true，则isOk=true
						result = ac.post(userJson);
						System.out.println(result);
						if("true".equals(result)&& ac.getStatus() == 0)
							isOk = true;
					}
				}
			}.run();
			return AjaxObject.newError("添加用户失败!").setCallbackType("").toString();
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[] { user.getUsername() }));
		return AjaxObject.newOk("添加用户成功！").toString();
	}

	@RequiresPermissions("User:edit:User拥有的资源")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		User user = userService.get(id);
		String a =user.getStatus();
		map.put("user", user);
		map.put("defaultPwd", com.its.frd.util.ResourceUtil.getDefaultPwd());
		return UPDATE;
	}
	/*@RequiresPermissions("User:edit:User拥有的资源")
	@RequestMapping(value ="/updatepassword/{id}",method = RequestMethod.GET)
	public String updatepassword(@PathVariable Long id){
		User user =userService.get(id);
		AjaxObject ajaxObject = new AjaxObject();
		ajaxObject.setCallbackType("");
			userService.resetPwd(user, "123456");
			ajaxObject.setMessage("重置密码成功，默认为123456！");	
			LogUitls.putArgs(
					LogMessageObject.newWrite().setObjects(new Object[] { user.getUsername(), ajaxObject.getMessage() }));
		return ajaxObject.toString();
		
		
	}*/
	@Log(message = "修改了{0}用户的信息。")
	@RequiresPermissions("User:edit:User拥有的资源")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody String update(@Valid User user) {		
		try{
			//修改商城user信息
			User oldUser = userService.get(user.getId());
			oldUser.setPhone(user.getPhone());
			oldUser.setEmail(user.getEmail());
			oldUser.setRealname(user.getRealname());
			//测试商城修改成功，mes系统未成功的异常情况
			if(StringUtils.isNotEmpty(user.getPassword())){
				oldUser.setPassword(user.getPassword());
			}
//			oldUser.setPassword(null);
		/*	
			String url = ResourceUtil.getValueForDefaultProperties("shop.api.update");
			APIHttpClient ac = new APIHttpClient(url);
			Gson gs = new Gson();
			UserApiModel newUser = new UserApiModel(user.getUsername(),oldUser.getPassword(),user.getPhone(),user.getEmail());
			String result = ac.post(gs.toJson(newUser));
			System.out.println(result);*/
			/*if("true".equals(result) && ac.getStatus() == 0){
				//商城user信息修改成功
				userService.saveOrUpdate(oldUser);
			}else{
				//商城user信息修改失败
				return AjaxObject.newError("修改用户信息失败！").setCallbackType("").toString();
			}*/
			userService.saveOrUpdate(oldUser);
		}catch(Exception e){
			//mes系统更新失败，需还原商城对应user信息
			new Runnable() {
				@Override
				public void run() {
					boolean isOk = false;
					User oldUser = userService.get(user.getId());
					String url = ResourceUtil.getValueForDefaultProperties("shop.api.update");
					APIHttpClient ac = new APIHttpClient(url);
					Gson gs = new Gson();
					UserApiModel newUser = new UserApiModel(oldUser.getUsername(),oldUser.getPassword(),oldUser.getPhone(),oldUser.getEmail());
					String result = "";
					String userJson = gs.toJson(newUser);
					while(!isOk){
						result = ac.post(userJson);
						System.out.println(result);
						if("true".equals(result)&& ac.getStatus() == 0)
							isOk = true;
					}
				}
			}.run();
			return AjaxObject.newError("修改用户信息失败！").setCallbackType("").toString();
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[] { user.getUsername() }));
		return AjaxObject.newOk("修改用户信息成功！").toString();
	}

	@RequestMapping("/preUpdateStatus/{id}")
	public String preUpdateStatus(@PathVariable Long id,Map<String, Object> map) {
		map.put("user", userService.get(id));
		return UPDATE_USER_STATUS;
	}
	//修改用户状态
	@RequestMapping("/updateStatus")
	@ResponseBody
	public String updateStatus(@Valid User user) {
		try{
			User oldUser = userService.get(user.getId());
			String url = null;
			if(user.getStatus().equals(User.STATUS_DISABLED)){
				if(oldUser.getStatus().equals(User.STATUS_ENABLED)){
					oldUser.setStatus(User.STATUS_DISABLED);
					url = ResourceUtil.getValueForDefaultProperties("shop.api.delete");
				}else if(oldUser.getStatus().equals(User.STATUS_DISABLED)){
					return AjaxObject.newOk("修改用户状态成功！").toString();
				}
			}else if(user.getStatus().equals(User.STATUS_ENABLED)){
				if(oldUser.getStatus().equals(User.STATUS_DISABLED)){
					oldUser.setStatus(User.STATUS_ENABLED);
					url = ResourceUtil.getValueForDefaultProperties("shop.api.open");
				}else if(oldUser.getStatus().equals(User.STATUS_ENABLED)){
					return AjaxObject.newOk("修改用户状态成功！").toString();
				}
			}else{
				return AjaxObject.newError("修改用户状态失败！").setCallbackType("").toString();
			}
			APIHttpClient ac = new APIHttpClient(url);
			Gson gs = new Gson();
			UserApiModel newUser = new UserApiModel(oldUser.getUsername(),null,null,null);
			String result = ac.post(gs.toJson(newUser));
			System.out.println(result);
			if("true".equals(result) && ac.getStatus() == 0){
				//商城对应user状态修改成功
				userService.saveOrUpdate(oldUser);
			}else{
				//商城对应user状态修改失败
				return AjaxObject.newError("修改用户状态失败！").setCallbackType("").toString();
			}
			userService.saveOrUpdate(oldUser);
		}catch(Exception e){
			//mes系统修改用户状态失败，需还原商城对应user状态
			new Runnable() {
				@Override
				public void run() {
					boolean isOk = false;
					String url = null;
					if(user.getStatus().equals(User.STATUS_DISABLED)){
						url = ResourceUtil.getValueForDefaultProperties("shop.api.open");
					}else if(user.getStatus().equals(User.STATUS_ENABLED)){
						url = ResourceUtil.getValueForDefaultProperties("shop.api.delete");
					}
					User oldUser = userService.get(user.getId());
					APIHttpClient ac = new APIHttpClient(url);
					Gson gs = new Gson();
					UserApiModel newUser = new UserApiModel(oldUser.getUsername(),null,null,null);
					String result = "";
					String userJson = gs.toJson(newUser);
					while(!isOk){
						result = ac.post(userJson);
						System.out.println(result);
						if("true".equals(result)&& ac.getStatus() == 0)
							isOk = true;
					}
				}
			}.run();
			return AjaxObject.newError("修改用户状态失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("修改用户状态成功！").toString();
	}

	@Log(message = "删除了{0}用户。")
	@RequiresPermissions("User:delete:User拥有的资源")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody String delete(@PathVariable Long id) {
		User user = null;
		/*
		 * 在删除用户的同时需要删除和用户相关的所有信息,包含公司数据,role.
		 */
		if(id==1){
			return AjaxObject.newError("admin用户不可删除!").setCallbackType("").toString();
		}
		try {
			user = userService.get(id);
			//删除商城对应user信息
			String url = ResourceUtil.getValueForDefaultProperties("shop.api.delete");
			APIHttpClient ac = new APIHttpClient(url);
			Gson gs = new Gson();
			UserApiModel newUser = new UserApiModel(user.getUsername(),null,null,null);
			String result = ac.post(gs.toJson(newUser));
			if("true".equals(result) && ac.getStatus() == 0){
				//删除商城对应user成功
				//删除user与公司角色关联
				List<UserCompanyrole> ucRole = userCompanyroleService.findByUserId(user.getId());
				if(ucRole.size() > 0)
					userCompanyroleService.deleteByUserId(user.getId());
				//将user创建的公司状态置为3
				List<Companyinfo> company = companyinfoService.findByUserid(user.getId());
				if(company.size() > 0){
					for(Companyinfo cp : company){
						cp.setCompanystatus("3");
						companyinfoService.saveOrUpdate(cp);
					}
				}
				//删除user与公司的关联
				List<Usercompanys> uc = ucServ.findByUserid(user.getId());
				if(uc.size() > 0){
					for(Usercompanys ucs : uc){
						ucServ.deleteinfoByUserIdAndCompanyId(user.getId(), ucs.getCompanyinfo().getId());
						mupServ.deleteinfoByUserIdAndCompanyId(user.getId(), ucs.getCompanyinfo().getId());
						List<Userdepartment> ud = udServ.findByUserid(user.getId());
						for(Userdepartment uds: ud){
							udServ.deleteById(uds.getId());
						}
					}
				}
				//删除user的好友关系
				List<Friends> friend = friendsServ.findMyFriends(user.getId());
				if(friend.size() > 0){
					for(Friends fr : friend){
						friendsServ.deleteFriends(fr);
					}
				}
				//删除user的group关系
				List<Friendgroup> group = friendsServ.findList(user.getId());
				if(group.size() > 0){
					for(Friendgroup groups : group){
						friendsServ.delGroup(groups);
					}
				}
				userService.delete(user.getId());
			}else{
				//删除商城对应user失败
				return AjaxObject.newError("删除用户失败!").setCallbackType("").toString();
			}
		} catch (ServiceException e) {
			User oldUser = userService.get(id);
			//mes系统删除失败，需还原user信息到商城
			new Runnable() {
				@Override
				public void run() {
					boolean isOk = false;
					while(!isOk){
						String url = ResourceUtil.getValueForDefaultProperties("shop.api.save");
						APIHttpClient ac = new APIHttpClient(url);
						Gson gs = new Gson();
						UserApiModel newUser = new UserApiModel(oldUser.getUsername(),null,null,null);
						//判断删除商城对应user是否删除
						String result = ac.post(gs.toJson(newUser));
						System.out.println(result);
						if("true".equals(result)&& ac.getStatus() == 0){
							isOk = true;
						}
					}
				}
			}.run();
			return AjaxObject.newError("删除用户失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除用户成功！").setCallbackType("").toString();
	}

	@Log(message = "删除了{0}用户。")
	@RequiresPermissions("User:delete:User拥有的资源")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteMany(Long[] ids) {
		try {
			for (int i = 0; i < ids.length; i++) {
				User user = userService.get(ids[i]);
				userService.delete(user.getId());
				List<Companyinfo> company = companyinfoService.findByUserid(ids[i]);
				for(Companyinfo cp : company){
					cp.setCompanystatus("3");
					companyinfoService.saveOrUpdate(cp);
				}
				List<Usercompanys> uc = ucServ.findByUserid(ids[i]);
				for(Usercompanys ucs : uc){
					ucServ.deleteinfoByUserIdAndCompanyId(ids[i], ucs.getCompanyinfo().getId());
					mupServ.deleteinfoByUserIdAndCompanyId(ids[i], ucs.getCompanyinfo().getId());
					List<Userdepartment> ud = udServ.findByUserid(ids[i]);
					for(Userdepartment uds: ud){
						udServ.deleteById(uds.getId());
					}
				}
				List<Friends> friend = friendsServ.findMyFriends(ids[i]);
				for(Friends fr : friend){
					friendsServ.deleteFriends(fr);
				}
				List<Friendgroup> group = friendsServ.findList(ids[i]);
				for(Friendgroup groups : group){
					friendsServ.delGroup(groups);
				}
			}
		} catch (ServiceException e) {
			return AjaxObject.newError("删除用户失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除用户成功！").setCallbackType("").toString();
	}

	@RequiresPermissions("User:view:User拥有的资源")
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(ServletRequest request, Page page, Map<String, Object> map) {
		return LIST;
	}

	@RequestMapping(value = "/data")
	@ResponseBody
	public String dataList(ServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 当非admin登陆时，不能查询到admin；
		 */
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		Specification<User> specification = null;
		if(shiroUser.getUser().getId() != 1l){
			specification = DynamicSpecifications.bySearchFilter(request, User.class,
					new SearchFilter("id", com.its.common.util.persistence.SearchFilter.Operator.NOTEQ, 1l)
					, new SearchFilter("id", com.its.common.util.persistence.SearchFilter.Operator.NOTEQ, shiroUser.getUser().getId()));
		}else{
			specification = DynamicSpecifications.bySearchFilter(request, User.class);
		}
		List<User> users = userService.findByExample(specification, page);
		for(User ue : users){
			List<Usercompanys> company = ucServ.findByUserid(ue.getId());
			StringBuilder stringBuilder = new StringBuilder();
			for(Usercompanys uc : company){
				String companyname = uc.getCompanyinfo().getCompanyname();
				if(stringBuilder.length() == 0){
					stringBuilder.append(companyname);
				}else
					stringBuilder.append(","+companyname);
			}
			ue.setCompanyinfo(stringBuilder.toString());
		}
		/*for(User uk :users){
			List<UserCompanyrole> usercompanyrole = userCompanyroleService.findByUserId(uk.getId());
			StringBuilder stringbuilder =new StringBuilder();
			for(UserCompanyrole ucr : usercompanyrole){
				String companyrole = ucr.getMesCompanyRole().getName();
				if(stringbuilder.length() == 0){
					stringbuilder.append(companyrole);
				}else
					stringbuilder.append(","+companyrole);
				}
			uk.setCompanyRole(stringbuilder.toString());
			}*/
		for(User uc :users){
			List<UserRole> userrolee = userrole.findByUserId(uc.getId());
			StringBuilder Stringbuilder =new StringBuilder();
			for(UserRole ur: userrolee){
				String user = ur.getRole().getName();
				if(Stringbuilder.length()==0){
					Stringbuilder.append(user);
				}else
					Stringbuilder.append(","+user);
			}
			uc.setUsersRole(Stringbuilder.toString());
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("users", users);
		return mapper.writeValueAsString(map);
	}

	@Log(message = "{0}用户{1}")
	@RequiresPermissions("User:reset:User拥有的资源")
	@RequestMapping(value = "/reset/{type}/{userId}", method = RequestMethod.POST)
	public @ResponseBody String reset(@PathVariable String type, @PathVariable Long userId) {
		User user = userService.get(userId);
		AjaxObject ajaxObject = new AjaxObject();
		ajaxObject.setCallbackType("");
		if (type.equals("password")) {
			user.setPassword("123456");
			ajaxObject.setMessage("重置密码成功，默认为123456！");
		userService.saveOrUpdate(user);
		} else if (type.equals("status")) {
			if (user.getStatus().equals("enabled")) {
				user.setStatus("disabled");
			} else {
				user.setStatus("enabled");
			}
			ajaxObject.setMessage("更新状态成功，当前为" + (user.getStatus().equals(User.STATUS_ENABLED) ? "可用" : "不可用"));
			userService.saveOrUpdate(user);
		}
		//userService.saveOrUpdate(user);
		LogUitls.putArgs(
				LogMessageObject.newWrite().setObjects(new Object[] { user.getUsername(), ajaxObject.getMessage() }));
		return ajaxObject.toString();
	}

	@Log(message = "向{0}用户分配了{1}的角色。")
	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/create/userRole", method = { RequestMethod.POST })
	public @ResponseBody void assignRole(UserRole userRole) {
		userRoleService.saveOrUpdate(userRole);
		User user = userService.get(userRole.getUser().getId());
		Role role = roleService.get(userRole.getRole().getId());
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[] { user.getUsername(), role.getName() }));
	}

	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/lookup2create/userRole/{userId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String listUnassignRole(ServletRequest request, Map<String, Object> map, @PathVariable Long userId) {
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		List<UserRole> userRoles = userRoleService.findByUserId(userId);
		/*
		 * 去掉超级管理员的角色
		 */
		Specification<Role> specification = DynamicSpecifications.bySearchFilter(request, Role.class,
				new SearchFilter("id", Operator.NOTEQ, 1L));
		List<Role> roles = roleService.findByExample(specification,page);
		//List<Role> roles = roleService.findAll(page);
		List<Role> rentList = new ArrayList<Role>();
		// 删除已分配roles
		for (Role role : roles) {
			boolean isHas = false;
			for (UserRole or : userRoles) {
				if (or.getRole().getId().equals(role.getId())) {
					isHas = true;
					break;
				}
			}
			if (isHas == false) {
				rentList.add(role);
			}
		}
		map.put("userRoles", userRoles);
		map.put("roles", rentList);
		map.put("userId", userId);
		return LOOK_UP_ROLE;
	}

	@Log(message = "向{0}用户分配了{1}的公司角色。")
	@RequestMapping(value = "/create/userCompanyRole", method = { RequestMethod.POST })
	public @ResponseBody void assignCompanyRole(UserCompanyrole userCompanyrole) {
		userCompanyroleService.saveOrUpdate(userCompanyrole);
		User user = userService.get(userCompanyrole.getUserId());
		MesCompanyRole mesCompanyRole = mesCompanyRoleService.get(userCompanyrole.getMesCompanyRole().getId());
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[] { user.getUsername(), mesCompanyRole.getName() }));
	}

	@RequestMapping("/getMesCompanyRoleByCompanyid/{userId}/{companyid}")
	public @ResponseBody String getMesCompanyRoleByCompanyid(Map<String, Object> map, @PathVariable Long companyid,
			@PathVariable Long userId, HttpServletRequest request, Page page) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			Long userid = companyinfoService.findById(companyid).getUserid();
			if (!userId.equals(userid)) {
				return mapper.writeValueAsString("NoPermission");
			}
			List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByUserId(userId);
			Specification<MesCompanyRole> specification = DynamicSpecifications.bySearchFilter(request,MesCompanyRole.class,
					new SearchFilter("companyid", Operator.EQ, companyid),
					new SearchFilter("changeable", Operator.EQ, "1")
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
			// map.put("mesCompanyRoles", rentList);
			return mapper.writeValueAsString(rentList);
		} catch (Exception e) {
			return AjaxObject.newError("分配公司角色权限失败！").setCallbackType("").toString();
		}
	}

	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/lookup2create/userCompanyRole/{userId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String listUnassignCompanyRole(Map<String, Object> map, @PathVariable Long userId,HttpServletRequest request) {
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByUserId(userId);
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
				new SearchFilter("companytype",Operator.EQ,"company"),
				new SearchFilter("companystatus",Operator.EQ,"1"),
				new SearchFilter("userid",Operator.EQ,userId)
				);
		List<Companyinfo> company = companyinfoService.findPage(specification, page);
		map.put("company", company);
		map.put("userCompanyroles", userCompanyroles);
		map.put("userId", userId);
		return LOOK_UP_COMPANYROLE;
	}

	@Log(message = "撤销了{0}用户的{1}角色。")
	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/delete/userRole/{userRoleId}", method = { RequestMethod.POST })
	public @ResponseBody void deleteUserRole(@PathVariable Long userRoleId) {
		UserRole userRole = userRoleService.get(userRoleId);
		LogUitls.putArgs(LogMessageObject.newWrite()
				.setObjects(new Object[] { userRole.getUser().getUsername(), userRole.getRole().getName() }));
		userRoleService.delete(userRoleId);
	}

	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/lookup2delete/userRole/{userId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String listUserRole(Map<String, Object> map, @PathVariable Long userId){
		List<UserRole> userRoles = userRoleService.findByUserId(userId);
		map.put("userRoles", userRoles);
		return LOOK_USER_ROLE;

	}

	@RequiresPermissions("User:assign")
	@RequestMapping(value = "/lookup2delete/userCompanyRole/{userId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String listUserCompanyRole(Map<String, Object> map, @PathVariable Long userId) {
		List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByUserId(userId);
		map.put("userCompanyroles", userCompanyroles);
		return LOOK_USER_COMPANYROLE;
	}

	@RequiresPermissions("User:assign")
	@Log(message = "撤销了{0}用户的{1}角色。")
	@RequestMapping(value = "/delete/userCompanyRole/{userCompanyRoleId}", method = { RequestMethod.POST })
	public @ResponseBody void deleteUserCompanyRole(@PathVariable Long userCompanyRoleId) {
		UserCompanyrole userCompanyRole = userCompanyroleService.get(userCompanyRoleId);
		LogUitls.putArgs(LogMessageObject.newWrite()
				.setObjects(new Object[] { userCompanyRole.getUserId(), userCompanyRole.getMesCompanyRole().getId() }));
		userCompanyroleService.delete(userCompanyRoleId);
	}

}
