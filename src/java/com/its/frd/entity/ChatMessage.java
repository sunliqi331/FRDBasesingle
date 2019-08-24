package com.its.frd.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -2393715828538521906L;
	
	private Long id;
	
	private String content;
	
	private String toUser;
	
	private Long userid;
	
	private String fromUser;
	
	private Long friendid;
	
	private long timestamp;
	
	private String messageType = ChatMessage.MESSAGE_TYPE_CHAT;
	
	private String chatType = ChatMessage.CHAT_TYPE_FRIEND;
	
	private int isRead = ChatMessage.MESSAGE_READ_NO;
	
	public static final String MESSAGE_TYPE_CHAT = "好友聊天";
	public static final String MESSAGE_TYPE_INVITE = "好友邀请";
	public static final String CHAT_TYPE_FRIEND = "私聊";
	public static final String CHAT_TYPE_GROUP = "群聊";
	
	public static final int MESSAGE_READ_YES = 0;
	public static final int MESSAGE_READ_NO = -1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getFriendid() {
		return friendid;
	}

	public void setFriendid(Long friendid) {
		this.friendid = friendid;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}
	
	

}
