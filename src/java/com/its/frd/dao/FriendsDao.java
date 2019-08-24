package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.Friends;

public interface FriendsDao extends 
				JpaRepository<Friends, Long>,
				JpaSpecificationExecutor<Friends>{
	public Friends findByUseridAndFriendid(Long userid, Long friendid);

	public List<Friends> findByUserid(long id);
	
}
