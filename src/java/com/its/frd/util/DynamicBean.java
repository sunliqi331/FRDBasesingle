package com.its.frd.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

public class DynamicBean {  

	/** 
	 * 实体Object 
	 */  
	private  Object object = null;  

	/** 
	 * 属性map 
	 */  
	private  BeanMap beanMap = null;  

	public DynamicBean() {  
		super();  
	}  

	public DynamicBean(Map<String,Class<? extends Object>> propertyMap) {  
		this.object = generateBean(propertyMap);  
		this.beanMap = BeanMap.create(this.object);  
	}  

	/** 
	 * 给bean属性赋值 
	 * @param property 属性名 
	 * @param value 值 
	 */  
	public void setValue(String property, Object value) {  
		beanMap.put(property, value);  
	}  

	/** 
	 * 通过属性名得到属性值 
	 * @param property 属性名 
	 * @return 值 
	 */  
	public Object getValue(String property) {  
		return beanMap.get(property);  
	}  

	/** 
	 * 得到该实体bean对象 
	 * @return 
	 */  
	public Object getObject() {  
		return this.object;  
	}  

	/** 
	 * @param propertyMap 
	 * @return 
	 */  
	private Object generateBean(Map<String,Class<? extends Object>> propertyMap) {  
		BeanGenerator generator = new BeanGenerator();  
		BeanGenerator.addProperties(generator, propertyMap);
		return generator.create();  
	}  

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {  
		  List<Object> list = new ArrayList<>();
		  for(int i = 0;i<10;i++){
            // 设置类成员属性  
            Map<String,Class<? extends Object>> propertyMap = new HashMap<String,Class<? extends Object>>();  
      
            propertyMap.put("id", Class.forName("java.lang.Integer"));  
      
            propertyMap.put("name", Class.forName("java.lang.String"));  
      
            propertyMap.put("address", Class.forName("java.lang.String"));  
      
            // 生成动态 Bean  
            DynamicBean bean = new DynamicBean(propertyMap);  
      
            // 给 Bean 设置值  
            bean.setValue("id", new Integer(123));  
      
            bean.setValue("name", "454");  
      
            bean.setValue("address", "789");  
            list.add(bean);
		  }
		  
		  
		  for(Object dynamicBean : list){
		      DynamicBean bean = (DynamicBean)dynamicBean;
		      Object obj2 = bean.getObject();
              Class clazz2 = obj2.getClass();  
              Method[] methods2 = clazz2.getDeclaredMethods();  
              for (int j = 0; j < methods2.length; j++) {  
                  System.out.println(methods2[j].getName());   
                  if(methods2[j].getName().indexOf("get") != -1){
                      Method m2 = clazz2.getMethod(methods2[j].getName(),null);
                      System.out.println(m2.invoke(obj2, null));
                  }
              }  
                  
		  }
		  
		  
        // 从 Bean 中获取值，当然了获得值的类型是 Object  
  
      /*  System.out.println("  >> id      = " + bean.getValue("id"));  
  
        System.out.println("  >> name    = " + bean.getValue("name"));  
  
        System.out.println("  >> address = " + bean.getValue("address"));  */
  
        // 获得bean的实体  
        /*Object object = bean.getObject();  
        System.out.println("getC".indexOf("get"));
        // 通过反射查看所有方法名  
        Class clazz = object.getClass();  
        Method[] methods = clazz.getDeclaredMethods();  
        for (int i = 0; i < methods.length; i++) {  
            System.out.println(methods[i].getName());   
            if(methods[i].getName().indexOf("get") == 0){
                Method m = clazz.getMethod(methods[i].getName(),null);
                System.out.println(m.invoke(object, null));
            }
        }  */
    }  
}  
