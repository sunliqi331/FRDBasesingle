package com.its.frd.util;

import java.util.ResourceBundle;
public class ResourceUtil {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("config");
	
	/**
	* @Title: readPropertiesValue
	* @Description: 根据key读取properties属性值
	* @param filename 属性名
	* @param key properties的key
	* @return String 返回properties的值
	 */
	public static String  readPropertiesValue(String filename,String key){
		   ResourceBundle bundle = java.util.ResourceBundle.getBundle(filename);
	       try {
	            return bundle.getString(key);
	       } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	       }
	}
	
	/**
	 * 读取默认comfig.properties文件的值
	 * @param key
	 * @return
	 */
	public static String getValueForDefaultProperties(String key){
		return bundle.getString(key);
	}

	/**
	 * 获得sessionInfo名字
	 * 
	 * @return
	 */
	public static final String getSessionInfoName() {
		return bundle.getString("sessionInfoName");
	}

	/**
	 * 获得上传文件要放到那个目录
	 * 
	 * @return
	 */
	public static final String getUploadDirectory() {
		return bundle.getString("file_Path");
	}
	
	public static final String getDefaultPwd() {
		return bundle.getString("defaultPwd");
	}

}
