package com.its.statistics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.entity.main.Dictionary;
import com.its.common.exception.ServiceException;
import com.its.common.service.DictionaryService;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.*;
import com.its.frd.entity.*;
import com.its.frd.schedule.ProductionSchedulerPageSearch;
import com.its.frd.service.MesDataProductProcedureService;
import com.its.frd.service.MesProductProcedureService;
import com.its.frd.service.QdasService;
import com.its.frd.util.*;
import com.its.frd.util.echarts.BarOption;
import com.its.frd.util.echarts.LineOption;
import com.its.statistics.vo.*;
import com.its.statistics.vo.ProductionRecord;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@Service("statisticsService")
public class StatisticsService {

	@Autowired
	private MesProductlineDao mesProductlineDao;

	@Autowired
	private MesProductDao mesProductDao;

	@Autowired
	private ProductionSchedulerPageSearch productionSchedulerPageSearch;

	@Autowired
	private MesSpcTemplateDao mesSpcTemplateDao;

	@Autowired
	private MesProductProcedureDao mesProductProcedureDao;

	@Autowired
	private MesProcedurePropertyDao mesProdcedurePropertyDao;

	@Autowired
	private MesDriverDao mesDriverDao;

	@Autowired
	private MesPointsDao mesPointsDao;

	//@Autowired
	//private HbaseTemplate hTemplate;

	@Autowired
	private CompanyinfoDao companyinfoDao;

	@Autowired
	private QdasService qdasService;
	
	@Resource
	private DictionaryService dictionaryService;
	
	@Autowired
	private MesProductProcedureService mesProductProcedureService;
	
	@Autowired
	private QdasDao qdasDao;

	@Autowired
	private MesDataProductProcedureService mesDataProductProcedureService;

	@Autowired
	private MonitorPainterDao monitorPainterDao;

    @Resource
    private MesProcedurePropertyDao mesProcedurePropertyDao;

	@Autowired
	private MonitorSpcDao monitorSpcDao;

    @PersistenceContext
    private EntityManager entityManager;

	public static final String TABLE_NAME = "frd2";
	public static final String FAMILY_NAME = "cf";
	@Value("${k1}")
	private String k1;
	@Value("${k2}")
	private String k2;
	@Value("${k3}")
	private String k3;



	/**
	 * 获取当前公司下的所有产线
	 * @return
	 */
	public List<MesProductline> getProductionLineByCurrentCompanyId(){
		return mesProductlineDao.findAll(new Specification<MesProductline>() {
			@Override
			public Predicate toPredicate(Root<MesProductline> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Long companyId = SecurityUtils.getShiroUser().getCompanyid();
				Predicate p1 = builder.equal(root.get("companyinfo").get("parentid").as(Long.class),companyId);
				return query.where(p1).getRestriction();
			}
		});
	}
	
	/**
	 * 获取当前公司下的所有产线
	 * @return
	 */
	public List<MesProductline> getProductionLineByCurrentCompanyId(Long mesproductLineId){
		return mesProductlineDao.findAll(new Specification<MesProductline>() {
			@Override
			public Predicate toPredicate(Root<MesProductline> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//				Long companyId = SecurityUtils.getShiroUser().getCompanyid();
				Predicate p1 = builder.equal(root.get("id").as(Long.class), mesproductLineId);
				return query.where(p1).getRestriction();
			}
		});
	}

	public List<MesProduct> getProductByCurrentCompanyId(){
		return mesProductDao.findAll(new Specification<MesProduct>() {
			@Override
			public Predicate toPredicate(Root<MesProduct> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Long companyId = SecurityUtils.getShiroUser().getCompanyid();
				Predicate p1 = builder.equal(root.get("companyinfo").get("id").as(Long.class),companyId);
				return query.where(p1).getRestriction();
			}
		});
	}
	/**
	 * 生成设备Select
	 * @return
	 */
	public Map<String, List<MesDriver>> generateDriverSelect() {
		Map<String, List<MesDriver>> map = new ConcurrentHashMap<>();
		for(MesProductline mesProductline : getProductionLineByCurrentCompanyId()){
			List<MesDriver> mesDrivers = mesProductline.getMesDrivers();
			if(null != mesProductline.getLinename() && !mesProductline.getLinename().equals("")){
				map.put(mesProductline.getLinename(), mesDrivers);
			}
		}
		return map;
	}
	public List<MesProduct> generateProductSelect() {
		return getProductByCurrentCompanyId();
	}

	/**
	 * 通过产品ID查找产品工序
	 * @param productId
	 * @return
	 */
	public String generateProductProcedureSelect(long productId) {
		MesProduct mesProduct = mesProductDao.findOne(productId);

		try {
			List<MesProductProcedure> mesProductProcedures = mesProduct.getMesProductProcedures();
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			return  mapper.writeValueAsString(mesProductProcedures);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}
	
    /**
     * 通过产品ID查找产品批次号
     * @param productId
     * @return
     */
    public String generateProbatchids(long productId) {

        try {
            MesProduct mesProduct = mesProductDao.findOne(productId);
            MesDataRowkeyUtil.setEntityManager(entityManager);
            List<MesDataProductProcedure>  MesDataProductProcedureList = MesDataRowkeyUtil.getProductBatchids(mesProduct.getModelnum(), "", "");
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            return  mapper.writeValueAsString(MesDataProductProcedureList);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
    
    /**
     * 通过产品ID查找产品批次号
     * @param productId
     * @return
     */
    public String generateProbatchids2(long productId) {

        try {
//            MesProduct mesProduct = mesProductDao.findOne(productId);
            MesDataRowkeyUtil.setEntityManager(entityManager);
            List<MesDataProductProcedure>  MesDataProductProcedureList = MesDataRowkeyUtil.getProductBatchids(String.valueOf(productId), "", "");
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            return  mapper.writeValueAsString(MesDataProductProcedureList);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
	public Map<String, List<MesProductline>> generateProductLineSelect(long companyId) {
		Map<String, List<MesProductline>> map = new ConcurrentHashMap<>();
		for(MesProductline mesProductline : getProductionLineByCurrentCompanyId()){
			if(!map.containsKey(mesProductline.getCompanyinfo().getCompanyname())){
				List<MesProductline> list = new ArrayList<>();
				map.put(mesProductline.getCompanyinfo().getCompanyname(), list);
			}
			map.get(mesProductline.getCompanyinfo().getCompanyname()).add(mesProductline);
		}
		return map;
	}
	/**
	 * 通过工序ID生成工序参数Select
	 * @param procedureId
	 * @return
	 */
	public String generateProcedurePropertySelect(long procedureId) {
		MesProductProcedure mesProductProcedure = mesProductProcedureDao.findOne(procedureId);
		try {
			List<MesProcedureProperty> mesProcedureProperties = mesProductProcedure.getMesProcedureProperties();
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			return  mapper.writeValueAsString(mesProcedureProperties);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}

	public String generateProcedurePropertySpcAnalyseSelect(long procedureId) {
		MesProductProcedure mesProductProcedure = mesProductProcedureDao.findOne(procedureId);
		try {
			List<MesProcedureProperty> mesProcedureProperties = mesProductProcedure.getMesProcedureProperties();
			List<MesProcedureProperty> new_mesProcedureProperties = new ArrayList<MesProcedureProperty>();
			List<MonitorPainter> monitorPainterList= monitorPainterDao.findBySpcAnalysisDataNotNull();
			//遍历监控配置表
			for(MonitorPainter m : monitorPainterList) {
//				long id = m.getId();
				String spc_analysis_Data = m.getSpcAnalysisData();
				JSONArray jsonArray = JSONArray.fromObject(spc_analysis_Data);
				//遍历监控配置中pp,ppk,cp,cpk有几个拖拉控件
				for (int i = 0; i < jsonArray.size(); i++) {
					net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
//					String chartId = (String) jsonObject.get("chartId");
					JSONArray jsonArray_data = (JSONArray) jsonObject.get("data");
					long procedurePropertyId = 0;
					for (int j = 0; j < jsonArray_data.size(); j++) {
						net.sf.json.JSONObject jsonObject_data = jsonArray_data.getJSONObject(j);
						String name = (String) jsonObject_data.get("name");
						if (name.equals("procedurePropertyId")) {
							procedurePropertyId = Long.valueOf((String) jsonObject_data.get("value"));
						}
					}
					for (MesProcedureProperty mm : mesProcedureProperties){
						if (mm.getId().equals(procedurePropertyId)){
							Integer hasData = 0;
							for (MesProcedureProperty new_mm : new_mesProcedureProperties){
								if (new_mm.getId().equals(procedurePropertyId)){
									hasData = 1;
									break;
								}
							}
							if (hasData == 1){
								continue;
							}
							new_mesProcedureProperties.add(mm);
							continue;
						}
					}

				}
//				saveSpcAnalysis(id, spc_analysis_Data);
//				System.out.println(id);
			}
			if (new_mesProcedureProperties.size() == 0){
				MesProcedureProperty empty_esProcedureProperty= new MesProcedureProperty();
				empty_esProcedureProperty.setPropertyname("无");
				new_mesProcedureProperties.add(empty_esProcedureProperty);
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			return  mapper.writeValueAsString(new_mesProcedureProperties);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}

	/**
	 * 生产记录：总件数，合格数，不合格数，合格率
	 * @param hBasePageModel
	 * @return
	 */
	public String pageCaculate(HBasePageModel hBasePageModel) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Map<String,Object> map = new HashMap<>();
		hBasePageModel.setPagable(false);
//		List<ProductionRecord> list = productionSchedulerPageSearch.getProductionRecordPagable(hBasePageModel);
		Map<String,Long> goodMap = new HashMap<>();
		goodMap.put("good", 0L);
		Map<String,Long> badMap = new HashMap<>();
		badMap.put("bad", 0L);
//		for(ProductionRecord productionRecord : list){
//			if(productionRecord.getStatus().equals("OK")){
//				goodMap.put("good", goodMap.get("good") + 1);
//			}else{
//				badMap.put("bad", badMap.get("bad") + 1);
//			}
//		}
		Map<String, Object> caluateMap = getCountByHbase(hBasePageModel);
		// Map<String, Object> totalCountMap = (Map<String, Object>) caluateMap.get("totalCount");
		Long totalCount = (Long)caluateMap.get("totalCount");
		// Map<String, Object> nopassCountMap = (Map<String, Object>) caluateMap.get("nopassCount");
		Long nopassCount = (Long)caluateMap.get("nopassCount");
		goodMap.put("good", Long.valueOf(String.valueOf(totalCount - nopassCount)) == 0L?null : Long.valueOf(String.valueOf(totalCount - nopassCount)));
		badMap.put("bad", nopassCount == 0L ? null : nopassCount);
//		goodMap.put("good", Long.valueOf(String.valueOf(totalCountMap.size() - nopassCountMap.size())));
//		badMap.put("bad", Long.valueOf(String.valueOf(nopassCountMap.size())));
		hBasePageModel.setGoodMap(goodMap);
		hBasePageModel.setBadMap(badMap);
		hBasePageModel.setTotalCount(totalCount);
		map.put("page", hBasePageModel);
		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "{}";
	}
	/**
	 * 生产记录非工件查询
	 * @param hBasePageModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String productionDataSearchByNewHbase(HBasePageModel hBasePageModel) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Map<String,Object> map = new HashMap<>();
//		List<ProductionRecord> list = productionSchedulerPageSearch.getProductionRecordPagable(hBasePageModel);
//		int amount = productionSchedulerPageSearch.getProductionRecordAmount(hBasePageModel);
		
		AnalyzeSearch analyzeSearch = hBasePageModel.getAnalyzeSearch();
		// 工厂ID
		Long factoryId = null;
		// 产线ID
		Long productlineId = analyzeSearch.getProductLineId();
		// 产品ID
		MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());
//		Long productId = StringUtils.isBlank(mesProduct.getModelnum()) ? null : Long.valueOf(mesProduct.getModelnum());
		String productId = StringUtils.isBlank(mesProduct.getModelnum()) ? null : mesProduct.getModelnum();
		// 工序ID
		Long produceId = analyzeSearch.getProductProcedureId();
	    // 批次号
        String productBatchid = analyzeSearch.getProductBatchid();
		// 产线名
		String productlineNm = "";
		// 产品名
		String productNm = mesProduct.getName();
		//测量类型
		Integer meastype = analyzeSearch.getMeastype();

		if(null != productlineId && 0L != productlineId) {
	        List<MesProductline> mesProductlineList = getProductionLineByCurrentCompanyId(productlineId);
	        factoryId = mesProductlineList != null && mesProductlineList.get(0) != null ?mesProductlineList.get(0).getCompanyinfo().getId() : null;
	        for(MesProductline mesProductline : getProductionLineByCurrentCompanyId(productlineId)){
	            if(productlineId.equals(productlineId)) {
	                if(null != mesProductline.getCompanyinfo()) {
	                    factoryId = mesProductline.getCompanyinfo().getId();
	                    productlineNm = mesProductline.getLinename();
	                }
	                break;
	            }
	        }
		}

        System.out.println("生产记录----工厂ID:" + factoryId);
        System.out.println("生产记录----产线ID:" + productlineId);
        System.out.println("生产记录----产品ID:" + productId);
        System.out.println("生产记录----工序ID:" + produceId);
        System.out.println("生产记录----批次号:" + productBatchid);
        System.out.println("生产记录----analyzeSearch.getProductionSn():" + analyzeSearch.getProductionSn());	
		Map<String, Date> timeForScan = MesDataRowkeyUtil.getScanTimeRangeByHBasePageModel(hBasePageModel);
        System.out.println("生产记录----timeForScan.get(begin):" + timeForScan.get("begin"));
        System.out.println("生产记录----timeForScan.get(end):" + timeForScan.get("end"));

        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, String.valueOf(factoryId)));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, String.valueOf(productlineId)));
        if(null != timeForScan.get("begin"))
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(timeForScan.get("begin").getTime()).replace("000", "")));
        if(null != timeForScan.get("end"))
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(timeForScan.get("end").getTime()).replace("000", "")));

        // 工序情报设定
        MesProductProcedure mesProductProcedure = mesProductProcedureDao.findOne(produceId);
        List<Long> producePerties = Lists.newArrayList();
        for(MesProcedureProperty obj : mesProductProcedure.getMesProcedureProperties()) {
            producePerties.add(obj.getId());
        }
        searchList.add(new SearchFilter("productProcedureId", Operator.IN, producePerties.toArray()));
        
//        searchList.add(new SearchFilter("productProcedureId", Operator.EQ, produceId));
        searchList.add(new SearchFilter("productMode", Operator.EQ, productId));

        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
//        Page outPage = new Page();
//        outPage.setOrderField("mesDataMultiKey.insertTimestamp");
//        outPage.setOrderDirection(Page.ORDER_DIRECTION_DESC);
//        outPage.setPageNum(0);
//        outPage.setNumPerPage(10);
        hBasePageModel.setOrderDirection("mesDataMultiKey.insertTimestamp");
        hBasePageModel.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        // List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findPage(specification, hBasePageModel);
        MesDataRowkeyUtil.setEntityManager(entityManager);
        String beginTimeS = null == timeForScan.get("begin") ? "" : String.valueOf(timeForScan.get("begin").getTime()).replace("000", "");
        String endTimeS = null == timeForScan.get("end") ? "" : String.valueOf(timeForScan.get("end").getTime()).replace("000", "");
        List<MesDataProductProcedure> mesDataProductProcedureList =MesDataRowkeyUtil.getMulKeyOfPage(String.valueOf(factoryId), String.valueOf(productlineId), "", String.valueOf(productId),
                String.valueOf(produceId),
                beginTimeS,endTimeS, hBasePageModel, "1",
                productBatchid,meastype);

//        List<String> distinctRowKeyList = Lists.newArrayList();
        List<String> statusTimeList = Lists.newArrayList();
        if(null != mesDataProductProcedureList && 0 <mesDataProductProcedureList.size()) {
            String beTime = String.valueOf(mesDataProductProcedureList.get(mesDataProductProcedureList.size() - 1).getMesDataMultiKey().getInsertTimestamp());
            String enTime = String.valueOf(mesDataProductProcedureList.get(0).getMesDataMultiKey().getInsertTimestamp());
            statusTimeList =MesDataRowkeyUtil.getCountByPageSelect(String.valueOf(factoryId), String.valueOf(productlineId), "", String.valueOf(productId),
                    String.valueOf(produceId),
                    beTime,enTime, "NG",
                    productBatchid);
        }
        if(StringUtils.isEmpty(productlineNm)) {
            if(null != mesDataProductProcedureList && 0 < mesDataProductProcedureList.size()) {
                Integer productLineId = mesDataProductProcedureList.get(0).getMesDataMultiKey().getProductLineId();
                MesProductline  mesproductline = mesProductlineDao.findOne(Long.valueOf(String.valueOf(productLineId)));
                productlineNm = mesproductline.getLinename();
            }
        }

        List<ProductionRecord> listOut = Lists.newArrayList();
        ProductionRecord productionRecordOut = null;
        for(MesDataProductProcedure obj : mesDataProductProcedureList) {
//            distinctRowKeyList.add(MesDataRowkeyUtil.getRowKey(obj));
            
            productionRecordOut = new ProductionRecord();
            productionRecordOut.setRowkey(MesDataRowkeyUtil.getRowKey(obj));
            // 批次号
            productionRecordOut.setProductBatchid(obj.getProductBatchid());
            // 工件号
            productionRecordOut.setProductSn(obj.getProductBsn());
            // 生产线
            productionRecordOut.setProductlineName(productlineNm);
            // 产品名称
            productionRecordOut.setProductName(productNm);
            // 日期
            productionRecordOut.setDatetime(obj.getMesDataMultiKey().getInsertTimestamp());
            // 状态
            productionRecordOut.setStatus(statusTimeList.contains(String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp())) ? "NG" : "OK");

			//测量类型
			switch (obj.getMeastype()) {
				case 0:
					productionRecordOut.setMeastype("首末检");
					break;
				case 1:
					productionRecordOut.setMeastype("换刀检");
					break;
				case 2:
					productionRecordOut.setMeastype("抽检");
					break;
				case 3:
					productionRecordOut.setMeastype("全尺寸检");
					break;
				default:
					break;
			}

			//首检是否合格
			if(obj.getMeastype()==0 && obj.getQualified()==1){
				productionRecordOut.setQualified("合格");
			}else if(obj.getMeastype()==0 && obj.getQualified()==0){
				productionRecordOut.setQualified("不合格");
			}

            listOut.add(productionRecordOut);
        }

        List<String> timeList =MesDataRowkeyUtil.getCountByPageSelect(String.valueOf(factoryId), String.valueOf(productlineId), "", String.valueOf(productId),
                String.valueOf(produceId),
                beginTimeS,endTimeS, "",
                productBatchid);
        hBasePageModel.setTotalCount(timeList !=null ?timeList.size() : 0);

//        List<Result> procedureRSList = new HbaseUtil().getResultListByHbase(factoryId,
//				productlineId, null, productId, produceId, null, timeForScan.get("begin"), timeForScan.get("end"), false);
		// 获取时间list，进行倒序
//        List<String> timestampList = Lists.newArrayList();
		// 根据时间重排rowkey
//        Map<String, String> rowkeyTimestamp = Maps.newHashMap();
//        Map<String, String> rsMap = null;
//
//        for (Result result : procedureRSList) {
//            rsMap = getRowByResult(result);
//            timestampList.add(rsMap.get("timestamp"));
//            rowkeyTimestamp.put(rsMap.get("timestamp"), rsMap.get("rowkey") + ":" + rsMap.get("qf"));
//            if(!distinctRowKeyList.contains(String.valueOf(rsMap.get("rowkey")))) {
//                distinctRowKeyList.add(rsMap.get("rowkey"));
//            }
//        }
//        int pageSelectedCount = timestampList.size() - (hBasePageModel.getPlainPageNum() - 1) * 10;
//        pageSelectedCount = pageSelectedCount > 0 ?  timestampList.size()- (hBasePageModel.getPlainPageNum() - 1) * 10 : pageSelectedCount;

//      int pageSelectedCount = timestampList.size() - (hBasePageModel.getPlainPageNum() - 1) * hBasePageModel.getPageNum();
//      pageSelectedCount = pageSelectedCount > 0 ?  timestampList.size()- (hBasePageModel.getPlainPageNum() - 1) * hBasePageModel.getNumPerPage() : pageSelectedCount;
//
//		List<String> outList = distinctRowKeyList.subList(pageSelectedCount<hBasePageModel.getNumPerPage() ? 0 : pageSelectedCount - hBasePageModel.getNumPerPage(), pageSelectedCount);
//		Collections.reverse(outList);
//		List<Result> resOut = HBaseUtilNew.getDatasFromHbase(HbaseUtil.HBASE_TABLE_PROCEDURE, "cf", outList, null, false, false);
//        List<ProductionRecord> listOut = Lists.newArrayList();
//        ProductionRecord productionRecordOut = null;
//        for (Result result : resOut) {
//        	Map<String, String> outResult = getRowByResultForOutPut(result);
//        	productionRecordOut = new ProductionRecord();
//        	productionRecordOut.setRowkey(outResult.get("rowkey"));
//        	// 工件号
//        	productionRecordOut.setProductSn(outResult.get("productSn"));
//        	// 生产线
//        	productionRecordOut.setProductlineName(productlineNm);
//        	// 产品名称
//        	productionRecordOut.setProductName(productNm);
//        	// 日期
//        	productionRecordOut.setDatetime(BigInteger.valueOf(Long.valueOf(outResult.get("timestamp"))));
//        	// 状态
//        	productionRecordOut.setStatus(outResult.get("status"));
//        	listOut.add(productionRecordOut);
//        }
//        
//		hBasePageModel.setTotalCount(timestampList.size());

		Map<Long ,MesProductProcedure> procedureMap = new HashMap<>();
		procedureMap.put(produceId, mesProductProcedure);
		hBasePageModel.setProcedureMap(procedureMap);
		map.put("productionRecords", listOut);
		map.put("page", hBasePageModel);

		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "{}";
	}
	public String productionDataSearch(HBasePageModel hBasePageModel) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Map<String,Object> map = new HashMap<>();
		List<ProductionRecord> list = productionSchedulerPageSearch.getProductionRecordPagable(hBasePageModel);
		int amount = productionSchedulerPageSearch.getProductionRecordAmount(hBasePageModel);
		hBasePageModel.setTotalCount(amount);
		map.put("productionRecords", list);
		map.put("page", hBasePageModel);
		/*





		long methodStart = System.currentTimeMillis();
		System.out.println("查询方法开始:"+methodStart);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		long firstHbaseStart = System.currentTimeMillis();
		System.out.println("第一次HBase查询开始:"+firstHbaseStart);
		List<ProductionRecord> productionRecords= getRecordList(hBasePageModel,true);
		long firstHbaseStop = System.currentTimeMillis();
		System.out.println("第一次HBase查询结束:"+firstHbaseStop);
		System.out.println("第一次HBase查询结束耗时:"+(firstHbaseStop-firstHbaseStart));
		if(productionRecords.size() == 0){
			hBasePageModel.setTotalCount(0);
			map.put("productionRecords", productionRecords);
			map.put("page", hBasePageModel);
			try {
				return objectMapper.writeValueAsString(map);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		long secondHbaseStart = System.currentTimeMillis();
		System.out.println("第二次HBase查询开始:"+secondHbaseStart);
		AggregationClient client = new AggregationClient(hTemplate.getConfiguration());
		Long count = 0L;
		try {
			Scan scan = getRecordScan(hBasePageModel, false).getScan();
			count = client.rowCount(TableName.valueOf("frd"),  
			        new LongColumnInterpreter(), scan);
			System.out.println("我日啊:"+count);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}  
		List<ProductionRecord> productionRecordsNum= getRecordList(hBasePageModel,false);
		long secondHbaseStop = System.currentTimeMillis();
		System.out.println("第二次HBase查询结束:"+secondHbaseStop);
		System.out.println("第二次HBase查询结束耗时:"+(secondHbaseStop-secondHbaseStart));
		if(productionRecordsNum.size() <= hBasePageModel.getNumPerPage()){
			hBasePageModel.setTotalCount(productionRecordsNum.size());
		}else{
			hBasePageModel.setTotalCount(productionRecordsNum.size() - 1);
			productionRecords.remove(productionRecords.size() - 1);
			//productionRecordsNum.remove(productionRecordsNum.size() - 1);
		}
		Map<String,Long> goodMap = new HashMap<>();
		goodMap.put("good", 0L);
		Map<String,Long> badMap = new HashMap<>();
		badMap.put("bad", 0L);
		for(ProductionRecord productionRecord : productionRecordsNum){
			if(productionRecord.getStatus().equals("OK")){
				goodMap.put("good", goodMap.get("good") + 1);
			}else{
				badMap.put("bad", badMap.get("bad") + 1);
			}
		}
		hBasePageModel.setGoodMap(goodMap);
		hBasePageModel.setBadMap(badMap);
		map.put("nextStartRowKey", productionRecords.get(productionRecords.size() - 1).getRowkey());
		map.put("preStartRowKey", hBasePageModel.getPagePreStartRowkey());
		map.put("startRowKey", productionRecords.get(0).getRowkey());
		map.put("productionRecords", productionRecords);
		map.put("pageType", "product");

		map.put("page", hBasePageModel);
		long methodStop = System.currentTimeMillis();*/

		try {
			//System.out.println("查询方法结束:"+methodStop);
			//System.out.println("查询方法结束耗时:"+(methodStop-methodStart));
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "{}";
	}
	/**
	 * 生产记录的新版HBASE处理
	 * @param hBasePageModel
	 * @return 处理结果
	 */
	public String productionDetailDataSearchByNewHbase(HBasePageModel hBasePageModel) {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,Object> map = new HashMap<>();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesProductProcedure> productionRecords = new ArrayList<>();
		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}
	public String productionDetailDataSearch(HBasePageModel hBasePageModel) {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,Object> map = new HashMap<>();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesProductProcedure> productionRecords = new ArrayList<>();
		if(hBasePageModel.getRowKey() != null && !hBasePageModel.getRowKey().equals("") 
				&& StringUtils.isEmpty(hBasePageModel.getAnalyzeSearch().getProductionSn())){
			try {
                productionRecords= getProcedureRecordList(hBasePageModel,false);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException
                    | NoSuchMethodException e) {
                e.printStackTrace();
            }
		}else {
			AnalyzeSearch analyzeSearch = hBasePageModel.getAnalyzeSearch();
			// 工厂ID
			Long factoryId = null;
			// 产线ID
			Long productlineId = analyzeSearch.getProductLineId();
			productlineId = productlineId !=null && 0L != productlineId ?productlineId: null;
			// 产品ID
			MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());
			Long productId = null;
			if(null != mesProduct) {
				productId = StringUtils.isBlank(mesProduct.getModelnum()) ? null : Long.valueOf(mesProduct.getModelnum());
			}
			
			// 工序ID
			// Long produceId = analyzeSearch.getProductProcedureId();
			if(null != productlineId && 0L != productlineId) {
				List<MesProductline> mesproductLineList = getProductionLineByCurrentCompanyId(productlineId);
				factoryId = mesproductLineList != null ? mesproductLineList.get(0).getCompanyinfo().getId() : null;
			}

//			for(MesProductline mesProductline : getProductionLineByCurrentCompanyId(productlineId)){
//				if(productlineId.equals(productlineId)) {
//					if(null != mesProductline.getCompanyinfo()) {
//						factoryId = mesProductline.getCompanyinfo().getId();
//					}
//					break;
//				}
//			}
			System.out.println("生产记录----工厂ID:" + factoryId);
			System.out.println("生产记录----产线ID:" + productlineId);
			System.out.println("生产记录----产品ID:" + productId);
			System.out.println("生产记录----工序ID:" + "不作为检索条件");
			System.out.println("生产记录----analyzeSearch.getProductionSn():" + analyzeSearch.getProductionSn());

			Map<String, Date> timeForScan = MesDataRowkeyUtil.getScanTimeRangeByHBasePageModel(hBasePageModel);
	         System.out.println("生产记录----timeForScan.get(begin):" + timeForScan.get("begin"));
	          System.out.println("生产记录----timeForScan.get(end):" + timeForScan.get("end"));

	          List<SearchFilter> searchList = Lists.newArrayList();
	          if(null != factoryId)
	          searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, String.valueOf(factoryId)));
	          if(null != productlineId)
	          searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, String.valueOf(productlineId)));
	          if(null != timeForScan.get("begin"))
	          searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(timeForScan.get("begin").getTime()).replace("000", "")));
	          if(null != timeForScan.get("end"))
	          searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(timeForScan.get("end").getTime()).replace("000", "")));
	          if(StringUtils.isNotEmpty(analyzeSearch.getProductionSn()))
	          searchList.add(new SearchFilter("productBsn", Operator.EQ, analyzeSearch.getProductionSn()));
	          if(0 != analyzeSearch.getProductId())
	          searchList.add(new SearchFilter("productMode", Operator.EQ, analyzeSearch.getProductId()));
	          Specification<MesDataProductProcedure> specification = DynamicSpecifications
	                  .bySearchFilter(MesDataProductProcedure.class, searchList);
	          List<MesDataProductProcedure> mesDataProductProcedrureList = mesDataProductProcedureService.findAll(specification);
	          Map<String, String> procedureMap = Maps.newHashMap();
	            Set<BigInteger> procedureIds = new HashSet<>();
	          for(MesDataProductProcedure  obj : mesDataProductProcedrureList) {
	                if(!procedureMap.containsKey(String.valueOf(obj.getProductProcedureId()))) {
	                    procedureMap.put(String.valueOf(obj.getProductProcedureId()), MesDataRowkeyUtil.getRowKey(obj));
	                    procedureIds.add(BigInteger.valueOf(obj.getProductProcedureId()));
	                }
	          }
//
//			List<Result> procedureRSList = new HbaseUtil().getResultListByHbase(factoryId,
//					productlineId, null, productId, null, analyzeSearch.getProductionSn(), timeForScan.get("begin"), timeForScan.get("end"), false);
//
//	        for (Result result : procedureRSList) {
//	        	Cell cell = result.listCells().get(0);
//                String rowkey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
//                if(!procedureMap.containsKey(rowkey.split("_")[4])) {
//	                procedureMap.put(rowkey.split("_")[4], rowkey);
//	                procedureIds.add(BigInteger.valueOf(Long.valueOf(rowkey.split("_")[4])));
//                }
//	        }

			//hBasePageModel.setPagable(false);
			System.out.println("nothing");
			//按工件号查询不过滤产品工序,查询出所有工序的记录数据
			// hBasePageModel.getAnalyzeSearch().setProductProcedureId(0);
			// List<ProductionRecord> productionRecordList = this.productionSchedulerPageSearch.getProductionRecordPagable(hBasePageModel);
			//List<String> rowKeys = new ArrayList<>();
			//Set<BigInteger> procedureIds = new HashSet<>();
			//Map<BigInteger,MesProductProcedure> productProcedureMap = new HashMap<>();
//			for(ProductionRecord productionRecord : productionRecordList){
//				rowKeys.add(productionRecord.getRowkey());
//				procedureIds.add(productionRecord.getProcedureId());
//			}
			List<MesProductProcedure> list2 = new ArrayList<>();
			if(procedureIds.size() != 0){
				list2 = this.mesProductProcedureDao.findAll(new Specification<MesProductProcedure>() {
					@Override
					public Predicate toPredicate(Root<MesProductProcedure> root, CriteriaQuery<?> query,
							CriteriaBuilder builder) {
						Predicate predicate = root.get("id").in(procedureIds);
						return query.where(predicate).getRestriction();
					}
				});
			}
			Map<BigInteger,MesProductProcedure> productProcedureMap = new HashMap<>();
			for(MesProductProcedure procedure : list2){
				productProcedureMap.put(BigInteger.valueOf(procedure.getId()), procedure);
			}
			for(String procedureIdIn : procedureMap.keySet()) {
				try {
					MesProductProcedure mesProductProcedureNew = (MesProductProcedure)BeanUtils.cloneBean(productProcedureMap.get(BigInteger.valueOf(Long.valueOf(procedureIdIn))));
					mesProductProcedureNew.setRowKey(procedureMap.get(procedureIdIn));
					mesProductProcedureNew.setProcessCharacteristicBtn("<a style='cursor:pointer'>过程特性</a>");
					mesProductProcedureNew.setProcessControlBtn("<a style='cursor:pointer'>过程控制</a>");
					mesProductProcedureNew.setSn(analyzeSearch.getProductionSn());
					productionRecords.add(mesProductProcedureNew);
				} catch (IllegalAccessException | InstantiationException | InvocationTargetException
						| NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
//			for(ProductionRecord productionRecord : productionRecordList){
//				if(productionRecord.getProcedureId() == null || productionRecord.getProcedureId().intValue() == 0 || null == productProcedureMap.get(productionRecord.getProcedureId())){
//					continue;
//				}
//				try {
//					MesProductProcedure mesProductProcedureNew = (MesProductProcedure)BeanUtils.cloneBean(productProcedureMap.get(productionRecord.getProcedureId()));
//					mesProductProcedureNew.setRowKey(productionRecord.getRowkey());
//					mesProductProcedureNew.setProcessCharacteristicBtn("<a style='cursor:pointer'>过程特性</a>");
//					mesProductProcedureNew.setProcessControlBtn("<a style='cursor:pointer'>过程控制</a>");
//					mesProductProcedureNew.setSn(productionRecord.getProductSn());
//					productionRecords.add(mesProductProcedureNew);
//				} catch (IllegalAccessException | InstantiationException | InvocationTargetException
//						| NoSuchMethodException e) {
//					e.printStackTrace();
//				}
//			}

			/*if(rowKeys.size() != 0){
				List<Result> listResult = HBaseUtilNew.getDatasFromHbase(HbaseUtil.TABLE_NAME, HbaseUtil.FAMILY_NAME, rowKeys, null, true, true);
				for(Result result : listResult){
					String row = Bytes.toString(result.getRow());
					String procedureId = row.split(":")[3];
					MesProductProcedure tmp = new MesProductProcedure();
					tmp.setId(Long.parseLong(procedureId));
					String quali = Bytes.toString( result.listCells().get(0).getQualifierArray(),result.listCells().get(0).getQualifierOffset(),result.listCells().get(0).getQualifierLength());
					if(quali.split(":")[3].equals(MesPointType.TYPE_PRODUCT_PROCEDURE)){
						MesProductProcedure mesProductProcedure = list2.get(list2.indexOf(tmp));
						MesProductProcedure mesProductProcedureNew = null;
						if(null != mesProductProcedure){
							try {
								mesProductProcedureNew = (MesProductProcedure)BeanUtils.cloneBean(mesProductProcedure);
							} catch (Exception e) {
								e.printStackTrace();
							} 
							mesProductProcedureNew.setRowKey(row);
							mesProductProcedureNew.setProcessCharacteristicBtn("<a style='cursor:pointer'>过程特性</a>");
							mesProductProcedureNew.setProcessControlBtn("<a style='cursor:pointer'>过程控制</a>");
							mesProductProcedureNew.setSn(row.split(":")[1]);
						}
						productionRecords.add(mesProductProcedureNew);
					}
				}
			}*/
		}

		if(productionRecords.size() == 0){
			hBasePageModel.setTotalCount(0);
			map.put("procedureRecords", productionRecords);
			map.put("page", hBasePageModel);
			try {
				return objectMapper.writeValueAsString(map);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		//List<MesProductProcedure> productionRecordsNum= getProcedureRecordList(hBasePageModel,false);
		/*if(productionRecordsNum.size() <= hBasePageModel.getNumPerPage()){
			hBasePageModel.setTotalCount(productionRecordsNum.size());
		}else{
			hBasePageModel.setTotalCount(productionRecordsNum.size() - 1);
			productionRecords.remove(productionRecords.size() - 1);
		}*/
		hBasePageModel.setTotalCount(productionRecords.size());
		/*	map.put("nextStartRowKey", productionRecords.get(productionRecords.size() - 1).getRowKey());
		map.put("preStartRowKey", hBasePageModel.getPagePreStartRowkey());
		map.put("startRowKey", productionRecords.get(0).getRowKey());*/
		map.put("procedureRecords", productionRecords);
		map.put("pageType", "productDetail");
		map.put("page", hBasePageModel);

		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}
	public List<List<MesProcedureProperty>> propertyHistoryTrend(PropertyTrendSearch propertyTrendSearch) throws Exception {
		Map<Long,MesProcedureProperty> map = new HashMap<>();
		List<MesProcedureProperty> mesProcedureProperties = mesProdcedurePropertyDao.findAll(new Specification<MesProcedureProperty>() {
			@Override
			public Predicate toPredicate(Root<MesProcedureProperty> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
				return criteriaQuery.where(root.get("id").in(propertyTrendSearch.getProcedurePropertyIds())).getRestriction();
			}
		});
		for(MesProcedureProperty mesProcedureProperty : mesProcedureProperties){
			map.put(mesProcedureProperty.getId(), mesProcedureProperty);
		}
		
        List<SearchFilter> searchList = Lists.newArrayList();

        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(propertyTrendSearch.getBegin().getTime()).replace("000", "")));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE,String.valueOf(propertyTrendSearch.getEnd().getTime()).replace("000", "")));
        searchList.add(new SearchFilter("productProcedureId", Operator.EQ, String.valueOf(propertyTrendSearch.getProductProcedureId())));
        MesProduct mesProduct = mesProductDao.findOne(propertyTrendSearch.getProductId());
        searchList.add(new SearchFilter("productMode", Operator.EQ, String.valueOf(mesProduct.getModelnum())));
        searchList.add(new SearchFilter("procedurePropertyId", Operator.IN, propertyTrendSearch.getProcedurePropertyIds().toArray()));
//        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, new Timestamp(propertyTrendSearch.getBegin().getTime())));
//        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, new Timestamp(propertyTrendSearch.getEnd().getTime())));
        // 产品ID
//        MesProduct mesProduct = mesProductDao.findOne(propertyTrendSearch.getProductId());
//        searchList.add(new SearchFilter("productProcedureId", Operator.EQ, String.valueOf(propertyTrendSearch.getProductProcedureId())));

        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
        List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findAll(specification);
        List<List<MesProcedureProperty>> outList = Lists.newArrayList();
//        List<MesProcedureProperty> outInerList = Lists.newArrayList();

        Map<Long, List<MesProcedureProperty>>  mesProcedurePropertyMap = Maps.newHashMap();
        List<MesProcedureProperty> mesProcedurePropertyList= null;
        for(MesDataProductProcedure obj : mesDataProductProcedureList) {
            MesProcedureProperty mesProductProcedureNew = (MesProcedureProperty) BeanUtils.cloneBean(map.get(Long.valueOf(obj.getProcedurePropertyId())));
//            MesProcedureProperty mesProductProcedureNew = map.get(Long.valueOf(obj.getProcedurePropertyId()));
            mesProductProcedureNew.setCheckValue(obj.getMetaValue());
            mesProductProcedureNew.setCheckTime(String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp()));
            mesProcedurePropertyList = mesProcedurePropertyMap.get(mesProductProcedureNew.getId());

            if(null == mesProcedurePropertyList) {
                mesProcedurePropertyList =  Lists.newArrayList();
            }
//            outInerList.add(mesProductProcedureNew);
            mesProcedurePropertyList.add(mesProductProcedureNew);
            mesProcedurePropertyMap.put(mesProductProcedureNew.getId(), mesProcedurePropertyList);
        }
        for(Long id : mesProcedurePropertyMap.keySet()) {
            outList.add(mesProcedurePropertyMap.get(id));
        }
//        outList.add(outInerList);
        return outList;
//
//        MesProductProcedure mesProductProcedure = (MesProductProcedure) BeanUtils.cloneBean(map.get(mesDataProductProcedureList.get(0).getProductProcedureId()));
//
//        MesProductProcedure mesProductProcedureNew = (MesProductProcedure)BeanUtils.cloneBean(mesProductProcedure);
/*
	List<List<MesProcedureProperty>> procedureList = hTemplate.execute(HbaseUtil.HBASE_TABLE_PROCEDURE, new TableCallback<List<List<MesProcedureProperty>>>() {
		@Override
		public List<List<MesProcedureProperty>> doInTable(HTableInterface table) throws Throwable {
			Scan scan = new Scan();
			AnalyzeSearch analyzeSearch = propertyTrendSearch;
			// 产品ID
			MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());
			String productId = String.valueOf(mesProduct.getModelnum());
			// 工序ID
			String produceId = String.valueOf(analyzeSearch.getProductProcedureId());
			// 时间区间确定
			Date begin = analyzeSearch.getBegin();
			Date end = analyzeSearch.getEnd();
			try {
				if (null != begin && null != end) {
					scan.setTimeRange(begin.getTime(), end.getTime());
				} else if (null == end && null != begin) {
					Date curretTime = new Date();
					scan.setTimeRange(begin.getTime(), curretTime.getTime());
				} else if (null == begin && null != end) {
					scan.setTimeRange(Long.valueOf("0"), end.getTime());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<Filter> filters = new ArrayList<>();
			// rowKey前匹配
			Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(
					"^" + "" + ".*_.*" + "_.*_" + productId + "_" + produceId + "_.*"));
			filters.add(filter2);

			FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
			scan.setFilter(filterList);
			ResultScanner rscanner = table.getScanner(scan);
			List<List<MesProcedureProperty>> outList = Lists.newArrayList();
			for (Result result : rscanner) {
				List<MesProcedureProperty> list = null;
				List<Cell> listCells = result.listCells();
				if(listCells != null && listCells.size() > 0){
					for(Cell cell : listCells){
						String quali = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
								cell.getQualifierLength());
						if(map.containsKey(Long.parseLong(quali.split("_")[2]))) {
							MesProcedureProperty mesProcedureProperty = (MesProcedureProperty) BeanUtils
									.cloneBean(map.get(Long.parseLong(quali.split("_")[2])));
							mesProcedureProperty.setCheckValue(quali.split("_")[3]);
							String checkTime = String.valueOf(cell.getTimestamp());
							if (checkTime.length() < 13) {
								int length = checkTime.length();
								for (int i = 0; i < 13 - length; i++) {
									checkTime += "0";
								}
							}
							mesProcedureProperty.setCheckTime(
									DateUtils.getYYYYMMDDHHMMSSDayStr(new Date(Long.parseLong(checkTime))));
							if(null == list) {
								list = Lists.newArrayList();
							}
							list.add(mesProcedureProperty);
						}
					}
					if(null != list) {
						outList.add(list);
					}
				}
			}
			return outList;
		}
	});*/
//	procedureList.removeAll(Collections.singleton(null));
//return null;
//	return procedureList;
	}
	private void getRecordScan(HBasePageModel hBasePageModel , boolean flag, Map<String, String> searchMap){
		Date start = hBasePageModel.getAnalyzeSearch().getBegin();
		if(null != start) {
		    searchMap.put("stattime", String.valueOf(start.getTime()));
		}
		// String start_rowkey = null != start ? String.valueOf(start.getTime()).substring(0,10) : "";
        Date end = hBasePageModel.getAnalyzeSearch().getEnd();
        if (null != end) {
            searchMap.put("endtime", String.valueOf(end.getTime()));
        }
		/// String stop_rowkey = null != end ? String.valueOf(end.getTime()).substring(0,10) :  "";
		MesProductline mesProductline = mesProductlineDao.findOne(hBasePageModel.getAnalyzeSearch().getProductLineId());
	   // 工厂ID
        searchMap.put("factoryId", String.valueOf(mesProductline.getCompanyinfo().getId()));
		// 产线ID
		searchMap.put("productLineId", String.valueOf(hBasePageModel.getAnalyzeSearch().getProductLineId()));
		MesProductProcedure mesProductProcedure = mesProductProcedureDao.findOne(hBasePageModel.getAnalyzeSearch().getProductProcedureId());
		searchMap.put("productProcedureId", String.valueOf(hBasePageModel.getAnalyzeSearch().getProductProcedureId()));
		MesProduct mesProduct = mesProductDao.findOne(hBasePageModel.getAnalyzeSearch().getProductId());
		// 工件号
		searchMap.put("productBsn", String.valueOf(hBasePageModel.getAnalyzeSearch().getProductId()));
		String productSn = hBasePageModel.getAnalyzeSearch().getProductionSn();
		// String columnConditionRegix = "";
		// String rowCondition = "^";
		//String valueCondition = "";
		Map<Long ,MesProductProcedure> procedureMap = new HashMap<>();
		Map<Long ,MesProcedureProperty> procedurePropertyMap = new HashMap<>();
		Map<Long ,MesProductline> productLineMap = new HashMap<>();
		Map<String ,MesProduct> productMap = new HashMap<>();
		boolean isEmpty = true;
		if(null != productSn && !"".equals(productSn)){
			isEmpty = false;
//			rowCondition += "((?!:).)+:.*"+productSn+".*?(?=\\b)";
			/*if(null != mesProduct){
				valueCondition += "^"+mesProduct.getModelnum() + ":.*" + productSn + ".*?(?=\\b):.+$";
			}*/
		}else{
//			rowCondition += "((?!:).)+:((?!:).)+";
		}
		if(null != mesProductline){
			isEmpty = false;
//			rowCondition += ":"+mesProductline.getId();
			productLineMap.put(mesProductline.getId(), mesProductline);
		}else{
//			rowCondition += ":((?!:).)+";
		}
		if(null != mesProductProcedure){
			isEmpty = false;
//			rowCondition += ":"+mesProductProcedure.getId();
			procedureMap.put(mesProductProcedure.getId(), mesProductProcedure);
			for(MesProcedureProperty mesProcedureProperty : mesProductProcedure.getMesProcedureProperties()){
				procedurePropertyMap.put(mesProcedureProperty.getId(), mesProcedureProperty);

			}
		}else{
//			rowCondition += ":((?!:).)+";
		}
//		rowCondition += "$";
		if(null != mesProduct){
			isEmpty = false;
			productMap.put(mesProduct.getModelnum(), mesProduct);
//			valueCondition += "^"+mesProduct.getModelnum() + ":.+$,";
			if(procedureMap.isEmpty()){
				for(MesProductProcedure _mesProductProcedure : mesProduct.getMesProductProcedures()){
					procedureMap.put(_mesProductProcedure.getId(), _mesProductProcedure);
					for(MesProcedureProperty mesProcedureProperty : _mesProductProcedure.getMesProcedureProperties()){
						procedurePropertyMap.put(mesProcedureProperty.getId(), mesProcedureProperty);
					}
				}
			}
		}
		if(procedureMap.isEmpty()){
			for(MesProduct _mesProduct : this.getProductByCurrentCompanyId()){
				productMap.put(_mesProduct.getModelnum(), _mesProduct);
				for(MesProductProcedure _mesProductProcedure : _mesProduct.getMesProductProcedures()){
					procedureMap.put(_mesProductProcedure.getId(), _mesProductProcedure);
					for(MesProcedureProperty mesProcedureProperty : _mesProductProcedure.getMesProcedureProperties()){
						procedurePropertyMap.put(mesProcedureProperty.getId(), mesProcedureProperty);
					}
				}
			}
		}
		if(productLineMap.isEmpty()){
			for(MesProductline productline : this.getProductionLineByCurrentCompanyId()){
				productLineMap.put(productline.getId(), productline);
			}
		}
		if(isEmpty){
//			rowCondition = "";
			for(MesProductline productline : this.getProductionLineByCurrentCompanyId()){
//				rowCondition += "^((?!:).)+:((?!:).)+:"+productline.getId()+":((?!0).)+$,";
			}
		}
		hBasePageModel.setProcedureMap(procedureMap);
		hBasePageModel.setProcedurePropertyMap(procedurePropertyMap);
		hBasePageModel.setProductLineMap(productLineMap);
		hBasePageModel.setProductMap(productMap);

//		return HbaseUtil.getScanByColoumsPageable(start_rowkey, stop_rowkey,rowCondition, columnConditionRegix,valueCondition, hBasePageModel,flag);
	}

	public List<MesProductProcedure> getProcedureRecordList(HBasePageModel hBasePageModel,boolean flag) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException{
		System.out.println("start------"+System.currentTimeMillis());
		List<MesProductProcedure> list = new ArrayList<>();
//        Map<String, String> searchMap = Maps.newHashMap();
//        Scan scan = getRecordScan(hBasePageModel, flag, searchMap).getScan();
        // getRecordScan(hBasePageModel, flag, searchMap);
        Map<String, String> paramMap =  MesDataRowkeyUtil.getValMapByProcedureDataRowKey(hBasePageModel.getRowKey());
        List<SearchFilter> searchList = Lists.newArrayList();

        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, paramMap.get("factoryId")));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, paramMap.get("productLineId")));
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, paramMap.get("driverId")));
        searchList.add(new SearchFilter("mesDataMultiKey.pointId", Operator.EQ, paramMap.get("pointId")));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.EQ, Long.valueOf(paramMap.get("insertTimestamp"))));
        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
        List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findAll(specification);
        
        MesProductProcedure mesProductProcedureNew = mesProductProcedureService.findById(Long.valueOf(paramMap.get("productProcedureId")));

        if(hBasePageModel.getAnalyzeSearch().getSearchType() == 1) {
//            MesProductProcedure mesProductProcedure = hBasePageModel.getProcedureMap()
//            .get(hBasePageModel.getAnalyzeSearch().getProductProcedureId());
//            MesProductProcedure mesProductProcedureNew = (MesProductProcedure)BeanUtils.cloneBean(mesProductProcedure);
            mesProductProcedureNew.setRowKey(hBasePageModel.getRowKey());
            mesProductProcedureNew.setProcessCharacteristicBtn("<a style='cursor:pointer'>过程特性</a>");
            mesProductProcedureNew.setProcessControlBtn("<a style='cursor:pointer'>过程控制</a>");
            mesProductProcedureNew.setSn(mesDataProductProcedureList.get(0).getProductBsn());
            list.add(mesProductProcedureNew);
        } else {
            
        }

        /*
		if(hBasePageModel.getAnalyzeSearch().getSearchType() == 1 && null!= hBasePageModel.getRowKey() && !hBasePageModel.getRowKey().equals("")){
			MesProductProcedure procedure = hTemplate.get(HbaseUtil.HBASE_TABLE_PROCEDURE, hBasePageModel.getRowKey(), new RowMapper<MesProductProcedure>() {
				@Override
				public MesProductProcedure mapRow(Result result, int arg1) throws Exception {
					// 获取ROWKEY
					String row = Bytes.toString( result.getRow());
					// 获取列
					MesProductProcedure mesProductProcedure = hBasePageModel.getProcedureMap()
							.get(hBasePageModel.getAnalyzeSearch().getProductProcedureId());
					MesProductProcedure mesProductProcedureNew = null;
					if(null != mesProductProcedure){
						mesProductProcedureNew = (MesProductProcedure)BeanUtils.cloneBean(mesProductProcedure);
						mesProductProcedureNew.setRowKey(row);
						mesProductProcedureNew.setProcessCharacteristicBtn("<a style='cursor:pointer'>过程特性</a>");
						mesProductProcedureNew.setProcessControlBtn("<a style='cursor:pointer'>过程控制</a>");
						mesProductProcedureNew.setSn(row.split("_")[5]);
					}
					return mesProductProcedureNew;
				}

			});
			if(null != procedure){
				list.add(procedure);
			}
		}else{
			 list = hTemplate.find(HbaseUtil.TABLE_NAME, scan, new RowMapper<MesProductProcedure>() {
				@Override
				public MesProductProcedure mapRow(Result result, int arg1) throws Exception {
					String row = Bytes.toString( result.getRow());
					String procedureId = row.split(":")[3];
					MesProductProcedure mesProductProcedure = hBasePageModel.getProcedureMap().get(Long.parseLong(procedureId));
					MesProductProcedure mesProductProcedureNew = null;
					if(null != mesProductProcedure){
						mesProductProcedureNew = (MesProductProcedure)BeanUtils.cloneBean(mesProductProcedure);
						mesProductProcedureNew.setRowKey(row);
						mesProductProcedureNew.setProcessCharacteristicBtn("<a style='cursor:pointer'>过程特性</a>");
						mesProductProcedureNew.setProcessControlBtn("<a style='cursor:pointer'>过程控制</a>");
						mesProductProcedureNew.setSn(row.split(":")[1]);
					}
					return mesProductProcedureNew;
				}
			});
			System.out.println("stop------"+System.currentTimeMillis());
			if(list.size() != 0){
				list.removeAll(Collections.singleton(null));
			}
		} */
		return list;
	}
	public List<MesProcedureProperty> getPropertyRecordList(HBasePageModel hBasePageModel){
		long procedureId = hBasePageModel.getAnalyzeSearch().getProductProcedureId();
		// String productSn = hBasePageModel.getAnalyzeSearch().getProductionSn();
		MesProductProcedure mesProductProcedure = mesProductProcedureDao.findOne(procedureId);
		Map<Long ,MesProcedureProperty> propertyMap = new HashMap<>();
		for(MesProcedureProperty mesProcedureProperty : mesProductProcedure.getMesProcedureProperties()){
			propertyMap.put(mesProcedureProperty.getId(), mesProcedureProperty);
		}
		mesProductProcedure.setMesProcedureProperties(new ArrayList<MesProcedureProperty>());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String rowCondition = "";
//		if(null != productSn){
//			rowCondition = "^.+:.*"+productSn+".*?(?=\\b):\\w+:"+procedureId+"$";
//		}
		String mulKey = hBasePageModel.getRowKey();
        String mainkey[] = null;
		if(StringUtils.isNotBlank(mulKey)) {
		    mainkey = mulKey.split("_");
		} else {
		    return Lists.newArrayList();
		}
		Map<String, String> paramMap =  MesDataRowkeyUtil.getValMapByProcedureDataRowKey(hBasePageModel.getRowKey());
        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, paramMap.get("factoryId")));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, paramMap.get("productLineId")));
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, paramMap.get("driverId")));
//        searchList.add(new SearchFilter("mesDataMultiKey.pointId", Operator.EQ, paramMap.get("pointId")));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.EQ, Long.valueOf(paramMap.get("insertTimestamp"))));
        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
        List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findAll(specification);
        MesProcedureProperty mesProcedureProperty = new MesProcedureProperty();
        for(MesDataProductProcedure obj : mesDataProductProcedureList) {
            mesProcedureProperty = propertyMap.get(Long.valueOf(obj.getProcedurePropertyId()));
            mesProductProcedure.getMesProcedureProperties().add(mesProcedureProperty);
        }

//        MesDataProductProcedure outdataprocedure = mesDataProductProcedureList!=null ? mesDataProductProcedureList.get(0) : null;
//        MesProcedureProperty mesProcedureProperty = propertyMap.get(Long.valueOf(outdataprocedure.getProductProcedureId()));
//        mesProductProcedure.getMesProcedureProperties().add(mesProcedureProperty);
        /*
		hTemplate.get(HbaseUtil.HBASE_TABLE_PROCEDURE, hBasePageModel.getRowKey(), new RowMapper<MesProductProcedure>() {
		// hTemplate.get(HbaseUtil.TABLE_NAME, hBasePageModel.getRowKey(), new RowMapper<MesProductProcedure>() {
			@Override
			public MesProductProcedure mapRow(Result result, int arg1) throws Exception {

				List<Cell> ceList =   result.listCells(); 
				if(ceList!=null&&ceList.size()>0){  
					for(Cell cell:ceList){ 
						// String row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());  
						// String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
						// String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());  
						String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
//						String propertyId = quali.split(":")[1];
						String propertyId = String.valueOf(hBasePageModel.getAnalyzeSearch().getProductProcedureId());
						if(null != propertyId && !propertyId.equals("0")){
							MesProcedureProperty mesProcedureProperty = propertyMap.get(Long.valueOf(quali.split("_")[2]));
							mesProcedureProperty.setCheckValue(quali.split("_")[4]);
							try {
								// Date dtime = new Date(Long.parseLong(value.substring(value.lastIndexOf(":")+1)+"000"));
								Date dtime = new Date(cell.getTimestamp());
								mesProcedureProperty.setCheckTime(sdf.format(dtime));
							} catch (Exception e) {
								System.out.println("时间格式化错误！");
							}
							//mesProcedureProperty
							mesProductProcedure.getMesProcedureProperties().add(mesProcedureProperty);
						}
					}
				}
				return mesProductProcedure;
			}
		}); */
		//组装单位
		if(mesProductProcedure.getMesProcedureProperties().size()>0) {
			List<Dictionary> unitList = dictionaryService.findAll(new Specification<Dictionary>() {
				@Override
				public Predicate toPredicate(Root<Dictionary> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
					// TODO Auto-generated method stub
					Expression<Long> expression = root.get("parent").get("id").as(Long.class);
					Predicate predicate = builder.equal(expression, 90);
					return query.where(predicate).getRestriction();
				}
			});
			Map<Long,String> unitsMap = new HashMap<>();
			for (Dictionary d : unitList){
				unitsMap.put(d.getId(), d.getName());
			}
			for(MesProcedureProperty procedureProperty : mesProductProcedure.getMesProcedureProperties()){
				MesPoints mesPoints = procedureProperty.getMesPoints();
				if(null != mesPoints){
					mesPoints.setUnits(unitsMap.get(mesPoints.getUnitsId()));
				}
			}
		}
		/*List<MesProductProcedure> list = hTemplate.find(HbaseUtil.TABLE_NAME, HbaseUtil.getScanByColoumsPageable("", "",rowCondition, "","", hBasePageModel,false).getScan(), new RowMapper<MesProductProcedure>() {
			@Override
			public MesProductProcedure mapRow(Result result, int arg1) throws Exception {

				List<Cell> ceList =   result.listCells(); 
				if(ceList!=null&&ceList.size()>0){  
					for(Cell cell:ceList){ 
						String row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());  
						String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
						String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());  
						String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
						String propertyId = quali.split(":")[1];
						if(null != propertyId && !propertyId.equals("0")){
							MesProcedureProperty mesProcedureProperty = propertyMap.get(Long.parseLong(propertyId));
							mesProcedureProperty.setCheckValue(value.split(":")[3]);
							mesProductProcedure.getMesProcedureProperties().add(mesProcedureProperty);
						}
						//String row = Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
						String checkValue = _value.split(":")[3];
						String propertyId = quali.split(":")[1];
						MesProcedureProperty mesProcedureProperty = hBasePageModel.getProcedurePropertyMap().get(Long.parseLong(propertyId));
						mesProcedureProperty.setCheckValue(checkValue);
					}
				}
				return mesProductProcedure;
			}
		});*/
		return mesProductProcedure.getMesProcedureProperties();
	}

	// 此方法无调用
    // 2018/05/16 单体soft重建
	/*
	public List<ProductionRecord> getRecordList(HBasePageModel hBasePageModel , boolean flag){
		System.out.println("-----------1:"+System.currentTimeMillis());
		List<ProductionRecord> productionRecords = hTemplate.find(HbaseUtil.TABLE_NAME, getRecordScan(hBasePageModel, flag).getScan(), new RowMapper<ProductionRecord>(){
			@Override
			public ProductionRecord mapRow(Result result, int arg1) throws Exception {
				ProductionRecord productionRecord = new ProductionRecord();
				if(flag){
					/*List<Cell> ceList =   result.listCells(); 
					Cell cell = ceList.get(0);
					String value = Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()); 
					String productModelNo = value.split(":")[0];
					String row = Bytes.toString( result.getRow());
					String timeStamp = row.substring(0, 10)+"000";
					productionRecord.setTime(DateUtils.getYYYYMMDDHHMMSSDayStr(new Date(Long.parseLong(timeStamp))));
					MesProductline mesProductline = hBasePageModel.getProductLineMap().get(Long.valueOf(row.split(":")[2]));
					if(mesProductline != null){
						productionRecord.setProductLine(mesProductline.getLinename());
					}else{
						System.out.println("没有产线的row:"+row);
					}
					//MesProductProcedure mesProductProcedure = hBasePageModel.getProcedureMap().get(Long.valueOf(row.split(":")[3]));
					String productionSn = row.split(":")[1];
					productionRecord.setProductSn(productionSn);
					productionRecord.setProductName(hBasePageModel.getProductMap().get(productModelNo).getName());
					productionRecord.setProductId(hBasePageModel.getProductMap().get(productModelNo).getId());
					productionRecord.setRowKey(row);
					if(ceList!=null&&ceList.size()>0){  
						boolean productionStatus = true;
						for(Cell _cell:ceList){ 
							String _value = Bytes.toString( _cell.getValueArray(), _cell.getValueOffset(), _cell.getValueLength());  
							String quali = Bytes.toString( _cell.getQualifierArray(),_cell.getQualifierOffset(),_cell.getQualifierLength());
							if(quali.split(":")[3].equals(MesPointType.TYPE_PRODUCT_PROCEDURE) && !quali.split(":")[4].equals("0")){
								productionStatus = false;
							}
							//String row = Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
							String checkValue = _value.split(":")[3];
							String propertyId = quali.split(":")[1];
							MesProcedureProperty mesProcedureProperty = hBasePageModel.getProcedurePropertyMap().get(Long.parseLong(propertyId));
							mesProcedureProperty.setCheckValue(checkValue);
						}
						if(productionStatus){
							productionRecord.setStatus("OK");
							//goodMap.put("good", goodMap.get("good") + 1);
						}else{
							//badMap.put("bad", badMap.get("bad") + 1);
						}

					}
				}

				//productionRecords.add(productionRecord);
				return productionRecord;
			}

		});
		System.out.println("-----------2:"+System.currentTimeMillis());
		//hBasePageModel.setGoodMap(goodMap);
		//hBasePageModel.setBadMap(badMap);
		return productionRecords;
	}*/

	/**
	 * 通过HBase查询数据
	 * HbaseUtil是一个查询工具，封装了一些查询filter
	 * @param analyzeSearch
	 * @return
	 */
	public String analyzeDataSearch(AnalyzeSearch analyzeSearch) {

		Date start = analyzeSearch.getBegin();
//		String start_rowkey = null != start ? String.valueOf(start.getTime()).substring(0,10) : "";
		Date end = analyzeSearch.getEnd();
//		String stop_rowkey = null != end ? String.valueOf(end.getTime()).substring(0,10) : "";
		MesDriver mesDriver = mesDriverDao.findOne(analyzeSearch.getMesDriverId());
		MesProductline mesProductline = mesDriver.getMesProductline();
		analyzeSearch.setProductLineId(mesProductline.getId());
		MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(analyzeSearch.getProcedurePropertyId());
		String pointKey = mesProcedureProperty.getMesPoints().getCodekey();
		MesProductProcedure productProcedure = mesProcedureProperty.getMesProductProcedure();
		MesProduct mesProduct = productProcedure.getMesProduct();
//		HBasePageModel basePageModel = new HBasePageModel();
//		basePageModel.setPagable(false);
//		basePageModel.setAnalyzeSearch(analyzeSearch);
//		List<ProductionRecord> productionRecords = productionSchedulerPageSearch.getProductionRecordPagable(basePageModel);
//		List<String> rowKeys = new ArrayList<>();
//		for(ProductionRecord productionRecord : productionRecords){
//			String rk = productionRecord.getRowkey();
//			rowKeys.add(rk);
//		}
		// List<Result> datas = HBaseUtilNew.getDatasFromHbase(TABLE_NAME, FAMILY_NAME, rowKeys, null, false, false);
        System.out.println("生产记录----工厂ID:" + mesProductline.getCompanyinfo().getId());
        System.out.println("生产记录----产线ID:" + mesProductline.getId());
        System.out.println("生产记录----产品ID:" + Long.valueOf(mesProduct.getModelnum()));
        System.out.println("生产记录----设备ID:" + mesDriver.getId());
        System.out.println("生产记录----工序ID:" + productProcedure.getId());
        System.out.println("生产记录----start:" + start);
        System.out.println("生产记录----end:" + end);

        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, String.valueOf(mesProductline.getCompanyinfo().getId())));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, String.valueOf(mesProductline.getId())));
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(mesDriver.getId())));
		searchList.add(new SearchFilter("mesDataMultiKey.pointId", Operator.EQ, pointKey));
		if(null != start)
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(start.getTime()).replace("000", "")));
        if(null != end)
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(end.getTime()).replace("000", "")));
        searchList.add(new SearchFilter("productProcedureId", Operator.EQ, productProcedure.getId()));
        searchList.add(new SearchFilter("productMode", Operator.EQ, mesProduct.getModelnum()));
        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
        List<MesDataProductProcedure> mesDataProductProcedrureList = mesDataProductProcedureService.findAll(specification);
        List<CgAnalyzeData> list = new ArrayList<>();
        for(MesDataProductProcedure obj : mesDataProductProcedrureList) {
            CgAnalyzeData cgAnalyzeData = new CgAnalyzeData();
            cgAnalyzeData.setRowKey(MesDataRowkeyUtil.getRowKey(obj));
            cgAnalyzeData.setMesDriverName(mesDriver.getName());
            cgAnalyzeData.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
            cgAnalyzeData.setProductionSn(obj.getProductBsn());
            cgAnalyzeData.setProductName(mesProduct.getName());
            cgAnalyzeData.setProductProcedureName(productProcedure.getProcedurename());
            String time = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp().longValue());
            if(time.length() < 11) {
                time = time + "000";
            }
            cgAnalyzeData.setTime(DateUtils.unixTimestampToDate(Long.valueOf(time)));
            cgAnalyzeData.setValue(obj.getMetaValue());
            list.add(cgAnalyzeData);
        
        }

        
        
//		Boolean timeLimitFlg = false;
//		List<Result> datas  = new  HbaseUtil().getResultListByHbase(mesProductline.getCompanyinfo().getId(), mesProductline.getId(),
//				mesDriver.getId(), Long.valueOf(mesProduct.getModelnum()), productProcedure.getId(), null, start, end, timeLimitFlg);
//		List<CgAnalyzeData> list = new ArrayList<>();
//		if(datas.size()>0){
//			for (Result result : datas) {
//				if(result.isEmpty()){
//					continue;
//				}
//				List<Cell> ceList =   result.listCells(); 
//				CgAnalyzeData cgAnalyzeData = new CgAnalyzeData();
//				for(Cell cell : ceList){
//					String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
//					// if(quali.indexOf(MesPointType.TYPE_PRODUCT_PROCEDURE) == -1 || quali.indexOf(pointKey) == -1){
//					if(quali.indexOf(pointKey) == -1){
//						continue;
//					}
//					String row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());  
//					String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
//					String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
//					String qf = Bytes.toString(cell.getQualifierArray(),
//							cell.getQualifierOffset(), cell.getQualifierLength());
//					System.out.println(row+"============"+value+"============"+family);
//					cgAnalyzeData.setRowKey(row);
//					cgAnalyzeData.setMesDriverName(mesDriver.getName());
//					cgAnalyzeData.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
//					cgAnalyzeData.setProductionSn(row.split("_")[6]);
//					// cgAnalyzeData.setProductionSn(value.split(":")[1]);
//					cgAnalyzeData.setProductName(mesProduct.getName());
//					cgAnalyzeData.setProductProcedureName(productProcedure.getProcedurename());
//					cgAnalyzeData.setTime(DateUtils.getYYYYMMDDHHMMSSDayStr(new Date(cell.getTimestamp())));
//					// cgAnalyzeData.setValue(value.split(":")[3]);
//					cgAnalyzeData.setValue(qf.split("_")[3]);
//					list.add(cgAnalyzeData);
//				}
//				
//				
//			}
//		}
		
		
		Map<String,Object> map = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		/*
		
		
		
		
		
		
		
		MesProcedurePropertyPointLog log = mesProcedurePropertyPointLogDao.findLastedLogByPropertyId(mesProcedureProperty.getId());
		
		//List<ProductionRecord> list = productionSchedulerPageSearch.getProductionRecordPagable(hBasePageModel);
		
		
		String rowCondition = "";
		String columnCondition = "";
		String params = analyzeSearch.getProductionSn();
		if(mesProcedurePropertyPointLogs.size() != 0){
			for(MesProcedurePropertyPointLog log : mesProcedurePropertyPointLogs){
		if(null != log && null != start && log.getCreatedate().after(start)){
			start_rowkey = String.valueOf(log.getCreatedate().getTime()).substring(0,10);
			MesPoints mesPoints = mesPointsDao.findOne(log.getPointid());
			if(null != mesPoints){
				if(null != params && !params.equals("")){
					rowCondition += "^\\w+"+mesPoints.getMesPointGateway().getMac()+":.*"+params+".*?(?=\\b):\\w+:"+mesProcedureProperty.getMesProductProcedure().getId()+"$,";
				}else{
					rowCondition += "^\\w+"+mesPoints.getMesPointGateway().getMac() + ":((?!:).)+:\\w+:"+mesProcedureProperty.getMesProductProcedure().getId()+"$,";
				}
				columnCondition += "^"+mesPoints.getCodekey() + ":" + mesProcedureProperty.getId() + ":" + mesDriver.getId() + ":"+ MesPointType.TYPE_PRODUCT_PROCEDURE +":((?!:).)+$,";
			}
		}else{
			if(null != params && !params.equals("")){
				rowCondition += "^((?!:).)+:.*"+params+".*?(?=\\b):\\w+:"+mesProcedureProperty.getMesProductProcedure().getId()+"$,";
			}
			columnCondition += "^((?!:).)+:" + mesProcedureProperty.getId() + ":" + mesDriver.getId() + ":"+ MesPointType.TYPE_PRODUCT_PROCEDURE +":((?!:).)+$,";
		}

		}
		}
		//String columnCondition = analyzeSearch.getProductionSn();
		int index =  columnCondition.lastIndexOf(",");
		int _index =  rowCondition.lastIndexOf(",");
		columnCondition = columnCondition.substring(0,index != -1 ? index : columnCondition.length());
		rowCondition = rowCondition.substring(0,_index != -1 ? _index : rowCondition.length());
		int count = 0;
		long startSearch = System.currentTimeMillis();
		System.out.println("startSearch:"+startSearch);
		List<CgAnalyzeData> list = hTemplate.find(TABLE_NAME, HbaseUtil.getResultScanner(start_rowkey, stop_rowkey, rowCondition, columnCondition,params,2000), new RowMapper<CgAnalyzeData>() {
			@Override
			public CgAnalyzeData mapRow(Result result, int arg1) throws Exception {
				//String tmp =Bytes.toString(result.getValue(HbaseUtil.FAMILY_NAME.getBytes(), _params.getBytes()));
				List<Cell> ceList =   result.listCells(); 
				CgAnalyzeData cgAnalyzeData = new CgAnalyzeData();
				if(ceList!=null&&ceList.size() == 1){  
					Cell cell = ceList.get(0) ;
					String row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());  
					String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
					String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());  
					String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
					cgAnalyzeData.setMesDriverName(mesDriver.getName());
					cgAnalyzeData.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
					cgAnalyzeData.setProductionSn(value.split(":")[1]);
					cgAnalyzeData.setProductName(mesProcedureProperty.getMesProductProcedure().getMesProduct().getName());
					cgAnalyzeData.setProductProcedureName(mesProcedureProperty.getMesProductProcedure().getProcedurename());
					cgAnalyzeData.setTime(DateUtils.getYYYYMMDDHHMMSSDayStr(new Date(Long.valueOf(row.substring(0, 10)))));
					cgAnalyzeData.setValue(value.split(":")[3]);

				}  
				//tmp = Bytes.toString( tmp.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
				return cgAnalyzeData;
			}

		});*/
//		long endSearch = System.currentTimeMillis();
		//System.out.println("endSearch:"+endSearch);
		//System.out.println("execute:"+(endSearch-startSearch));
//		list.sort(new Comparator<CgAnalyzeData>() {
//			@Override
//			public int compare(CgAnalyzeData o1, CgAnalyzeData o2) {
//				if(DateUtils.parse(o1.getTime()).before(DateUtils.parse(o2.getTime()))){
//					return 1;
//				}
//				return -1;
//			}
//		});
//		int id = 1;
//		for(CgAnalyzeData cgAnalyzeData : list){
//			cgAnalyzeData.setId(id);
//			id++;
//		}
//		List<CgAnalyzeData> outNew = Lists.newArrayList();
//		for(CgAnalyzeData cgAnalyzeData : list){
//		    if("sn1013-1521449749".equals(cgAnalyzeData.getProductionSn())
//		       || "sn1013-1521289683".equals(cgAnalyzeData.getProductionSn())
//		       ) {
//		        continue;
//		    }
//		    outNew.add(cgAnalyzeData);
//		}
//		list = outNew;
		/**
		 * spc筛选条件
		 */
		if(analyzeSearch.getSubSeq() != -1){
			if(analyzeSearch.getSubSeq() == 0){ 
				int size = analyzeSearch.getSubNum()*analyzeSearch.getSubRange();
//				list = list.subList(0, size < list.size() ? size : list.size());
				int index_size = (list.size() - size) <0 ? 0: (list.size() - size);
				list = list.subList(index_size, size < list.size() ? list.size() : size);
			}else{
				List<CgAnalyzeData> newList = new ArrayList<>();
				newList.add(list.get(0));
				for(int i = 1; i < list.size(); i++){
					long time1 = DateUtils.parse(list.get(i).getTime()).getTime();
					long time2 = DateUtils.parse(newList.get(newList.size() - 1).getTime()).getTime();
					BigDecimal bigDecimal1 = new BigDecimal(time1);
					BigDecimal bigDecimal2 = new BigDecimal(time2);
					if(bigDecimal2.subtract(bigDecimal1).longValue() >= (analyzeSearch.getSubSeq() * 60 * 60 * 1000)){
						newList.add(list.get(i));
					}
					if(newList.size() == analyzeSearch.getSubNum()*analyzeSearch.getSubRange()){
						break;
					}
				}
				list = newList;
			}
		}

		Collections.sort(list, new Comparator<CgAnalyzeData>(){
			public int compare(CgAnalyzeData p1, CgAnalyzeData p2) {
				if(DateUtils.parse(p1.getTime()).before(DateUtils.parse(p2.getTime()))){
					return 1;
				}
				if(DateUtils.parse(p1.getTime()).equals(DateUtils.parse(p2.getTime()))){
					return 0;
				}

				return -1;
			}
		});

		Page page = new Page();
		int size = list.size();
		if(list.size() > 5000){
			List<CgAnalyzeData> subList = list.subList(0, 5000);
			map.put("cgAnalyzeData", subList);
			size = subList.size();
		}else{
			map.put("cgAnalyzeData", list);
			size = list.size();
		}
		page.setTotalCount(size);
		page.setTotalPage(1);
		page.setNumPerPage(size);
		//map.put("cgAnalyzeData", list);
		map.put("page", page);

		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public String analyseGrrData(GrrAnalyzeSearch grrAnalyzeSearch){
		JSONObject jsonObject1 = new JSONObject(grrAnalyzeSearch);
		System.out.println(jsonObject1.toString());
		JSONObject K1 = new JSONObject(k1);
		JSONObject K2 = new JSONObject(k2);
		JSONObject K3 = new JSONObject(k3);
		MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(grrAnalyzeSearch.getProcedurePropertyId());
		List<CgAnalyzeData> analyzeDatas = grrAnalyzeSearch.getDataList();
		/*	Map<String,List<CgAnalyzeData>> resultMap = new HashMap<>();
		for(CgAnalyzeData analyzeData : analyzeDatas){  
			if(resultMap.containsKey(analyzeData.getProductionSn())){  
				resultMap.get(analyzeData.getProductionSn()).add(analyzeData);  
			}else{
				List<CgAnalyzeData> tmpList = new ArrayList<CgAnalyzeData>();  
				tmpList.add(analyzeData);  
				tmpList.sort(new Comparator<CgAnalyzeData>() {
					@Override
					public int compare(CgAnalyzeData o1, CgAnalyzeData o2) {
						return DateUtils.parse(o1.getTime()).before(DateUtils.parse(o2.getTime())) ? -1 : 1;
					}
				});
				resultMap.put(analyzeData.getProductionSn(), tmpList);  
			}  
		}  */
		List<Double> listValues = new ArrayList<>();
		for(CgAnalyzeData analyzeData : analyzeDatas){
			listValues.add(Double.valueOf(analyzeData.getValue()));
		}
		/*for (Entry<String,List<CgAnalyzeData>> entry : resultMap.entrySet()) {
			for(CgAnalyzeData analyzeData : entry.getValue()){
				listValues.add(Double.valueOf(analyzeData.getValue()));
			}
		}*/
		Map<String,Map<Integer,List<Double>>> tableMap = new ConcurrentHashMap<>();
		//按照工件进行分组，有多少组工件，分多少组
		//List<List<Double>> _list_workpiece = groupListByQuantity(listValues,grrAnalyzeSearch.getPersonNum() * grrAnalyzeSearch.getCheckNum());//按照人分组
		//数据分成grrAnalyzeSearch.getPersonNum()块
		//按照检测人员数量，初始化tableMap
		/*	for(int i = 0; i < grrAnalyzeSearch.getPersonNum(); i++){
			Map<Integer,List<Double>> column_map = new ConcurrentHashMap<>();
			tableMap.put("检测员"+(i+1), column_map);
		}*/
		if(grrAnalyzeSearch.getAnalyseGrrType() == 0){
			List<List<Double>> _list_workpiece = groupListByQuantity(listValues,grrAnalyzeSearch.getWorkpieceNum()*grrAnalyzeSearch.getCheckNum());//按照人分组
			for (int j = 0; j < _list_workpiece.size(); j++) {
				List<Double> list = _list_workpiece.get(j);//第i组工件
				//对第i组工件分组，按照检测次数分组，即每个人每个工件检查的次数
				List<List<Double>> countList = groupListByQuantity(list,grrAnalyzeSearch.getWorkpieceNum());
				Map<Integer,List<Double>> _map = new ConcurrentHashMap<>();
				for (int k = 0; k < countList.size(); k++) {
					//第i组工件的第j次检查
					List<Double> _list = countList.get(k);
					_map.put(k, _list);
				}
				tableMap.put("检测员"+(j+1), _map);
			}
		}else{
			List<List<Double>> _list_workpiece = groupListByQuantity(listValues,grrAnalyzeSearch.getWorkpieceNum()*grrAnalyzeSearch.getPersonNum());//按照人分组
			for (int j = 0; j < _list_workpiece.size(); j++) {
				List<Double> list = _list_workpiece.get(j);//第i组工件
				//按照人数分组
				List<List<Double>> countList = groupListByQuantity(list,grrAnalyzeSearch.getWorkpieceNum());
				for (int k = 0; k < countList.size(); k++) {
					List<Double> _list = countList.get(k);
					if(!tableMap.containsKey("检测员"+(k+1))){
						Map<Integer,List<Double>> _map = new ConcurrentHashMap<>();
						tableMap.put("检测员"+(k+1), _map);
					}
					tableMap.get("检测员"+(k+1)).put(j, _list);	
				}

			}
		}
		List<Map.Entry<String, Map<Integer,List<Double>>>> infoIds =
				new ArrayList<Map.Entry<String, Map<Integer,List<Double>>>>(tableMap.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Map<Integer,List<Double>>>>() {   
			public int compare(Map.Entry<String, Map<Integer,List<Double>>> o1, Map.Entry<String, Map<Integer,List<Double>>> o2) {      
				//return (o2.getValue() - o1.getValue()); 
				String key1 = o1.getKey().split("检测员")[1];
				String key2 = o2.getKey().split("检测员")[1];
				try {
					return Double.parseDouble(key1) < Double.parseDouble(key2) ? -1 : 1;
				} catch (NumberFormatException e) {
					return o1.getKey().compareTo(o2.getKey());
				}
			}
		});
		Map<String,Map<String,Double>> partAppraiserAverageMap = new ConcurrentHashMap<>();
		Map<String,Map<String,Double>> partAppraiserAverageCompareMap = new ConcurrentHashMap<>();
		Map<String,Map<String,Double>> repeatAbilityRangeMap  = new ConcurrentHashMap<>();
		Map<String,Map<String,Double>> repeatAbilityRangeCompareMap  = new ConcurrentHashMap<>();
		List<Double> partAppraiserAverageList = new ArrayList<>();
		List<Double> repeatAbilityRangeList = new ArrayList<>();
		for(Entry<String, Map<Integer, List<Double>>> entry : infoIds){
			String key = entry.getKey();
			/*}
		for(String key : tableMap.keySet()){*/
			Map<Integer,List<Double>> map = tableMap.get(key);
			List<Double> xBarList = new ArrayList<>();
			List<Double> rList = new ArrayList<>();
			for(int i = 0; i < grrAnalyzeSearch.getWorkpieceNum(); i++){
				List<Double> tmp = new ArrayList<>();
				for(int j = 0; j < grrAnalyzeSearch.getCheckNum(); j++){
					List<Double> columnList = map.get(j);
					tmp.add(columnList.get(i));
				}
				Double[] tmp_averArray = new Double[tmp.size()];
				//rList.add(new BigDecimal(Double.toString(Collections.max(tmp))).subtract(new BigDecimal(Double.toString(Collections.min(tmp)))).doubleValue());
				rList.add(DecimalCalculate.sub(Collections.max(tmp), Collections.min(tmp)));
				xBarList.add(DecimalCalculate.getAverage(grrAnalyzeSearch.getScale(),tmp.toArray(tmp_averArray)));
			}
			for(int j = 0; j < xBarList.size(); j++){
				if(!partAppraiserAverageCompareMap.containsKey(String.valueOf(j+1))){
					Map<String,Double> _map = new ConcurrentHashMap<>();
					partAppraiserAverageCompareMap.put(String.valueOf(j+1), _map);
				}
				partAppraiserAverageCompareMap.get(String.valueOf(j+1)).put(key, xBarList.get(j));
			}
			for(int j = 0; j < rList.size(); j++){
				if(!repeatAbilityRangeCompareMap.containsKey(String.valueOf(j+1))){
					Map<String,Double> _map = new ConcurrentHashMap<>();
					repeatAbilityRangeCompareMap.put(String.valueOf(j+1), _map);
				}
				repeatAbilityRangeCompareMap.get(String.valueOf(j+1)).put(key, rList.get(j));
			}
			map.put(grrAnalyzeSearch.getCheckNum(), xBarList);
			partAppraiserAverageList.addAll(xBarList);
			repeatAbilityRangeList.addAll(rList);
			map.put(grrAnalyzeSearch.getCheckNum()+1, rList);
		}
		for(int i = 0; i < partAppraiserAverageList.size(); i++){
			Map<String,Double> averageMap = new ConcurrentHashMap<>();
			averageMap.put("测量均值", partAppraiserAverageList.get(i));
			partAppraiserAverageMap.put(String.valueOf(i+1), averageMap);
		}
		for(int i = 0; i < repeatAbilityRangeList.size(); i++){
			Map<String,Double> averageMap = new ConcurrentHashMap<>();
			averageMap.put("极差值", repeatAbilityRangeList.get(i));
			repeatAbilityRangeMap.put(String.valueOf(i+1), averageMap);
		}
		Double[] personXBARArray = new Double[tableMap.size()];
		Double[] personRArray = new Double[tableMap.size()];
		Double[] RArray = new Double[tableMap.size()];
		int personArrayIndex = 0;
		/**
		 * 这个Map用来存放横向的测试值
		 */
		Map<Integer,List<Double>> RMap = new HashMap<>();
		double averagexBarSingle = 0D;
		double averageSingle = 0D;
		Map<String,String> extraMap = new HashMap<>();
		for(Entry<String,Map<Integer,List<Double>>> entry : tableMap.entrySet()){
			String personName = entry.getKey();
			Map<Integer,List<Double>> checkContent = entry.getValue();
			for(int i = 0; i < grrAnalyzeSearch.getCheckNum(); i++){
				List<Double> columnList = checkContent.get(i);
				for(int j = 0; j < columnList.size(); j++){
					if(!RMap.containsKey(j)){
						List<Double> list = new ArrayList<>();
						RMap.put(j, list);
					}
					RMap.get(j).add(columnList.get(j));
				}
			}
			List<Double> columnxBarList = checkContent.get(grrAnalyzeSearch.getCheckNum());//获取XBar列
			List<Double> columnRList = checkContent.get(grrAnalyzeSearch.getCheckNum()+1);//获取R列
			Double[] columnxBarArray = new Double[columnxBarList.size()];
			Double[] columnRArray = new Double[columnRList.size()];
			columnxBarList.toArray(columnxBarArray);
			columnRList.toArray(columnRArray);
			averagexBarSingle = DecimalCalculate.getAverage(10,columnxBarArray);
			averageSingle = DecimalCalculate.getAverage(10,columnRArray);

			//将科学计数法转化成普通
			BigDecimal B2 = new BigDecimal(averageSingle);
			BigDecimal averageSingle2 = BigDecimal.ZERO;
			averageSingle2 = averageSingle2.add(B2.setScale(grrAnalyzeSearch.getScale(),RoundingMode.FLOOR));

			extraMap.put(personName, averagexBarSingle+"-"+averageSingle2);
			personXBARArray[personArrayIndex] = averagexBarSingle;
			personRArray[personArrayIndex] = averageSingle;
			//RArray[personArrayIndex] = new BigDecimal(Double.toString(Collections.max(columnRList))).subtract(new BigDecimal(Double.toString(Collections.min(columnRList)))).doubleValue();
			RArray[personArrayIndex] = DecimalCalculate.sub(Collections.max(columnRList), Collections.min(columnRList));
			personArrayIndex++;
		}
		List<Double> personXBARList = Arrays.asList(personXBARArray);
		List<Double> personRList = Arrays.asList(personRArray);
		/**
		 * 显示参数
		 */
		JSONObject jsonObject = new JSONObject();
		Map<String,Map<Integer,List<String>>> rowMap = new HashMap<>();
		for(Entry<String,Map<Integer,List<Double>>> entry : tableMap.entrySet()){
			String key = entry.getKey();
			Map<Integer,List<Double>> checkContent = entry.getValue();
			Map<Integer,List<String>> rMap = new HashMap<>();
			List<String> listTitle = new ArrayList<>();
			listTitle.add("XBAR");
			listTitle.add("R");
			rMap.put(0, listTitle);
			for(int i = 0; i < checkContent.size(); i++){
				List<Double> columnList = checkContent.get(i);
				String title = "";
				if(checkContent.size() - 2 > i){
					switch (i) {
					case 0:
						title = "一";
						break;
					case 1:
						title = "二";
						break;
					case 2:
						title = "三";
						break;
					default:
						break;
					}
				}
				if(!title.equals("")){
					rMap.get(0).add(i,title);
				}
				for(int j = 0; j < columnList.size(); j++){
					if(!rMap.containsKey(j + 1)){
						List<String> list = new ArrayList<>();
						rMap.put(j + 1, list);
					}
					rMap.get(j + 1).add(String.valueOf(columnList.get(j)));
				}
			}
			List<String> extraList = new ArrayList<>();
			for(int m = 0; m < grrAnalyzeSearch.getCheckNum(); m++){
				if(m == 0){
					extraList.add(String.valueOf(m));
				}else{
					extraList.add(null);
				}
			}
			extraList.add(String.valueOf(DecimalCalculate.round(Double.parseDouble(extraMap.get(key).split("-")[0]), grrAnalyzeSearch.getScale())));
			extraList.add(String.valueOf(DecimalCalculate.round(Double.parseDouble(extraMap.get(key).split("-")[1]), grrAnalyzeSearch.getScale())));
			rMap.put(Collections.max(rMap.keySet())+1, extraList);
			rowMap.put(key, rMap);
		}
		//jsonObject.put("tableMap", rowMap);
		/**/
		SortedMap<String, Map<Integer, List<String>>>  sortedMap = new TreeMap<>(rowMap);
		jsonObject.put("tableMap", sortedMap);
		try {
			jsonObject.put("partAppraiserAverageMap", new LineOption().data(partAppraiserAverageMap));
			jsonObject.put("partAppraiserAverageCompareMap", new LineOption().data(partAppraiserAverageCompareMap));
			jsonObject.put("repeatAbilityRangeMap", new LineOption().data(repeatAbilityRangeMap));
			jsonObject.put("repeatAbilityRangeCompareMap", new LineOption().data(repeatAbilityRangeCompareMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
		double XBAR = DecimalCalculate.getAverage(10,personXBARArray);
		jsonObject.put("XBAR", DecimalCalculate.round(XBAR, grrAnalyzeSearch.getScale()));
		double RBAR = DecimalCalculate.getAverage(10,personRArray);
		jsonObject.put("RBAR", DecimalCalculate.round(RBAR, grrAnalyzeSearch.getScale()));
		double Xdiff = DecimalCalculate.sub(Collections.max(personXBARList), Collections.min(personXBARList));
		jsonObject.put("Xdiff", Xdiff);
		Map<Integer,Map<String,Double>> template = getMesSpcTemplate();
		double A2 = template.get(grrAnalyzeSearch.getCheckNum()).get("A2");
		double D4 = template.get(grrAnalyzeSearch.getCheckNum()).get("D4");
		//Xbar管制图的管制上限
		double UCLx = DecimalCalculate.add(XBAR,DecimalCalculate.mul(A2, DecimalCalculate.getAverage(10,RBAR)));
		jsonObject.put("UCLx", DecimalCalculate.round(UCLx, grrAnalyzeSearch.getScale()));
		//Xbar管制图的管制下限
		double LCLx = DecimalCalculate.sub(XBAR,DecimalCalculate.mul(A2, DecimalCalculate.getAverage(10,RBAR)));
		jsonObject.put("LCLx", DecimalCalculate.round(LCLx,grrAnalyzeSearch.getScale()));
		double UCLr = DecimalCalculate.mul(D4, RBAR, grrAnalyzeSearch.getScale());
		jsonObject.put("UCLr", UCLr);
		double EV = DecimalCalculate.mul(RBAR, K1.getDouble(String.valueOf(grrAnalyzeSearch.getCheckNum())));
		jsonObject.put("EV", DecimalCalculate.round(EV, grrAnalyzeSearch.getScale()));
		double multiplier = DecimalCalculate.mul(Xdiff, K2.getDouble(String.valueOf(grrAnalyzeSearch.getPersonNum())));
		double EV2 = DecimalCalculate.div(DecimalCalculate.pow(EV, 2),DecimalCalculate.mul(grrAnalyzeSearch.getCheckNum(), grrAnalyzeSearch.getWorkpieceNum()) , 10);
		double _AV = DecimalCalculate.sub(DecimalCalculate.pow(multiplier, 2), EV2);
		double AV = 0;
		if(_AV >= 0){
			AV = DecimalCalculate.sqrt(_AV, 10);
		}
		jsonObject.put("AV", DecimalCalculate.round(AV,grrAnalyzeSearch.getScale()));
		List<Double> RpList = new ArrayList<>();
		for (Entry<Integer,List<Double>> entry : RMap.entrySet()) {
			List<Double> list = entry.getValue();
			Double[] tmp = new Double[list.size()];
			RpList.add(DecimalCalculate.getAverage(10,list.toArray(tmp)));
		}
		double Rp = DecimalCalculate.sub(Collections.max(RpList), Collections.min(RpList));
		jsonObject.put("Rp", DecimalCalculate.round(Rp, grrAnalyzeSearch.getScale()));
		double PV = DecimalCalculate.mul(Rp, K3.getDouble(String.valueOf(grrAnalyzeSearch.getWorkpieceNum())),10);
		jsonObject.put("PV", DecimalCalculate.round(PV, grrAnalyzeSearch.getScale()));
		double Grr = DecimalCalculate.sqrt(DecimalCalculate.add(DecimalCalculate.pow(EV, 2), DecimalCalculate.pow(AV, 2)), 10);
		jsonObject.put("Grr", DecimalCalculate.round(Grr, grrAnalyzeSearch.getScale()));
		double TV = DecimalCalculate.sqrt(DecimalCalculate.add(DecimalCalculate.pow(Grr, 2), DecimalCalculate.pow(PV, 2)) , 10); 
		jsonObject.put("TV", DecimalCalculate.round(TV, grrAnalyzeSearch.getScale()));
		double percentEV = DecimalCalculate.round(DecimalCalculate.mul(100, DecimalCalculate.div(EV, TV)) ,  grrAnalyzeSearch.getScale()) ;
		jsonObject.put("percentEV", percentEV);
		double percentAV = DecimalCalculate.round(DecimalCalculate.mul(100, DecimalCalculate.div(AV, TV)) ,  grrAnalyzeSearch.getScale()) ;
		jsonObject.put("percentAV", percentAV);
		double percentGrr = DecimalCalculate.round(DecimalCalculate.mul(100, DecimalCalculate.div(Grr, TV)) ,  grrAnalyzeSearch.getScale());
		jsonObject.put("percentGrr", percentGrr);
		double percentPV = DecimalCalculate.round(DecimalCalculate.mul(100, DecimalCalculate.div(PV, TV)) ,  grrAnalyzeSearch.getScale());
		jsonObject.put("percentPV", percentPV);
		double ndc = DecimalCalculate.round(DecimalCalculate.mul(1.41, DecimalCalculate.mul(100, DecimalCalculate.div(PV, Grr))),grrAnalyzeSearch.getScale()) ;
		jsonObject.put("ndc", ndc);

		BigDecimal tmp = new BigDecimal(TV).multiply(new BigDecimal(TV).multiply(new BigDecimal("100")));
		double m_fContributionEv = DecimalCalculate.div(DecimalCalculate.pow(EV, 2), DecimalCalculate.pow(TV, 2), grrAnalyzeSearch.getScale()); 
		double m_fContributionAv = DecimalCalculate.div(DecimalCalculate.pow(AV, 2), DecimalCalculate.pow(TV, 2), grrAnalyzeSearch.getScale()); 
		double m_fContributionGrr = DecimalCalculate.div(DecimalCalculate.pow(Grr, 2), DecimalCalculate.pow(TV, 2), grrAnalyzeSearch.getScale()); 
		double m_fContributionPv = DecimalCalculate.div(DecimalCalculate.pow(PV, 2), DecimalCalculate.pow(TV, 2), grrAnalyzeSearch.getScale()); 
		double m_fContributionTv = DecimalCalculate.div(DecimalCalculate.pow(TV, 2), DecimalCalculate.pow(TV, 2), grrAnalyzeSearch.getScale()); ;

		double T = DecimalCalculate.sub(Double.valueOf(mesProcedureProperty.getUppervalues()), Double.valueOf(mesProcedureProperty.getLowervalues()));
		double m_fTolerenceEv = DecimalCalculate.div(DecimalCalculate.mul(6, EV), T);
		double m_fTolerenceAv = DecimalCalculate.div(DecimalCalculate.mul(6, AV), T);
		double m_fTolerenceGrr = DecimalCalculate.div(DecimalCalculate.mul(6, Grr), T);
		double m_fTolerencePv = DecimalCalculate.div(DecimalCalculate.mul(6, PV), T);
		double m_fTolerenceTv = DecimalCalculate.div(DecimalCalculate.mul(6, TV), T);
		Map<String,Map<String,Double>> variationMap = new ConcurrentHashMap<>();
		Map<String,Double> variationSubMap1 = new ConcurrentHashMap<>();

		double percent_m_fContributionEv1 = m_fTolerenceEv != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100, m_fContributionEv), m_fTolerenceEv,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fvariation1 =  m_fTolerenceEv != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100,DecimalCalculate.div(EV, TV)),m_fTolerenceEv,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fTolerenceEv1 = m_fTolerenceEv != 0 ? 100 : 0;

		double percent_m_fContributionEv2 = m_fTolerenceAv != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100, m_fContributionAv), m_fTolerenceAv,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fvariation2 = m_fTolerenceAv != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100,DecimalCalculate.div(AV, TV)),m_fTolerenceAv,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fTolerenceEv2 = m_fTolerenceAv != 0 ? 100 : 0;

		double percent_m_fContributionEv3 = m_fTolerenceGrr != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100, m_fContributionGrr), m_fTolerenceGrr,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fvariation3 = m_fTolerenceGrr != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100,DecimalCalculate.div(Grr, TV)),m_fTolerenceGrr,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fTolerenceEv3 = m_fTolerenceGrr != 0 ? 100 : 0;

		double percent_m_fContributionEv4 = m_fTolerencePv != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100, m_fContributionPv), m_fTolerencePv,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fvariation4 = m_fTolerencePv != 0 ? DecimalCalculate.div(DecimalCalculate.mul(100,DecimalCalculate.div(PV, TV)),m_fTolerencePv,grrAnalyzeSearch.getScale()) : 0;
		double percent_m_fTolerenceEv4 = m_fTolerencePv != 0 ? 100 : 0;


		variationSubMap1.put("贡献", percent_m_fContributionEv1);
		variationSubMap1.put("研究变异", percent_m_fvariation1);
		variationSubMap1.put("公差", percent_m_fTolerenceEv1);
		Map<String,Double> variationSubMap2 = new ConcurrentHashMap<>();
		variationSubMap2.put("贡献", percent_m_fContributionEv2);
		variationSubMap2.put("研究变异", percent_m_fvariation2);
		variationSubMap2.put("公差", percent_m_fTolerenceEv2);
		Map<String,Double> variationSubMap3 = new ConcurrentHashMap<>();
		variationSubMap3.put("贡献", percent_m_fContributionEv3);
		variationSubMap3.put("研究变异", percent_m_fvariation3);
		variationSubMap3.put("公差", percent_m_fTolerenceEv3);
		Map<String,Double> variationSubMap4 = new ConcurrentHashMap<>();
		variationSubMap4.put("贡献", percent_m_fContributionEv4);
		variationSubMap4.put("研究变异", percent_m_fvariation4);
		variationSubMap4.put("公差", percent_m_fTolerenceEv4);
		variationMap.put("重复性", variationSubMap1);
		variationMap.put("再现性", variationSubMap2);
		variationMap.put("量具R&R", variationSubMap3);
		variationMap.put("部件间", variationSubMap4);
		try {
			jsonObject.put("variationMap", new BarOption().data(variationMap));
		}catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("result", DecimalCalculate.mul(DecimalCalculate.div(Grr, TV,10), 100,grrAnalyzeSearch.getScale()));
		return jsonObject.toString();
	}
	public Map<Integer,Map<String,Double>> getMesSpcTemplate(){
		List<MesSpcTemplate> mesSpcTemplates = mesSpcTemplateDao.findAll();
		Map<Integer,Map<String,Double>> template = new HashMap<>();
		for (MesSpcTemplate mesSpcTemplate : mesSpcTemplates) {
			Class<MesSpcTemplate> clz = MesSpcTemplate.class;
			Field[] fields = clz.getDeclaredFields();
			Map<String,Double> map = new HashMap<>();
			try {
				template.put((Integer)clz.getDeclaredMethod("getCOLUMN").invoke(mesSpcTemplate), map);
				for(Field field : fields){
					if(field.getGenericType() == Double.class){
						map.put(field.getName(), (Double)clz.getDeclaredMethod("get"+field.getName()).invoke(mesSpcTemplate));
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return template;
	}

	/**
	 * 通过HBase查询数据
	 * HbaseUtil是一个查询工具，封装了一些查询filter
	 * @param analyzeSearch
	 * @return
	 */
	public Map<String, Object> spcAnalyzeDataSearch(AnalyzeSearch analyzeSearch) {

		Date start = analyzeSearch.getBegin();
//		String start_rowkey = null != start ? String.valueOf(start.getTime()).substring(0,10) : "";
		Date end = analyzeSearch.getEnd();
//		String stop_rowkey = null != end ? String.valueOf(end.getTime()).substring(0,10) : "";
		MesDriver mesDriver = mesDriverDao.findOne(analyzeSearch.getMesDriverId());
		MesProductline mesProductline = mesDriver.getMesProductline();
		analyzeSearch.setProductLineId(mesProductline.getId());
		MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(analyzeSearch.getProcedurePropertyId());
		String pointKey = mesProcedureProperty.getMesPoints().getCodekey();
		MesProductProcedure productProcedure = mesProcedureProperty.getMesProductProcedure();
		MesProduct mesProduct = productProcedure.getMesProduct();
		System.out.println("生产记录----工厂ID:" + mesProductline.getCompanyinfo().getId());
		System.out.println("生产记录----产线ID:" + mesProductline.getId());
		System.out.println("生产记录----产品ID:" + mesProduct.getModelnum());
		System.out.println("生产记录----设备ID:" + mesDriver.getId());
		System.out.println("生产记录----工序ID:" + productProcedure.getId());
		System.out.println("生产记录----start:" + start);
		System.out.println("生产记录----end:" + end);

		List<SearchFilter> searchList = Lists.newArrayList();
		searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, String.valueOf(mesProductline.getCompanyinfo().getId())));
		searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, String.valueOf(mesProductline.getId())));
		searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(mesDriver.getId())));
		searchList.add(new SearchFilter("mesDataMultiKey.pointId", Operator.EQ, pointKey));
		if(null != start)
			searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(start.getTime()).replace("000", "")));
		if(null != end)
			searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(end.getTime()).replace("000", "")));
		searchList.add(new SearchFilter("productProcedureId", Operator.EQ, productProcedure.getId()));
		searchList.add(new SearchFilter("productMode", Operator.EQ, mesProduct.getModelnum()));
		Specification<MesDataProductProcedure> specification = DynamicSpecifications
				.bySearchFilter(MesDataProductProcedure.class, searchList);
		List<MesDataProductProcedure> mesDataProductProcedrureList = mesDataProductProcedureService.findAll(specification);
		List<CgAnalyzeData> list = new ArrayList<>();
		for(MesDataProductProcedure obj : mesDataProductProcedrureList) {
			CgAnalyzeData cgAnalyzeData = new CgAnalyzeData();
			cgAnalyzeData.setRowKey(MesDataRowkeyUtil.getRowKey(obj));
			cgAnalyzeData.setMesDriverName(mesDriver.getName());
			cgAnalyzeData.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
			cgAnalyzeData.setProductionSn(obj.getProductBsn());
			cgAnalyzeData.setProductName(mesProduct.getName());
			cgAnalyzeData.setProductProcedureName(productProcedure.getProcedurename());
			String time = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp().longValue());
			if(time.length() < 11) {
				time = time + "000";
			}
			cgAnalyzeData.setTime(DateUtils.unixTimestampToDate(Long.valueOf(time)));
			cgAnalyzeData.setValue(obj.getMetaValue());
			list.add(cgAnalyzeData);

		}
		Map<String,Object> map = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		long endSearch = System.currentTimeMillis();
		//System.out.println("endSearch:"+endSearch);
		//System.out.println("execute:"+(endSearch-startSearch));
		try {
			list.sort(new Comparator<CgAnalyzeData>() {
				@Override
				public int compare(CgAnalyzeData o1, CgAnalyzeData o2) {
					if(DateUtils.parse(o1.getTime()).before(DateUtils.parse(o2.getTime()))){
						return 1;
					} else {
						return -1;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		int id = 1;
		for(CgAnalyzeData cgAnalyzeData : list){
			cgAnalyzeData.setId(id);
			id++;
		}
		List<CgAnalyzeData> outNew = Lists.newArrayList();
		for(CgAnalyzeData cgAnalyzeData : list){
			if("sn1013-1521449749".equals(cgAnalyzeData.getProductionSn())
					|| "sn1013-1521289683".equals(cgAnalyzeData.getProductionSn())
			) {
				continue;
			}
			outNew.add(cgAnalyzeData);
		}
		list = outNew;
		/**
		 * spc筛选条件
		 */
		if(analyzeSearch.getSubSeq() != -1){
			if(analyzeSearch.getSubSeq() == 0){
				int size = analyzeSearch.getSubNum()*analyzeSearch.getSubRange();
				list = list.subList(0, size < list.size() ? size : list.size());
			}else{
				List<CgAnalyzeData> newList = new ArrayList<>();
				newList.add(list.get(0));
				for(int i = 1; i < list.size(); i++){
					long time1 = DateUtils.parse(list.get(i).getTime()).getTime();
					long time2 = DateUtils.parse(newList.get(newList.size() - 1).getTime()).getTime();
					BigDecimal bigDecimal1 = new BigDecimal(time1);
					BigDecimal bigDecimal2 = new BigDecimal(time2);
					if(bigDecimal2.subtract(bigDecimal1).longValue() >= (analyzeSearch.getSubSeq() * 60 * 60 * 1000)){
						newList.add(list.get(i));
					}
					if(newList.size() == analyzeSearch.getSubNum()*analyzeSearch.getSubRange()){
						break;
					}
				}
				list = newList;
			}
		}
		Page page = new Page();
		int size = list.size();
		if(list.size() > 5000){
			List<CgAnalyzeData> subList = list.subList(0, 5000);
			map.put("cgAnalyzeData", subList);
			size = subList.size();
		}else{
			map.put("cgAnalyzeData", list);
			size = list.size();
		}
		page.setTotalCount(size);
		page.setTotalPage(1);
		page.setNumPerPage(size);
		//map.put("cgAnalyzeData", list);
		map.put("page", page);
		try {
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	public List<MonitorSpc> findPage(Specification<MonitorSpc> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MonitorSpc> springDataPage = monitorSpcDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	public Map<String,Object> spcMonitorDataSearch(AnalyzeSearch analyzeSearch) {
		Map<String, Object> map = new HashMap<>();
		List<MonitorSpc> list = monitorSpcDao.findBymonitorPainterIdAndChartId(analyzeSearch.getMonitorId(), analyzeSearch.getChartId());
//		List<MonitorSpc> list = monitorSpcDao.findBymonitorPainterIdAndChartId(analyzeSearch.getMonitorId());
		map.put("data",list);
		return map;
	}

	public String saveAnalyseSPCData(SpcAnalyzeSearch spcAnalyzeData) {
		Map<Integer,Map<String,Double>> template = getMesSpcTemplate();
		List<Double> datas = Lists.newArrayList();
		MesProduct mesProduct = mesProductDao.findOne(spcAnalyzeData.getProductId());
		// MQ联动式监控
		if("1".equals(spcAnalyzeData.getMonitorFlg()) && SecurityUtils.getShiroUser().getCompanyid() == 554L) {
			datas = analyzeDataSearchForMonitor(spcAnalyzeData, "1");
		}
		// 定时式监控
		//if("2".equals(spcAnalyzeData.getMonitorFlg()) && SecurityUtils.getShiroUser().getCompanyid() == 553L) {
		if("2".equals(spcAnalyzeData.getMonitorFlg())) {
			datas = analyzeDataSearchForMonitor(spcAnalyzeData, "2");
		}
		if(null == datas || 0 == datas.size()) {
			datas = spcAnalyzeData.getValues();
		}
		Double[] nums = new Double[datas.size()];
		double X_average = DecimalCalculate.getAverage(10,datas.toArray(nums));
		double sample_sigma = getSigma(10,0,datas.toArray(nums));//σ算法(样本)
		double standard_sigma = getSigma(10,1,datas.toArray(nums));//σ算法(样本)
		int subNum = spcAnalyzeData.getSubNum();//子组数量:一共subNum个组
		int subRange = spcAnalyzeData.getSubRange();//子组大小:每组一共subRange个数据
		List<List<Double>> groupList = this.groupListByQuantity(datas, subRange);

		Map<String,Map<String,Double>> XBarMap = new ConcurrentHashMap<>();
		Map<String,Map<String,Double>> RBarMap = new ConcurrentHashMap<>();

		/**
		 * 迭代每一个组，求组平均值
		 */
		double groupTotal = 0;//每组平均值和
		double RTotal = 0;//每组R值求和（极差求和）
		for(int i = 0; i < groupList.size(); i++){
			List<Double> doubleList = groupList.get(i);
			double r = DecimalCalculate.sub(Collections.max(doubleList), Collections.min(doubleList));
			Map<String,Double> _map = new ConcurrentHashMap<>();
			_map.put("rBar", r);
			RBarMap.put(i+"", _map);
			RTotal = DecimalCalculate.add(r, RTotal);
			Double[] groups_tmp = new Double[doubleList.size()];
			double average = DecimalCalculate.getAverage(10, doubleList.toArray(groups_tmp));
			Map<String,Double> map = new ConcurrentHashMap<>();
			map.put("xBarAverage", average);
			XBarMap.put(i+"", map);
			groupTotal = DecimalCalculate.add(average, groupTotal);
		}

		/**
		 * 计算参数
		 */
		MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(spcAnalyzeData.getProcedurePropertyId());
		double USL = DecimalCalculate.add(Double.valueOf(mesProcedureProperty.getStandardvalues()), Double.valueOf(mesProcedureProperty.getUppervalues()));//USL:标准值+上工差
		double LSL = DecimalCalculate.add(Double.valueOf(mesProcedureProperty.getStandardvalues()), Double.valueOf(mesProcedureProperty.getLowervalues()));//USL:标准值+下工差
		double Ppk1 = DecimalCalculate.div(DecimalCalculate.sub(USL, X_average),DecimalCalculate.mul(3, sample_sigma) ,10);
		double Ppk2 = DecimalCalculate.div(DecimalCalculate.sub(X_average, LSL),DecimalCalculate.mul(3, sample_sigma) ,10);
		double Cpk1 = DecimalCalculate.div(DecimalCalculate.sub(USL, X_average),DecimalCalculate.mul(3, standard_sigma) ,10);
		double Cpk2 = DecimalCalculate.div(DecimalCalculate.sub(X_average, LSL),DecimalCalculate.mul(3, standard_sigma) ,10);

		/**
		 * 计算显示值
		 */
		JSONObject jsonObject = new JSONObject();
		double Pp = DecimalCalculate.div(DecimalCalculate.sub(USL, LSL), DecimalCalculate.mul(6, sample_sigma),spcAnalyzeData.getScale());
		jsonObject.put("Pp", Pp);
		double Ppk = Ppk1 < Ppk2 ? Ppk1 : Ppk2;
		jsonObject.put("Ppk", DecimalCalculate.round(Ppk, spcAnalyzeData.getScale()));
		double Cp = DecimalCalculate.div(DecimalCalculate.sub(USL, LSL), DecimalCalculate.mul(6, standard_sigma),spcAnalyzeData.getScale());
		jsonObject.put("Cp", DecimalCalculate.round(Cp, spcAnalyzeData.getScale()));
		double Cpk = Cpk1 < Cpk2 ? Cpk1 : Cpk2;
		jsonObject.put("Cpk", DecimalCalculate.round(Cpk, spcAnalyzeData.getScale()));
		return jsonObject.toString();
	}

	public Map<String,Object> spcDataSearch(AnalyzeSearch analyzeSearch) {


		Date start = analyzeSearch.getBegin();
		Date end = analyzeSearch.getEnd();

		MesDriver mesDriver = mesDriverDao.findOne(analyzeSearch.getMesDriverId());
		MesProductline mesProductline = mesDriver.getMesProductline();
		analyzeSearch.setProductLineId(mesProductline.getId());
		MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(analyzeSearch.getProcedurePropertyId());
		String pointKey = mesProcedureProperty.getMesPoints().getCodekey();
		MesProductProcedure productProcedure = mesProcedureProperty.getMesProductProcedure();
		MesProduct mesProduct = productProcedure.getMesProduct();
		System.out.println("生产记录----工厂ID:" + mesProductline.getCompanyinfo().getId());
		System.out.println("生产记录----产线ID:" + mesProductline.getId());
//		System.out.println("生产记录----产品ID:" + Long.valueOf(mesProduct.getModelnum()));
		System.out.println("生产记录----设备ID:" + mesDriver.getId());
		System.out.println("生产记录----工序ID:" + productProcedure.getId());
		System.out.println("生产记录----start:" + start);
		System.out.println("生产记录----end:" + end);

		List<SearchFilter> searchList = Lists.newArrayList();
		searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, String.valueOf(mesProductline.getCompanyinfo().getId())));
		searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, String.valueOf(mesProductline.getId())));
		searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(mesDriver.getId())));
		searchList.add(new SearchFilter("mesDataMultiKey.pointId", Operator.EQ, pointKey));
		if(null != start)
			searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(start.getTime()).replace("000", "")));
		if(null != end)
			searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(end.getTime()).replace("000", "")));
		searchList.add(new SearchFilter("productProcedureId", Operator.EQ, productProcedure.getId()));
		searchList.add(new SearchFilter("productMode", Operator.EQ, mesProduct.getModelnum()));

		Specification<MesDataProductProcedure> specification = DynamicSpecifications
				.bySearchFilter(MesDataProductProcedure.class, searchList);
		List<MesDataProductProcedure> mesDataProductProcedrureList = mesDataProductProcedureService.findAll(specification);
		List<CgAnalyzeData> list = new ArrayList<>();
		for(MesDataProductProcedure obj : mesDataProductProcedrureList) {
			CgAnalyzeData cgAnalyzeData = new CgAnalyzeData();
			cgAnalyzeData.setRowKey(MesDataRowkeyUtil.getRowKey(obj));
			cgAnalyzeData.setMesDriverName(mesDriver.getName());
			cgAnalyzeData.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
			cgAnalyzeData.setProductionSn(obj.getProductBsn());
			cgAnalyzeData.setProductName(mesProduct.getName());
			cgAnalyzeData.setProductProcedureName(productProcedure.getProcedurename());
			String time = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp().longValue());
			if(time.length() < 11) {
				time = time + "000";
			}
			cgAnalyzeData.setTime(DateUtils.unixTimestampToDate(Long.valueOf(time)));
			cgAnalyzeData.setValue(obj.getMetaValue());
			list.add(cgAnalyzeData);

		}

		Map<String,Object> map = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		long endSearch = System.currentTimeMillis();
		//System.out.println("endSearch:"+endSearch);
		//System.out.println("execute:"+(endSearch-startSearch));
		list.sort(new Comparator<CgAnalyzeData>() {
			@Override
			public int compare(CgAnalyzeData o1, CgAnalyzeData o2) {
				if(DateUtils.parse(o1.getTime()).before(DateUtils.parse(o2.getTime()))){
					return 1;
				}
				return -1;
			}
		});
		int id = 1;
		for(CgAnalyzeData cgAnalyzeData : list){
			cgAnalyzeData.setId(id);
			id++;
		}
		List<CgAnalyzeData> outNew = Lists.newArrayList();
		for(CgAnalyzeData cgAnalyzeData : list){
			if("sn1013-1521449749".equals(cgAnalyzeData.getProductionSn())
					|| "sn1013-1521289683".equals(cgAnalyzeData.getProductionSn())
			) {
				continue;
			}
			outNew.add(cgAnalyzeData);
		}
		list = outNew;
		/**
		 * spc筛选条件
		 */
		if(analyzeSearch.getSubSeq() != -1){
			if(analyzeSearch.getSubSeq() == 0){
				int size = analyzeSearch.getSubNum()*analyzeSearch.getSubRange();
				list = list.subList(0, size < list.size() ? size : list.size());
			}else{
				List<CgAnalyzeData> newList = new ArrayList<>();
				newList.add(list.get(0));
				for(int i = 1; i < list.size(); i++){
					long time1 = DateUtils.parse(list.get(i).getTime()).getTime();
					long time2 = DateUtils.parse(newList.get(newList.size() - 1).getTime()).getTime();
					BigDecimal bigDecimal1 = new BigDecimal(time1);
					BigDecimal bigDecimal2 = new BigDecimal(time2);
					if(bigDecimal2.subtract(bigDecimal1).longValue() >= (analyzeSearch.getSubSeq() * 60 * 60 * 1000)){
						newList.add(list.get(i));
					}
					if(newList.size() == analyzeSearch.getSubNum()*analyzeSearch.getSubRange()){
						break;
					}
				}
				list = newList;
			}
		}
		Page page = new Page();
		int size = list.size();
		if(list.size() > 5000){
			List<CgAnalyzeData> subList = list.subList(0, 5000);
			map.put("cgAnalyzeData", subList);
			size = subList.size();
		}else{
			map.put("cgAnalyzeData", list);
			size = list.size();
		}
		page.setTotalCount(size);
		page.setTotalPage(1);
		page.setNumPerPage(size);
		//map.put("cgAnalyzeData", list);
		map.put("page", page);

		return map;
	}

	public String analyseSpcData(SpcAnalyzeSearch spcAnalyzeData) {
		Map<Integer,Map<String,Double>> template = getMesSpcTemplate();
		List<Double> datas = Lists.newArrayList();
		MesProduct mesProduct = mesProductDao.findOne(spcAnalyzeData.getProductId());
		// 555_272_438_58813116411_288_1520488453_sn1013-1
		// MQ联动式监控
		if("1".equals(spcAnalyzeData.getMonitorFlg()) && SecurityUtils.getShiroUser().getCompanyid() == 554L) {
		    System.out.println("监控型检索----------------" + new Date());
		    System.out.println("监控型检索----------------" + new Date());
		    System.out.println("监控型检索----------------" + new Date());
		    datas = analyzeDataSearchForMonitor(spcAnalyzeData, "1");
		}
		// 定时式监控
        //if("2".equals(spcAnalyzeData.getMonitorFlg()) && SecurityUtils.getShiroUser().getCompanyid() == 553L) {
        if("2".equals(spcAnalyzeData.getMonitorFlg())) {
            System.out.println("定时式监控----------------" + new Date());
            System.out.println("定时式监控----------------" + new Date());
            System.out.println("定时式监控----------------" + new Date());
            datas = analyzeDataSearchForMonitor(spcAnalyzeData, "2");
        }
		if(null == datas || 0 == datas.size()) {
            System.out.println("单次评定型检索----------------" + new Date());
            System.out.println("单次评定型检索----------------" + new Date());
            System.out.println("单次评定型检索----------------" + new Date());
		    datas = spcAnalyzeData.getValues();
		}
		Double[] nums = new Double[datas.size()];
		double X_average = DecimalCalculate.getAverage(10,datas.toArray(nums));
		double sample_sigma = getSigma(10,0,datas.toArray(nums));//σ算法(样本)
		double standard_sigma = getSigma(10,1,datas.toArray(nums));//σ算法(样本)
		int subNum = spcAnalyzeData.getSubNum();//子组数量:一共subNum个组
		int subRange = spcAnalyzeData.getSubRange();//子组大小:每组一共subRange个数据
		int subSeq = spcAnalyzeData.getSubSeq();//子组频率
		List<List<Double>> groupList = this.groupListByQuantity(datas, subRange);

		Map<String,Map<String,Double>> XBarMap = new ConcurrentHashMap<>();
		Map<String,Map<String,Double>> RBarMap = new ConcurrentHashMap<>();

		/**
		 * 迭代每一个组，求组平均值
		 */
		double groupTotal = 0;//每组平均值和
		double RTotal = 0;//每组R值求和（极差求和）
		for(int i = 0; i < groupList.size(); i++){
			List<Double> doubleList = groupList.get(i);
			double r = DecimalCalculate.sub(Collections.max(doubleList), Collections.min(doubleList));
			Map<String,Double> _map = new ConcurrentHashMap<>();
			_map.put("rBar", r);
			RBarMap.put(i+"", _map);
			RTotal = DecimalCalculate.add(r, RTotal);
			Double[] groups_tmp = new Double[doubleList.size()];
			double average = DecimalCalculate.getAverage(10, doubleList.toArray(groups_tmp));
			Map<String,Double> map = new ConcurrentHashMap<>();
			map.put("xBarAverage", average);
			XBarMap.put(i+"", map);
			groupTotal = DecimalCalculate.add(average, groupTotal);
		}
		double xBar = DecimalCalculate.div(groupTotal, subNum,  10);//各组X的平均值再平均
		double rBar = DecimalCalculate.div(RTotal, subNum, 10);//各组R的平均值

		/**
		 * 计算参数
		 */
		double A2 = template.get(subRange).get("A2");
		double D4 = template.get(subRange).get("D4");
		double D3 = template.get(subRange).get("D3");
		MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(spcAnalyzeData.getProcedurePropertyId());
		//		String USL = new BigDecimal(mesProcedureProperty.getStandardvalues()).add(new BigDecimal(mesProcedureProperty.getUppervalues())).toString();//USL:标准值+上工差
		double USL = DecimalCalculate.add(Double.valueOf(mesProcedureProperty.getStandardvalues()), Double.valueOf(mesProcedureProperty.getUppervalues()));//USL:标准值+上工差
		double LSL = DecimalCalculate.add(Double.valueOf(mesProcedureProperty.getStandardvalues()), Double.valueOf(mesProcedureProperty.getLowervalues()));//USL:标准值+下工差
		double SL = Double.valueOf(mesProcedureProperty.getStandardvalues());
		double Ppk1 = DecimalCalculate.div(DecimalCalculate.sub(USL, X_average),DecimalCalculate.mul(3, sample_sigma) ,10);
		double Ppk2 = DecimalCalculate.div(DecimalCalculate.sub(X_average, LSL),DecimalCalculate.mul(3, sample_sigma) ,10);
		double Cpk1 = DecimalCalculate.div(DecimalCalculate.sub(USL, X_average),DecimalCalculate.mul(3, standard_sigma) ,10);
		double Cpk2 = DecimalCalculate.div(DecimalCalculate.sub(X_average, LSL),DecimalCalculate.mul(3, standard_sigma) ,10);

		/**
		 * 计算显示值
		 */
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("xBarMap", new LineOption().data(XBarMap));
			jsonObject.put("RBarMap", new LineOption().data(RBarMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("Max", Collections.max(datas));
		jsonObject.put("Min", Collections.min(datas));
		try {
			jsonObject.put("maxValue", Double.parseDouble(mesProcedureProperty.getUppervalues()));
			jsonObject.put("minValue", Double.parseDouble(mesProcedureProperty.getLowervalues()));
			jsonObject.put("standardValue", Double.parseDouble(mesProcedureProperty.getStandardvalues()));
			jsonObject.put("max_minValue", new BigDecimal(mesProcedureProperty.getUppervalues()).subtract(new BigDecimal(mesProcedureProperty.getLowervalues())).doubleValue());
		} catch (NumberFormatException e) {
			throw new ServiceException("这个转换错误应该是由于该参数的名义值、公差等值包含有角度等因素造成的");
		}
		jsonObject.put("sigma", DecimalCalculate.round(sample_sigma, spcAnalyzeData.getScale()));
		jsonObject.put("X_average", DecimalCalculate.round(X_average, spcAnalyzeData.getScale()));
		jsonObject.put("rBar", DecimalCalculate.round(rBar, spcAnalyzeData.getScale()));
		jsonObject.put("subSeq", subSeq);
		jsonObject.put("subNum", subNum);
		double UCLx =DecimalCalculate.add(DecimalCalculate.mul(A2, rBar), xBar);//管制图的管制上限
		jsonObject.put("UCLx",  DecimalCalculate.round(UCLx, spcAnalyzeData.getScale()));
		double LCLx =DecimalCalculate.sub(xBar,DecimalCalculate.mul(A2, rBar));//管制图的管制下限
		jsonObject.put("LCLx", DecimalCalculate.round(LCLx, spcAnalyzeData.getScale()));
		double UCLr = DecimalCalculate.mul(D4,rBar);//R管制图的管制上限
		jsonObject.put("UCLr", DecimalCalculate.round(UCLr, spcAnalyzeData.getScale()));
		double LCLr = DecimalCalculate.mul(D3,rBar);//R管制图的管制下限
		jsonObject.put("LCLr", DecimalCalculate.round(LCLr, spcAnalyzeData.getScale()));
		double Pp = DecimalCalculate.div(DecimalCalculate.sub(USL, LSL), DecimalCalculate.mul(6, sample_sigma),spcAnalyzeData.getScale());
		jsonObject.put("Pp", Pp);
		double Ppk = Ppk1 < Ppk2 ? Ppk1 : Ppk2;
		jsonObject.put("Ppk", DecimalCalculate.round(Ppk, spcAnalyzeData.getScale()));
		double Cp = DecimalCalculate.div(DecimalCalculate.sub(USL, LSL), DecimalCalculate.mul(6, standard_sigma),spcAnalyzeData.getScale());
		jsonObject.put("Cp", DecimalCalculate.round(Cp, spcAnalyzeData.getScale()));
		double Cpk = Cpk1 < Cpk2 ? Cpk1 : Cpk2;
		jsonObject.put("Cpk", DecimalCalculate.round(Cpk, spcAnalyzeData.getScale()));
		String cpkLevel = "";
		if(Cpk >= 1.67){
			cpkLevel = "A+";
		}else if(Cpk < 1.67 && Cpk >= 1.33){
			cpkLevel = "A";
		}else if(Cpk < 1.33 && Cpk >= 1){
			cpkLevel = "B";
		}else if(Cpk < 1 && Cpk >= 0.67){
			cpkLevel = "C";
		}else if(Cpk < 0.67){
			cpkLevel = "D";
		}
		jsonObject.put("CpkLevel", cpkLevel);
		//制程准确度
		double Ca = DecimalCalculate.div(DecimalCalculate.sub(X_average, DecimalCalculate.div(DecimalCalculate.add(USL, LSL), 2)), DecimalCalculate.div(DecimalCalculate.sub(USL, LSL), 2),spcAnalyzeData.getScale());
		jsonObject.put("Ca", Ca);
		//下限过程能力指数
		double Cpl = DecimalCalculate.div(DecimalCalculate.sub(xBar, LSL), DecimalCalculate.mul(3, standard_sigma), spcAnalyzeData.getScale());
		jsonObject.put("Cpl", Cpl);
		//上限过程能力指数
		double Cpu = DecimalCalculate.div(DecimalCalculate.sub(USL, xBar), DecimalCalculate.mul(3, standard_sigma), spcAnalyzeData.getScale());
		jsonObject.put("Cpu", Cpu);

		jsonObject.put("LSL", LSL);
		jsonObject.put("USL", USL);
		jsonObject.put("SL", SL);

		jsonObject.put("datas", datas);
		
		// X图上下公差 DecimalCalculate.round(sample_sigma, spcAnalyzeData.getScale())
		jsonObject.put("xImageUcl",DecimalCalculate.round(DecimalCalculate.add(X_average, DecimalCalculate.mul(3, sample_sigma)), spcAnalyzeData.getScale()));
		jsonObject.put("xImageLcl",DecimalCalculate.round(DecimalCalculate.sub(X_average, DecimalCalculate.mul(3, sample_sigma)), spcAnalyzeData.getScale()));

        // XBar图上下公差
        jsonObject.put("xBarImageUcl",DecimalCalculate.round(DecimalCalculate.add(X_average, DecimalCalculate.mul(2, sample_sigma)), spcAnalyzeData.getScale()));
        jsonObject.put("xBarImageLcl",DecimalCalculate.round(DecimalCalculate.sub(X_average, DecimalCalculate.mul(2, sample_sigma)), spcAnalyzeData.getScale()));
		return jsonObject.toString();
	}
	/**
	 * 这个是一个分组算法
	 * @param list
	 * @param quantity
	 * @return
	 */
	private List<List<Double>> groupListByQuantity(List<Double> list, int quantity) {  
		if (list == null || list.size() == 0) {  
			return null;  
		}  

		if (quantity <= 0) {  
			new IllegalArgumentException("Wrong quantity.");  
		}  

		List<List<Double>> wrapList = new ArrayList<List<Double>>();  
		int count = 0;  
		while (count < list.size()) {  
			wrapList.add(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity));  
			count += quantity;  
		}  

		return wrapList;  
	}  
	public String analyseCgData(CgAnalyzeSearch cgAnalyzeData) {
		int scope = cgAnalyzeData.getStatisticalStandard();
		String upperValues = Double.toString(cgAnalyzeData.getMaxValue());
		String lowerValues = Double.toString(cgAnalyzeData.getMinValue());
		String T =  new BigDecimal(upperValues).subtract(new BigDecimal(lowerValues)).toString();
		List<Double> datas = cgAnalyzeData.getValues();
		Double[] nums = new Double[datas.size()];
		String Bi = new BigDecimal(Double.toString(getAverage(cgAnalyzeData.getScale(),datas.toArray(nums)))).subtract(new BigDecimal(Double.toString(cgAnalyzeData.getActualValue()))).toString();
		String sigma = Double.toString(getSigma(cgAnalyzeData.getScale(),0,datas.toArray(nums)));
		String Cg = "";
		String Cgk = "";
		String NG = "NG";
		if(scope == 4){
			Cg = new BigDecimal(new BigDecimal(T).multiply(new BigDecimal(0.1)).toString())
					.divide(new BigDecimal((new BigDecimal(2).multiply(new BigDecimal(sigma))).toString()), cgAnalyzeData.getScale(), RoundingMode.HALF_UP).toString();
			Cgk = new BigDecimal(new BigDecimal(T).multiply(new BigDecimal(0.1)).toString())
					.subtract(new BigDecimal(Math.abs(Double.valueOf(Bi)))).divide(new BigDecimal((new BigDecimal(2).multiply(new BigDecimal(sigma))).toString()), 4, RoundingMode.HALF_UP).toString();
		}else if(scope == 6){
			Cg = new BigDecimal(new BigDecimal(T).multiply(new BigDecimal(0.1)).toString())
					.divide(new BigDecimal((new BigDecimal(3).multiply(new BigDecimal(sigma))).toString()), cgAnalyzeData.getScale(), RoundingMode.HALF_UP).toString();
			Cgk = new BigDecimal(new BigDecimal(T).multiply(new BigDecimal(0.1)).toString())
					.subtract(new BigDecimal(Math.abs(Double.valueOf(Bi)))).divide(new BigDecimal((new BigDecimal(3).multiply(new BigDecimal(sigma))).toString()), 4, RoundingMode.HALF_UP).toString();
		}
		if(Double.valueOf(Cg) >= 1.33 && Double.valueOf(Cgk) >= 1.33){
			NG = "OK";
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Cg", Cg).put("Cgk", Cgk).put("NG", NG);
		return jsonObject.toString();
	}

	private double getAverage(int scale,Double... nums){
		double total = 0D;
		for(double num : nums){
			total = new BigDecimal(Double.toString(total)).add(new BigDecimal(Double.toString(num))).doubleValue();
		}
		return new BigDecimal(Double.toString(total)).divide(new BigDecimal(nums.length), scale, RoundingMode.HALF_UP).doubleValue();
	}
	private double getSigma(int scale,int type,Double... nums){
		double average = this.getAverage(scale,nums);
		double sum = 0D;
		for(double num : nums){

			double tmp = DecimalCalculate.sub(num, average);
			//double tmp = new BigDecimal(Double.toString(num)).subtract(new BigDecimal(Double.toString(average))).doubleValue();

			double _tmp = DecimalCalculate.pow(tmp, 2);
			//double _tmp = new BigDecimal(Double.toString(tmp)).multiply(new BigDecimal(Double.toString(tmp))).doubleValue();

			sum = DecimalCalculate.add(sum, _tmp);
			//sum = new BigDecimal(Double.toString(sum)).add(new BigDecimal(Double.toString(_tmp))).doubleValue();
		}
		int tmp = 0;
		switch (type) {
		case 0:
			tmp = nums.length - 1;
			break;
		default:
			tmp = nums.length;
			break;
		}
		double inValue = DecimalCalculate.div(sum, tmp, 10);
		//double inValue = new BigDecimal(Double.toString(sum)).divide(new BigDecimal(Double.toString(tmp)), 10, RoundingMode.HALF_UP).doubleValue();
		//	return new BigDecimal(Math.sqrt(inValue)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
		return DecimalCalculate.sqrt(inValue, 10);
	}

	// 此方法无法调用
	// 2018/05/16 单体soft重建
	/*
	public String getProductionRecordInfo(String id) {

		String result = this.hTemplate.get(HbaseUtil.TABLE_NAME, id, new RowMapper<String>() {
			@Override
			public String mapRow(Result result, int arg1) throws Exception {

				List<Cell> ceList =   result.listCells(); 
				if(ceList!=null&&ceList.size()>0){  
					String returnStr = "";
					for(Cell cell:ceList){  
						String row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());  
						String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
						String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());  
						String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
						String pointKey = quali.split(":")[0];
						String gateway = row.substring(12);
						String pointValue = value.split(":")[3];
						MesPoints mesPoints = mesPointsDao.findOne(new Specification<MesPoints>() {
							@Override
							public Predicate toPredicate(Root<MesPoints> root, CriteriaQuery<?> query,
									CriteriaBuilder builder) {
								Predicate p1 = builder.equal(root.get("codekey").as(String.class),pointKey);
								Predicate p2 = builder.equal(root.get("mesPointGateway").get("mac").as(String.class),gateway);
								return query.where(p1,p2).getRestriction();
							}
						});
						returnStr += mesPoints.getName() + ":" + pointValue + "<br>";
					}
					return returnStr;
				}
				return "";  
			}
		});


		if(null == result || result.equals("")){
			return "暂无数据!";
		}
		return result;
	}
*/
	public Map<Long,List<Object>> exportQdas(AnalyzeSearch analyzeSearch,Map<String,Map<String,String>> qdasMap,
			Long chooseProcedure) throws ClassNotFoundException {
//		String start_row = String.valueOf(analyzeSearch.getBegin().getTime()).substring(0, 10);
//		String stop_row = String.valueOf(analyzeSearch.getEnd().getTime()).substring(0, 10);
		//MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());

		MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());
		Long productId = StringUtils.isBlank(mesProduct.getModelnum()) ? null : Long.valueOf(mesProduct.getModelnum());
		Long facId = mesProduct.getCompanyinfo().getId();

		Long factoryId = null;
		factoryId = facId;
        for (MesProductline mesProductline : getProductionLineByCurrentCompanyId()) {
        	MesProductline line = mesProductlineDao.findOne(mesProductline.getId());
//    		List<MesProductline> mesProductlineList = getProductionLineByCurrentCompanyId(mesProductline.getId());
    		factoryId = line.getCompanyinfo().getId();
    		if(null == factoryId || 0L == factoryId) {
    			continue;
    		}
    		if(factoryId == facId) {
    			break;
    		}
        }

		// Companyinfo companyinfo = companyinfoDao.findOne(SecurityUtils.getShiroUser().getCompanyid());
		Map<String,MesPoints> mesPointsMap = new HashMap<>();
		Map<Long,MesProcedureProperty> mesProcedurePropertyMap = new HashMap<>();
		Map<Long,MesProductline> mesProductLineMap = new HashMap<>();
		//获所选择工序
		MesProductProcedure currentProcedure = mesProductProcedureService.findById(chooseProcedure);
		if(null == currentProcedure){
			return new HashMap<>();
		}
		List<MesProcedureProperty> currentProcedureProperties = currentProcedure.getMesProcedureProperties();
		if(null == currentProcedureProperties || currentProcedureProperties.size()<1){
			return new HashMap<>();
		}
		//获取加工该工序的设备
		MesDriver driver = (null == currentProcedureProperties.get(0).getMesPoints() ? 
				new MesDriver() : currentProcedureProperties.get(0).getMesPoints().getCurrentMesDriver());
		//获取该工序有关的数据
		for(MesProcedureProperty mesProcedureProperty : currentProcedureProperties){
			mesProcedurePropertyMap.put(mesProcedureProperty.getId(), mesProcedureProperty);
			MesPoints currentPoint = mesProcedureProperty.getMesPoints();
			if(null != currentPoint){
				mesPointsMap.put(currentPoint.getMesPointGateway().getMac()+":"+currentPoint.getCodekey(), currentPoint);
				if(null != currentPoint.getCurrentMesProductline()){
					mesProductLineMap.put(currentPoint.getCurrentMesProductline().getId(), currentPoint.getCurrentMesProductline());
				}
			}
		}

        List<SearchFilter> searchList = Lists.newArrayList();
//        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, String.valueOf(factoryId)));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(analyzeSearch.getBegin().getTime()).replace("000", "")));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(analyzeSearch.getEnd().getTime()).replace("000", "")));
        searchList.add(new SearchFilter("productProcedureId", Operator.EQ, currentProcedure.getId()));
        searchList.add(new SearchFilter("productMode", Operator.EQ, productId));
        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
        List<MesDataProductProcedure> mesDataProductProcedrureList = mesDataProductProcedureService.findAll(specification);
        List<Map<String,String>> list = Lists.newArrayList();
        for(MesDataProductProcedure obj : mesDataProductProcedrureList) {
            Map<String,String> map = new HashMap<>();
            map.put(obj.getMacAddr()+":"+obj.getMesDataMultiKey().getPointId()+
                    "-"+obj.getProductProcedureId()+
                    "-"+obj.getMesDataMultiKey().getProductLineId() +
                    "-"+obj.getProcedurePropertyId()
                    ,obj.getValueStatus()+"-"+obj.getMesDataMultiKey().getInsertTimestamp());
            list.add(map);
        }


//		List<Result> rsResultList = new HbaseUtil().getResultListByHbase(factoryId, null, null, productId, currentProcedure.getId(),
//				null, analyzeSearch.getBegin(), analyzeSearch.getEnd(), false);
//		for (Result result : rsResultList) {
//			List<Cell> ceList =   result.listCells(); 
//			Map<String,String> map = new HashMap<>();
//			if(ceList!=null&&ceList.size()>0){  
//				for(Cell cell:ceList){ 
//					String row = Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
//					String value = Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
//					String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
//					// String gateway = row.split(":")[0].substring(12);
//					String gateway = value.split("_")[4];
//					String productLineId = row.split("_")[1];
//					String pointKey = quali.split("_")[1];
//					String propertyId = quali.split("_")[2];
//					// map.put(gateway+":"+pointKey+"-"+propertyId+"-"+productLineId, value.split(":")[3]+"-"+value.substring(value.lastIndexOf(":")+1));
//					map.put(gateway+":"+pointKey+"-"+propertyId+"-"+productLineId, quali.split("_")[3]+"-"+row.split("_")[5]);
//				}
//			}  
//			list.add(map);
//		}
		
		Map<Long,List<Object>> resultMap = new HashMap<>();
		//该工序所属设备的数据map
		Map<String,String> mesDriverDataMap = new HashMap<>();
		mesDriverDataMap = generateExportQdasData(driver, qdasMap);
		//遍历数据集
		for(Map<String,String> map : list){
			//List<Object> resultList = new ArrayList<>();
			for(String key : map.keySet()){ 
				List<Map<String,String>> list_ = new ArrayList<>();
//				MesPoints mesPoints =  mesPointsMap.get(key.split("-")[0]+"-"+key.split("-")[1]+"-"+key.split("-")[2]+"-"+key.split("-")[3]+"-"+key.split("-")[4]+"-"+key.split("-")[5]);
				MesPoints mesPoints =  mesPointsMap.get(key.split(":")[0] + ":" + key.split(":")[1].split("-")[0]);
				if(mesPoints==null){
					continue;
				}
				Map<String,String> mesPointsDataMap = generateExportQdasData(mesPoints,qdasMap);
				if(!mesPointsDataMap.isEmpty()){
					list_.add(mesPointsDataMap);
				}
				MesPointGateway mesPointGateway = mesPoints.getMesPointGateway();
				Map<String,String> mesPointGatewayDataMap = generateExportQdasData(mesPointGateway,qdasMap);
				if(!mesPointGatewayDataMap.isEmpty()){
					list_.add(mesPointGatewayDataMap);
				}
				MesPointType mesPointType = mesPoints.getMesPointType();
				Map<String,String> mesPointTypeDataMap = generateExportQdasData(mesPointType,qdasMap);
				if(!mesPointTypeDataMap.isEmpty()){
					list_.add(mesPointTypeDataMap);
				}

				MesProcedureProperty mesProcedureProperty = mesProcedurePropertyMap.get(Long.parseLong(key.split(":")[1].split("-")[3]));
				if(null == mesProcedureProperty){
					continue;
				}
				Map<String,String> mesProcedurePropertyDataMap = new HashMap<>();
				Map<String,String> mesProductProcedureDataMap = new HashMap<>();
				Map<String,String> mesProductDataMap = new HashMap<>();
				Map<String,String> companyinfoDataMap = new HashMap<>();
				Map<String,String> mesProductlineDataMap = new HashMap<>();
				String _value = map.get(key);
				mesProcedureProperty.setCheckValue(_value.split("-")[0]);
				String checkTime = _value.split("-")[1];
				if(checkTime.length() < 13){
					int length = checkTime.length();
					for(int i = 0; i < 13 - length; i++){
						checkTime += "0";
					}
				}
				mesProcedureProperty.setCheckTime(DateUtils.getYYYYMMDDHHMMSSDayStr(new Date(Long.parseLong(checkTime))));
				mesProcedurePropertyDataMap = generateExportQdasData(mesProcedureProperty,qdasMap);
				if(!mesProcedurePropertyDataMap.isEmpty()){
					list_.add(mesProcedurePropertyDataMap);
				}
				MesProductProcedure mesProductProcedure = mesProcedureProperty.getMesProductProcedure();
				if(!resultMap.containsKey(mesProductProcedure.getId())){
					List<Object> resultList = new ArrayList<>();
					resultMap.put(mesProductProcedure.getId(), resultList);
				}

				mesProductProcedureDataMap = generateExportQdasData(mesProductProcedure,qdasMap);
				if(!mesProductProcedureDataMap.isEmpty()){
					list_.add(mesProductProcedureDataMap);
				}
				MesProduct mesProduct2 = mesProductProcedure.getMesProduct();
				mesProductDataMap = generateExportQdasData(mesProduct2,qdasMap);
				if(!mesProductDataMap.isEmpty()){
					list_.add(mesProductDataMap);
				}
				Companyinfo companyinfo2 = mesProduct2.getCompanyinfo();
				companyinfoDataMap = generateExportQdasData(companyinfo2,qdasMap);
				if(!companyinfoDataMap.isEmpty()){
					list_.add(companyinfoDataMap);
				}
				MesProductline mesProductline = mesProductLineMap.get(Long.parseLong(key.split("-")[7]));
				mesProductlineDataMap = generateExportQdasData(mesProductline,qdasMap);
				if(!mesProductlineDataMap.isEmpty()){
					list_.add(mesProductlineDataMap);
				}
				//添加设备的数据
				if(!mesDriverDataMap.isEmpty()){
					list_.add(mesDriverDataMap);
				}

				Map<String,Class<? extends Object>> propertyMap = new HashMap<String,Class<? extends Object>>();
				for(Map<String,String> _map : list_){
					for(String field : _map.keySet()){
						propertyMap.put(field, Class.forName("java.lang.String"));
					}
				}
				if(!propertyMap.isEmpty()){
					DynamicBean bean = new DynamicBean(propertyMap);
					for(Map<String,String> _map : list_){
						for(String field : _map.keySet()){
							bean.setValue(field.substring(0,1).toLowerCase()+field.substring(1), _map.get(field));
						}
					}
					resultMap.get(mesProductProcedure.getId()).add(bean);

					//resultList.add(bean);
				}
			}

		}
		return resultMap;

	}
	private Map<String,String> generateExportQdasData(Object o,Map<String,Map<String,String>> qdasMap){
		//Map<String,Class<? extends Object>> propertyMap = new HashMap<String,Class<? extends Object>>();
		Map<String,String> map = new HashMap<>();
		Class<? extends Object> clazz;
		if(HbUtils.isProxy(o)){
			clazz = HbUtils.getClassWithoutInitializingProxy(o);
		}else{
			clazz = o.getClass();
		}
		String pString = clazz.getSimpleName();
		Map<String, String> fieldMap = qdasMap.get(pString);
		if(fieldMap!=null){
			for(String field : fieldMap.keySet()){
				String keyField = fieldMap.get(field);
				if(null != keyField && !keyField.equals("")){
					try {
						Method method = clazz.getMethod("get"+field.substring(0, 1).toUpperCase()+field.substring(1));
						Object obj = method.invoke(o);
						//						obj.getClass().getMethod("getId");
						map.put(keyField, String.valueOf(obj));
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						continue;
					}
				}
			}
		}
		return map;
	}

	public String ExportQdasData(AnalyzeSearch analyzeSearch,Map<String, String> mapping,Long chooseProcedure)  {
		List<Map<String, Object>> list = new ArrayList<>();
		/*
		 * Map --> 
		 *        属性1 - value1
		 *        属性2 - value2
		 */
		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder QDASHeaderBuilder = new StringBuilder();//导出qdas文件的标头，包括统计和属性描述
		Set<String> allProperty = new HashSet<>();
		List<MesQdas> mesQdasList = qdasService.findAll();
		Map<String,Map<String,String>> qdasMap = new HashMap<>();
		for(MesQdas mesQdas : mesQdasList){
			String mesQdasParam = mesQdas.getParameter();
			String key = mesQdas.getKeyfield();
			if(null == key){
				continue;
			}
			String classtype = mesQdas.getClasstype();
			if(!qdasMap.containsKey(classtype)){
				Map<String,String> map = new HashMap<>();
				qdasMap.put(classtype, map);
			}
			qdasMap.get(classtype).put(mesQdasParam, key);
		}
		//判断是否需要映射文件
		if(null!=mapping){
			Map<String,Map<String,String>> tempQdasMap = new HashMap<>();
			for(String ks : qdasMap.keySet()){
				Map<String, String> tempMap = qdasMap.get(ks);
				Map<String, String> instantMap = new HashMap<String, String>();
				for(String cs : tempMap.keySet()){
					String value = tempMap.get(cs);
					//根据映射map集合过滤K值域
					boolean flag = false; //映射集合中是否包含此K值域
					String finalKvalue = "";
					for(String s : mapping.keySet()){
						if(value.equals(s)){
							flag = true;
							finalKvalue = mapping.get(s);
							break;
						}
					}
					if(flag){
						instantMap.put(cs, finalKvalue);	
					}
				}
				tempQdasMap.put(ks, instantMap);
			}
			qdasMap = null;
			qdasMap = tempQdasMap;
		}
		try{
			Map<Long, List<Object>> exportQdas = exportQdas(analyzeSearch,qdasMap,chooseProcedure);
			for(Long procedureId : exportQdas.keySet()){
				List<Object> resultList = exportQdas.get(procedureId);
				QDASHeaderBuilder.append("K0100 "+resultList.size() + "\r\n");
				for(int i = 1; i <= resultList.size(); i++){
					Object dynamicBean = resultList.get(i-1);
					/*}
				for(Object dynamicBean : resultList){*/
					DynamicBean bean = (DynamicBean)dynamicBean;
					Object obj2 = bean.getObject();
					Class clazz2 = obj2.getClass();  
					Method[] methods2 = clazz2.getDeclaredMethods();  
					for (int j = 0; j < methods2.length; j++) {  
						String methodName = methods2[j].getName();   
						if(methodName.indexOf("get") != -1){
							Map<String, Object> map = new HashMap<>();
							Method m2 = clazz2.getMethod(methodName);
							
							String value = String.valueOf(m2.invoke(obj2));
							value = "null".equals(value)?"":value;
							System.out.println(value);
							//String field = methodName.substring(methodName.indexOf("get"));
							allProperty.add(methods2[j].getName().split("get")[1]);
							stringBuilder.append(methods2[j].getName().split("get")[1]+"/"+i+" "+value + "\r\n");

							map.put(methods2[j].getName().split("get")[1], m2.invoke(obj2));
							list.add(map);
						}
					}  

				}
			}


			//System.out.println(stringBuilder.toString());



			/*for(Object dynamicBean : resultList){
                Class clazz = dynamicBean.getClass();  
                Method[] methods = clazz.getDeclaredMethods();  
                for (int i = 0; i < methods.length; i++) {  
                    System.out.println("----------"+methods[i].getName());
                    Method m = clazz.getMethod("getObject", null);
                    Object obj = m.invoke(dynamicBean, null);//dynamicBean中的object
                    Class clazz2 = obj.getClass();  
                    Method[] methods2 = clazz2.getDeclaredMethods();  
                    for (int j = 0; j < methods.length; j++) {  
                        System.out.println(methods[j].getName());   
                        if(methods[j].getName().indexOf("get") != -1){
                            Method m2 = clazz.getMethod(methods[j].getName(),null);
                            System.out.println(m2.invoke(obj, null));
                        }
                    }  

                }  
        }*/
			for(String s : allProperty){
				MesQdas qdas = new MesQdas();
				String tempName = "";
				try {
					qdas = qdasDao.findByKeyfield(s);
					tempName = qdas.getName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null == qdas){
					continue;
				}
				QDASHeaderBuilder.append(s + " " + tempName + "\r\n");
			}
			return QDASHeaderBuilder.toString() + stringBuilder.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Map<String,Map<String,String>> getPropertyValues(String _rowKeys, String _propertyIds) {
	    List<String> rowKeys = Arrays.asList(_rowKeys.split(","));
        Map<String,Map<String,String>> map = new HashMap<>();
	    for(String rowObj : rowKeys) {
	        Map<String, String> paramMap = MesDataRowkeyUtil.getValMapByProcedureDataRowKey(rowObj);
	        List<SearchFilter> searchList = Lists.newArrayList();
	        if(StringUtils.isNotBlank(paramMap.get("insertTimestamp")))
	        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.EQ,Long.valueOf(paramMap.get("insertTimestamp"))));
	        if(StringUtils.isNotBlank(paramMap.get("factoryId")))
	        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, paramMap.get("factoryId")));
	        if(StringUtils.isNotBlank(paramMap.get("productLineId")))
	        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, paramMap.get("productLineId")));
	        if(StringUtils.isNotBlank(paramMap.get("driverId")))
	        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, paramMap.get("driverId")));
	        if(StringUtils.isNotBlank(paramMap.get("productProcedureId")))
	        searchList.add(new SearchFilter("productProcedureId", Operator.EQ, paramMap.get("productProcedureId")));

	        Specification<MesDataProductProcedure> specification = DynamicSpecifications
	                .bySearchFilter(MesDataProductProcedure.class, searchList);
	        List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findAll(specification);

	        Map<String,String> valueMap = new HashMap<>();
	        for(MesDataProductProcedure outObj : mesDataProductProcedureList) {
	            // valueMap.put(String.valueOf(outObj.getProcedurePropertyId()), outObj.getValueStatus());
	            valueMap.put(String.valueOf(outObj.getProcedurePropertyId()), outObj.getMetaValue());
	        }
	        map.put(rowObj, valueMap);
	    }

//        map.put("", "");
//		List<String> rowKeys = Arrays.asList(_rowKeys.split(","));
//		List<String> columns = Arrays.asList(_propertyIds.split(","));
//		List<Result> listResult = HBaseUtilNew.getDatasFromHbase(HbaseUtil.HBASE_TABLE_PROCEDURE, "cf", rowKeys, null, false, false);
//
//		for(Result result : listResult){
//			if(result == null){
//				continue;
//			}
//			if(result.isEmpty()){
//				continue;
//			}
//
//			for(Cell _cell:result.listCells()){ 
//				if(_cell != null){
////					String row = Bytes.toString( _cell.getRowArray(), _cell.getRowOffset(), _cell.getRowLength());
////					String _value = Bytes.toString( _cell.getValueArray(), _cell.getValueOffset(), _cell.getValueLength());  
//					String quali = Bytes.toString( _cell.getQualifierArray(),_cell.getQualifierOffset(),_cell.getQualifierLength());
//					String propertyId = quali.split("_")[2];
////					if(!quali.split(":")[3].equals(MesPointType.TYPE_PRODUCT_PROCEDURE) || !columns.contains(propertyId)){
//					if(!columns.contains(propertyId)){
//						//productionStatus = false;
//						continue;
//					}
////					String checkValue = _value.split(":")[3];
//					String checkValue = quali.split("_")[3];
//					valueMap.put(propertyId, checkValue);
//					//list.add(valueMap);
//				}
//				
//			}
//			map.put(Bytes.toString(result.getRow()), valueMap);
//		}
		return map;

	}

    /**
     * 获取同一个rowkey下的记录集合
     * 
     * @param result
     *            结果集
     * @return
     */
    private Map<String, String> getRowByResult(Result result) {
        if (result == null) {
            return null;
        }
        Map<String, String> cellMap = new HashMap<String, String>();
        for (Cell cell : result.listCells()) {
            String rowkey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
            cellMap.put("rowkey", rowkey);
            cellMap.put("timestamp", String.valueOf(cell.getTimestamp()));
//            System.out.println(cellMap.get("timestamp"));
            String qf = Bytes.toString(cell.getQualifierArray(),
            cell.getQualifierOffset(), cell.getQualifierLength());
			cellMap.put("pointId", qf.split("_")[1]);
			cellMap.put("status", qf.split("_")[4]);
			cellMap.put("qf", qf);
        }
        return cellMap;
    }
    
    /**
     * 获取同一个rowkey下的记录集合
     * 
     * @param result
     *            结果集
     * @return
     */
    private Map<String, Object> getRowByResultForProdurce(Result result) {
        if (result == null) {
            return null;
        }
        Map<String, Object> resMap = Maps.newHashMap();
        Map<String, String> rowkeyTimestamp = Maps.newHashMap();
        List<String> timestampList = Lists.newArrayList();

        List<String> nopassCountList = Lists.newArrayList();
        List<String> passCountList = Lists.newArrayList();

        for (Cell cell : result.listCells()) {
            String rowkey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
            String qf = Bytes.toString(cell.getQualifierArray(),
            cell.getQualifierOffset(), cell.getQualifierLength());
            String status = qf.split("_")[4];
            rowkeyTimestamp.put(String.valueOf(cell.getTimestamp()), rowkey+ ":" + qf);
            timestampList.add(String.valueOf(cell.getTimestamp()));
            if("NG".equals(status)) {
            	nopassCountList.add(String.valueOf(cell.getTimestamp()));
            } else if("OK".equals(status)) {
            	passCountList.add(String.valueOf(cell.getTimestamp()));
            }
        }
        resMap.put("rowkeyTimestampMap", rowkeyTimestamp);
        resMap.put("timestampList", timestampList);

        resMap.put("nopassCountList", nopassCountList);
        resMap.put("passCountList", passCountList);
        return resMap;
    }
    
    /**
     * 获取同一个rowkey下的记录集合
     * 
     * @param result
     *            结果集
     * @return
     */
    private Map<String, String> getRowByResultForOutPut(Result result) {
        if (result == null) {
            return null;
        }
        Map<String, String> cellMap = new HashMap<String, String>();
        int index = 0;
        String status = "OK";
        for (Cell cell : result.listCells()) {
            String qf = Bytes.toString(cell.getQualifierArray(),
            cell.getQualifierOffset(), cell.getQualifierLength());
            if(0 == index) {
                String rowkey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                cellMap.put("rowkey", rowkey);
                cellMap.put("productSn", rowkey.split("_")[6]);
                cellMap.put("timestamp", String.valueOf(cell.getTimestamp()).substring(0, 10));
                cellMap.put("pointId", qf.split("_")[1]);
        	}
            if("NG".equals(qf.split("_")[4])) {
            	status = "NG";
            	break;
            }
            index++;
        }
        cellMap.put("status", status);
        return cellMap;
    }


    /**
     * 搜索总件数，合格件数，不合格件数
     * @param hBasePageModel
     */
	private Map<String, Object> getCountByHbase(HBasePageModel hBasePageModel) {
        AnalyzeSearch analyzeSearch = hBasePageModel.getAnalyzeSearch();
        // 工厂ID
        Long factoryId = null;
        // 产线ID
        Long productlineId = analyzeSearch.getProductLineId();
        // 产品ID
        MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());
//		Long productId = mesProduct != null
//				? StringUtils.isNotBlank(mesProduct.getModelnum()) ? Long.valueOf(mesProduct.getModelnum()) : null
//				: null;
		String productId = mesProduct != null
				? StringUtils.isNotBlank(mesProduct.getModelnum()) ? mesProduct.getModelnum() : null
				: null;
        // 工序ID
        Long produceId = analyzeSearch.getProductProcedureId();
        for (MesProductline mesProductline : getProductionLineByCurrentCompanyId(analyzeSearch.getProductLineId())) {
            if (productlineId.equals(productlineId)) {
                if (null != mesProductline.getCompanyinfo()) {
                	factoryId = mesProductline.getCompanyinfo().getId();
                	factoryId = factoryId != null && 0L != factoryId?factoryId : null;
                }
                break;
            }
        }


		Map<String, Date> timeForScan = MesDataRowkeyUtil.getScanTimeRangeByHBasePageModel(hBasePageModel);
		
        MesDataRowkeyUtil.setEntityManager(entityManager);
        String beginTimeS = null == timeForScan.get("begin") ? "" : String.valueOf(timeForScan.get("begin").getTime()).replace("000", "");
        String endTimeS = null == timeForScan.get("end") ? "" : String.valueOf(timeForScan.get("end").getTime()).replace("000", "");

        List<String> ngCount =MesDataRowkeyUtil.getCountByPageSelect(String.valueOf(factoryId), String.valueOf(productlineId), "", String.valueOf(productId),
                String.valueOf(produceId),
                beginTimeS,endTimeS, "NG", analyzeSearch.getProductBatchid());

        List<String> totolCount =MesDataRowkeyUtil.getCountByPageSelect(String.valueOf(factoryId), String.valueOf(productlineId), "", String.valueOf(productId),
                String.valueOf(produceId),
                beginTimeS,endTimeS, "", analyzeSearch.getProductBatchid());
        Map<String, Object> outMap = Maps.newHashMap();
        outMap.put("totalCount", Long.valueOf(ngCount != null ? totolCount.size() : 0L));
        outMap.put("nopassCount", Long.valueOf(totolCount != null ? ngCount.size() : 0L));
//		
//		List<Result> procedureRSList = new HbaseUtil().getResultListByHbase(factoryId,
//				productlineId, null, productId, produceId, null, timeForScan.get("begin"), timeForScan.get("end"), true);
//        Map<String, String> rsMap = null;
//        List<String> totalCountList = Lists.newArrayList();
//        List<String> nopassCountList = Lists.newArrayList();
//        List<String> passCountList = Lists.newArrayList();
//        for (Result result : procedureRSList) {
//            rsMap = getRowByResultForOutPut(result);
//            totalCountList.add(rsMap.get("rowkey"));
//            if ("NG".equals(rsMap.get("status"))) {
//                nopassCountList.add(rsMap.get("status"));
//            } else {
//                passCountList.add(rsMap.get("status"));
//            }
//        }
//        Map<String, Object> outMap = Maps.newHashMap();
//        outMap.put("totalCount", Long.valueOf(totalCountList.size()));
//        if (null != nopassCountList) {
//            outMap.put("nopassCount", Long.valueOf(nopassCountList.size()));
//        } else {
//            outMap.put("nopassCount", 0);
//        }
        return outMap;
    }
	
    /**
     * 搜索总件数，合格件数，不合格件数
     * @param
     */
    public Map<String, Object> getPassNoPassCountByMysql(Long factoryId,
            Long productLineId, Long driverId, Long productId,
            Long produceId, Date startTime, Date stopTime) {

        MesDataRowkeyUtil.setEntityManager(entityManager);
        String beginTimeS = "";
        if(String.valueOf(startTime.getTime()).length() > 10) {
            beginTimeS = String.valueOf(startTime.getTime()).replace("000", "");
        } else {
            beginTimeS = String.valueOf(startTime.getTime());
        }
        if(beginTimeS.length() < 10) {
            StringBuffer sb = new StringBuffer();
            sb.append(beginTimeS);
            for(int i =0;i<10-beginTimeS.length();i++) {
                sb.append("0");
            }
            beginTimeS = sb.toString();
        }

        String endTimeS = ""; 
        if(String.valueOf(stopTime.getTime()).length() > 10) {
            endTimeS = String.valueOf(stopTime.getTime()).replace("000", "");
        } else {
            endTimeS = String.valueOf(stopTime.getTime());
        }

        List<String> ngCount =MesDataRowkeyUtil.getCountByPageSelect(
                factoryId == null?"":String.valueOf(factoryId),
                        productLineId == null?"":String.valueOf(productLineId), "",
                                productId == null?"":String.valueOf(productId),
                                        produceId == null?"":String.valueOf(produceId),
                beginTimeS,endTimeS, "NG", "");

        List<String> totolCount =MesDataRowkeyUtil.getCountByPageSelect(
                factoryId == null?"":String.valueOf(factoryId),
                        productLineId == null?"":String.valueOf(productLineId), "",
                                productId == null?"":String.valueOf(productId),
                                        produceId == null?"":String.valueOf(produceId),
                beginTimeS,endTimeS, "", "");

        List<String> okCount =MesDataRowkeyUtil.getCountByPageSelect(
                factoryId == null?"":String.valueOf(factoryId),
                        productLineId == null?"":String.valueOf(productLineId), "",
                                productId == null?"":String.valueOf(productId),
                                        produceId == null?"":String.valueOf(produceId),
                beginTimeS,endTimeS, "OK", "");
        Map<String, Object> outMap = Maps.newHashMap();
        outMap.put("totalCount", Long.valueOf(ngCount != null ? totolCount.size() : 0L));
        outMap.put("nopassCount", Long.valueOf(totolCount != null ? ngCount.size() : 0L));
        outMap.put("okCount", Long.valueOf(okCount != null ? okCount.size() : 0L));

        return outMap;

    }

    /**
     * 搜索总件数，合格件数，不合格件数
     * @param hBasePageModel
     */
//    public Map<String, Object> getPassNoPassCountByHbase(Long factoryId,
//            Long productLineId, Long driverId, Long productId,
//            Long produceId, Date startTime, Date stopTime) {
//        List<Result> rsList = new HbaseUtil().getResultListByHbase(factoryId, productLineId, driverId, productId,
//                produceId, null,  startTime, stopTime, true);
//        Map<String, String> rsMap = null;
//        Map<String, String> totalCountMap = Maps.newHashMap();
//        List<String> nopassCountList = Lists.newArrayList();
//        List<String> passCountList = Lists.newArrayList();
//        for (Result result : rsList) {
//            rsMap = getRowByResult(result);
//            totalCountMap.put(rsMap.get("rowkey"), "");
//            System.out.println(rsMap.get("status"));
//            if ("NG".equals(rsMap.get("status"))) {
//                nopassCountList.add(rsMap.get("rowkey"));
//            } else if ("OK".equals(rsMap.get("status"))) {
//                passCountList.add(rsMap.get("rowkey"));
//            }
//        }
//        Map<String, Object> outMap = Maps.newHashMap();
//        outMap.put("totalCount", totalCountMap);
//        if (null != nopassCountList) {
//            outMap.put("nopassCount", Long.valueOf(nopassCountList.size()));
//        } else {
//            outMap.put("nopassCount", 0);
//        }
//        return outMap;
//
//    }
    
    /**
     * 嘉兴双环传动SPC数据专用监控
     * 通过HBase查询数据
     * HbaseUtil是一个查询工具，封装了一些查询filter
     * @param analyzeSearch
     * @return
     */
    public List<Double> analyzeDataSearchForMonitor(AnalyzeSearch analyzeSearch, String searchFlg) {
        System.out.println("监控型检索----------------------------------start" + new Date());
        Date start = analyzeSearch.getBegin();
        Date end = analyzeSearch.getEnd();
        MesDriver mesDriver = mesDriverDao.findOne(analyzeSearch.getMesDriverId());
        MesProductline mesProductline = mesDriver.getMesProductline();
        analyzeSearch.setProductLineId(mesProductline.getId());
        MesProcedureProperty mesProcedureProperty = mesProdcedurePropertyDao.findOne(analyzeSearch.getProcedurePropertyId());
        String pointKey = mesProcedureProperty.getMesPoints().getCodekey();
        MesProductProcedure productProcedure = mesProcedureProperty.getMesProductProcedure();
        MesProduct mesProduct = productProcedure.getMesProduct();
        System.out.println("监控型检索----------------------------------start" + new Date());
        System.out.println("生产记录----工厂ID:" + mesProductline.getCompanyinfo().getId());
        System.out.println("生产记录----产线ID:" + mesProductline.getId());
        System.out.println("生产记录----产品ID:" + Long.valueOf(mesProduct.getModelnum()));
        System.out.println("生产记录----设备ID:" + mesDriver.getId());
        System.out.println("生产记录----工序ID:" + productProcedure.getId());
        System.out.println("生产记录----start:" + start);
        System.out.println("生产记录----end:" + end);

        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, String.valueOf(mesProductline.getCompanyinfo().getId())));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, String.valueOf(mesProductline.getId())));
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(mesDriver.getId())));

        if(null != start)
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(start.getTime()).replace("000", "")));
        if(null != end)
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(end.getTime()).replace("000", "")));
        searchList.add(new SearchFilter("productProcedureId", Operator.EQ, productProcedure.getId()));
        searchList.add(new SearchFilter("productMode", Operator.EQ, Long.valueOf(mesProduct.getModelnum())));
        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
        List<MesDataProductProcedure> mesDataProductProcedrureList = Lists.newArrayList();
        if("2".equals(searchFlg)) {
            Page page = new Page();
            page.setOrderField("mesDataMultiKey.insertTimestamp");
            page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
            int subNum = analyzeSearch.getSubNum();
            int subRange = analyzeSearch.getSubRange();
            int totalCount = subNum * subRange;
            page.setTotalCount(Long.valueOf(String.valueOf(totalCount)));
            page.setNumPerPage(totalCount);
            mesDataProductProcedrureList = mesDataProductProcedureService.findPage(specification, page);
        } else {
            mesDataProductProcedrureList = mesDataProductProcedureService.findAll(specification);
        }

        List<CgAnalyzeData> list = new ArrayList<>();
        for(MesDataProductProcedure obj : mesDataProductProcedrureList) {
            CgAnalyzeData cgAnalyzeData = new CgAnalyzeData();
            cgAnalyzeData.setRowKey(MesDataRowkeyUtil.getRowKey(obj));
            cgAnalyzeData.setMesDriverName(mesDriver.getName());
            cgAnalyzeData.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
            cgAnalyzeData.setProductionSn(obj.getProductBsn());
            cgAnalyzeData.setProductName(mesProduct.getName());
            cgAnalyzeData.setProductProcedureName(productProcedure.getProcedurename());
            cgAnalyzeData.setTime(DateUtils.unixTimestampToDate(obj.getMesDataMultiKey().getInsertTimestamp().longValue()));
            cgAnalyzeData.setTime(obj.getMesDataMultiKey().getInsertTimestamp().toString());
//            cgAnalyzeData.setTime(DateUtils.unixTimestampToDate(Long.valueOf(String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp()) + "000")));
//            cgAnalyzeData.setTime(DateUtils.getTimeStrByTimeStamp(Timestamp.valueOf(obj.getMesDataMultiKey().getInsertTimestamp().toString())));
            
            cgAnalyzeData.setValue(obj.getMetaValue());
            list.add(cgAnalyzeData);
        
        }

//        Boolean timeLimitFlg = false;
//        List<Result> datas  = new  HbaseUtil().getResultListByHbase(mesProductline.getCompanyinfo().getId(), mesProductline.getId(),
//                mesDriver.getId(), Long.valueOf(mesProduct.getModelnum()), productProcedure.getId(), null, null, null, timeLimitFlg);
//        List<CgAnalyzeData> list = new ArrayList<>();
//        if(datas.size()>0){
//            for (Result result : datas) {
//                if(result.isEmpty()){
//                    continue;
//                }
//                List<Cell> ceList =   result.listCells(); 
//                CgAnalyzeData cgAnalyzeData = new CgAnalyzeData();
//                for(Cell cell : ceList){
//                    String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
//                    if(quali.indexOf(pointKey) == -1){
//                        continue;
//                    }
//                    String row =Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());  
//                    String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
//                    String family =  Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
//                    String qf = Bytes.toString(cell.getQualifierArray(),
//                            cell.getQualifierOffset(), cell.getQualifierLength());
//                    System.out.println(row+"============"+value+"============"+family);
//                    cgAnalyzeData.setMesDriverName(mesDriver.getName());
//                    cgAnalyzeData.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
//                    cgAnalyzeData.setProductionSn(row.split("_")[6]);
//                    cgAnalyzeData.setProductName(mesProduct.getName());
//                    cgAnalyzeData.setProductProcedureName(productProcedure.getProcedurename());
//                    cgAnalyzeData.setTime(DateUtils.getYYYYMMDDHHMMSSDayStr(new Date(cell.getTimestamp())));
//                    cgAnalyzeData.setValue(qf.split("_")[3]);
//                    list.add(cgAnalyzeData);
//                }
//                
//                
//            }
//        }
        
        
        Map<String,Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        long endSearch = System.currentTimeMillis();
        list.sort(new Comparator<CgAnalyzeData>() {
            @Override
            public int compare(CgAnalyzeData o1, CgAnalyzeData o2) {
//                if(DateUtils.parse(o1.getTime()).before(DateUtils.parse(o2.getTime()))){
//                    return 1;
//                }
                if(0 > (Integer.valueOf(o1.getTime()) - Integer.valueOf(o2.getTime())) ) {
                    return 1;
                }
                return -1;
            }
        });
        for(CgAnalyzeData cgAnalyzeData : list){
            System.out.println("spc设备名称：" + cgAnalyzeData.getMesDriverName());
            System.out.println("spc工序属性名称：" + cgAnalyzeData.getProcedurePropertyName());
            System.out.println("spc产品SN名称：" + cgAnalyzeData.getProductionSn());
            System.out.println("spc产品名称：" + cgAnalyzeData.getProductName());
            System.out.println("spc产品工序名称：" + cgAnalyzeData.getProductProcedureName());
            System.out.println("产品时间：" + cgAnalyzeData.getTime());
        }
        int id = 1;
        for(CgAnalyzeData cgAnalyzeData : list){
            cgAnalyzeData.setId(id);
            id++;
        }
        /**
         * spc筛选条件
         */
        if(analyzeSearch.getSubSeq() != -1){
            if(analyzeSearch.getSubSeq() == 0){ 
                int size = analyzeSearch.getSubNum()*analyzeSearch.getSubRange();
                list = list.subList(0, size < list.size() ? size : list.size());
            }else{
                List<CgAnalyzeData> newList = new ArrayList<>();
                newList.add(list.get(0));
                for(int i = 1; i < list.size(); i++){
                    long time1 = DateUtils.parse(list.get(i).getTime()).getTime();
                    long time2 = DateUtils.parse(newList.get(newList.size() - 1).getTime()).getTime();
                    BigDecimal bigDecimal1 = new BigDecimal(time1);
                    BigDecimal bigDecimal2 = new BigDecimal(time2);
                    if(bigDecimal2.subtract(bigDecimal1).longValue() >= (analyzeSearch.getSubSeq() * 60 * 60 * 1000)){
                        newList.add(list.get(i));
                    }
                    if(newList.size() == analyzeSearch.getSubNum()*analyzeSearch.getSubRange()){
                        break;
                    }
                }
                list = newList;
            }
        }
        if(null != list && 0 < list.size()) {
            List<CgAnalyzeData> subList = list.subList(0, analyzeSearch.getSubNum() * analyzeSearch.getSubRange());
            List<Double> rsList = Lists.newArrayList();
            for(CgAnalyzeData out : subList) {
                rsList.add(Double.valueOf(out.getValue()));
            }
            return rsList;
        }

        return null;
    }
	
//	private List<Map<String, Object>> setMapByListResult(List<Result> inputParam) {
//		List<Map<String, Object>> outList = Lists.newArrayList(); 
//		Map<String, Object> outMap = Maps.newHashMap();
//		// 获取时间list，进行倒序
//        List<String> timestampList = Lists.newArrayList();
//		// 根据时间重排rowkey
//        Map<String, String> rowkeyTimestamp = Maps.newHashMap();
//        Map<String, String> rsMap = null;
//        for (Result result : inputParam) {
//            rsMap = getRowByResult(result);
//            timestampList.add(rsMap.get("timestamp"));
//            rowkeyTimestamp.put(rsMap.get("timestamp"), rsMap.get("rowkey") + ":" + rsMap.get("qf"));
//        }
//        outMap.put("timestampList", timestampList);
//        outMap.put("rowkeyTimestamp", rowkeyTimestamp);
//        outList.
//		return outMap;
//	}

}
