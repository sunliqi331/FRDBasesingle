package com.its.frd.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.its.common.entity.main.User;
import com.its.common.service.UserService;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Subsysteminfo;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.SubsysroleService;
import com.its.frd.service.SubsysteminfoService;
import com.its.frd.service.UserApiService;
import com.its.frd.util.ExcelFileGenerator;
import com.its.frd.util.ResourceUtil;

/**
 * 主要用作对外提供API
 *
 */
@Controller
@RequestMapping("/urlapi")
public class UrlController {
	private final String FAIL = "fail";
	private final String SECCUSS = "seccuss";
	
	@Resource
	private CompanyinfoService cpServ;
	
	@Resource
	private UserService userServ;
	@Resource
	private SubsysroleService ssServ;
	@Resource
	private SubsysteminfoService sfServ;
	@Resource
	private UserApiService userApiServ;
	
	/**
	 * 对商城提供用户写入服务
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/SU")
	@ResponseBody
	public boolean saveUserInfoForShop(HttpServletRequest request,@RequestBody User user){
		if(!this.checkRemoteIp(request))
			return false;
		if(user == null)
			return false;
		System.out.println(new Gson().toJson(user));
		return userApiServ.saveUser(user);
	}
	
	/**
	 * 更新用户信息
	 * 更新手机号或邮箱或密码
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/UU")
	@ResponseBody
	public boolean updateUserInfoForShop(HttpServletRequest request,@RequestBody User user){
		System.out.println(new Gson().toJson(user));
		if(!this.checkRemoteIp(request))
			return false;
		if(user == null)
			return false;
		return userApiServ.updateUser(user);
	}
	
	/**
	 * 删除用户
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/DU")
	@ResponseBody
	public boolean deleteUserInfoForShop(HttpServletRequest request,@RequestBody User user){
		if(!this.checkRemoteIp(request))
			return false;
		if(user == null)
			return false;
		return userApiServ.updateStatusByUser(user.getUsername(), "disabled");
	}
	/**
	 * 让用户失效
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/DU_D")
	@ResponseBody
	public boolean deleteUserInfoForShop3(HttpServletRequest request,@RequestBody User user){
		if(!this.checkRemoteIp(request))
			return false;
		if(user == null)
			return false;
		return userApiServ.updateStatusByUser(user.getUsername(),"disabled");
	}
	/**
	 * 让用户生效
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/DU_E")
	@ResponseBody
	public boolean deleteUserInfoForShop2(HttpServletRequest request,@RequestBody User user){
		if(!this.checkRemoteIp(request))
			return false;
		if(user == null)
			return false;
		return userApiServ.updateStatusByUser(user.getUsername(),"enabled");
	}
	
	/**
	 * 检查调用者的IP是否合法
	 * @param request
	 * @return
	 */
	private boolean checkRemoteIp(HttpServletRequest request){
		String remoteIp = request.getRemoteAddr();
		String cfIp = ResourceUtil.getValueForDefaultProperties("shop.ip");
		if(!remoteIp.equalsIgnoreCase(cfIp))
			return false;
		return true;
	}
	@RequestMapping("/excel")
	public void getUserList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//1：初始化数据
		//excel的标题数据（只有一条）
		List<String> fieldName = new ArrayList<>();
		fieldName.add("编号");
		fieldName.add("姓名");
		//excel的内容数据（多条）
		List<List<String>> fieldDatas = new ArrayList<>();
		List<String> fieldData = new ArrayList<>();
		fieldData.add("007");
		fieldData.add("wenli");
		fieldDatas.add(fieldData);
		//2：调用封装的POI报表的导出类ExcelFileGenerator完成excel报表的导出
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDatas);
		
		/**导出报表的文件名*/
		String filename = "生产记录.xls";
		//处理乱码
		filename = new String(filename.getBytes("gbk"),"iso-8859-1"); 
		/**response中进行设置，总结下载，导出，需要io流和头*/
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+filename);
		response.setBufferSize(1024);
		
		
		//获取输出流
		OutputStream os = response.getOutputStream();
		excelFileGenerator.expordExcel(os);//使用输出流，导出 
	}
	
	@RequestMapping("/cpinfos")
	@ResponseBody
	public List<Companyinfo> getCpInfos(){
		if(SecurityUtils.getShiroUser().getMenuMap().get("companys") != null)
			return (List<Companyinfo>)SecurityUtils.getShiroUser().getMenuMap().get("companys");
		return null;
	}
	
	@RequestMapping("/shopOk")
	@ResponseBody
	public Boolean isAbleShop(@RequestBody User user){
		return true;
	}
	
	/**
	 * 获取没有公司信息的用户数据
	 * key:用户真实姓名
	 * value: 用户名(登录)
	 * @return
	 */
	@RequestMapping("/getUserList")
	@ResponseBody
	public Map<String,String> getUserList(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		List<SearchFilter> sflist = new ArrayList<SearchFilter>();
		sflist.add(new SearchFilter("companyid", SearchFilter.Operator.EQ, null));
		Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class,sflist);
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		List<User> userlist = userServ.findByExample(specification, page);
		if(userlist != null)
			for(User user : userlist){
				map.put(user.getRealname(), user.getUsername());
			}
		return map;
	}
	
	/**
	 * subsystem加人员信息到FRD平台
	 * 针对MES系统
	 * @param username 被添加新用户名
	 * @param nowusername 当前添加用户名即调用者
	 * @param roleid
	 * @param subSysId
	 * @return
	 */
	@RequestMapping("/cuser")
	@ResponseBody
	public boolean addOupdateUser(String nowusername,String username,
			String password,Long roleid,Long subSysId,Long companyid){
		//需先判断调用者是否有效,否则不能添加
		/*if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)
				|| companyid == null)
			return false;
		User nowUser = userServ.getByUsername(nowusername);
		if(nowUser == null || nowUser.getPassword().equals(new SimpleHash("md5", password)))
			return false;
		//有companyid表示此用户是其申请了公司信息
		if(nowUser.getCompanyid() == null)
			return false;
		//需判断公司信息是否有效
		Companyinfo cpinfo = cpServ.findById(companyid);
		if(!cpinfo.getStatus().equals("1"))
			return false;
		
		User newUser = userServ.getByUsername(username);
		Subsysrole subRole = new Subsysrole();
		//账号存在
		if(newUser != null){
			//有公司信息
			if(newUser.getCompanyid() != null){
				return false;
			}else{
				newUser.setCompanyid(nowUser.getCompanyid());
				subRole.setRoleid(roleid);
				subRole.setUserid(newUser.getId());
				subRole.setSubsysid(subSysId);
			}
		}else{
			newUser = new User();
			newUser.setUsername(username);
			SimpleHash sh = new SimpleHash("md5", "123456");
			newUser.setPassword(sh.toString());
			//添加
			newUser.setCompanyid(nowUser.getCompanyid());
			subRole.setRoleid(roleid);
			subRole.setSubsysid(subSysId);
		}
		
		try{
			userServ.saveOrUpdate(newUser);
			subRole.setUserid(newUser.getId());
			ssServ.saveOrUpdateInfo(subRole);
		}catch(Exception e){
			return false;
		}*/
		return true;
	}
	
	/**
	 * 作用: 当进入subsystem时使用
	 * 逻辑: 1.需要判断当前用户是否有该subsys的权限信息;
	 * 	   2.如果有,则让其进入
	 * 
	 * @param subsysID 子系统id
	 * @return
	 */
	@RequestMapping("/checkOk")
	@ResponseBody
	public Map<String,Object> checkOk(Long subsysID){
		try{
			Subsysteminfo sf = sfServ.findOneById(subsysID);
			//对应subsystem的roleid
			String subsysRoleId = SecurityUtils.getRoleIdBySubSysName(sf.getSysname());
			if(StringUtils.isEmpty(subsysRoleId))
				return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 针对MES
	 * 在重定向时需要检查用户是否有进入的资格
	 * 1.companyinfo表中有user的公司数据
	 * 		且company的状态需要是"1"(已审核)		
	 * 2.user表中有companyid的信息
	 * 		且company的状态需要是"1"(已审核)
	 * @param url 重定向url
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public boolean toUrl(String url){
		/*Companyinfo info = cpServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
		Companyinfo info2 = cpServ.findById(SecurityUtils.getShiroUser().getUser().getCompanyid());
		if((info != null && info.getStatus() != null && info.getStatus().equals("1"))
				|| (info2 != null && info2.getStatus() != null && info2.getStatus().equals("1")))
			return true;*/
		return false;
	}
	
	/**
	 * 返回的List中,user必须是没有公司信息的
	 * 
	 * @param username 用于验证是否有权限来获取用户信息
	 * @param password
	 * @return
	 */
	@RequestMapping("/userList")
	@ResponseBody
	public List<User> findUserList(String username,String password){
		//用户信息无效
		if(!this.authUserInfo(username, password))
			return null;
		//userServ.findByExample(specification, page);
		return null;
	}
	
	private Boolean authUserInfo(String username,String password){
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
			return false;
		User user = userServ.getByUsername(username);
		if(user == null || !"enabled".equals(user.getStatus()))
			return false;
		String pswMD5 = this.getPasswordMD5(password);
		if(pswMD5 == null)
			return false;
		if(!pswMD5.equals(user.getPassword()))
			return false;
		return true;
	}
	
	private String getPasswordMD5(String password){
		if(StringUtils.isEmpty(password))
			return null;
		return new SimpleHash("md5",password,1).toString();
	}
	
	/**
	 * 用于处于外部发送的增加用户信息的请求
	 * 
	 * @param jsonStr json字符串,包含被添加的用户信息,添加人的用户名和密码
	 * 		格式: jsonstr = 被添加人用户名,被添加人用户密码,添加人用户名,添加人密码
	 * 		1.先验证添加人的信息是否有效
	 * 			如果有效则,继续下一步;
	 * 		2.需要验证添加人是否注册过公司信息,且公司资质是否有效;
	 * 			公司信息有效才能添加;
	 * @return
	 */
	@RequestMapping("/addUser")
	@ResponseBody
	public String addUserInfoFromSubSys(String jsonStr){
		Companyinfo cpinfo = null;
		try{
			if(!this.authInfo(jsonStr,cpinfo))
				return FAIL;
			User user = new User();
			String[] strs = this.strResolve(jsonStr);
			user.setUsername(strs[0]);
			SimpleHash sh = new SimpleHash("md5",strs[1],1);
			user.setPassword(sh.toString());
			userServ.saveOrUpdate(user);
		}catch(Exception e){
			return FAIL;
		}
		return SECCUSS;
	}
	
	/**
	 * 验证是否能添加新用户
	 * @param jsonStr
	 * @return
	 */
	private Boolean authInfo(String jsonStr,Companyinfo cpinfo){
		/*try{
			String[] strs = this.strResolve(jsonStr);
			if(strs == null)
				return false;
			User userInfo = userServ.getByUsername(strs[2]);
			if(userInfo == null)
				return false;
			SimpleHash sh = new SimpleHash("md5", strs[3], 1);
			if(!userInfo.getPassword().equals(sh.toString()))
				return false;
			cpinfo = cpServ.findByUserid(userInfo.getId());
			if(cpinfo == null || cpinfo.getStatus() == null || !cpinfo.getStatus().equals("1"))
				return false;
		}catch(Exception e){
			return false;
		}*/
		return true;
	}
	
	private String[] strResolve(String jsonStr){
		if(StringUtils.isEmpty(jsonStr))
			return null;
		String[] strs = jsonStr.split(",");
		for(String str : strs){
			if(StringUtils.isEmpty(str))
				return null;
		}
		return strs;
	}
	
}
















