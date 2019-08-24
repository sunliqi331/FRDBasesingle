package com.its.common.util.persistence;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

public class DataSourceExchange implements MethodBeforeAdvice,AfterReturningAdvice{

	/*@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		DataSource dataSource = invocation.getMethod().is.getAnnotation(DataSource.class); 
		DynamicDataSourceHolder.setDataSourceType(dataSource.name());
		try {
			invocation.proceed();
		} catch (Exception ex) { 
		}
		return null;
	}*/

	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
		// TODO Auto-generated method stub
		DynamicDataSourceHolder.clearDataSourceType();  
	}

	/**
	 * service方法执行前,进行AOP横切,查看该方法的数据源是否手动指定,并注入该设定的数据源
	 */
	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		// TODO Auto-generated method stub
		if (method.isAnnotationPresent(DataSource.class))   
        {  
            DataSource datasource = method.getAnnotation(DataSource.class);  
            DynamicDataSourceHolder.setDataSourceType(datasource.name());  
        }  
	}

}
