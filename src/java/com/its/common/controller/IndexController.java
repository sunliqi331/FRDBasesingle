package com.its.common.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.SecurityConstants;
import com.its.common.entity.main.LogInfo;
import com.its.common.entity.main.Module;
import com.its.common.entity.main.Permission;
import com.its.common.entity.main.User;
import com.its.common.entity.main.UserRole;
import com.its.common.exception.ServiceException;
import com.its.common.log.Log;
import com.its.common.log.LogMessageObject;
import com.its.common.log.impl.LogUitls;
import com.its.common.service.LogInfoService;
import com.its.common.service.ModuleService;
import com.its.common.service.RoleService;
import com.its.common.service.UserRoleService;
import com.its.common.service.UserService;
import com.its.common.shiro.ShiroCasRealm;
import com.its.common.shiro.ShiroUser;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesCompanyPosition;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverAlarm;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductAlarm;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.MesProductline;
import com.its.frd.entity.MesUserPosition;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.entity.Usercompanys;
import com.its.frd.params.CompanyfileType;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesCompanyPositionService;
import com.its.frd.service.MesCompanyRoleService;
import com.its.frd.service.MesDriverAlarmService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesPointsService;
import com.its.frd.service.MesProductAlarmService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.MesProductlineService;
import com.its.frd.service.MesUserPositionService;
import com.its.frd.service.UserCompanyroleService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.util.DateUtils;

@Controller
@RequestMapping("/management/index")
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserCompanyroleService uCrService;

	@Resource
	private UserRoleService userRoleService;

	@Autowired
	private ModuleService moduleService;

	@Autowired
	private CompanyinfoService cpService;

	@Autowired
	private MesProductlineService plService;

	@Autowired
	private MesDriverService mdService;
	
	@Autowired
	private MesDriverAlarmService mesDriverAlarmService;

	@Autowired
	private MesPointsService mpService;	

	@Autowired
	private MesCompanyRoleService mesCompanyRoleService;

	@Resource
	private MesCompanyPositionService mcpServ;

	@Resource
	private MesUserPositionService mupServ;
	
	@Resource
	private MesProductService mesProductService;
	
	@Resource
	private MesProductAlarmService mesProductAlarmService;

	@Autowired
	private UsercompanysService ucServ;

	@Resource
	private UserCompanyroleService userCpRoleServ;

	@Resource
	private MesDriverService driverServ;

	@Resource
	private MesPointsService pointsServ;

	@Resource
	private RoleService roleServ;

	@Resource
	private ShiroCasRealm shiroCasRealm;

	@Resource
	private LogInfoService logServ;
	
	@Resource
	private CompanyfileService cfServ;

	private static final String INDEX = "management/index/index";
	private static final String UPDATE_PASSWORD = "management/index/updatePwd";
	private static final String UPDATE_BASE = "management/index/updateBase";

	@Log(message="{0}登录了系统。")
	@RequiresUser
	@RequestMapping(value="", method=RequestMethod.GET)
	public String index(HttpServletRequest request, Map<String, Object> map,Page page) {
		/*
		 * 需判断当前登录的用户和传入的companyid是否存在相关的角色关联
		 */
		shiroCasRealm.clearCurrentUserAuthorizationInfoCache();
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		shiroUser.setIpAddress(request.getLocalAddr());
		Long companyId = shiroUser.getCompanyid();
		request.getSession().setAttribute("shiroUser", shiroUser);
		if(companyId != null){
			Companyinfo companyInfo = cpService.findById(companyId);
			map.put("companyinfo", companyInfo);
			request.getSession().setAttribute("companyinfo", companyInfo);
			List<UserCompanyrole> userCpRoles = 
					userCpRoleServ.findByUserId(shiroUser.getUser().getId());
			boolean isOk = false;
			for(UserCompanyrole role : userCpRoles){
				if(role.getMesCompanyRole().getCompanyid().equals(companyId))
					isOk = true;
			}
			if(!isOk)
				return INDEX;
		}else{
			request.getSession().removeAttribute("companyinfo");
		}
		//shiroUser.setCompanyid(companyId);
		getFactoryAndProductline(request, map, shiroUser);
		getTotalNumber(request, map, shiroUser);
		//组装菜单
		Module menuModule = getMenuModule(SecurityUtils.getSubject());

		if(shiroUser.getUser().getId()!=null){
			//获取公司相关数据
			User user = getCompanyInfos(request, map, page, shiroUser);
			map.put("user", user);
			map.put("page", page);
		}
		//登录用户职位
		String position = null;
		if(companyId != null) {
			StringBuilder userPosition = new StringBuilder();
			List<MesUserPosition> mesUserPositions = mupServ.findByUseridAndCompanyId(shiroUser.getUser().getId(),companyId);
			for(MesUserPosition mesUserPosition : mesUserPositions){
				userPosition.append(mesUserPosition.getMesCompanyPosition().getPositionname() + " ");
			}
			position = userPosition.toString();
		}
		//登录头像显示
		String pic = new String();
		List<Companyfile> cf= cfServ.findByParentidAndType(shiroUser.getUser().getId(),CompanyfileType.HEADPICTYPEFILE.toString());
		for(Companyfile cfs : cf) {
			pic = request.getServletContext().getContextPath()+"/company/showPic/"+cfs.getId();
		}
//		pic = request.getServletContext().getContextPath()+"/company/showPic/"+"458";
		boolean bool = false;
		if(cf.size() > 0)
			bool = true;
		List<MesDriverAlarm> mesDriverAlarms = new ArrayList<>();
        List<MesDriver> mesDrivers = this.getDriver(request, page);//
        List<Long> ids = new ArrayList<Long>();
        for (MesDriver mesDriver : mesDrivers) {
            ids.add(mesDriver.getId());
        }
        if(ids.size() > 0){
        	/*Page page2 = new Page();
        	page2.setPageNum(1);
        	page2.setNumPerPage(5);
        	page2.setOrderField("updatetime");
            Specification<MesDriverAlarm> specification = DynamicSpecifications.bySearchFilter(request, MesDriverAlarm.class
                    ,new SearchFilter("mesDriver.id",Operator.IN,ids.toArray()));*/
            mesDriverAlarms = mesDriverAlarmService.findTen(ids);
            for(MesDriverAlarm mesDriverAlarm : mesDriverAlarms){
                if(mesDriverAlarm.getStatecode().equals("1")){
                    mesDriverAlarm.setTstatecode("超过上限值");
                }
                if(mesDriverAlarm.getStatecode().equals("-1")){
                    mesDriverAlarm.setTstatecode("低于下限值");
                }
            }
        }
        if(mesDriverAlarms.size()>0){
            List<MesDriverAlarm> list = new ArrayList<>();
            list.addAll(mesDriverAlarms);
            Collections.reverse(list);
//            for(int i=0 ; i<mesDriverAlarms.size() ; i++){
//                list.add(mesDriverAlarms.get(mesDriverAlarms.size()-i-1));
//            }
            map.put("mesDriverAlarms", list);
        }else{
            map.put("mesDriverAlarms", null);
        }
        List<MesProductAlarm> mesProductAlarms = new ArrayList<>();
        List<MesProduct> mesProducts = this.getProduct(request, page);
        List<String> ids1 = new ArrayList<String>();
        List<Long> ids2 = new ArrayList<Long>();
        for(MesProduct mesProduct : mesProducts){
            ids1.add(mesProduct.getModelnum());
            List<MesProductProcedure> procedures = mesProduct.getMesProductProcedures();
            for (MesProductProcedure procedure : procedures) {
            	for(MesProcedureProperty mesProcedureProperty : procedure.getMesProcedureProperties()){
            		ids2.add(mesProcedureProperty.getId());
            	}
			}
        }
        if(ids1.size() > 0 && ids2.size() > 0){
        	/*Page page2 = new Page();
        	page2.setPageNum(1);
        	page2.setNumPerPage(5);
        	page2.setOrderField("updatetime");
            Specification<MesProductAlarm> specification1 = DynamicSpecifications.bySearchFilter(request, MesProductAlarm.class
                    ,new SearchFilter("productmodelnum",Operator.IN,ids1.toArray())
                    ,new SearchFilter("mesProcedureProperty.id",Operator.IN,ids2.toArray())
                    );*/
            mesProductAlarms = mesProductAlarmService.findTen(ids2);
            for(MesProductAlarm MesProductAlarm : mesProductAlarms){
                if(MesProductAlarm.getStatecode().equals("1")){
                    MesProductAlarm.setTstatecode("超过上限值");
                }
                if(MesProductAlarm.getStatecode().equals("-1")){
                    MesProductAlarm.setTstatecode("低于上限值");
                }
            }
        }
        if(mesProductAlarms.size()>0){
            List<MesProductAlarm> list = new ArrayList<>();
            list.addAll(mesProductAlarms);
            Collections.reverse(list);
            map.put("mesProductAlarms", list);
        }else{
            map.put("mesProductAlarms", null);
        }
        
		map.put("bool", bool);
		map.put("pic", pic);
		//上次登录时间
		getLoginTime(request, map, shiroUser);
		map.put("position", position);
		map.put("login_role", this.getRoleByUserId(shiroUser.getId(),request));
		map.put(SecurityConstants.LOGIN_USER, shiroUser.getUser());
		map.put("menuModule", menuModule);
		//MenuListInfo.menuMap = map;
		shiroUser.menuMap = map;
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{shiroUser.getLoginName()}));
		return INDEX;
	}

    /**
     * 根据参数来判断是获取登录用户公司的设备列表,或是和该用户关联所有公司的设备
     * @return
     */
    private List<MesProduct> getProduct(HttpServletRequest request, Page page){
        //用户登录的公司
        if(SecurityUtils.getShiroUser().getCompanyid() != null){
            return mesProductService.findByCompanyinfo((SecurityUtils.getShiroUser().getCompanyid()));
        //用户关联的所有公司
        }else if(SecurityUtils.getShiroUser().getCompanyid() == null){
            List<Long> companyinfoIds = new ArrayList<>();
            List<MesProduct> mesProducts = new ArrayList<>();
            List<Usercompanys> usercompanys = ucServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
            for(Usercompanys usercompany : usercompanys){
                companyinfoIds.add(usercompany.getCompanyinfo().getId());
            }
            if(companyinfoIds.size() > 0){
                Specification<MesProduct> specification = DynamicSpecifications.bySearchFilter(request, MesProduct.class
                        ,new SearchFilter("companyinfo.id",Operator.IN,companyinfoIds.toArray())
                        );
                mesProducts = mesProductService.findPage(specification, page);
            }
            return mesProducts;
        //异常返回空
        }else{
            return null;
        }
    }

	
	 /**
     * 根据参数来判断是获取登录用户公司的设备列表,或是和该用户关联所有公司的设备
     * @return
     */
    private List<MesDriver> getDriver(HttpServletRequest request, Page page){
        //用户登录的公司
        if(SecurityUtils.getShiroUser().getCompanyid() != null){
            return mdService.findByCompanyidAndDifferencetype(SecurityUtils.getShiroUser().getCompanyid(),"1");
        //用户关联的所有公司
        }else if(SecurityUtils.getShiroUser().getCompanyid() == null){
            List<Long> companyinfoIds = new ArrayList<>();
            List<MesDriver> mesDrivers = new ArrayList<>();
            List<Usercompanys> usercompanys = ucServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
            for(Usercompanys usercompany : usercompanys){
                companyinfoIds.add(usercompany.getCompanyinfo().getId());
            }
            if(companyinfoIds.size() > 0){
                Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class
                        ,new SearchFilter("companyinfo.id",Operator.IN,companyinfoIds.toArray())
                        );
                mesDrivers = mdService.findPage(specification, page);
            }
            return mesDrivers;
        //异常返回空
        }else{
            return null;
        }
    }

	
	/**
	 * 检查公司id是否和当前用户有效
	 * @param companyId
	 * @return
	 */
	private boolean checkCompanyId(Long companyId){
		Companyinfo cpinfo = cpService.findById(companyId);
		if(cpinfo == null){
			return false;
		}else if(!cpinfo.getCompanystatus().equals(Companyinfo.COPANYSTATUS_OK)){
			return false;
		}
		return true;
	}

	private User getCompanyInfos(HttpServletRequest request, Map<String, Object> map, Page page, ShiroUser shiroUser) {
		//与用户关联的公司集合
		Specification<Usercompanys> specification = 
				DynamicSpecifications.bySearchFilter(request, Usercompanys.class,
						new SearchFilter("userid",Operator.EQ,shiroUser.getUser().getId()));
		page.setNumPerPage(Integer.MAX_VALUE);
		List<Usercompanys> usercompanys = ucServ.findPage(specification,page);
		List<Long> ids = new ArrayList<Long>();
		List<Companyinfo> company = new ArrayList<>();
		if(usercompanys != null)
			for(Usercompanys uc : usercompanys){
				ids.add(uc.getCompanyinfo().getId());
			}
		if(ids.size() > 0){
			Specification<Companyinfo> specification1 = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
					new SearchFilter("id", SearchFilter.Operator.IN, ids.toArray())
					,new SearchFilter("companystatus",Operator.NOTEQ,"3")
					);
			company = cpService.findPage(specification1, page);
		}

		List<Long> ids1 = new ArrayList<Long>();
		List<UserCompanyrole> userCompanyroles = uCrService.findAll(page);
		for(UserCompanyrole ucr : userCompanyroles){
			//查询拥有的公司角色
			if(ucr.getUserId().compareTo(shiroUser.getUser().getId()) == 0){
				ids1.add(mesCompanyRoleService.findById(ucr.getMesCompanyRole().getId()).getCompanyid());
			}
		}
		List<Companyinfo> companys = new ArrayList<>();
		if(ids1.size() > 0){
			Specification<Companyinfo> specification2 = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
					new SearchFilter("id", SearchFilter.Operator.IN, ids1.toArray())
					,new SearchFilter("companystatus", SearchFilter.Operator.EQ, Companyinfo.COPANYSTATUS_OK)
					);
			companys = cpService.findPage(specification2, page);
		}
		User user = userService.findById(shiroUser.getUser().getId());
		//关联公司
		map.put("company", company);
		//有权限公司
		map.put("companys", companys);
		shiroUser.setCompanyInfos(company);
		return user;
	}
	

	private void getLoginTime(HttpServletRequest request, Map<String, Object> map, ShiroUser shiroUser) {
		String username = shiroUser.getUser().getUsername();
		Date last_login_time = null;
		Page page3 = new Page();
		page3.setNumPerPage(Integer.MAX_VALUE);
		Specification<LogInfo> specification3 = DynamicSpecifications.bySearchFilter(request, LogInfo.class
				,new SearchFilter("username", Operator.EQ,username)
				,new SearchFilter("message", Operator.EQ,username + "登录了系统。"));
		List<LogInfo> logInfo = logServ.findByExample(specification3, page3);
		if(logInfo.size() > 0)
			last_login_time = logInfo.get(logInfo.size()-1).getCreateTime();
		if(last_login_time != null)
			map.put("last_login_time", DateUtils.getYYYYMMDDDayStr(last_login_time));
	}

	//	获得设备总数,工厂总数,产线总数信息。（MES系统中）
	private void getFactoryAndProductline(HttpServletRequest request, Map<String, Object> map, ShiroUser shiroUser) {
		Long companyId = shiroUser.getCompanyid();
		Page pagen = new Page();
		Specification<Companyinfo> specifications = DynamicSpecifications.bySearchFilter(request, Companyinfo.class
				,new SearchFilter("parentid", Operator.EQ, companyId)
				,new SearchFilter("companytype", Operator.EQ, "factory")
				,new SearchFilter("companystatus",Operator.NOTEQ,"3")
				);
		Specification<MesProductline> specification1 = DynamicSpecifications.bySearchFilter(request, MesProductline.class
				,new SearchFilter("companyinfo.parentid",Operator.EQ, companyId)
				);
		Specification<MesDriver> specification2 = DynamicSpecifications.bySearchFilter(request, MesDriver.class
				, new SearchFilter("companyinfo.id",Operator.EQ,companyId)
				,new SearchFilter("differencetype",Operator.NOTEQ,"0")
				);
		Specification<MesCompanyPosition> specification3 = DynamicSpecifications.bySearchFilter(request, MesCompanyPosition.class
				, new SearchFilter("companyinfo.id",Operator.EQ,companyId)
				);
		List<Companyinfo> factoryList = cpService.findPage(specifications, pagen);
		//		List<Companyinfo> factoryList = cpService.findByParentidAndCompanytype(companyId, "factory");
		List<MesProductline> productline=plService.findPage(specification1, pagen);
		List<MesDriver> mesdriver = mdService.findPage(specification2, pagen);
		List<MesCompanyPosition> mesCompanyPosition = mcpServ.findPage(specification3, pagen);
		map.put("Fids", factoryList.size());
		map.put("pln", productline.size());
		map.put("mdn", mesdriver.size());
		map.put("mcp", mesCompanyPosition.size());	
	}


	//	获得公司总数,用户总数,公司人员数,设备总数,测点总量。（平台中）
	private void getTotalNumber(HttpServletRequest request,Map<String, Object> map, ShiroUser shiroUser) {
		Page page = new Page();
		page.setNumPerPage(Integer.MAX_VALUE);
		List<Companyinfo> company = new ArrayList<>();
		Long users = 0l;
		Long drivers = 0l;
		Long points = 0l;
		Specification<UserRole> specification = DynamicSpecifications.bySearchFilter(request, UserRole.class
				,new SearchFilter("role.deleteable",Operator.EQ,"1")
				,new SearchFilter("user.id",Operator.EQ,shiroUser.getUser().getId())
				);
		List<UserRole> userRole = userRoleService.findByExample(specification, page);
		List<Long> urIds = new ArrayList<>();
		for(UserRole ur : userRole){
			urIds.add(ur.getRole().getId());
		}
		if(urIds.size() > 0){
			if(urIds.contains(12l)){
				List<Usercompanys> uc = ucServ.findByUserid(shiroUser.getUser().getId());
				List<Long> ids = new ArrayList<>(); 
				for(Usercompanys ucs : uc){
					ids.add(ucs.getCompanyinfo().getId());
				}
				//		与当前用户关联的公司
				if(ids.size() > 0){
					Specification<Companyinfo> specification2 = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
							new SearchFilter("id", SearchFilter.Operator.IN, ids.toArray()),
							new SearchFilter("companytype",Operator.EQ,"company"),
							new SearchFilter("companystatus",Operator.NOTEQ,"3")
							);
					company = cpService.findPage(specification2, page);
				}
				if(company.size() > 0){
				    for(Companyinfo cps : company){
				        //		用户
				        Specification<Usercompanys> specification3 = DynamicSpecifications.bySearchFilter(request, Usercompanys.class
				                ,new SearchFilter("companyinfo.id",Operator.EQ,cps.getId())
				                );
				        List<Usercompanys> user = ucServ.findPage(specification3, page);
				        //判断是否为公司创建者
				        if(cps.getUserid().compareTo(shiroUser.getUser().getId()) == 0){
				            users += (user.size()-1); 
				        }else{
				            users += user.size(); 
				        }
				        //		设备
				        Specification<MesDriver> specification4 = DynamicSpecifications.bySearchFilter(request, MesDriver.class
				                ,new SearchFilter("companyinfo.id", Operator.EQ, cps.getId())
				                ,new SearchFilter("differencetype",Operator.NOTEQ,"0")
				                );
				        List<MesDriver> driver = driverServ.findPage(specification4, page);
				        drivers += driver.size();
				        //		测点
				        Specification<MesPoints> specification5 = DynamicSpecifications.bySearchFilter(request, MesPoints.class
				                ,new SearchFilter("mesPointGateway.companyinfo.id", Operator.EQ, cps.getId())
				                );
				        List<MesPoints> point = pointsServ.findPage(specification5, page);
				        points += point.size();
				    }
				}
			}else{
				Specification<Companyinfo> specification6 = DynamicSpecifications.bySearchFilter(request, Companyinfo.class
						,new SearchFilter("companytype", Operator.EQ, "company")
//						,new SearchFilter("companystatus", Operator.NOTEQ, "3")
						);
				company = cpService.findPage(specification6, page);
				List<User> user = userService.findAll(page);
				users = (long) user.size();
				List<MesDriver> driver = driverServ.findPage(page);
				drivers = (long) driver.size();
				List<MesPoints> point = pointsServ.findPage(page);
				points = (long) point.size();

			}
		}
		map.put("tcp", company.size());
		map.put("tuser", users);
		map.put("tmd", drivers);
		map.put("tmp", points);

	}



	private UserRole getRoleByUserId(Long id,HttpServletRequest request){
		if (id != null) {
			Specification<UserRole> specification = DynamicSpecifications.bySearchFilter(request, UserRole.class);
			Page page = new Page();
			page.setNumPerPage(Integer.MAX_VALUE);
			List<UserRole> list = userRoleService.findByExample(specification, page);
			if(list != null)
				return list.get(0);
		}
		return null;
	}

	private Module getMenuModule(Subject subject) {
		Module rootModule = moduleService.getTree();

		check(rootModule, subject);
		return rootModule;
	}

	private void check(Module module, Subject subject) {
		List<Module> list1 = new ArrayList<Module>();
		for (Module m1 : module.getChildren()) {
			// 只加入拥有show权限的Module
			try{
				subject.isPermitted(m1.getSn() + ":" + Permission.PERMISSION_SHOW);
			}catch(Exception e){
				System.out.println(e.getMessage());			
			}
			if (subject.isPermitted(m1.getSn() + ":" + Permission.PERMISSION_SHOW)) {
				check(m1, subject);
				list1.add(m1);
			}
		}
		module.setChildren(list1);
	}

	/**
	 * 获取当前用户在哪些公司有权限
	 *  1.在userCompany表中查询出该用户有多少关联的公司;
	 *  2.关联公司有多少是有效的;
	 *  3.该用户在这些公司是否有权限;
	 * @return
	 */
	@RequestMapping("/userCompanys")
	@ResponseBody
	public List<Companyinfo> getUserCompanyListForRole(){
		//当前用户
		User user = SecurityUtils.getShiroUser().getUser();
		//查询用户有哪些公司的角色
		List<UserCompanyrole> userCompanyrole = uCrService.findByUserId(user.getId());
		//从userCompanyrole中剥离出公司id
		List<Long> roleCompanyIds = this.getCompanysForUserCompanyroles(userCompanyrole);
		List<Companyinfo> OkCompanyinfos = cpService.findByIdInAndCompanystatusAndCompanytype(roleCompanyIds, 
				Companyinfo.COPANYSTATUS_OK, Companyinfo.COMPANY);
		return OkCompanyinfos;
	}
	
	/**
	 * 从UserCompanyrole集合中查询出Companyinfo集合
	 * @param userCompanyroles
	 * @return
	 */
	private List<Long> getCompanysForUserCompanyroles(List<UserCompanyrole> userCompanyroles){
		if(userCompanyroles == null || userCompanyroles.size() <= 0)
			return null;
		List<Long> cps = new ArrayList<>();
		for(UserCompanyrole cpu : userCompanyroles){
			if(cpu.getMesCompanyRole() != null){
				cps.add(cpu.getMesCompanyRole().getCompanyid());
			}
		}
		return cps;
	}
	
	/**
	 * 获取Usercompanys集合中的companyid集合
	 */
	private List<Long> getCompanyIdForUserCompanys(List<Usercompanys> ucps){
		if(ucps == null || ucps.size() <= 0)
			return null;
		List<Long> ids = new ArrayList<>();
		for(Usercompanys upc : ucps){
			if(upc.getCompanyinfo() != null){
				ids.add(upc.getCompanyinfo().getId());
			}
		}
		return ids;
	}
	
/*	@RequiresUser
	@RequestMapping(value="/updatePwd", method=RequestMethod.GET)
	public String preUpdatePassword() {
		return UPDATE_PASSWORD;
	}*/

	@Log(message="{0}修改了密码。")
	@RequiresUser
	@RequestMapping(value="/updatePwd", method=RequestMethod.POST)
	public @ResponseBody String updatePassword(ServletRequest request, String plainPassword, 
			String newPassword, String rPassword) {
		User user = SecurityUtils.getLoginUser();

		if (newPassword != null && newPassword.equals(rPassword)) {
			user.setPlainPassword(plainPassword);
			try {
				userService.updatePwd(user, newPassword);
			} catch (ServiceException e) {
				LogUitls.putArgs(LogMessageObject.newIgnore());//忽略日志
				return AjaxObject.newError(e.getMessage()).setCallbackType("").toString();
			}
			LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{user.getUsername()}));
			return AjaxObject.newOk("修改密码成功！").toString();
		}

		return AjaxObject.newError("修改密码失败！").setCallbackType("").toString();
	}

	@RequiresUser
	@RequestMapping(value="/updateBase", method=RequestMethod.GET)
	public String preUpdateBase(Map<String, Object> map) {
		map.put(SecurityConstants.LOGIN_USER, SecurityUtils.getLoginUser());
		return UPDATE_BASE;
	}

	@Log(message="{0}修改了详细信息。")
	@RequiresUser
	@RequestMapping(value="/updateBase", method=RequestMethod.POST)
	public @ResponseBody String updateBase(User user, ServletRequest request) {
		User loginUser = SecurityUtils.getLoginUser();

		loginUser.setPhone(user.getPhone());
		loginUser.setEmail(user.getEmail());

		userService.saveOrUpdate(loginUser);

		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{user.getUsername()}));
		return AjaxObject.newOk("修改详细信息成功！").toString();
	}
}
