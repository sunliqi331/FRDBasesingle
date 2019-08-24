package com.its.frd.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.frd.dao.ChatMessageDao;
import com.its.frd.entity.ChatMessage;
import com.its.frd.service.ChatMessageService;

@Service("chatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService {
	@Autowired
	private ChatMessageDao chatMessageDao;
	
	@Override
	public void saveOrUpdateChatMessage(ChatMessage chatMessage) {
		chatMessageDao.saveAndFlush(chatMessage);
	}

	@Override
	public List<ChatMessage> findChatMessageByCurrentUserAndReadStatus(long userId, int read,String messageType,String chatType) {
		// TODO Auto-generated method stub
		return chatMessageDao.findByFriendidAndIsReadAndMessageTypeAndChatType(userId, read,messageType,chatType);
	}

	@Override
	public long unhandledOrHandledMessageNum(long friendId, long userId, String messageType,int isRead) {
		// TODO Auto-generated method stub
		return chatMessageDao.unhandledOrHandledMessageNum(friendId,userId,messageType,isRead);
	}

	@Override
	public List<ChatMessage> findChatMessageByCondition(ChatMessage chatMessage) {
		return chatMessageDao.findAll(new Specification<ChatMessage>() {
			@Override
			public Predicate toPredicate(Root<ChatMessage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				if(null != chatMessage.getChatType()){
					Predicate p = builder.equal(root.get("chatType").as(String.class), chatMessage.getChatType());
					predicates.add(p);
				}
				Predicate p_isRead = builder.equal(root.get("isRead"), chatMessage.getIsRead());
				predicates.add(p_isRead);
				Predicate p_messageType = builder.equal(root.get("messageType").as(String.class), chatMessage.getMessageType());
				predicates.add(p_messageType);
				Predicate p_chatType = builder.equal(root.get("chatType").as(String.class), chatMessage.getChatType());
				predicates.add(p_chatType);
				if(null != chatMessage.getFromUser()){
					Predicate p = builder.equal(root.get("fromUser").as(String.class), chatMessage.getFromUser());
					predicates.add(p);
				}
				if(null != chatMessage.getToUser()){
					Predicate p = builder.equal(root.get("toUser").as(String.class), chatMessage.getToUser());
					predicates.add(p);
				}
				if(null != chatMessage.getUserid()){
					Predicate p = builder.equal(root.get("userid").as(Long.class), chatMessage.getUserid());
					predicates.add(p);
				}
				if(null != chatMessage.getFriendid()){
					Predicate p = builder.equal(root.get("friendid").as(Long.class), chatMessage.getFriendid());
					predicates.add(p);
				}
				Predicate[] predicateArray = new Predicate[predicates.size()];
				return query.where(predicates.toArray(predicateArray)).getRestriction();
			}
		});
	}

}
