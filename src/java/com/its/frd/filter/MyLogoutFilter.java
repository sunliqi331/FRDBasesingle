package com.its.frd.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLogoutFilter extends LogoutFilter{
		
	private static final Logger log = LoggerFactory.getLogger(MyLogoutFilter.class);
	 @Override
	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
	        Subject subject = getSubject(request, response);
	        String redirectUrl = getRedirectUrl(request, response, subject);
	        //try/catch added for SHIRO-298:
	        try {
	            subject.logout();
	        } catch (SessionException ise) {
	            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
	        }
	        String kickout = request.getParameter("kickout");
	        if(kickout != null && kickout.equals("1")){
	        	redirectUrl += "?kickout=1";
	        	issueRedirect(request, response, redirectUrl);
	        }else{
	        	issueRedirect(request, response, redirectUrl);
	        }
	        return false;
	    }
}
