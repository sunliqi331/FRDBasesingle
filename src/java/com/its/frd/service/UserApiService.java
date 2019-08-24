package com.its.frd.service;

import java.util.List;

import com.its.common.entity.main.User;

/**
 * 主要用于对商城提供服务
 *
 */
public interface UserApiService {
	/**
	 * user中的数据只有,用户名,密码,手机号,邮箱
	 * 在保存user信息后需要给该用户平台的普通role
	 * @param user
	 * @return
	 */
	public boolean saveUser(User user);
	/**
	 * 只修改username相同中的手机号,邮箱,密码
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user);
	/**
	 * 根据username删除
	 * @param username
	 * @return
	 */
	public boolean deleteUserByUserName(String username);
	
	public boolean updateStatusByUser(String username,String status);
	
	public boolean batchDeleteUser(List<User> users);
}
