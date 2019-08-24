package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.its.common.entity.main.User;
import com.its.common.exception.ExistedException;
import com.its.common.service.UserService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Department;
import com.its.frd.entity.MesCompanyRole;
import com.its.frd.entity.Usercompanys;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.DepartmentService;
import com.its.frd.service.MesUserPositionService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.service.UserdepartmentService;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	private final String PAGEPRE = "department/";

	@Resource
	private DepartmentService dtServ;

	@Resource
	private CompanyinfoService cpService;

	@Resource
	private UsercompanysService ucService;

	@Resource
	private UserdepartmentService userdepartmentService;

	@Resource
	private UserService userService;

	@Resource
	private MesUserPositionService mesUserPositionService;

	@RequiresPermissions("Department:view")
	@RequestMapping(value="/tree_list", method={RequestMethod.GET, RequestMethod.POST})
	public String tree_list(Map<String, Object> map) {
//		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
//		if(companyId == null)
//			return "error/403";
//		Department department = dtServ.findByDepartmentIdAndCompanyid(companyId);
//		map.put("id", department.getId());
		return "department/tree_list";
	}

	@RequiresPermissions("Department:view")
	@RequestMapping(value="/tree", method=RequestMethod.GET)
	public String tree(Map<String, Object> map) {
		Department department = dtServ.getTree();

		map.put("department", department);
		return "department/tree";
	}

	@RequiresPermissions("Department:view")
	@RequestMapping(value="/list", method={RequestMethod.GET, RequestMethod.POST})
	public String list(ServletRequest request, Page page, Map<String, Object> map) {
//		Specification<Department> specification = DynamicSpecifications.bySearchFilter(request, Department.class,
//				new SearchFilter("department.id", Operator.EQ, parentDepartmentId));
//		List<Department> departments = dtServ.findPage(specification, page);

		map.put("page", page);
//		map.put("departments", departments);
//		map.put("parentDepartmentId", parentDepartmentId);
		return "department/list";
	}

	@RequiresPermissions("Department:view")
	@RequestMapping(value="/data")
	@ResponseBody
	public String dataList(ServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<Department> specification = DynamicSpecifications.bySearchFilter(request, Department.class,
				new SearchFilter("companyid", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()));
		List<Department> departments = dtServ.findPage(specification, page);
		
		for (Department department : departments) {
			if (department.getDepartment()!=null&&department.getDepartment().getId()!=null) {
				department.setParentInfo("[部门]"+dtServ.findById(department.getDepartment().getId()).getName());
			}else if (department.getFactoryid()!=null) {
				department.setParentInfo("[工厂]"+cpService.findById(department.getFactoryid()).getCompanyname());
			}else {
				department.setParentInfo("[公司]"+cpService.findById(SecurityUtils.getShiroUser().getCompanyid()).getCompanyname());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("departments", departments);
		//map.put("parentDepartmentId", parentDepartmentId);
		return mapper.writeValueAsString(map);
	}

	/**
	 * 通过id查询部门信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("Department:edit")
	@RequestMapping("/findDepartmentByid/{id}")
	public String showDtInfo(@PathVariable Long id, Model model,Page page,HttpServletRequest request,Map<String,Object> map) {
		Department dtinfo = dtServ.findById(id);
		List<Usercompanys> usercompanys = ucService.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		List<Long> ids = new ArrayList<Long>();
		List<User> users = new ArrayList<>();
		Specification<User> specification = null;
		if(usercompanys.size() > 0){
			for(Usercompanys uc : usercompanys){
				//将找出的userid存入数组ids里
				ids.add(uc.getUserid());
			}
			//找出所有userid在ids里的user
			if(ids.size() > 0){
			    specification = DynamicSpecifications.bySearchFilter(request, User.class
			            ,new SearchFilter("id", SearchFilter.Operator.IN, ids.toArray())
			            ,new SearchFilter("username", SearchFilter.Operator.NOTEQ,dtinfo.getPrincipal())
			            ,new SearchFilter("id", SearchFilter.Operator.NOTEQ,cpService.findById(SecurityUtils.getShiroUser().getCompanyid()).getUserid())
			            );
			    users = userService.findByExample(specification, page);
			}
		}
		
		List<Department> departments = dtServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		//去除当前部门
		departments.remove(dtServ.findById(id));
		List<Companyinfo> currentFactorys = cpService.findByParentidAndCompanytype(SecurityUtils.getShiroUser().getCompanyid(), "factory");
		List<Companyinfo> factorys = new ArrayList<>();
		for (Companyinfo factory : currentFactorys) {
			factorys.add(factory);
			factorys.addAll(findChildFactory(factory));
		}
		
		String companyname = cpService.findById(dtinfo.getCompanyid()).getCompanyname();
		model.addAttribute("department", dtinfo);
		map.put("page", page);
		map.put("users", users);
		map.put("departments", departments);//所属部门可选列表
		map.put("factorys", factorys);//所属工厂可选列表
		map.put("companyname", companyname);
		return PAGEPRE + "update";
	}

	//添加
	@RequiresPermissions("Department:save")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(HttpServletRequest request,Page page,Map<String, Object> map) {
		List<Usercompanys> usercompanys = ucService.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		List<Long> ids = new ArrayList<Long>();
		List<User> users = new ArrayList<>();
		Specification<User> specification = null;
		if(usercompanys.size() > 0){
			for(Usercompanys uc : usercompanys){
				//将找出的userid存入数组ids里
				ids.add(uc.getUserid());
			}
			if(ids.size() > 0){
			    //找出所有userid在ids里的user
			    specification = DynamicSpecifications.bySearchFilter(request, User.class,
			            new SearchFilter("id", SearchFilter.Operator.IN, ids.toArray())
//					,new SearchFilter("id", SearchFilter.Operator.NOTEQ,SecurityUtils.getShiroUser().getUser().getId())
//					,new SearchFilter("id", SearchFilter.Operator.NOTEQ,cpService.findById(SecurityUtils.getShiroUser().getCompanyid()).getUserid())
			            );
			    users = userService.findByExample(specification, page);
			}
		}
		List<Department> departments = dtServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		List<Companyinfo> currentFactorys = cpService.findByParentidAndCompanytype(SecurityUtils.getShiroUser().getCompanyid(), "factory");
		List<Companyinfo> factorys = new ArrayList<>();
		for (Companyinfo factory : currentFactorys) {
			factorys.add(factory);
			factorys.addAll(findChildFactory(factory));
		}		
		Companyinfo currentCompany = cpService.findById(SecurityUtils.getShiroUser().getCompanyid());
		
		map.put("page", page);
		map.put("users", users);//部门负责人可选列表（来源是当前公司的所有人）
		map.put("departments", departments);//所属部门可选列表
		map.put("factorys", factorys);//所属工厂可选列表
		map.put("currentCompany", currentCompany);
		map.put("companyId", SecurityUtils.getShiroUser().getCompanyid());
//		map.put("parentDepartmentId", parentDepartmentId);
		return PAGEPRE + "create";
	}

	private List<Companyinfo> findChildFactory(Companyinfo parentFactory) {
		List<Companyinfo> result = new ArrayList<>();
		List<Companyinfo> factorys = cpService.findByParentidAndCompanytype(parentFactory.getId(), "factory");
		if (!factorys.isEmpty()) {
			for (Companyinfo factory : factorys) {
				result.add(factory);
				result.addAll(findChildFactory(factory));
			}
		}
		return result;
	}

	@RequiresPermissions(value={"Department:save","Department:edit"},logical=Logical.OR)
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody String saveOrUpdate(@Valid Department department, HttpServletRequest request) {
		String msg = "修改";
		if (department.getId() == null)
			msg = "添加";
		try {
			if ("p_d".equals(request.getParameter("parentType"))) {
				//部门创建子部门不能超过5级
				Long parentid = Long.parseLong(request.getParameter("parentDepartment"));
				if(parentid != null){
					Department depart2 = dtServ.findById(parentid).getDepartment();
					if(depart2 != null){
						Department depart3 = dtServ.findById(depart2.getId()).getDepartment();
						if(depart3 != null){
							Department depart4 = dtServ.findById(depart3.getId()).getDepartment();
							if(depart4 != null){
								Department depart5 = dtServ.findById(depart4.getId()).getDepartment();
								if(depart5 != null)
									return AjaxObject.newError("无法添加子部门！").setCallbackType("").toString();
							}
						}
					}
				}
			}

/*			List<Department> list = dtServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
			if(list.size() > 0){
			for(Department depart : list){
				if(department.getName().equals(depart.getName())){
					if(department.getId() == null || department.getId().compareTo(depart.getId()) != 0)
						return AjaxObject.newError("该部门名称已存在！").setCallbackType("").toString();
				}
				if(department.getSn().equals(depart.getSn())){
					if(department.getId() == null || department.getId().compareTo(depart.getId()) != 0)
						return AjaxObject.newError("该部门编号已存在！").setCallbackType("").toString();
				}
			}
			}*/
			if(department.getCompanyid() == null)
				department.setCompanyid(SecurityUtils.getShiroUser().getCompanyid());
			if ("p_d".equals(request.getParameter("parentType"))) {
				Department parentDept = dtServ.findById(Long.parseLong(request.getParameter("parentDepartment")));
				department.setDepartment(parentDept);
				department.setFactoryid(null);
			}else if("p_f".equals(request.getParameter("parentType"))){
				if (!request.getParameter("parentFactory").equals("0")) {
					department.setFactoryid(Long.parseLong(request.getParameter("parentFactory")));
				}else {
					department.setFactoryid(null);
				}
				department.setDepartment(null);
			}
			
			dtServ.saveOrUpdate(department);
		} catch (ExistedException e) {
			return AjaxObject.newError(msg + "部门失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "部门成功！").toString();
	}

	@RequiresPermissions("Department:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public String deleteById(Long[] ids) {
		try {
			if(ids!=null && ids.length>0){
				boolean key = false;
				//检查是否还有子部门
				for(int i = 0; i < ids.length; i++){
					List<Department> currentDept = new ArrayList<>();
					currentDept.add(dtServ.findById(ids[i]));
					if(!findChildDept(currentDept).isEmpty()){
						key = true;
					}
				}
				if(key){
					return AjaxObject.newError("所选部门中含有子部门，删除失败！").setCallbackType("").toString();
				}
				for(int i = 0; i < ids.length; i++){
					dtServ.deleteById(ids[i]);
				}
			}
		} catch (Exception e) {
			return AjaxObject.newError("错误，删除部门失败！").setCallbackType("").toString();
		}
    	return AjaxObject.newOk("删除成功！").setCallbackType("").toString();
	}
	
	private List<Department> findChildDept(List<Department> departments) {
		List <Department> result = new ArrayList<>();
		for (Department department : departments) {
			result.addAll(dtServ.findByParentid(department.getId()));
		}
		if (!result.isEmpty()) {//传来的部门有子部门则继续递归
			result.addAll(findChildDept(result));
		}
		return result;
	}

	    @RequestMapping("/checkSn/{sn}")
    @ResponseBody
    public String checkSn(@PathVariable String sn) throws JsonProcessingException{
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
                if(dtServ.findByCompanyinfoidAndSn(SecurityUtils.getShiroUser().getCompanyid(), sn).size()<1){
                map.put("1", 1);
            }else {
                map.put("0", 0);
            }
        } catch (Exception e) {
            return null;
        }
        return mapper.writeValueAsString(map);

}

    @RequestMapping("/checkName/{name}")
    @ResponseBody
    public String checkName(@PathVariable String name) throws JsonProcessingException{
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            if(dtServ.findByCompanyinfoidAndName(SecurityUtils.getShiroUser().getCompanyid(), name).size()<1){
                map.put("1", 1);
            }else {
                map.put("0", 0);
            }
        } catch (Exception e) {
            return null;
        }
        return mapper.writeValueAsString(map);

    }
    
    @RequestMapping("/checkNameNew")
    @ResponseBody
    public String checkNameNew(HttpServletRequest request) throws JsonProcessingException{
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        String name = request.getParameter("name");
        try {
            if(dtServ.findByCompanyinfoidAndName(SecurityUtils.getShiroUser().getCompanyid(), name).size()<1){
                map.put("1", 1);
            }else {
                map.put("0", 0);
            }
        } catch (Exception e) {
            return null;
        }
        return mapper.writeValueAsString(map);

    }
}
