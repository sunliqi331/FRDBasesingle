package com.its.frd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.service.RedisService;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPoints;
import com.its.frd.service.MesPointGatewayService;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.service.MonitorService;
import com.its.monitor.vo.MesPointsTemplate;

@Controller
@RequestMapping("/remoteAccess")
public class RemoteAccessController {
	@Resource
	private MesPointGatewayService mesPointGatewayService;
	@Resource
	private MonitorService monitorService;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;
	
	@Resource
	private RedisService redisServ;
	
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	
	@RequestMapping("/getVerification/{name}/{password}")
	@ResponseBody
	public String getGatewayAccessIdentity(@PathVariable String name,@PathVariable String password){
		MesPointGateway mesPointGateway = new MesPointGateway();
		mesPointGateway.setMac(name);
		mesPointGateway.setMacCode(password);
		MesPointGateway gateway = mesPointGatewayService.findAsSingle(mesPointGateway);
		return null != gateway ? SUCCESS : ERROR;
	}
	@RequestMapping("/sendTemplate")
	@ResponseBody
    public String sendTemplate(HttpServletRequest httpServletRequest) throws Exception {
    	//monitorService.sendTemplate();
		List<MesPointGateway> mesPointGatewayList = mesPointGatewayService.findAll();
		for(MesPointGateway gateway : mesPointGatewayList){
			List<MesPoints> mesPointses = gateway.getMesPointses();
			for (MesPoints mesPoints : mesPointses) {
				String result = mesPointsTemplateService.getTemplate(mesPoints);
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), result);
				redisServ.setHash(key, hash);
				
			}
			mesPointsTemplateService.sendTemplate(gateway.getMac());
		}
    	return SUCCESS;
    }
}
