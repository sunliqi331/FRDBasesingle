package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.stereotype.Service;

import com.its.common.dao.UserDAO;
import com.its.common.entity.main.Role;
import com.its.common.entity.main.User;
import com.its.common.entity.main.UserRole;
import com.its.common.service.RoleService;
import com.its.common.service.UserRoleService;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Friendgroup;
import com.its.frd.entity.Friends;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.entity.Usercompanys;
import com.its.frd.entity.Userdepartment;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.FriendsService;
import com.its.frd.service.MesUserPositionService;
import com.its.frd.service.UserApiService;
import com.its.frd.service.UserCompanyroleService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.service.UserdepartmentService;

@Service
public class UserApiServiceImp implements UserApiService {
	@Resource
	private UserDAO userDao;
	@Resource
	private RoleService roleServ;
	@Resource
	private UserRoleService userRoleServ;
	@Resource
	private UserCompanyroleService userCompanyroleService;
	@Resource
	private CompanyinfoService companyinfoService;
	@Resource
	private UsercompanysService ucServ;
	@Resource
	private MesUserPositionService mupServ;
	@Resource
	private UserdepartmentService udServ;
	@Resource
	private FriendsService friendsServ;

	@Override
	public boolean saveUser(User user) {
		try{
			//新增的用户需要检验user信息的有效性
			if(!this.checkUserIsNULL(user))
				return false;
			User u = userDao.save(user);
			if(u == null)
				return false;
			// 将已激活用户角色添加给用户
			Role role = (roleServ.get((long) 12));
			UserRole userRole = new UserRole();
			userRole.setUser(u);
			userRole.setRole(role);
			userRoleServ.saveOrUpdate(userRole);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * 验证user中用户名,手机号,邮箱,密码是否有值
	 * @param user
	 * @return
	 */
	private boolean checkUserIsNULL(User user){
		if(StringUtils.isBlank(user.getPhone())||
				StringUtils.isBlank(user.getPassword())||
				StringUtils.isBlank(user.getEmail())||
				StringUtils.isBlank(user.getUsername()))
			return false;
		return true;
	}

	@Override
	public boolean updateUser(User user) {
		try {
			if(user == null || StringUtil.isEmpty(user.getUsername()))
				return false;

			User u = userDao.getByUsername(user.getUsername());
			if(u == null)	
				return false;
			//手机
			if(StringUtils.isNotBlank(user.getPhone()))
				u.setPhone(user.getPhone());
			//密码
			if(StringUtils.isNotBlank(user.getPassword()))
				u.setPassword(user.getPassword());
			//邮箱
			if(StringUtils.isNotBlank(user.getEmail()))
				u.setEmail(user.getEmail());

			userDao.save(u);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteUserByUserName(String username) {
		try {
			if(!StringUtils.isNotBlank(username))
				return false;
			User user = userDao.getByUsername(username);
			if(user != null){
				//删除user与公司角色关联
				List<UserCompanyrole> ucRole = userCompanyroleService.findByUserId(user.getId());
				if(ucRole.size() > 0)
					userCompanyroleService.deleteByUserId(user.getId());
				//将user创建的公司状态置为3
				List<Companyinfo> company = companyinfoService.findByUserid(user.getId());
				if(company.size() > 0){
					for(Companyinfo cp : company){
						cp.setCompanystatus("3");
						companyinfoService.saveOrUpdate(cp);
					}
				}
				//删除user与公司的关联
				List<Usercompanys> uc = ucServ.findByUserid(user.getId());
				if(uc.size() > 0){
					for(Usercompanys ucs : uc){
						ucServ.deleteinfoByUserIdAndCompanyId(user.getId(), ucs.getCompanyinfo().getId());
						mupServ.deleteinfoByUserIdAndCompanyId(user.getId(), ucs.getCompanyinfo().getId());
						List<Userdepartment> ud = udServ.findByUserid(user.getId());
						for(Userdepartment uds: ud){
							udServ.deleteById(uds.getId());
						}
					}
				}
				//删除user的好友关系
				List<Friends> friend = friendsServ.findMyFriends(user.getId());
				if(friend.size() > 0){
					for(Friends fr : friend){
						friendsServ.deleteFriends(fr);
					}
				}
				//删除user的group关系
				List<Friendgroup> group = friendsServ.findList(user.getId());
				if(group.size() > 0){
					for(Friendgroup groups : group){
						friendsServ.delGroup(groups);
					}
				}
				userDao.delete(user.getId());
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean batchDeleteUser(List<User> users) {
		try {
			userDao.delete(users);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateStatusByUser(String username, String status) {
		try {
			//userDao.updateStatusByUsername(username, status);
			User user = userDao.findByUsername(username);
			if(user != null){
				user.setStatus(status);
				userDao.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
