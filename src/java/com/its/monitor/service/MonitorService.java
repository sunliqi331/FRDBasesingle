package com.its.monitor.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.ItemStyle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.frd.dao.*;
import com.its.frd.entity.*;
import com.its.frd.service.*;
import com.its.frd.util.*;
import com.its.frd.util.echarts.*;
import com.its.frd.websocket.SocketSessionUtil;
import com.its.monitor.dao.ConnectionInfoDao;
import com.its.monitor.dao.ElementInfoDao;
import com.its.monitor.dao.ProductionLineDao;
import com.its.monitor.vo.*;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * the monitor design page component
 * 
 * @author Administrator
 *
 */
@Service("monitorService")
@Transactional
public class MonitorService {

    @Resource
    private MesDriverTypeService mdServ;
    @Resource
    private MesDriverDao driverDao;

    @Autowired
    private MesProductlineDao mesProductlineDao;

    @Autowired
    private ProductionLineDao productionLineDao;
    @Autowired
    private ElementInfoDao elementInfoDao;
    @Autowired
    private ConnectionInfoDao connectionInfoDao;
    @Autowired
    private MesProductDao mesProductDao;
    @Autowired
    private MesDriverPointsDao mesDriverPointsDao;
    @Autowired
    private MonitorPainterDao monitorPainterDao;
    @Autowired
    private MesDriverTypeDao mesDriverTypeDao;

    @Resource
    private MesDriverStatsDao driverStatsDao;

    @Autowired
    private MesProductionRecordDao mesProductionRecordDao;

    @Autowired
    private CompanyinfoDao companyinfoDao;

    @Autowired
    private MonitorPainterUserDao monitorPainterUserDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    protected SimpMessagingTemplate template;

    @Autowired
    private MesPointGatewayDao mesPointGatewayDao;

    @Resource(name = "redisService")
    private RedisService redisService;

    @Autowired
    private MesEnergyDao mesEnergyDao;
    @Autowired
    private MesPointsDao mesPointsDao;

    @Autowired
    private MesPointsTemplateService mesPointsTemplateService;

    @Autowired
    private MesDataProductProcedureService mesDataProductProcedureService;

    @Autowired
    private MesDataDriverStatusService mesDataDriverStatusService;

    @Autowired
    private MesDriverService mesDriverService;

    @Autowired
    private MesDriverPropertyService mesDriverPropertyService;

    @Autowired
    private MesDriverTypePropertyService mesDriverTypePropertyService;

    @Autowired
    private MesDataDriverPropertyService mesDataDriverPropertyService;

    /**
     * this method is used for monitor design page its a ajax request and show all
     * the {@link com.its.frd.entity.MesDriver} which the driverType equals the
     * given parameter
     * 
     * @param driveType
     * @return serialized Object
     * @throws JsonProcessingException
     */
    public String getDriveNameListByDriveType(Specification<MesDriver> specification, Page page)
            throws JsonProcessingException {
        // MesDrivertype mesDrivertype = mdServ.findById(driveType.getId());
        /*
         * List<MesDriver> mesDriverList = driverServ.findPage(specification, page);
         * 
         * //List<MesDriver> mesDriverList = mesDrivertype.getMesDrivers(); ObjectMapper
         * mapper = new ObjectMapper();
         * mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
         * 
         * return mapper.writeValueAsString(mesDriverList);
         */
        return "";
    }

    /**
     * 根据设备获取该设备的属性列表
     * 
     * @param driver
     * @return
     * @throws JsonProcessingException
     */
    public String getDriverPropertyListByDriver(MesDriver driver) throws JsonProcessingException {
        MesDriver mesDriver = driverDao.findOne(driver.getId());
        // List<MesDriverPoints> driverPropertyList = mesDriver.getMesDriverPointses();
        List<MesDriverPoints> driverPropertyList = mesDriverPointsDao.findAll(new Specification<MesDriverPoints>() {
            @Override
            public Predicate toPredicate(Root<MesDriverPoints> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate p1 = builder.equal(root.get("mesDriver").get("id").as(Long.class), mesDriver.getId());
                Predicate p2 = root.get("mesPoints").get("mesPointType").get("pointtypekey").as(String.class)
                        .in(MesPointType.TYPE_DRIVER_MONITOR, MesPointType.TYPE_DRIVER_STATUS);
                return query.where(p1, p2).getRestriction();
            }
        });
        if (mesDriver.getDifferencetype().equals("0") && driverPropertyList.size() == 0) {
            MesDriverPoints mesDriverPoints = new MesDriverPoints();
            MesPoints mesPoints = new MesPoints();
            mesPoints.setId(0L);
            mesPoints.setCodekey("0");
            mesPoints.setName("状态");
            mesDriverPoints.setMesPoints(mesPoints);
            mesDriverPoints.setMesDriver(mesDriver);
            mesDriverPoints.setId(0L);
            driverPropertyList.add(mesDriverPoints);
            String gateway = mesDriver.getSn();
            List<MesPoints> mesPointsList = mesPointsDao.findAll(new Specification<MesPoints>() {
                @Override
                public Predicate toPredicate(Root<MesPoints> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                    Predicate p1 = builder.equal(root.get("mesPointGateway").get("mac").as(String.class), gateway);
                    Predicate p2 = builder.equal(root.get("mesPointType").get("pointtypekey").as(String.class),
                            MesPointType.TYPE_REPEATER);
                    return query.where(p1, p2).getRestriction();
                }
            });
            for (MesPoints points : mesPointsList) {
                MesDriverPoints _mesDriverPoints = new MesDriverPoints();
                _mesDriverPoints.setMesPoints(points);
                _mesDriverPoints.setMesDriver(mesDriver);
                _mesDriverPoints.setId(0L);
                driverPropertyList.add(_mesDriverPoints);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MesDriverPoints.class, new JsonSerializer<MesDriverPoints>() {
            @Override
            public void serialize(MesDriverPoints mesDriverPoints, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                // TODO Auto-generated method stub
                jgen.writeStartObject();
                jgen.writeNumberField("id", mesDriverPoints.getId());
                jgen.writeStringField("name", mesDriverPoints.getMesPoints().getName());
                jgen.writeStringField("propertyKey", mesDriverPoints.getMesPoints().getCodekey());
                jgen.writeStringField("mesDriver", mesDriverPoints.getMesDriver().getName());
                jgen.writeNumberField("mesDriverId", mesDriverPoints.getMesDriver().getId());
                jgen.writeStringField("mesDriverSn", mesDriverPoints.getMesDriver().getSn());
                // jgen.writeStringField("monitor",mesDriverProperty.getMonitor());
                jgen.writeEndObject();
            }
        });
        mapper.registerModule(module);
        if (driverPropertyList.size() != 0) {
            return mapper.writeValueAsString(driverPropertyList);
        } else
            return "[]";
    }

    public MonitorPainter savePage(MonitorPainter productionLine) {

        return monitorPainterDao.save(productionLine);
    }

    private void writeStaticHTML(ProductionLine productionLine) throws TemplateNotFoundException,
            MalformedTemplateNameException, ParseException, IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("template.ftl");
        Template _template = configuration.getTemplate("_template.ftl");
        String path = DynamicSpecifications.getRequest().getSession().getServletContext().getRealPath("/");
        File file1 = new File(path + "\\monitorPage\\" + productionLine.getLineName() + ".html");
        File file = new File(
                "E:\\job\\workspace\\FRDBase\\WebContent\\monitorPage\\" + productionLine.getLineName() + ".html");
        File file3 = new File("E:\\job\\demo\\frd\\" + productionLine.getLineName() + ".html");
        Map<String, String> rootMap = new HashMap<String, String>();
        rootMap.put("domContent", productionLine.getDomContent());
        Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        Writer out1 = new OutputStreamWriter(new FileOutputStream(file1), "UTF-8");
        Writer out3 = new OutputStreamWriter(new FileOutputStream(file3), "UTF-8");
        template.process(rootMap, out);
        template.process(rootMap, out1);
        _template.process(rootMap, out3);
        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(out1);
        IOUtils.closeQuietly(out3);
    }

    public ProductionLine getProductionLine(long id) {
        return productionLineDao.findOne(id);

    }

    public List<ProductionLine> findAllProductionLine() {
        // TODO Auto-generated method stub
        return productionLineDao.findAll();
    }

    public String driverMonitor(String key) {
        return redisService.get(key);
    }

    /**
     * test method
     * 
     * @return
     */
    public String readJSONFile(String id) {
        try {
            String json = FileUtils.readFileToString(new File("E:\\temp\\temp" + id + ".json"));
            return json;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public void getMonitorInfo(MonitorPage page, Message<Object> socketMessage) throws InterruptedException {
        // JSONObject jsonObject = new JSONObject(ids);
        // Map<String,DriverMonitor> previousDriverMonitor = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        try {
            StringBuffer points = new StringBuffer();
            List<DriverMonitor> driverMonitors = page.getDriverMonitors();
            if (null != driverMonitors && 0 < driverMonitors.size()) {
                for (DriverMonitor driverMonitor : driverMonitors) {
                    List<DriverCheckPoint> driverCheckPointList = driverMonitor.getDriverCheckPoints();
                    if (null != driverCheckPointList && 0 < driverCheckPointList.size()) {
                        for (DriverCheckPoint driverCheckPoint : driverCheckPointList) {
                            if (StringUtils.isNotBlank(driverCheckPoint.getMesPointKey())) {
                                points.append(driverCheckPoint.getMesPointKey());
                                points.append(",");
                            }
                        }
                    }
                }
                page.setDriverMonitorsStr(points.toString());
            }

            points = new StringBuffer();
            List<ProductMonitor> productMonitors = page.getProductMonitors();
            if (null != productMonitors && 0 < productMonitors.size()) {
                for (ProductMonitor productMonitor : productMonitors) {
                    List<ProductCheckPoint> productCheckPoints = productMonitor.getProductCheckPoints();
                    if (null != productCheckPoints && 0 < productCheckPoints.size()) {
                        for (ProductCheckPoint productCheckPoint : productCheckPoints) {
                            if (StringUtils.isNotBlank(productCheckPoint.getMesPointKey())) {
                                points.append(productCheckPoint.getMesPointKey());
                                points.append(",");
                            }
                        }
                    }
                }
                page.setDriverMonitorsStr(points.toString());
            }
//            String pointArrayValue = points.toString();
//            String changeValue = "";
//            if (StringUtils.isNotBlank(pointArrayValue)) {
//                if (0 < pointArrayValue.lastIndexOf(",")) {
//                    changeValue = pointArrayValue.substring(0, pointArrayValue.lastIndexOf(","));
//                }
//            }
//             page.setProductMonitorsStr(StringUtils.isNotBlank(changeValue) ? changeValue : points.toString());

            // template.convertAndSend( "/queue/showMonitor/advise/protocal",
            // mapper.writeValueAsString(page));
            // template.convertAndSend("/queue/showMonitor/advise/protocal",
            // mapper.writeValueAsString(page));
            // template.convertAndSend("/queues/showMonitor/advise/protocal",
            // mapper.writeValueAsString(page));
            System.out.println("---------------------/showMonitor/advise/protocal-----------------------");
            System.out.println("---------------------/showMonitor/advise/protocal-----------------------");
            System.out.println("---------------------/showMonitor/advise/protocal-----------------------");
            System.out.println("---------------------/showMonitor/advise/protocal-----------------------");
            System.out.println("---------------------/showMonitor/advise/protocal-----------------------");
            
            
            System.out.println("---------------------mapper.writeValueAsString(page)-----------------------" + mapper.writeValueAsString(page));
            System.out.println("---------------------mapper.writeValueAsString(page)-----------------------" + mapper.writeValueAsString(page));
            System.out.println("---------------------mapper.writeValueAsString(page)-----------------------" + mapper.writeValueAsString(page));
            System.out.println("---------------------mapper.writeValueAsString(page)-----------------------" + mapper.writeValueAsString(page));
            template.convertAndSend("/showMonitor/advise/protocal", mapper.writeValueAsString(page));
        } catch (MessagingException | JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
         * while(true){ List<DriverMonitor> driverMonitors = new ArrayList<>();
         * if(!SocketSessionUtil.hasConnection(socketMessage.getHeaders().get(
         * "simpSessionId").toString())){ break; } for(DriverMonitor driverMonitor :
         * page.getDriverMonitors()){ List<String> fields = new ArrayList<>();
         * for(DriverCheckPoint driverCheckPoint :
         * driverMonitor.getDriverCheckPoints()){
         * fields.add(driverCheckPoint.getMesPointKey()); } List<Object> values =
         * redisService.getHash(driverMonitor.getMesDriverId(), fields); } for(String
         * driverId : jsonObject.keySet()){ JSONArray jsonArray = new
         * JSONArray(jsonObject.get(driverId).toString()); List<String> fields =
         * (List)jsonArray.toList(); List<Object> values =
         * redisService.getHash(driverId, fields); for(Object object : values){ if(null
         * == object){ continue; } DriverMonitor driverMonitor =
         * generateDriverMonitor(object); driverMonitor.setMesDriverId(driverId); String
         * key = driverId+"-"+driverMonitor.getMesPointKey(); if(null ==
         * previousDriverMonitor.get(key) ||
         * !previousDriverMonitor.get(key).equals(driverMonitor)){
         * driverMonitors.add(driverMonitor);
         * previousDriverMonitor.put(key,driverMonitor); } } }
         * 
         * Thread.sleep(1000); }
         */

    }

    private DriverMonitor generateDriverMonitor(Object object) {
        DriverMonitor driverMonitor = new DriverMonitor();
        JSONObject propertyValues = new JSONObject(object.toString());
        Class<DriverMonitor> clz = DriverMonitor.class;
        for (String key : propertyValues.keySet()) {
            String name = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
            try {
                Method method = clz.getDeclaredMethod(name, String.class);
                method.invoke(driverMonitor, propertyValues.get(key).toString());
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return driverMonitor;
    }

    public MonitorPainter getMonitorPainter(MonitorPainter monitorPainter) {
        // TODO Auto-generated method stub

        return monitorPainterDao.findOne(monitorPainter.getId());

    }

    public List<MesPointsTemplate> getTemplate() {
        List<MesPointsTemplate> list = new ArrayList<MesPointsTemplate>();
        List<MesPoints> mesPointsList = mesPointsDao.findAll();
        for (MesPoints mesPoints : mesPointsList) {
            MesPointType mesPointType = mesPoints.getMesPointType();
            MesPointGateway mesPointGateway = mesPoints.getMesPointGateway();
            MesDriverPoints mesDriverPoints = mesPoints.getMesDriverPointses().size() != 0
                    ? mesPoints.getMesDriverPointses().get(0)
                    : new MesDriverPoints();
            MesDriver mesDriver = mesDriverPoints.getMesDriver();
            // MesProcedureProperty mesProcedureProperty =
            // mesPoints.getMesProcedureProperties().size() != 0 ?
            // mesPoints.getMesProcedureProperties().get(0) : new MesProcedureProperty();

            MesPointsTemplate mesPointsTemplate = new MesPointsTemplate();
            mesPointsTemplate.setMesPointKey(null != mesPoints ? mesPoints.getCodekey() : "");
            mesPointsTemplate.setMesDriverProcedureId(mesPoints.getMesProcedureProperties().size() != 0
                    ? mesPoints.getMesProcedureProperties().get(0).getMesProductProcedure().getId()
                    : 0);
            mesPointsTemplate.setProductId(mesPoints.getMesProcedureProperties().size() != 0
                    ? mesPoints.getMesProcedureProperties().get(0).getMesProductProcedure().getMesProduct().getId()
                    : 0);
            mesPointsTemplate.setMesPointGateway(null != mesPointGateway ? mesPointGateway.getMac() : "");
            mesPointsTemplate.setMesPointTypeKey(null != mesPointType ? mesPointType.getPointtypekey() : "");
            mesPointsTemplate.setMesDriverPointsName(null != mesPointType ? mesPointType.getName() : "");
            // mesPointsTemplate.setMesDriverId(null != mesDriver ? mesDriver.getSn() : "");
            mesPointsTemplate.setMaxValue(null != mesDriverPoints.getMaxValue() ? mesDriverPoints.getMaxValue() : "");
            mesPointsTemplate.setMinValue(null != mesDriverPoints.getMinValue() ? mesDriverPoints.getMinValue() : "");
            mesPointsTemplate.setStandardValue(
                    null != mesDriverPoints.getStandardValue() ? mesDriverPoints.getStandardValue() : "");
            list.add(mesPointsTemplate);
        }
        /*
         * List<MesDriverPoints> mesDriverPointsList = mesDriverPointsDao.findAll();
         * for(MesDriverPoints mesDriverPoints : mesDriverPointsList){ MesPoints
         * mesPoints = mesDriverPoints.getMesPoints(); MesPointType mesPointType =
         * mesPoints.getMesPointType(); MesPointGateway mesPointGateway =
         * mesPoints.getMesPointGateway(); MesDriver mesDriver =
         * mesDriverPoints.getMesDriver(); MesPointsTemplate mesPointsTemplate = new
         * MesPointsTemplate(); mesPointsTemplate.setMaxValue(null !=
         * mesDriverPoints.getMaxValue() ? mesDriverPoints.getMaxValue() : 0d);
         * mesPointsTemplate.setMinValue(null != mesDriverPoints.getMinValue() ?
         * mesDriverPoints.getMinValue() : 0d);
         * mesPointsTemplate.setMesPointTypeKey(null != mesPointType ?
         * mesPointType.getPointtypekey() : "");
         * mesPointsTemplate.setMesPointGateway(null != mesPointGateway ?
         * mesPointGateway.getMac() : ""); mesPointsTemplate.setStandardValue(null !=
         * mesDriverPoints.getStandardValue() ? mesDriverPoints.getStandardValue() :
         * 0d); mesPointsTemplate.setMesPointKey(null != mesPoints ?
         * mesPoints.getCodekey() : ""); mesPointsTemplate.setMesDriverId(null !=
         * mesDriver ? mesDriver.getSn() : "");
         * mesPointsTemplate.setMesDriverPointsName(null != mesPointType ?
         * mesPointType.getName() : "");
         * mesPointsTemplate.setMesDriverProcedureId(mesPoints.getMesProcedureProperties
         * ().size() != 0?mesPoints.getMesProcedureProperties().get(0).getId():0);
         * list.add(mesPointsTemplate); }
         */
        return list;
    }
    /*
     * private List<Long> getDriverIdsByChartsCondition(ChartsBaseMonitor
     * monitor,long id){ List<Long> ids = new ArrayList<>(); List<MesDriver>
     * listDriver = driverDao.findAll(new Specification<MesDriver>(){
     * 
     * @Override public Predicate toPredicate(Root<MesDriver> root, CriteriaQuery<?>
     * query, CriteriaBuilder builder) { Predicate p1 = null;
     * if(monitor.getScope().equals("factory") ||
     * monitor.getScope().equals("company")){ p1 =
     * builder.equal(root.join("mesProductline").join("companyinfo").get("id").as(
     * Long.class),id); }else if(monitor.getScope().equals("productLine")){ p1 =
     * builder.equal(root.join("mesProductline").get("id").as(Long.class),id); }
     * Predicate p2 = null; Expression<String> expression =
     * root.joinList("mesDriverPointses",JoinType.INNER).join("mesPoints").join(
     * "mesPointType").get("pointtypekey").as(String.class);
     * if(monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT)){
     * p2 = builder.equal(expression,MesPointType.TYPE_PRODUCT_COUNT); }else
     * if(monitor.getCategory().equals(ChartsBaseMonitor.
     * CATEGORY_PRODUCT_STATUS_PASS_FAILURE)){ p2 =
     * builder.equal(expression,MesPointType.TYPE_PRODUCT_COUNT); }else
     * if(monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_WATER)){ p2 =
     * builder.equal(expression,MesPointType.TYPE_WATER); }else
     * if(monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC)){ p2 =
     * builder.equal(expression,MesPointType.TYPE_ELECTRIC); }else if
     * (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER)) {
     * p2 =
     * builder.or(builder.equal(expression,MesPointType.TYPE_WATER),builder.equal(
     * expression,MesPointType.TYPE_ELECTRIC)); } query.where(p1,p2); return
     * query.getRestriction(); }}); for(MesDriver driver : listDriver){
     * ids.add(driver.getId()); } return ids; }
     */

    // updatedBy:xsq 7.17 增加显示对应子工厂
    /**
     * 根据监控控件的条件，获取Mes监控点KeyCode
     * 
     * @param monitor
     *            监控内容
     * @param id
     * @return Mes监控点KeyCode
     * 
     */
    private Map<String, List<MesDriverPoints>> getMesPointsKeyCodeByChartsCondition(ChartsBaseMonitor monitor,
            long id) {
        List<String> ids = new ArrayList<>();
        Map<String, List<MesDriverPoints>> map = new ConcurrentHashMap<>();
        // 主表【mes_driver_points】 设备关联测点
        // inner join 【mes_driver】 设备
        // inner join 【mes_productline】 产线
        // inner join 【companyinfo】 公司情报
        // inner join 【mes_points】 测点
        // inner join 【mes_point_type】 测点类型
        System.out.println("---------------mesDriverPointsDao.findAll---------start-----------");
        System.out.println("---------------mesDriverPointsDao.findAll---------start-----------");
        System.out.println("---------------mesDriverPointsDao.findAll---------start-----------");
        System.out.println("---------------mesDriverPointsDao.findAll---------start-----------");
        System.out.println("---------------id-----------" + id);
        System.out.println("---------------id-----------" + id);
        System.out.println("---------------id-----------" + id);
        // 根据以上各表获取设备上的测点信息
        List<MesDriverPoints> listMesDriverPoints = mesDriverPointsDao.findAll(new Specification<MesDriverPoints>() {
            @Override
            public Predicate toPredicate(Root<MesDriverPoints> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate p1 = null;
                if (monitor.getScope().equals("factory") || monitor.getScope().equals("company")) {
                    p1 = builder.equal(
                            root.join("mesDriver").join("mesProductline").join("companyinfo").get("id").as(Long.class),
                            id);
                } else if (monitor.getScope().equals("productLine")) {
                    p1 = builder.equal(root.join("mesDriver").join("mesProductline").get("id").as(Long.class), id);
                } else if (monitor.getScope().equals("driver")) {
                    p1 = builder.equal(root.join("mesDriver").get("id").as(Long.class), id);
                }
                Predicate p2 = null;
                Expression<String> expression = root.join("mesPoints").join("mesPointType").get("pointtypekey")
                        .as(String.class);
                if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT)
                        ||
                        monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_DRIVER_STATUS_ANALYSIS)) {
                    // p2 = builder.equal(expression,MesPointType.TYPE_PRODUCT_COUNT);
                    // 监控页面统计的产量统计的是设备产量
                    p2 = builder.equal(expression, MesPointType.TYPE_DRIVER_COUNT);
                } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_FAILURE)) {
                    p2 = builder.equal(expression, MesPointType.TYPE_PRODUCT_STATUS);
                } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_WATER)) {
                    p2 = builder.equal(expression, MesPointType.TYPE_WATER);
                } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC)) {
                    p2 = builder.equal(expression, MesPointType.TYPE_ELECTRIC);
                } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER)) {
                    p2 = builder.or(builder.equal(expression, MesPointType.TYPE_WATER),
                            builder.equal(expression, MesPointType.TYPE_ELECTRIC));
                } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_GAS)) {
                    p2 = builder.or(builder.equal(expression, MesPointType.TYPE_GAS),
                            builder.equal(expression, MesPointType.TYPE_GAS));
                }
                query.where(p1, p2);
                return query.getRestriction();
            }
        });
        System.out.println("---------------mesDriverPointsDao.findAll---------end-----------");
        System.out.println("---------------mesDriverPointsDao.findAll---------end-----------");
        System.out.println("---------------mesDriverPointsDao.findAll---------end-----------");
        System.out.println("---------------mesDriverPointsDao.findAll---------end-----------");
        System.out.println("---------------listMesDriverPoints------------------" + listMesDriverPoints);
        System.out.println("---------------listMesDriverPoints------------------" + listMesDriverPoints);
        System.out.println("---------------listMesDriverPoints------------------" + listMesDriverPoints);
        System.out.println("---------------listMesDriverPoints------------------" + listMesDriverPoints);
        // 设置返回值Map<网关MAC, 设备测点信息list>
        for (MesDriverPoints driver : listMesDriverPoints) {
            String mac = driver.getMesPoints().getMesPointGateway().getMac();
            List<MesDriverPoints> list = map.get(mac);
            if (null == list) {
                list = new ArrayList<>();
                map.put(mac, list);
            }
            list.add(driver);
            // map.put(, driver);
            // ids.add(driver.getMesPoints().getCodekey());
        }
        return map;
    }

    /**
     * 根据监控内容，获取各种统计信息
     * 
     * @param monitor
     *            监控内容
     * @param id
     * @return 各种统计信息
     */
    private Map<String, String> getEnergyRecord(ChartsBaseMonitor monitor, String id) {
        // 根据根据监控控件内容，获取网关对应的设备/测点信息
        Map<String, List<MesDriverPoints>> map = this.getMesPointsKeyCodeByChartsCondition(monitor, Long.parseLong(id));
        Map<String, String> _mMap = new HashMap<>();
        if (map.size() == 0) {
            switch (monitor.getCategory()) {
            case ChartsBaseMonitor.CATEGORY_ELECTRIC:
                _mMap.put(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT, "0");
                break;
            case ChartsBaseMonitor.CATEGORY_WATER:
                _mMap.put(ChartsBaseMonitor.CATEGORY_WATER_TEXT, "0");
                break;
            case ChartsBaseMonitor.CATEGORY_GAS:
                _mMap.put(ChartsBaseMonitor.CATEGORY_GAS_TEXT, "0");
                break;
            case ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER:
                _mMap.put(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT, "0");
                _mMap.put(ChartsBaseMonitor.CATEGORY_WATER_TEXT, "0");
                _mMap.put(ChartsBaseMonitor.CATEGORY_GAS_TEXT, "0");
                break;
            case ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT:
                _mMap.put("产量", "0");
                break;
            case ChartsBaseMonitor.CATEGORY_DRIVER_STATUS_ANALYSIS:
                _mMap.put("设备运行时间", "0");
                break;
            default:
                break;
            }
        }

        for (String key : map.keySet()) {
            List<MesDriverPoints> mesDriverPointList = map.get(key);
            for (MesDriverPoints mesDriverPoints : mesDriverPointList) {
                String hashKey = key + ":" + mesDriverPoints.getMesPoints().getCodekey();
                if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC)
                        || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_WATER)
                        || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_GAS)
                        || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER)
                        || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT)
                        || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_DRIVER_STATUS_ANALYSIS)
                                ) {
                    System.out.println("-------------hashKey-------------" + hashKey);
                    System.out.println("-------------hashKey-------------" + hashKey);
                    System.out.println("-------------hashKey-------------" + hashKey);
                    String tableNmStr = "";
                    if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC)) {
                        tableNmStr = "realElectricList";

                    } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_WATER)) {
                        tableNmStr = "realWaterList";

                    } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_GAS)) {
                        tableNmStr = "realGasList";

                    } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT) && ("driver".equals(monitor.getScope()))) {
                        tableNmStr = "driveCount";

                    }else {
                        tableNmStr = "realElectricList";
                    }
                    Object obj = redisService.getHash(tableNmStr,
                            String.valueOf(mesDriverPoints.getMesDriver().getId()));
                    // Object obj = redisService.getHash(hashKey,
                    // mesDriverPoints.getMesPoints().getMesPointType().getPointtypekey());
                    System.out.println("--------------tableNmStr-----------------" + tableNmStr);
                    System.out.println("--------------tableNmStr-----------------" + tableNmStr);
                    System.out.println("--------------tableNmStr-----------------" + tableNmStr);
                    System.out.println("--------------driverId-----------------" + String.valueOf(mesDriverPoints.getMesDriver().getId()));
                    System.out.println("--------------driverId-----------------" + String.valueOf(mesDriverPoints.getMesDriver().getId()));
                    System.out.println("--------------driverId-----------------" + String.valueOf(mesDriverPoints.getMesDriver().getId()));
                    System.out.println("-------------------------------");
                    System.out.println("-------------------------------");
                    System.out.println("-------------------------------");

                    System.out.println("----------monitor.getCategory()---------" + monitor.getCategory());
                    System.out.println("----------monitor.getCategory()---------" + monitor.getCategory());
                    System.out.println("----------monitor.getCategory()---------" + monitor.getCategory());
                    System.out.println("----------monitor.getCategory()---------" + monitor.getCategory());
                    System.out.println("----------monitor.getCategory()---------" + monitor.getCategory());
                    // if(null != obj &&
                    // !monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT)){
                    if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC)) {
                        // BigDecimal electricValue = new BigDecimal(null !=
                        // _mMap.get(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT)
                        // ? _mMap.get(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT)
                        // : "0").add(new BigDecimal(obj.toString()));
                        // _mMap.put(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT,decimalFormat.format(electricValue));
                        _mMap.put(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT,
                                getAddRes(obj != null ?obj.toString() : "0", ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT, _mMap));
                    }
                    if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_WATER)) {
                        // BigDecimal waterValue = new BigDecimal(null !=
                        // _mMap.get(ChartsBaseMonitor.CATEGORY_WATER_TEXT)
                        // ? _mMap.get(ChartsBaseMonitor.CATEGORY_WATER_TEXT)
                        // : "0").add(new BigDecimal(obj.toString()));
                        // _mMap.put(ChartsBaseMonitor.CATEGORY_WATER_TEXT,decimalFormat.format(waterValue));
                        _mMap.put(ChartsBaseMonitor.CATEGORY_WATER_TEXT,
                                getAddRes(obj != null ?obj.toString() : "0", ChartsBaseMonitor.CATEGORY_WATER_TEXT, _mMap));
                    }
                    if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_GAS)) {
                        // _mMap.put(ChartsBaseMonitor.CATEGORY_GAS_TEXT,
                        // new BigDecimal(null != _mMap.get(ChartsBaseMonitor.CATEGORY_GAS_TEXT)
                        // ? _mMap.get(ChartsBaseMonitor.CATEGORY_GAS_TEXT)
                        // : 0D).add(new BigDecimal(obj.toString())).doubleValue());
                        _mMap.put(ChartsBaseMonitor.CATEGORY_GAS_TEXT,
                                getAddRes(obj != null ?obj.toString() : "0", ChartsBaseMonitor.CATEGORY_GAS_TEXT, _mMap));
                    }
                    if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER)) {
                        _mMap.put(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT,
                                getAddRes(obj != null ?obj.toString() : "0", ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT, _mMap));
                        _mMap.put(ChartsBaseMonitor.CATEGORY_WATER_TEXT,
                                getAddRes(obj != null ?obj.toString() : "0", ChartsBaseMonitor.CATEGORY_WATER_TEXT, _mMap));
                        _mMap.put(ChartsBaseMonitor.CATEGORY_GAS_TEXT,
                                getAddRes(obj != null ?obj.toString() : "0", ChartsBaseMonitor.CATEGORY_GAS_TEXT, _mMap));
                    }
                    // 图标类设备产量监控
                    if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT) && ("driver".equals(monitor.getScope()))) {
                        _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT,
                                getAddRes(obj != null ?obj.toString() : "0", ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT, _mMap));
                    }
                    // }
                    if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT) && (!"driver".equals(monitor.getScope()))) {
                        // _mMap.put("产量", new BigDecimal(null != _mMap.get("产量") ? _mMap.get("产量") :
                        // 0D).add(new BigDecimal(obj.toString())).doubleValue());
                        // _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT, new BigDecimal(null
                        // != _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT) ?
                        // _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT) : 0D).add(new
                        // BigDecimal(obj.toString())).doubleValue());
                        MesDriver mesDriver = mesDriverPoints.getMesPoints().getCurrentMesDriver();
                        MesProductline mesProductline = mesDriver.getMesProductline();
                        Long factoryId = mesProductline.getCompanyinfo().getId();
                        Long productLineId = mesProductline.getId();
                        Long driverId = mesDriver.getId();
                        MesProduct currentMesProduct = mesDriverPoints.getMesPoints().getCurrentMesProduct();
                        Long productId = currentMesProduct.getId();
                        MesProductProcedure currentMesProductProcedure = mesDriverPoints.getMesPoints()
                                .getCurrentMesProductProcedure();
                        Long procedureId = currentMesProductProcedure.getId();

                        Double count = 0D;
                        Calendar calendarToday = Calendar.getInstance();
                        calendarToday.setTime(new Date());
                        calendarToday.set(Calendar.HOUR_OF_DAY, 0);
                        calendarToday.set(Calendar.MINUTE, 0);
                        calendarToday.set(Calendar.SECOND, 0);

                        Calendar calendarTodayEnd = Calendar.getInstance();
                        calendarTodayEnd.setTime(new Date());
                        calendarTodayEnd.set(Calendar.HOUR_OF_DAY, 23);
                        calendarTodayEnd.set(Calendar.MINUTE, 59);
                        calendarTodayEnd.set(Calendar.SECOND, 59);
                        if (monitor.getBegin() != null) {
                            Calendar calendarSearch = Calendar.getInstance();
                            calendarSearch.setTime(monitor.getBegin());

                            if (calendarSearch.after(calendarToday)) {
                                // Double _count = 0D;
                                // List<MesDriverStats> statsList =
                                // driverStatsDao.findByMesDriverIdAndUpdatetimeBetween(mesDriver.getId(), new
                                // Timestamp(calendarToday.getTimeInMillis()), new
                                // Timestamp(calendarSearch.getTimeInMillis()));
                                // for(MesDriverStats ds : statsList){
                                // _count += ds.getCount();
                                // }
                                // count = DecimalCalculate.sub(Double.valueOf(null != obj ? obj.toString() :
                                // "0"), _count);
                                
//                                count = new HbaseUtil().getDataCountByHbase(factoryId, productLineId, driverId,
//                                        productId, procedureId, new Timestamp(calendarToday.getTimeInMillis()),
//                                        new Timestamp(calendarSearch.getTimeInMillis()));
                                
                                List<SearchFilter> searchList = Lists.newArrayList();

                                searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, factoryId));
                                searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, productLineId));
                                searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, driverId));

                                searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, calendarToday.getTimeInMillis()));
                                searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, calendarSearch.getTimeInMillis()));
                                
                                searchList.add(new SearchFilter("productMode", Operator.EQ, productId));
                                searchList.add(new SearchFilter("productProcedureId", Operator.EQ, procedureId));

                                Specification<MesDataProductProcedure> specification = DynamicSpecifications
                                        .bySearchFilter(MesDataProductProcedure.class, searchList);

                                List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findAll(specification);
                                count = Double.valueOf(mesDataProductProcedureList.size());

                            } else {
                                // Double _count = 0D;
                                // List<MesDriverStats> statsList =
                                // driverStatsDao.findByMesDriverIdAndUpdatetimeBetween(mesDriver.getId(), new
                                // Timestamp(calendarSearch.getTimeInMillis()),new
                                // Timestamp(calendarToday.getTimeInMillis()));
                                // for(MesDriverStats ds : statsList){
                                // _count += ds.getCount();
                                // }
                                // count = DecimalCalculate.add(Double.valueOf(null != obj ? obj.toString() :
                                // "0"), _count);
//                                count = new HbaseUtil().getDataCountByHbase(factoryId, productLineId, driverId,
//                                        productId, procedureId, new Timestamp(calendarSearch.getTimeInMillis()),
//                                        new Timestamp(calendarToday.getTimeInMillis()));
                                
                                List<SearchFilter> searchList = Lists.newArrayList();

                                searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, factoryId));
                                searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, productLineId));
                                searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, driverId));

                                searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, calendarSearch.getTimeInMillis()));
                                searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, calendarToday.getTimeInMillis()));
                                
                                searchList.add(new SearchFilter("productMode", Operator.EQ, productId));
                                searchList.add(new SearchFilter("productProcedureId", Operator.EQ, procedureId));

                                Specification<MesDataProductProcedure> specification = DynamicSpecifications
                                        .bySearchFilter(MesDataProductProcedure.class, searchList);

                                List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findAll(specification);
                                count = Double.valueOf(mesDataProductProcedureList.size());
                            }

                        } else {
                            // count = (null == obj ? 0D : new BigDecimal(obj.toString()).doubleValue());
//                            count = new HbaseUtil().getDataCountByHbase(factoryId, productLineId, driverId, productId,
//                                    procedureId, new Timestamp(calendarToday.getTimeInMillis()),
//                                    new Timestamp(calendarTodayEnd.getTimeInMillis()));
                            
                            List<SearchFilter> searchList = Lists.newArrayList();

                            searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, factoryId));
                            searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, productLineId));
                            searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, driverId));

                            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, calendarToday.getTimeInMillis()));
                            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, calendarTodayEnd.getTimeInMillis()));
                            
                            searchList.add(new SearchFilter("productMode", Operator.EQ, productId));
                            searchList.add(new SearchFilter("productProcedureId", Operator.EQ, procedureId));

                            Specification<MesDataProductProcedure> specification = DynamicSpecifications
                                    .bySearchFilter(MesDataProductProcedure.class, searchList);

                            List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findAll(specification);
                            count = Double.valueOf(mesDataProductProcedureList.size());
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("0");
                        _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT, getAddRes(decimalFormat.format(count),
                                ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT, _mMap));
                        // _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT, new BigDecimal(null
                        // != _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT) ?
                        // _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT) : 0D).add(new
                        // BigDecimal(count)).doubleValue());
                    }

                } else if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_FAILURE)) {
                    // 统计图产品合格/不合格显示
                    Object obj1 = redisService.getHash(hashKey, "P_STATUS_GOOD");
                    String obj1Str = "";
                    if (null == obj1) {
                        obj1Str = "0";
                    } else {
                        obj1Str = obj1.toString();
                    }
                    // _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_TEXT,
                    // new BigDecimal(null !=
                    // _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_TEXT) ?
                    // _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_TEXT) : 0D).add(new
                    // BigDecimal(obj1Str)).doubleValue());

                    _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_TEXT,
                            getAddRes(obj1Str, ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_TEXT, _mMap));
                    Object obj2 = redisService.getHash(hashKey, "P_STATUS_BAD");
                    String obj2Str = "";
                    if (null == obj2) {
                        obj2Str = "0";
                    } else {
                        obj2Str = obj2.toString();
                    }
                    // _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_FAIL_TEXT,
                    // new BigDecimal(null !=
                    // _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_FAIL_TEXT) ?
                    // _mMap.get(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_FAIL_TEXT) : 0D).add(new
                    // BigDecimal(obj2Str)).doubleValue());

                    _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_FAIL_TEXT,
                            getAddRes(obj2Str, ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_FAIL_TEXT, _mMap));
                }
            }

            /*
             * 
             * 
             * MesEnergy mesEnergy = new MesEnergy();
             * mesEnergy.setEnergytype(mesDriverPoints.getMesPoints().getMesPointType().
             * getPointtypekey()); mesEnergy.setValue(null != obj ?
             * Double.parseDouble(obj.toString()) : 0); mesEnergys.add(mesEnergy);
             */
        }
        return _mMap;
    }

    /**
     * 获取生产记录
     * 
     * @param monitor
     * @param id
     * @return
     */
    private List<ProductionRecord> getProductionRecord(ChartsBaseMonitor monitor, String id) {
        Map<String, List<MesDriverPoints>> map = this.getMesPointsKeyCodeByChartsCondition(monitor, Long.parseLong(id));
        List<ProductionRecord> productionRecords = new ArrayList<>();
        for (String key : map.keySet()) {
            List<MesDriverPoints> mesDriverPointList = map.get(key);
            for (MesDriverPoints mesDriverPoints : mesDriverPointList) {
                String hashKey = key + ":" + mesDriverPoints.getMesPoints().getCodekey();
                Object count = redisService.getHash(hashKey, "count");
                Object standard = redisService.getHash(hashKey, "standard");
                Object belowstandard = redisService.getHash(hashKey, "belowstandard");
                ProductionRecord productionRecord = new ProductionRecord();
                productionRecord.setFailureCount(null != belowstandard ? Long.parseLong(belowstandard.toString()) : 0);
                productionRecord.setPassCount(null != standard ? Long.parseLong(standard.toString()) : 0);
                productionRecord.setTotalCount(null != count ? Long.parseLong(count.toString()) : 0);
                productionRecord.setUpdatetime(new Timestamp(new Date().getTime()));
                productionRecords.add(productionRecord);
            }
        }
        return productionRecords;
        /*
         * 
         * 
         * List<Long> ids = getDriverIdsByChartsCondition(monitor,Long.parseLong(id));
         * List<ProductionRecord> list = new ArrayList<>(); if (ids.size() != 0) { list
         * = mesProductionRecordDao.findAll(new Specification<ProductionRecord>() {
         * 
         * @Override public Predicate toPredicate(Root<ProductionRecord> root,
         * CriteriaQuery<?> query, CriteriaBuilder builder) { Calendar calendarBegin =
         * Calendar.getInstance(); calendarBegin.setTime(monitor.getBegin());
         * calendarBegin.set(Calendar.HOUR_OF_DAY, 0);
         * calendarBegin.set(Calendar.MINUTE, 0); calendarBegin.set(Calendar.SECOND, 0);
         * Calendar calendarEnd = Calendar.getInstance(); if(monitor.getEnd() != null){
         * calendarEnd.setTime(monitor.getEnd()); calendarEnd.set(Calendar.HOUR_OF_DAY,
         * 0); calendarEnd.set(Calendar.MINUTE, 0); calendarEnd.set(Calendar.SECOND, 0);
         * }else{ calendarEnd.setTime(calendarBegin.getTime());
         * calendarEnd.set(Calendar.HOUR_OF_DAY, 23); calendarEnd.set(Calendar.MINUTE,
         * 59); calendarEnd.set(Calendar.SECOND, 59); }
         * 
         * Predicate p =
         * builder.greaterThan(root.get("timestamp"),calendarBegin.getTime()); Predicate
         * p1 = builder.lessThan(root.get("timestamp"),calendarEnd.getTime()); Predicate
         * p2 = root.get("mesDriver").get("id").in(ids); return
         * query.where(p,p1,p2).getRestriction(); }
         * 
         * }); } return list;
         */
    }

    public Map<String, Map<String, Long>> getProductionRecord(ChartsBaseMonitor monitor) {
        Map<String, Map<String, Long>> map = new ConcurrentHashMap<>();
        for (Entry<String, String> entry : monitor.getMap().entrySet()) {
            Map<String, Long> _mMap = new ConcurrentHashMap<>();
            String id = entry.getKey();
            String name = entry.getValue();
            long passNum = 0l, failureNum = 0l, totalNum = 0l;
            for (ProductionRecord productionRecord : getProductionRecord(monitor, id)) {
                passNum += productionRecord.getPassCount();
                failureNum += productionRecord.getFailureCount();
                totalNum += productionRecord.getTotalCount();
            }
            if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT)) {
                _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT_TEXT, totalNum);
            }
            if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_FAILURE)) {
                _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_TEXT, passNum);
                _mMap.put(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_FAIL_TEXT, failureNum);
            }
            map.put(name, _mMap);
        }
        return map;
    }

    public Map<String, Map<String, Double>> getProductionRecordGroupByTime(ChartsBaseMonitor monitor) {
        Map<String, Map<String, Double>> map = new ConcurrentHashMap<>();
        for (Entry<String, String> entry : monitor.getMap().entrySet()) {
            String id = entry.getKey();
            String name = entry.getValue();
            double totalNum = 0l;
            for (ProductionRecord productionRecord : getProductionRecord(monitor, id)) {
                Map<String, Double> _mMap = new ConcurrentHashMap<>();
                totalNum += productionRecord.getTotalCount();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(productionRecord.getUpdatetime().getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                _mMap.put(name, totalNum);
                map.put(dateFormat.format(calendar.getTime()), _mMap);
            }
        }
        List<Map.Entry<String, Map<String, Double>>> infoIds = new ArrayList<Map.Entry<String, Map<String, Double>>>(
                map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Map<String, Double>>>() {
            public int compare(Map.Entry<String, Map<String, Double>> o1, Map.Entry<String, Map<String, Double>> o2) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(DateUtils.parse(o1.getKey(), "HH:mm:ss"));
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(DateUtils.parse(o2.getKey(), "HH:mm:ss"));
                return calendar2.before(calendar1) ? 0 : -1;
            }
        });
        return map;
    }

    /**
     * 获取产量记录
     * 
     * @param monitor
     * @return
     */
    public Map<String, Map<String, String>> getEnergyRecord(ChartsBaseMonitor monitor) {
        Map<String, Map<String, String>> map = new ConcurrentHashMap<>();
        for (Entry<String, String> entry : monitor.getMap().entrySet()) {
            // Map<String, Double> _mMap = new HashMap<>();
            String id = entry.getKey();
            String name = entry.getValue();
            // List<MesEnergy> mesEnergys = getEnergyRecord(monitor,id);
            // 获取各种统计信息
            Map<String, String> mesEnergys = getEnergyRecord(monitor, id);
            /*
             * double electricValue = 0d; double waterValue = 0d; double gasValue = 0d;
             * for(MesEnergy mesEnergy : mesEnergys){ BigDecimal bigDecimal = new
             * BigDecimal(mesEnergy.getValue()); if
             * (mesEnergy.getEnergytype().equals(MesPointType.TYPE_ELECTRIC)) {
             * electricValue = bigDecimal.add(new BigDecimal(electricValue)).doubleValue();
             * } if (mesEnergy.getEnergytype().equals(MesPointType.TYPE_WATER)) { waterValue
             * = bigDecimal.add(new BigDecimal(waterValue)).doubleValue(); }
             * if(mesEnergy.getEnergytype().equals(MesPointType.TYPE_GAS)){ gasValue =
             * bigDecimal.add(new BigDecimal(gasValue)).doubleValue(); }
             * 
             * 
             * } if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC)) {
             * _mMap.put(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT, electricValue); } if
             * (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_WATER)) {
             * _mMap.put(ChartsBaseMonitor.CATEGORY_WATER_TEXT, waterValue); } if
             * (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_GAS)) {
             * _mMap.put(ChartsBaseMonitor.CATEGORY_GAS_TEXT, gasValue); }
             * if(monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER)){
             * _mMap.put(ChartsBaseMonitor.CATEGORY_ELECTRIC_TEXT, electricValue);
             * _mMap.put(ChartsBaseMonitor.CATEGORY_WATER_TEXT, waterValue);
             * _mMap.put(ChartsBaseMonitor.CATEGORY_GAS_TEXT, gasValue); }
             */
            map.put(name, mesEnergys);
        }
        JSONObject jsonObj = new JSONObject(map);
        System.out.println("-----------------data------------------" + jsonObj.toString());
        System.out.println("-----------------data------------------" + jsonObj.toString());
        System.out.println("-----------------data------------------" + jsonObj.toString());
        System.out.println("-----------------data------------------" + jsonObj.toString());
        System.out.println("-----------------data------------------" + jsonObj.toString());
        System.out.println("-----------------data------------------" + jsonObj.toString());
        System.out.println("-----------------data------------------" + jsonObj.toString());
        return map;
    }

    /**
     * 运算处理监控页面柱状图/直方图/饼图的数据
     * 
     * @param monitor
     * @return
     * @throws Exception
     */
    public String caculate(ChartsBaseMonitor monitor) throws Exception {
        String result = "";
        String title = "";
        switch (monitor.getCategory()) {
        case ChartsBaseMonitor.CATEGORY_ELECTRIC:
            title = "耗电量";
            break;
        case ChartsBaseMonitor.CATEGORY_GAS:
            title = "耗气量";
            break;
        case ChartsBaseMonitor.CATEGORY_WATER:
            title = "耗水量";
            break;
        case ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER:
            title = "耗电/耗水/耗气";
            break;
        case ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT:
            title = "产量";
            break;
        case ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_FAILURE:
            title = "合格/不合格";
            break;
        case ChartsBaseMonitor.CATEGORY_DRIVER_STATUS_ANALYSIS:
            title = "设备状态对比图";
            break;
        default:
            break;
        }
        // 实时获取【耗电量】/【耗气量】/【耗水量】/【耗电/耗水/耗气】/【产量】/【合格/不合格】
        Map<String, Map<String, String>> map = getEnergyRecord(monitor);

        if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_COUNT)
                || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_PRODUCT_STATUS_PASS_FAILURE)
                || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_DRIVER_STATUS_ANALYSIS)) {

            if (monitor.getChartsType().equals("bar")) {
                // Map<String, Map<String,Double>> map = getEnergyRecord(monitor);
                EchartsOption<BarOption> echartsOption = new BarOption();
                result = echartsOption.title(title + "柱状图").data(map).toString();
            }
            if (monitor.getChartsType().equals("pie")) {
                // Map<String, Map<String,Double>> map = getEnergyRecord(monitor);
                EchartsOption<PieOption> echartsOption = new PieOption();
                result = echartsOption.title(title + "饼图").dataHaveRadis(map, monitor.getPieType()).toString();
            }
            if (monitor.getChartsType().equals("line")) {
                EchartsOption<LineOption> echartsOption = new LineOption();
                // Map<String, Map<String,Double>> mapRecord =
                // getProductionRecordGroupByTime(monitor);
                result = echartsOption.title(title + "折线图").data(map).toString();
            }
            if (monitor.getChartsType().equals("gauge")) {
                EchartsOption<GaugeOption> echartsOption = new GaugeOption();
                // Map<String, Map<String,Double>> mapRecord =
                // getProductionRecordGroupByTime(monitor);
                result = echartsOption.title(title + "仪表盘").data(map).toString();
            }
        }
        if (monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC)
                || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_GAS)
                || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_WATER)
                || monitor.getCategory().equals(ChartsBaseMonitor.CATEGORY_ELECTRIC_WATER)) {
            if (monitor.getChartsType().equals("bar")) {
                // Map<String,Map<String,Double>> map = getEnergyRecord(monitor);
                EchartsOption<BarOption> echartsOption = new BarOption();
                GsonOption option = echartsOption.title(title + "柱状图").data(map);
                for (Series<Line> line : option.series()) {
                    line.label().normal().show(true);
                    line.label().normal().setPosition(Position.outside);
                }
                // result = echartsOption.title(title+"柱状图").data(map).toString();
                result = option.toString();
            }
            if (monitor.getChartsType().equals("pie")) {
                // Map<String, Map<String,Double>> map = getEnergyRecord(monitor);
                EchartsOption<PieOption> echartsOption = new PieOption();
                // GsonOption option = echartsOption.title(title+"饼图").data(map);
                // for(Series<Line> line : option.series()){
                // // line.label().normal().show(true);
                // line.label().normal().setPosition(Position.inside);
                // for(Object piedata : line.data()) {
                // PieData piedataNew = (PieData)piedata;
                // piedataNew.setName(piedataNew.getValue().toString());
                // }
                // }
                //
                result = echartsOption.title(title + "饼图").data(map).toString();
                // result = option.toString();
            }
            if (monitor.getChartsType().equals("line")) {
                EchartsOption<LineOption> echartsOption = new LineOption();
                // Map<String, Map<String,Double>> mapRecord =
                // getProductionRecordGroupByTime(monitor);
                result = echartsOption.title(title + "折线图").data(map).toString();
            }

        }
        return result;
    }

    public List<ProductionRecord> getProductionRecordByCondition(ChartsBaseMonitor monitor, long id) {
        List<ProductionRecord> list = mesProductionRecordDao.findAll(new Specification<ProductionRecord>() {
            @Override
            public Predicate toPredicate(Root<ProductionRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate p = null;
                if (monitor.getScope().equals("factory")) {
                    p = builder.equal(root.join("mesDriver").get("companyinfo").get("id").as(Long.class), id);
                } else if (monitor.getScope().equals("productLine")) {
                    p = builder.equal(root.join("mesDriver").get("mesProductline").get("id").as(Long.class), id);
                }
                predicate.add(p);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Predicate p2 = builder.greaterThan(root.get("timestamp"), calendar.getTime());
                predicate.add(p2);
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }

        });
        return list;
    }

    public void getEnergyInfo(ChartsBaseMonitor monitor, Message<Object> socketMessage) throws InterruptedException {
        while (true) {
            if (!SocketSessionUtil.hasConnection(socketMessage.getHeaders().get("simpSessionId").toString())) {
                break;
            }
            // template.convertAndSend( "/showMonitor/monitorCharts", "{\""+
            // monitor.getName() +"\":"+caculate(monitor,jsonObject)+"}");
            Thread.sleep(Integer.parseInt(monitor.getTimeSequence()) * 60 * 1000);
        }
    }

    public List<MesProductline> getProductionLinesByCurrentUser(long userId) {
        return mesProductlineDao.findAll(new Specification<MesProductline>() {

            @Override
            public Predicate toPredicate(Root<MesProductline> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                // TODO Auto-generated method stub
                Predicate predicate = builder.equal(root.join("companyinfo").get("userid").as(Long.class), userId);
                query.where(predicate);
                return query.getRestriction();
            }
        });
    }

    /**
     * 获取设备类型列表
     * 
     * @param companyid
     * @return
     */
    public List<MesDrivertype> getMesDriverTypeList(final long companyid) {
        return mesDriverTypeDao.findAll(new Specification<MesDrivertype>() {
            @Override
            public Predicate toPredicate(Root<MesDrivertype> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate p = builder.equal(root.get("companyinfo").get("id").as(Long.class), companyid);
                return query.where(p).getRestriction();
            }
        });
    }

    public List<Companyinfo> findFactoryByCompany(long companyId) {
        return companyinfoDao.findAll(new Specification<Companyinfo>() {
            @Override
            public Predicate toPredicate(Root<Companyinfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                // TODO Auto-generated method stub
                Predicate p1 = builder.equal(root.get("companytype").as(String.class), "factory");
                Predicate p2 = builder.equal(root.get("parentid").as(Long.class), companyId);
                return query.where(p1, p2).getRestriction();
            }
        });
    }

    // updatedBy:xsq 7.14 增加对应要显示的子工厂

    /**
     * 获取子工厂
     * 
     * @param factoryParentId
     * @return
     */
    public List<Companyinfo> findChildFactoryByFactory(long factoryParentId) {
        return companyinfoDao.findAll(new Specification<Companyinfo>() {
            @Override
            public Predicate toPredicate(Root<Companyinfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                // TODO Auto-generated method stub
                Predicate p1 = builder.equal(root.get("companytype").as(String.class), "factory");
                Predicate p2 = builder.equal(root.get("parentid").as(Long.class), factoryParentId);
                return query.where(p1, p2).getRestriction();
            }
        });
    }

    private void generateSubFactory(Companyinfo companyinfo) {
        List<Companyinfo> list = this.companyinfoDao.findByParentId(companyinfo.getId(), Companyinfo.COPANYSTATUS_OK,
                Companyinfo.FACTORY);
        for (Companyinfo factory : list) {
            generateSubFactory(factory);
        }
    }

    public List<MesProduct> getProductionByCompanyId(final long companyId) {
        return mesProductDao.findAll(new Specification<MesProduct>() {
            @Override
            public Predicate toPredicate(Root<MesProduct> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate p = builder.equal(root.join("companyinfo", JoinType.INNER).get("id").as(Long.class),
                        companyId);
                return query.where(p).getRestriction();
            }
        });
    }

    public List<MesDrivertypeProperty> getMesDrivertypePropertyList(long driverId) {
        MesDriver mesDriver = mesDriverService.findById(Long.valueOf(driverId));
        return mesDriver.getMesDrivertype().getMesDrivertypeProperties();
    }
    
    public List<MesDriver> findMesDriverByProducLine(long lineId) {
        // TODO Auto-generated method stub
        return driverDao.findByMesProductlineid(lineId);
    }

    public String getDriverInfo(Long id) {
        MesDriver driver = driverDao.findOne(id);
        List<MesDriverPoints> list = driver.getMesDriverPointses();
        Map<String, Object> map = redisService.getHash(String.valueOf(id));
        if (null == map || map.size() == 0) {
            return "暂无数据!";
        }
        String result = "";
        for (MesDriverPoints mesDriverPoints : list) {
            String gateway = mesDriverPoints.getMesPoints().getMesPointGateway().getMac();
            String pointKey = mesDriverPoints.getMesPoints().getCodekey();
            if (!map.containsKey(gateway + ":" + pointKey)) {
                continue;
            }
            String propertyName = null != mesDriverPoints.getMesDrivertypeProperty()
                    ? mesDriverPoints.getMesDrivertypeProperty().getPropertyname()
                    : "";
            JSONObject jsonObject = new JSONObject(map.get(gateway + ":" + pointKey).toString());
            result += propertyName + ":" + jsonObject.getDouble("value");
            if (jsonObject.getInt("status") == 0) {
                result = "<span style='color:green;'>" + result + "</span>";
            } else
                result = "<span style='color:red;'>" + result + "</span>";

            result += "<br>";
        }
        return result;
    }

    /**
     * 删除监控画面
     * 
     * @param monitorPainter
     * @param id
     * @return
     */
    public int deleteMonitor(MonitorPainter monitorPainter, Long id) {
        MonitorPainter monitorPainter_ = monitorPainterDao.findOne(monitorPainter.getId());
        if (monitorPainter_.getUserId() != id) {
            return -1;// 非监控页面创建者
        }
        boolean delFlag = true;
        try {
            try {
                AmqManager.getAMQ().initJmxConnector();
                AmqManager.getAMQ().getBrokerViewMBean();
                AmqManager.getAMQ().initTopics();
            } catch (Exception e) {
                e.printStackTrace();
                monitorPainterDao.delete(monitorPainter.getId());
                monitorPainterUserDao.deleteByMonitorPainterId(monitorPainter.getId());
                return 0;
            }
            Map<String, TopicViewMBean> topicViewMBeanMap = AmqManager.getTopicViewMBeanMap();
            for (String queueName : topicViewMBeanMap.keySet()) {
                TopicViewMBean topicBean = topicViewMBeanMap.get(queueName);
                System.out.println("******************************");
                System.out.println("队列的名称：" + topicBean.getName());
                System.out.println("队列中剩余的消息数：" + topicBean.getQueueSize());
                System.out.println("消费者数：" + topicBean.getConsumerCount());
                System.out.println("出队列的数量：" + topicBean.getDequeueCount());
                if (topicBean.getName().equals("showMonitor/monitor/" + monitorPainter.getId())) {
                    delFlag = false;
                    if (topicBean.getConsumerCount() == 0) {
                        monitorPainterDao.delete(monitorPainter.getId());
                        monitorPainterUserDao.deleteByMonitorPainterId(monitorPainter.getId());
                        return 0;
                    }
                    break;
                }
            }
            if (delFlag) {
                monitorPainterDao.delete(monitorPainter.getId());
                monitorPainterUserDao.deleteByMonitorPainterId(monitorPainter.getId());
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -3;// 删除异常
        }
        return -2;// 存在正在监控的页面
    }

    /**
     * 水电气产量技术叠加
     * 
     * @param objStr
     * @param mapKey
     * @param _mMap
     * @return
     */
    private String getAddRes(String objStr, String mapKey, Map<String, String> _mMap) {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        if(StringUtils.isEmpty(objStr))
            return "0";
        BigDecimal rs = new BigDecimal(null != _mMap.get(mapKey) ? _mMap.get(mapKey) : "0")
                .add(new BigDecimal(objStr.toString()));
        return decimalFormat.format(rs);
    }

    /**
     * 运算处理监控页面切片图的数据
     * 
     * @param monitor
     * @return
     * @throws Exception
     */
    public String getDriverStatusData(ChartsBaseMonitor monitor) throws Exception {
//        String driverId = "358";
        String driverId = "";
        for(String dirverI : monitor.getMap().keySet()) {
            driverId = dirverI;
        }
        //driverId = "358";
        MesDriver mesDriver = mesDriverService.findById(Long.valueOf(driverId));
        List<MesDrivertypeProperty> mesDrivertypePropertyList = mesDriver.getMesDrivertype().getMesDrivertypeProperties();
        Map<Integer, String> propertyMap = Maps.newHashMap();
        List<Integer> ids = new ArrayList<Integer>();
        Integer guanjiID = null;
        for(MesDrivertypeProperty property : mesDrivertypePropertyList) {
            if(property.getPropertyname().contains("时间")) {
                if(property.getPropertyname().contains("关机时间")){
                    guanjiID = property.getId().intValue();
                }
                ids.add(property.getId().intValue());
                propertyMap.put(property.getId().intValue(), property.getPropertyname());
            }
        }
        List<SearchFilter> searchList1 = Lists.newArrayList();
        searchList1.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, Long.valueOf(driverId)));
        Map<String, Timestamp> timestampList1 = DateUtils.getEndTimestampForOneDay();
        Timestamp startTime1 = timestampList1.get("start");
        Timestamp endTime1 = timestampList1.get("end");
        searchList1.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,
                String.valueOf(startTime1.getTime()).substring(0, 10)));
        searchList1.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE,
                String.valueOf(endTime1.getTime()).substring(0, 10)));
        searchList1.add(new SearchFilter("driverPropertyId", Operator.IN, ids.toArray()));
        searchList1.add(new SearchFilter("metaPropertyValue", Operator.EQ, "1"));
        Specification<MesDataDriverProperty> specification1 = DynamicSpecifications
                .bySearchFilter(MesDataDriverProperty.class, searchList1);
        Sort sortParam1 = new Sort(Sort.Direction.ASC, "mesDataMultiKey.insertTimestamp");

        List<MesDataDriverProperty> mesDataDriverPropertyLs = mesDataDriverPropertyService.findAll(specification1, sortParam1);
        if(mesDataDriverPropertyLs!=null&&mesDataDriverPropertyLs.size()>1){
            MesDataDriverProperty mesDataDriverPropertyLast= mesDataDriverPropertyLs.get(mesDataDriverPropertyLs.size()-1);
            String insertTimestamp = String.valueOf(mesDataDriverPropertyLast.getMesDataMultiKey().getInsertTimestamp());
            long time = new Date().getTime()/1000;
            Long minusVal =time-Long.valueOf(insertTimestamp);
            //超过1分钟无数据上传默认机器关机
            if(minusVal>60){
                MesDataDriverProperty mesDataDriverProperty = new MesDataDriverProperty();
                mesDataDriverProperty.setDriverPropertyId(guanjiID);
                mesDataDriverProperty.setMetaPropertyValue(mesDataDriverPropertyLast.getMetaPropertyValue());
                MesDataMultiKey mesDataMultiKey = new  MesDataMultiKey();
                mesDataMultiKey.setDriverId(mesDataDriverPropertyLast.getMesDataMultiKey().getDriverId());
                mesDataMultiKey.setFactoryId(mesDataDriverPropertyLast.getMesDataMultiKey().getFactoryId());
                mesDataMultiKey.setPointId(guanjiID);
                mesDataMultiKey.setProductLineId(mesDataDriverPropertyLast.getMesDataMultiKey().getProductLineId());
                mesDataMultiKey.setInsertTimestamp(BigInteger.valueOf(time));
                mesDataDriverProperty.setMesDataMultiKey(mesDataMultiKey);
                this.mesDataDriverPropertyService.save(mesDataDriverProperty);
                mesDataDriverPropertyLs = mesDataDriverPropertyService.findAll(specification1, sortParam1);
            }
        }

        Map<String, Object> dataRsMap = Maps.newHashMap();
        System.out.println("设备ID:"+ driverId);
        System.out.println("检索结果件数：" + mesDataDriverPropertyLs.size());

        // 数据格式样例
        // {name: '运行',value: [1, 1525835791000, 1525835791000 + 600000,
        // 600000],itemStyle:{normal: {color: 'green'}}},
        String beforeStausVal = "";
        String beforeTimeVal = "";
        List<Object> statusChangetimeList = Lists.newArrayList();
        if (null != mesDataDriverPropertyLs && 0 < mesDataDriverPropertyLs.size()) {
            int countIndex = 1;
            for(MesDataDriverProperty obj : mesDataDriverPropertyLs) {
                if (!"".equals(beforeStausVal)) {
                    String status = propertyMap.get(obj.getDriverPropertyId());
                    // 不同的状态发生时，记录时间差
                    if (!beforeStausVal.equals(status)) {
                        Map<String, String> statusMap = Maps.newHashMap();
                        statusMap.put("status", beforeStausVal);
                        BigInteger beforeTime = BigInteger.valueOf(Long.valueOf(beforeTimeVal));
                        BigInteger nowTime = BigInteger
                                .valueOf(obj.getMesDataMultiKey().getInsertTimestamp().longValue());
                        statusMap.put("time", beforeTimeVal);
                        statusMap.put("endtime", String.valueOf(nowTime));
                        statusMap.put("minusTime", String.valueOf(nowTime.subtract(beforeTime)));
                        if ("0".equals(statusMap.get("minusTime")))
                            statusMap.put("minusTime", "0");
                        if (Integer.valueOf(statusMap.get("minusTime")) < 0)
                            statusMap.put("minusTime", "0");
                        statusChangetimeList.add(statusMap);
                        beforeStausVal = status;
                        beforeTimeVal = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());
                        System.out.println("不同的状态发生时，记录时间差。");

                    } else {
                        if(countIndex == mesDataDriverPropertyLs.size()) {
                            if(0 == statusChangetimeList.size()) {
                                Map<String, String> statusMap = Maps.newHashMap();
                                // 第一条数据
                                BigInteger beforeTime = mesDataDriverPropertyLs.get(0).getMesDataMultiKey().getInsertTimestamp();
                                String beforeStatusVal = propertyMap.get(obj.getDriverPropertyId());

                                statusMap.put("status", beforeStatusVal);
                                BigInteger nowTime = BigInteger
                                        .valueOf(obj.getMesDataMultiKey().getInsertTimestamp().longValue());
                                statusMap.put("time", String.valueOf(beforeTime));
                                statusMap.put("endtime", String.valueOf(nowTime));
                                statusMap.put("minusTime", String.valueOf(nowTime.subtract(beforeTime)));
                                if ("0".equals(statusMap.get("minusTime")))
                                    statusMap.put("minusTime", "0");
                                if (Integer.valueOf(statusMap.get("minusTime")) < 0)
                                    statusMap.put("minusTime", "0");
                                statusChangetimeList.add(statusMap);
                                System.out.println("当前为最后一件， 没有出现不同设备状态的数据。");
                            }
                        }
                    }

                } else {
                    beforeStausVal = propertyMap.get(obj.getDriverPropertyId());
                    beforeTimeVal = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());

                }
                // list只有一件时
                if(1 == mesDataDriverPropertyLs.size()) {
                    Map<String, String> statusMap = Maps.newHashMap();
                    statusMap.put("status", beforeStausVal);
                    BigInteger nowTime = BigInteger
                            .valueOf(obj.getMesDataMultiKey().getInsertTimestamp().longValue());
                    statusMap.put("time", beforeTimeVal);
                    statusMap.put("endtime", String.valueOf(nowTime));
                    statusMap.put("minusTime", "6000");
                    if ("0".equals(statusMap.get("minusTime")))
                        statusMap.put("minusTime", "0");
                    if (Integer.valueOf(statusMap.get("minusTime")) < 0)
                        statusMap.put("minusTime", "0");
                    statusChangetimeList.add(statusMap);
                    System.out.println("设备检索list内，只有一件。");
                }
                System.out.println("当前编辑的件：" + countIndex);

                countIndex++;
            }


        }
        List<BigInteger> stopByTotalList = Lists.newArrayList();
        List<BigInteger> standByTotalList = Lists.newArrayList();
        List<BigInteger> runByTotalList = Lists.newArrayList();
        // 数据格式样例
        // {name: '运行',value: [1, 1525835791000, 1525835791000 + 600000,
        // 600000],itemStyle:{normal: {color: 'green'}}},
        List<Object> outList = Lists.newArrayList();
        if (null != statusChangetimeList && 0 < statusChangetimeList.size()) {
            // BigInteger standByTotalTime = BigInteger.valueOf(0L);
            // BigInteger standByTotalTime1 = BigInteger.valueOf(0L);
            // BigInteger stopTotalTime = BigInteger.valueOf(0L);
            // BigInteger runTotalTime = BigInteger.valueOf(0L);

            statusChangetimeList.forEach(obj -> {
                Map<String, Object> makeMap = (Map) obj;
                Map<String, Object> paramMap = Maps.newHashMap();
                // String status = obj.getDriverStatus();
                String status = (String) makeMap.get("status");
                // 0 :运行， 1：停机 2：待机
                // 0：开机 1：关机 2：待机 3：运行
                paramMap.put("name", MesAnylsanysUtils.getStatusValueByCode(status));

                List<Object> valels = Lists.newArrayList();
                valels.add(0);
                // 设备状态起始时间
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("time")) + "000")));
                // 设备状态结束时间
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("endtime")) + "000")));
                // 设备状态持续时间
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                paramMap.put("value", valels);

                ItemStyle itemStyle = new ItemStyle();
                itemStyle.normal().color(MesAnylsanysUtils.getStatusColorByCode(status));
                paramMap.put("itemStyle", itemStyle);
                if (!String.valueOf(makeMap.get("minusTime")).contains("-")) {
                    outList.add(paramMap);
                }

                // 各种时间的统计
                if ("1".equals(status)) {
                    stopByTotalList
                            .add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                } else if ("2".equals(status)) {
                    standByTotalList
                            .add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                } else {
                    runByTotalList
                            .add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                }
            });

        }
        List<Object> outRsList = MonitorUtil.getMonitorList(outList, startTime1.getTime(), endTime1.getTime());
        dataRsMap.put("driverRuntimeList", outRsList);
        return JSONUtils.beanToJson(dataRsMap);
    }
    
    /**
     * 运算处理监控页面码表的数据
     * 
     * @param monitor
     * @return
     * @throws Exception
     */
    public String getDriverPropertyData(ChartsBaseMonitor monitor) throws Exception {
        String driverId = monitor.getDriverId();
        // 获取设备属性ID
        String dPropertyId = "";
        for(String dirverI : monitor.getMap().keySet()) {
            dPropertyId = dirverI;
        }
        
        MesDriver mesDriver = mesDriverService.findById(Long.valueOf(driverId));
        List<MesDrivertypeProperty> mesDrivertypePropertyList = mesDriver.getMesDrivertype().getMesDrivertypeProperties();
        String searchPropertyId = "0";
        for(MesDrivertypeProperty property : mesDrivertypePropertyList) {
            if(dPropertyId.equals(property.getPropertykeyid())) {
                searchPropertyId = property.getId().toString();
                break;
            }
        }

        List<SearchFilter> searchList1 = Lists.newArrayList();
        searchList1.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, Long.valueOf(driverId)));
//        Map<String, Timestamp> timestampList1 = DateUtils.getEndTimestampForOneDay();
//        Timestamp startTime1 = timestampList1.get("start");
//        Timestamp endTime1 = timestampList1.get("end");
//        searchList1.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,
//                String.valueOf(startTime1.getTime()).substring(0, 10)));
//        searchList1.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE,
//                String.valueOf(endTime1.getTime()).substring(0, 10)));
        searchList1.add(new SearchFilter("driverPropertyId", Operator.EQ, searchPropertyId));
        Specification<MesDataDriverProperty> specification1 = DynamicSpecifications
                .bySearchFilter(MesDataDriverProperty.class, searchList1);
//        Sort sortParam1 = new Sort(Sort.Direction.DESC, "mesDataMultiKey.insertTimestamp");

        Page page = new Page();
        page.setOrderField("mesDataMultiKey.insertTimestamp");
        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        page.setNumPerPage(Integer.valueOf(1));
        List<MesDataDriverProperty> mesDataDriverPropertyLs = 
                mesDataDriverPropertyService.findPage(specification1, page);

        Map<String, Object> dataRsMap = Maps.newHashMap();
        System.out.println("设备ID:"+ driverId);
        System.out.println("检索结果件数：" + mesDataDriverPropertyLs.size());

        String propertyData;

        if(null == mesDataDriverPropertyLs || 0 == mesDataDriverPropertyLs.size()) {
            propertyData = "0";
        } else {
            MesDataDriverProperty rs = mesDataDriverPropertyLs.get(0);
            propertyData = rs.getMetaPropertyValue();
        }
        dataRsMap.put("propertyData", propertyData);
        return JSONUtils.beanToJson(dataRsMap);
    }
}
