package com.its.statistics.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.its.common.controller.BaseController;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesDriverDao;
import com.its.frd.dao.MesProcedurePropertyDao;
import com.its.frd.dao.MesProductDao;
import com.its.frd.dao.MesProductProcedureDao;
import com.its.frd.entity.*;
import com.its.frd.schedule.ProductionSchedulerPageSearch;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.MesDataProductProcedureService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.util.*;
import com.its.frd.util.echarts.LineOption;
import com.its.frd.websocket.WebSocketBrokerConfig;
import com.its.monitor.service.MonitorService;
import com.its.statistics.service.StatisticsService;
import com.its.statistics.vo.*;
import com.its.statistics.vo.ProductionRecord;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 统计模块
 * @author conno
 *
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

	@Autowired
	private MonitorService monitorService;


	@Resource
	private MesProductDao mesProductDao;

	@Resource
	private MesProductService mesProductService;

	@Resource
	private StatisticsService statisticsService;
	
	@Autowired
	private ProductionSchedulerPageSearch productionSchedulerPageSearch;
	
	@Resource
	private MesProductProcedureDao mesProductProcedureDao;
	@Resource
	private MesProcedurePropertyDao mesProcedurePropertyDao;

	@Resource
	private CompanyfileService cfServ;

    @Resource
    private UsercompanysService ucServ;

    @Autowired
    private MesDataProductProcedureService mesDataProductProcedureService;

	@Resource
	private MesDriverDao mesDriverDao;

	@Autowired
	private MesProcedurePropertyDao mesProdcedurePropertyDao;

	@PersistenceContext
	private EntityManager entityManager;

    /**
     * JMS-MQ服务器地址
     */
    @Value("${monitorStomp}")
    private String monitorStomp;

	@RequestMapping("/msa")
	public ModelAndView msa(HttpServletRequest request,CgAnalyzeSearch analyzeData) throws JsonProcessingException {
		ModelAndView modelAndView = new ModelAndView("count/msa");
		if(Boolean.parseBoolean(request.getParameter("refresh"))){
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			modelAndView.addObject("analyzeData", mapper.writeValueAsString(analyzeData));
		}
		modelAndView.addObject("checkDrivers", statisticsService.generateDriverSelect());
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		
		return modelAndView;
	}
	@RequestMapping("/spc")
	public ModelAndView spc() {
		ModelAndView modelAndView = new ModelAndView("count/spc");
		Properties properties = new Properties();
        InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/jmsConfig.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
		monitorStomp = properties.getProperty("monitorStomp");
        modelAndView.addObject("monitorStomp", monitorStomp);

//        List<Usercompanys> usercompanys = ucServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
//        for(Usercompanys ucom : usercompanys) {
//            if("".equals(ucom.getId().toString())) {
//                
//                break;
//            }
//        }
        modelAndView.addObject("companyId", String.valueOf(SecurityUtils.getShiroUser().getCompanyid()));
		modelAndView.addObject("checkDrivers", statisticsService.generateDriverSelect());
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		return modelAndView;
	}

	@RequestMapping("/monitor_spc")
	public ModelAndView MonitorSpc() {
		ModelAndView modelAndView = new ModelAndView("count/monitor_spc");
		Properties properties = new Properties();
		InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/jmsConfig.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		monitorStomp = properties.getProperty("monitorStomp");
		modelAndView.addObject("monitorStomp", monitorStomp);

//        List<Usercompanys> usercompanys = ucServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
//        for(Usercompanys ucom : usercompanys) {
//            if("".equals(ucom.getId().toString())) {
//
//                break;
//            }
//        }
		modelAndView.addObject("companyId", String.valueOf(SecurityUtils.getShiroUser().getCompanyid()));
		modelAndView.addObject("checkDrivers", statisticsService.generateDriverSelect());
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		return modelAndView;
	}

	@RequestMapping("/spcAnalyse")
	public ModelAndView spcAnalyse() {
		ModelAndView modelAndView = new ModelAndView("count/spcAnalyse");
		Properties properties = new Properties();
		InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/jmsConfig.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		monitorStomp = properties.getProperty("monitorStomp");
		modelAndView.addObject("monitorStomp", monitorStomp);
		modelAndView.addObject("companyId", String.valueOf(SecurityUtils.getShiroUser().getCompanyid()));
		modelAndView.addObject("checkDrivers", statisticsService.generateDriverSelect());
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		return modelAndView;
	}

	@RequestMapping("/monitor_spc_analysis")
	public ModelAndView MonitorSpcAnalysis() {
		ModelAndView modelAndView = new ModelAndView("count/monitor_spc_analysis");
		Properties properties = new Properties();
		InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/jmsConfig.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		monitorStomp = properties.getProperty("monitorStomp");
		modelAndView.addObject("monitorStomp", monitorStomp);

//        List<Usercompanys> usercompanys = ucServ.findByUserid(SecurityUtils.getShiroUser().getUser().getId());
//        for(Usercompanys ucom : usercompanys) {
//            if("".equals(ucom.getId().toString())) {
//
//                break;
//            }
//        }
		modelAndView.addObject("companyId", String.valueOf(SecurityUtils.getShiroUser().getCompanyid()));
		modelAndView.addObject("checkDrivers", statisticsService.generateDriverSelect());
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		return modelAndView;
	}

	@RequestMapping("/analyseCgDataPage")
	public ModelAndView analyseCgDataPage() {
		ModelAndView modelAndView = new ModelAndView("count/cgResult");
		return modelAndView;
	}
	@RequestMapping("/analyseSpcDataPage")
    public ModelAndView analyseSpcCgDataPage() {
        ModelAndView modelAndView = new ModelAndView("count/spcResult");
        Properties properties = new Properties();
        InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/jmsConfig.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        monitorStomp = properties.getProperty("monitorStomp");
        modelAndView.addObject("companyId", String.valueOf(SecurityUtils.getShiroUser().getCompanyid()));
        modelAndView.addObject("monitorStomp", monitorStomp);
        return modelAndView;
    }
	@RequestMapping("/analyseGrrDataPage")
	public ModelAndView analyseSpcGrrDataPage() {
		ModelAndView modelAndView = new ModelAndView("count/grrResult");
		return modelAndView;
	}

	@RequestMapping("/generateProductProcedureSelect/{productId}")
	public @ResponseBody String generateProductProcedureSelect(@PathVariable long productId){
		return statisticsService.generateProductProcedureSelect(productId);
	}
	
   @RequestMapping("/generateProBatchids/{productId}")
    public @ResponseBody String generateProBatchids(@PathVariable long productId){
        return statisticsService.generateProbatchids(productId);
    }
   
   @RequestMapping("/generateProBatchids2/{productId}")
   public @ResponseBody String generateProBatchids2(@PathVariable long productId){
       return statisticsService.generateProbatchids2(productId);
   }

	@RequestMapping("/generateProcedurePropertySelect/{procedureId}")
	public @ResponseBody String generateProcedurePropertySelect(@PathVariable long procedureId){
		return statisticsService.generateProcedurePropertySelect(procedureId);
	}

	@RequestMapping("/analyseDataSearch")
	public @ResponseBody String analyzeDataSearch(AnalyzeSearch analyzeData){
		return statisticsService.analyzeDataSearch(analyzeData);
	}

    @SuppressWarnings("static-access")
    @RequiresPermissions("spc:delete")
    @RequestMapping("/delHBaseDate")
    public @ResponseBody String delHBaseDate(AnalyzeSearch analyzeData) {
        if(StringUtils.isNotEmpty(analyzeData.getRowKeyDelFlg())
           && "0".equals(analyzeData.getRowKeyDelFlg())
           && StringUtils.isNotEmpty(analyzeData.getRowKeyList())) {
            String[] rowArray = analyzeData.getRowKeyList().split(",");
            for(String rowkey : rowArray) {
                if(!",".equals(rowkey)) {
                    System.out.println("删除rowkey：" + rowkey);
                    Map<String, String> rsMap = MesDataRowkeyUtil.getValMapByProcedureDataRowKey(rowkey);
                    MesDataMultiKey mesDataMultiKey = new MesDataMultiKey();
                    mesDataMultiKey.setFactoryId(Integer.valueOf(rsMap.get("factoryId")));
                    mesDataMultiKey.setProductLineId(Integer.valueOf(rsMap.get("productLineId")));
                    mesDataMultiKey.setDriverId(Integer.valueOf(rsMap.get("driverId")));
                    mesDataMultiKey.setPointId(Integer.valueOf(rsMap.get("pointId")));
                    mesDataMultiKey.setInsertTimestamp(BigInteger.valueOf(Long.valueOf(rsMap.get("insertTimestamp"))));
                    mesDataProductProcedureService.delByRowKey(mesDataMultiKey);
//                        new HbaseUtil().deleteRowByRowkey("procedure", rowkey);

                }
            }
        }
        return statisticsService.analyzeDataSearch(analyzeData);
    }

	@RequestMapping("/generateProcedurePropertySpcAnalyseSelect/{procedureId}")
	public @ResponseBody String generateProcedurePropertySpcAnalyseSelect(@PathVariable long procedureId){
		return statisticsService.generateProcedurePropertySpcAnalyseSelect(procedureId);
	}

	@RequestMapping("/spcAnalyseDataSearch")
	public @ResponseBody String spcAnalyzeDataSearch(HttpServletRequest request, HBasePageModel hBasePageModel) throws JsonProcessingException, ParseException {
		ObjectMapper objectMapper = new ObjectMapper();
		MesDriver mesDriver = mesDriverDao.findOne(Long.valueOf(request.getParameter("mesDriverId")));
		MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(Long.valueOf(request.getParameter("procedurePropertyId")));
		MesProductProcedure productProcedure = mesProcedureProperty.getMesProductProcedure();
		MesProduct mesProduct = productProcedure.getMesProduct();
		List<MonitorSpc> list = new ArrayList<MonitorSpc>();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<SearchFilter> searchList = Lists.newArrayList();
		String mesDriverId = request.getParameter("mesDriverId");
		String productId = request.getParameter("productId");
		String productProcedureId = request.getParameter("productProcedureId");
		String driverPropertyId = request.getParameter("driverPropertyId");
		String subrange = request.getParameter("subRange");
		String subnum = request.getParameter("subNum");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		SpcDataRowkeyUtil.setEntityManager(entityManager);

		searchList.add(new SearchFilter("mesDriverId", SearchFilter.Operator.EQ, request.getParameter("mesDriverId")));
		searchList.add(new SearchFilter("productId", SearchFilter.Operator.EQ,request.getParameter("productId")));
		searchList.add(new SearchFilter("productProcedureId", SearchFilter.Operator.EQ,request.getParameter("productProcedureId")));
		searchList.add(new SearchFilter("driverPropertyId", SearchFilter.Operator.EQ,request.getParameter("procedurePropertyId")));
		searchList.add(new SearchFilter("subrange", SearchFilter.Operator.EQ,request.getParameter("subRange")));
		searchList.add(new SearchFilter("subnum", SearchFilter.Operator.EQ,request.getParameter("subNum")));
		if (request.getParameter("begin") != null && !request.getParameter("begin").equals("")){
			searchList.add(new SearchFilter("createtime", SearchFilter.Operator.GTE, request.getParameter("begin")));
		}
		if (request.getParameter("end") != null && !request.getParameter("end").equals("")){
			searchList.add(new SearchFilter("createtime", SearchFilter.Operator.LTE, request.getParameter("end")));
		}
		Specification<MonitorSpc> specification = DynamicSpecifications.bySearchFilter(request, MonitorSpc.class,searchList);


		list = SpcDataRowkeyUtil.getMulKeyOfPage(
				mesDriverId,
				productId,
				productProcedureId,
				driverPropertyId,
				subrange,
				subnum,
				begin,
				end, "1", hBasePageModel);

		List<String> timeList = SpcDataRowkeyUtil.getCountByPageSelect(
				mesDriverId,
				productId,
				productProcedureId,
				driverPropertyId,
				subrange,
				subnum,
				begin,
				end
		);
		hBasePageModel.setTotalCount(timeList !=null ?timeList.size() : 0);
//		list = statisticsService.findPage(specification, page);
		List<SpcAnalyzeSearch> spcAnalyzeSearchList = new ArrayList<>();
		for (MonitorSpc m : list){
			SpcAnalyzeSearch spcAnalyzeSearch = new SpcAnalyzeSearch();
			spcAnalyzeSearch.setCp(m.getCp());
			spcAnalyzeSearch.setPp(m.getPp());
			spcAnalyzeSearch.setPpk(m.getPpk());
			spcAnalyzeSearch.setCpk(m.getCpk());
			spcAnalyzeSearch.setCreatetime(m.getCreatetime());
			spcAnalyzeSearch.setMesDriverName(mesDriver.getName());
			spcAnalyzeSearch.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
			spcAnalyzeSearch.setProductName(mesProduct.getName());
			spcAnalyzeSearch.setProductProcedureName(productProcedure.getProcedurename());
			spcAnalyzeSearchList.add(spcAnalyzeSearch);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("page", hBasePageModel);
		map.put("data", spcAnalyzeSearchList);
		return objectMapper.writeValueAsString(map);
	}

	@RequestMapping("/analyseCgData")
	public @ResponseBody String analyseCgData(CgAnalyzeSearch cgAnalyzeData) {
		String result = statisticsService.analyseCgData(cgAnalyzeData);
		return result;
	}
	@RequestMapping("/analyseGrrData")
	public @ResponseBody String analyseGrrData(HttpServletRequest request,GrrAnalyzeSearch grrAnalyzeData) {
		String datas = request.getParameter("datas");
		@SuppressWarnings("unchecked")
		List<CgAnalyzeData> analyzeDatas = (List<CgAnalyzeData>) JSONArray.toCollection(JSONArray.fromObject(datas),CgAnalyzeData.class);
		grrAnalyzeData.setDataList(analyzeDatas);
		String result = statisticsService.analyseGrrData(grrAnalyzeData);
		return result;
	}
	@RequestMapping("/analyseSPCData")
	public @ResponseBody String analyseSPCData(SpcAnalyzeSearch spcAnalyzeData) {
		String result = statisticsService.analyseSpcData(spcAnalyzeData);
		return result;
	}
	@RequestMapping("/getProcedureProperty/{procedurePropertyId}")
	public @ResponseBody String getProcedureProperty(@PathVariable Long procedurePropertyId) throws JsonProcessingException {
		MesProcedureProperty mesProcedureProperty = mesProcedurePropertyDao.findOne(procedurePropertyId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesProcedureProperty);
	}
	@RequestMapping("/getProduct/{productId}")
	public @ResponseBody String getProduct(@PathVariable Long productId) throws JsonProcessingException {
		MesProduct mesProduct = mesProductDao.findOne(productId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesProduct);
	}

	@RequestMapping("/productionRecordPage")
	public ModelAndView productionRecordPage() {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null){
			return new ModelAndView("error/403");
		}
		ModelAndView modelAndView = new ModelAndView("productRecord/WorkPieceNumberSearch");
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		return modelAndView.addObject("productLineSelect", statisticsService.generateProductLineSelect(companyId));
	}

	@RequestMapping("/productionRecordPage2")
	public ModelAndView productionRecordPage2() {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null){
			return new ModelAndView("error/403");
		}
		ModelAndView modelAndView = new ModelAndView("productRecord/WorkProcedureNumberSearch");
		modelAndView.addObject("products", statisticsService.generateProductSelect());
		return modelAndView.addObject("productLineSelect", statisticsService.generateProductLineSelect(companyId));
	}
	/**
	 * 生产记录的列表结果集
	 * @param page
	 * @return
	 */
	@RequestMapping("/productionData")
	public @ResponseBody String productionData(HBasePageModel page) {
		if(page.getAnalyzeSearch().getSearchType() == 0){
			// return statisticsService.productionDataSearch(page);
			return statisticsService.productionDataSearchByNewHbase(page);
		}else{
			return statisticsService.productionDetailDataSearch(page);
		}

	}
	/**
	 * 生产记录的列表结果集的工序属性对应关系取得
	 * @param request
	 * @return
	 */
	@RequestMapping("/productionPropertyData")
	public @ResponseBody Map<String, Map<String, String>> productionPropertyData(HttpServletRequest request) {
		String _rowKeys = request.getParameter("rowKeys");
		String _propertyIds = request.getParameter("propertyIds");
		if(StringUtils.isEmpty(_rowKeys) || StringUtils.isEmpty(_propertyIds))
		    return null;
		Map<String, Map<String, String>> propertyValues = statisticsService.getPropertyValues(_rowKeys,_propertyIds);
		return propertyValues;

	}

	/**
	 * 总数， 合格数， 不合格数， 合格率计算
	 * @param page
	 * @return
	 */
	@RequestMapping("/pageCaculate")
	public @ResponseBody String pageCaculate(HBasePageModel page) {
		String result = statisticsService.pageCaculate(page);
		return result;
	}
	@RequestMapping("/toPropertyDetail")
	public ModelAndView toPropertyDetail(String sn, String rowKey,String procedureId){
		ModelAndView modelAndView = new ModelAndView("productRecord/WorkPieceProperty");
		modelAndView.addObject("sn", sn).addObject("rowKey", rowKey).addObject("procedureId", procedureId);
		return modelAndView;
	}
	@RequestMapping("/toPropertyHistoryTrend")
    public ModelAndView toPropertyHistoryTrend(HttpServletRequest request) {
        String page = request.getParameter("page");
        ModelAndView modelAndView = new ModelAndView("productRecord/historyTrend");
        modelAndView.addObject("products", statisticsService.generateProductSelect());
        modelAndView.addObject("page", page);

        modelAndView.addObject("productLineId", request.getParameter("productLineId"));
        modelAndView.addObject("productId", request.getParameter("productId"));
        modelAndView.addObject("productProcedureId", request.getParameter("productProcedureId"));
        return modelAndView;
    }
	@RequestMapping("/propertyHistoryTrend")
	public  @ResponseBody String propertyHistoryTrend(PropertyTrendSearch propertyTrendSearch) throws Exception{
		List<List<MesProcedureProperty>> procedureList = statisticsService.propertyHistoryTrend(propertyTrendSearch);
		Map<Long,MesProcedureProperty> map = new HashMap<>();
		List<MesProcedureProperty> mesProcedureProperties = mesProcedurePropertyDao.findAll(new Specification<MesProcedureProperty>() {
			@Override
			public Predicate toPredicate(Root<MesProcedureProperty> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
				return criteriaQuery.where(root.get("id").in(propertyTrendSearch.getProcedurePropertyIds())).getRestriction();
			}
		});
		for(MesProcedureProperty mesProcedureProperty : mesProcedureProperties){
			map.put(mesProcedureProperty.getId(), mesProcedureProperty);
		}
		Map<Long,Map<String,Map<String,Double>>> tempMap = new HashMap<>();
		for(List<MesProcedureProperty> list : procedureList){
			for(MesProcedureProperty mesProcedureProperty : list){
				Map<String,Double> valueMap = new ConcurrentHashMap<>();
				valueMap.put(mesProcedureProperty.getPropertyname(), Double.parseDouble(mesProcedureProperty.getCheckValue()));
				if(!tempMap.containsKey(mesProcedureProperty.getId())){
					Map<String,Map<String,Double>> _map = new ConcurrentHashMap<>();
					tempMap.put(mesProcedureProperty.getId(), _map);
				}
				tempMap.get(mesProcedureProperty.getId()).put(mesProcedureProperty.getCheckTime(), valueMap);
			}
		}
		List<String> lineOptions = new ArrayList<>();
		for(long propertyId : tempMap.keySet()){
			LineOption lineOption = new LineOption().title(map.get(propertyId).getPropertyname()+"参数属性趋势");
			lineOptions.add(lineOption.data(tempMap.get(propertyId)).toString());
		}

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


		return objectMapper.writeValueAsString(lineOptions);
	}
	// 此方法无调用
    // 2018/05/16 单体soft重建
	/*
	@RequestMapping("/getProductionRecordInfo/{id}")
	public @ResponseBody String getProductionRecordInfo(@PathVariable String id,HttpServletRequest request) throws JsonProcessingException{
		JSONObject object = new JSONObject();
		object.put("success", statisticsService. getProductionRecordInfo(id));
		return object.toString();
	} */
	@RequestMapping("/getPropertyRecordList")
	public @ResponseBody String getPropertyRecordList(HBasePageModel page,HttpServletRequest request) throws JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,Object> map = new HashMap<>();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesProcedureProperty> propertyRecordList = statisticsService.getPropertyRecordList(page);
		page.setTotalCount(propertyRecordList.size());
		map.put("page", page);
		map.put("mesProcedureProperty", propertyRecordList);
		return objectMapper.writeValueAsString(map);
	}
	@RequestMapping("/getEntityOption")
	public @ResponseBody String getEntityOption(HttpServletRequest request) throws JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		String productProcedureId = request.getParameter("productProcedureId");
		MesProductProcedure mesProductProcedure = mesProductProcedureDao.findOne(Long.parseLong(null != productProcedureId ? productProcedureId : "0"));
		List<MesProcedureProperty> list = mesProductProcedure.getMesProcedureProperties();
		return objectMapper.writeValueAsString(list);
	}

	/**
	 * 生产记录excel记录导出
	 * @param request
	 * @param response
	 * @param tableTitles
	 * @param page
	 * @return
	 */
	@RequestMapping("/productRecordExport")
	@ResponseBody
	public String getUserList(HttpServletRequest request,HttpServletResponse response,String tableTitles,HBasePageModel page){
		JSONObject jsonObject = new JSONObject();
		//1：初始化数据
		//excel的标题数据（只有一条）
		String[] tableTitle = tableTitles.split(",");
		Map<String,String> titleMap = new HashMap<>();
		List<String> fieldName = new ArrayList<>();
		for(String title : tableTitle){
			titleMap.put(title.split(":")[1], title.split(":")[0]);
			fieldName.add(title.split(":")[1]);
		}
		//List<String> fieldName = Arrays.asList(tableTitle);
		
		page.setPagable(false);
		List<ProductionRecord> list = null;
		long productProcedureId = page.getAnalyzeSearch().getProductProcedureId();
		MesProductProcedure mesProductProcedure = mesProductProcedureDao.findOne(productProcedureId);
		List<MesProcedureProperty> propertyList = null;
		if(null != mesProductProcedure){
			propertyList = mesProductProcedure.getMesProcedureProperties();
		}
		if(page.getAnalyzeSearch().getSearchType() == 0){
//			list = productionSchedulerPageSearch.getProductionRecordPagable(page);
			list = productionSchedulerPageSearch.getProductionRecordPagableByMql(page);
//			int amount = productionSchedulerPageSearch.getProductionRecordAmount(page);
			//int amount = list.size();
		}else{
				jsonObject.put("success", 0);
				return jsonObject.toString();
		}
		List<List<String>> fieldDatas = new ArrayList<>();
		
		String _rowKeys = ""; 
		String _propertyIds = "";
		if(null != propertyList){
			for(MesProcedureProperty mesProcedureProperty : propertyList){
				if(_propertyIds.equals("")){
					_propertyIds = String.valueOf(mesProcedureProperty.getId());
				}else{
					_propertyIds += ","+String.valueOf(mesProcedureProperty.getId());
				}
			}
		}
		for(ProductionRecord productionRecord : list){
			if(_rowKeys.equals("")){
				_rowKeys = productionRecord.getRowkey();
			}else{
				_rowKeys += ","+productionRecord.getRowkey();
			}
		}
		Map<String, Map<String, String>> propertyValues = statisticsService.getPropertyValues(_rowKeys,_propertyIds);

		int number = 0;
		for(ProductionRecord productionRecord : list){
			ArrayList<String> fieldData = new ArrayList<>();
			Class<? extends ProductionRecord> clz = productionRecord.getClass();
			number++;
			for(String key : fieldName){
				String field = titleMap.get(key);
					Method m;
					try {
						m = clz.getMethod("get"+field.substring(0, 1).toUpperCase()+field.substring(1));
						fieldData.add(m.invoke(productionRecord).toString());
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						Map<String,String> _map = propertyValues.get(productionRecord.getRowkey());
						String value = null != _map ? _map.get(field) : "";
						if("number".equals(field.toLowerCase()))
							fieldData.add(String.valueOf(number));
						else
							fieldData.add(value);
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
			/*fieldData.add(productionRecord.getProductSn());
			fieldData.add(productionRecord.getProductlineName());
			fieldData.add(productionRecord.getMesDriverName());
			fieldData.add(productionRecord.getGateway());
			fieldData.add(productionRecord.getProductName());
			// fieldData.add(productionRecord.getDatetime());
			fieldData.add(productionRecord.getStatus());*/
			
			fieldDatas.add(fieldData);
		}
		//2：调用封装的POI报表的导出类ExcelFileGenerator完成excel报表的导出
		ExcelFileGenerator excelFileGenerator = new ExcelFileGenerator(fieldName, fieldDatas);

		/**导出报表的文件名*/
		String filename = "生产记录报表.xlsx";
		//处理乱码
		try {
			filename = new String(filename.getBytes("gbk"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/**response中进行设置，总结下载，导出，需要io流和头*/
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
		//File uploadFile = new java.io.File(filePath + File.separator + newFileName);
		if(!uploadFile.exists()){
			uploadFile.mkdirs();
		}
		//获取输出流
		OutputStream os;
		try {
			os = new FileOutputStream(filePath + File.separator + newFileName);
			excelFileGenerator.expordExcel(os);//使用输出流，导出 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	@RequestMapping("/downloadExport/{id}")
	public void downloadExport(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Companyfile companyfile = cfServ.findById(id);
		if(null == companyfile || (null != companyfile && !new File(companyfile.getfilepath()).exists())){
			String errorMessage = "Sorry. The file you are downloading does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		File file = new File(companyfile.getfilepath());

		String mimeType= URLConnection.guessContentTypeFromName(file.getName());
		if(mimeType==null){
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);

		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));

		response.setContentLength((int)file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		//Copy bytes from source to destination(outputstream in this example), closes both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
}
