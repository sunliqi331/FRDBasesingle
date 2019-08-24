package com.its.frd.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.dao.UserDAO;
import com.its.common.entity.main.User;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.FriendgroupDao;
import com.its.frd.dao.FriendsDao;
import com.its.frd.dao.GroupDao;
import com.its.frd.entity.Friendgroup;
import com.its.frd.entity.Friends;
import com.its.frd.entity.Group;
import com.its.frd.service.FriendsService;
import com.its.frd.util.DateUtils;
@Service
public class FriendsServiceImpl implements FriendsService {
	@Resource
	private FriendsDao fdDao;
	@Resource
	private FriendgroupDao fgDao;
	@Resource
	private GroupDao groupDao;
	@Resource
	private UserDAO userDao;
	@Override
	public List<Friends> findPage(Specification<Friends> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Friends> springDataPage = fdDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public void saveFriend(Friends fd) {
		fdDao.save(fd);
	}

	@Override
	public List<Friendgroup> findList(Long userid) {
		return fgDao.findByUserid(userid);
	}

	@Override
	public List<Group> findGroupPage(Specification<Friendgroup> specification, Page page) {
		org.springframework.data.domain.Page<Friendgroup> springDataPage = fgDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		List<Friendgroup> list = springDataPage.getContent();
		List<Group> groups = new ArrayList<>();
		for(Friendgroup friendgroup : list){
			Group g = groupDao.findOne(friendgroup.getGroupid());
			friendgroup.setCreateTime(DateUtils.getYYYYMMDDHHMMSSDayStr(new Date(g.getCreateTime().getTime())));
			friendgroup.setCreateUser(g.getCreateUserName());
			groups.add(g);
		}
		return groups;
	}

	@Override
	public void saveGroup(Friendgroup group) {
		fgDao.save(group);
	}

	@Override
	public void deleteFriends(Friends fd) {
		Friends friends = fdDao.findOne(fd.getId());
		Friends _friends = fdDao.findByUseridAndFriendid(friends.getFriendid(), friends.getUserid());
		if(null != _friends){
			fdDao.delete(_friends);
		}
		fdDao.delete(friends);
	}

	@Override
	public void delGroup(Friendgroup group) {
		fgDao.delete(group);
	}

	@Override
	public Friends findFriendRelationship(Long userid, Long friendid) {
		return fdDao.findByUseridAndFriendid(userid, friendid);
	}

	@Override
	public List<Friends> findMyFriends(long id) {
		// TODO Auto-generated method stub
		return fdDao.findByUserid(id);
	}

	@Override
	public List<User> findGroupMembers(Long groupId) {
		// TODO Auto-generated method stub
		List<Friendgroup> friendgroups = fgDao.findByGroupid(groupId);
		List<Long> ids = new ArrayList<Long>();
		for(Friendgroup friendgroup : friendgroups){
			ids.add(friendgroup.getUserid());
		}
		List<User> userList = userDao.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return query.where(root.get("id").in(ids)).getRestriction();
			}
		});
		return userList;
	}

}
