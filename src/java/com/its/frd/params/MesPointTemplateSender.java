package com.its.frd.params;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;

import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesPointsDao;
import com.its.monitor.service.MesPointsTemplateService;

public class MesPointTemplateSender implements MethodBeforeAdvice, AfterReturningAdvice {

	@Autowired
	private MesPointsTemplateService mesPointTemplateService;
	
	@Autowired
	private MesPointsDao mesPointsDao;
	@Override
	public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {
		// TODO Auto-generated method stub
		if (method.isAnnotationPresent(SendTemplate.class))   
        {  
            //mesPointTemplateService.sendTemplate(mesPointsDao.findAll());
        } 
	}

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		// TODO Auto-generated method stub
		 
	}

}
