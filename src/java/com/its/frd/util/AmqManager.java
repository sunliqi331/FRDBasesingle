package com.its.frd.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;  

public class AmqManager {  


	public static BrokerService broker = new BrokerService();  

	private static String objectName;

	private static String jmxUrlStr;

	private final static AmqManager amq = new AmqManager();    
	/** 连接器 */  
	private static Connection connection = null;  

	/**
	 * jmx连接器
	 */
	private static JMXConnector jmxConnector = null;
	/** 按队列名获取session */  
	private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();  
	private static Map<String, QueueViewMBean> queueViewMBeanMap = new ConcurrentHashMap<String, QueueViewMBean>();  
	private static Map<String, TopicViewMBean> topicViewMBeanMap = new ConcurrentHashMap<String, TopicViewMBean>();  
	private static BrokerViewMBean brokerViewMBean = null;
	/** 按队列名称获取生产者对象 */  
	private static Map<String, MessageProducer> producerMap = new ConcurrentHashMap<String, MessageProducer>();  
	/** 按队列名称获取消费者对象 */  
	private static Map<String, MessageConsumer> consumerMap = new ConcurrentHashMap<String, MessageConsumer>();  

	static{
		Properties properties = new Properties();
		InputStream in = AmqManager.class.getClassLoader().getResourceAsStream("jmsConfig.properties");
		//InputStream in = AmqManager.class.getClassLoader().getResourceAsStream("d:/jmsConfig.properties");
		try {
		    System.out.println("---in:" + in);
			properties.load(in);
			//properties.load(new FileInputStream(new File("d:/jmsConfig.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		objectName = properties.getProperty("objectName");
		jmxUrlStr = properties.getProperty("jmxUrl");

	}

	private AmqManager(){};  

	public static synchronized AmqManager getAMQ() throws Exception {  
		return amq;  
	}  
	
	public static Map<String, QueueViewMBean> getQueueViewMBeanMap(){
		return queueViewMBeanMap;
	}
	public static Map<String, TopicViewMBean> getTopicViewMBeanMap(){
		return topicViewMBeanMap;
	}
	/** 
	 * 获取连接器 
	 * @param brokerUri 
	 * @param clientID 
	 * @return 
	 * @throws Exception  
	 */  
	public synchronized Connection initConnection(String brokerUri, String clientID) throws Exception { 
		if (null == connection) {  
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUri);  
			connection = connectionFactory.createConnection();
			connection.setClientID(clientID);  
			connection.start();  
		}  
		return connection;  
	}  
	public synchronized BrokerViewMBean getBrokerViewMBean() throws Exception { 
		if(null == brokerViewMBean){
			MBeanServerConnection conn = jmxConnector.getMBeanServerConnection();
			ObjectName name = new ObjectName(objectName);
			brokerViewMBean = (BrokerViewMBean)MBeanServerInvocationHandler.newProxyInstance
					(conn, name, BrokerViewMBean.class, true);
		}
		return brokerViewMBean;  
	}  


	public synchronized JMXConnector initJmxConnector(){ 
		if (null == jmxConnector) {
			String[] jmxUrls = jmxUrlStr.split(",");
			for(String jmxUrl : jmxUrls){
				JMXServiceURL url;
				try {
					url = new JMXServiceURL(jmxUrl);
					jmxConnector = JMXConnectorFactory.connect(url,null);
					jmxConnector.connect();
					break;
				} catch (MalformedURLException e) {
				} catch (IOException e) {
				}
			}
		}  
		return jmxConnector;  
	}  


	public Map<String, QueueViewMBean> initQueue() throws JMSException, IOException {  
		if(null != brokerViewMBean){
			for(ObjectName na : brokerViewMBean.getQueues()){
				QueueViewMBean queueBean = (QueueViewMBean)
						MBeanServerInvocationHandler.newProxyInstance(jmxConnector.getMBeanServerConnection(), na, QueueViewMBean.class, true);
				queueViewMBeanMap.put(queueBean.getName(), queueBean);
			}
		}  
		return queueViewMBeanMap;
	}  
	public Map<String, TopicViewMBean> initTopics() throws JMSException, IOException {  
		if(null != brokerViewMBean){
			for(ObjectName na : brokerViewMBean.getTopics()){
				TopicViewMBean topicBean = (TopicViewMBean)
						MBeanServerInvocationHandler.newProxyInstance(jmxConnector.getMBeanServerConnection(), na, TopicViewMBean.class, true);
				topicViewMBeanMap.put(topicBean.getName(), topicBean);
			}
		}  
		return topicViewMBeanMap;
	}  

	/** 
	 * 初始化生产者 
	 * @param queueName 
	 * @param acknowledgeMode 
	 * @param deliveryMode 
	 * @return 
	 * @throws JMSException 
	 */  
	public MessageProducer initProducer(String queueName, int acknowledgeMode, int deliveryMode) throws JMSException {  
		MessageProducer producer = null;  
		if (connection != null) {  
			Session session = connection.createSession(false, acknowledgeMode);  
			Destination destination = session.createQueue(queueName);  
			producer = session.createProducer(destination);  
			producer.setDeliveryMode(deliveryMode);  
			sessionMap.put(queueName, session);  
			producerMap.put(queueName, producer);  
		}  
		return producer;  
	}  

	/** 
	 * 初始化消费者 
	 * @param queueName 
	 * @param acknowledgeMode 
	 * @param deliveryMode 
	 * @return 
	 * @throws JMSException 
	 */  
	public MessageConsumer initConsumer(String queueName, int acknowledgeMode) throws JMSException {  
		MessageConsumer consumer = null;  
		if (connection != null) {  
			Session session = connection.createSession(false, acknowledgeMode);  
			Destination destination = session.createQueue(queueName);  
			consumer = session.createConsumer(destination);  
			consumerMap.put(queueName, consumer);  
		}  
		return consumer;  
	}  

	/** 
	 * 接收消息 
	 * @param consumer 
	 * @throws JMSException 
	 */  
	public void getMessage(String queueName) throws JMSException  
	{  
		MessageConsumer consumer = consumerMap.get(queueName);  
		while (true) {  
			TextMessage textMessage = (TextMessage) consumer.receive(100000);  
			if(textMessage != null){  
				System.out.println("收到消息:" + textMessage.getText());  
			}else {  
				System.out.println("接收消息异常");  
				break;  
			}  
		}  
	}  

	public void getMessageAsListener(String queueName) throws JMSException  
	{  
		MessageConsumer consumer = consumerMap.get(queueName);  
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message msg) {
				if (msg != null) {
                    TextMessage textMessage = (TextMessage) msg;
                    try {
                        System.out.println("收到消息" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
				
			}
		});
	}  
	
	
	/** 
	 * 发送消息 
	 * @param queueName 
	 * @param message 
	 * @throws JMSException 
	 */  
	public void sendMessage(String queueName, String message) throws JMSException {  
		TextMessage msg = sessionMap.get(queueName).createTextMessage(message);  
		//msg.setStringProperty("sqlId", "comstar-market-data-feedhub");  
		producerMap.get(queueName).send(msg);  
	}  
	/** 
	 * 发送消息 
	 * @param queueName 
	 * @param message 
	 * @throws JMSException 
	 */  
	public String sendJMXMessage(String queueName, String message) throws Exception {  
		String msg = queueViewMBeanMap.get(queueName).sendTextMessage(message);
		return msg;
	}  


	/** 
	 * 发送消息 
	 * @param queueName 
	 * @param message 
	 * @param headName 
	 * @param headValue 
	 * @throws JMSException 
	 */  
	public void sendMessage(String queueName, String message, String headName,  
			String headValue) throws JMSException {  
		TextMessage msg = sessionMap.get(queueName).createTextMessage(message);  
		msg.setStringProperty(headName, headValue);  
		producerMap.get(queueName).send(msg);  
	}  
	/**
	 * 新增队列
	 * @param queueName
	 * @throws Exception
	 */
	public void addQueue(String queueName) throws Exception{
		if(!queueViewMBeanMap.containsKey(queueName))
			brokerViewMBean.addQueue(queueName);
	}
	/**
	 * 删除队列
	 * @param queueName
	 * @throws Exception
	 */
	public void removeQueue(String queueName) throws Exception{
		if(queueViewMBeanMap.containsKey(queueName))
			brokerViewMBean.removeQueue(queueName);
	}
	/**
	 * 关闭连接
	 * @throws IOException
	 */
	public void closeConnection() throws IOException{
		if(jmxConnector != null){
			jmxConnector.close();
		}
	}
	/** 
	 * 关闭session 
	 * @param queueName 
	 * @throws JMSException 
	 */  
	public void close(String queueName) throws JMSException {  
		Session session = sessionMap.get(queueName);  
		if (null != session) {  
			session.close();  
			sessionMap.remove(queueName);  
			destroy();  
		}  
	}  

	/** 
	 * 销毁连接 
	 * note：如果session都没有了，则销毁连接 
	 * @throws JMSException 
	 */  
	private synchronized void destroy() throws JMSException {  
		if (connection != null) {  
			if (0 == sessionMap.size()) {  
				connection.close();  
				connection = null;  
			}  
		}  
	}  

	public synchronized static void initAMQ() {  
		try {  
			TransportConnector connector = new TransportConnector();  
			connector.setUri(new URI("tcp://localhost:61616"));  
			broker.addConnector(connector);  
			broker.start();  
		} catch (JMSException e) {  
			e.printStackTrace();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  
	public void destoryAMQ() {  
		try {  
			//close("");  
			broker.stop();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  

	public static void main(String[] args) {  
		try {  
			//getAMQ();  
			/*BrokerService broker = new BrokerService();  
			  TransportConnector connector = new TransportConnector();  
            connector.setUri(new URI("tcp://localhost:61616"));  
            broker.addConnector(connector);  
            broker.start(); */
		  //  amq.initConnection("failover://tcp://localhost:61616", "cnning");  
            //amq.initProducer("queue.test", Session.AUTO_ACKNOWLEDGE, DeliveryMode.NON_PERSISTENT);  
         //   amq.initConsumer("/showMonitor/advise/template", Session.AUTO_ACKNOWLEDGE);  
          //  amq.sendMessage("queue.test", "hahahahahahahhahaha"); 
         //   amq.getMessageAsListener("/showMonitor/advise/template");

			//amq.initConnection("failover://tcp://localhost:61616", "cning"); 
			amq.initJmxConnector();
			amq.getBrokerViewMBean();
			amq.initQueue();
			//amq.sendMessage("", "hahaha");
			for(String queueName : queueViewMBeanMap.keySet()){
				QueueViewMBean queueBean = queueViewMBeanMap.get(queueName);
				System.out.println("******************************");
				System.out.println("队列的名称："+queueBean.getName());
				System.out.println("队列中剩余的消息数："+queueBean.getQueueSize());
				System.out.println("消费者数："+queueBean.getConsumerCount());
				System.out.println("出队列的数量："+queueBean.getDequeueCount());
				queueBean.sendTextMessage("asdasdasdasd");
			}
			// amq.getMessageAsListener("queue.test");  
			//  amq.destoryAMQ();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  
}  