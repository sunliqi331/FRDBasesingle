package com.its.common.shiro;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import com.its.frd.websocket.WebSocketBrokerConfig;

public class DWZUserFilter extends UserFilter {

	public final static String X_R = "X-Requested-With";
	public final static String X_R_VALUE = "XMLHttpRequest";
	public static String localURL = "";
	static{
		Properties properties = new Properties();
		InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/config.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		localURL = properties.getProperty("localURL");
	}
	/**
	 * 加入ajax查询参数，以便跳转至超时登录页面。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException  
	 * @see org.apache.shiro.web.filter.AccessControlFilter#redirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected void redirectToLogin(ServletRequest request,
			ServletResponse response) throws IOException {
    	HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String xrv = httpServletRequest.getHeader(X_R);
		
		if (xrv != null && xrv.equalsIgnoreCase(X_R_VALUE)) {
			Map<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("ajax", "true");
			HttpServletResponse servletResponse = (HttpServletResponse)response;
			servletResponse.setHeader("Access-Control-Allow-Origin","*");  
			servletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			WebUtils.issueRedirect(request, servletResponse, localURL, queryParams);
		} else {
			super.redirectToLogin(request, response);
		}
	}
}
