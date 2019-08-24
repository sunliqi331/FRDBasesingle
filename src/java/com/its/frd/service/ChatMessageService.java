package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.ChatMessage;

public interface ChatMessageService {
	public void saveOrUpdateChatMessage(ChatMessage chatMessage);
	
	public List<ChatMessage> findChatMessageByCurrentUserAndReadStatus(long userId, int read,String messageType, String chatType);
	
	public List<ChatMessage> findChatMessageByCondition(ChatMessage chatMessage);
	
	public long unhandledOrHandledMessageNum(long friendId,long userId,String messageType,int isRead);
	
}
