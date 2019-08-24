package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.its.frd.entity.ChatMessage;

public interface ChatMessageDao extends JpaRepository<ChatMessage, Long>,JpaSpecificationExecutor<ChatMessage> {

	public List<ChatMessage> findByFriendidAndIsReadAndMessageTypeAndChatType(long friendid,int isRead,String messageType,String chatType);
	
	public List<ChatMessage> findByUseridAndIsReadAndFriendid(long userId,int isRead,long friendId);

	@Query(value="select count(*) from ChatMessage cm where "
			+ "cm.userid = :friendId and cm.friendid = :userId "
			+ " and cm.messageType = :messageType and cm.isRead = :isRead"
		)
	public long unhandledOrHandledMessageNum(@Param("friendId") long friendId, @Param("userId") long userId, @Param("messageType") String messageType, @Param("isRead") int isRead);
	
}
