package com.its.frd.websocket;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketSessionConnectedListener implements ApplicationListener<SessionConnectedEvent> {

	@Override
	public void onApplicationEvent(SessionConnectedEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		List<String> clientIDs = sha.getNativeHeader("session");
		String sessionId = sha.getSessionId();
		SocketSessionUtil.add(sessionId, sessionId);
		System.out.println("connection connected :" + sessionId);
	}
}
