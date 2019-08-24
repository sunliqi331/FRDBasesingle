package MQ.queue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.DeliveryMode;
import javax.jms.Session;

import com.its.frd.util.AmqManager;

public class MQSendMsg {
    
    public static void main(String[] args) {
        try {
            AmqManager.getAMQ().initJmxConnector();
            AmqManager.getAMQ().initConnection(getBroUrl(), "testClientId1");
            AmqManager.getAMQ().initProducer("DriverAlert", Session.AUTO_ACKNOWLEDGE, DeliveryMode.PERSISTENT);
            AmqManager.getAMQ().sendMessage("DriverAlert", "设备发生异常33");
            AmqManager.getAMQ().sendMessage("DriverAlert", "设备发生异常444");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getBroUrl() {
       Properties properties = new Properties();
       InputStream in = AmqManager.class.getClassLoader().getResourceAsStream("jmsConfig.properties");
       try {
           properties.load(in);
       } catch (IOException e) {
           e.printStackTrace();
       }
       String brokerUrl = properties.getProperty("brokerUriForDriverAlert");
       return brokerUrl;
    }

}
