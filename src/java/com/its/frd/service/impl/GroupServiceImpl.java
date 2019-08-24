package com.its.frd.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.dao.UserDAO;
import com.its.common.entity.main.User;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.FriendgroupDao;
import com.its.frd.dao.GroupDao;
import com.its.frd.entity.Friendgroup;
import com.its.frd.entity.Group;
import com.its.frd.service.GroupService;

@Service("groupService")
public class GroupServiceImpl implements GroupService {
	@Resource
	private GroupDao groupDao;
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private FriendgroupDao friendgroupDao;
	
	@Override
	public void addGroup(Group group) {
		// TODO Auto-generated method stub
		/**
		 * 创建讨论组
		 */
		Group persistentGroup = null;
		if(group.getId() == 0){
			Timestamp timestamp = new Timestamp(new Date().getTime());
			group.setCreateTime(timestamp);
			group.setCreateUserId(SecurityUtils.getShiroUser().getUser().getId());
			group.setCreateUserName(SecurityUtils.getShiroUser().getUser().getRealname());
			persistentGroup = groupDao.saveAndFlush(group);
		}
		/**
		 * 编辑讨论组
		 */
		else{
			persistentGroup = groupDao.findOne(group.getId());
			
			List<Friendgroup> existFriendGroup = friendgroupDao.findAll(new Specification<Friendgroup>() {
				@Override
				public Predicate toPredicate(Root<Friendgroup> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
					Predicate p = builder.equal(root.get("groupid").as(Long.class),group.getId());
					return query.where(p).getRestriction();
				}
			});
			friendgroupDao.delete(existFriendGroup);
		}
		
		/**
		 * 数据库检索页面选择的讨论组成员
		 */
		List<Long> ids = group.getUserIds();
		ids.add(SecurityUtils.getShiroUser().getUser().getId());
		List<User> userList = userDao.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return query.where(root.get("id").in(ids)).getRestriction();
			}
		});
	
		/**
		 * 创建用户与讨论组关系并进行新增
		 */
		List<Friendgroup> friendgroups = new ArrayList<>();
		for(User user : userList){
			Friendgroup friendgroup = new Friendgroup();
			friendgroup.setGroupid(persistentGroup.getId());
			friendgroup.setGroupname(persistentGroup.getName());
			friendgroup.setUserid(user.getId());
			friendgroups.add(friendgroup);
		}
		friendgroupDao.save(friendgroups);
	}

	@Override
	public void deleteGroupRelationship(Long id) {
		/**
		 * 退出讨论组
		 */
		friendgroupDao.delete(friendgroupDao.findByUseridAndGroupid(SecurityUtils.getShiroUser().getUser().getId(), id).getId());
		
		/**
		 * 如果讨论组中没有成员,解散
		 */
		if(friendgroupDao.findByGroupid(id).size() == 0){
			groupDao.delete(id);
		}
		
	}

	@Override
	public Group findOne(Long groupId) {
		// TODO Auto-generated method stub
		return groupDao.findOne(groupId);
	}

}
