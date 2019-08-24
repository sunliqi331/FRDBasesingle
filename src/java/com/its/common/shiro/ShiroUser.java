package com.its.common.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.its.common.entity.main.DataControl;
import com.its.common.entity.main.Module;
import com.its.common.entity.main.User;
import com.its.frd.entity.Companyinfo;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {

	private static final long serialVersionUID = -1748602382963711884L;
	private Long id;
	private String loginName;
	private String ipAddress;
	private User user;
	
	private Long companyid; //当前使用的公司
	private Boolean phoneMode = false;
	/**
	 * 登录用户相关的公司信息
	 */
	private List<Companyinfo> companyInfos = new ArrayList<Companyinfo>();
	
	/**
	 * 公司id与公司role对应
	 */
	private Map<Long,Long> CompanyRoleMap = new HashMap<Long,Long>();
	
	public Map<String, Object> menuMap = null;
	
	private Map<String, DataControl> hasDataControls = new HashMap<String, DataControl>();
	private Map<String, Module> hasModules = new HashMap<String, Module>();
	
	/**
	 * 加入更多的自定义参数
	 */
	private Map<String, Object> attribute = new HashMap<String, Object>();
	
	public ShiroUser() {
		
	}
	
	public ShiroUser(String loginName) {
		this.loginName = loginName;
	}
	
	public ShiroUser(Long id, String loginName) {
		this.id = id;
		this.loginName = loginName;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the hasDataControls
	 */
	public Map<String, DataControl> getHasDataControls() {
		return hasDataControls;
	}

	/**
	 * @param hasDataControls the hasDataControls to set
	 */
	public void setHasDataControls(Map<String, DataControl> hasDataControls) {
		this.hasDataControls = hasDataControls;
	}

	/**
	 * @return the hasModules
	 */
	public Map<String, Module> getHasModules() {
		return hasModules;
	}

	/**
	 * @param hasModules the hasModules to set
	 */
	public void setHasModules(Map<String, Module> hasModules) {
		this.hasModules = hasModules;
	}
	
	public void setAttribute(String name, Object value) {
		attribute.put(name, value);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return attribute.get(name);
	}
	
	public Object removeAttribute(String name) {
		return attribute.remove(name);
	}
	
	public Map<String, Object> getAttributes() {
		return this.attribute;
	}

	public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	public Map<Long, Long> getCompanyRoleMap() {
		return CompanyRoleMap;
	}

	public void setCompanyRoleMap(Map<Long, Long> companyRoleMap) {
		CompanyRoleMap = companyRoleMap;
	}

	public List<Companyinfo> getCompanyInfos() {
		return companyInfos;
	}

	public void setCompanyInfos(List<Companyinfo> companyInfos) {
		this.companyInfos = companyInfos;
	}

	public Map<String, Object> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<String, Object> menuMap) {
		this.menuMap = menuMap;
	}

	public Boolean getPhoneMode() {
		return phoneMode;
	}

	public void setPhoneMode(Boolean phoneMode) {
		this.phoneMode = phoneMode;
	}
	
	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	/*@Override
	public String toString() {
		return loginName;
	}*/
}