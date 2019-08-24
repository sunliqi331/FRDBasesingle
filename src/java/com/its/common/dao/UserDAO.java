package com.its.common.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.entity.main.User;

public interface UserDAO extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	User getByUsername(String username);

	@Query("FROM User M WHERE M.phone=?1")
	User findByPhone(String phone);
	
	List<User> findByOrganizationId(Long id);
	
	@Query("delete from User where username = ?1")
	public void deleteByUsername(String username);
	
	public User findByUsername(String username);
	
}