package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.Friendgroup;

public interface FriendgroupDao extends 
				JpaRepository<Friendgroup, Long>,
				JpaSpecificationExecutor<Friendgroup>{
	
	/**
	 * 通过userid查询群组
	 * @param userid
	 * @return
	 */
	public List<Friendgroup> findByUserid(Long userid);
	
	public Friendgroup findByUseridAndGroupid(Long userid,Long groupid);
	
	public List<Friendgroup> findByGroupid(Long groupid);
}
