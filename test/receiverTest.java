import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class receiverTest {
    // ConnectionFactory ：连接工厂，JMS 用它创建连接
    private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
            ActiveMQConnection.DEFAULT_PASSWORD, "tcp://192.168.217.35:61616");
//    ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");

    public static void main(String[] args) throws JMSException {
        // Connection ：JMS 客户端到JMS Provider 的连接
        final Connection connection = connectionFactory.createConnection();

        connection.start();
        // Session： 一个发送或接收消息的线程
        final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        String topicStr = "showMonitor/monitor/spc";
        topicStr = "showMonitor/advise/protocal";
        Topic topic = session.createTopic(topicStr);
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage) message;
                try {
                    System.out.println("线程" + tm + "收到消息:" + tm.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }

            }
        });

//        // Destination ：消息的目的地;消息送谁那获取.
//        Destination destination = session.createTopic("showMonitor/monitor/spc");
//        // 消费者，消息接收者
//        MessageConsumer consumer1 = session.createConsumer(destination);
//
//        consumer1.setMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(Message msg) {
//
//                try {
//
//                    TextMessage message = (TextMessage) msg;
//                    System.out.println("consumer1收到消息： " + message.getText());
//                    session.commit();
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        // 再来一个消费者，消息接收者
//        MessageConsumer consumer2 = session.createConsumer(destination);
//
//        consumer2.setMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(Message msg) {
//
//                try {
//
//                    TextMessage message = (TextMessage) msg;
//                    System.out.println("consumer2收到消息： " + message.getText());
//                    session.commit();
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

    }

}
