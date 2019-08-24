package com.its.frd.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;

public class BeanCopyUtil {
	/**
	 * 只复制简单类型
	 * 
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object CopySimplePropertValue(Object object,Object org) throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class classType = object.getClass();
		System.out.println("Class:" + classType.getName());
		Object objectCopy = org;//classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
		// 获得对象的所有属性
		Field fields[] = classType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			System.out.println("field.getType() : "+field.getType());
			if (field.getType().equals(java.lang.String.class) 
					|| field.getType().equals(java.lang.Long.class)
					|| field.getType().equals(java.sql.Timestamp.class)
					|| field.getType().equals(java.lang.Double.class)
					|| field.getType().equals(java.lang.Integer.class)) {
				System.out.println("开始Set值");
				String fieldName = field.getName();
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				// 获得和属性对应的getXXX（）方法的名字
				String getMethodName = "get" + firstLetter + fieldName.substring(1);
				// 获得和属性对应的setXXX()方法的名字
				String setMethodName = "set" + firstLetter + fieldName.substring(1);

				// 获得和属性对应的getXXX()方法
				Method getMethod = classType.getMethod(getMethodName, new Class[] {});
				// 获得和属性对应的setXXX()方法
				Method setMethod = classType.getMethod(setMethodName, new Class[] { field.getType() });

				// 调用原对象的getXXX()方法
				Object value = getMethod.invoke(object, new Object[] {});
				System.out.println(fieldName + ":" + value);
				// 调用复制对象的setXXX()方法
				setMethod.invoke(org, new Object[] { value });
			}
		}
		return org;

	}
	
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Companyinfo md = new Companyinfo();
		Companyinfo md2 = null;
		Companyinfo info = new Companyinfo();
		/*info.setCompanyname("xxx");
		md.setCompanyinfo(info);*/
		md.setCompanyname("xxx");
		md2 = (Companyinfo) CopySimplePropertValue(md,new Companyinfo());
		System.out.println(md2.getCompanyname());
	}


}
