package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.entity.main.User;
import com.its.common.util.dwz.Page;
import com.its.frd.entity.Friendgroup;
import com.its.frd.entity.Friends;
import com.its.frd.entity.Group;

public interface FriendsService {
	/**
	 * 查询好友列表
	 * @param specification
	 * @param page
	 * @return
	 */
	public List<Friends> findPage(Specification<Friends> specification,Page page);
	/**
	 * 查询好友列表
	 * @param specification
	 * @param page
	 * @return
	 */
	public List<Group> findGroupPage(Specification<Friendgroup> specification,Page page);
	
	public void saveFriend(Friends fd);
	/**
	 * 查询userid的群组
	 * @param userid 用户id
	 * @return
	 */
	public List<Friendgroup> findList(Long userid);
	
	public void saveGroup(Friendgroup group);
	
	public void deleteFriends(Friends fd);
	
	public void delGroup(Friendgroup group);
	
	public Friends findFriendRelationship(Long userid,Long friendid);
	
	public List<Friends> findMyFriends(long id);
	public List<User> findGroupMembers(Long groupId);
}
