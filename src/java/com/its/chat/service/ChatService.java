package com.its.chat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.its.common.entity.main.User;
import com.its.common.service.UserService;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.FriendgroupDao;
import com.its.frd.dao.GroupDao;
import com.its.frd.entity.ChatMessage;
import com.its.frd.entity.Friendgroup;
import com.its.frd.entity.Friends;
import com.its.frd.entity.Group;
import com.its.frd.service.ChatMessageService;
import com.its.frd.service.FriendsService;
import com.its.frd.util.AmqManager;

@Service
public class ChatService {
	
	@Autowired
	private SimpMessagingTemplate template;
	
	
	@Autowired
	private FriendsService friendsService;
	
	@Autowired
	private FriendgroupDao friendgroupDao;
	
	@Autowired
	private UserService userService;
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private ChatMessageService chatMessageService;
	/**
	 * sending friend invitation, before that ,we need check the relationship between this two guys
	 * @param chatMessage
	 */
	public ChatMessage saveInvitationMessage(ChatMessage chatMessage){
		User user = SecurityUtils.getShiroUser().getUser();
		chatMessage.setFromUser(user.getRealname());
		Friends friends = friendsService.findFriendRelationship(user.getId(), chatMessage.getFriendid());
		if(null != friends)//the relationship exist ,just return null
			return null;
		/*List<ChatMessage> tmp = chatMessageService.findChatMessageByCurrentUserAndFriendId(user.getId(), ChatMessage.MESSAGE_READ_NO, chatMessage.getFriendid());
		if(null != tmp && tmp.size() != 0){
			return null;
		}*/
		chatMessage.setUserid(user.getId());
		chatMessage.setMessageType(ChatMessage.MESSAGE_TYPE_INVITE);
		chatMessage.setTimestamp(System.currentTimeMillis());
		if(chatMessageService.findChatMessageByCondition(chatMessage).size() != 0)
			return null;
		User user_ = userService.findById(chatMessage.getFriendid());
		chatMessage.setToUser(user_.getRealname());
		this.chatMessageService.saveOrUpdateChatMessage(chatMessage);
		template.convertAndSendToUser(user_.getUsername(), "/invitation", chatMessage);
		return chatMessage;
	}
	
	
	/**
	 * get my invitations
	 * @param name
	 * @return
	 */
	public List<ChatMessage> getCurrentUserInvitations(String name){
		User user = userService.getByUsername(name);
		return chatMessageService.findChatMessageByCurrentUserAndReadStatus(user.getId(), ChatMessage.MESSAGE_READ_NO,ChatMessage.MESSAGE_TYPE_INVITE,ChatMessage.CHAT_TYPE_FRIEND);
	}

	/**
	 * update the chatMessage, this method only used for accept the friend invitation in this stage
	 * @param chatMessage
	 * @param accept
	 */
	public void updateInvitationMessage(ChatMessage chatMessage,int accept) {
		// TODO Auto-generated method stub
		if(accept == 0){
			
			
			Friends friends = friendsService.findFriendRelationship(chatMessage.getUserid(), chatMessage.getFriendid());
			if(null == friends){
				friends = new Friends();
			}else{
				return;
			}
			friends.setUserid(chatMessage.getUserid());
			friends.setFriendid(chatMessage.getFriendid());
			friends.setFriendname(chatMessage.getToUser());
			friends.setEmail(userService.findById(chatMessage.getFriendid()).getEmail());
			friendsService.saveFriend(friends);
			
			Friends _friends = new Friends();
			_friends.setUserid(chatMessage.getFriendid());
			_friends.setFriendid(chatMessage.getUserid());
			_friends.setFriendname(chatMessage.getFromUser());
			_friends.setEmail(userService.findById(chatMessage.getUserid()).getEmail());
			friendsService.saveFriend(_friends);
		}
		chatMessage.setIsRead(ChatMessage.MESSAGE_READ_YES);
		chatMessageService.saveOrUpdateChatMessage(chatMessage);
		
	}

	public List<Friends> findMyAllFriends(String name) {
		User user = userService.getByUsername(name);
		Map<String, Friends> map = new HashMap<>();
		List<Friends> list = friendsService.findMyFriends(user.getId());
		for (Friends friends : list) {
			friends.setRemainingNum(chatMessageService.unhandledOrHandledMessageNum(friends.getFriendid(), friends.getUserid(),ChatMessage.MESSAGE_TYPE_CHAT ,ChatMessage.MESSAGE_READ_NO));
		}
		/*for(Friends friends : list){
			map.put(friends.getFriendid()+"-"+friends.getUserid(), friends);
		}
		try {
			AmqManager.getAMQ().initJmxConnector();
			AmqManager.getAMQ().getBrokerViewMBean();
			AmqManager.getAMQ().initQueue();
			for(String queueName : AmqManager.getQueueViewMBeanMap().keySet()){
				QueueViewMBean queueBean = AmqManager.getQueueViewMBeanMap().get(queueName);
				if(map.containsKey(queueName.split("/")[2])){
					map.get(queueName.split("/")[2]).setRemainingNum(queueBean.getQueueSize());
				}
			}
		} catch (Exception e) {
			
		}*/
		
		return list;
	}
	public List<Group> findMyAllGroups(String name) {
		User user = userService.getByUsername(name);
		List<Friendgroup> list = friendgroupDao.findByUserid(user.getId());
		
		List<Long> ids = new ArrayList<Long>();
		
		for(Friendgroup friendgroup : list){
			ids.add(friendgroup.getGroupid());
		}
		if(ids.size() != 0){
			return groupDao.findAll(new Specification<Group>() {
				@Override
				public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
					return query.where(root.get("id").in(ids)).getRestriction();
				}
			});
		}
		return new ArrayList<Group>();
	}
	
	/**
	 * just chat with your friend
	 * @param chatMessage
	 */
	public ChatMessage greeting(ChatMessage chatMessage){
		User user2 = userService.get(chatMessage.getUserid());
		chatMessage.setFromUser(user2.getRealname());
		chatMessage.setMessageType(ChatMessage.MESSAGE_TYPE_CHAT);
		if(chatMessage.getChatType().equals(ChatMessage.CHAT_TYPE_FRIEND)){
			User user1 = userService.get(chatMessage.getFriendid());
			chatMessage.setToUser(user1.getRealname());
			template.convertAndSendToUser(chatMessage.getUserid()+"-"+chatMessage.getFriendid(), "/chat", chatMessage);
		}else if(chatMessage.getChatType().equals(ChatMessage.CHAT_TYPE_GROUP)){
			Group group = groupDao.findOne(chatMessage.getFriendid());
			chatMessage.setToUser(group.getName());
			chatMessage.setIsRead(ChatMessage.MESSAGE_READ_YES);
			template.convertAndSend("/topic/"+chatMessage.getFriendid()+"/chat", chatMessage);
		}
		return chatMessage;
		//chatMessage.setToUser(user1.getRealname());
	}


	public void updateFriendsRemainingNum(Friends fd) {
		// TODO Auto-generated method stub
		List<ChatMessage> list = chatMessageService.findChatMessageByCurrentUserAndReadStatus(SecurityUtils.getShiroUser().getUser().getId(), ChatMessage.MESSAGE_READ_NO, ChatMessage.MESSAGE_TYPE_CHAT,ChatMessage.CHAT_TYPE_FRIEND);
		for(ChatMessage chatMessage : list){
			chatMessage.setIsRead(ChatMessage.MESSAGE_READ_YES);
		}
	}
}
