package com.its.frd.log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.its.common.entity.main.LogInfo;
import com.its.common.log.LogLevel;
import com.its.common.service.LogInfoService;
import com.its.common.shiro.ShiroUser;
import com.its.common.utils.SecurityUtils;

@Order(1)
@Aspect
@Component
public class LogAspect {
	private static Map<String,String> modelNameMap 
		= new HashMap<String,String>();
	
	static{
		modelNameMap.put("CompanyfileServiceImp", "附件信息");
		modelNameMap.put("CompanyinfoServiceImp", "公司信息");
		modelNameMap.put("DepartmentServiceImp", "部门信息");
		modelNameMap.put("FriendsServiceImp", "好友信息");
		modelNameMap.put("MesCompanyPositionServiceImp", "公司职位");
		modelNameMap.put("MesCompanyRolePermissionServiceImp", "公司权限");
		modelNameMap.put("MesCompanyRoleServiceImpl", "公司角色");
		modelNameMap.put("MesDriverImp", "设备信息");
		modelNameMap.put("MesDriverrepairServiceImp", "设备维修记录");
		modelNameMap.put("MesDriverTypeServiceImp", "设备类型");
		modelNameMap.put("MesDriverTypePropertyServiceImp", "设备类型属性");
		modelNameMap.put("MesPointCheckDataServiceImp", "测点数据比较值");
		modelNameMap.put("MesPointGatewarServiceImp", "测点网关");
		modelNameMap.put("MesPointsServiceImp", "测点数据");
		modelNameMap.put("MesPointTypeServiceImp", "测点类型");
		modelNameMap.put("MesProcedurePropertyImp", "工序属性");
		modelNameMap.put("MesProductImp", "产品信息");
		modelNameMap.put("MesProductlineImp", "产线");
		modelNameMap.put("MesProductProcedureServiceImp", "产品工序");
		modelNameMap.put("UserPositionServiceImp", "用户职位");
		modelNameMap.put("MonitorPainterServiceImp", "监控数据");
		modelNameMap.put("UserCompanyroleServiceImpl", "用户公司角色");
		modelNameMap.put("UsercompanysServiceImp", "用户公司");
		modelNameMap.put("UserdepartmentServiceImp", "用户部门");
		modelNameMap.put("FriendsServiceImpl", "好友信息");
		modelNameMap.put("UserApiServiceImp", "用户信息");
	}
	
	@Resource
	private LogInfoService logServ;
	
	@Pointcut("execution(* com.its.frd.service.impl.*.*(..))") 
    public void joinPointExpression() {}
	
	@AfterReturning(pointcut="joinPointExpression()")
	public void beforeMethod(JoinPoint point){
		String userActive = this.getCRUDName(point)+ " " +this.getActiveClassName(point);
		this.savaLogInfo(userActive, LogLevel.TRACE);
	}
	
	@AfterThrowing(value="joinPointExpression()",throwing="ex")
	public void afterThrowing(JoinPoint point,Throwable ex){
		String userActive = this.getCRUDName(point)+ " " +this.getActiveClassName(point);
		String exMsg = ex.toString();
		userActive += " 异常信息: "+exMsg.substring(0,exMsg.length()>255?254:exMsg.length());
		this.savaLogInfo(userActive, LogLevel.ERROR);
	}
	
	private void savaLogInfo(String msg,LogLevel level){
		LogInfo log = this.getInstanceLogInfo(msg, level);
		new Runnable(){
			@Override
			public void run() {
				try {
					logServ.saveOrUpdate(log);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}.run();
	}
	
	private String getCRUDName(JoinPoint point){
		String methodName = point.getSignature().getName();
		String activeName = "";
		if(methodName.contains("find")){
			activeName = "查询";
		}else if(methodName.contains("del")){
			activeName = "删除";
		}else if(methodName.contains("update")){
			activeName = "修改";
		}else if(methodName.contains("save")){
			activeName = "增加";
		}
		return activeName;
	}
	
	
	
	private String getActiveClassName(JoinPoint point){
		String className = point.getTarget().toString();
		className = className.substring(className.lastIndexOf(".")+1,className.lastIndexOf("@"));
		return modelNameMap.get(className);
	}

	private LogInfo getInstanceLogInfo(String msg,LogLevel level){
		LogInfo log = new LogInfo();
		log.setMessage(msg);
		String username = "";
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		if(shiroUser != null && shiroUser.getUser() != null){
			username = shiroUser.getUser().getUsername();
		}else{
			username = "商城调用接口";
		}
		log.setUsername(username);
		log.setCreateTime(new Date());
		log.setLogLevel(level);
		log.setIpAddress(SecurityUtils.getShiroUser().getIpAddress());
		return log;
	}
	
}
