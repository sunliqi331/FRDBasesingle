package com.its.frd.websocket;

import org.springframework.web.socket.WebSocketSession;


public class WebSocketThreadLocal {
	private static ThreadLocal<WebSocketSession> threadLocal = new ThreadLocal<WebSocketSession>();
	
	public static WebSocketSession getSocketSession(){
		return threadLocal.get();
	}
	
	public static void setSocketSession(WebSocketSession webSocketSession){
		threadLocal.set(webSocketSession);
	}
	public static void removeSocketSession() {
		threadLocal.remove();
	}
	
}
