package com.its.monitor.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.entity.main.Module;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesPointCheckData;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesSpcMonitor;
import com.its.frd.entity.MesSpcMonitorConfig;
import com.its.frd.entity.MesSpcMonitorMonitorConfig;
import com.its.frd.service.MesSpcMonitorConfigService;
import com.its.frd.service.MesSpcMonitorMonitorConfigService;
import com.its.frd.service.MesSpcMonitorService;
import com.its.statistics.service.StatisticsService;

@Controller
@RequestMapping("/mesSpcMonitor")
public class MesSpcMonitorController {
	@Resource
	private StatisticsService statisticsService;

	@Resource
	private MesSpcMonitorService mesSpcMonitorService;
	@Resource
	private MesSpcMonitorConfigService mesSpcMonitorConfigService;
	@Resource
	private MesSpcMonitorMonitorConfigService mesSpcMonitorMonitorConfigService;
	@RequestMapping("/spc")
	public ModelAndView spc() {
		ModelAndView modelAndView = new ModelAndView("spcMonitor/spcMonitor");
		List<MesSpcMonitorConfig> mesSpcMonitorConfigs = mesSpcMonitorConfigService.findAll();
		//modelAndView.addObject("checkDrivers", statisticsService.generateDriverSelect());
		//modelAndView.addObject("products", statisticsService.generateProductSelect());
		modelAndView.addObject("mesSpcMonitorConfigs", mesSpcMonitorConfigs);
		return modelAndView;
	}

	@RequiresPermissions("spcMonitor:view")
	@RequestMapping(value="/data", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String list(ServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<>();
		Specification<MesSpcMonitor> specification = DynamicSpecifications.bySearchFilter(request, MesSpcMonitor.class,
				new SearchFilter("userId", Operator.EQ, SecurityUtils.getShiroUser().getUser().getId()));
		List<MesSpcMonitor> mesSpcMonitors = mesSpcMonitorService.findPage(specification, page);
		map.put("page", page);
		map.put("mesSpcMonitors", mesSpcMonitors);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return objectMapper.writeValueAsString(map);
	}

	@RequestMapping("/toAddMesSpcMonitor/{id}")
	public ModelAndView toAddMesSpcMonitor(@PathVariable Long id,HttpServletRequest request) throws JsonProcessingException {
		ModelAndView modelAndView = new ModelAndView("spcMonitor/spcMonitorAdd");
		modelAndView.addObject("checkDrivers", statisticsService.generateDriverSelect());
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		MesSpcMonitor mesSpcMonitor = new MesSpcMonitor();
		if(id != 0){
			mesSpcMonitor = mesSpcMonitorService.findById(id);
		}
		modelAndView.addObject("mesSpcMonitor", mesSpcMonitor);
		return modelAndView;
	}
	@RequestMapping("/toAddMesSpcMonitorRule/{id}")
	public ModelAndView toAddMesSpcMonitorRule(@PathVariable Long id,HttpServletRequest request) throws JsonProcessingException {
		ModelAndView modelAndView = new ModelAndView("spcMonitor/spcMonitorRuleAdd");
		modelAndView.addObject("mesSpcMonitorConfigs", mesSpcMonitorConfigService.findAll());
		MesSpcMonitor mesSpcMonitor = new MesSpcMonitor();
		if(id != 0){
			mesSpcMonitor = mesSpcMonitorService.findById(id);
		}
		modelAndView.addObject("mesSpcMonitor", mesSpcMonitor);
		return modelAndView;
	}
	@RequiresPermissions(value={"spcMonitor:save","spcMonitor:edit"},logical=Logical.OR)
	@RequestMapping("/saveMesSpcMonitor")
	@ResponseBody
	public String saveMesSpcMonitor(@Valid MesSpcMonitor mesSpcMonitor, HttpServletRequest request) {
		String msg = "添加";
		if (mesSpcMonitor.getId() != 0) {
			msg = "修改";
		}
		MesSpcMonitor savedMesSpcMonitor = mesSpcMonitorService.saveOrUpdate(mesSpcMonitor);
		if(null!= savedMesSpcMonitor){
			return AjaxObject.newOk(msg + "SPC告警成功!").toString();
		}
		return AjaxObject.newError(msg + "SPC告警失败!").setCallbackType("").toString();
	}
}
