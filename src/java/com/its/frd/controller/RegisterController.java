package com.its.frd.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.its.common.entity.main.Role;
import com.its.common.entity.main.User;
import com.its.common.entity.main.UserRole;
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
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.UserApiModel;
import com.its.frd.params.CompanyfileType;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.SubsysroleService;
import com.its.frd.service.SubsysteminfoService;
import com.its.frd.util.APIHttpClient;
import com.its.frd.util.DateUtils;
import com.its.frd.util.ResourceUtil;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Resource
	UserService userService;

	@Resource
	RoleService roleService;

	@Resource
	UserRoleService userRoleService;

	@Resource
	SubsysroleService subsysroleService;

	@Resource
	SubsysteminfoService subsysteminfoService;
	@Resource
	private CompanyfileService cfServ;

	@RequestMapping("/infoFillPage")
	public String infoFillPage(){
		return "register/infoFillPage";
	}

	@RequestMapping("/personalinfo")
	public String personInfo(Map<String,Object> map,HttpServletRequest request) {
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		/*if(shiroUser.getUser() == null)
			return "redirect:/management/index";*/
		User user = userService.get(shiroUser.getUser().getId());
		//		User user = userService.findById(shiroUser.getUser().getId());
		List<UserRole> userRoles = userRoleService.findByUserId(user.getId());
		StringBuilder rolename = new StringBuilder();
		for(UserRole userRole : userRoles){
			if(userRole != null && userRole.getRole() != null)
				rolename.append(roleService.get(userRole.getRole().getId()).getName()+".");
		}
		String pic = new String();
		List<Companyfile> cf= cfServ.findByParentidAndType(user.getId(),CompanyfileType.HEADPICTYPEFILE.toString());
		for(Companyfile cfs : cf) {
			pic = request.getServletContext().getContextPath()+"/company/showPic/"+cfs.getId();
		}
		boolean bool = false;
		if(cf.size() > 0)
			bool = true;
		StringBuilder sb = new StringBuilder();
		sb.append(user.getPhone());
		sb.replace(3, 7, "****");
		user.setPhone(sb.toString());
		map.put("phone", sb.toString());
		map.put("rolename", rolename);
		map.put("user", user);
		map.put("bool", bool);
		map.put("pic", pic);
		return "register/personalinfo";
	}

	@RequestMapping("/updateInfo")
	public String updateInfo(Map<String,Object> map) {
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		User user = shiroUser.getUser();
		int i = 0;
		if("男".equals(user.getSex()))
			i = 1;
		if("女".equals(user.getSex()))
			i = 2;
		map.put("user", user);
		map.put("i",i);
		return "register/updateInfo";
	}

	@RequestMapping("/updatePassword")
	public String updatePassword(Map<String,Object> map) {
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		User user = shiroUser.getUser();
		map.put("user", user);
		return "register/updatePassword";
	}

	@RequestMapping("/updatePhone")
	public String updatePhone(Map<String,Object> map) {
		User user = SecurityUtils.getShiroUser().getUser();
		map.put("user", user);
		return "register/updatePhone";
	}

	@RequestMapping("/updatePic")
	public String updatePic(Map<String,Object> map) {
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		User user = shiroUser.getUser();
		map.put("user", user);
		List<Companyfile> fileList = cfServ.findByParentidAndType(user.getId(), CompanyfileType.HEADPICTYPEFILE.toString());
		boolean bool = false;
		if(fileList.size() > 0){
			bool = true;
		}
		map.put("bool", bool);
		map.put("pic", fileList);
		return "register/updatePic";
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdate(@Valid User user) {
	//try {
			User oldUser = userService.get(user.getId());
			oldUser.setEmail(user.getEmail());
			oldUser.setSex(user.getSex());
			oldUser.setRealname(user.getRealname());
			oldUser.setProvince(user.getProvince());
			oldUser.setCity(user.getCity());
			userService.saveOrUpdate2(oldUser);
			SecurityUtils.getShiroUser().setUser(oldUser);
			//测试商城修改成功，mes系统未成功的情况
//			oldUser.setPassword(null);
			
			/*String url = ResourceUtil.getValueForDefaultProperties("shop.api.update");
			System.out.println(url);
			APIHttpClient ac = new APIHttpClient(url);
			Gson gs = new Gson();
			UserApiModel newUser = new UserApiModel(oldUser.getUsername(),oldUser.getPassword(),oldUser.getPhone(),oldUser.getEmail());
			String result = ac.post(gs.toJson(newUser));
			System.out.println(result);
			if("true".equals(result)&& ac.getStatus() == 0){
				//商城user信息修改成功
				userService.saveOrUpdate(oldUser);
				SecurityUtils.getShiroUser().setUser(oldUser);
			}else{
				//商城user信息修改失败
				return AjaxObject.newError("修改个人信息失败！").setCallbackType("").toString();
			}
			userService.saveOrUpdate(oldUser);
			SecurityUtils.getShiroUser().setUser(oldUser);
		} catch (Exception e) {
			new Runnable(){
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
			return AjaxObject.newError("修改个人信息失败！").setCallbackType("").toString();*/
		//}
		return AjaxObject.newOk("修改个人信息成功！").toString();
	}

/*	@RequestMapping("/users")
	@ResponseBody
	public Boolean saveUsers(){
		List<User> users = new ArrayList<>();
		users.add(new User("zjk2", ""));
		users.add(new User("zjk3", ""));
		try {
			userService.saveMoreUser(users,""+Calendar.getInstance().get(Calendar.MILLISECOND));
			
		} catch (Exception e) {
			User user2 = userService.findById(207l);
			return false;
		}
		return true;
	}*/
	
	@RequestMapping(value="/savePhone")
	@ResponseBody
	public String savePhone(@Valid User user,HttpServletRequest request) {
		User olduser = userService.get(user.getId());
		String code = request.getParameter("code");
		HttpSession session = request.getSession();
		String vsgineCode = (String) session.getAttribute("vsgineCode");
		if(!code.equals(vsgineCode)){
			return AjaxObject.newError("验证码不正确！").setCallbackType("").toString();
		}
		olduser.setPhone(user.getPhone());
		userService.saveOrUpdate2(olduser);
		//SecurityUtils.getShiroUser().setUser(olduser);
			/*try{
			User oldUser = userService.get(user.getId());
			oldUser.setPhone(user.getPhone());
			//测试商城修改成功，mes系统未成功的异常情况
//			oldUser.setPassword(null);
			
			//修改商城对应user信息
			String url = ResourceUtil.getValueForDefaultProperties("shop.api.update");
			APIHttpClient ac = new APIHttpClient(url);
			Gson gs = new Gson();
			UserApiModel newUser = new UserApiModel(oldUser.getUsername(),oldUser.getPassword(),oldUser.getPhone(),oldUser.getEmail());
			String result = ac.post(gs.toJson(newUser));
			System.out.println(result);
			if("true".equals(result)&& ac.getStatus() == 0){
				//商城user信息修改成功
				userService.saveOrUpdate(oldUser);
				SecurityUtils.getShiroUser().setUser(oldUser);
			}else{
				//商城user信息修改失败
				return AjaxObject.newError("修改手机号失败！").setCallbackType("").toString();
			}
			userService.saveOrUpdate(oldUser);
			SecurityUtils.getShiroUser().setUser(oldUser);
		}catch(Exception e){
			//mes系统user信息更新失败，需将商城对应user信息还原到未修改之前
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
			return AjaxObject.newError("修改手机号失败！").setCallbackType("").toString();
		}*/
		return AjaxObject.newOk("修改手机号成功!").toString();
	}

	@RequestMapping(value = "/savePassword")
	public @ResponseBody String updatePassword(@Valid User user,HttpServletRequest request) {
		//try {
			com.its.frd.util.MD5 md5 = new com.its.frd.util.MD5();
			User oldUser = userService.get(user.getId());
			String password = request.getParameter("oldPassword");
			if(!oldUser.getPassword().equals(md5.getMD5ofStr(password)))
				return AjaxObject.newError("原密码不正确！").setCallbackType("").toString();
			oldUser.setPassword(user.getPassword());
			userService.saveOrUpdate(oldUser);
			//测试商城修改成功，mes系统未成功的异常情况
//			oldUser.setPassword(null);
			
			//修改商城对应user的密码
			/*String url = ResourceUtil.getValueForDefaultProperties("shop.api.update");
			APIHttpClient ac = new APIHttpClient(url);
			Gson gs = new Gson();
			UserApiModel newUser = new UserApiModel(oldUser.getUsername(),oldUser.getPassword(),oldUser.getPhone(),oldUser.getEmail());
			String result = ac.post(gs.toJson(newUser));
			System.out.println(result);*/
			/*if("true".equals(result)&& ac.getStatus() == 0){
				//商城对应user信息修改成功
				
				SecurityUtils.getShiroUser().setUser(oldUser);
			}else{
				//商城对应user信息修改失败
				return AjaxObject.newError("修改密码失败！").setCallbackType("").toString();
			}
			userService.saveOrUpdate(oldUser);
			SecurityUtils.getShiroUser().setUser(oldUser);
		} catch (Exception e) {
			//mes系统user信息更新失败，需将商城对应user信息还原到未修改之前
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
			return AjaxObject.newError("修改密码失败！").setCallbackType("").toString();
		}*/
		return AjaxObject.newOk("修改密码成功！").toString();
	}

	@RequestMapping(value="/savePic")
	@ResponseBody
	public String savePic(@RequestParam("files") MultipartFile[] files,	@Valid User user,HttpServletRequest request) {
		try{
			List<Companyfile> companyfilelist = null;
			if (files != null && files.length > 0) {
				companyfilelist = new ArrayList<Companyfile>();
				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					if (!file.isEmpty()) {
						try {
							fileSave(companyfilelist, file);
						} catch (Exception e) {
							e.printStackTrace();
							return AjaxObject.newError("上传失败:请检查上传文件是否正确!").setCallbackType("").toString();
						}
					}
				}
			}
			if(companyfilelist != null && companyfilelist.size() > 0)
				cfServ.deleteCompanyfileByParentidAndType(user.getId(),CompanyfileType.HEADPICTYPEFILE.toString());
			for(Companyfile cpfile : companyfilelist){
				cpfile.setParentid(user.getId());
				cpfile.setParenttype(CompanyfileType.HEADPICTYPEFILE.toString());
				cfServ.saveOrUpdate(cpfile);;
			}

		}catch(Exception e){
			return AjaxObject.newError("上传头像失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("上传头像成功!").toString();
	}

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
	@RequestMapping(value="/register")
	@ResponseBody
	//public String register(String username,String password,String phone,String email,String realname,String sex,String province,String city,HttpServletRequest request,Page page) throws JsonProcessingException {
	public String register(User user,HttpServletRequest request,Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Role role = new Role();
		UserRole userRole = new UserRole();
		user.setCreateTime(new Date());
		user.setRegisterstate("1");
		//测试商城添加成功，mes系统未成功的异常情况
//		user.setPassword(null);
		
		
			//将注册用户的数据写到商城的user库中
			//com.its.frd.util.MD5 md5 = new com.its.frd.util.MD5();
			//String url = ResourceUtil.getValueForDefaultProperties("shop.api.save");
			//System.out.println(url);
			//APIHttpClient ac = new APIHttpClient(url);
			//Gson gs = new Gson();
			//UserApiModel newUser = new UserApiModel(user.getUsername(),user.getPassword(),user.getPhone(),user.getEmail());
		/*	String result = ac.post(gs.toJson(newUser));
			System.out.println(result);
*/			//判断商城是否写入成功
		/*	if("true".equals(result)&& ac.getStatus() == 0){
*/				//保存user
				userService.saveOrUpdate(user);
				// 将平台用户角色添加给用户
				role = (roleService.get((long) 12));
				userRole.setUser(user);
				userRole.setRole(role);
				userRoleService.saveOrUpdate(userRole);
				
			/*}else{
				//商城写入user失败
				map.put("false", false);
				return mapper.writeValueAsString(map);
			}
			role = (roleService.get((long) 12));
			userRole.setUser(user);
			userRole.setRole(role);
			userService.saveOrUpdate(user);
			userRoleService.saveOrUpdate(userRole);
		} catch (Exception e) {
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
			map.put("false", false);
			return mapper.writeValueAsString(map);
		}*/
		map.put("true", true);
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/success")
	public String success(){
		return "register/result_success";
	}

	@RequestMapping("/error")
	public String error(){
		return "register/result_failure";
	}

	@RequestMapping("/sendMessage/{phone}")
	@ResponseBody
	public String sendMessage(@PathVariable String phone,HttpServletRequest request) {
		try {
			int randomInt = new Random().nextInt(899999) + 100000;
			String vsgineCode = String.valueOf(randomInt);
			HttpSession session = request.getSession();
			session.setAttribute("vsgineCode",vsgineCode);
			com.its.common.utils.SMSUtil.SendRegSMS(phone, vsgineCode);
		}catch(Exception e) {
			return AjaxObject.newError("发送失败！请重新发送！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("发送成功！").toString();
	}

	@RequestMapping("/checkCode/{code}")
	@ResponseBody
	public String checkCode(@PathVariable String code,HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			HttpSession session = request.getSession();
			String vsgineCode = (String) session.getAttribute("vsgineCode");
			if(code.equals(vsgineCode)){
				map.put("1", 1);
			}else {
				map.put("0", 0);
			}
		} catch (Exception e) {
			return null;
		}
		return mapper.writeValueAsString(map);
	}
	
	@RequestMapping("/checkPaasword/{password}")
	@ResponseBody
	public String checkPaasowrd(@PathVariable String password,HttpServletRequest request) throws JsonProcessingException {
	    Map<String, Object> map = new HashMap<String, Object>();
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	    com.its.frd.util.MD5 md5 = new com.its.frd.util.MD5();
	    try {
	        if(!SecurityUtils.getShiroUser().getUser().getPassword().equals(md5.getMD5ofStr(password))){
	            map.put("0", 0);
	        }else {
	            map.put("1", 1);
	        }
	    } catch (Exception e) {
	        return null;
	    }
	    return mapper.writeValueAsString(map);
	}

	@RequestMapping("/checkUsername/{username}")
	@ResponseBody
	public String checkUsername(@PathVariable String username) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			if(userService.getByUsername(username) == null){	
				map.put("1", 1);
			}else {
				map.put("0", 0);
			}
		} catch (Exception e) {

			return null;
		}
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/checkPhone/{phone}")
	@ResponseBody
	public String checkPhone(@PathVariable String phone,HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class
				,new SearchFilter("phone", Operator.EQ, phone)
				);
		List<User> user = userService.findByExample(specification, page);
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

	@RequestMapping("/checkEmail")
	@ResponseBody
	public String checkEmail(String email,HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class
				,new SearchFilter("email", Operator.EQ, email)
				);
		List<User> user = userService.findByExample(specification, page);
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
