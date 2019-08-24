package MQ.topic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.jms.DeliveryMode;
import javax.jms.Session;

import org.apache.activemq.broker.jmx.TopicViewMBean;

import com.its.frd.util.AmqManager;

public class MQReciveMsg {
    
    public static void main(String[] args) throws Exception {
        try {
            String topicNm = "showMonitor/monitor/323";
            AmqManager.getAMQ().initJmxConnector();
            AmqManager.getAMQ().initConnection(getBroUrl(), "testClientIdRecive");
            AmqManager.getAMQ().initConsumer(topicNm, Session.AUTO_ACKNOWLEDGE);
            AmqManager.getAMQ().getMessage(topicNm);
        } catch (Exception e) {
            e.printStackTrace();
            //AmqManager.getAMQ().destoryAMQ();
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
