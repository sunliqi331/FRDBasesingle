package com.its.frd.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductAlarm;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.MesProductline;
import com.its.frd.entity.Usercompanys;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverPointService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesProcedurePropertyService;
import com.its.frd.service.MesProductAlarmService;
import com.its.frd.service.MesProductProcedureService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.MesProductlineService;
import com.its.frd.service.UsercompanysService;
import com.its.statistics.service.StatisticsService;


@Controller
@RequestMapping("/MesProductAlarm")
public class MesProductAlarmController {

    private final String PAGEPRE = "Alarm/";
    
    @Resource
    private MesProductAlarmService mesProductAlarmService;
    
    @Resource
    private MesProductService mesProductService;
    
    @Resource
    private UsercompanysService usercompanysService;
    
    @Resource
    private MesProductlineService mesProductlineService;
    
    @Resource
	private CompanyinfoService cpServ;
    
    @Resource
    private MesDriverService mesDriverService;
    
    @Resource
    private MesDriverPointService mesDriverPointService;
    
    @Resource
	private StatisticsService statisticsService;
    
    @Resource
	private MesProductProcedureService mesProductProcedureService;
    
    @Resource
	private MesProcedurePropertyService mesProcedurePropertyService;

    
    @RequiresPermissions("MesProductAlarm:show")
    @RequestMapping("/MesProductAlarmList")
    public ModelAndView MesProductAlarmList() {
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return new ModelAndView("error/403");
		ModelAndView modelAndView = new ModelAndView(PAGEPRE + "MesProductAlarmList");
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		modelAndView.addObject("companyinfos",cpServ.getTreeFactory());
		
		/*modelAndView.addAllObjects("companyinfos");*/
		return modelAndView.addObject("productLineSelect", statisticsService.generateProductLineSelect(companyId));
   
    }

    @RequestMapping("/data")
    @ResponseBody
    public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
	
      /*  Map<String, Object> map = new HashMap<String, Object>();
        List<MesProductAlarm> mesProductAlarms = new ArrayList<MesProductAlarm>();
        String parameter = request.getParameter("search_EQ_mesProcedureProperty.mesProductProcedure.procedurenum");
        Enumeration<String> parameterNames = request.getParameterNames();
       page.setOrderField("updatetime");
       page.setOrderField("mesProcedureProperty.mesProductProcedure.mesProduct.name");
        page.setOrderField("mesProcedureProperty.mesProductProcedure.procedurenum");
        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        List<MesProduct> mesProducts = this.getProduct(request, page);
        List<String> ids = new ArrayList<String>();
        for(MesProduct mesProduct : mesProducts){
            ids.add(mesProduct.getModelnum());
        }
        Specification<MesProductAlarm> specification;
        List<SearchFilter> filters = new ArrayList<>();
        if(ids.size() > 0){
    		String 	 = request.getParameter("productLineName");
        	
        	filters.add(new SearchFilter("companyname",Operator.EQ,cpServ.findById(SecurityUtils.getShiroUser().getCompanyid()).getCompanyname()));
        	filters.add(new SearchFilter("productmodelnum",Operator.IN,ids.toArray()));
        	filters.add(new SearchFilter("companyId",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
        	if (!(productLineName==null||productLineName.isEmpty())) {
		        List<Long> points = this.getPoints(productLineName);
            	filters.add(new SearchFilter("pointid",Operator.IN,points.toArray()));
			}
            String mesProcedureStr = request.getParameter("mesProcedure");

        	if (!(mesProcedureStr==null||mesProcedureStr.isEmpty())) {
        		Long mesProcedure = Long.valueOf(request.getParameter("mesProcedure"));
                List<MesProcedureProperty> mesProcedureProperties = mesProcedurePropertyService.findByMesProductProcedure(mesProductProcedureService.findById(mesProcedure));
                
                List<Long> procedurePropertiesIds = new ArrayList<>();
                for (MesProcedureProperty mesProcedureProperty : mesProcedureProperties) {
                	procedurePropertiesIds.add(mesProcedureProperty.getId());
        		}
                filters.add(new SearchFilter("mesProcedureProperty.id",Operator.IN,procedurePropertiesIds));
			}
        	specification = DynamicSpecifications.bySearchFilter(request, MesProductAlarm.class,filters);
        	
            mesProductAlarms = mesProductAlarmService.findPage(specification, page);
            for(MesProductAlarm MesProductAlarm : mesProductAlarms){
                if(MesProductAlarm.getStatecode().equals("1")){
                    MesProductAlarm.setTstatecode("超过上限值");
                }
                if(MesProductAlarm.getStatecode().equals("-1")){
                    MesProductAlarm.setTstatecode("低于上限值");
                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        map.put("mesProductAlarms", mesProductAlarms);
        return mapper.writeValueAsString(map);*/
    	List<MesProductAlarm> list =new ArrayList<MesProductAlarm>();
    	Map<String,Object> map =new HashMap<String,Object>();
    	List<SearchFilter> filters = new ArrayList<>();
    	 page.setOrderField("updatetime");
/*    	filters.add(new SearchFilter("companyname",Operator.EQ,cpServ.findById(SecurityUtils.getShiroUser().getCompanyid())));
*/    
    	 String productLineName = request.getParameter("productLineName");
    	 if (!(productLineName==null||productLineName.isEmpty())) {
		        List<Long> points = this.getPoints(productLineName);
         	filters.add(new SearchFilter("pointid",Operator.IN,points.toArray()));
			}  
    	/* String chooseCompanyName =request.getParameter("chooseCompanyName");
    	 Long company =Long.parseLong(chooseCompanyName);
    	 if(!(company==null)){
    		 List<Long> pointss =this.getPoint(company);
    		 filters.add(new SearchFilter("pointid",Operator.IN,pointss.toArray()));
    	 }*/
    	 filters.add(new SearchFilter("mesProcedureProperty.mesProductProcedure.mesProduct.companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
    	 Specification<MesProductAlarm> specication= DynamicSpecifications.bySearchFilter(request, MesProductAlarm.class, filters);
    	list= mesProductAlarmService.findPage(specication, page);
    	 for(MesProductAlarm MesProductAlarm : list){
             if(MesProductAlarm.getStatecode().equals("1")){
                 MesProductAlarm.setTstatecode("超过上限值");
             }
             if(MesProductAlarm.getStatecode().equals("-1")){
                 MesProductAlarm.setTstatecode("低于上限值");
             }
         }
    	 ObjectMapper mapper = new ObjectMapper();
 		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

 		map.put("page", page);
 		map.put("mesProductAlarms", list);
 		return mapper.writeValueAsString(map);
    }

    private List<Long> getPoints(String productLineName ) {
    	List<Long> result = new ArrayList<Long>();
		MesProductline mesProductline = mesProductlineService.findByLinename(productLineName);
		List<MesDriver> mesDrivers =  mesDriverService.findByMesProductlineid(mesProductline.getId());
		for (MesDriver mesDriver : mesDrivers) {
			List<MesDriverPoints> mesDriverPoints = mesDriverPointService.findByMesDriver(mesDriver);
			for (MesDriverPoints mesDriverPoint : mesDriverPoints) {
				result.add(mesDriverPoint.getMesPoints().getId());
			}
		}
		return result;
	}
  /*  private List<Long> getPoint(Long chooseCompanyName){
    	List<Long> result =new ArrayList<Long>();
    	Companyinfo company = cpServ.findById(chooseCompanyName);
    	List<MesDriver> mesDrivers =  mesDriverService.findByMesProductline(company.getId());
    	for (MesDriver mesDriver : mesDrivers) {
			List<MesDriverPoints> mesDriverPoints = mesDriverPointService.findByMesDriver(mesDriver);
			for (MesDriverPoints mesDriverPoint : mesDriverPoints) {
				result.add(mesDriverPoint.getMesPoints().getId());
			}
		}
		return result;
	}*/
	/**
     * 根据参数来判断是获取登录用户公司的设备列表,或是和该用户关联所有公司的设备
     * @return
     */
    private List<MesProduct> getProduct(HttpServletRequest request, Page page){
        //用户登录的公司
    	
    	String productName = request.getParameter("productName");

        if(SecurityUtils.getShiroUser().getCompanyid() != null){
        	if(productName==null||productName.isEmpty()){
        		return mesProductService.findByCompanyinfo((SecurityUtils.getShiroUser().getCompanyid()));
        	}else {
        		return mesProductService.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid(),productName);
			}
            
        //用户关联的所有公司
        }else if(SecurityUtils.getShiroUser().getCompanyid() == null){
            List<Long> companyinfoIds = new ArrayList<>();
            List<MesProduct> mesProducts = new ArrayList<MesProduct>();
            List<Usercompanys> usercompanys = usercompanysService.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
            for(Usercompanys usercompany : usercompanys){
                companyinfoIds.add(usercompany.getCompanyinfo().getId());
            }
            if(companyinfoIds.size() > 0){
                Specification<MesProduct> specification = DynamicSpecifications.bySearchFilter(request, MesProduct.class
                        ,new SearchFilter("companyinfo.id",Operator.IN,companyinfoIds.toArray())
                        );
                mesProducts = mesProductService.findPage(specification, page);
            }
            return mesProducts;
        //异常返回空
        }else{
            return null;
        }
    }
    @RequestMapping("/getprocedureForProduct/{productid}")
    @ResponseBody
    public String getprocedureForProduct(@PathVariable Long productid) throws JsonProcessingException{
    	List<MesProductProcedure> mesProductProcedures = mesProductService.findById(productid).getMesProductProcedures();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesProductProcedures);
    }
}

