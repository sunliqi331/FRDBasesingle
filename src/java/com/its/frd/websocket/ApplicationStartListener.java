package com.its.frd.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.jms.Session;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.its.frd.util.AmqManager;

//@Component
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
            System.out.println("---------------tomcat 启动调用---------设备报警信息收集--------------------------------start");

            Map<String, String> proMap = getBroUrl();
            try {
                AmqManager.getAMQ().initJmxConnector();
                AmqManager.getAMQ().initConnection(proMap.get("brokerUrl"), "testClientIdRecive");
                AmqManager.getAMQ().initConsumer(proMap.get("drirverAlert"), Session.AUTO_ACKNOWLEDGE);
                AmqManager.getAMQ().getMessage(proMap.get("drirverAlert"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("---------------tomcat 启动调用-----------设备报警信息收集------------------------------end");
        }
    }

    private Map<String, String> getBroUrl() {
        Properties properties = new Properties();
        InputStream in = AmqManager.class.getClassLoader().getResourceAsStream("jmsConfig.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> rsMap = Maps.newHashMap();
        String brokerUrl = properties.getProperty("brokerUriForDriverAlert");
        rsMap.put("brokerUrl", brokerUrl);
        String drirverAlert = properties.getProperty("drirverAlert");
        rsMap.put("drirverAlert", drirverAlert);

        return rsMap;
    }
}
