package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import com.its.common.service.UserService;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Department;
import com.its.frd.entity.MesUserPosition;
import com.its.frd.entity.Userdepartment;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.DepartmentService;
import com.its.frd.service.MesCompanyPositionService;
import com.its.frd.service.MesUserPositionService;
import com.its.frd.service.UserdepartmentService;

@Controller
@RequestMapping("/companyOrganization")
public class CompanyOrganizationController {
	
	private final String PAGEPRE = "companyOrganization/";
	//公司-工厂-部门-人员  关联关系
	@Resource
	private CompanyinfoService companyinfoService;
	@Resource
	private DepartmentService departmentService;
	@Resource
	private UserdepartmentService userdepartmentService;
	@Resource
	private UserService userServ;
	//人员信息关联
	@Resource
	private UserService userService;
	//人员-职位-职位名  关联关系
	@Resource
	private MesUserPositionService mesUserPositionService;
	@Resource
	private MesCompanyPositionService mesCompanyPositionService;
	
	//区分公司(Company)ID的前缀
	private static final String C = "C";
	//区分工厂(Factory)ID的前缀
	private static final String F = "F";
	//区分部门(Department)ID的前缀
	private static final String D = "D";
	//区分人员(User)ID的前缀
	private static final String U = "U";
	
	//体系结构公用json字符串（在调用检索方法时初始化）
	private StringBuilder jsonStr = new StringBuilder();
	
	//菜单入口
	@RequestMapping(value="/tree_list", method={RequestMethod.GET, RequestMethod.POST})
	public String treeList(Map<String, Object> map){
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
		map.put("id",C+companyId);
		return PAGEPRE + "/tree_list";
	}
	
	@RequestMapping(value="/tree", method=RequestMethod.GET)
	public String tree(Map<String, Object> map){
		//初始化
		jsonStr = new StringBuilder();
		//获取当前登录的公司为根节点
		findCompany(SecurityUtils.getShiroUser().getCompanyid());
		
		//去除字符串尾部最后一个逗号
		String result = jsonStr.toString();
		map.put("companyOrganizationInfo", result.substring(0, result.length()-1));
		return PAGEPRE + "/tree";
	}

	//根据id查询id下的工厂，id可能是公司的也可能是父工厂的
	@RequestMapping("/factoryInfoData/{idStr}")
	@ResponseBody
	public String factoryInfoData(@PathVariable String idStr, HttpServletRequest request, Page page) throws JsonProcessingException{
		Long id = Long.parseLong(idStr.substring(1));
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Companyinfo> factoryinfos = companyinfoService.findByParentidAndCompanytype(id, "factory");
		
		page.setTotalCount(factoryinfos.size());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("factoryinfos", factoryinfos);
		map.put("page", page);
		return mapper.writeValueAsString(map);
	}
	
	//根据id查询出该id下所有部门，id可能是公司的，工厂的，父部门的
	@RequestMapping("/departmentInfoData/{idStr}")
	@ResponseBody
	public String departmentInfoData(@PathVariable String idStr, HttpServletRequest request, Page page) throws JsonProcessingException{
		Long id = Long.parseLong(idStr.substring(1));
		Map<String, Object> map = new HashMap<String, Object>();
		List<Department> departmentinfos;
		if (idStr.startsWith(C)) {
			departmentinfos = departmentService.findForCompany(id);
		} else if (idStr.startsWith(F)) {
			departmentinfos = departmentService.findForFactory(id);
		} else {
			departmentinfos = departmentService.findByParentid(id);
		}
		page.setTotalCount(departmentinfos.size());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("departmentinfos", departmentinfos);
		map.put("page", page);
		return mapper.writeValueAsString(map);
	}
	
	//根据id查询出该id下所有人员，id可能是部门的，或者是指定员工的
	@RequestMapping("/userInfoData/{idStr}")
	@ResponseBody
	public String userInfoData(@PathVariable String idStr, HttpServletRequest request, Page page) throws JsonProcessingException{
		Long id = Long.parseLong(idStr.substring(1));
		Map<String, Object> map = new HashMap<String, Object>();
		List<User> userinfos = new ArrayList<User>();
		if (idStr.startsWith(D)) {
			List<Userdepartment> userdepartments = userdepartmentService.findByDepartmentid(id);
			List<Long> userids = new ArrayList<Long>();
			if(userdepartments.size() > 0){
				for(Userdepartment userdepartment : userdepartments){
					//将找出的userid存入数组ids里
					userids.add(userdepartment.getUserid());
				}
				if(userids.size() > 0){
				    //当前部门下的人员
				    Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class
				            ,new SearchFilter("id", Operator.IN, userids.toArray()));
				    userinfos = userService.findByExample(specification, page);
				}
			}
		} else {
			User user = userService.findById(id);
			userinfos.add(user);
		}
		for(User user : userinfos){
			//查找出人员在当前登录公司下的职位
			if(user != null) {
				List<MesUserPosition> mesUserPositions = mesUserPositionService.findByUseridAndCompanyId(user.getId(), SecurityUtils.getShiroUser().getCompanyid());
				StringBuilder position = new StringBuilder();
				for(MesUserPosition mesUserPosition : mesUserPositions){
					position.append(mesUserPosition.getMesCompanyPosition().getPositionname() + " ");
				}
				user.setPosition(position.toString());
			}
		}
		page.setTotalCount(userinfos.size());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("userinfos", userinfos);
		map.put("page", page);
		return mapper.writeValueAsString(map);
	}
		
	private void findCompany(Long companyId) {
		Companyinfo company = companyinfoService.findById(companyId);
		jsonStr.append(jsonStrFormat(C+company.getId(), "0", "[公司]"+company.getCompanyname())); 
		//获取公司的工厂
		findFactorys(company.getId(), C);
		//获取公司的部门
		findDepts(company.getId(),C);
		
	}
	private void findFactorys(Long companyId,String parentType) {
		List<Companyinfo> factorys = companyinfoService.findByParentidAndCompanytype(companyId, "factory");
		if (factorys.isEmpty()) {
			return;
		}
		for (Companyinfo factory : factorys) {
			if (parentType.equals(C)) {
				jsonStr.append(jsonStrFormat(F+factory.getId(), C+companyId, "[工厂]"+factory.getCompanyname()));
			}else {
				jsonStr.append(jsonStrFormat(F+factory.getId(), F+companyId, "[工厂]"+factory.getCompanyname()));
			}
			//获取工厂的子工厂
			findFactorys(factory.getId(),F);
			//获取工厂的部门
			findDepts(factory.getId(),F);
		}
	}
	
	private void findDepts(Long id, String parentType) {
		List<Department> departments;
		if (parentType.equals(C)) {
			departments = departmentService.findForCompany(id);
		}else if (parentType.equals(F)) {
			departments = departmentService.findForFactory(id);
		}else {
			departments = departmentService.findByParentid(id);
		}
		if (departments.isEmpty()) {
			return;
		}
		for (Department department : departments) {
			if (parentType.equals(C)) {
				jsonStr.append(jsonStrFormat(D+department.getId(), C+id, "[部门]"+department.getName()));
			}else if (parentType.equals(F)) {
				jsonStr.append(jsonStrFormat(D+department.getId(), F+id, "[部门]"+department.getName()));
			} else {
				jsonStr.append(jsonStrFormat(D+department.getId(), D+id, "[部门]"+department.getName()));
			}
			//获取部门的子部门
			findDepts(department.getId(),D);
			//获取部门里的用户
			findUsers(department.getId());
		}
	}
	
	
	private void findUsers(Long deptId) {
		List<Userdepartment> userdepartments = userdepartmentService.findByDepartmentid(deptId);
		for (Userdepartment userdepartment : userdepartments) {
			User user = userService.findById(userdepartment.getUserid());
			jsonStr.append(jsonStrFormat(U+user.getId(), D+deptId, "[人员]"+user.getRealname()));
		}
	}
	
	private String jsonStrFormat(String id, String pId, String name){
		return "{id:\"" + id +  "\", pId:\"" + pId + "\", name:\"" + name + "\", target:\"ajax\"},";
	}
}
