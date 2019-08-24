package com.its.common.utils;

import com.its.common.shiro.ShiroUser;

public class ShiroUserThreadLocal {
	private static ThreadLocal<ShiroUser> threadLocal = new ThreadLocal<ShiroUser>();
	
	public static ShiroUser getShiroUser(){
		return threadLocal.get();
	}
	
	public static void setShiroUser(ShiroUser shiroUser){
		threadLocal.set(shiroUser);
	}
}
