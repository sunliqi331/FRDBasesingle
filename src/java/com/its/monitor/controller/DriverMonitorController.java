package com.its.monitor.controller;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.its.frd.util.DateUtils;
import com.its.monitor.service.MonitorService;
import com.its.monitor.vo.ChartsBaseMonitor;
import com.its.monitor.vo.MonitorPage;

@Controller
public class DriverMonitorController {
	@Autowired
	private MonitorService monitorService;

    @MessageMapping("/getMonitorInfo") 
    public void greeting(MonitorPage page,Message<Object> socketMessage) throws Exception {
        System.out.println("----------getMonitorInfo---------start--------");
        System.out.println("----------getMonitorInfo---------start--------");
        System.out.println("----------getMonitorInfo---------start--------");
        System.out.println("----------getMonitorInfo---------start--------");
        System.out.println("----------getMonitorInfo---------start--------");
    	monitorService.getMonitorInfo(page,socketMessage);
    }
    @MessageMapping("/sendTemplate") 
    public void sendTemplate(Message<Object> socketMessage) throws Exception {
    	//monitorService.sendTemplate();
    }
    @MessageMapping("/getEnergyInfo") 
    public void getEnergyInfo(String monitors,Message<Object> socketMessage) throws Exception{
    	//JSONArray jsonArray = new JSONArray(monitors);
    	//List<ChartsBaseMonitor> chartsBaseMonitors = new ArrayList<>();
    	//for(int i = 0; i < jsonArray.length(); i++){
    		JSONObject jsonObject = new JSONObject(monitors);
    		ChartsBaseMonitor chartsBaseMonitor = new ChartsBaseMonitor();
    		Class<ChartsBaseMonitor> clazz = ChartsBaseMonitor.class;
    		Map<String,String> map = new HashMap<>();
    		for(String field : jsonObject.keySet()){
    			if(field.indexOf("map") != -1){
    				String idArray = field.split("map")[1];
    				JSONArray array = new JSONArray(idArray);
    				map.put(array.getString(0), jsonObject.getString(field));
    			}else if(field.indexOf("timeScope") != -1){
    				Method method = clazz.getMethod("set"+field.substring(0, 1).toUpperCase()+field.substring(1),Date.class);
    				method.invoke(chartsBaseMonitor, DateUtils.getDateByStr(jsonObject.getString(field)));
    			}else{
    				Method method = clazz.getMethod("set"+field.substring(0, 1).toUpperCase()+field.substring(1),String.class);
    				method.invoke(chartsBaseMonitor, jsonObject.getString(field));
    			}
    			
    		}
    		Method method = clazz.getMethod("setMap",Map.class);
			method.invoke(chartsBaseMonitor, map);
    		//chartsBaseMonitors.add(chartsBaseMonitor);
    	//}
    	monitorService.getEnergyInfo(chartsBaseMonitor,socketMessage);
    }
}