package com.its.frd.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesDriverDao;
import com.its.frd.dao.MesDriverOEEDao;
import com.its.frd.dao.MesDriverPointsDao;
import com.its.frd.dao.MesDriverStatsDao;
import com.its.frd.dao.MesEnergyDao;
import com.its.frd.entity.MesDataDriverCount;
import com.its.frd.entity.MesDataDriverProperty;
import com.its.frd.entity.MesDataMultiKey;
import com.its.frd.entity.MesDataProductProcedure;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverOEE;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesDriverStats;
import com.its.frd.entity.MesDrivertypeProperty;
import com.its.frd.entity.MesEnergy;
import com.its.frd.entity.MesPointCheckData;
import com.its.frd.entity.MesPointType;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.schedule.DriverRecordSchedulerPageSearch;
import com.its.frd.schedule.ProductionSchedulerPageSearch;
import com.its.frd.service.MesDataDriverCountService;
import com.its.frd.service.MesDataDriverPropertyService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesDriverTypePropertyService;
import com.its.frd.service.MesPointCheckDataService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;
import com.its.frd.util.DateUtils;
import com.its.frd.util.DecimalCalculate;
import com.its.frd.util.HBasePageModel;
import com.its.frd.util.HBaseUtilNew;
import com.its.frd.util.HbaseUtil;
import com.its.frd.util.MesDataRowkeyUtil;
import com.its.frd.util.TimeSplitUtil;
import com.its.statistics.vo.AnalyzeSearch;
import com.its.statistics.vo.DriverRecord;
import com.its.statistics.vo.ProductionRecord;

@Service
public class MesDriverImp implements MesDriverService {
	@Resource
	private MesDriverDao dao;
	
	@Autowired
	private MesDriverOEEDao oeeDao;
	
	@Autowired
	private MesEnergyDao meyDao;
	
	@Autowired
	private MesDriverStatsDao mds;
	
	@Autowired
	private DriverRecordSchedulerPageSearch driverRecordSchedulerPageSearch;
	
	@Autowired
	private MesDriverPointsDao driverPointDao;
	
	@Autowired
	private MesDriverTypePropertyService mesDriverTypePropertyService;
	
	@Autowired
	private MesPointCheckDataService mesPointCheckDataService;
	
	@Autowired
	private MesProductService mesProductService;
	
	@Autowired
	private ProductionSchedulerPageSearch productionSchedulerPageSearch;
	
    @Autowired
    private MesDataDriverPropertyService mesDataDriverPropertyService;
    
    @Autowired
    private MesDataDriverCountService mesDataDriverCountService;

    //@Autowired
//     private HbaseTemplate hTemplate;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public MesDriver findById(Long id) {
		return dao.findOne(id);
	}
	
	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}
	
	@Override
	public List<MesDriver> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriver> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	@Override
	public List<MesDriver> findPage(Specification<MesDriver> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriver> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDriver saveOrUpdate(MesDriver t) {
		return dao.save(t);
	}

    @Override
    public List<MesDriver> findByCompanyidAndDifferencetype(Long comapnyid,String differencetype) {
        return dao.findByCompanyidAndDifferencetype(comapnyid,differencetype);
    }

    @Override
    public List<MesDriver> findByMesProductline(Long companyid) {
        return dao.findByMesProductline(companyid);
    }

    @Override
    public List<MesDriver> findByMesProductlineid(Long mesProductlineid) {
        return dao.findByMesProductlineid(mesProductlineid);
    }

    @Override
    public List<MesDriver> findByCompanyinfoidAndSnAndDif(Long companyid,String sn,String dif) {
        return dao.findByCompanyinfoidAndSnAndDif(companyid, sn, dif);
    }

    @Override
    public List<MesDriver> findByMesDriverTypeId(Long mesDriverTypeId) {
        return dao.findByMesDrivertypeId(mesDriverTypeId);
    }
    
    @Override
    public MesDriver findBySn(String sn) {
    	return dao.findBySn(sn);
    }

	@Override
	public List<MesDriverOEE> findOEEPage(Specification<MesDriverOEE> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverOEE> springDataPage = oeeDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public void saveHandOEEResult(MesDriverOEE mesDriverOEE) {
		oeeDao.save(mesDriverOEE);
	}

	@Override
	public List<MesEnergy> findenergyPage(Specification<MesEnergy> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesEnergy> springDataPage = meyDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	
	@Override
	public List<MesEnergy> findenergy(String energyType, Timestamp startDate, Timestamp endDate, Long driverId) {
		return meyDao.findEnergy(energyType.toUpperCase(), startDate, endDate, driverId);
	}
	
	@Override
	public List<MesEnergy> findenergy(String energyType, Timestamp startDate, Timestamp endDate) {
		return meyDao.findEnergy(energyType.toUpperCase(), startDate, endDate);
	}
	
	@Override
	public List<MesDriverStats> findDriverOutput(Timestamp startDate, Timestamp endDate, Long driverId) {
		return mds.findByDriverIdAndTimeSpan(driverId, startDate, endDate);
	}
	
	@Override
	public String findDriverRedisMac(MesDriver driver) {
		List<MesDriverPoints> list = driver.getMesDriverPointses();
		for(MesDriverPoints m : list){
			if("P_COUNT".equals(m.getMesPoints().getMesPointType().getPointtypekey())){
				return m.getMesPoints().getMesPointGateway().getMac()+":"+m.getMesPoints().getCodekey();
			}
		}
		return null;
	}
	
	@Override
	public List<String> findDriverPropertyRowKeys(Timestamp start, Timestamp end,
			Long driverId,HBasePageModel page) {
		List<String> rowkeys = new ArrayList<String>();
		AnalyzeSearch analyzeSearch = new AnalyzeSearch();
    	analyzeSearch.setMesDriverId(driverId);
    	analyzeSearch.setBegin(start);
    	analyzeSearch.setEnd(end);
    	page.setAnalyzeSearch(analyzeSearch);
    	page.setTotalCount(driverRecordSchedulerPageSearch.getDriverRecordAmount(page));
		List<DriverRecord> list = driverRecordSchedulerPageSearch.getDriverRecordPagable(page);
		if(list==null || list.size()<1){
			return null;
		}
		for(DriverRecord d : list){
    		rowkeys.add(d.getRowkey());
		}
		return rowkeys;
	}
	
	@Override
	public List<DriverRecord> getDriverOutputByOne(Timestamp start, Timestamp end,
			Long driverId,HBasePageModel page) {
		AnalyzeSearch analyzeSearch = new AnalyzeSearch();
    	analyzeSearch.setMesDriverId(driverId);
    	analyzeSearch.setBegin(start);
    	analyzeSearch.setEnd(end);
    	analyzeSearch.setDriver_count("1");
    	page.setAnalyzeSearch(analyzeSearch);
    	page.setTotalCount(driverRecordSchedulerPageSearch.getDriverRecordAmount(page));
		List<DriverRecord> list = new ArrayList<DriverRecord>();
		list = driverRecordSchedulerPageSearch.getDriverRecordPagable(page);
		return list;
	}
	
	   @Override
	    public List<DriverRecord> getDriverOutputByHbase(Timestamp start, Timestamp end,
	            String factoryId, String productLineId,Long driverId,HBasePageModel page,
	            DateType dateType) {
//	        List<SearchFilter> list = Lists.newArrayList();
//	        String beginTime = String.valueOf(start.getTime());
//	        beginTime = beginTime.substring(0, 10);
//	        list.add(new SearchFilter("mesDataMulrtiKey.insertTimestamp", Operator.GTE, beginTime));
//	        String endTime = String.valueOf(end.getTime());
//	        endTime = endTime.substring(0, 10);
//	        list.add(new SearchFilter("mesDataMulrtiKey.insertTimestamp", Operator.LTE, end.getTime()));
//	        list.add(new SearchFilter("mesDataMulrtiKey.productLineId", Operator.EQ, productLineId));
//	        list.add(new SearchFilter("mesDataMulrtiKey.driverId", Operator.EQ, driverId));
//	        list.add(new SearchFilter("mesDataMulrtiKey.factoryId", Operator.EQ, factoryId));
//
//	        Specification<MesDataDriverCount> specification = DynamicSpecifications
//	                .bySearchFilter(MesDataDriverCount.class, list);
//	        page.setTotalPage(page.getTotalPage());
//	        page.setOrderField("mesDataMulrtiKey.insertTimestamp");
//	        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
//	        List<MesDataDriverCount> mesDataDriverCountList = mesDataDriverCountService.findPage(specification, page);
//	        
//	        List<MesDataDriverCount> totalResult = mesDataDriverCountService.findAll(specification);
//	        page.setTotalCount(totalResult != null?totalResult.size() : 0);
//	        int totalPage = Integer.valueOf(String.valueOf(page.getTotalCount()))/10;
//	        page.setTotalPage(totalPage);
//
//	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        MesDriver driver = dao.findOne(driverId);
//	        List<Map<String, String>> listOut = Lists.newArrayList();
//	        
//	        List<DriverRecord> driverRecordList = Lists.newArrayList();
//	        DriverRecord driverRecord = null;
//	        MesDataMultiKey mesDataMulrtiKey = null;
//	        for(MesDataDriverCount obj : mesDataDriverCountList) {
//	            mesDataMulrtiKey = obj.getMesDataMulrtiKey();
//	            driverRecord = new DriverRecord();
//	            driverRecord.setMesDriverName(driver.getName());
//	            driverRecord.setDriver_count(String.valueOf(obj.getDriverCount()));
//	            driverRecord.setDatetime(mesDataMulrtiKey.getInsertTimestamp());
//	            driverRecordList.add(driverRecord);
//	        }

	       List<DriverRecord> driverRecordList = Lists.newArrayList();
	        List<Timestamp> times = TimeSplitUtil.getDateList(dateType, start, end);
	        MesDataRowkeyUtil.setEntityManager(entityManager);
	        Map<String, String> rsMap = MesDataRowkeyUtil.getDriverCountByPageSelect(factoryId, productLineId, String.valueOf(driverId), times);
	        rsMap.forEach((k,v)->{
	            DriverRecord driverRecordout = new DriverRecord();
	            driverRecordout.setMesDriverName(driver.getName());
	            driverRecordout.setDriver_count(String.valueOf(v));
	            String outTime = k.substring(0, 10);
	            driverRecordout.setDatetime(BigInteger.valueOf(Long.valueOf(outTime)));
	            driverRecordList.add(driverRecordout);
	        });

	        return driverRecordList;
	    }

//	@Override
//	public List<DriverRecord> getDriverOutputByHbaseDel(Timestamp start, Timestamp end,
//			String factoryId, String productLineId,Long driverId,HBasePageModel page) {
//        List<SearchFilter> list = Lists.newArrayList();
//        String beginTime = String.valueOf(start.getTime());
//        beginTime = beginTime.substring(0, 10);
//        list.add(new SearchFilter("mesDataMulrtiKey.insertTimestamp", Operator.GTE, beginTime));
//        String endTime = String.valueOf(end.getTime());
//        endTime = endTime.substring(0, 10);
//        list.add(new SearchFilter("mesDataMulrtiKey.insertTimestamp", Operator.LTE, end.getTime()));
//        list.add(new SearchFilter("mesDataMulrtiKey.productLineId", Operator.EQ, productLineId));
//        list.add(new SearchFilter("mesDataMulrtiKey.driverId", Operator.EQ, driverId));
//        list.add(new SearchFilter("mesDataMulrtiKey.factoryId", Operator.EQ, factoryId));
//
//        Specification<MesDataDriverCount> specification = DynamicSpecifications
//                .bySearchFilter(MesDataDriverCount.class, list);
//        page.setTotalPage(page.getTotalPage());
//        page.setOrderField("mesDataMulrtiKey.insertTimestamp");
//        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
//        List<MesDataDriverCount> mesDataDriverCountList = mesDataDriverCountService.findPage(specification, page);
//        
//        List<MesDataDriverCount> totalResult = mesDataDriverCountService.findAll(specification);
//    	page.setTotalCount(totalResult != null?totalResult.size() : 0);
//		int totalPage = Integer.valueOf(String.valueOf(page.getTotalCount()))/10;
//		page.setTotalPage(totalPage);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        MesDriver driver = dao.findOne(driverId);
//        List<Map<String, String>> listOut = Lists.newArrayList();
//        
//        List<DriverRecord> driverRecordList = Lists.newArrayList();
//        DriverRecord driverRecord = null;
//        MesDataMultiKey mesDataMulrtiKey = null;
//        for(MesDataDriverCount obj : mesDataDriverCountList) {
//            mesDataMulrtiKey = obj.getMesDataMulrtiKey();
//            driverRecord = new DriverRecord();
//            driverRecord.setMesDriverName(driver.getName());
//            driverRecord.setDriver_count(String.valueOf(obj.getDriverCount()));
//            driverRecord.setDatetime(mesDataMulrtiKey.getInsertTimestamp());
//            driverRecordList.add(driverRecord);
//        }
//
//		return driverRecordList;
//	}
	
	/**
	 * 旧版：废除
	 */
//	@Override
//	public List<Map<String, String>> findDriverPropertyByHbase(List<String> rowkeys, 
//			List<Long> driverPropertyIds, Long driverId) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//		MesDriver driver = dao.findOne(driverId);
//    	//从hbase中筛选查找数据
//    	List<Result> res = HBaseUtilNew.getDatasFromHbase("frd2", "cf", rowkeys, null, false, false);
//    	if(res.size()<1 || res==null){
//    		return null;
//    	}
//    	for(Result result : res){  //遍历结果集
//			List<Cell> listCells = result.listCells();
//			if(listCells != null && listCells.size() > 0){
//				String val =Bytes.toString( listCells.get(0).getValueArray(), 
//						listCells.get(0).getValueOffset(),listCells.get(0).getValueLength());  
//				String checkTime = sdf.format(new Date(Long.parseLong(val.split(":")[5])*1000));
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("driverName", driver.getName());
//				map.put("time", checkTime);
//				for(Cell cell : listCells){
//					String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
//					String[] split = value.split(":");
//					String codekey = split[2];
//					for(Long id : driverPropertyIds){
//						MesDrivertypeProperty drivertypeProperty = mesDriverTypePropertyService.findById(id);
//						String pointCodeKey = this.getPointKey(id,driverId); //测点CodeKey
//						if(pointCodeKey.equals(codekey)){
//							map.put("d"+drivertypeProperty.getId(), split[3]);
//						}
//					}
//				}
//				list.add(map);
//			}
//    	}
//		return list;
//	}
	
	@Override
	public List<DriverRecord> getDriverRecordRowkeys(Map<String, String> map, Long chooseDriver2, String modelNum, Long chooseFactory2,Long chooseProductLine2) throws ParseException {
		List<DriverRecord> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String key : map.keySet()){
	    	Date begin = sdf.parse(key);
	    	Date end = sdf.parse(map.get(key));
	    	List<DriverRecord> driverRecords = driverRecordSchedulerPageSearch.getByDriverIdAndTimeIfModelnum(begin, end, chooseDriver2, modelNum, chooseFactory2, chooseProductLine2);
	    	if(driverRecords==null || driverRecords.size()<1){
	    		continue;
	    	}
	    	list.addAll(driverRecords);
		}
		return list;
	}
	
	//public int getDriverRunTime(String start_rowkey, String stop_rowkey, Long productionLineId, Long DriverId, String modelNum) throws ParseException {
	@Override
	public int getDriverRunTime(List<DriverRecord> driverRecordList,Long driverId) {
		if(driverRecordList==null || driverRecordList.size()<1){
    		return 0;
    	}
		//把十位的时间戳 和 状态值拼装到一起，便于排序
		List<Long> find = new ArrayList<Long>();
		for(DriverRecord dr :driverRecordList){
			//(begin.getTime()+"").substring(0, 10)
			Long tempS = Long.parseLong((Timestamp.valueOf(dr.getDatetime()).getTime()+"").substring(0, 10)+""+dr.getDriver_status());
			find.add(tempS);
		}
		/*System.out.println("数据库中查询到的rowkey数量---"+list.size());
    	//从hbase中筛选查找数据
		List<Long> find = new ArrayList<Long>();
    	long currentTimeMillis = System.currentTimeMillis();
    	List<Result> res = HBaseUtilNew.getDatasFromHbase("frd", "cf", list, null, false, false);
    	long currentTimeMillis2 = System.currentTimeMillis();
    	System.out.println("hbase查到的数据条数："+res.size());
    	System.out.println("查询hbase耗时>>>>>>>>>>>>>>"+(currentTimeMillis2-currentTimeMillis));
    	if(res.size()<1 || res==null){
    		return 0;
    	}
    	for(Result result : res){  //遍历结果集
			List<Cell> listCells = result.listCells();
			if(listCells != null && listCells.size() > 0){
				for(Cell cell : listCells){
					String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());  
					String quali = Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
					String[] split = value.split(":");
					String property = quali.split(":")[3];
					String model = split[0];
					if(modelNum!=null){
						if(("D_STATUS".equals(property)&&modelNum.equals(model))){
							int checkTime = Integer.parseInt(split[5]);
							int sta = Integer.parseInt(split[3]);
							if((sta==1)||(sta==0)){
								//把十位的时间戳 和 状态值拼装到一起，便于排序
								String tempA = checkTime+""+sta;
								Long tempB = Long.parseLong(tempA);
								find.add(tempB);
							}
						}
					}else{
						if("D_STATUS".equals(property)){
							int checkTime = Integer.parseInt(split[5]);
							int sta = Integer.parseInt(split[3]);
							if((sta==1)||(sta==0)){
								//把十位的时间戳 和 状态值拼装到一起，便于排序
								String tempA = checkTime+""+sta;
								Long tempB = Long.parseLong(tempA);
								find.add(tempB);
							}
						}
					}
					continue;
				}
			}
    	}*/
    	if(find.size()<1){
    		return 0;
    	}
    	//给 时间戳+状态值  的集合排序
    	Collections.sort(find);
    	//获取设备配置的状态值
    	MesDriver driver = dao.findOne(driverId);
    	List<MesDriverPoints> points = driver.getMesDriverPointses();
    	int normalStatus = 0;
    	if(points.size() > 0){
    		for(MesDriverPoints mdp : points){
    			if((MesPointType.TYPE_DRIVER_STATUS).equals(mdp.getMesPoints().getMesPointType().getPointtypekey())){
    				List<Long> checkIdList = new ArrayList<>();
    				checkIdList.add(mdp.getId());
    				List<MesPointCheckData> checkList = mesPointCheckDataService.findByPointsId(checkIdList);
    				if(checkList.size()<1){
    					break;
    				}
    				for(MesPointCheckData mpcd : checkList){
    					if("正常".equals(mpcd.getName())){
    						normalStatus = mpcd.getCheckvalue().intValue();
    					}
    				}
    				break;
    			}
    		}
    	}
    	//总分钟数
    	int totalRunTime = 0;
    	int lastTime = 0;//每段时间间隔中第一个数据的key
    	int lastStatus = normalStatus;//每段时间间隔中第一个数据的value
    	//第一条数据
    	Long firstA = find.get(0);
    	String first = String.valueOf(firstA);
    	lastTime = Integer.parseInt(first.substring(0, 10));
    	lastStatus = Integer.parseInt(first.substring(10));
    	//遍历集合，筛选计算设备运行时间
    	for(int i=1;i<find.size();i++){
    		Long tempC = find.get(i);
    		String tempCC = String.valueOf(tempC);
    		int mapKey = Integer.parseInt(tempCC.substring(0, 10));//设备本次循环的时间戳
    		int mapValue = Integer.parseInt(tempCC.substring(10));//设备本次循环的状态
    		//状态从1到1
    		if((mapValue!=normalStatus)&&(mapValue==lastStatus)){
    			lastTime = mapKey;
    		//状态从1到0
    		}else if((mapValue!=lastStatus)&&((mapValue==normalStatus))){
    			lastStatus = mapValue;
    			lastTime = mapKey;
    		//状态从0到1
    		}else if((mapValue!=lastStatus)&&((mapValue!=normalStatus))){
    			totalRunTime = totalRunTime + (mapKey-lastTime);
    			lastStatus = mapValue;
    			lastTime = mapKey;
    	    //状态从0到0
    		}else if((mapValue==normalStatus)&&(mapValue==lastStatus)){
    			totalRunTime = totalRunTime + (mapKey-lastTime);
    			lastTime = mapKey;
    		}else{
    			continue;
    		}
    		//System.out.println(mapKey+"-----"+mapValue);
    	}
		return totalRunTime/60;
	}

	@Override
	public MesDriverOEE calculate(Integer runTime, Integer haltTime, Integer adjustTime, Integer hitchTime,
			Double circle, Integer output, Integer waste, Integer chooseExact, Integer autoTrueTime) {
		MesDriverOEE mesDriverOEE = new MesDriverOEE();
		double oeeRate = 0;
		double timeRate = 0;
		double propertyRate = 0;
		double passRate = 0;
		//时间开动率
		if(null!=adjustTime && null!=hitchTime){
			timeRate = DecimalCalculate.div((runTime-haltTime-adjustTime-hitchTime), (runTime-haltTime), chooseExact);
		}else{
			timeRate = DecimalCalculate.div(autoTrueTime, runTime, chooseExact);
		}
		if(adjustTime==null && hitchTime==null){
			adjustTime = 0;
			hitchTime = 0;
		}
		//性能开动率
		double mul = DecimalCalculate.mul(circle, output, chooseExact);
		propertyRate = DecimalCalculate.div(mul, (runTime-haltTime-adjustTime-hitchTime), chooseExact);
		//合格品率
		if(output!=0) {
			passRate = DecimalCalculate.div((output-waste), output, chooseExact);
		}
		//OEE
		oeeRate = DecimalCalculate.mul(DecimalCalculate.mul(timeRate, propertyRate, chooseExact), passRate, chooseExact);
		mesDriverOEE.setTimeRate(DecimalCalculate.mul(timeRate, 100, chooseExact)+"");
		mesDriverOEE.setPropertyRate(DecimalCalculate.mul(propertyRate, 100, chooseExact)+"");
		mesDriverOEE.setPassRate(DecimalCalculate.mul(passRate, 100, chooseExact)+"");
		mesDriverOEE.setOeeRate(DecimalCalculate.mul(oeeRate, 100, chooseExact)+"");
		return mesDriverOEE;
	}

	@Override
	public Map<String, String> getTotalTime(String autotime1, String autotime2, String autotime3) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取起始日期到现在的所有日期
		List<String> alldays = this.getAllDays(autotime3,null);
		if((alldays==null) || (alldays.size()==0)){
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		Date date = new Date();//当前时间
		String nowdate = sdf.format(new Date());
		long nowtime = date.getTime();
		long time1 = sdf.parse(autotime1).getTime(); //今天的班次开始时间
		long time2 = sdf.parse(autotime2).getTime(); //今天的班次结束时间
		long time3 = sdf.parse(autotime3).getTime(); //运算起始时间
		//遍历list集合，填充map集合
		for(String as : alldays){
			String stime = "";//每一天开始时间点
			String etime = "";//每一天结束时间点
			//如果符合的时间段只有一天
			if(alldays.size()==1){
				if(time3>time1 && time3<time2){ //起始时间处于今天的班次日期之内
					if(nowtime>=time1 && nowtime<=time2){ //判断当前时间
						stime = autotime3;
						etime = nowdate;
					}else{
						stime = autotime3;
						etime = autotime2;
					}
				}else if(time3>=time2){  //起始时间处于今天的班次日期之后
					break;
				}else{  //起始时间处于今天的班次日期之前
					if(nowtime<time1){
						break;
					}else if(nowtime>=time1 && nowtime<=time2){
						stime = autotime1;
						etime = nowdate;
					}else{
						stime = autotime1;
						etime = autotime2;
					}
				}
			}
			//如果符合的时间段不止一天
			if(as.equals(nowdate.split(" ")[0])){ //如果是今天
				if(nowtime>=time1 && nowtime<=time2){ //当前时间处于班次时间段之内
					stime = autotime1;
					etime = nowdate;
				}else if(nowtime<=time1){
					continue;
				}else{
					stime = autotime1;
					etime = autotime2;
				}
			}else if((!as.equals(nowdate.split(" ")[0]))
					&&(as.equals(autotime3.split(" ")[0]))){ //如果是第一天
					//获取开始当天的开始和结束时间
					long thatday1 =  sdf.parse(as+" "+autotime1.split(" ")[1]).getTime();
					long thatday2 =  sdf.parse(as+" "+autotime2.split(" ")[1]).getTime();
					if(time3>=thatday1 && time3<=thatday2){ //起始时间处于班次时间段之内
						stime = autotime3;
						etime = as+" "+autotime2.split(" ")[1];
					}else if(time3>=thatday2){
						continue;
					}else{
						stime = as+" "+autotime1.split(" ")[1];
						etime = as+" "+autotime2.split(" ")[1];
					}
			}else{  //中间的天数
				stime = as+" "+autotime1.split(" ")[1];
				etime = as+" "+autotime2.split(" ")[1];
			}
			if((StringUtils.isNotEmpty(stime))&&(StringUtils.isNotEmpty(etime))){
				map.put(stime, etime);
			}
			//System.out.println(stime+"----"+etime);
		}
		return map;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<String> getAllDays(String autotime3,String closeTime) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> list = new ArrayList<String>();
		String[] split = autotime3.split("-");
		int year = Integer.parseInt(split[0]);//起始时间年份
		int month = Integer.parseInt(split[1]);//起始时间月份
		int day = Integer.parseInt((split[2]).split(" ")[0]);//起始时间日
		Date now;
		if(StringUtils.isNotEmpty(closeTime)){
			now = sdf.parse(closeTime);
		}else{
			now = new Date();
		}
		int nowYear = now.getYear()+1900;//当前时间年份
		int nowMonth = now.getMonth()+1;//当前时间年份
		int nowDay = now.getDate();//当前时间年份
		int days = 31;//一个月中的天数
	    for(int i=year;i<=nowYear;i++){
	    	boolean key = false;//判断平闰年
	    	if(((i%4==0 && i%100!=0) || i%400==0)){
	    		key = true;
	    	}
	    	//如果起始日和当前时间是同一年
    		if((year==nowYear)&&(i==year)){
    			if(month==nowMonth){  //如果起始时间是当前月
    				for(int a=day;a<=nowDay;a++){
    					list.add(this.addDate(year,month,a));
    				}
    			}else{  //起始日不是当前月
    				for(int j=month;j<=nowMonth;j++){
    	    			days = lastDayInMonth(j,key);
    	    			if(j==nowMonth){ //如果是最后一个月
    	    				for(int a=1;a<=nowDay;a++){
    	    					list.add(this.addDate(i,j,a));
    	    				}
    	    			}else if(j==month){ //第一个月
    	    				for(int a=day;a<=days;a++){
    	    					list.add(this.addDate(i,j,a));
    	    				}
    	    			}else if(j!=month && j!=nowMonth){  //中间的月份
    	    				for(int a=1;a<=days;a++){
    	    					list.add(this.addDate(i,j,a));
    	    				}
    	    			}
    	    		}
    			}
    			break;
    		}
	    	
	    	//如果起始日和当前时间不是同一年，起始年份
	    	if((year!=nowYear)&&(i==year)){
	    		for(int j=month;j<=12;j++){
	    			days = lastDayInMonth(j,key);
	    			if(j==month){ //如果是起始日当月
	    				for(int a=day;a<=days;a++){
	    					list.add(this.addDate(i,j,a));
	    				}
	    			}else{
	    				for(int a=1;a<=days;a++){
	    					list.add(this.addDate(i,j,a));
	    				}
	    			}
	    		}
	    		continue;
	    	}else if((year!=nowYear)&&(i!=year)&&(i!=nowYear)){ //起始时间与当前时间不是同一年，中间年份
	    		for(int j=1;j<=12;j++){
	    			days = lastDayInMonth(j,key);
	    			for(int a=1;a<=days;a++){
	    				list.add(this.addDate(i,j,a));
	    			}
	    		}
	    		continue;
	    	}else if((year!=nowYear)&&(i==nowYear)){ //起始时间与当前时间不是同一年，今年的年份
	    		for(int j=1;j<=nowMonth;j++){
	    			days = lastDayInMonth(j,key);
	    			if(j==nowMonth){ //如果是当前月
	    				for(int a=1;a<=nowDay;a++){
	    					list.add(this.addDate(i,j,a));
	    				}
	    			}else{
	    				for(int a=1;a<=days;a++){
	    					list.add(this.addDate(i,j,a));
	    				}
	    			}
	    		}
	    		continue;
	    	}
	    	
	    }
		return list;
	}
	
	/**
	 * 向不满十的数字前面加零
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return
	 */
	public String addDate(int year,int month,int day){
		String month1 = month<10?"0"+month:""+month;
		String day1 = day<10?"0"+day:""+day;
		return year+"-"+month1+"-"+day1;
	}

	/**
	 * 获取每个月的天数
	 * @param month
	 * @param key //是否是闰年
	 * @return
	 */
	public int lastDayInMonth(int month,boolean key){
		int day = 0;
		switch(month){
		case 1:day=31;break;
		case 3:day=31;break;
		case 5:day=31;break;
		case 7:day=31;break;
		case 8:day=31;break;
		case 10:day=31;break;
		case 12:day=31;break;
		case 4:day=30;break;
		case 6:day=30;break;
		case 9:day=30;break;
		case 11:day=30;break;
		case 2:
			if(key){
				day=29;
			}else{
				day=28;
			}
		}
		return day;
	}

	@Override
	public void deleteOEEById(Long id) {
		oeeDao.delete(id);
	}

	@Override
	public List<MesDriverOEE> findByTimeAndClasses(Date start, Date end, Long driverId, String classes) {
		List<MesDriverOEE> list = null;
		if(StringUtils.isNotEmpty(classes)){
			list = oeeDao.findByTimeAndClasses(start,end,driverId,classes);
		}else{
			list = oeeDao.findByTime(start,end,driverId);
		}
		return list;
	}

	@Override
	public Double averageOEE(List<MesDriverOEE> list) {
		if(list==null || list.size()<1){
			return 0.0;
		}else if(list.size()==1){
			return Double.parseDouble(list.get(0).getOeeRate());
		}
		double total = 0;
		for(MesDriverOEE oee : list){
			total = total + Double.parseDouble(oee.getOeeRate());
		}
		double average = DecimalCalculate.div(total, list.size(), 1);
		return average;
	}
	
	/**
	 * 获取mac地址
	 * @param propertyId 设备属性id
	 * @return
	 */
	/*private String getMacByDriverPropertyId(Long propertyId){
		if(propertyId == null)
			return null;
		MesPoints point = this.getPoint(propertyId);
		if(point != null)
			return point.getMesPointGateway().getMac();
		return null;
	}*/
	
	/**
	 * 获取测点key
	 * @param propertyId
	 * @return
	 */
	private String getPointKey(Long propertyId,Long driverId){
		if(propertyId == null)
			return null;
		MesPoints point = this.getPoint(propertyId,driverId);
		if(point != null)
			return point.getCodekey();
		return null;
	}
	
	/**
	 * 获取测点
	 * @param propertyId
	 * @return
	 */
	private MesPoints getPoint(Long propertyId,Long driverId){
		if(propertyId == null)
			return null;
		List<MesDriverPoints> dp = driverPointDao.findByMesDrivertypePropertyAndMesDriver(propertyId, driverId);
		if(dp.size()>0 && dp.get(0).getMesPoints() != null){
			return dp.get(0).getMesPoints();
		}else{
			return null;
		}
	}

	@Override
	public MesDriver findByMesPointGatewayid(Long mesPointGatewayid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Long> getYieldAndFailure(Long chooseFactory2, Long chooseProductLine2, String chooseModel, Long chooseDriver2,
			String autotime3) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Long> resMap = new HashMap<>();
		resMap.put("yield", 0L);
		resMap.put("failure", 0L);
		Date start = new Date();
		Date end = new Date();
		try {
			start = sdf.parse(autotime3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long companyid = SecurityUtils.getShiroUser().getCompanyid();
		Long currentProcedureId = 0L;
		MesProduct product = mesProductService.findByCompanyIdAndModelnum(companyid, chooseModel);
		if(product.getMesProductProcedures().size()<1) {
			return resMap;
		}
		//获取该产品下的所有工序
		List<Long> procedureIds = new ArrayList<>();
		for(MesProductProcedure mpp : product.getMesProductProcedures()) {
			procedureIds.add(mpp.getId());
		}
		MesDriver driver = dao.findOne(chooseDriver2);
		List<MesDriverPoints> pointses = driver.getMesDriverPointses();
		//对应一个设备只会生产某个产品的一个工序，（一般情况）
		for(MesDriverPoints mp : pointses) {
			if(MesPointType.TYPE_PRODUCT_PROCEDURE.equals(mp.getMesPoints().getMesPointType().getPointtypekey())) {
				Long tempProcedureId = mp.getMesPoints().getCurrentMesProductProcedure() == null ? 0L : (
						mp.getMesPoints().getCurrentMesProductProcedure().getId() == null ? 0L : mp.getMesPoints().getCurrentMesProductProcedure().getId());
				if(procedureIds.contains(tempProcedureId)) {
					currentProcedureId = tempProcedureId;
					break;
				}
			}
		}
		//计算合格和不合格数
		if(currentProcedureId == 0) {
			return resMap;
		}
//		HBasePageModel hBasePageModel = new HBasePageModel();
//		AnalyzeSearch analy = new AnalyzeSearch();
//		analy.setBegin(start);
//		analy.setEnd(end);
//		analy.setProductId(product.getId());
//		analy.setProductLineId(chooseProductLine2);
//		analy.setProductProcedureId(currentProcedureId);
//		hBasePageModel.setPagable(false);
//		hBasePageModel.setAnalyzeSearch(analy);
//		List<ProductionRecord> list = productionSchedulerPageSearch.getProductionRecordPagable(hBasePageModel);
//        if (list.size() < 1) {
//            return resMap;
//        }
//        for (ProductionRecord productionRecord : list) {
//            if (!productionRecord.getStatus().equals("OK")) {
//                resMap.put("failure", resMap.get("failure") + 1);
//            }
//        }
//        resMap.put("yield", new Long(list.size()));

		MesDataRowkeyUtil.setEntityManager(entityManager);
		// 新版工序数据收集
		List<String> totalCount = MesDataRowkeyUtil.getCountByPageSelect(String.valueOf(chooseFactory2),
		        String.valueOf(chooseProductLine2),
		        String.valueOf(chooseDriver2),
		        String.valueOf(product.getModelnum()),
		        String.valueOf(currentProcedureId),
		        String.valueOf(start.getTime()).substring(0, 10),
		        String.valueOf(end.getTime()).substring(0, 10),
		        "",
		        "");
        if (totalCount.size() < 1) {
            return resMap;
        }
        List<String> ngCount = MesDataRowkeyUtil.getCountByPageSelect(String.valueOf(chooseFactory2),
                String.valueOf(chooseProductLine2),
                String.valueOf(chooseDriver2),
                String.valueOf(product.getModelnum()),
                String.valueOf(currentProcedureId),
                String.valueOf(start.getTime()).substring(0, 10),
                String.valueOf(end.getTime()).substring(0, 10),
                "NG",
                "");
        resMap.put("failure", Long.valueOf(ngCount.size()));
        resMap.put("yield", new Long(totalCount.size()));

		return resMap;
	}
	
	@Override
	public List<Map<String, String>> findDriverPropertyByHbase(Timestamp start, Timestamp end, String factoryId,
			String productLineId, List<Long> driverPropertyIds, Long driverId, String rowKey, HBasePageModel page) throws IOException {
		// 设置总件数
		page.setTotalCount(getPageCount(start, end, factoryId, productLineId, driverPropertyIds, driverId));
		// 设置页数
		int totalPage = Integer.valueOf(String.valueOf(page.getTotalCount()))/10;
        List<SearchFilter> list = Lists.newArrayList();
        String beginTime = String.valueOf(start.getTime());
        beginTime = beginTime.substring(0, 10);
        list.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, beginTime));
        String endTime = String.valueOf(end.getTime());
        endTime = endTime.substring(0, 10);
        list.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, end.getTime()));
        list.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, productLineId));
        list.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, driverId));
        list.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, factoryId));
        list.add(new SearchFilter("driverPropertyId", Operator.IN, driverPropertyIds.toArray()));

        Specification<MesDataDriverProperty> specification = DynamicSpecifications
                .bySearchFilter(MesDataDriverProperty.class, list);
		page.setTotalPage(totalPage);
		page.setOrderField("mesDataMultiKey.insertTimestamp");
		page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
		List<MesDataDriverProperty> mesDataDriverPropertyList = mesDataDriverPropertyService.findPage(specification, page);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MesDriver driver = dao.findOne(driverId);
        List<Map<String, String>> listOut = Lists.newArrayList();
        Map<String, String> map = null;
        MesDataMultiKey mesDataMultiKey = null;
        if(null != mesDataDriverPropertyList)
        for(MesDataDriverProperty obj : mesDataDriverPropertyList) {
            mesDataMultiKey = obj.getMesDataMultiKey();
            map = Maps.newHashMap();
            map.put("driverName", driver.getName());
            String timeOut = String.valueOf(mesDataMultiKey.getInsertTimestamp());
            timeOut = timeOut + "000";
            map.put("time", DateUtils.unixTimestampToDate(Long.valueOf(timeOut)));
//            map.put("time", String.valueOf(mesDataMultiKey.getInsertTimestamp()));
            map.put("d" + obj.getDriverPropertyId(), obj.getDriverPropertyValue());
            listOut.add(map);
        }

        return listOut;
    }

	/**
	 * 获取当前总件数
	 * @param start
	 * @param end
	 * @param factoryId
	 * @param productLineId
	 * @param driverPropertyIds
	 * @param driverId
	 * @return
	 */
	private int getPageCount(Timestamp start, Timestamp end, String factoryId,
			String productLineId, List<Long> driverPropertyIds, Long driverId) {
		int result = 0;
        try {
            List<SearchFilter> list = Lists.newArrayList();
            String beginTime = String.valueOf(start.getTime());
            beginTime = beginTime.substring(0, 10);
            list.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, beginTime));
            String endTime = String.valueOf(end.getTime());
            endTime = endTime.substring(0, 10);
            list.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, endTime));
            list.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, productLineId));
            list.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, driverId));
            list.add(new SearchFilter("driverPropertyId", Operator.IN, driverPropertyIds.toArray()));

            Specification<MesDataDriverProperty> specification = DynamicSpecifications
                    .bySearchFilter(MesDataDriverProperty.class, list);
            
            List<String> sortProperties = Lists.newArrayList();
            sortProperties.add("mesDataMultiKey.insertTimestamp");
            Sort sort = new Sort(Sort.Direction.DESC, sortProperties);
            List<MesDataDriverProperty> rs = mesDataDriverPropertyService.findAll(specification, sort);
            if(null != rs && 0 < rs.size())
                result = rs.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
//        List<Result> scanResult = hTemplate.execute(HbaseUtil.HBASE_TABLE_DRIVERPROPERTYANDSTATUS, new TableCallback<List<Result>>() {
//            List<Result> list = new ArrayList<>();
//            @Override
//            public List<Result> doInTable(HTableInterface table) throws Throwable {
//
//                Scan scan = new Scan();
//                scan.setTimeRange(start.getTime(), end.getTime());
//                scan.setBatch(10);
//                List<Filter> filters = new ArrayList<>();
//                Filter filterPreFixFilter = new PrefixFilter(Bytes.toBytes(factoryId + "_" + productLineId + "_" + driverId));
//                filters.add(filterPreFixFilter);
//                // 表中存在以property打头的列族，过滤结果为该列族所有行
//                FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
//                        new BinaryPrefixComparator(Bytes.toBytes("property")));
//                filters.add(familyFilter);
//                byte[][] prefixes = new byte[driverPropertyIds.size()][];
//                for(int i = 0;i<driverPropertyIds.size();i++) {
//                	prefixes[i] = Bytes.toBytes(String.valueOf(driverPropertyIds.get(i)));
//                }
//                MultipleColumnPrefixFilter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(prefixes);
//                filters.add(multipleColumnPrefixFilter);
//                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
//                scan.setFilter(filterList);
//
//                ResultScanner rscanner = table.getScanner(scan);
//                for(Result result : rscanner){
//                    list.add(result);
//                }
//                return list;
//            }
//
//        });
//
//        if(null != scanResult && 0 != scanResult.size()) {
//        	result = scanResult.size();
//        }
//		return result;
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
//		System.out.println("---------------start---------------------------------------------------------------------------------------------------------------");
		Map<String, String> cellMap = new HashMap<String, String>();
		for (Cell cell : result.listCells()) {
			String rowkey = Bytes.toString(cell.getRowArray(),
					cell.getRowOffset(), cell.getRowLength());
			String cf = Bytes.toString(cell.getFamilyArray(),
					cell.getFamilyOffset(), cell.getFamilyLength());
			String qf = Bytes.toString(cell.getQualifierArray(),
					cell.getQualifierOffset(), cell.getQualifierLength());
			String value = Bytes.toString(cell.getValueArray(),
					cell.getValueOffset(), cell.getValueLength());
//			System.out.println("-------------rowkey--------------" + rowkey);
//			System.out.println("----------------cf-----------" + cf);
//			System.out.println("-----------------qf----------" + qf);
//			System.out.println("----------------value-----------" + value);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_ROWKEY, rowkey);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_COLUMNFAMILY, cf);
			cellMap.put("rowkey", rowkey);
			cellMap.put(qf, value);
		}
//		System.out.println("---------------end---------------------------------------------------------------------------------------------------------------");
		return cellMap;
	}
}
