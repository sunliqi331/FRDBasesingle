package com.its.frd.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.CompanyinfoDao;
import com.its.frd.echarts.entity.BarChart;
import com.its.frd.echarts.entity.BarChart.SeriesItem;
import com.its.frd.echarts.entity.BaseChart;
import com.its.frd.echarts.entity.BaseChart.LegendEnum;
import com.its.frd.echarts.entity.BaseChart.OptionEnum;
import com.its.frd.echarts.entity.BaseChart.SeriesEnum;
import com.its.frd.echarts.entity.BaseChart.TitleEnum;
import com.its.frd.echarts.entity.BaseChart.seriesDataEnum;
import com.its.frd.echarts.entity.BaseChart.xAxis_yAxisEnum;
import com.its.frd.echarts.entity.PieChart;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDataWeg;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverOEE;
import com.its.frd.entity.MesDriverStats;
import com.its.frd.entity.MesDrivertypeProperty;
import com.its.frd.entity.MesEnergy;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductline;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDataWegService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesDriverTypePropertyService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.MesProductionService;
import com.its.frd.service.MesStatsService;
import com.its.frd.service.OEEService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.BarKeyType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.EnergyType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.PassType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.TypeScope;
import com.its.frd.service.impl.ProductAndEnergyAndDriverChartServiceImp;
import com.its.frd.util.DateUtils;
import com.its.frd.util.ExcelFileGenerator;
import com.its.frd.util.HBasePageModel;
import com.its.frd.util.JSONUtils;
import com.its.statistics.vo.DriverRecord;
import com.its.statistics.vo.PropertyTrendSearch;

/**
 * 统计功能
 */

@Controller
@RequestMapping("/stats")
public class MesStatsController {
	public enum KeyType{
		Option,Count,PassCount
	}
	
	@Resource
	private ProductAndEnergyAndDriverChartService productChartServ;
	@Resource
	private MesProductService mpServ;
	@Resource
	private CompanyinfoService cpServ;
	@Resource
	private OEEService oeeService;
	@Resource
	private MesDriverService mesDriverService;
	@Autowired
	private MesProductService mps;
	@Autowired
	private MesProductionService mesProductionService;
	@Resource
	private CompanyfileService cfServ;
	@Autowired
	private RedisService redisService;
	@Resource
	private CompanyinfoDao cpDao;
	@Autowired
	private MesDriverTypePropertyService mesDriverTypePropertyService;
	@Autowired
	private MesDataWegService mesDataWegService;
    @Autowired
	private MesStatsService mesStatsService;
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public Map<BaseChart.OptionEnum, Object> toCountPage(){
		PieChart pie = new PieChart();
		pie.title.put(BaseChart.TitleEnum.TEXT, "饼图测试");
		return pie.option;
	}
	
	 /*
     * 检索某一设备的能耗
     * 
     * */
    @RequestMapping("/searchDriverenergy")
    @ResponseBody
    public String SearchDriverEnergy(HttpServletRequest reuqest , Page page,Long driverid,String energyType,
    		Timestamp startDate,Timestamp endDate) throws JsonProcessingException{
    	Map<String, Object> map = new HashMap<String, Object>();
//    	SearchFilter classFilter = new SearchFilter("mesDriver.id", Operator.EQ, driverid);
//    	SearchFilter energyType2 =new SearchFilter("energytype",Operator.EQ,energyType);
//    	SearchFilter timeFilter1 = new SearchFilter("updatetime", Operator.GT, startDate);
//    	SearchFilter timeFilter2 = new SearchFilter("updatetime", Operator.LT, endDate);
//    	Specification<MesEnergy> bySearchFilter;
//    	bySearchFilter =DynamicSpecifications.bySearchFilter(reuqest,MesEnergy.class, energyType2,
//    			classFilter,timeFilter1,timeFilter2);
//    	List<MesEnergy> Driverenergy= mesDriverService.findenergyPage(bySearchFilter, page);

        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(driverid)));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(startDate.getTime()).replace("000", "")));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(endDate.getTime()).replace("000", "")));
        String searchEnergyType = "";
        if("electric".equals(energyType)) {
            searchEnergyType = "ELECTRIC";
        } else if("gas".equals(energyType)) {
            searchEnergyType = "GAS";
        } else if("water".equals(energyType)) {
            searchEnergyType = "WATER";
        }
        searchList.add(new SearchFilter("type", Operator.EQ, searchEnergyType));
        Specification<MesDataWeg> specification = DynamicSpecifications
                .bySearchFilter(MesDataWeg.class, searchList);
        page.setOrderField("mesDataMultiKey.insertTimestamp");
        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        List<MesDataWeg> mesDataWegList = mesDataWegService.findPage(specification, page);
        List<MesEnergy> Driverenergy = Lists.newArrayList();
        MesDriver o = mesDriverService.findById(driverid);
        MesEnergy mesEnergy = null;
        for(MesDataWeg obj : mesDataWegList) {
            mesEnergy = new MesEnergy();
            mesEnergy.setEnergytype(obj.getType());
            mesEnergy.setMesDriver(o);
            mesEnergy.setValue(Double.valueOf(obj.getMetaValue()));
            String updateTimeStr = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());
            updateTimeStr = updateTimeStr + "000";
            updateTimeStr = DateUtils.unixTimestampToDate(Long.valueOf(updateTimeStr));
            mesEnergy.setUpdatetime(Timestamp.valueOf(updateTimeStr));
            Driverenergy.add(mesEnergy);
        }

    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("Driverenergy", Driverenergy);
		map.put("page", page);
    	return mapper.writeValueAsString(map);
    }
    
    /**
     * 初始化检索能耗表格
     * 检索某一能耗下所有数据
     */
    @RequestMapping("/listAlldriverenergy")
    @ResponseBody
    public String listdriverEnergy(String energyType,HttpServletRequest request, Page page,TypeScope typeScope,
    		Long id,Timestamp startDate,Timestamp endDate) throws JsonProcessingException{
		Map<String,Object> map =new HashMap<String,Object>();
		List<MesDriver> driverList = new ArrayList<>();
		if(TypeScope.productline.equals(typeScope)){
			//当统计范围是产线时
			List<MesProductline> lines = new ArrayList<>();
			lines.add(new MesProductline(id));
			driverList = productChartServ.getDriverListForProductLineList(lines);
		}else if(TypeScope.factory.equals(typeScope)){
			//当统计范围是工厂时
			//查询出该工厂下所有的产线
			Companyinfo factorys = cpDao.findOne(id);
			List<MesProductline> lines = productChartServ.getProductlineOfFactory(factorys);
			driverList = productChartServ.getDriverListForProductLineList(lines);
		}else if(TypeScope.company.equals(typeScope)){
			//当统计范围是公司时
			//查询公司下一共多少工厂
		    id = SecurityUtils.getShiroUser().getCompanyid();
			List<Companyinfo> factorys = cpDao.findByParentid(id);
			List<MesProductline> lines = productChartServ.getProductlineOfFactory(factorys);
			driverList = productChartServ.getDriverListForProductLineList(lines);			
		}
		if(driverList.size()<1){
			return null;
		}
		List<Long> driverIds = new ArrayList<Long>();
		for(MesDriver md : driverList){
			driverIds.add(md.getId());
		}
//    	Specification<MesEnergy> specification = DynamicSpecifications.bySearchFilter(request, MesEnergy.class,
//    			new SearchFilter("energytype", Operator.EQ, energyType),
//    			new SearchFilter("updatetime", Operator.GT, startDate),
//    			new SearchFilter("updatetime", Operator.LT, endDate),
//    			new SearchFilter("mesDriver.id", Operator.IN, driverIds.toArray()));
//		List<MesEnergy> DriverEnergy = mesDriverService.findenergyPage(specification, page);
		
        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.IN, driverIds.toArray()));
        String time = String.valueOf(startDate.getTime());
        if(time.contains("000000"))
            time = time.replace("000000", "000");
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,time));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(endDate.getTime()).replace("000", "")));
        String searchEnergyType = "";
        if("electric".equals(energyType)) {
            searchEnergyType = "ELECTRIC";
        } else if("gas".equals(energyType)) {
            searchEnergyType = "GAS";
        } else if("water".equals(energyType)) {
            searchEnergyType = "WATER";
        }
        searchList.add(new SearchFilter("type", Operator.EQ, searchEnergyType));
        Specification<MesDataWeg> specification = DynamicSpecifications
                .bySearchFilter(MesDataWeg.class, searchList);
        page.setOrderField("mesDataMultiKey.insertTimestamp");
        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        List<MesDataWeg> mesDataWegList = mesDataWegService.findPage(specification, page);
        List<MesEnergy> Driverenergy = Lists.newArrayList();
        Map<Long, MesDriver> driverMap = Maps.newHashMap();
        for(MesDriver driver : driverList) {
            driverMap.put(driver.getId(), driver);
        }
//        MesDriver o = mesDriverService.findById(driverid);
        MesEnergy mesEnergy = null;
        for(MesDataWeg obj : mesDataWegList) {
            mesEnergy = new MesEnergy();
            mesEnergy.setEnergytype(obj.getType());
            mesEnergy.setMesDriver(driverMap.get(obj.getMesDataMultiKey().getDriverId().longValue()));
            mesEnergy.setValue(Double.valueOf(obj.getMetaValue()));
            String updateTimeStr = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());
            updateTimeStr = updateTimeStr + "000";
            updateTimeStr = DateUtils.unixTimestampToDate(Long.valueOf(updateTimeStr));
            mesEnergy.setUpdatetime(Timestamp.valueOf(updateTimeStr));
            Driverenergy.add(mesEnergy);
        }
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("DriverEnergy", Driverenergy);
		map.put("page", page);
		return mapper.writeValueAsString(map);	
    }

    @RequestMapping(value="/driverPropertyValues", method={RequestMethod.GET, RequestMethod.POST})
    public String driverPropertyValues(Map<String, Object> map,HttpServletRequest request,Page page) {
        Long companyId = SecurityUtils.getShiroUser().getCompanyid();
        if(companyId == null)
            return "error/403";
        Companyinfo companyinfo = cpServ.findById(SecurityUtils.getShiroUser().getCompanyid());
        map.put("mesProduct", mpServ.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid()));
        map.put("companyinfos", cpServ.getTreeFactory());
        map.put("company", companyinfo);
    	if(null != request.getSession().getAttribute("prePageRowKey")) {
    		request.getSession().removeAttribute("prePageRowKey");
    	}
        return "driver/driverPropertyValues";
    }
    @RequestMapping(value="/driverStats")
    public String driverStats(Map<String, Object> map,HttpServletRequest request,Page page) {
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid();
    	if(companyId == null)
    		return "error/403";
    	Companyinfo companyinfo = cpServ.findById(SecurityUtils.getShiroUser().getCompanyid());
    	map.put("mesProduct", mpServ.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid()));
    	map.put("companyinfos", cpServ.getTreeFactory());
    	map.put("company", companyinfo);
    	return "driver/driverStatsBar";
    }
	
    /**
     * 设备产量统计直方图
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param driverId  设备主键id
     * @return
     */
    /*@RequestMapping("/driverStatsBarChartForTotal")
	@ResponseBody
	public Map<BaseChart.OptionEnum, Object> driverStatsBarChartOptionDataForTotal(Timestamp startDate,Timestamp endDate,
			Long driverId,Long productLineId){
		BarChart barChart = new BarChart();
		Map<BarKeyType, Object> resultMap = productChartServ.getDriverStatsBarChart(startDate, endDate, driverId,productLineId, null);
		if(resultMap == null)
			return null;
		Object obj = resultMap.get(BarKeyType.xAxisOfData);
		List<String> xAxis_Data = null;
		if(obj != null)
			xAxis_Data = (List<String>)obj;
		barChart.getxAxis_data().put(xAxis_yAxisEnum.data, xAxis_Data);
		
		 * x轴对应的数据集合
		 * List<SeriesItem>集合从service获得;
		 * series中的元素与legend中的data需要一一对应;
		 
		obj = resultMap.get(BarKeyType.seriesItemList);
		if(obj != null)
			barChart.getOption().put(OptionEnum.SERIES, (List<SeriesItem>) obj);
		barChart.getTitle().put(TitleEnum.TEXT, "产能直方图");
		return barChart.getOption();
	}*/
    
    
    /**
     * 设备产量统计表，统计每一天的设备产量，时间范围大于一天
     * @return
     * @throws ParseException 
     * @throws JsonProcessingException 
     */
    @RequestMapping("/driverOutputOfDay")
	@ResponseBody
	public String driverOutputOfDay(Timestamp startDate,Timestamp endDate,
			Long driverId,Page page) throws ParseException, JsonProcessingException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	List<String> allday = mesDriverService.getAllDays(sdf.format(startDate), sdf.format(endDate));
    	List<String> allDays = new ArrayList<String>();
    	for(int i = allday.size()-1;i>=0;i--){
    		allDays.add(allday.get(i));
    	}
    	List<MesDriverStats> statsList = new ArrayList<MesDriverStats>();
    	MesDriver driver = mesDriverService.findById(driverId);
    	Long id = 1L;
    	for(String s : allDays){
    		//计算设备当前天的总产量
    		List<MesDriverStats> list = mesDriverService.findDriverOutput(Timestamp.valueOf(s+" 00:00:00"), 
    				Timestamp.valueOf(s+" 23:59:59"), driverId);
    		Long total = 0L;
    		//如果是当天，需要从redis中取出今天最新的数据
    		String nowDay = sdf.format(new Date()).split(" ")[0];
    		if(nowDay.equals(s)){
    			String mac = mesDriverService.findDriverRedisMac(driver);
    			if(StringUtils.isNotEmpty(mac)){
    				Map<String, Object> result = redisService.getHash(mac);
    				Object obj = result.get("P_COUNT");
    				if(obj!=null){
    					Long yield = Long.valueOf(String.valueOf(obj));//取出当前实时产量
    					total = total < yield ? yield : total;
    				}
    			}
    		}else if(!nowDay.equals(s)){
    			if(list!=null && list.size()>0){
            		for(MesDriverStats m : list){
            			total = total + m.getCount();
            		}
        		}else{
        			continue;
        		}
    		}
    		//为driverStats对象赋值
    		MesDriverStats mesDriverStats = new MesDriverStats();
    		mesDriverStats.setId(id);
    		mesDriverStats.setMesDriver(driver);
    		mesDriverStats.setUpdatetime(Timestamp.valueOf(s+" 00:00:00"));
    		mesDriverStats.setCount(total);
    		statsList.add(mesDriverStats);
    		id++;
    	}
    	if(statsList.size()<1){
    		return null;
    	}
    	page.setTotalCount(statsList.size());
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		//手动分页
		int perPage = page.getNumPerPage(); //每页显示数量
		int nowPage = page.getPageNum();//当前页码
		int startIndex = 0; //开始索引
		int endIndex = 0; //结束索引
		if(perPage<statsList.size()){
			startIndex = (nowPage-1)*perPage;
			endIndex = nowPage*perPage;
			if(endIndex>statsList.size()){
				endIndex = statsList.size();
			}
			List<MesDriverStats> subList = statsList.subList(startIndex, endIndex);
			map.put("driverOutputs", subList);
		}else{
			map.put("driverOutputs", statsList);
		}
    	map.put("page", page);
		return mapper.writeValueAsString(map);
	}
    
    /**
     * 设备产量统计表，统计每一小时的设备产量，时间范围处于一天之内
     * @return 
     * @throws ParseException 
     * @throws JsonProcessingException 
     */
    @SuppressWarnings("deprecation")
	@RequestMapping("/driverOutputOfHour")
	@ResponseBody
	public String driverOutputOfHour(Timestamp startDate,Timestamp endDate,
			Long driverId,Page page) throws ParseException, JsonProcessingException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesDriverStats> statsList = new ArrayList<MesDriverStats>();
		MesDriver driver = mesDriverService.findById(driverId);
		
		//时间准备
    	Date now = new Date();
    	String nowDay = sdf.format(now).split(" ")[0];
		long pre = sdf.parse(nowDay+" "+now.getHours()+":02:00").getTime();//当前时间上一个整点
		if((startDate.getTime()<=pre)&&(endDate.getTime()>pre)){ //判断是否需要从redis中取值
			List<MesDriverStats> tempList = new ArrayList<MesDriverStats>();
			tempList = mesDriverService.findDriverOutput(startDate, endDate, driverId);
			String mac = mesDriverService.findDriverRedisMac(driver);
			if(StringUtils.isNotEmpty(mac)){
				Map<String, Object> result = redisService.getHash(mac);
				Object obj = result.get("P_COUNT");
				if(obj!=null){
					Long yield = Long.valueOf(String.valueOf(obj));//取出当前实时产量
					long todayTotal = 0L; //计算今天截止到上一个整点的产量
					long aid = 1L;
					if(tempList.size()>0){
						for(MesDriverStats m : tempList){
    						todayTotal += m.getCount();
    					}
						aid = tempList.get(0).getId()+1;
					}
					MesDriverStats mesDriverStats = new MesDriverStats();
					mesDriverStats.setId(aid);
					mesDriverStats.setMesDriver(driver);
					mesDriverStats.setUpdatetime(new Timestamp(new Date().getTime()));
					mesDriverStats.setCount(yield-todayTotal);
					statsList.add(mesDriverStats);//把redis中取出的数据放在集合第一位
				}
				statsList.addAll(tempList);
			}else{
				statsList = tempList;
			}
		}else{
			statsList = mesDriverService.findDriverOutput(startDate, endDate, driverId);
		}
    	
    	if(statsList.size()<1){
    		return null;
    	}
    	page.setTotalCount(statsList.size());
    	//手动分页
		int perPage = page.getNumPerPage(); //每页显示数量
		int nowPage = page.getPageNum();//当前页码
		int startIndex = 0; //开始索引
		int endIndex = 0; //结束索引
		if(perPage<statsList.size()){
			startIndex = (nowPage-1)*perPage;
			endIndex = nowPage*perPage;
			if(endIndex>statsList.size()){
				endIndex = statsList.size();
			}
			List<MesDriverStats> subList = statsList.subList(startIndex, endIndex);
			map.put("driverOutputs", subList);
		}else{
			map.put("driverOutputs", statsList);
		}
    	map.put("page", page);
    	return mapper.writeValueAsString(map);
	}
    
    /**
     * 设备产量统计，一条一条的显示
     */
    @RequestMapping("/driverOutputByOne")
    @ResponseBody
    public String driverOutputByOne(Timestamp startDate,Timestamp endDate,String chooseFactory, String chooseProductLine,
			Long driverId,DateType dateType,HBasePageModel page) throws JsonProcessingException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<DriverRecord> driverRecordList = new ArrayList<DriverRecord>();
		driverRecordList = mesDriverService.getDriverOutputByHbase(startDate,endDate,chooseFactory,chooseProductLine, driverId,page, dateType);
		map.put("driverOutputs", driverRecordList);
		map.put("page", page);
    	return mapper.writeValueAsString(map);
    }
    
    
    /**
     * 设备属性历史记录
     * @param start 时间选择的起始时间
     * @param end 时间选择的终止时间
     * @param factoryId 工厂ID
     * @param productLineId 产线ID
     * @param driverId 设备ID
     * @param driverPropertyIds 设备属性数组
     * @param page HBASE选择模板类
     * @return 设备属性历史记录集合
     * @throws IOException 
     * 
     */
    @RequestMapping("/driverPropertyValuesHistory")
	@ResponseBody
    public String driverPropertyValuesHistory(HttpServletRequest reuqest ,Timestamp start,Timestamp end,
    		String factoryId, String productLineId, Long driverId,String driverPropertyIds,
    		HBasePageModel page) throws IOException{
    	//从数据库中获取rowkeys集合
//    	List<String> rowkeys =  mesDriverService.findDriverPropertyRowKeys(start,end,driverId,page);
//    	if(rowkeys==null || rowkeys.size()<1){
//    		return null;
//    	}
//    	HttpSession session = reuqest.getSession();
    	String prePageRowKey = "";
//    	if(null != session.getAttribute("prePageRowKey")) {
//    		prePageRowKey = String.valueOf(session.getAttribute("prePageRowKey"));
//    	}
    	List<Long> driverPropertyList = new ArrayList<Long>();
    	for(String s : driverPropertyIds.split(",")){
    		driverPropertyList.add(Long.parseLong(s));
    	}
    	List<Map<String, String>> list = mesDriverService.findDriverPropertyByHbase(start, end, factoryId,
    			productLineId, driverPropertyList, driverId, prePageRowKey, page);
//    	session.setAttribute("prePageRowKey", page.getPagePreStartRowkey());
//    	List<Map<String, String>> list = mesDriverService.findDriverPropertyByHbase(rowkeys,driverPropertyList,driverId);
		if(list==null || list.size()<1){
    		return null;
    	}
		Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("driverPropertys", list);
		map.put("page", page);
    	return mapper.writeValueAsString(map);
    }
    
    @RequestMapping("/getDriverPropertyList")
    @ResponseBody
    public String getDriverPropertyList(HttpServletRequest request, String driverPropertyIds) throws JsonProcessingException{
    	List<MesDrivertypeProperty> list = new ArrayList<MesDrivertypeProperty>();
    	String[] split = driverPropertyIds.split(",");
    	for(String s : split){
    		MesDrivertypeProperty property = mesDriverTypePropertyService.findById(Long.parseLong(s));
    		list.add(property);
    	}
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    	if(null != request.getSession().getAttribute("prePageRowKey")) {
    		request.getSession().removeAttribute("prePageRowKey");
    	}
    	return mapper.writeValueAsString(list);
    }
    
	/**
	 * 设备属性折线图
	 * @param startDate 开始时间
	 * @param endDate	结束时间
	 * @param driverPropertyId	设备属性id
	 * @return
	 */
	/*@RequestMapping("/driverPropertyValuesForLineChart")
	@ResponseBody
	public List<LineChartOptionModel> lineChartOptionDataForDriverProperty(Timestamp start,Timestamp end,String driverPropertyIds){
		System.out.println(start.getTime());
		System.out.println(end.getTime());
		System.out.println(driverPropertyIds);
		List<LineChartOptionModel> lineOptions = new ArrayList<>();
		for(String driverPropertyId : driverPropertyIds.split(",")){
			Map<LineElement,Object> resultMap = productChartServ.getDriverPropertyLineChart(start, end, Long.parseLong(driverPropertyId));
			if(resultMap == null)
				return null;
			LineChartOptionModel lineOption = new LineChartOptionModel();
			LineChartOptionModel.SeriesItem seriesItem = new LineChartOptionModel().new SeriesItem();
			Object obj = resultMap.get(LineElement.seriesData);
			if(obj != null)
				seriesItem.setData((List<Double>) obj);
			List<LineChartOptionModel.SeriesItem> series = new ArrayList<>();
			series.add(seriesItem);
			lineOption.setSeries(series);
			
			obj = resultMap.get(LineElement.xAxisData);
			List<String> xAxisData = null;
			if(obj != null){
				xAxisData = ((List<String>)obj);
				lineOption.getxAxis().put(XAxisElement.data, xAxisData);
			}
			obj = resultMap.get(LineElement.titleText);
			if(obj != null){
				lineOption.getTitle().put(TitleElement.text, (String)obj);
			}
			obj = resultMap.get(LineElement.unit);
			if(obj != null){
				lineOption.getTitle().put(TitleElement.text, lineOption.getTitle().get(TitleElement.text)+ "("+(String)obj +")");
			}
			lineOptions.add(lineOption);
		}
		return lineOptions;
	}*/
    
    /**
     * 设备运行时间柱状图
     * @param startDate
     * @param endDate
     * @param typeScope
     * @param energyType
     * @param driverId
     * @param dateType
     * @return
     */
    @RequestMapping("/driverRuntimeBarChart")
	@ResponseBody
	public Map<BaseChart.OptionEnum, Object> driverRuntimeBarChart(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,EnergyType energyType,Long id,DateType dateType,String ifIsDriver){
    	BarChart barChart = new BarChart();
		Map<BarKeyType, Object> resultMap;
		try {
			resultMap = productChartServ.getRuntimeDataForTotal(startDate, endDate, typeScope, id, dateType,ifIsDriver);
		} catch (ParseException e) {
			return null;
		}
		/*
		 * x轴时间集合
		 * xAxisOfData是从service中获取的时间段间隔集合.
		 */
		Object obj = resultMap.get(BarKeyType.xAxisOfData);
		List<String> xAxis_Data = null;
		if(obj != null)
			xAxis_Data = (List<String>)obj;//new ArrayList<>();
		barChart.getxAxis_data().put(xAxis_yAxisEnum.data, xAxis_Data);
		/*
		 * x轴对应的数据集合
		 * List<SeriesItem>集合从service获得;
		 * series中的元素与legend中的data需要一一对应;
		 */
		obj = resultMap.get(BarKeyType.seriesItemList);
		if(obj != null)
			barChart.getOption().put(OptionEnum.SERIES, (List<SeriesItem>) obj);
		barChart.getTitle().put(TitleEnum.TEXT, this.getEnergyName(energyType));
		/**
		 * 填充legend的数据
		 */
		obj = resultMap.get(BarKeyType.legendData);
		List<String> legendData = null;
		if(obj != null)
			legendData = (List<String>) obj;
		barChart.getLegend().put(LegendEnum.data, legendData);
		return barChart.getOption();
    }
    
	/**
	 * 能耗柱状图
	 * 某一个设备的能耗统计，X轴为时间段
	 * @param startDate
	 * @param endDate
	 * @param typeScope 公司,或工厂,或产线
	 * @param energyType 统计的能耗类型
	 * @param id        公司id,或工厂id,或产线id
	 * @param dateType  日期标准
	 * @return
	 */
	@RequestMapping("/energyBarChartForTotal")
	@ResponseBody
	public Map<BaseChart.OptionEnum, Object> energyBarChartOptionDataForTotal(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,EnergyType energyType,Long driverId,DateType dateType){
		BarChart barChart = new BarChart();
		Map<BarKeyType, Object> resultMap = productChartServ.getEnergyDataForTotal(startDate, endDate, energyType, driverId, dateType);
		/*
		 * x轴时间集合
		 * xAxisOfData是从service中获取的时间段间隔集合.
		 */
		Object obj = resultMap.get(BarKeyType.xAxisOfData);
		List<String> xAxis_Data = null;
		if(obj != null)
			xAxis_Data = (List<String>)obj;//new ArrayList<>();
		barChart.getxAxis_data().put(xAxis_yAxisEnum.data, xAxis_Data);
		/*
		 * x轴对应的数据集合
		 * List<SeriesItem>集合从service获得;
		 * series中的元素与legend中的data需要一一对应;
		 */
		obj = resultMap.get(BarKeyType.seriesItemList);
		if(obj != null)
			barChart.getOption().put(OptionEnum.SERIES, (List<SeriesItem>) obj);
		barChart.getTitle().put(TitleEnum.TEXT, this.getEnergyName(energyType));
		return barChart.getOption();
	}
	
	private String getEnergyName(EnergyType energyType){
		if(energyType == null){
			return "";
		}else if(EnergyType.gas.equals(energyType)){
			return "耗气量";
		}else if(EnergyType.water.equals(energyType)){
			return "耗水量";
		}else if(EnergyType.electric.equals(energyType)){
			return "耗电量";
		}
		return "电水气总和";
	}
	
	/**
	 * 能耗柱状图
	 * 所有设备，X轴以每个设备为柱状
	 * @param startDate
	 * @param endDate
	 * @param typeScope 公司,或工厂,或产线
	 * @param energyType 统计的能耗类型
	 * @param id        公司id,或工厂id,或产线id
	 * @param dateType  日期标准
	 * @return
	 */
	@RequestMapping("/energyBarChart")
	@ResponseBody
	public Map<BaseChart.OptionEnum, Object> energyBarChartOptionDataForDriver(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,EnergyType energyType,Long id,DateType dateType){
		BarChart barChart = new BarChart();
		Map<BarKeyType, Object> resultMap = productChartServ.getEnergyDataForDriver(startDate, endDate, typeScope, energyType, id, dateType);
		/*
		 * x轴时间集合
		 * xAxisOfData是从service中获取的时间段间隔集合.
		 */
		Object obj = resultMap.get(BarKeyType.xAxisOfData);
		List<String> xAxis_Data = null;
		if(obj != null)
			xAxis_Data = (List<String>)obj;//new ArrayList<>();
		barChart.getxAxis_data().put(xAxis_yAxisEnum.data, xAxis_Data);
		/*
		 * x轴对应的数据集合
		 * List<SeriesItem>集合从service获得;
		 * series中的元素与legend中的data需要一一对应;
		 */
		obj = resultMap.get(BarKeyType.seriesItemList);
		if(obj != null)
			barChart.getOption().put(OptionEnum.SERIES, (List<SeriesItem>) obj);
		barChart.getTitle().put(TitleEnum.TEXT, "能耗直方图");
		return barChart.getOption();
		
	}
	
	/**
	 * 能耗折线图
	 * X轴以电能或水能或气能为柱状
	 * @param startDate
	 * @param endDate
	 * @param typeScope 公司,或工厂,或产线
	 * @param energyType 统计的能耗类型
	 * @param id        公司id,或工厂id,或产线id
	 * @param dateType  日期标准
	 * @return
	 */
	//--
	/*@RequestMapping("/energyForLineChart")
	@ResponseBody
	public List<LineChartOptionModel> energyForLineChart(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,EnergyType energyType,Long id,DateType dateType){
		List<LineChartOptionModel> lineOptions = new ArrayList<>();		
			Map<LineElement,Object> resultMap = productChartServ.getenergyForLineChart(startDate, endDate, typeScope, energyType, id, dateType);
			if(resultMap == null)
				return null;
			LineChartOptionModel lineOption = new LineChartOptionModel();
			LineChartOptionModel.SeriesItem seriesItem = new LineChartOptionModel().new SeriesItem();
			Object obj = resultMap.get(LineElement.seriesData);
			if(obj != null)
				seriesItem.setData((List<Double>) obj);
			List<LineChartOptionModel.SeriesItem> series = new ArrayList<>();
			series.add(seriesItem);
			lineOption.setSeries(series);
			obj = resultMap.get(LineElement.xAxisData);
			List<String> xAxisData = null;
			if(obj != null){
				xAxisData = ((List<String>)obj);
				lineOption.getxAxis().put(XAxisElement.data, xAxisData);
			}
			obj = resultMap.get(LineElement.titleText);
			if(obj != null){
				lineOption.getTitle().put(TitleElement.text, this.getEnergyNam(energyType));
			}
			lineOptions.add(lineOption);
			return lineOptions;
			}
		private String getEnergyNam(EnergyType energyType){
		if(energyType == null){
			return "";
		}else if(EnergyType.gas.equals(energyType)){
			return "耗气量";
		}else if(EnergyType.water.equals(energyType)){
			return "耗水量";
		}else if(EnergyType.electric.equals(energyType)){
			return "耗电量";
		}
		return "电水气总和";
	}*/
		
	/**
	 * 合格率直图
	 * @param startDate
	 * @param endDate
	 * @param typeScope 公司,或工厂,或产线
	 * @param modelnum  产品型号(modelnum)
	 * @param passType
	 * @param id        公司id,或工厂id,或产线id
	 * @return
	 */
	/*@RequestMapping("/OkBarChart")
	@ResponseBody
	public Map<BaseChart.OptionEnum,Object> OkBarChartOptionData(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,String modelnum,PassType passType,Long id){
		BarChart barChart = new BarChart();
		Map<BarKeyType, Object> resultMap = productChartServ.getCountNum(startDate, endDate, typeScope, modelnum, passType, id);
		
		 * x轴时间集合
		 * xAxisOfData是从service中获取的时间段间隔集合.
		 
		Object obj = resultMap.get(BarKeyType.xAxisOfData);
		List<String> xAxis_Data = null;
		if(obj != null)
		xAxis_Data = (List<String>)obj;//new ArrayList<>();
		barChart.getxAxis_data().put(xAxis_yAxisEnum.data, xAxis_Data);
			
		 * x轴对应的数据集合
		 * List<SeriesItem>集合从service获得;
		 * series中的元素与legend中的data需要一一对应;
		 
		obj = resultMap.get(BarKeyType.seriesItemList);
		if(obj != null)
			barChart.getOption().put(OptionEnum.SERIES, (List<SeriesItem>) obj);
		barChart.getTitle().put(TitleEnum.TEXT, "合格率直方图");
		return barChart.getOption();
	}*/
	
	/**
	 * 产量柱状图
	 * @param startDate
	 * @param endDate
	 * @param typeScope 公司,或工厂,或产线
	 * @param modelnum  产品型号(modelnum)
	 * @param passType  
	 * @param id        默认为公司id
	 * @param dateType
	 * @return
	 */
	@RequestMapping("/productionBarChart")
	@ResponseBody
	public Map<BaseChart.OptionEnum,Object> productionBarChartOptionData(Timestamp startDate,Timestamp endDate
			,String modelnum,PassType passType,Long id,DateType dateType){
		BarChart barChart = new BarChart();
		Map<BarKeyType, Object> resultMap = productChartServ.getProductionData(startDate, endDate, TypeScope.company, modelnum, id, dateType);
		/*
		 * x轴时间集合
		 * xAxisOfData是从service中获取的时间段间隔集合.
		 */
		Object obj = resultMap.get(BarKeyType.xAxisOfData);
		List<String> xAxis_Data = null;
		if(obj != null)
			xAxis_Data = (List<String>)obj;//new ArrayList<>();
		barChart.getxAxis_data().put(xAxis_yAxisEnum.data, xAxis_Data);
		/*
		 * x轴对应的数据集合
		 * List<SeriesItem>集合从service获得;
		 * series中的元素与legend中的data需要一一对应;
		 */
		obj = resultMap.get(BarKeyType.seriesItemList);
		if(obj != null)
			barChart.getOption().put(OptionEnum.SERIES, (List<SeriesItem>) obj);
		/*
		 * legendData数据从service获得
		 */
		obj = resultMap.get(BarKeyType.legendData);
		List<String> legendData = null;
		if(obj != null)
			legendData = (List<String>) obj;
		barChart.getLegend().put(LegendEnum.data, legendData);
		barChart.getTitle().put(TitleEnum.TEXT, "产能直方图");
		return barChart.getOption();
	}
	
	/**
	 * 合格率饼图
	 * @param startDate
	 * @param endDate
	 * @param typeScope 公司,或工厂,或产线
	 * @param modelnum  产品型号(modelnum)
	 * @param passType 固定值【COUNT】
	 * @param id        默认为公司id
	 * @return
	 */
	@RequestMapping("/pieChart")
	@ResponseBody
	public Map<KeyType,Object> pieChartOptionData(Timestamp startDate,Timestamp endDate
			,String modelnum,PassType passType,Long id,DateType dateType){
		PieChart pieChart = new PieChart();
		List<String> center = new ArrayList<>();
		center.add("50%");
		center.add("60%");
		pieChart.getSeries().put(SeriesEnum.center, center);
		Map<seriesDataEnum,Object> seriesData_data = new HashMap<>();
		
		//获取合格数,不合格数  
		//统计范围默认为公司
		Map<PassType, Object> resultMap = productChartServ.getProductCountNumByHase(startDate, endDate, TypeScope.company, modelnum, passType, id,dateType);
		
		seriesData_data.put(seriesDataEnum.value, resultMap!=null?resultMap.get(PassType.PASSCOUNT):0);
		seriesData_data.put(seriesDataEnum.name, "合格");
		pieChart.getSeriesData().add(seriesData_data);
		
		seriesData_data = new HashMap<>();
		seriesData_data.put(seriesDataEnum.value, resultMap!=null?resultMap.get(PassType.FAILCOUNT):0);
		seriesData_data.put(seriesDataEnum.name, "不合格");
		pieChart.getSeriesData().add(seriesData_data);
		
		Map<KeyType,Object> map = new HashMap<>();
		map.put(KeyType.Option, pieChart.getOption());
		map.put(KeyType.Count, (resultMap!=null?(long)resultMap.get(PassType.PASSCOUNT):0)+
				(resultMap!=null?(long)resultMap.get(PassType.FAILCOUNT):0));
		map.put(KeyType.PassCount, resultMap!=null?resultMap.get(PassType.PASSCOUNT):0);
		return map;
	}
	
	@RequestMapping(value="/countChart", method={RequestMethod.GET, RequestMethod.POST})
    public String countChart(Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
        Companyinfo companyinfo = cpServ.findById(SecurityUtils.getShiroUser().getCompanyid());
        map.put("mesProduct", mpServ.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid()));
        map.put("companyinfos", cpServ.getTreeFactory());
        map.put("company", cpServ.getTreeFactory().get(0));
        return "count/countChart";
    }
	
	/**
	 * 设备能耗报表导出
	 * @param energyType
	 * @param response
	 * @param startDate
	 * @param endDate
	 * @param driverId
	 * @return
	 */
	@RequestMapping("/exportEnergyExcel")
	@ResponseBody
	public String exportEnergyExcel(String energyType,HttpServletResponse response,
			Timestamp startDate,Timestamp endDate,Long driverId, TypeScope typeScope, Long id){
		JSONObject jsonObject = new JSONObject();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//初始化数据表头数据
    	String[] tableTitles = new String[]{"序号","时间","工厂","产线","设备名称","能耗类型","耗量"};
		List<String> fieldName = Arrays.asList(tableTitles);
    	//获取数据集合
		List<MesEnergy> mesEnergys = null;
//		if(driverId==null){
//			mesEnergys = mesDriverService.findenergy(energyType, startDate, endDate);
//		}else{
//			mesEnergys = mesDriverService.findenergy(energyType, startDate, endDate,driverId);
//		}
		
	      List<MesDriver> driverList = new ArrayList<>();
	        if(TypeScope.productline.equals(typeScope)){
	            //当统计范围是产线时
	            List<MesProductline> lines = new ArrayList<>();
	            lines.add(new MesProductline(id));
	            driverList = productChartServ.getDriverListForProductLineList(lines);
	        }else if(TypeScope.factory.equals(typeScope)){
	            //当统计范围是工厂时
	            //查询出该工厂下所有的产线
	            Companyinfo factorys = cpDao.findOne(id);
	            List<MesProductline> lines = productChartServ.getProductlineOfFactory(factorys);
	            driverList = productChartServ.getDriverListForProductLineList(lines);
	        }else if(TypeScope.company.equals(typeScope)){
	            //当统计范围是公司时
	            //查询公司下一共多少工厂
	            id = SecurityUtils.getShiroUser().getCompanyid();
	            List<Companyinfo> factorys = cpDao.findByParentid(id);
	            List<MesProductline> lines = productChartServ.getProductlineOfFactory(factorys);
	            driverList = productChartServ.getDriverListForProductLineList(lines);
	        }

        Map<Long, MesDriver> driverMap = Maps.newHashMap();
        for(MesDriver driver : driverList) {
            driverMap.put(driver.getId(), driver);
        }

        List<SearchFilter> searchList = Lists.newArrayList();
        if(null != driverId)
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(driverId)));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(startDate.getTime()).replace("000", "")));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(endDate.getTime()).replace("000", "")));
        String searchEnergyType = "";
        if("electric".equals(energyType)) {
            searchEnergyType = "ELECTRIC";
        } else if("gas".equals(energyType)) {
            searchEnergyType = "GAS";
        } else if("water".equals(energyType)) {
            searchEnergyType = "WATER";
        }
        searchList.add(new SearchFilter("type", Operator.EQ, searchEnergyType));
        Specification<MesDataWeg> specification = DynamicSpecifications
                .bySearchFilter(MesDataWeg.class, searchList);
        List<MesDataWeg> mesDataWegList = mesDataWegService.findAll(specification);
        List<MesEnergy> Driverenergy = Lists.newArrayList();
//        MesDriver o = mesDriverService.findById(driverid);
        MesEnergy mesEnergy = null;
        for(MesDataWeg obj : mesDataWegList) {
            mesEnergy = new MesEnergy();
            mesEnergy.setEnergytype(obj.getType());
            mesEnergy.setMesDriver(driverMap.get(obj.getMesDataMultiKey().getDriverId().longValue()));
            mesEnergy.setValue(Double.valueOf(obj.getMetaValue()));
            String updateTimeStr = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());
            updateTimeStr = updateTimeStr + "000";
            updateTimeStr = DateUtils.unixTimestampToDate(Long.valueOf(updateTimeStr));
            mesEnergy.setUpdatetime(Timestamp.valueOf(updateTimeStr));
            Driverenergy.add(mesEnergy);
        }
		
        mesEnergys =Driverenergy;
		
		//准备数据
		List<List<String>> fieldDatas = new ArrayList<>();
		int number = 0;
		for(MesEnergy me : mesEnergys){
			ArrayList<String> fieldData = new ArrayList<>();
			number++;
			fieldData.add(String.valueOf(number));
			fieldData.add(sdf.format(me.getUpdatetime()));
			fieldData.add(me.getMesDriver().getCompanyinfo().getCompanyname());
			fieldData.add(me.getMesDriver().getMesProductline().getLinename());
			fieldData.add(me.getMesDriver().getName());
			fieldData.add(me.getEnergytype());
			fieldData.add(me.getValue()+"");
			fieldDatas.add(fieldData);
		}
		//生成报表对象
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDatas);
		String filename = "设备能耗报表.xlsx";
		try {
			filename = new String(filename.getBytes("gbk"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+filename);
		response.setBufferSize(1024);
		String fileBaseName = filename;
		String newFileName = UUID.randomUUID().toString()
				+ fileBaseName.substring(fileBaseName.lastIndexOf("."));
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE"+File.separator;
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new java.io.File(filePath);
		if(!uploadFile.exists()){
			uploadFile.mkdirs();
		}
		//获取输出流
		OutputStream os;
		try {
			os = new FileOutputStream(filePath + File.separator + newFileName);
			excelFileGenerator.expordExcel(os);//使用输出流，导出 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Companyfile cpfile = new Companyfile();
		cpfile.setfilebasename(fileBaseName);
		cpfile.setfilelength(new File(filePath + File.separator + newFileName).length());
		cpfile.setfilenewname(fileBaseName);
		cpfile.setfilepath(filePath + File.separator + newFileName);
		Companyfile companyfile = cfServ.saveOrUpdate(cpfile);
		
		jsonObject.put("success", companyfile.getId());
		return jsonObject.toString();
	}
	
	
	/**
	 * 进入设备OEE页面
	 * @param map
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/countOee", method={RequestMethod.GET, RequestMethod.POST})
	public String countOee(Map<String, Object> map,HttpServletRequest request) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		List<MesProduct> mesProductList = mps.findByCompanyinfo(companyId);
		if(companyId == null){
			return "error/403";			
		}
        map.put("companyinfos", cpServ.getTreeFactory());
        map.put("mesProductList", mesProductList);
	    return "count/oee";
	}
	
    /**
     * 检索出数据库中所有当前公司的OEE数据
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/listAllOEE")
    @ResponseBody
    public String listAllOEE(HttpServletRequest request, Page page) throws JsonProcessingException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid(); 
    	List<Companyinfo> factorys = cpDao.findByParentid(companyId);
		List<Companyinfo> sonFctys = new ArrayList<>();
		for(Companyinfo f : factorys){
			sonFctys.addAll(productChartServ.getFactoryListByFactoryId(f.getId()));
		}
		//获取用户公司下的所有工厂id集合
    	List<Long> companyIds = new ArrayList<Long>();
    	for(Companyinfo c : sonFctys){
    		companyIds.add(c.getId());
    	}
    	Specification<MesDriverOEE> specification = DynamicSpecifications.bySearchFilter(request, 
    			MesDriverOEE.class,new SearchFilter("companyinfo.id", Operator.IN, companyIds.toArray()));
		List<MesDriverOEE> DriverOEEs = mesDriverService.findOEEPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("DriverOEEs", DriverOEEs);
		map.put("page", page);
    	return mapper.writeValueAsString(map);
    }
    
    /**
     * 运算手动OEE结果，更新柱状图
     * @param runTime  设备运行时间
     * @param haltTime  计划停机时间
     * @param adjustTime  设备调整时间 
     * @param hitchTime  故障停机时间
     * @param circle  理论加工周期
     * @param output  总产量
     * @param waste 废品件
     * @param chooseExact 数值精确度
     * @return
     */
    @RequestMapping("/handOperate")
    @ResponseBody
    public String handOperate(Integer runTime,Integer haltTime,Integer adjustTime,
		Integer hitchTime,Double circle,Integer output,Integer waste,Integer chooseExact){
    	String oeeRate = "";
		String timeRate = "";
		String propertyRate = "";
		String passRate = "";
		if((runTime!=null)&&(haltTime!=null)&&(adjustTime!=null)&&(hitchTime!=null)
				&&(circle!=null)&&(output!=null)&&(waste!=null)){
			MesDriverOEE mesDriverOEE = mesDriverService.calculate(runTime,haltTime,
					adjustTime,hitchTime,circle,output,waste,chooseExact,null);
    		oeeRate = mesDriverOEE.getOeeRate();
    		timeRate = mesDriverOEE.getTimeRate();
    		propertyRate = mesDriverOEE.getPropertyRate();
    		passRate = mesDriverOEE.getPassRate();
		}else{
			return "error";
		}
    	return oeeRate+","+timeRate+","+propertyRate+","+passRate;
    }
   
    /**
     * 保存手动OEE记录
     * @throws ParseException
     */
    @RequestMapping("/saveHandResult")
    @ResponseBody
    public String saveHandResult(Long chooseFactory1,Long chooseProductLine1,Long chooseDriver1,
    		String chooseClasses1,String handtime1,String handtime2,String oeeRate1,
    		String timeRate1,String propertyRate1,String passRate1) throws ParseException{
    	Companyinfo companyinfo = new Companyinfo();
    	companyinfo.setId(chooseFactory1);
    	MesProductline mesProductline = new MesProductline(chooseProductLine1);
    	MesDriver mesDriver = new MesDriver();
    	mesDriver.setId(chooseDriver1);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date hand1 = sdf.parse(handtime1);
    	Date hand2 = sdf.parse(handtime2);
    	//使用构造器创建oee对象
    	MesDriverOEE mesDriverOEE = new MesDriverOEE(companyinfo, mesProductline, mesDriver, new Date(), 
    			chooseClasses1, timeRate1, propertyRate1, passRate1, oeeRate1, hand1, hand2,"手动");
    	mesDriverService.saveHandOEEResult(mesDriverOEE);
    	return "ok";
    }
    
    
    /**
     * 自动oee
     * @param chooseFactory2  工厂
     * @param chooseProductLine2  产线
     * @param chooseDriver2  设备
     * @param chooseClasses2  班次
     * @param chooseModel  产品型号
     * @param autotime1  班次起始时间
     * @param autotime2  班次结束时间
     * @param autotime3  自动计算日期
     * @param chooseCycle  计算周期
     * @param period  理论加工周期
     * @param chooseExact2  计算精确度
     * @return
     * @throws ParseException 
     */
    @RequestMapping("/autoOperate")
    @ResponseBody
    public String autoOperate(Long chooseFactory2,Long chooseProductLine2,Long chooseDriver2,
    		String chooseClasses2,String chooseModel,String autotime1,String autotime2,
    		String autotime3,Double period,Integer chooseExact2) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//计算设备所有运行时间段
    	Map<String, String> map = new HashMap<String, String>();
    	map = mesDriverService.getTotalTime(autotime1,autotime2,autotime3);
    	if(map==null || map.size()<1){
    		return "none";
    	}
    	//计算设备总产量
    	int sumyield = 0; //总产量
    	int sumfailure = 0;  //不合格品数
    	Map<String, Long> resMap = mesDriverService.getYieldAndFailure(chooseFactory2, chooseProductLine2,chooseModel,chooseDriver2,autotime3);
    	if(resMap.size()>0) {
    		if(null != resMap.get("yield")) {
    			sumyield = resMap.get("yield").intValue();
    		}
    		if(null != resMap.get("failure")) {
    			sumfailure = resMap.get("failure").intValue();
    		}
    	}
    	/*for(String key : map.keySet()){
			List<MesProduction> mesProductions = mesProductionService.findByTimeSpan(Timestamp.valueOf(key), 
					Timestamp.valueOf(map.get(key)), chooseDriver2, chooseModel);
			if(mesProductions==null ||mesProductions.size()<1){
				continue;
			}
			for(MesProduction mp : mesProdurctions){
				sumyield = sumyield + mp.getTotalCount().intValue();
				sumfailure = sumfailure + mp.getFailureCount().intValue();
			}
    	}*/
    	
    	System.out.println("总产量："+sumyield+",不合格品数:"+sumfailure);
    	//设备所有运行时间段 之和  总时间   单位：分钟
    	int allTimeSum = 0;
    	long tempTimes = 0;
    	for(String key : map.keySet()){
    		tempTimes = tempTimes + (sdf.parse(map.get(key)).getTime()-sdf.parse(key).getTime());
    	}
    	allTimeSum = (int)(tempTimes/1000/60);
    	System.out.println("设备所有运行时间段 之和 "+allTimeSum);
    	//设备实际运行总时间
    	//获取要查询的hbase的rowkey集合
    	long mills = System.currentTimeMillis();
    	List<DriverRecord> driverRecordList =  mesDriverService.getDriverRecordRowkeys(map,chooseDriver2,chooseModel, chooseFactory2, chooseProductLine2);
    	System.out.println("查询运行时间数据条数："+driverRecordList.size());
    	System.out.println("查询数据库设备运行时间-------"+(System.currentTimeMillis()-mills));
    	int sumTime = mesDriverService.getDriverRunTime(driverRecordList,chooseDriver2);
    	System.out.println("设备实际运行总时间"+sumTime);
    	//设备停机时间
    	int haltTime = allTimeSum-sumTime;
    	//计算oee
    	if((allTimeSum!=0)&&(sumTime!=0)&&(period!=null)&&(chooseExact2!=null)&&(sumyield!=0)){
			MesDriverOEE mesDriverOEE = mesDriverService.calculate(allTimeSum,haltTime,
					null,null,period,sumyield,sumfailure,chooseExact2,sumTime);
    		String oeeRate = mesDriverOEE.getOeeRate();
    		String timeRate = mesDriverOEE.getTimeRate();
    		String propertyRate = mesDriverOEE.getPropertyRate();
    		String passRate = mesDriverOEE.getPassRate();
    		Companyinfo companyinfo = new Companyinfo();
        	companyinfo.setId(chooseFactory2);
        	MesProductline mesProductline = new MesProductline(chooseProductLine2);
        	MesDriver mesDriver = new MesDriver();
        	mesDriver.setId(chooseDriver2);
        	mesDriverOEE.setCompanyinfo(companyinfo);
        	mesDriverOEE.setMesProductline(mesProductline);
        	mesDriverOEE.setMesDriver(mesDriver);
        	mesDriverOEE.setCreateDate(new Date());
        	mesDriverOEE.setClasses(chooseClasses2);
        	mesDriverOEE.setStarttime(sdf.parse(autotime1));
        	mesDriverOEE.setEndtime(sdf.parse(autotime2));
        	mesDriverOEE.setType("自动");
        	mesDriverService.saveHandOEEResult(mesDriverOEE);
        	return oeeRate+","+timeRate+","+propertyRate+","+passRate+","+allTimeSum
        			+","+sumTime+","+haltTime+","+sumyield+","+sumfailure;
		}else{
			return "none";
		}
    }
    
    /**
     * 删除一条或者多条oee数据
     * @param ids
     * @return
     */
    @RequestMapping(value="/deleteDrvierOEE", method = RequestMethod.POST)
    public @ResponseBody String deleteDrvierOEE(Long[] ids){
    	try {
			if(ids!=null && ids.length>0){
				for(int i = 0; i < ids.length; i++){
					mesDriverService.deleteOEEById(ids[i]);
				}
			}
		} catch (Exception e) {
			return AjaxObject.newError("删除失败!").setCallbackType("").toString();
		}
    	return AjaxObject.newOk("删除成功!").setCallbackType("").toString();
    }
    
    /**
     * 检索历史oee记录
     * @param request
     * @param page
     * @param startDate //开始时间
     * @param endDate //结束时间
     * @param driverId //设备id
     * @return
     * @throws ParseException
     * @throws JsonProcessingException
     */
    @RequestMapping("/searchOeeHistory")
    @ResponseBody
    public String searchOeeHistory(HttpServletRequest request, Page page,String type,
    		String startDate,String endDate,Long driverId,String chooseClasses3) throws ParseException, JsonProcessingException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date startDate1 = sdf.parse(startDate);
    	Date endDate1 = sdf.parse(endDate);
    	//开始时间不能大于结束时间
		if(startDate1.getTime() > endDate1.getTime()){
			return "";
		}
		List<SearchFilter> filters = new ArrayList<SearchFilter>();
		SearchFilter classFilter = null;
		if(StringUtils.isNotEmpty(chooseClasses3)){
			classFilter = new SearchFilter("classes", Operator.EQ, chooseClasses3);
			filters.add(classFilter);
		}
		SearchFilter typeFilter = null;
		if(StringUtils.isNotEmpty(type)&&(!"All".equals(type))){
			typeFilter = new SearchFilter("type", Operator.EQ, type);
			filters.add(typeFilter);
		}
		filters.add(new SearchFilter("mesDriver.id", Operator.EQ, driverId));
		filters.add(new SearchFilter("createDate", Operator.LT, endDate1));
		filters.add(new SearchFilter("createDate", Operator.GT, startDate1));
    	Specification<MesDriverOEE> bySearchFilter = DynamicSpecifications.bySearchFilter(request,MesDriverOEE.class,filters);
    	List<MesDriverOEE> DriverOEEs = mesDriverService.findOEEPage(bySearchFilter, page);
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("DriverOEEs", DriverOEEs);
		map.put("page", page);
    	return mapper.writeValueAsString(map);
    }
    
    /**
     * oee历史记录里面的某一天的柱状图趋势，显示某天的每一条记录
     */
    @RequestMapping("/oEEBarDay")
    @ResponseBody
    public String oEEBarDay(String startDate,String endDate,String type,
    		Long driverId,String chooseClasses3) throws ParseException, JsonProcessingException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	List<MesDriverOEE> tempDriverOEEs = mesDriverService.findByTimeAndClasses(sdf.parse(startDate), 
				sdf.parse(endDate), driverId, chooseClasses3);
    	//过滤手动自动记录
    	List<MesDriverOEE> DriverOEEs = this.filterOeetype(tempDriverOEEs,type);
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("DriverOEEs", DriverOEEs);
    	return mapper.writeValueAsString(map);
    }
    
    /**
     * 查询计算某一周或者某个月的每天    平均oee
     */
    @RequestMapping("/oEEBarWeekOrMonth")
    @ResponseBody
    public String oEEBarWeekOrMonth(HttpServletRequest request,String startDate,String endDate,
    		DateType dateType,Long driverId,String type) throws ParseException, JsonProcessingException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//获取时间段的开始和结束时间点，前台传过来的值一个月最多只有29天
    	List<Timestamp> datalist = new ProductAndEnergyAndDriverChartServiceImp().getDateList(dateType,
    			Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
    	Date tempstart = datalist.get(0); //开始时间点
    	Date tempend = datalist.get(datalist.size()-1); //结束时间点
    	
    	List<String> xAxis = new ArrayList<String>();//x轴集合
    	List<Double> aList = new ArrayList<Double>();//早班次每日平均oee集合
    	List<Double> pList = new ArrayList<Double>();//中班次每日平均oee集合
    	List<Double> qList = new ArrayList<Double>();//晚班次每日平均oee集合
    	List<String> allDays = mesDriverService.getAllDays(sdf.format(tempstart), sdf.format(tempend));
    	if(allDays==null || allDays.size()<1){
    		return "";
    	}
    	for(String d : allDays){
    		xAxis.add(d);
    		Date start = sdf.parse(d+" 00:00:00");
    		Date end = sdf.parse(d+" 23:59:59");
    		List<MesDriverOEE> temmpAList = mesDriverService.findByTimeAndClasses(start, end, driverId, "A");
    		List<MesDriverOEE> AList = this.filterOeetype(temmpAList,type);
    		aList.add(mesDriverService.averageOEE(AList));
    		List<MesDriverOEE> temmpPList = mesDriverService.findByTimeAndClasses(start, end, driverId, "P");
    		List<MesDriverOEE> PList = this.filterOeetype(temmpPList,type);
    		pList.add(mesDriverService.averageOEE(PList));
    		List<MesDriverOEE> tempQList = mesDriverService.findByTimeAndClasses(start, end, driverId, "Q");
    		List<MesDriverOEE> QList = this.filterOeetype(tempQList,type);
    		qList.add(mesDriverService.averageOEE(QList));
    	}
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    	MesDriver mesDriver = mesDriverService.findById(driverId);
    	map.put("driverName", mesDriver.getName());
    	map.put("xAxis", xAxis);
    	map.put("aList", aList);
    	map.put("pList", pList);
    	map.put("qList", qList);
    	return mapper.writeValueAsString(map);
    }
    
    /**
     * 生成oee历史记录报表
     * @return
     * @throws ParseException 
     */
    @RequestMapping("/exportHistory")
	@ResponseBody
    public String exportExcel(HttpServletRequest request,HttpServletResponse response,String type,
    		String startDate,String endDate,Long driverId,String chooseClasses3) throws ParseException{
    	JSONObject jsonObject = new JSONObject();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//初始化数据表头数据
    	String[] tableTitles = new String[]{"序号","工厂","产线","设备","运算记录日期","班次",
    			"开始时间","结束时间","时间开动率","性能开动率","合格品率","设备OEE","记录类型"};
		List<String> fieldName = Arrays.asList(tableTitles);
    	//获取数据集合
		List<MesDriverOEE> tempDriverOEEs = null;
		tempDriverOEEs = mesDriverService.findByTimeAndClasses(sdf.parse(startDate), 
				sdf.parse(endDate), driverId, chooseClasses3);
		List<MesDriverOEE> DriverOEEs = this.filterOeetype(tempDriverOEEs, type);
		//准备数据
		List<List<String>> fieldDatas = new ArrayList<>();
		int number = 0;
		for(MesDriverOEE oee : DriverOEEs){
			ArrayList<String> fieldData = new ArrayList<>();
			number++;
			fieldData.add(String.valueOf(number));
			fieldData.add(oee.getCompanyinfo().getCompanyname());
			fieldData.add(oee.getMesProductline().getLinename());
			fieldData.add(oee.getMesDriver().getName());
			fieldData.add(sdf.format(oee.getCreateDate()));
			fieldData.add(oee.getClasses());
			fieldData.add(sdf.format(oee.getStarttime()));
			fieldData.add(sdf.format(oee.getEndtime()));
			fieldData.add(oee.getTimeRate()+"%");
			fieldData.add(oee.getPropertyRate()+"%");
			fieldData.add(oee.getPassRate()+"%");
			fieldData.add(oee.getOeeRate()+"%");
			fieldData.add(oee.getType());
			fieldDatas.add(fieldData);
		}
		//生成报表对象
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDatas);
		String filename = "oee历史记录.xlsx";
		try {
			filename = new String(filename.getBytes("gbk"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+filename);
		response.setBufferSize(1024);
		String fileBaseName = filename;
		String newFileName = UUID.randomUUID().toString()
				+ fileBaseName.substring(fileBaseName.lastIndexOf("."));
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE"+File.separator;
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new java.io.File(filePath);
		if(!uploadFile.exists()){
			uploadFile.mkdirs();
		}
		//获取输出流
		OutputStream os;
		try {
			os = new FileOutputStream(filePath + File.separator + newFileName);
			excelFileGenerator.expordExcel(os);//使用输出流，导出 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Companyfile cpfile = new Companyfile();
		cpfile.setfilebasename(fileBaseName);
		cpfile.setfilelength(new File(filePath + File.separator + newFileName).length());
		cpfile.setfilenewname(fileBaseName);
		cpfile.setfilepath(filePath + File.separator + newFileName);
		Companyfile companyfile = cfServ.saveOrUpdate(cpfile);
		
		jsonObject.put("success", companyfile.getId());
		return jsonObject.toString();
    }
    
    /**
     * oee记录中过滤手动和自动
     * @param tempDriverOEEs oee记录集合
     * @param type 类型 ：手动，自动
     * @return
     */
    private List<MesDriverOEE> filterOeetype(List<MesDriverOEE> tempDriverOEEs,String type){
    	List<MesDriverOEE> DriverOEEs = new ArrayList<MesDriverOEE>();
    	if(type.equals("All")){
    		return tempDriverOEEs;
    	}
    	for(MesDriverOEE mdo : tempDriverOEEs){
    		if(type.equals(mdo.getType())){
    			DriverOEEs.add(mdo);
    		}
    	}
    	return DriverOEEs;
    }

    /**
     * 统计分析：产品分析
     * @param map
     * @return
     */
    @RequestMapping("/productAnalyse")
    public ModelAndView productAnalyse(Timestamp startDate,Timestamp endDate
            ,String modelnum,PassType passType,Long id,DateType dateType, String searchKind){
//        Long companyId = SecurityUtils.getShiroUser().getCompanyid();
//        System.out.println(companyId);
//        Companyinfo companyinfo = cpServ.findById(SecurityUtils.getShiroUser().getCompanyid());
//        map.put("mesProduct", mpServ.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid()));
//        map.put("companyinfos", cpServ.getTreeFactory());
//        map.put("company", cpServ.getTreeFactory().get(0));
//        startDate = Timestamp.valueOf("1529337600000");
//        endDate = Timestamp.valueOf("1529423999000");
        

//        String time = "2018-06-19 00:00:00";
//        startDate = Timestamp.valueOf(time);
//        String time1 = "2018-06-19 23:59:59";
//        endDate = Timestamp.valueOf(time1);
//        modelnum = "9";
//        passType = PassType.COUNT;
//        id = 516L;
        //dateType = DateType.defineDate;
        /*
        Map<BarKeyType, Object> resultMap = mesStatsService.getProductionData(startDate, endDate, TypeScope.company, modelnum, id, dateType);
        LineChartOptionModelForList lineOption = LineChartUtils.setDemo();
        // resultMap.get(BarKeyType.xAxisOfData)
        lineOption.getxAxis().put(XAxisElement.data, resultMap.get(BarKeyType.xAxisOfData));
        lineOption.getSeries().clear();
        lineOption.setSeries((List)resultMap.get(BarKeyType.seriesItemList)); */

        ModelAndView modelAndView = null;
        if ("1".equals(searchKind)) {
            modelAndView = new ModelAndView("count/productAnalyseForLine");
        } else {
            modelAndView = new ModelAndView("count/productAnalyseForBar");
        }
        Long compayId = SecurityUtils.getShiroUser().getCompanyid();
        MesProduct mesProduct = mpServ.findByCompanyIdAndModelnum(compayId, modelnum);
        if(null != mesProduct)
        modelAndView.addObject("productNm", mesProduct.getName());

        Map<BarKeyType, Object> resultMap = mesStatsService.getProductionData(startDate, endDate, TypeScope.company, modelnum, id, dateType, "");
        if(null != resultMap && resultMap.size() > 0) {
            modelAndView.addObject("dataflg", "0");
            modelAndView.addObject("seriesData", resultMap.get(BarKeyType.seriesItemList));
            modelAndView.addObject("seriesPercentListData", resultMap.get(BarKeyType.seriesPercentList));
            modelAndView.addObject("xAxisData", JSONUtils.beanToJson(resultMap.get(BarKeyType.xAxisOfData)));

            if(null != resultMap.get(BarKeyType.ngPercent)) {
                modelAndView.addObject("ngPercent", resultMap.get(BarKeyType.ngPercent));
            } else {
                modelAndView.addObject("ngPercent", "0");
            }

            if(null != resultMap.get(BarKeyType.okPercent)) {
                modelAndView.addObject("okPercent", resultMap.get(BarKeyType.okPercent));
            } else {
                modelAndView.addObject("okPercent", "0");
            }

        } else {
            modelAndView.addObject("dataflg", "1");
        }

        modelAndView.addObject("searchKind", searchKind);

        return modelAndView;
    }
    
    @RequestMapping("/productAnalyseData")
    public  @ResponseBody String productAnalyseData(PropertyTrendSearch propertyTrendSearch) throws Exception{
        List<String> lineOptions = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper.writeValueAsString(lineOptions);
    }
    

    /**
     * 统计分析：产品分析
     * @param startDate 起始时间
     * @param endDate   终止时间
     * @param energyType    统计类型
     * @param typeScope
     *     company ：工厂/产线/设备没有选择， 直接指向全公司全部数据
     *     factory ：工厂只选择了， 直接指向改工厂下的所有产线的全部数据
     *     productline ：产线只选择了， 直接指向改产线下的所有设备的全部数据
     * @param searchKind
     * @param dateType
     * @param driverid
     * @param id
     * @return
     */
    @RequestMapping("/productAnalyseForDriver")
    @ResponseBody
    public String productAnalyseForDriver(
            Timestamp startDate,
            Timestamp endDate,
            String energyType,
            TypeScope typeScope,
            int searchKind,
            DateType dateType,
            Long driverid,
            Long id,
            Long chooseFactoryId,
            Long chooseProductLineId,
            Timestamp anlysisDate // 设备分析的运行时间
    ) {
        Map<String, Object> resultMap = mesStatsService.getProductionDataForDriver(energyType, typeScope, id, startDate,
                endDate, driverid, searchKind, dateType, chooseFactoryId, chooseProductLineId, anlysisDate);

        Map<String, Object> serviceData = (Map<String, Object>)resultMap.get("serviceData");
        if(null != serviceData) {
            if(null != serviceData.get("sumVaue")) {
                Float sumValue = (float) serviceData.get("sumVaue");
                // dataflg 0 有数据 1没数据
                serviceData.put("dataflg", "0");
                if (0f == sumValue)
                    serviceData.put("dataflg", "1");
            }
            resultMap.put("serviceData", serviceData);
        }

        resultMap.put("driverNm", resultMap.get("driverNm"));
        resultMap.put("driverSn", resultMap.get("driverSn"));
        // modelAndView.addObject("productNm", resultMap.get("productNm"));

        resultMap.put("searchKind", searchKind);
        // modelAndView.addObject("xAxisData",
        // JSONUtils.beanToJson(resultMap.get("xAxisData")));
        // modelAndView.addObject("seriesData",
        // JSONUtils.beanToJson(resultMap.get("serviceData")));
        return JSONUtils.beanToJson(resultMap);
    }
    
    /**
     * 统计分析：产品分析
     * @param map
     * @return
     */
    @RequestMapping("/productAnalyseNew")
    @ResponseBody
    @SuppressWarnings({ "unchecked" })
    public String productAnalyseNew(Timestamp startDate,Timestamp endDate
            , String modelnum, PassType passType, Long id, DateType dateType,
            String productBatchid) {
        Map<String, Object> rsMap = Maps.newHashMap();
        Long compayId = SecurityUtils.getShiroUser().getCompanyid();
        MesProduct mesProduct = mpServ.findByCompanyIdAndModelnum(compayId, modelnum);
        if (null != mesProduct)
            rsMap.put("productNm", mesProduct.getName());

        Map<BarKeyType, Object> resultMap = mesStatsService.getProductionData(startDate, endDate, TypeScope.company,
                modelnum, id, dateType, productBatchid);
        if (null != resultMap && resultMap.size() > 0 && (null != resultMap.get(BarKeyType.totalDisplay))) {
            rsMap.put("dataflg", "0");
            rsMap.put("seriesData", resultMap.get(BarKeyType.seriesItemList));

            String out[] = mesStatsService.getPercent((List<String>)resultMap.get(BarKeyType.seriesItemList),
                    (List<String>)resultMap.get(BarKeyType.xAxisOfData));
            rsMap.put("seriesDataComMax", JSONUtils.beanToJson(out));

            rsMap.put("seriesDataOkPercent", JSONUtils.beanToJson(resultMap.get(BarKeyType.okPercentByEveryTime)));

            rsMap.put("seriesPercentListData", resultMap.get(BarKeyType.seriesPercentList));
            rsMap.put("xAxisData", JSONUtils.beanToJson(resultMap.get(BarKeyType.xAxisOfData)));

            rsMap.put("okDisplay", resultMap.get(BarKeyType.okDisplay));
            rsMap.put("totalDisplay", resultMap.get(BarKeyType.totalDisplay));

            rsMap.put("okPercentMaxList", resultMap.get(BarKeyType.okPercentMaxList));
            rsMap.put("percentMaxList", resultMap.get(BarKeyType.percentMaxList));

            if (null != resultMap.get(BarKeyType.ngPercent)) {
                rsMap.put("ngPercent", resultMap.get(BarKeyType.ngPercent));
            } else {
                rsMap.put("ngPercent", "0");
            }

            if (null != resultMap.get(BarKeyType.okPercent)) {
                rsMap.put("okPercent", resultMap.get(BarKeyType.okPercent));
            } else {
                rsMap.put("okPercent", "0");
            }

            rsMap.put("batchIdXvalues", resultMap.get(BarKeyType.batchIdXvalues));
            rsMap.put("batchIdYvalues", resultMap.get(BarKeyType.batchIdYvalues));
            rsMap.put("seriesDataComMaxForBatch", JSONUtils.beanToJson(resultMap.get(BarKeyType.seriesDataComMaxForBatch)));
            rsMap.put("batchXvalueLis", resultMap.get(BarKeyType.batchXvalueLis));

        } else {
            rsMap.put("dataflg", "1");
        }
        return JSONUtils.beanToJson(rsMap);
    }

}