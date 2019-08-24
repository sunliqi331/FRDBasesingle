package com.its.frd.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("all")
public class SpringBeanUtils implements ApplicationContextAware{ 
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

	//隐藏构造器
	public SpringBeanUtils(){}
	
	/**
	 * 获取对象
	 * @param beanName
	 * @return 一个以所给名字注册的bean的实例
	 * @see 1.0
	 */
	public static Object getBean(String beanName){
		return context.getBean(beanName);
	}
	
	/**
	 * 获取对应class的bean名称的对象
	 * @param beanName
	 * @param clazz
	 * @return
	 * @see 1.0
	 */
	public static <T>T getBean(String beanName, Class<T> clazz){
		return (T) context.getBean(beanName, clazz);
	}
	
	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true〉
	 * @param name bean名称
	 * @return
	 * @see 1.0
	 */
	public static boolean containsBean(String name) {
		return context.containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 
	 *     如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 * @see 1.0
	 */
	public static boolean isSingleton(String name) 	throws NoSuchBeanDefinitionException {
		return context.isSingleton(name);
	}

	/**
	 * 得到对应bean的class类型
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 * @see 1.0
	 */
	public static Class getType(String name) throws NoSuchBeanDefinitionException {
		return context.getType(name);
	}

	/**
	 * 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 * @see 1.0
	 */
	public static String[] getAliases(String name)
			throws NoSuchBeanDefinitionException {
		return context.getAliases(name);
	}
	
	/**
     * 获取类型为requiredType的对象
     *
     * @param clz
     * @return
     * @throws org.springframework.beans.BeansException
     *
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        T result = (T) context.getBean(clz);
        return result;
    }
	
}