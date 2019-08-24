package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.exception.ServiceException;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverrepair;
import com.its.frd.entity.MesProductline;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesDriverrepairService;
import com.its.frd.service.MesProductlineService;

@Controller
@RequestMapping("/driverRepair")
public class MesDriverrepairController {
	private final String PRE_PAGE = "driver/";
	
	@Resource
	private MesDriverrepairService driRServ;
	@Resource
	private MesDriverService driServ;
	@Resource
	private MesProductlineService mplServ;
	@Resource
	private CompanyinfoService cpServ;
	
	@RequiresPermissions("driverRepair:view")
    @RequestMapping("/driverMaintainList")
    public String driverMaintainList (Page page,Map<String, Object> map,HttpServletRequest request) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		List<MesProductline> mesProductlines = new ArrayList<MesProductline>();
		if(companyId == null)
			return "error/403";
	    List<Long> ids = new ArrayList<Long>();
	    List<Companyinfo> companyinfos = cpServ.getTreeFactory();
	    for(Companyinfo companyinfo : companyinfos){
	        ids.add(companyinfo.getId());
	    }
	    ids.add(SecurityUtils.getShiroUser().getCompanyid());
	    if(ids.size() > 0){
	        Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class,
	                new SearchFilter("companyinfo.parentid",Operator.IN,ids.toArray()));
	        mesProductlines = mplServ.findPage(specification,page);
	    }
        map.put("mesProductlines", mesProductlines);
        return PRE_PAGE + "driverMaintainList";
    }
    
    @RequiresPermissions("driverRepair:save")
    @RequestMapping("/addDriverMaintain")
    public String addDriverMaintain (Page page,Map<String, Object> map,HttpServletRequest request) {
        List<Long> ids = new ArrayList<Long>();
        List<MesProductline> productline = new ArrayList<MesProductline>();
        List<Companyinfo> companyinfos = cpServ.getTreeFactory();
        for(Companyinfo companyinfo : companyinfos){
            ids.add(companyinfo.getId());
        }
        ids.add(SecurityUtils.getShiroUser().getCompanyid());
        if(ids.size() > 0){
            Specification<MesProductline> specification = DynamicSpecifications.bySearchFilter(request, MesProductline.class,
                    new SearchFilter("companyinfo.parentid",Operator.IN,ids.toArray()));
            productline = mplServ.findPage(specification,page);
        }
        map.put("productline", productline);
        return PRE_PAGE + "addDriverMaintain";
    }
	
	/**
	 * 分页
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverRepairData")
	@ResponseBody
	public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesDriverrepair> specification1 = DynamicSpecifications.bySearchFilter(request, MesDriverrepair.class,
//	            new SearchFilter("mesDriver.companyinfo.status", Operator.EQ, "1"),
	            new SearchFilter("mesDriver.companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()));
		List<MesDriverrepair> mds = driRServ.findPage(specification1, page);
		ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesDriverrepairs", mds);
		return  mapper.writeValueAsString(map);
	}
	
	/**
	 * 添加数据
	 * @param MesDriverrepair
	 * @return
	 */
	@RequiresPermissions("driverRepair:save")
	@RequestMapping("/saveDriverRepair")
	@ResponseBody
	public String saveDriver(MesDriverrepair mesDriverrepair,HttpServletRequest request){
	    if(mesDriverrepair.getMesDriver().getId() == null){
	        return AjaxObject.newError("添加失败!").setCallbackType("").toString();
	    }
		try {
			MesDriver driver = driServ.findById(mesDriverrepair.getMesDriver().getId());
			mesDriverrepair.setMesDriver(driver);
			driRServ.saveOrUpdate(mesDriverrepair);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("添加失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("添加成功!").toString();
	}
	
	
	@RequiresPermissions("driverRepair:delete")
    @RequestMapping(value = "/deleteDriverRepair", method = RequestMethod.POST)
    public @ResponseBody String deleteDriverRepair(Long[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                MesDriverrepair mesDriverrepair = driRServ.findById(ids[i]);
                driRServ.deleteById(mesDriverrepair.getId());
            }
        } catch (ServiceException e) {
            return AjaxObject.newError("删除失败!").setCallbackType("").toString();
        }
        return AjaxObject.newOk("删除成功！").setCallbackType("").toString();
    }
    
	@RequestMapping("/getDriversByProductline/{productlineId}")
    public @ResponseBody String getDriversByCompany(@PathVariable Long productlineId,HttpServletRequest request,Page page) throws JsonProcessingException{
	    Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class,
                new SearchFilter("mesProductline.id", Operator.EQ, productlineId));
	    List<MesDriver> mesDrivers = driServ.findPage(specification, page);
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	    if(mesDrivers.size() < 1){
	        return mapper.writeValueAsString(null);
	    }
        return mapper.writeValueAsString(mesDrivers);
	}
	
	/**
	 * 根据id查找
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("driverRepair:edit")
	@RequestMapping("/findById/{id}")
	public String findByID(@PathVariable Long id,Model model,Page page,Map<String,Object> map ){
		MesDriverrepair repair = driRServ.findById(id);
		String startDate = repair.getStarttime().toString();
		startDate = startDate.substring(0,19);
		repair.setStartDate(startDate);
		String endDate = repair.getEndtime().toString();
		endDate = endDate.substring(0, 19);
		repair.setEndDate(endDate);
		model.addAttribute("mesDriverrepair",repair);
        map.put("MesDriver", driRServ.findById(id).getMesDriver());
        map.put("company", driRServ.findById(id).getMesDriver().getCompanyinfo());
		return PRE_PAGE + "editDriverMaintain";
	}
	
	@RequiresPermissions("driverRepair:edit")
	@ModelAttribute("preloadMesDriverrepair")
    public MesDriverrepair preload(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            MesDriverrepair mesDriverrepair = driRServ.findById(id);
            return mesDriverrepair;
        }
        return null;
    }
    
	@RequiresPermissions("driverRepair:edit")
    @RequestMapping(value = "/editMesDriverrepair", method = RequestMethod.POST)
    public @ResponseBody String editMesDriverrepair(@Valid @ModelAttribute("preloadMesDriverrepair") MesDriverrepair mesDriverrepair) {
        try{
            driRServ.saveOrUpdate(mesDriverrepair);
        }catch(Exception e){
            return AjaxObject.newError("修改设备维修失败").setCallbackType("").toString();
        }
        return AjaxObject.newOk("修改设备维修成功！").toString();
    }
	
}
