package com.its.monitor.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.google.common.collect.Lists;
import com.its.common.controller.BaseController;
import com.its.common.dao.UserDAO;
import com.its.common.entity.main.User;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.*;
import com.its.frd.entity.*;
import com.its.frd.params.CompanyfileType;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.*;
import com.its.frd.util.DateUtils;
import com.its.frd.util.echarts.EchartsOption;
import com.its.frd.util.echarts.LineOption;
import com.its.frd.websocket.WebSocketBrokerConfig;
import com.its.monitor.service.MonitorService;
import com.its.monitor.vo.ChartsBaseMonitor;
import com.its.monitor.vo.MesPointsTemplate;
import com.its.statistics.service.StatisticsService;
import com.its.statistics.vo.AnalyzeSearch;
import com.its.statistics.vo.CgAnalyzeData;
import com.its.statistics.vo.PropertyTrendSearch;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Controller
@RequestMapping("/procedureMonitor")
public class ProcedureMonitorController extends BaseController {
	private static final String INDEX = "monitor/index";
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ProcedureMonitorController.class);
	@Resource(name="monitorService")
	private MonitorService monitorService;

	@Autowired
	private MonitorPainterService monitorPainterService;

	@Resource
	private MesDriverService driverServ;

	@Resource(name="redisService")
	private RedisService redisService;

	@Resource
	private MesProductlineService mesProductlineService;

	@Resource
	private MesPointGatewayService mesPointGatewayService;

	@Resource
	private CompanyfileService companyfileService;

	@Resource
	private MesProductDao mesProductDao;

	@Resource
	private MesDriverTypeService mdtServ;

	@Resource
	private UserDAO userDao;

	@Resource
	private MesDriverStatsDao driverStatsDao;

	@Autowired
	private MonitorPainterUserService monitorPainterUserService;
	@Resource
	private UsercompanysDao userCompanyDao;
	
	@Resource
	private MesPointTypeService mptServ;

	@Resource
	private MonitorPainterUserDao monitorPainterUserDao;

	@Resource
	private CompanyinfoService cpService;
	@Resource
	private MesProductProcedureDao mesProductProcedureDao;
	@Resource
	private MesProcedurePropertyDao mesProcedurePropertyDao;

	@Resource
	private CompanyinfoDao companyInfoDao;

	@Resource
	private CompanyfileDao companyfileDao;
	@Resource
	private MesDriverTypeDao mesDriverTypeDao;
	@Resource
	private StatisticsService statisticsService;
	@Resource
	private MesProcedurePropertyService propertyServ;
	@Resource
	private MesEnergyDao mesEnergyDao;
	@Autowired
	private MesProductionService mesProductionService;
	@Resource ProductAndEnergyAndDriverChartService productAndEnergyAndDriverChartService;

	@Resource MesDriverPointService mesDriverPointService;
	
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
	/**
	 * 进入监控设计页面
	 */
	@Value("${monitorStomp}")
	private String monitorStomp;
	@SendTemplate
	@RequiresPermissions("monitorDesginer:view")
	@RequestMapping(value="monitorIndex", method=RequestMethod.GET)
	public ModelAndView index(MonitorPainter monitorPainter, HttpServletRequest request) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null){
			return new ModelAndView("error/403");
		}
		List<MesDrivertype> drivertypes = mesDriverTypeDao.findAll(new Specification<MesDrivertype>() {
			@Override
			public Predicate toPredicate(Root<MesDrivertype> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate p = builder.equal(root.get("companyinfo").get("id").as(Long.class),SecurityUtils.getShiroUser().getCompanyid());
				return query.where(p).getRestriction();
			}
		});
		List<Long> ids = new ArrayList<>();
		for(MesDrivertype mesDrivertype : drivertypes){
			ids.add(mesDrivertype.getId());
		}
		List<Companyfile> files = new ArrayList<>();
		if(ids.size() != 0){
			files = companyfileDao.findAll(new Specification<Companyfile>() {
				@Override
				public Predicate toPredicate(Root<Companyfile> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
					// TODO Auto-generated method stub
					return query.where(builder.equal(root.get("parenttype").as(String.class),CompanyfileType.DRIVERTYPEFILE.toString()),root.get("parentid").in(ids)).getRestriction();
				}
			});
		}


		if(monitorPainter.getId() != 0){
			monitorPainter = monitorPainterService.findById(monitorPainter.getId());
		}
		return new ModelAndView(INDEX).addObject("imageList", files).addObject("monitorPainter", monitorPainter).addObject("companyId", companyId);
	}

	/**
	 * 进入监控页面
	 * @param request
	 * @return
	 */
	@RequiresPermissions("monitor:view")
	@RequestMapping(value="toMonitor", method=RequestMethod.GET)
	public ModelAndView toMonitor(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("monitor/monitor");
		modelAndView.addObject("currentUserId", SecurityUtils.getLoginUser().getId());
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null){
			return new ModelAndView("error/403");
		}
		List<MonitorPainterUser> list = monitorPainterUserDao.findByUserId(SecurityUtils.getLoginUser().getId());
		List<Long> ids = new ArrayList<>();
		for(MonitorPainterUser monitorPainterUser : list){
			ids.add(monitorPainterUser.getMonitorPainterId());
		}
		// 送检/制样/检验入库 检测报告录入 检测报告效果 监控屏
		Properties properties = new Properties();
		InputStream in = WebSocketBrokerConfig.class.getClassLoader().getResourceAsStream("/jmsConfig.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		monitorStomp = properties.getProperty("monitorStomp");
		// JMS链接地址
		modelAndView.addObject("monitorStomp", monitorStomp);

		if(ids.size() != 0){
			List<MonitorPainter> monitorPainters = monitorPainterService.findByIdIn(ids,companyId);
			User user = userDao.findOne(SecurityUtils.getLoginUser().getId());
//			User user = userDao.findOne(monitorPainters.get(0).getUserId());
			modelAndView.addObject("user", user);
			modelAndView.addObject("monitorPainterList", monitorPainters);
		}
		return modelAndView;
	}
	
	/**
	 * 根据监控画面ID获取用户名
	 * @param monitorPainterId
	 * @return
	 */
	@RequestMapping("/getUserName")
	@ResponseBody
	public String getUserName(Long monitorPainterId){
		long userId = monitorPainterService.findById(monitorPainterId).getUserId();
		User user = userDao.findOne(userId);
		if(user==null){
			return "未知";
		}
		return user.getRealname()+","+user.getId();
	}
	
	@RequestMapping(value="showCountTime", method=RequestMethod.GET)
	public ModelAndView showCountTime(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("monitor/showCountTime");

		return modelAndView;
	}

	@RequestMapping(value="mesGateWayMonitorPage", method=RequestMethod.GET)
	public ModelAndView mesGateWayMonitorPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("monitor/mesGateWayMonitorPage");
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null){
			return new ModelAndView("error/403");
		}
		List<MesPointGateway> mesGateways = Lists.newArrayList();;
		MesPointGateway allSelect = new MesPointGateway();
		allSelect.setId(Long.valueOf("0"));
		allSelect.setName("全部");
		mesGateways.add(allSelect);
		mesGateways.addAll(mesPointGatewayService.findByCompanyid(companyId));
		return modelAndView.addObject("mesGateways", mesGateways);
	}
	
    @RequestMapping(value="mesProceseAlter", method=RequestMethod.GET)
    public ModelAndView mesProceseAlter(HttpServletRequest request) {
        List<MesPointType> mesPointTypes = mptServ.findAllOrderByName();
        String bindType = request.getParameter("bindType");
        String dataMac = request.getParameter("data-mac");
        MesPointGateway mac = mesPointGatewayService.findByMac(dataMac);
        String dataCodekey = request.getParameter("data-codekey");
        String countTime = request.getParameter("data-countTime");
        ModelAndView modelAndView = new ModelAndView("monitor/mesProceseAlter");
        Long companyId = SecurityUtils.getShiroUser().getCompanyid();
        if(companyId == null){
            return new ModelAndView("error/403");
        }
        List<MesPointGateway> mesGateways = Lists.newArrayList();;
        MesPointGateway allSelect = new MesPointGateway();
        allSelect.setId(Long.valueOf("0"));
        allSelect.setName("全部");
        mesGateways.add(allSelect);
        mesGateways.addAll(mesPointGatewayService.findByCompanyid(companyId));
//      return modelAndView.addObject("mesGateways", mesPointGatewayService.findByCompanyid(companyId)).addObject("bindType", bindType)
        return modelAndView.addObject("mesGateways", mesGateways).addObject("bindType", bindType)
                .addObject("dataMac", null != mac ? mac.getId() : "")
                .addObject("dataCodekey", null != dataCodekey ? dataCodekey : "")
                .addObject("countTime", countTime)
                .addObject("mesPointTypes", mesPointTypes);
    }
	
    @RequestMapping(value="mesPointsMonitorPage", method=RequestMethod.GET)
    public ModelAndView mesPointsMonitorPage(HttpServletRequest request) {
        List<MesPointType> mesPointTypes = mptServ.findAllOrderByName();
        String bindType = request.getParameter("bindType");
        String dataMac = request.getParameter("data-mac");
        MesPointGateway mac = mesPointGatewayService.findByMac(dataMac);
        String dataCodekey = request.getParameter("data-codekey");
        String countTime = request.getParameter("data-countTime");
        ModelAndView modelAndView = new ModelAndView("monitor/mesPointsMonitorPage");
        Long companyId = SecurityUtils.getShiroUser().getCompanyid();
        if(companyId == null){
            return new ModelAndView("error/403");
        }
        List<MesPointGateway> mesGateways = Lists.newArrayList();;
        MesPointGateway allSelect = new MesPointGateway();
        allSelect.setId(Long.valueOf("0"));
        allSelect.setName("全部");
        mesGateways.add(allSelect);
        mesGateways.addAll(mesPointGatewayService.findByCompanyid(companyId));
//      return modelAndView.addObject("mesGateways", mesPointGatewayService.findByCompanyid(companyId)).addObject("bindType", bindType)
        return modelAndView.addObject("mesGateways", mesGateways).addObject("bindType", bindType)
                .addObject("dataMac", null != mac ? mac.getId() : "")
                .addObject("dataCodekey", null != dataCodekey ? dataCodekey : "")
                .addObject("countTime", countTime)
                .addObject("mesPointTypes", mesPointTypes);
    }
	@RequestMapping(value="mesDriverMonitorPage", method=RequestMethod.GET)
	public ModelAndView mesDriverMonitorPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("monitor/mesDriverMonitorPage");
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return new ModelAndView("error/403");
		Specification<MesDrivertype> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertype.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				);
		List<MesDrivertype> mesDrivertypes = mdtServ.findAll(specification);
		modelAndView.addObject("mesDrivertypes", mesDrivertypes).addObject("companyinfos", cpService.getTreeFactory());
		return modelAndView;
	}
	
	/**
	 * 进入保存监控画面
	 * @param monitorPainter
	 * @param request
	 * @return
	 */
	@RequestMapping(value="toSaveMonitor", method=RequestMethod.GET)
	public ModelAndView toSaveMonitor(MonitorPainter monitorPainter, HttpServletRequest request) {
		List<Long> ids = new ArrayList<>();
		ModelAndView modelAndView = new ModelAndView("monitor/saveMonitor");
		Long currentUserId = SecurityUtils.getLoginUser().getId();
		modelAndView.addObject("currentUserId", currentUserId);
		List<Usercompanys> usercompanyList = userCompanyDao.findByCompanyinfoId(SecurityUtils.getShiroUser().getCompanyid());
		for(Usercompanys usercompanys : usercompanyList){
			ids.add(usercompanys.getUserid());
		}
		if(monitorPainter.getId() != 0){
			MonitorPainter existMonitorPainter = monitorPainterService.findById(monitorPainter.getId());
			monitorPainter.setName(existMonitorPainter.getName());
			List<MonitorPainterUser> monitorPainterUsers = monitorPainterUserDao.monitorPainterId(monitorPainter.getId());
			List<Long> selectedIds = new ArrayList<>();
			for(MonitorPainterUser monitorPainterUser : monitorPainterUsers){
				selectedIds.add(monitorPainterUser.getUserId());
			}
			modelAndView.addObject("selectedIds", selectedIds.toString());
			modelAndView.addObject("painterFounderId", existMonitorPainter.getUserId());
		}
		List<User> users = userDao.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return query.where(root.get("id").in(ids)).getRestriction();
			}
		});
		for(int i=0;i<users.size();i++){
			if(users.get(i).getId().longValue()==currentUserId.longValue()){
				users.remove(i);
			}
		}
		return modelAndView.addObject("users", users)/*.addObject("monitorPainterList", monitorPainterService.findByIdIn(ids))*/;
	}
	
	/**
	 * 保存监控画面
	 * @param request
	 * @param monitorPainter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public @ResponseBody Map<String,String> insert(HttpServletRequest request,
			MonitorPainter monitorPainter) throws Exception {
		Map<String, String> map = new HashMap<String,String>();
		/**
		 * 这个写法有问题,主要原因是前台ajax请求提交html元素的时候  把尖括号，分号转义了
		 */
		monitorPainter.setBindingData(monitorPainter.getBindingData().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		monitorPainter.setChartsData(monitorPainter.getChartsData().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		monitorPainter.setComponents(monitorPainter.getComponents().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		monitorPainter.setConnections(monitorPainter.getConnections().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		monitorPainter.setDomContent(monitorPainter.getDomContent().replaceAll("&quot;", "\"").replaceAll("&amp;quot;", "'").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		monitorPainter.setElementsInfo(monitorPainter.getElementsInfo().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		monitorPainter.setContainer(monitorPainter.getContainer().replaceAll("&quot;", "\""));
		if(null != monitorPainter.getBackground()){
			monitorPainter.setBackground(monitorPainter.getBackground().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		}
		monitorPainter.setIsActive("-1");
		monitorPainter.setDriverSns(monitorPainter.getDriverSns().replaceAll("&quot;", "\""));
		monitorPainter.setMonitorTableData(monitorPainter.getMonitorTableData().replaceAll("&quot;", "\""));
		monitorPainter.setCompanyId(SecurityUtils.getShiroUser().getCompanyid());
		/* 2019-05-15 slq spc数据*/
		monitorPainter.setSpcData(monitorPainter.getSpcData().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		monitorPainter.setSpcAnalysisData(monitorPainter.getSpcAnalysisData().replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		/* end */
		MonitorPainter result = null;
		if(monitorPainter.getId() == 0){
			MonitorPainter painter = monitorPainterService.findByNameAndCompanyId(monitorPainter.getName(),monitorPainter.getCompanyId());
			if(null != painter){
				map.put("result", "error");
				return map;
			}
		}

		MonitorPainter painter = monitorPainterService.findById(monitorPainter.getId());
		if(null != painter){
			monitorPainter.setUserId(painter.getUserId());
			BeanUtils.copyProperties(painter, monitorPainter);
			//判断是否是创建人，如果不是就结束方法，跳过权限修改
			/*if(painter.getUserId()!=SecurityUtils.getLoginUser().getId().longValue()){
				result = monitorService.savePage(painter);
				if(null != result){
					map.put("result", "success");
				}
				return map;
			}*/
			monitorPainterUserService.deleteMonitorPaintUserByPainterId(painter.getId());
		}else {
			monitorPainter.setUserId(SecurityUtils.getLoginUser().getId());
			painter = monitorPainter;
		}
		result = monitorService.savePage(painter);
		//保存或者修改页面
		String authIds = request.getParameter("authIds");
		List<MonitorPainterUser> monitorPainterUsers = new ArrayList<>();
		if((StringUtils.isNotEmpty(authIds)) && (authIds.length() != 0) && (!"null".equals(authIds))){
			JSONArray jsonArray = new JSONArray(authIds);
			for(int i = 0; i < jsonArray.length(); i++){
				long userId = jsonArray.getLong(i);
				MonitorPainterUser monitorPainterUser = new MonitorPainterUser();
				monitorPainterUser.setMonitorPainterId(result.getId());
				monitorPainterUser.setUserId(userId);
				monitorPainterUsers.add(monitorPainterUser);
			}
		}
		MonitorPainterUser monitorPainter_User = new MonitorPainterUser();
		monitorPainter_User.setMonitorPainterId(result.getId());
		monitorPainter_User.setUserId(monitorPainter.getUserId());
		monitorPainterUsers.add(monitorPainter_User);
		monitorPainterUserService.saveMonitorPainterUsers(monitorPainterUsers);
		//ActiveMQManager.getAMQ().initConnection();
		//jmsTemplate.

		//monitorService.saveAndSyncMonitor(result.getId(),request.getParameter("driverIds"));

		if(null != result){
			map.put("result", "success");
		}
		return map;
	}
	
	
	
	/*	@RequestMapping(value="insert", method=RequestMethod.POST)
	public @ResponseBody Map<String,String> insert(HttpServletRequest request,
			@RequestBody ProductionLine productionLine) {
		//保存或者修改页面
		ProductionLine result = monitorService.savePage(productionLine);
		Map<String, String> map = new HashMap<String,String>();
		if(null != result){
			map.put("result", "success");
		}else{
			map.put("result", "fail");
		}
		return map;
	}
	 */	
	@RequestMapping(value="getProductionLineByCompanyId", method=RequestMethod.POST)
	public @ResponseBody String getProductionLineByCompanyId(HttpServletRequest request,long companyId) throws JsonProcessingException {
		List<MesProductline> list = mesProductlineService.findByCompanyid(companyId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(list);
	}
	@RequestMapping(value="getProductLineByCompanyId/{id}", method=RequestMethod.POST)
	public @ResponseBody String getProductLineByCompanyId(HttpServletRequest request,@PathVariable Long id) throws JsonProcessingException {
		List<MesProductline> list = mesProductlineService.findByCompanyid(id);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(list);
	}
	
	/*
	 * 获取监控画面
	 */
	@RequestMapping(value="getPainter", method=RequestMethod.POST)
	public @ResponseBody String getPainter(HttpServletRequest request,MonitorPainter monitorPainter) throws JsonProcessingException {
		monitorPainter = monitorService.getMonitorPainter(monitorPainter);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(monitorPainter);
	}
	@RequestMapping(value="delMonitor", method=RequestMethod.POST)
	public @ResponseBody String delMonitor(HttpServletRequest request,MonitorPainter monitorPainter) throws JsonProcessingException {
		Long userId = SecurityUtils.getShiroUser().getUser().getId();

		int isDeleted = monitorService.deleteMonitor(monitorPainter,userId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", isDeleted);
		return jsonObject.toString();
	}
	/**
	 * 监控画面ajax获取设备类别
	 * @param request
	 * @param driveType
	 * @param page
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="getDeviceName_ajax", method=RequestMethod.GET)
	public @ResponseBody String getDeviceList(HttpServletRequest request,MesDrivertype driveType,Page page) throws JsonProcessingException {
		Specification<MesDriver> specification = DynamicSpecifications.bySearchFilter(request, MesDriver.class
				,new SearchFilter("companyinfo.id", Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
		Map<String, Object> map = new HashMap<String, Object>();
		List<MesDriver> mesDriverList = driverServ.findPage(specification, page);

		//List<MesDriver> mesDriverList = mesDrivertype.getMesDrivers();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesDrivers", mesDriverList);
		return mapper.writeValueAsString(map);
	}
	
	/**
	 * 获取网关列表
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="getGatewayName_ajax", method=RequestMethod.GET)
	public @ResponseBody String getGatewayList(HttpServletRequest request,Page page) throws JsonProcessingException {
		Specification<MesPointGateway> specification = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class);
		Map<String, Object> map = new HashMap<String, Object>();
		List<MesPointGateway> mesGatewayList = mesPointGatewayService.findPage(specification, page);

		//List<MesDriver> mesDriverList = mesDrivertype.getMesDrivers();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesDrivers", mesGatewayList);
		return mapper.writeValueAsString(map);
	}
	@RequestMapping(value="getGateway_ajax")
	public @ResponseBody String getGatewayListWithoutPage(HttpServletRequest request) throws JsonProcessingException {
		Specification<MesPointGateway> specification = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class);
		List<MesPointGateway> mesGatewayList = mesPointGatewayService.findAll(specification);

		//List<MesDriver> mesDriverList = mesDrivertype.getMesDrivers();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(mesGatewayList);
	}
	
	/**
	 * 获取设备属性列表
	 * @param request
	 * @param mesDriver
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="getDeviceProperties_ajax", method=RequestMethod.GET)
	public @ResponseBody String getDevicePropertyList(HttpServletRequest request,MesDriver mesDriver) throws JsonProcessingException {
		return monitorService.getDriverPropertyListByDriver(mesDriver);
	}
	
	/**
	 * 更新监控画面统计图表：柱状图/折线图/饼图
	 * @param request
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getJson_ajax", method=RequestMethod.POST)
	public @ResponseBody String getJson_ajax(HttpServletRequest request,ChartsBaseMonitor monitor) throws Exception {
		return monitorService.caculate(monitor);
		//return jsonObject.toString();
		//		return monitorService.generateEChart(monitor, eneryArray, recordArray);
	}

	@RequestMapping(value="getSpcJson_ajax", method=RequestMethod.POST)
	public @ResponseBody String getSpcJson_ajax(HttpServletRequest request, AnalyzeSearch monitor) throws Exception {
		Map<String, Object> map = statisticsService.spcDataSearch(monitor);
		Map<String, Object> map_new = new HashMap<>();
		List<CgAnalyzeData> list = (List<CgAnalyzeData>) map.get("cgAnalyzeData");
		Collections.reverse(list);
		Map<String, Map<String, Double>> map_ = new HashMap<>();
		String jiance_name = "";
		if (list.size() >0){
			jiance_name = list.get(0).getProcedurePropertyName();
		} else {
			MesProcedureProperty mesProcedureProperty = propertyServ.findById(monitor.getProcedurePropertyId());
			jiance_name = mesProcedureProperty.getPropertyname();
			Map<String, Double> map_string = new HashMap<>();
			map_string.put(jiance_name, 0.0);
			map_.put("spc实时监控", map_string);
		}
		int i = 0;
		for (CgAnalyzeData cgAnalyzeData : list) {
			Map<String, Double> map_string = new HashMap<>();
			map_string.put("SPC-"+jiance_name, Double.parseDouble(cgAnalyzeData.getValue()));
//			map_.put(String.valueOf(cgAnalyzeData.getId()), map_string);
			map_.put(String.valueOf(i), map_string);
			i++;
		}
		Map<String, Double> markeLineMap = new ConcurrentHashMap<>();
		map_.put("markLine", markeLineMap);
		EchartsOption<LineOption> echartsOption = new LineOption();
		String result = "";
		result = echartsOption.title( "折线图").data(map_).toString();
		return result;
	}

	@RequestMapping(value="getSpcAnalysisJson_ajax", method=RequestMethod.POST)
	public @ResponseBody String getSpcAnalysisJson_ajax(HttpServletRequest request, AnalyzeSearch monitor) throws Exception {
		Map<String, Object> map = statisticsService.spcMonitorDataSearch(monitor);
		MesProcedureProperty mesProcedureProperty = propertyServ.findById(monitor.getProcedurePropertyId());
		String jiance_name = "";
		jiance_name = mesProcedureProperty.getPropertyname();
		Map<String, Object> map_new = new HashMap<>();
		List<MonitorSpc> list = new ArrayList<>();
		Map<String, Map<String, Double>> map_ = new ConcurrentHashMap<>();
		int i =0;
		List<MonitorSpc> monitorSpcList = (List<MonitorSpc>) map.get("data");
		if (monitorSpcList.size() == 0){
			for (int j=0; j<4; j++){
				Map<String, Double> map_string = new HashMap<>();
				map_string.put(monitor.getQueryname()+"-"+jiance_name, 0.2);
				map_.put(String.valueOf(j), map_string);
			}
		}
		for (MonitorSpc monitorSpc : monitorSpcList) {
			Map<String, Double> map_string = new HashMap<>();
			Double value = null;
			if (monitor.getQueryname().equals("CP")){
				value = monitorSpc.getCp();
			} else if (monitor.getQueryname().equals("PP")){
				value = monitorSpc.getPp();
			} else if (monitor.getQueryname().equals("PPK")){
				value = monitorSpc.getPpk();
			} else if (monitor.getQueryname().equals("CPK")){
				value = monitorSpc.getCpk();
			}
			map_string.put(monitor.getQueryname()+"-"+jiance_name, Double.parseDouble(value.toString()));
            String time = String.valueOf(monitorSpc.getCreatetime());
            time = time.substring(5,19);
            //
//			time = time.substring(5,10)+" "+time.substring(11,19);
			map_.put(time, map_string);
//			map_.put(String.valueOf(i), map_string);
			i++;
		}

		Map<String, Double> makepointMap = new ConcurrentHashMap<>();
		if (monitor.getUpper() != null && !monitor.getUpper().equals("")) {
			String uppervalues = monitor.getUpper();
			makepointMap.put("uppervalues", Double.parseDouble(uppervalues));
			map_.put("makepoint", makepointMap);
		}
		if (monitor.getLower() != null && !monitor.getLower().equals("")){
			String lowervalues = monitor.getLower();
			makepointMap.put("lowervalues", Double.parseDouble(lowervalues));
			map_.put("makepoint", makepointMap);
		}
		EchartsOption<LineOption> echartsOption = new LineOption();
		String result = "";
		GsonOption gsonOption = echartsOption.title( "折线图").data(map_);
		List<Series> Series = gsonOption.getSeries();
		result = gsonOption.toString();
		return result;
		//return jsonObject.toString();
		//		return monitorService.generateEChart(monitor, eneryArray, recordArray);
	}

	   /**
     * 更新监控画面统计图表：切片图
     * @param request
     * @param monitor
     * @return
     * @throws Exception
     */
    @RequestMapping(value="getJson_ajaxFroDriver", method=RequestMethod.POST)
    public @ResponseBody String getJson_ajaxFroDriver(HttpServletRequest request,ChartsBaseMonitor monitor) throws Exception {
        return monitorService.getDriverStatusData(monitor);
        //return jsonObject.toString();
        //      return monitorService.generateEChart(monitor, eneryArray, recordArray);
    }
    
    /**
  * 更新监控画面统计图表：码表
  * @param request
  * @param monitor
  * @return
  * @throws Exception
  */
 @RequestMapping(value="getJson_ajaxFroDriverProperty", method=RequestMethod.POST)
 public @ResponseBody String getJson_ajaxFroDriverProperty(HttpServletRequest request,ChartsBaseMonitor monitor) throws Exception {
     return monitorService.getDriverPropertyData(monitor);
 }

	@SuppressWarnings("unchecked")
    @RequestMapping(value="getHistoryTrend", method=RequestMethod.POST)
	public @ResponseBody String getHistoryTrend(HttpServletRequest request,PropertyTrendSearch propertyTrendSearch) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, -10);
		propertyTrendSearch.setBegin(calendar.getTime());
		calendar.add(Calendar.MINUTE, 10);

		propertyTrendSearch.setEnd(calendar.getTime());
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
		Map<String,Map<String,Double>> tempMap = new ConcurrentHashMap<>();
		for(List<MesProcedureProperty> list : procedureList){
			for(MesProcedureProperty mesProcedureProperty : list){
				if(!tempMap.containsKey(mesProcedureProperty.getCheckTime())){
					Map<String,Double> valueMap = new ConcurrentHashMap<>();
					for(MesProcedureProperty _mesProcedureProperty : mesProcedureProperties){
						valueMap.put(_mesProcedureProperty.getPropertyname(), 0D);
					}
					tempMap.put(mesProcedureProperty.getCheckTime(), valueMap);
					tempMap.get(mesProcedureProperty.getCheckTime()).put(mesProcedureProperty.getPropertyname(), Double.parseDouble(mesProcedureProperty.getCheckValue()));
				}
			}
		}
		// nisin
		// 客户SAP系统的实施顾问，解决FI/MM模块的SAP扩展模块项目实施。
		// 
		// 产品需求模块的
//		负责客户方的开发工作的展开
//		・作为客户方和公司间的联络窗口
//		・开发团队的人员管理工作

        // 2018/03/13 嘉兴双环传动-折线图初期化追加上下公差 start
        if (null != mesProcedureProperties && 0 < mesProcedureProperties.size()) {
            Map<String, Double> makepointMap = new ConcurrentHashMap<>();
            BigDecimal uppervalues = new BigDecimal(mesProcedureProperties.get(0).getUppervalues());
            BigDecimal standardvalues = new BigDecimal(mesProcedureProperties.get(0).getStandardvalues());
            BigDecimal lowervalues = new BigDecimal(mesProcedureProperties.get(0).getLowervalues());
            makepointMap.put("uppervalues", standardvalues.add(uppervalues).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            makepointMap.put("lowervalues", standardvalues.add(lowervalues).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            tempMap.put("makepoint", makepointMap);
        }
        // 2018/03/13 嘉兴双环传动-折线图初期化追加上下公差 end
		/*List<String> lineOptions = new ArrayList<>();
		for(long propertyId : tempMap.keySet()){
			LineOption lineOption = new LineOption().title(propertyTrendSearch.getProcedurePropertyName()+"参数属性趋势");
			lineOptions.add(lineOption.data(tempMap.get(propertyId)));
		}*/
		LineOption lineOption = new LineOption().title("参数实时监控");
		if(null == procedureList || procedureList.size() == 0){
			Map<String,Double> valueMap = new ConcurrentHashMap<>();
			for(MesProcedureProperty _mesProcedureProperty : mesProcedureProperties){
				valueMap.put(_mesProcedureProperty.getPropertyname(), 0D);
			}
			tempMap.put("参数实时监控", valueMap);
		}
		GsonOption option = lineOption.data(tempMap);
		for(Series<Line> line : option.series()){
			for(MesProcedureProperty mesProcedureProperty : mesProcedureProperties){
				if(mesProcedureProperty.getPropertyname().equals(line.name())){
					line.label().normal().formatter(mesProcedureProperty.getMesPoints().getCodekey()).show(true);
					line.itemStyle().normal().formatter(mesProcedureProperty.getMesPoints().getCodekey()).show(false);
				}
			}
		}
		return option.toString();
	}

	@RequestMapping("/getEnergyInfo") 
	public @ResponseBody Map<String,String> getEnergyInfo(String monitors) throws Exception{
		/*monitors = monitors.replaceAll("&quot;", "\"");
    	org.json.JSONArray jsonArray = new org.json.JSONArray(monitors);
    	List<ChartsBaseMonitor> chartsBaseMonitors = new ArrayList<>();
    	for(int i = 0; i < jsonArray.length(); i++){
    		JSONObject jsonObject = jsonArray.getJSONObject(0);
    		ChartsBaseMonitor chartsBaseMonitor = new ChartsBaseMonitor();
    		Class<ChartsBaseMonitor> clazz = ChartsBaseMonitor.class;
    		Map<String,String> map = new HashMap<>();
    		for(String field : jsonObject.keySet()){
    			if(field.indexOf("map") != -1){
    				String idArray = field.split("map")[1];
    				org.json.JSONArray array = new org.json.JSONArray(idArray);
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
    		chartsBaseMonitors.add(chartsBaseMonitor);
    	}
    	return monitorService.getEnergyInfo(chartsBaseMonitors);*/
		return null;
	}
	@RequestMapping(value="getDummyDevice_ajax/{id}", method=RequestMethod.GET)
	public @ResponseBody String getDummyDevice_ajax(@PathVariable Long id,HttpServletRequest request) throws JsonProcessingException {
		return monitorService.readJSONFile("DummyDevice" + id);
	}
	
	/**
	 * 获取模板
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="getTemplate", method=RequestMethod.GET)
	public @ResponseBody List<MesPointsTemplate> getTemplate(HttpServletRequest request) throws JsonProcessingException {
		return monitorService.getTemplate();
	}
	
	/**
	 * 获取当前用户工厂下的产线信息
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="getProductionLinesByCurrentUser", method=RequestMethod.POST)
	public @ResponseBody String getProductionLinesByCurrentUser(HttpServletRequest request) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(monitorService.getProductionLinesByCurrentUser(SecurityUtils.getShiroUser().getUser().getId()));
	}
	@RequestMapping(value="getMesDriverTypeList", method=RequestMethod.POST)
	public @ResponseBody List<MesDrivertype> getMesDriverTypeList(HttpServletRequest request) throws JsonProcessingException {
		return monitorService.getMesDriverTypeList(SecurityUtils.getShiroUser().getCompanyid());
	}
	
	/**
	 * 根据当前公司获取所有工厂
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/getFactorynameByCurrentCompany/{companyId}")
	public @ResponseBody String getFactorynameByCurrentCompany(@PathVariable long companyId,HttpServletRequest request) throws JsonProcessingException{
		if(companyId == 0){
			companyId = SecurityUtils.getShiroUser().getCompanyid();
		}
		List<Companyinfo> companyinfos = this.monitorService.findFactoryByCompany(companyId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(companyinfos);
	}

	//updatedBy:xsq 7.14 总厂对应子工厂
	/**
	 * updatedBy:xsq
	 * @param factoryParentId
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	/*@RequestMapping("/getChildFactorynameByFactoryName/{factoryParentId}")
	public @ResponseBody String getChildFactorynameByFactory(@PathVariable long factoryParentId,List<Companyinfo> childFactorys,HttpServletRequest request) throws JsonProcessingException{
		List<Companyinfo> childFactory = this.monitorService.findChildFactoryByFactory(factoryParentId);
		//childFactorys.add(childFactory);
		if(childFactory.size() != 0 && !childFactory.isEmpty()){
			for(Companyinfo childFac:childFactory){
				//List<Companyinfo> childrenFac = this.monitorService.findChildFactoryByFactory(childFac.getId());
				//if(childrenFac.size() != 0 && !childrenFac.isEmpty()){
					//for(Companyinfo chi:childrenFac){
						childFactorys.add(childFac);
						getChildFactorynameByFactory(childFac.getId(),childFactorys,request);
					//}
				//}
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		log.info("查到的总厂对应的子工厂："+childFactorys);
		return  mapper.writeValueAsString(childFactorys);
	}*/
	
	
	//查询出所有的子工厂
	@RequestMapping("/getChildFactorynameByFactoryName/{factoryParentId}")
	public @ResponseBody String getChildFactorynameByFactory(@PathVariable long factoryParentId,HttpServletRequest request) throws JsonProcessingException{
		List<Companyinfo> childFactorys = productAndEnergyAndDriverChartService.getFactoryListByFactoryId(factoryParentId);
		//去掉总工厂
		childFactorys.remove(0);
		ObjectMapper mapper = new ObjectMapper(); 
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		//log.info("查到的总厂对应的子工厂："+childFactorys);
		return  mapper.writeValueAsString(childFactorys);
	}

	
	@RequestMapping("/getDrivernameByCurrentLine/{lineId}")
	public @ResponseBody String getDrivernameByCurrentLine(@PathVariable long lineId,HttpServletRequest request) throws JsonProcessingException{
		List<MesDriver> mesDrivers = this.monitorService.findMesDriverByProducLine(lineId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesDrivers);
	}
    @RequestMapping("/getpropertyByDriverId/{driverId}")
    public @ResponseBody String getpropertyByDriverId(@PathVariable long driverId,HttpServletRequest request) throws JsonProcessingException{
        List<MesDrivertypeProperty> mesDrivertypePropertyList = this.monitorService.getMesDrivertypePropertyList(driverId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return  mapper.writeValueAsString(mesDrivertypePropertyList);
    }
	@RequestMapping("/getGatewayByFactory/{factoryId}")
	public @ResponseBody String getGatewayByFactory(@PathVariable long factoryId,HttpServletRequest request) throws JsonProcessingException{
		List<MesDriver> mesDrivers = this.driverServ.findByCompanyidAndDifferencetype(factoryId,"0");
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesDrivers);
	}
	@RequestMapping("/getCompanynameByCurrentUser")
	public @ResponseBody String getCompanynameByCurrentUser(HttpServletRequest request) throws JsonProcessingException{
		List<Usercompanys> usercompanys = userCompanyDao.findAll(new Specification<Usercompanys>() {
			@Override
			public Predicate toPredicate(Root<Usercompanys> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate1 = builder.equal(root.get("userid").as(Long.class),SecurityUtils.getShiroUser().getUser().getId());
				Predicate predicate2 = builder.equal(root.get("companyinfo").get("companystatus").as(String.class),"1");
				return query.where(predicate1,predicate2).getRestriction();
			}
		});

		/*List<Companyinfo> companyinfos = companyInfoDao.findAll(new Specification<Companyinfo>() {
			@Override
			public Predicate toPredicate(Root<Companyinfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				Predicate p = builder.equal(root.joinList("usercompanyses").get("userid").as(Long.class),SecurityUtils.getShiroUser().getUser().getId());
				return query.where(p).getRestriction();
			}
		}) ;*/
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(usercompanys);
	}
	
	/**
	 * 获取当前选择的工厂信息
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getCompanynameByCurrentChose")
	public @ResponseBody String getCompanynameByCurrentChose(HttpServletRequest request) throws JsonProcessingException{
		Long companyid = SecurityUtils.getShiroUser().getCompanyid();

		List<Companyinfo> companyinfos = new ArrayList<>();
		companyinfos.add( cpService.findById(companyid));

		/*List<Companyinfo> companyinfos = companyInfoDao.findAll(new Specification<Companyinfo>() {
			@Override
			public Predicate toPredicate(Root<Companyinfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				Predicate p = builder.equal(root.joinList("usercompanyses").get("userid").as(Long.class),SecurityUtils.getShiroUser().getUser().getId());
				return query.where(p).getRestriction();
			}
		}) ;*/
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(companyinfos);
	}
	
	/**
	 * 根据公司id获取生产记录信息
	 * @param companyId
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getProductionByCompanyId/{companyId}")
	public @ResponseBody String getProductionByCompanyId(@PathVariable long companyId,HttpServletRequest request) throws JsonProcessingException{
		if(companyId == 0){
			companyId = SecurityUtils.getShiroUser().getCompanyid();
		}
		List<MesProduct> mesProducts = monitorService.getProductionByCompanyId(companyId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesProducts);
	}
	@RequestMapping("/getProcedureByProduct/{productnum}")
	public @ResponseBody String getProcedureByProduct(@PathVariable Long productnum,HttpServletRequest request) throws JsonProcessingException{
		MesProduct mesProduct = mesProductDao.findOne(productnum);
		//mesProduct.setId(productId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesProductProcedureDao.findByMesProduct(mesProduct));
	}
	
	/**
	 * 监控页面的数据更新主要逻辑，包括动态文本框，矩形，圆形等
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getCountPoint")
	public @ResponseBody String getCountPoint(HttpServletRequest request) throws JsonProcessingException{
		String countPoints = request.getParameter("countPoints");
		System.out.println("-----countPoints-------" + countPoints);
		System.out.println("-----countPoints-------" + countPoints);
		System.out.println("-----countPoints-------" + countPoints);
		System.out.println("-----countPoints-------" + countPoints);
		JSONObject jsonObject = new JSONObject(countPoints);
		//String gatewayId = request.getParameter("gatewayId");
		JSONObject result = new JSONObject();
		if(!StringUtils.isBlank(countPoints)){
			for (String key : jsonObject.keySet()) {
				MesPointGateway gateway = mesPointGatewayService.findByMac(key);
				System.out.println("-----gateway-------" + gateway.getName());
				System.out.println("-----gateway-------" + gateway.getName());
				System.out.println("-----gateway-------" + gateway.getName());
				System.out.println("-----gateway-------" + gateway.getName());
				List<MesPoints> mesPointses = gateway.getMesPointses();
				System.out.println("-----mesPointses-------" + mesPointses.size());
				System.out.println("-----mesPointses-------" + mesPointses.size());
				System.out.println("-----mesPointses-------" + mesPointses.size());
				System.out.println("-----mesPointses-------" + mesPointses.size());
				Map<String,MesPoints> map = new ConcurrentHashMap<>();
				for(MesPoints mesPoint : mesPointses){
					map.put(mesPoint.getCodekey(), mesPoint);
				}
				JSONArray jsonArray = jsonObject.getJSONArray(key);
				//for(Iterator<Object> itr = jsonObject.getJSONArray(key).iterator(); itr.hasNext();){
				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject object = jsonArray.getJSONObject(i);
					String unit = "";
					String countTime = "";
					if(object.has("unit")){
						unit = object.getString("unit");
						object.remove("unit");
					}
					if(object.has("countTime")){
						countTime = object.getString("countTime");
						object.remove("countTime");
					}
//					Calendar calendarToday = Calendar.getInstance();
//					calendarToday.setTime(new Date());
//					Date begin = new Date();
//					calendarToday.set(Calendar.HOUR_OF_DAY, 0);
//					calendarToday.set(Calendar.MINUTE, 0);
//					calendarToday.set(Calendar.SECOND, 0);
//					if(!countTime.equals("")){
//						begin = DateUtils.parse(countTime);
//					}else{
//						Long current =System.currentTimeMillis();
//						Long zero =current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();
//						Calendar calendarTodayzero =Calendar.getInstance();
//						calendarTodayzero.setTimeInMillis(zero);
//						begin =calendarTodayzero.getTime();
//					}
//					System.out.println(calendarToday);
//					Calendar calendarSearch = Calendar.getInstance();
//					calendarSearch.setTime(begin);
					for (String key_ : object.keySet()) {
						String codeKey = object.getString(key_);
						System.out.println("-----codeKey-------" + codeKey);
						System.out.println("-----codeKey-------" + codeKey);
						System.out.println("-----codeKey-------" + codeKey);
						System.out.println("-----codeKey-------" + codeKey);
						if(null == map.get(codeKey) ){
							continue;
						}
						MesDriver currentMesDriver = map.get(codeKey).getCurrentMesDriver();
						System.out.println("-----currentMesDriver-------" + String.valueOf(currentMesDriver.getId()));
						System.out.println("-----currentMesDriver-------" + String.valueOf(currentMesDriver.getId()));
						System.out.println("-----currentMesDriver-------" + String.valueOf(currentMesDriver.getId()));
						System.out.println("-----currentMesDriver-------" + String.valueOf(currentMesDriver.getId()));
						if(null == currentMesDriver ){
							continue;
						}
//						MesProduct currentMesProduct = map.get(codeKey).getCurrentMesProduct();
						String tableNmStr = "";
						if(object.has("ELECTRIC")) {
							tableNmStr = "realElectricList";

						} else if(object.has("WATER")) {
							tableNmStr = "realWaterList";

						} else if(object.has("GAS")) {
							tableNmStr = "realGasList";

						} else if(object.has("D_COUNT")) {
							tableNmStr = "driveCount";
						}
						System.out.println("-----tableNmStr-------" + tableNmStr);
						System.out.println("-----tableNmStr-------" + tableNmStr);
						System.out.println("-----tableNmStr-------" + tableNmStr);
						System.out.println("-----tableNmStr-------" + tableNmStr);

						// Map<String, Object> _map = redisService.getHash(key+":"+object.getString(key_));
						// Object obj = _map.get(key_);
//						RedisConnection con =  redisTemplate.getConnectionFactory().getConnection();
//						con.select(2);
//						redisService.setRedisTemplate(redisTemplate);
						Object obj = redisService.getHash(tableNmStr, String.valueOf(currentMesDriver.getId()));
						System.out.println("-----obj-------" + obj);
						System.out.println("-----obj-------" + obj);
						System.out.println("-----obj-------" + obj);
						System.out.println("-----obj-------" + obj);
//						Double count = 0D;
						/*if(calendarSearch.after(calendarToday)){
							Double _count = 0D;
							if(key_.equals(MesPointType.TYPE_DRIVER_COUNT)){
								List<MesDriverStats> statsList = driverStatsDao.findByMesDriverIdAndUpdatetimeBetween(currentMesDriver.getId(), new Timestamp(calendarToday.getTimeInMillis()), new Timestamp(calendarSearch.getTimeInMillis()));
								for(MesDriverStats ds : statsList){
									_count += ds.getCount();
								}
							}else if(key_.equals(MesPointType.TYPE_PRODUCT_COUNT)){
								if(null == currentMesProduct){
									continue;
								}
								List<MesProduction> list = mesProductionService.findByTimeSpan(new Timestamp(calendarToday.getTimeInMillis()), new Timestamp(calendarSearch.getTimeInMillis()), currentMesDriver.getId(), currentMesProduct.getProductnum());
								for(MesProduction production : list){
									_count += production.getTotalCount();
								}
							}
							else{
								List<MesEnergy> energyList = mesEnergyDao.findByMesDriverIdAndUpdatetimeBetween(currentMesDriver.getId(), new Timestamp(calendarToday.getTimeInMillis()), new Timestamp(calendarSearch.getTimeInMillis()));
								for(MesEnergy mesEnergy : energyList){
									_count += mesEnergy.getValue();
								}
							}
							count = DecimalCalculate.sub(Double.valueOf(null != obj ? obj.toString() : "0"), _count);
						}else{ */
						
						
//							Double _count = 0D;
//							if(key_.equals(MesPointType.TYPE_DRIVER_COUNT)){
//								calendarToday.setTime(new Date());
//								List<MesDriverStats> statsList = driverStatsDao.findByMesDriverIdAndUpdatetimeBetween(currentMesDriver.getId(), new Timestamp(calendarSearch.getTimeInMillis()),new Timestamp(calendarToday.getTimeInMillis()));
//								for(MesDriverStats ds : statsList){
//									_count += ds.getCount();
//								}
//							}else if(key_.equals(MesPointType.TYPE_PRODUCT_COUNT)){
//								if(null == currentMesProduct){
//									continue;
//								}
//								List<MesProduction> list = mesProductionService.findByTimeSpan(new Timestamp(calendarToday.getTimeInMillis()), new Timestamp(calendarSearch.getTimeInMillis()), currentMesDriver.getId(), currentMesProduct.getProductnum());
//								for(MesProduction production : list){
//									_count += production.getTotalCount();
//								}
//							}else{
//								List<MesEnergy> energyList = mesEnergyDao.findByMesDriverIdAndUpdatetimeBetween(currentMesDriver.getId(), new Timestamp(calendarSearch.getTimeInMillis()),new Timestamp(calendarToday.getTimeInMillis()));
//								for(MesEnergy mesEnergy : energyList){
//									_count += mesEnergy.getValue();
//								}
//							}
							
							
							
//						DecimalFormat decimalFormat = new DecimalFormat("0");
						// count = DecimalCalculate.add(Double.valueOf(null != obj ? obj.toString() : "0"), _count);
						// String tmp = String.valueOf(count);
						String tmp = obj != null ? obj.toString() : "";
						System.out.println("-----tmp-------" + tmp);
						System.out.println("-----tmp-------" + tmp);
						System.out.println("-----tmp-------" + tmp);
						System.out.println("-----tmp-------" + tmp);

						if(object.has("D_COUNT")) {
	                        if(tmp.contains(".")){
	                            tmp = tmp.substring(0, tmp.indexOf("."));
	                        }
						}
						result.put(key+"_"+object.getString(key_)+"_"+countTime, tmp+" "+unit);
					}

				}
			}
		}

		return result.toString();
	}
	@RequestMapping("/getProcedurePropByProce/{procedureId}")
	public @ResponseBody String getProcedurePropByProce(@PathVariable long procedureId,HttpServletRequest request) throws JsonProcessingException{
		MesProductProcedure mesProductProcedure = new MesProductProcedure();
		mesProductProcedure.setId(procedureId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(mesProcedurePropertyDao.findByMesProductProcedure(mesProductProcedure));
	}
	
	/**
	 * 根据当前公司获取用户信息
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getUserByCurrentCompany")
	public @ResponseBody String getUserByCurrentCompany(HttpServletRequest request) throws JsonProcessingException{
		List<Usercompanys> usercompanyList = userCompanyDao.findAll(new Specification<Usercompanys>() {
			@Override
			public Predicate toPredicate(Root<Usercompanys> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate p1 =  builder.equal(root.get("companyinfo").get("id").as(Long.class),SecurityUtils.getShiroUser().getCompanyid());
				return query.where(p1).getRestriction();
			}
		});
		List<Long> ids = new ArrayList<>();
		for(Usercompanys usercompanys : usercompanyList){
			ids.add(usercompanys.getUserid());
		}
		List<User> users = userDao.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return query.where(root.get("id").in(ids)).getRestriction();
			}
		});
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(users);
	}
	
	/**
	 * 获取设备信息
	 * @param id
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getDriverInfo/{id}")
	public @ResponseBody String getDriverInfo(@PathVariable Long id,HttpServletRequest request) throws JsonProcessingException{
		JSONObject object = new JSONObject();
		object.put("success", monitorService. getDriverInfo(id));
		return object.toString();
	}
	
	/**
	 * 保存背景图片
	 * @param files
	 * @param widgetId
	 * @return
	 */
	@RequestMapping(value = "/saveBackgroundPic", method = RequestMethod.POST)
	@ResponseBody
	public String create(@RequestParam("files") MultipartFile[] files, String widgetId) {
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					try {
						Companyfile companyfile = fileSave(file);
						return AjaxObject.newOk(widgetId + "-" + companyfile.getId()+"").setCallbackType("").toString();
					} catch (Exception e) {
						e.printStackTrace();
						return AjaxObject.newError("上传失败:请检查上传文件是否正确!").setCallbackType("").toString();
					}
				}
			}
		}
		return AjaxObject.newError("上传失败:请检查上传文件是否正确!").setCallbackType("").toString();
	}
	private Companyfile fileSave(MultipartFile file) throws IOException {
		String fileBaseName = file.getOriginalFilename();
		String newFileName = UUID.randomUUID().toString()
				+ fileBaseName.substring(fileBaseName.lastIndexOf("."));
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		System.out.println("this is a test:"+this.getClass().getClassLoader().getResource("/").getPath());
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE/";
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new File(filePath + File.separator + newFileName);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		file.transferTo(uploadFile);
		Companyfile cpfile = new Companyfile();
		cpfile.setfilebasename(fileBaseName);
		cpfile.setfilelength(file.getSize());
		cpfile.setfilenewname(newFileName);
		cpfile.setfilepath(filePath);
		companyfileService.saveOrUpdate(cpfile);
		return cpfile;
	}
	
	/**
	 * 接收上传的监控画面文件，处理文件内容，向数据库插入数据
	 * 导入的监控画面文件中字符串内容格式 ： MonitorPainter对象的json格式字符串
	 * @param painterFile
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	@RequestMapping(value="/painterFileUpload", method = RequestMethod.POST)
	@ResponseBody
	public String painterFileUpload(@RequestParam("painterFile") MultipartFile painterFile,
			String painterNewName) throws IllegalAccessException, InvocationTargetException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		String fileContent = null;
		try {
			fileContent = new String(painterFile.getBytes());
		} catch (IOException e) {
			return "error";
		}
		if(StringUtils.isEmpty(fileContent)){
			return "error";
		}
		
		//原来的监控画面对象
		MonitorPainter originalPainter = null;
		try {
			originalPainter = mapper.readValue(fileContent, MonitorPainter.class);
		} catch (JsonParseException | JsonMappingException e) {
			return "error";
		}
		MonitorPainter result = null;
		MonitorPainter newPainter = new MonitorPainter(); //新的属于当前用户的监控画面对象
		if(null != originalPainter){
			BeanUtils.copyProperties(newPainter, originalPainter);
			newPainter.setId(0);
			newPainter.setUserId(SecurityUtils.getLoginUser().getId());
			newPainter.setCompanyId(SecurityUtils.getShiroUser().getCompanyid());
			//新的监控名字
			newPainter.setName(painterNewName);
		}else{
			return "error";
		}
		//储存监控画面
		result = monitorService.savePage(newPainter);
		//储存查看权限
		MonitorPainterUser monitorPainter_User = new MonitorPainterUser();
		monitorPainter_User.setMonitorPainterId(result.getId());
		monitorPainter_User.setUserId(result.getUserId());
		monitorPainterUserService.saveMonitorPainterUser(monitorPainter_User);
		return "success";
	}
	
	/**
	 * 监控画面储存文件导出
	 * 格式  XXX.mptu
	 * @param id
	 * @return
	 */
	@RequestMapping("/painterFileDownload")
	@ResponseBody
	public String painterFileDownload(Long id){
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		if(id==null){
			return "error";
		}
		//创建本地文件
		String newFileName = UUID.randomUUID().toString()+".mptu";
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE/";
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new File(filePath + File.separator);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		//向本地文件中写入数据
		Long fileLength = null;
		try {
			File painterFile = new File(filePath + File.separator + newFileName);
			if(!painterFile.exists()){
				painterFile.createNewFile();
			}
			MonitorPainter originalPainter = monitorPainterService.findById(id);
			//监控画面对象转化为json字符串
			String painterStr = mapper.writeValueAsString(originalPainter);
			PrintWriter pw = new PrintWriter(new FileWriter(painterFile));
			Integer length = painterStr.length();
			fileLength = length.longValue();
			pw.println(painterStr);
			pw.close();
		} catch (IOException e) {
			return "error";
		}
		Companyfile cpfile = new Companyfile();
		cpfile.setfilebasename("监控画面导出_"+id);
		cpfile.setfilelength(fileLength);
		cpfile.setfilenewname(newFileName);
		cpfile.setfilepath(filePath + File.separator + newFileName);
		companyfileService.saveOrUpdate(cpfile);
		//回传文件id，下载文件
		return cpfile.getId()+""; 
	}
	
	/**
	 * 获取监控列表数据，刷新select框
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getMonitorPainterList")
	@ResponseBody
	public String getMonitorPainterList() throws JsonProcessingException{
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null){
			return null;
		}
		List<MonitorPainterUser> list = monitorPainterUserDao.findByUserId(SecurityUtils.getLoginUser().getId());
		List<Long> ids = new ArrayList<>();
		for(MonitorPainterUser monitorPainterUser : list){
			ids.add(monitorPainterUser.getMonitorPainterId());
		}
		List<MonitorPainter> monitorPainters = new ArrayList<MonitorPainter>();
		if(ids.size() != 0){
			monitorPainters = monitorPainterService.findByIdIn(ids,companyId);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writeValueAsString(monitorPainters);
	}

	/**
	 * 根据测点ID获取工序属性
	 * @param monitorPainterId
	 * @return
	 */
//	@RequestMapping(value="/getProcedureByPointId", method=RequestMethod.POST)
	@RequestMapping("/getProcedureByPointId")
	@ResponseBody
//	public String getUserName(HttpServletRequest request){
	public String getUserName(@RequestBody String param){
//		// 终端机ID号
//		String terminalid = String.valueOf(input.get("terminalid"));
//		// 终端机sn号
//		String terminalsn = String.valueOf(input.get("terminalsn"));
//		// 终端机访问密码
//		String terminalpassword = String.valueOf(input.get("terminalpassword"));
		MesDriverPoints driverPiont = mesDriverPointService.findById(Long.valueOf("558"));
		System.out.println("----------param---------------" + param);
		net.sf.json.JSONObject resulObj = new net.sf.json.JSONObject().fromObject(param);
		net.sf.json.JSONArray measuredataArray = null;
		net.sf.json.JSONArray measuredataArrayOut = new net.sf.json.JSONArray();
		String rsFlg = "OK";
		try {
			//BeanUtils.copyProperties(resulObj, input);
			measuredataArray = resulObj.getJSONArray("measuredata");
			if(null == resulObj || !resulObj.containsKey("measuredata")) {
				throw new Exception("measuredata is null");
			}
			for(Object obj : measuredataArray) {
				net.sf.json.JSONObject objJs = net.sf.json.JSONObject.fromObject(obj);
				System.out.println("测点ID号：" + Long.valueOf(String.valueOf(objJs.get("measureid"))));
				MesDriverPoints mp = mesDriverPointService.findByMesPointsId(Long.valueOf(String.valueOf(objJs.get("measureid"))));
				if(null == mp) {
					throw new Exception("find null by measureid");
				}
				// 标准值
				objJs.put("standardvalue", mp.getStandardValue());
				// 单位
				if(null == mp.getMesDrivertypeProperty()) {
					throw new Exception("finded unit is null");
				}
				objJs.put("unit", mp.getMesDrivertypeProperty().getUnits());
				// 上公差
				objJs.put("uppertolerance", mp.getMaxValue());
				// 下公差
				objJs.put("lowertolerance", mp.getMinValue());
				measuredataArrayOut.add(objJs);
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
			rsFlg = "false";
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			rsFlg = "false";
		} catch (Exception e) {
			e.printStackTrace();
			rsFlg = "false";
		}
		// 处理状态
		resulObj.put("result", rsFlg);
		if("OK" == rsFlg) {
			// 测点情报
			resulObj.put("measureparameter", measuredataArrayOut);
			resulObj.remove("measuredata");
		}

		// 向终端机返回处理结果
		return resulObj.toString();
	}
}
