
import java.util.Hashtable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;

public class mqtest {
    public int MAX_DELTA_PERCENT = 1;
    public Map<String, Double> LAST_PRICES = new Hashtable<String, Double>();
    public static int count = 1;
    public static int total;

    public static String brokerURL = "tcp://192.168.217.45:61616";
//    public static String brokerURL = "tcp://localhost:61616";
    public static transient ConnectionFactory factory;
    public transient Connection connection;
    public transient Session session;
    public transient Destination destination;
    public transient MessageProducer producer;

    public mqtest() throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL); // 创建连接工场
        connection = factory.createConnection(); // 创建连接
        try {
            connection.start(); // 打开连接
        } catch (JMSException jmse) {
            connection.close();
            throw jmse;
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); // 创建session 不带事务
        destination = session.createTopic("showMonitor/monitor/spc"); // 创建topic
        producer = session.createProducer(destination); // 创建publisher
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        mqtest publisher = new mqtest();
        while (total < 1) {
            for (int i = 0; i < count; i++) {
                publisher.sendMessage();
            }
            total += count;
            System.out.println("Published '" + count + "' of '" + total + "' price messages");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException x) {
            }
        }
        publisher.close();
    }

    protected void sendMessage() throws JMSException {
        session.createTextMessage("516_242_318_9_236_1516340201_BN0024728");
        Message message = createStockMessage(session);
        System.out.println(
                "Sending: " + ((ActiveMQMapMessage) message).getContentMap() + " on destination: " + destination);
        producer.send(destination, session.createTextMessage("516_242_358_9_238_1516340201_BN0024728"));
    }

    protected Message createStockMessage(Session session) throws JMSException {
        Double value = LAST_PRICES.get("topic");
        if (value == null) {
            value = new Double(Math.random() * 100);
        }

        double oldPrice = value.doubleValue();
        value = new Double(mutatePrice(oldPrice));
        LAST_PRICES.put("topic", value);
        double price = value.doubleValue();
        double offer = price * 1.001;
        boolean up = (price > oldPrice);

        MapMessage message = session.createMapMessage();
        message.setString("topic", "topic");
        message.setDouble("price", price);
        message.setDouble("offer", offer);
        message.setBoolean("up", up);
        return message;
    }

    protected double mutatePrice(double price) {
        double percentChange = (2 * Math.random() * MAX_DELTA_PERCENT) - MAX_DELTA_PERCENT;
        return price * (100 + percentChange) / 100;
    }

}
