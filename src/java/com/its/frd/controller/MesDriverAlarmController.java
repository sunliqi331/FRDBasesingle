package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.shiro.ShiroUser;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesDriverAlarmDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverAlarm;
import com.its.frd.entity.MesProductline;
import com.its.frd.entity.Usercompanys;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverAlarmService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesProductlineService;
import com.its.frd.service.UsercompanysService;


@Controller
@RequestMapping("/MesDriverAlarm")
public class MesDriverAlarmController {

    private final String PAGEPRE = "Alarm/";
    
    @Resource
    private MesDriverAlarmService mesDriverAlarmService;
    
    @Resource
    private MesDriverService mesDriverService;
    
    @Resource
    private UsercompanysService usercompanysService;
    
    @Resource
    private MesDriverAlarmDao mdaDao;
    
    @Resource
    private CompanyinfoService cmp;
    
    @Resource
    private MesProductlineService mesd;

    @RequiresPermissions("MesDriverAlarm:show")
    @RequestMapping("/MesDriverAlarmList")
    public ModelAndView MesDriverAlarmList() {
    	ModelAndView modelAndView = new ModelAndView(PAGEPRE + "MesDriverAlarmList");
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid();
    	List<MesDriver> drivers = mesDriverService.findByCompanyidAndDifferencetype(companyId, "1");
    	modelAndView.addObject("drivers", drivers);
    	modelAndView.addObject("company",cmp.getTreeFactory());
		if(companyId == null)
			return new ModelAndView("error/403");
        return modelAndView;
    }
    
    /**
     * 通过request中的一个特定的参数值来判断是获取设备列表
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/data")
    @ResponseBody
    public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Long> ids = new ArrayList<Long>();
        List<MesDriverAlarm> mesDriverAlarms = new ArrayList<MesDriverAlarm>();
        String selectedDrivers = request.getParameter("mesDriver");
        if (selectedDrivers==null||selectedDrivers.equals("")) {
        	List<MesDriver> mesDrivers = this.getDriver(request, page);//
            for (MesDriver mesDriver : mesDrivers) {
                ids.add(mesDriver.getId());
            }
		}else {
			ids.add(Long.parseLong(selectedDrivers));
		}
        page.setOrderField("updatetime");
        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        if(ids.size() > 0){
            Specification<MesDriverAlarm> specification = DynamicSpecifications.bySearchFilter(request, MesDriverAlarm.class
                    ,new SearchFilter("mesDriver.id",Operator.IN,ids.toArray()));
            mesDriverAlarms = mesDriverAlarmService.findPage(specification, page);
            for(MesDriverAlarm mesDriverAlarm : mesDriverAlarms){
                if(mesDriverAlarm.getStatecode().equals("1")){
                    mesDriverAlarm.setTstatecode("超过上限值");
                }
                if(mesDriverAlarm.getStatecode().equals("-1")){
                    mesDriverAlarm.setTstatecode("低于下限值");
                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        map.put("mesDriverAlarms", mesDriverAlarms);
        return mapper.writeValueAsString(map);
    }
    
    /**
     * 根据参数来判断是获取登录用户公司的设备列表,或是和该用户关联所有公司的设备
     * @return
     */
    private List<MesDriver> getDriver(HttpServletRequest request,	 Page page){
    	ShiroUser shiroUser = SecurityUtils.getShiroUser();
    	//用户登录的公司
    	if(shiroUser.getCompanyid() != null){
    		return mesDriverService.findByCompanyidAndDifferencetype(shiroUser.getCompanyid(),"1");
    	//用户关联的所有公司
    	}else if(shiroUser.getCompanyid() == null){
    	    List<Long> companyinfoIds = new ArrayList<>();
    	    List<MesDriver> mesDrivers = new ArrayList<MesDriver>();
    	    List<Usercompanys> usercompanys = usercompanysService.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
    	    for(Usercompanys usercompany : usercompanys){
    	        companyinfoIds.add(usercompany.getCompanyinfo().getId());
    	    }
    	    if(companyinfoIds.size() > 0){
    	        Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class
    	                ,new SearchFilter("companyinfo.id",Operator.IN,companyinfoIds.toArray())
    	                );
    	        mesDrivers = mesDriverService.findPage(specification, page);
    	    }
    		return mesDrivers;
    	//异常返回空
    	}else{
    		return null;
    	}
    }

    
}

