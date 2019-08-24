package MQ.topic;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.activemq.broker.jmx.TopicViewMBean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.frd.util.AmqManager;
import com.its.frd.util.JSONUtils;

public class MQSendMsg {

    public static void main(String[] args) throws Exception {
        try {
            String topicNm = "showMonitor/monitor/323";
            AmqManager.getAMQ().initJmxConnector();
            AmqManager.getAMQ().initConnection(getBroUrl(), "testClientId1");
            AmqManager.getAMQ().getBrokerViewMBean();
            Map<String, TopicViewMBean> topicMap = AmqManager.getAMQ().initTopics();
            topicMap.get(topicNm).sendTextMessage(sendMsg());
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

    private static String sendMsg() { 
        String rs = "";
        List<Map<String, Object>> rsList = Lists.newArrayList();
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("mesPointTypeKey", "P_PROCEDURE");
        rsMap.put("sn", "sn-test");
        rsMap.put("mesProduct", "150");
        rsMap.put("mesProcedureName", "测试产品");
        rsMap.put("mesProductCompanyinfo", "测试产品info");
        rsMap.put("mesProductModel", "model9");

        rsMap.put("mesDriverProcedureId", "277");
        rsMap.put("procedurePropertyId", "178");

        rsMap.put("value", "value_test");
        rsMap.put("uploadTime", "20180801");
        rsMap.put("status", "1");
        rsMap.put("mesPointKey", "5606");
        rsMap.put("unit", "台");
        rsList.add(rsMap);

        // -----------------------------------
        rsMap = Maps.newHashMap();
        rsMap.put("mesPointTypeKey", "P_PROCEDURE");
        rsMap.put("sn", "sn-test");
        rsMap.put("mesProduct", "150");
        rsMap.put("mesProcedureName", "测试产品");
        rsMap.put("mesProductCompanyinfo", "测试产品info");
        rsMap.put("mesProductModel", "model9");

        rsMap.put("mesDriverProcedureId", "277");
        rsMap.put("procedurePropertyId", "179");

        rsMap.put("value", "value_test");
        rsMap.put("uploadTime", "20180801");
        rsMap.put("status", "1");
        rsMap.put("mesPointKey", "5606");
        rsMap.put("unit", "台");
        rsList.add(rsMap);
        rs = JSONUtils.beanToJson(rsList);
        return rs;
    }

}
