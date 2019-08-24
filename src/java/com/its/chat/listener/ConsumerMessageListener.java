package com.its.chat.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 消息监听器
 * @author Administrator
 *
 */
public class ConsumerMessageListener implements MessageListener {
   /**
    * 消息处理
    */
    public void onMessage(Message message) {
        TextMessage textMsg = (TextMessage) message;
        try {
            System.out.println("接收者受到消息：" + textMsg.getText());
            System.out.println("开始进行解析并调用service执行....");
            
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
 
}