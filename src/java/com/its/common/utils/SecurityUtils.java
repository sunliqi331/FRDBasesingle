package com.its.common.utils;

import java.util.HashMap;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.its.common.entity.main.User;
import com.its.common.service.UserService;
import com.its.common.shiro.ShiroUser;
import com.its.frd.util.SpringBeanUtils;

public abstract class SecurityUtils {
	
	public static final String COMPANYIDCODE = "companyId";
	
	private static UserService userServ = SpringBeanUtils.getBean(UserService.class);
	
	public static User getLoginUser() {
		return getShiroUser().getUser();
	}
	
	
	/**
	 * 兼容Cas中单纯传入“用户名”身份的场景
	 * 与cas集成时，cas server只返回用户名的身份信息，所以只有两种情况
	 * @return
	 */
	public static ShiroUser getShiroUser() {
		Subject subject = getSubject();
 		ShiroUser shiroUser = null;
		Object obj = subject.getPrincipal();
		//为单独部署使用时
		if(obj != null && obj instanceof ShiroUser){
			shiroUser = (ShiroUser) obj;
		//cas集成时
		}else {
			Session session = subject.getSession();
			if(session != null){
				shiroUser = (ShiroUser) session.getAttribute("shiroUser");
    			if(shiroUser == null){
    				shiroUser = CreateShiroUser(obj, session);
    			}
			}else{
			    shiroUser = CreateShiroUser(obj, null);
			}
		}
		return shiroUser;
	}

    private static ShiroUser CreateShiroUser(Object obj, Session sn) {
        ShiroUser shiroUser;
        String userName = (String) obj;
        shiroUser = new ShiroUser();
        shiroUser.setLoginName(userName);
        User user = shiroUser.getUser();
        if(user == null){
        	/*
        	 * 与cas集成时添加的逻辑
        	 * 根据登录用户名从库中查询出User实体 
        	 */
        	if(shiroUser.getUser() == null){
        		if(shiroUser.getLoginName() != null){
	        		user = userServ.getByUsername(shiroUser.getLoginName());
	        		shiroUser.setUser(user);
	        		shiroUser.setId(user.getId());
        		}
        	}
        }
        if(sn != null)
            sn.setAttribute("shiroUser", shiroUser);
        return shiroUser;
    }
	
	/**
	 * 与cas集成时,从身份信息中获取对应子系统的进入角色信息,
	 *
	 * @param subsysName 子系统
	 * @return 对应角色id
	 */
	public static String getRoleIdBySubSysName(String subsysName){
		Subject subject = getSubject();
		PrincipalCollection  pc = subject.getPrincipals();
		List<Object> list = pc.asList();
		// 由于已经和cas server协商,故可以直接获取
		HashMap<String,String> map = (HashMap<String,String>) list.get(1);
		if(map != null)
			return map.get(subsysName);
		return null;
	}
	
	public static Subject getSubject() {
		return org.apache.shiro.SecurityUtils.getSubject();
	}
	
	/**
	 * 暂时不使用
	 * @return
	 */
	private static ShiroUser getShiroUser2() {
		Subject subject = getSubject();
		ShiroUser shiroUser = (ShiroUser)subject.getPrincipal();
		return shiroUser;
	}

}
