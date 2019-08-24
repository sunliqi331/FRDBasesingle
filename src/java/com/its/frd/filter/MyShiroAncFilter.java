package com.its.frd.filter;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import com.its.common.shiro.ShiroUser;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Usercompanys;

public class MyShiroAncFilter extends AuthorizationFilter{
	
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		//过滤静态资源
		if(httpRequest.getRequestURI().contains("/js/") 
				|| httpRequest.getRequestURI().contains("/styles/")){
			return true;
		}
		String companyId = request.getParameter("companyId");
		if ((companyId != null) && (!"".equals(companyId))) {
			ShiroUser shiroUser = SecurityUtils.getShiroUser();
			try {
				Long cpId = Long.valueOf(Long.parseLong(companyId));
				List<Usercompanys> userCps = null;//userCpServ.findByCompanyid(cpId);
				//判断传入companyid是否与当前用户有关联
				if(this.checkContain(userCps, shiroUser.getUser().getId())){
					shiroUser.setCompanyid(cpId);
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
			//但传入的companyid为空时也取消设置companyid
			//if(httpRequest.getSession().getAttribute("shiroUser") != null)
				//SecurityUtils.getShiroUser().setCompanyid(null);
		}
		return true;
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

}
