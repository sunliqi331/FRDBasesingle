package com.its.common.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.its.common.entity.main.DataControl;
import com.its.common.entity.main.Permission;
import com.its.common.entity.main.Role;
import com.its.common.entity.main.RolePermission;
import com.its.common.entity.main.RolePermissionDataControl;
import com.its.common.entity.main.UserRole;
import com.its.common.service.RoleService;
import com.its.common.service.UserRoleService;
import com.its.common.service.UserService;
import com.its.common.util.dwz.Page;
import com.its.common.utils.Digests;
import com.its.common.utils.Encodes;
import com.its.frd.entity.MesCompanyRole;
import com.its.frd.entity.MesCompanyRolePermission;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.service.MesCompanyRolePermissionService;
import com.its.frd.service.MesCompanyRoleService;
import com.its.frd.service.UserCompanyroleService;

public class ShiroCasRealm extends CasRealm{
	private static final Logger log = LoggerFactory.getLogger(ShiroCasRealm.class);
	private static final int INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private static final String ALGORITHM = "SHA-1";

	// 是否启用超级管理员
	protected boolean activeRoot = false;
	
	// 是否使用验证码
	protected boolean useCaptcha = false;
	protected UserService userService;
	protected RoleService roleService;
	protected UserRoleService userRoleService;
	
	private MesCompanyRoleService companyRoleServ;
	private MesCompanyRolePermissionService companyPermissionServ;
	private UserCompanyroleService userCompanyroleServ;
	
	private PrincipalCollection principals;
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		this.principals = principals;
		Collection<?> collection = principals.fromRealm(getName());
		if (collection == null || collection.isEmpty()) {
			return null;
		}
		/**
		 * 1.先判断shiroUser中companyid是否有值
		 * 		若有值则,从查询和companyrole相关的权限.
		 * 2.拼接权限
		 */
		
		//ShiroUser shiroUser = (ShiroUser) collection.iterator().next();
		ShiroUser shiroUser = com.its.common.utils.SecurityUtils.getShiroUser();
		// 设置、更新User
		//shiroUser.setUser(userService.get(shiroUser.getId()));
		return newAuthorizationInfo(shiroUser);
	}
	
	private SimpleAuthorizationInfo newAuthorizationInfo(ShiroUser shiroUser) {
		Collection<String> hasPermissions = null;
		Collection<String> hasRoles = new HashSet<String>();
		
		// 是否启用超级管理员 && id==1为超级管理员，构造所有权限 
		if (activeRoot && shiroUser.getId() == 1) {
			
			Page page = new Page();
			page.setNumPerPage(Integer.MAX_VALUE);
			List<Role> roles = roleService.findAll(page);
			      
			for (Role role : roles) {
				hasRoles.add(role.getName());
			}
			
			hasPermissions = new HashSet<String>();
			hasPermissions.add("*");
			
			if (log.isInfoEnabled()) {
				log.info("使用了" + shiroUser.getLoginName() + "的超级管理员权限:" + "。At " + new Date());
				log.info(shiroUser.getLoginName() + "拥有的角色:" + hasRoles);
				log.info(shiroUser.getLoginName() + "拥有的权限:" + hasPermissions);
			}
		} else {
			/**
			 * 1.此处需区分平台权限和公司权限
			 * 2.当shiroUser中的companyid有值时,则从companyPerssmion中取权限的数据
			 * 3.当是手机段登录的系统,则需要在浏览MES系统时,只需要展示手机段权限的资源
			 */
			
			if(shiroUser.getCompanyid() != null){
				//查询当前用户对应当前公司的是什么role
				//List<UserCompanyrole> userCompanyroles = userCompanyroleServ.findByUserId(shiroUser.getUser().getId());
				List<UserCompanyrole> userCompanyroles = 
						userCompanyroleServ.findByUserIdAndMesCompanyRoleCompanyid(shiroUser.getUser().getId(),shiroUser.getCompanyid());
				List<MesCompanyRole> companyRoles = new ArrayList<MesCompanyRole>();
				//对应当前用户当前公司的角色具体权限
				List<MesCompanyRolePermission> companyUserPermissions = new ArrayList<MesCompanyRolePermission>();
				for(UserCompanyrole userCompanyrole : userCompanyroles){
					MesCompanyRole companyRole = 
							companyRoleServ.findById(userCompanyrole.getMesCompanyRole().getId());
					
					if(companyRole != null){
						companyRoles.add(companyRole);
						companyUserPermissions.addAll(companyRole.getRolePermissions());
					}
				}
				
				//非手机端登录
				if(!shiroUser.getPhoneMode()){
					//拼装权限字符串
					hasPermissions = this.makeCompanyPermissions(companyUserPermissions, shiroUser);
					hasRoles = makeCompanyRoles(companyRoles, shiroUser);
					
				//手机端登录
				}else{
					/*
					 * 此处需要判断手机端权限中的permission是否在该用户在该公司下的role中有相同的permission
					 * 1.查出手机端role
					 * 2.查出该用户在该公司下的role
					 * 3.循环手机端的permission,不匹配公司role中permission的过滤掉
					 */
					//查询出role.固定死的手机端权限 
					Role role = roleService.get(19L);
					//手机端的permission
					List<RolePermission> rolePermissions = role.getRolePermissions();
					List<RolePermission> newRolePermissions = new ArrayList<>();
					for(RolePermission rolePermission : rolePermissions){
						for(MesCompanyRolePermission companyRolePermission : companyUserPermissions){
							if(rolePermission.getPermission().getId().compareTo(companyRolePermission.getPermission().getId()) == 0){
								newRolePermissions.add(rolePermission);
								break;
							}
						}
					}
					role.setRolePermissions(newRolePermissions);
					hasRoles.add(role.getName());
					//查询出permission
					hasPermissions = this.makePhonePermissions(role, shiroUser);
				}
				
			}else{
				List<UserRole> userRoles = userRoleService.findByUserId(shiroUser.getId());
				//去掉组织部分
				Collection<Role> roles = getUserRoles(userRoles);
				hasRoles = makeRoles(roles, shiroUser);
				hasPermissions = makePermissions(roles, shiroUser);
			}
		}
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRoles(hasRoles);
		info.addStringPermissions(hasPermissions);
		
		return info;
	}
	
	private Collection<Role> getUserRoles(List<UserRole> userRoles) {
		Set<Role> roles = new HashSet<Role>();
		for (UserRole userRole : userRoles) {
			roles.add(userRole.getRole());
		}
		return roles;
	}
	
	/**
	 * 组装手机端资源操作权限
	 * @param roles
	 * @param shiroUser
	 * @return
	 */
	private Collection<String> makePhonePermissions(Role role, ShiroUser shiroUser) {
		// 清空shiroUser中map
		shiroUser.getHasDataControls().clear();
		shiroUser.getHasModules().clear();
		
		Collection<String> stringPermissions = new ArrayList<String>();
		if(role == null)
			return null;
		List<RolePermission> rolePermissions = role.getRolePermissions();
		for (RolePermission rolePermission : rolePermissions) {
			Permission permission = rolePermission.getPermission();
			
			String resource = permission.getModule().getSn();
			String operate = permission.getSn();
	
			StringBuilder builder = new StringBuilder();
			builder.append(resource + ":" + operate);
			
			shiroUser.getHasModules().put(resource, permission.getModule());
			
			StringBuilder dcBuilder = new StringBuilder();
			for (RolePermissionDataControl rpdc : rolePermission.getRolePermissionDataControls()) {
				DataControl dataControl = rpdc.getDataControl();
				dcBuilder.append(dataControl.getName() + ",");
				
				shiroUser.getHasDataControls().put(dataControl.getName(), dataControl);
			}
			
			if (dcBuilder.length() > 0) {
				builder.append(":" + dcBuilder.substring(0, dcBuilder.length() - 1));
			}
			
			stringPermissions.add(builder.toString());
		}

        if (log.isInfoEnabled()) {
            log.info(shiroUser.getLoginName() + "拥有的权限:" + stringPermissions);
        }
        return stringPermissions;
    }
	
	/**
	 * 组装公司角色资源操作权限
	 * @param roles
	 * @param shiroUser
	 * @return
	 */
	private Collection<String> makeCompanyPermissions(
			Collection<MesCompanyRolePermission> companyPermissions, ShiroUser shiroUser){
		// 清空shiroUser中map
		shiroUser.getHasDataControls().clear();
		shiroUser.getHasModules().clear();
		
		Collection<String> stringPermissions = new ArrayList<String>();
		for (MesCompanyRolePermission rolePermission : companyPermissions) {
			Permission permission = rolePermission.getPermission();
			String resource = permission.getModule().getSn();
			String operate = permission.getSn();
	
			StringBuilder builder = new StringBuilder();
			builder.append(resource + ":" + operate);
			shiroUser.getHasModules().put(resource, permission.getModule());
			
			StringBuilder dcBuilder = new StringBuilder();
			/*for (RolePermissionDataControl rpdc : rolePermission.getRolePermissionDataControls()) {
				DataControl dataControl = rpdc.getDataControl();
				dcBuilder.append(dataControl.getName() + ",");
				
				shiroUser.getHasDataControls().put(dataControl.getName(), dataControl);
			}*/
			
			if (dcBuilder.length() > 0) {
				builder.append(":" + dcBuilder.substring(0, dcBuilder.length() - 1));
			}
			
			stringPermissions.add(builder.toString());
		}

        if (log.isInfoEnabled()) {
            log.info(shiroUser.getLoginName() + "拥有的权限:" + stringPermissions);
        }
        return stringPermissions;
	}
	
	/**
	 * 组装公司角色权限
	 * @param roles
	 * @param shiroUser
	 * @return
	 */
	private Collection<String> makeCompanyRoles(Collection<MesCompanyRole> roles, ShiroUser shiroUser) {
		Collection<String> hasRoles = new HashSet<String>();
		for (MesCompanyRole role : roles) {
			if(role != null)
				hasRoles.add(role.getName());
		}

		if (log.isInfoEnabled()) {
			log.info(shiroUser.getLoginName() + "拥有的角色:" + hasRoles);
		}
		return hasRoles;
	}
	
	/**
	 * 组装角色权限
	 * @param roles
	 * @param shiroUser
	 * @return
	 */
	private Collection<String> makeRoles(Collection<Role> roles, ShiroUser shiroUser) {
		Collection<String> hasRoles = new HashSet<String>();
		for (Role role : roles) {
			if(role != null)
				hasRoles.add(role.getName());
		}

		if (log.isInfoEnabled()) {
			log.info(shiroUser.getLoginName() + "拥有的角色:" + hasRoles);
		}
		return hasRoles;
	}
	
	
	
	/**
	 * 组装资源操作权限
	 * @param roles
	 * @param shiroUser
	 * @return
	 */
	private Collection<String> makePermissions(Collection<Role> roles, ShiroUser shiroUser) {
		// 清空shiroUser中map
		shiroUser.getHasDataControls().clear();
		shiroUser.getHasModules().clear();
		
		Collection<String> stringPermissions = new ArrayList<String>();
		for (Role role : roles) {
			if(role == null) {
				continue;				
			}
			//在平台只展示平台用户的权限
			if((!"平台用户".equals(role.getName())) && (!"平台管理员".equals(role.getName()))) {
				continue;
			}
			List<RolePermission> rolePermissions = role.getRolePermissions();
			for (RolePermission rolePermission : rolePermissions) {
				Permission permission = rolePermission.getPermission();
				
				String resource = permission.getModule().getSn();
				String operate = permission.getSn();
		
				StringBuilder builder = new StringBuilder();
				builder.append(resource + ":" + operate);
				
				shiroUser.getHasModules().put(resource, permission.getModule());
				
				StringBuilder dcBuilder = new StringBuilder();
				for (RolePermissionDataControl rpdc : rolePermission.getRolePermissionDataControls()) {
					DataControl dataControl = rpdc.getDataControl();
					dcBuilder.append(dataControl.getName() + ",");
					
					shiroUser.getHasDataControls().put(dataControl.getName(), dataControl);
				}
				
				if (dcBuilder.length() > 0) {
					builder.append(":" + dcBuilder.substring(0, dcBuilder.length() - 1));
				}
				
				stringPermissions.add(builder.toString());
			}
		}

        if (log.isInfoEnabled()) {
            log.info(shiroUser.getLoginName() + "拥有的权限:" + stringPermissions);
        }
        return stringPermissions;
    }
	
	public static class HashPassword {
		public String salt;
		public String password;
	}
	
	public static HashPassword encryptPassword(String plainPassword) {
		HashPassword result = new HashPassword();
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		result.salt = Encodes.encodeHex(salt);

		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, INTERATIONS);
		result.password = Encodes.encodeHex(hashPassword);
		return result;
	}
	
	/**
	 * 
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @param salt 盐值
	 * @return
	 */
	public static boolean validatePassword(String plainPassword, String password, String salt) {
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), Encodes.decodeHex(salt), INTERATIONS);
		String oldPassword = Encodes.encodeHex(hashPassword);
		return password.equals(oldPassword);
	}
	
	/* 
	 * 覆盖父类方法，设置AuthorizationCacheKey为ShiroUser的loginName，这样才能顺利删除shiro中的缓存。
	 * 因为shiro默认的AuthorizationCacheKey为PrincipalCollection的对象。
	 * @see org.apache.shiro.realm.AuthorizingRealm#getAuthorizationCacheKey(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		return principals.getPrimaryPrincipal();
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String loginName) {
		ShiroUser shiroUser = new ShiroUser(loginName);
		
		SimplePrincipalCollection principals = new SimplePrincipalCollection(shiroUser, getName());
		clearCachedAuthorizationInfo(principals);
	}
	
	/**
	 * 清除当前用户的授权
	 */
	public void clearCurrentUserAuthorizationInfoCache(){
		//if(principals != null)
			//super.clearCachedAuthorizationInfo(principals);
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);

	}
	
	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
	
	/**  
	 * 设置 isActiveRoot 的值  
	 * @param isActiveRoot
	 */
	public void setActiveRoot(boolean activeRoot) {
		this.activeRoot = activeRoot;
	}

	/**  
	 * 设置 useCaptcha 的值  
	 * @param useCaptcha
	 */
	public void setUseCaptcha(boolean useCaptcha) {
		this.useCaptcha = useCaptcha;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**  
	 * 设置 userRoleService 的值  
	 * @param userRoleService
	 */
	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	/**
	 * @param roleService the roleService to set
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setCompanyRoleServ(MesCompanyRoleService companyRoleServ) {
		this.companyRoleServ = companyRoleServ;
	}

	public void setCompanyPermissionServ(MesCompanyRolePermissionService companyPermissionServ) {
		this.companyPermissionServ = companyPermissionServ;
	}

	public void setUserCompanyroleServ(UserCompanyroleService userCompanyroleServ) {
		this.userCompanyroleServ = userCompanyroleServ;
	}
}
