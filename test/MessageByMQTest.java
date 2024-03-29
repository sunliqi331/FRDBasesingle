import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicView;
import org.apache.activemq.broker.jmx.TopicViewMBean;

public class MessageByMQTest {
    // jmx服务地址，注意端口
    static String jmxserviceURL = "service:jmx:rmi:///jndi/rmi://192.168.217.45:11099/jmxrmi";
    // brokerName和type首字母要小些，brokerName=broker要与上面配置文件一致，斜体下划线部分
    static String objectName = "myDomain:brokerName=localhost,type=Broker";

    public static void main(String[] args) throws IOException, MalformedObjectNameException {

        JMXServiceURL url = new JMXServiceURL(jmxserviceURL);
        JMXConnector connector = JMXConnectorFactory.connect(url, null);
        connector.connect();
        MBeanServerConnection connection = connector.getMBeanServerConnection();

        ObjectName name = new ObjectName(objectName);
        BrokerViewMBean mBean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection, name,
                BrokerViewMBean.class, true);
        // System.out.println(mBean.getBrokerName());
        
        for (ObjectName queueName : mBean.getTopics()) {
            TopicViewMBean queueMBean = (TopicViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
                    queueName, TopicViewMBean.class, true);
            System.out.println("\n------------------------------\n");

            // 消息队列名称
            System.out.println("States for queue --- " + queueMBean.getName());

            // 队列中剩余的消息数
            System.out.println("Size --- " + queueMBean.getQueueSize());

            // 消费者数
            System.out.println("Number of consumers --- " + queueMBean.getConsumerCount());

            // 出队数
            System.out.println("Number of dequeue ---" + queueMBean.getDequeueCount());
        }
        /*
        for (ObjectName queueName : mBean.getQueues()) {
            QueueViewMBean queueMBean = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
                    queueName, QueueViewMBean.class, true);
            System.out.println("\n------------------------------\n");

            // 消息队列名称
            System.out.println("States for queue --- " + queueMBean.getName());

            // 队列中剩余的消息数
            System.out.println("Size --- " + queueMBean.getQueueSize());

            // 消费者数
            System.out.println("Number of consumers --- " + queueMBean.getConsumerCount());

            // 出队数
            System.out.println("Number of dequeue ---" + queueMBean.getDequeueCount());
        } */

    }

}
