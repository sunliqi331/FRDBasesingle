package com.its.common.util.persistence;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个annotation主要用于切换数据源的,在service方法上进行注解(目前仅在com.its.*.service下的包有效,详见applicationContext.xml中的切面配置)
 * @author Administrator
 *
 */

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
@Documented
public @interface DataSource {  
	String name() default DataSource.master;  
	
	public static String master = "dataSource";
	 
    public static String slave1 = "dataSource_postgre";
 
}  