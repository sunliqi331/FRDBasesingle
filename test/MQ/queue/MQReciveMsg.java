package MQ.queue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.Session;

import com.its.frd.util.AmqManager;

public class MQReciveMsg {

    public static void main(String[] args) {
        try {
            AmqManager.getAMQ().initJmxConnector();
            AmqManager.getAMQ().initConnection(getBroUrl(), "testClientIdRecive");
            AmqManager.getAMQ().initConsumer("/driver/alert", Session.AUTO_ACKNOWLEDGE);
            AmqManager.getAMQ().getMessage("/driver/alert");
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
