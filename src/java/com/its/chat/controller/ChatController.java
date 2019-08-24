package com.its.chat.controller;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.chat.service.ChatService;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.ChatMessage;
import com.its.frd.entity.Friends;
import com.its.frd.entity.Group;
import com.its.frd.service.ChatMessageService;

@Controller
@RequestMapping("/chat.do")
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	@Autowired
	private ChatMessageService chatMessageService;
	
	
    /**
     * chat with your friend
     * @param chatMessage
     * @param socketMessage
     * @param principal
     * @throws Exception
     */
	@MessageMapping("/chatting") 
    public void greeting(ChatMessage chatMessage,Message<Object> socketMessage,Principal principal) throws Exception {
		chatService.greeting(chatMessage);
		chatMessageService.saveOrUpdateChatMessage(chatMessage);
    	//template.convertAndSendToUser(chatMessage.getToUser(), "/chat/chat", chatMessage);
    }
	/**get the friends list
	 * 
	 * @param principal
	 * @return
	 */
	@SubscribeMapping("/chat/participants")
    public Collection<Friends> retrieveParticipants(Principal principal) {
		String name = principal.getName();
		return chatService.findMyAllFriends(name);
    }
	/**get the groups list
	 * 
	 * @param principal
	 * @return
	 */
	@SubscribeMapping("/chat/groups")
	public Collection<Group> retrieveGroups(Principal principal) {
		String name = principal.getName();
		return chatService.findMyAllGroups(name);
	}
/*	*//**
	 * get the invitations list
	 * @param principal
	 * @return
	 *//*
	@SubscribeMapping("/chat/invitations")
    public Collection<ChatMessage> retrieveInvitations(Principal principal) {
		String name = principal.getName();
		return chatService.getCurrentUserInvitations(name);
    }*/
	/**
	 * send the invitation to you target
	 * @param request
	 * @param chatMessage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/friendInvitation") 
    public @ResponseBody String friendInvitation(HttpServletRequest request ,ChatMessage chatMessage) throws Exception {
		ChatMessage result = chatService.saveInvitationMessage(chatMessage);
		JSONObject jsonObject = new JSONObject();
		if(null != result){
			jsonObject.put("success", "success");
		}else{
			jsonObject.put("success", "error");
		}
		return jsonObject.toString();
    	//template.convertAndSendToUser(chatMessage.getToUser(), "/chat/invitation", chatMessage);
    }
	
}
