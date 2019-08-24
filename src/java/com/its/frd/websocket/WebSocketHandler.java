package com.its.frd.websocket;
import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.jetty.util.log.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.its.monitor.service.MonitorService;


public class WebSocketHandler extends TextWebSocketHandler {
	@Autowired
	private MonitorService monitorService;

	public WebSocketHandler(){

	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("大河向东流");
		WebSocketThreadLocal.setSocketSession(session);
		super.afterConnectionEstablished(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String text = message.getPayload(); // 获取提交过来的消息
		System.out.println("handMessage:" + text);
		try {
			while(true){
				Thread.sleep(2000);
				if(null != WebSocketThreadLocal.getSocketSession() && WebSocketThreadLocal.getSocketSession().isOpen()){
					//JSONArray jsonArray = new JSONArray(monitorService.getMonitorInfo(text));
					//session.sendMessage(new TextMessage(jsonArray.toString().getBytes()));
				}else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionClosed(session, status);
		WebSocketThreadLocal.removeSocketSession();
		System.out.println("!@#%^%#$@%^#%^@$@%&&@%%^@");
	}
}