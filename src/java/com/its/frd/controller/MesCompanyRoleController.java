package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.entity.main.Module;
import com.its.common.log.Log;
import com.its.common.service.ModuleService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesCompanyRole;
import com.its.frd.entity.MesCompanyRolePermission;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesCompanyRolePermissionService;
import com.its.frd.service.MesCompanyRoleService;
import com.its.frd.service.UserCompanyroleService;

@Controller
@RequestMapping("/companyRole")
public class MesCompanyRoleController {

	@Autowired
	private MesCompanyRoleService roleService;

	@Autowired
	private UserCompanyroleService userCompanyroleService;

	@Autowired
	private MesCompanyRolePermissionService rolePermissionService;

	@Autowired
	private CompanyinfoService companyinfoService;

	@Autowired
	private ModuleService moduleService;

	private static final String CREATE = "companyrole/create";
	private static final String UPDATE = "companyrole/update";
	private static final String LIST = "companyrole/list";
	private static final String VIEW = "companyrole/view";
	private static final String NOPERMISSION = "companyrole/noPermission";
	private static final String ASSIGN_DATA_CONTROL = "companyrole/assign_data_control";

	@InitBinder
	public void initListBinder(WebDataBinder binder) {
		// 设置需要包裹的元素个数，默认为256
		binder.setAutoGrowCollectionLimit(5000);
	}

    @RequestMapping("/showM")
    @ResponseBody
    public Module showModule(){
        return moduleService.getTree();
    }

	@RequiresPermissions("companyRole:save")
	@RequestMapping(value="/toCreate", method=RequestMethod.GET)
	public String precreate(Map<String, Object> map,HttpServletRequest request,Page page) {
	    if(SecurityUtils.getShiroUser().getCompanyid()==null){
	        map.put("Admin", "0");
	    }else {
	        List<MesCompanyRole> roles = new ArrayList<MesCompanyRole>();
	        List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByUserId(SecurityUtils.getShiroUser().getUser().getId());
	        for(UserCompanyrole userCompanyrole : userCompanyroles){
	            if(userCompanyrole.getMesCompanyRole().getCompanyid().equals(SecurityUtils.getShiroUser().getCompanyid())){
	                roles.add(userCompanyrole.getMesCompanyRole());
	            }
	        }
	        MesCompanyRole newrole = new MesCompanyRole();
	        if(roles != null && roles.size() >=1)
	            for(MesCompanyRole oldrole : roles){
	                newrole.getRolePermissions().addAll(oldrole.getRolePermissions());
	            }
	        map.put("role", newrole);
	        map.put("Admin", "1");
	    }
	    Specification<Companyinfo> specification = null;
	    //判断是注册用户还是系统添加用户
	    if(SecurityUtils.getShiroUser().getUser().getRegisterstate() == null){
	        specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
	                new SearchFilter("companytype",Operator.EQ,"company"),
	                new SearchFilter("companystatus",Operator.EQ,"1")
	                );
	    }else{
	        specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
	                new SearchFilter("companytype",Operator.EQ,"company"),
	                new SearchFilter("companystatus",Operator.EQ,"1"),
	                new SearchFilter("userid",Operator.EQ,SecurityUtils.getShiroUser().getUser().getId())
	                );
	    }
	    Page page2 = new Page();
	    page2.setNumPerPage(Integer.MAX_VALUE);
	    List<Companyinfo> company = companyinfoService.findPage(specification, page2);
	    if(SecurityUtils.getShiroUser().getCompanyid()==null){
	        map.put("haveCompany", false);
	    }else{
	        map.put("haveCompany", true);
	        map.put("companyid", SecurityUtils.getShiroUser().getCompanyid());
	    }
	    map.put("companys", company);
	    map.put("module", moduleService.getTree());
	    return CREATE;
	}
	
	@Log(message="添加了{0}角色。")
	@RequiresPermissions("companyRole:save")
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public @ResponseBody String create(@Valid MesCompanyRole role,HttpServletRequest request) {
		List<MesCompanyRolePermission> rolePermissions = new ArrayList<MesCompanyRolePermission>();
		for (MesCompanyRolePermission rolePermission : role.getRolePermissions()) {
			if (rolePermission.getPermission() != null && rolePermission.getPermission().getId() != null) {
				rolePermissions.add(rolePermission);
			}
		}
		for (MesCompanyRolePermission rolePermission : rolePermissions) {
			rolePermission.setMesCompanyRole(role);
		}
		if(SecurityUtils.getShiroUser().getCompanyid()==null){
			role.setChangeable("1");
		}else {
			role.setChangeable("0");
		}
		roleService.saveOrUpdate(role);
		return AjaxObject.newOk("添加角色成功！").toString();
	}

	@RequiresPermissions("companyRole:edit")
	@RequestMapping(value="/update/{id}", method=RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map,HttpServletRequest request,Page page) {
		List<MesCompanyRole> roles = new ArrayList<MesCompanyRole>();
		//获取当前登录者的所有公司角色
		List<UserCompanyrole> userCompanyroles = userCompanyroleService.findByUserId(SecurityUtils.getShiroUser().getUser().getId());
		for(UserCompanyrole userCompanyrole : userCompanyroles){
			//获得当前登陆者在当前公司的角色
			if(userCompanyrole.getMesCompanyRole().getCompanyid().equals(SecurityUtils.getShiroUser().getCompanyid())){
				roles.add(userCompanyrole.getMesCompanyRole());
			}
		}
		MesCompanyRole newrole = new MesCompanyRole();
		if(roles != null && roles.size() >=1)
			for(MesCompanyRole oldrole : roles){
				//将当前登录用户的所有权限赋给新创建的公司角色
				newrole.getRolePermissions().addAll(oldrole.getRolePermissions());
			}
		MesCompanyRole role = roleService.get(id);
		if(SecurityUtils.getShiroUser().getCompanyid()!=null){
			map.put("Admin", "1");
			if(role.getChangeable().equals("1")){
				return NOPERMISSION;
			}
		}else {
			map.put("Admin", "0");
		}
		if(SecurityUtils.getShiroUser().getCompanyid()==null){
			map.put("haveCompany", false);
		}else{
			map.put("haveCompany", true);
			map.put("companyid", SecurityUtils.getShiroUser().getCompanyid());
		}
		Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
				new SearchFilter("companytype",Operator.EQ,"company"),
				new SearchFilter("companystatus",Operator.EQ,"1"),
				new SearchFilter("userid",Operator.EQ,SecurityUtils.getShiroUser().getUser().getId()));
		List<Companyinfo> company = companyinfoService.findPage(specification, page);
		map.put("companys", company);
		map.put("module", moduleService.getTree());
		map.put("roles", newrole);
		map.put("mesCompanyRole", role);
		map.put("company", companyinfoService.findById(role.getCompanyid()));
		return UPDATE;
	}

	@Log(message="修改了{0}角色的信息。")
	@RequiresPermissions("companyRole:edit")
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public @ResponseBody String update(@Valid MesCompanyRole role, HttpServletRequest request, Page page) {
	    //获取修改前的公司角色
		MesCompanyRole oldRole = roleService.get(role.getId());
		//获取当前公司下的公司角色
		Specification<MesCompanyRole> specification = DynamicSpecifications.bySearchFilter(request, MesCompanyRole.class,
                new SearchFilter("companyid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
                );
		List<MesCompanyRole> mesCompanyRoles = roleService.findByExample(specification, page);
		//将修改后的公司角色的名称,公司,描述赋给原来的角色
		oldRole.setName(role.getName());
		oldRole.setCompanyid(role.getCompanyid());
		oldRole.setDescription(role.getDescription());
		//创建新的公司角色权限List
		List<MesCompanyRolePermission> newRList = new ArrayList<MesCompanyRolePermission>();
		//创建需要删除的的公司角色权限List
		List<MesCompanyRolePermission> delRList = new ArrayList<MesCompanyRolePermission>();
		//获取已有的权限List
		List<MesCompanyRolePermission> hasRolePermissions = rolePermissionService.findByCompanyRoleId(role.getId());
		//循环修改后的权限
		for (MesCompanyRolePermission rolePermission : role.getRolePermissions()) {
		    //如果权限是新增的
			if (rolePermission.getId() == null && rolePermission.getPermission() != null) {
			    //将新增的权限赋给修改前的公司角色
				rolePermission.setMesCompanyRole(oldRole);
				//将新增的权限存入新的公司角色权限List
				newRList.add(rolePermission);
				//如果是需要删除的权限
			} else if (rolePermission.getId() != null && rolePermission.getPermission() == null) {
			    //循环修改前的权限
				for (MesCompanyRolePermission rp : hasRolePermissions) {
					if (rp.getId().equals(rolePermission.getId())) {
						delRList.add(rp);
						break;
					}
				}
			}
		}
//		List<MesCompanyRolePermission> list = new ArrayList<MesCompanyRolePermission>();
//		list.addAll(delRList);
		for(MesCompanyRolePermission mesCompanyRolePermission : delRList){
		    for(MesCompanyRole mesCompanyRole : mesCompanyRoles){
		        List<MesCompanyRolePermission> hasRolePermissions2 = rolePermissionService.findByCompanyRoleId(mesCompanyRole.getId());
		        for (MesCompanyRolePermission rp : hasRolePermissions2) {
		            if (rp.getId().equals(mesCompanyRolePermission.getId())) {
		                rolePermissionService.delete(rp.getId());
		                break;
		            }
		        }
		    }
		}
		rolePermissionService.save(newRList);
		rolePermissionService.delete(delRList);
		roleService.saveOrUpdate(oldRole);
		return AjaxObject.newOk("修改角色成功！").toString();
	}

	@Log(message="删除了{0}角色。")
	@RequiresPermissions("companyRole:delete")
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody String delete(Long[] ids) {
		try {
			if(ids!=null && ids.length>0){
				for(int i = 0; i < ids.length; i++){
					MesCompanyRole role = roleService.get(ids[i]);
					roleService.delete(role.getId());
				}
			}
		} catch (Exception e) {
			return AjaxObject.newError("删除角色失败!").setCallbackType("").toString();
		}
    	return AjaxObject.newOk("删除角色成功!").setCallbackType("").toString();
	}

	@RequiresPermissions(value={"companyRole:view","companyRole:show"},logical=Logical.OR)
	@RequestMapping(value="/list", method={RequestMethod.GET, RequestMethod.POST})
	public String list(ServletRequest request, Page page, Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(SecurityUtils.getShiroUser().getUser().getRegisterstate() != null){
			if(companyId == null){
				return "error/403";
			}
		}
		Specification<MesCompanyRole> specification = DynamicSpecifications.bySearchFilter(request, MesCompanyRole.class
				);
		List<MesCompanyRole> roles = roleService.findByExample(specification, page);
		map.put("page", page);
		map.put("roles", roles);
		return LIST;
	}

	@RequiresPermissions(value={"companyRole:view","companyRole:show"},logical=Logical.OR)
	@RequestMapping(value="/data")
	@ResponseBody
	public Map<String, Object> dataList(ServletRequest request, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesCompanyRole> specification = null;
		if(SecurityUtils.getShiroUser().getCompanyid() != null || 
				SecurityUtils.getShiroUser().getUser().getRegisterstate() != null){
			specification = DynamicSpecifications.bySearchFilter(request, MesCompanyRole.class
					,new SearchFilter("companyid", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid())
					,new SearchFilter("changeable", Operator.NOTEQ, "1")
					);
		}else{
			specification = DynamicSpecifications.bySearchFilter(request, MesCompanyRole.class
					,new SearchFilter("changeable", Operator.EQ, "1")
					);
		}
		List<MesCompanyRole> roles = roleService.findByExample(specification, page);
		map.put("page", page);
		map.put("roles", roles);
		return map;
	}

	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
	public String view(@PathVariable Long id, Map<String, Object> map) {
		MesCompanyRole role = roleService.get(id);
		map.put("module", moduleService.getTree());
		map.put("role", role);
		return VIEW;
	}

	@RequestMapping(value="/assign/{id}", method=RequestMethod.GET)
	public String preAssign(@PathVariable Long id, Map<String, Object> map) {
		MesCompanyRole role = roleService.get(id);
		Map<Module, List<MesCompanyRolePermission>> mpMap = new LinkedHashMap<Module, List<MesCompanyRolePermission>>(); 
		for (MesCompanyRolePermission rp : role.getRolePermissions()) {
			Module module = rp.getPermission().getModule();
			List<MesCompanyRolePermission> rps = mpMap.get(module);
			if (rps == null) {
				rps = new ArrayList<MesCompanyRolePermission>();
				mpMap.put(module, rps);
			}
			rps.add(rp);
		}
		map.put("role", role);
		map.put("mpMap", mpMap);
		return ASSIGN_DATA_CONTROL;
	}

	@RequestMapping("/checkName/{name}/{companyid}")
	@ResponseBody
	public String checkName(@PathVariable String name,@PathVariable Long companyid,HttpServletRequest request) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		try {
			Specification<MesCompanyRole> specification = DynamicSpecifications.bySearchFilter(request, MesCompanyRole.class
					,new SearchFilter("name", Operator.EQ, name)
					,new SearchFilter("companyid", Operator.EQ, companyid)
					);
			List<MesCompanyRole> mcr = roleService.findByExample(specification, page);
			if(mcr.size() > 0){
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
