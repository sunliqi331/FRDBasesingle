package com.its.monitor.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisListener implements MessageListener {

	@Override
	public void onMessage(Message arg0, byte[] arg1) {
		
			System.out.println();
	}

}
