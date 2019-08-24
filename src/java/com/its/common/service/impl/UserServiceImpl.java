package	com.its.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.dao.UserDAO;
import com.its.common.entity.main.User;
import com.its.common.exception.ExistedException;
import com.its.common.exception.IncorrectPasswordException;
import com.its.common.exception.NotDeletedException;
import com.its.common.exception.ServiceException;
import com.its.common.service.UserService;
import com.its.common.shiro.ShiroCasRealm;
import com.its.common.shiro.ShiroDbRealm;
import com.its.common.shiro.ShiroDbRealm.HashPassword;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.utils.SecurityUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDAO userDAO;
	
	/*
	 * 单独部署时使用
	 
	@Autowired
	private ShiroDbRealm shiroRealm;*/
	/*
	 * 与cas集成时使用
	 */
	@Resource
	private ShiroCasRealm shiroRealm;
	

	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.UserService#get(java.lang.Long)  
	 */ 
	@Override
	public User get(Long id) {
		return userDAO.findOne(id);
	}

	/*
	 * (non-Javadoc) 
	 * @see com.ketayao.ketacustom.service.UserService#saveOrUpdate(com.ketayao.ketacustom.entity.main.User) 
	 * 
	 */
	@Override
	public void saveOrUpdate(User user) {
		if (user.getId() == null) {
			if (userDAO.getByUsername(user.getUsername()) != null) {
				throw new ExistedException("用户名：" + user.getUsername() + "已存在。");
			}
			
			//设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
			/*if (StringUtils.isNotBlank(user.getPlainPassword()) && shiroRealm != null) {
				HashPassword hashPassword = ShiroDbRealm.encryptPassword(user.getPlainPassword());
				user.setSalt(hashPassword.salt);
				user.setPassword(hashPassword.password);
			}*/
		}
		//使用MD5加密
		if (StringUtils.isNotBlank(user.getPassword()) && shiroRealm != null) {
			com.its.frd.util.MD5 md5 = new com.its.frd.util.MD5();
			user.setPassword(md5.getMD5ofStr(user.getPassword()));
		}
		//userDAO.save(user);
		userDAO.saveAndFlush(user);
		//标注
		//userDAO.flush();
		shiroRealm.clearCachedAuthorizationInfo(user.getUsername());
	}

	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.UserService#delete(java.lang.Long)  
	 */
	@Override
	public void delete(Long id) {
		if (isSupervisor(id)) {
			logger.warn("操作员{}，尝试删除超级管理员用户", SecurityUtils.getSubject()
					.getPrincipal() + "。");
			throw new NotDeletedException("不能删除超级管理员用户。");
		}
		User user = userDAO.findOne(id);
		userDAO.delete(user.getId());
		
		// TODO 从shiro中注销
		shiroRealm.clearCachedAuthorizationInfo(user.getUsername());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.UserService#findAll(com.ketayao.ketacustom.util.dwz.Page)  
	 */
	@Override
	public List<User> findAll(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<User> springDataPage = userDAO.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.UserService#findByExample(org.springframework.data.jpa.domain.Specification, com.ketayao.ketacustom.util.dwz.Page)	
	 */
	@Override
	public List<User> findByExample(
			Specification<User> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<User> springDataPage = userDAO.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<User> findByExample(Specification<User> specification) {
		return userDAO.findAll(specification);
	}
	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id) {
		return id == 1;
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.UserService#updatePwd(com.ketayao.ketacustom.entity.main.User, java.lang.String)
	 */
	@Override
	public void updatePwd(User user, String newPwd) throws ServiceException {
		//设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
		boolean isMatch = ShiroDbRealm.validatePassword(user.getPlainPassword(), user.getPassword(), user.getSalt());
		if (isMatch) {
			HashPassword hashPassword = ShiroDbRealm.encryptPassword(newPwd);
			user.setSalt(hashPassword.salt);
			user.setPassword(hashPassword.password);
			
			userDAO.save(user);
			shiroRealm.clearCachedAuthorizationInfo(user.getUsername());
			
			return; 
		}
		
		throw new IncorrectPasswordException("用户密码错误！");
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.UserService#resetPwd(com.ketayao.ketacustom.entity.main.User, java.lang.String)
	 */
	@Override
	public void resetPwd(User user, String newPwd) {
		if (newPwd == null) {
			newPwd = "123456";
		}
		
		HashPassword hashPassword = ShiroDbRealm.encryptPassword(newPwd);
		user.setSalt(hashPassword.salt);
		user.setPassword(hashPassword.password);
		
		userDAO.save(user);
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.UserService#getByUsername(java.lang.String)
	 */
	@Override
	public User getByUsername(String username) {
		return userDAO.getByUsername(username);
	}

	@Override
	public User findById(Long id) {
		return userDAO.findOne(id);
	}

	@Override
	public User findByPhone(String phone) {
		return userDAO.findByPhone(phone);
	}

	@Override
	public void saveMoreUser(List<User> users,String phone) {
		User u = null;
		for(User user : users){
			u = userDAO.findByUsername(user.getUsername());
			if(u != null)
				u.setPhone(phone);;
			userDAO.save(u);
		}
		
	}

	@Override
	public void saveOrUpdate2(User user) {
		// TODO Auto-generated method stub
		if (user.getId() == null) {
			if (userDAO.getByUsername(user.getUsername()) != null) {
				throw new ExistedException("用户名：" + user.getUsername() + "已存在。");
			}
			
			//设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
			/*if (StringUtils.isNotBlank(user.getPlainPassword()) && shiroRealm != null) {
				HashPassword hashPassword = ShiroDbRealm.encryptPassword(user.getPlainPassword());
				user.setSalt(hashPassword.salt);
				user.setPassword(hashPassword.password);
			}*/
		}
		//使用MD5加密
				
		
		//userDAO.save(user);
		userDAO.saveAndFlush(user);
		//标注
		//userDAO.flush();
		shiroRealm.clearCachedAuthorizationInfo(user.getUsername());
	}
	

	@Override
	public void saveOrUpdate3(User user) {
		// TODO Auto-generated method stub
		
	}

}
