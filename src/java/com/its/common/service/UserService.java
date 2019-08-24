package com.its.common.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.User;
import com.its.common.util.dwz.Page;

public interface UserService {
	User get(Long id);

	void saveOrUpdate(User user);
	 
	void saveOrUpdate2(User user);
    
	void saveOrUpdate3(User user);
	
	void delete(Long id);
	
	List<User> findAll(Page page);
	
	List<User> findByExample(Specification<User> specification, Page page);
	
	List<User> findByExample(Specification<User> specification);
	
	void updatePwd(User user, String newPwd);
	
	void resetPwd(User user, String newPwd);
	
	User getByUsername(String username);
	
	User findById(Long id);
	
	User findByPhone(String phone);
	
	public void saveMoreUser(List<User> users,String phone);
}
