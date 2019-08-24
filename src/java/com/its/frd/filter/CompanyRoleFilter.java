package com.its.frd.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.its.common.shiro.ShiroUser;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.entity.Usercompanys;
import com.its.frd.service.MesCompanyRoleService;
import com.its.frd.service.UserCompanyroleService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.util.SpringBeanUtils;

public class CompanyRoleFilter implements Filter {
	private UsercompanysService userCpServ = SpringBeanUtils.getBean(UsercompanysService.class);
	private MesCompanyRoleService cpRoleServ = SpringBeanUtils.getBean(MesCompanyRoleService.class);
	private UserCompanyroleService ucprServ = SpringBeanUtils.getBean(UserCompanyroleService.class);
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//过滤静态资源
		if(httpRequest.getRequestURI().contains("/js/") 
				|| httpRequest.getRequestURI().contains("/styles/")
				|| !httpRequest.getRequestURI().contains("/management/index")
				|| httpRequest.getRequestURI().endsWith("/management/index/userCompanys")
				){
	            chain.doFilter(request, response);
	            return;
		}
		
		String companyId = request.getParameter("companyId");
		if ((companyId != null) && (!"".equals(companyId))) {
			ShiroUser shiroUser = SecurityUtils.getShiroUser();
			try {
				//判断是否是手机端访问
				shiroUser.setPhoneMode(this.checkPhoneMode(httpRequest));
				
				Long cpId = Long.valueOf(Long.parseLong(companyId));
				//与用户关联的userCompanys集合
				List<Usercompanys> userCps = userCpServ.findByCompanyid(cpId);
				//判断传入companyid是否与当前用户有关联
				if(this.checkContain(userCps, shiroUser.getUser().getId())
						&& this.checkContainCompanyRole(cpId, shiroUser.getUser().getId())){
					shiroUser.setCompanyid(cpId);
				}else{
					shiroUser.setCompanyid(null);
				}
			} catch (Exception e) {
				try {
					//但传入的companyid异常时,取消companyid
					SecurityUtils.getShiroUser().setCompanyid(null);
				} catch (Exception e2) {
					e.printStackTrace();
				}
				e.printStackTrace();
			}
		}else{
			/*
			 *  1.但传入的companyid为空时也取消设置companyid
			 *  2.需要判断uri中是否是 /management/index 路径的子路经
			 */
			//if(httpRequest.getRequestURI().contains(""))
			if(httpRequest.getSession().getAttribute("shiroUser") != null)
				SecurityUtils.getShiroUser().setCompanyid(null);
		}
		chain.doFilter(request, response);
	}
	
	/**
	 * 检查用户是否在传入的公司Id所在的公司存在角色关联
	 * @return
	 */
	private boolean checkContainCompanyRole(Long companyId,Long userId){
		List<UserCompanyrole> ucpr = ucprServ.findByUserIdAndMesCompanyRoleCompanyid(userId, companyId);
		if(ucpr.size() > 0)
			return true;
		return false;
	}
	
	/**
	 * 判断当前用户是否与传入的公司id有关联,且关联公司状态是审核通过的.
	 * @param userCompanys
	 * @param userId
	 * @return
	 */
	private boolean checkContain(List<Usercompanys> userCompanys,Long userId){
		for(Usercompanys uc : userCompanys){
			if(uc.getUserid().compareTo(userId) == 0){
				Companyinfo cp = uc.getCompanyinfo();
				if(cp.getCompanystatus().equals(Companyinfo.COPANYSTATUS_OK)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 检查是否是手机端访问.
	 * @param request
	 * @return 如果是手机端访问则返回true,否则返回false.
	 */
	private Boolean checkPhoneMode(HttpServletRequest request){
		String agentStr = request.getHeader("user-agent");
		if(agentStr.contains("Android")) {
			System.out.println("Android移动客户端");
			return true;
		}else if(agentStr.contains("iPhone")) {
			System.out.println("iPhone移动客户端");
			return true;
		}else if(agentStr.contains("iPad")) {
			System.out.println("iPad客户端");
			return true;
		} 
		return false;
	}
	
	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
	

}