package com.its.frd.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.CompanyinfoDao;
import com.its.frd.dao.MesDriverDao;
import com.its.frd.dao.MesDriverPointsDao;
import com.its.frd.dao.MesDriverStatsDao;
import com.its.frd.dao.MesDriverTypePropertyDao;
import com.its.frd.dao.MesEnergyDao;
import com.its.frd.dao.MesProductDao;
import com.its.frd.dao.MesProductionDao;
import com.its.frd.dao.MesProductlineDao;
import com.its.frd.echarts.entity.BarChart;
import com.its.frd.echarts.entity.BarChart.SeriesItem;
import com.its.frd.echarts.entity.DriverProperty;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDataWeg;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesDriverStats;
import com.its.frd.entity.MesEnergy;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProduction;
import com.its.frd.entity.MesProductline;
import com.its.frd.schedule.DriverRecordSchedulerPageSearch;
import com.its.frd.service.MesDataWegService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.TypeScope;
import com.its.frd.util.DateUtils;
import com.its.frd.util.DecimalCalculate;
import com.its.frd.util.HBasePageModel;
import com.its.frd.util.HbaseUtil;
import com.its.statistics.service.StatisticsService;
import com.its.statistics.vo.AnalyzeSearch;
import com.its.statistics.vo.DriverRecord;

@Service
public class ProductAndEnergyAndDriverChartServiceImp implements ProductAndEnergyAndDriverChartService {
	
	@Resource
	private MesProductionDao mesProDao; //产量
	@Resource
	private MesEnergyDao energyDao;  //能耗
	@Resource
	private MesDriverDao driverDao;  //设备
	@Resource
	private CompanyinfoDao cpDao;    //公司(工厂)
	@Resource
	private MesProductDao productDao; //产品
	@Resource
	private MesProductlineDao productLineDao; //产线
	@Resource
	private MesDriverPointsDao driverPointDao; //设备测点
	@Resource
	private MesDriverStatsDao driverStatsDao;
	@Resource
	private MesDriverTypePropertyDao driverTypePropertyDao;
	//@Resource
	private HbaseTemplate hbaseTemp;  //操作HBase库的模板
	@Resource
	private StatisticsService statisticsService;
	@Autowired
	private DriverRecordSchedulerPageSearch driverRecordSchedulerPageSearch;
	@Autowired
	private MesDriverService mesDriverService;
    @Autowired
    private MesDataWegService mesDataWegService;;
	
	@Override
	public String getDriverStatus(Long driverId) {
		
		return null;
	}

	
	/*@Override
	public Map<BarKeyType, Object> getDriverStatsBarChart(Timestamp startDate, Timestamp endDate, Long driverId,Long productLineId,
			DateType dateType) {
		
		 * 1.当driverId不为空时,只统计该设备的产量;
		 * 2.当driverId为空时,则统计该产线上设备的产量
		 
		if(startDate == null || endDate == null || productLineId == null)
			return null;
		Map<BarKeyType, Object> resultMap = new HashMap<>();
		//设备时间段的产量计数集合
		List<MesDriverStats> statsList = null;
		//X轴数据 
		List<SeriesItem> series = new ArrayList<>();
		List<String> xAxis_data = new ArrayList<>();
		List<Double> driverTotals = new ArrayList<>();
		BarChart.SeriesItem seriesItem = new BarChart().new SeriesItem();
		Double count = 0d;
		if(driverId != null){
			统计单个设备
			statsList = driverStatsDao.findByDriverIdAndTimeSpan(driverId, startDate, endDate);
			String driverName = driverDao.findOne(driverId).getName();
			count = (statsList.get(statsList.size()-1).getCount().doubleValue())-(statsList.get(0).getCount().doubleValue());
			seriesItem = new BarChart().new SeriesItem();
			seriesItem.setName(driverName);
			driverTotals.add(count);
			seriesItem.setData(driverTotals);
			series.add(seriesItem);
			xAxis_data.add(driverName);
		}else{
			统计一条产线上的设备
			List<MesDriver> driverList = this.getDriverListForId(TypeScope.productline, productLineId);
			if(driverList == null || driverList.size() < 1){
				return null;
			}
			List<String> driverNams = new ArrayList<String>();
			for(MesDriver md : driverList){
				statsList = driverStatsDao.findByDriverIdAndTimeSpan(md.getId(), startDate, endDate);
				count = (statsList.get(statsList.size()-1).getCount().doubleValue())-(statsList.get(0).getCount().doubleValue());
				driverTotals.add(count);
				count = 0d;
				driverNams.add(md.getName());
			}
			//X轴设备名称
			xAxis_data.addAll(driverNams);
			seriesItem.setName("产量");
			seriesItem.setData(driverTotals);
			series.add(seriesItem);
		}
		resultMap.put(BarKeyType.seriesItemList, series);  //series
		resultMap.put(BarKeyType.xAxisOfData, xAxis_data); //xAxis中的data
		return resultMap;
	}*/
	
	
	
	
	/**
	 * 获取设备集合的主键id集合
	 * @param drivers
	 * @return
	 */
	private List<Long> getDriverIdsByDrivers(List<MesDriver> drivers){
		List<Long> ids = new ArrayList<>();
		for(MesDriver driver : drivers)
			ids.add(driver.getId());
		return ids;
	}
	
	/*@Override
	public Map<LineElement,Object> getDriverPropertyLineChart(Timestamp startDate,Timestamp endDate,Long driverPropertyId){
		if(startDate == null || endDate == null || driverPropertyId == null)
			return null;
		Map<LineElement,Object> resultMap = new HashMap<>();
		String mac = this.getMacByDriverPropertyId(driverPropertyId) + ":";
		String start_rowkey = String.valueOf(startDate.getTime());
		String stop_rowkey = String.valueOf(endDate.getTime());
		String columnCondition = this.getPointKey(driverPropertyId);
		List<List<DriverProperty>> propertyValueLists = hbaseTemp.find(HbaseUtil.TABLE_NAME, 
				HbaseUtil.getResultScanner(start_rowkey, stop_rowkey, mac, columnCondition, "",0), 
				new RowMapper<List<DriverProperty>>() {
			@Override
			public List<DriverProperty> mapRow(Result result, int arg1) throws Exception {
				List<Cell> ceList =  result.listCells(); //列的数据集合
				System.out.println(Arrays.toString(ceList.toArray()));
				List<DriverProperty> driverPropertys = new ArrayList<>();
				String[] value_splits = null;
				DriverProperty driverProperty = null;
				for(Cell cell : ceList){
					driverProperty = new DriverProperty();
					String value =Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
					if(value != null && !"".equals(value.trim())){
						value_splits = value.split(":");
						try{
							driverProperty.setPointValue(Double.valueOf(value_splits[3]));
							driverProperty.setUpdateTime(new Date(Long.valueOf(value_splits[5]) * 1000));
						}catch(Exception e){
						}
					}
					driverPropertys.add(driverProperty);
				}
				return driverPropertys;
			}
		});
		//设备各个属性值及对应的时间点
		List<DriverProperty> propertyValues = new ArrayList<>();
		if(propertyValueLists != null && propertyValueLists.size() > 0){
			for(List<DriverProperty> lst: propertyValueLists){
				propertyValues.addAll(lst);
			}
		}
			//propertyValues = propertyValueLists.get(0);
		List<String> xAxisData = new ArrayList<>();
		List<Double> seriesData = new ArrayList<>();
		for(DriverProperty  dp : propertyValues){
			xAxisData.add(DateUtils.getYYYYMMDDHHMMSSDayStr(dp.getUpdateTime()));
			seriesData.add(dp.getPointValue());
		}
		resultMap.put(LineElement.seriesData, seriesData);
		resultMap.put(LineElement.xAxisData, xAxisData);
		resultMap.put(LineElement.titleText, this.getPropertyName(driverPropertyId));
		resultMap.put(LineElement.unit, this.getPointUnitName(driverPropertyId));
		return resultMap;
	}*/
	
	/**
	 * 
	 * @param tableName	表名
	 * @param familly	族名
	 * @param start_rowkey	row开始匹配(模糊)
	 * @param stop_rowkey
	 * @param rowCondition	row的条件
	 * @param columCondition	colum的条件
	 * @param valueCondition	value的条件
	 * @return
	 */
	private Scan getScan(String tableName,String familly,String start_rowkey,String stop_rowkey,String rowCondition,String columnCondition,String valueCondition){
		Scan scan = new Scan();
		
		return scan;
	}
	
	/**
	 * 获取测点的单位
	 * @param propertyId
	 * @return
	 */
	private String getPointUnitName(Long propertyId,Long driverId){
		if(propertyId == null)
			return null;
		return this.getPoint(propertyId,driverId).getUnits();
	}
	
	/**
	 * 获取设备属性名称
	 * @param propertyId
	 * @return
	 */
	private String getPropertyName(Long propertyId){
		if(propertyId == null)
			return null;
		return driverTypePropertyDao.findOne(propertyId).getPropertyname();
	}
	
	/**
	 * 获取mac地址
	 * @param propertyId 设备属性id
	 * @return
	 */
	private String getMacByDriverPropertyId(Long propertyId,Long driverId){
		if(propertyId == null)
			return null;
		MesPoints point = this.getPoint(propertyId,driverId);
		if(point != null)
			return point.getMesPointGateway().getMac();
		return null;
	}
	
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
	public Map<BarKeyType, Object> getRuntimeDataForTotal(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,Long id,DateType dateType,String ifIsDriver) throws ParseException {
		if(id == null || dateType == null || dateType.equals(""))
			return null; 
		//当为自定义时间时,如果开始时间或者结束时间为空则返回null;
		if(dateType.equals(DateType.defineDate) && (startDate == null || endDate == null))
			return null;
		//开始时间不能大于结束时间
		if(startDate.getTime() > endDate.getTime())
			return null;
		//返回结果集
		Map<BarKeyType,Object> resultMap = new HashMap<>();
		//xAxis中data的时间段间隔集合
		List<String> xAxis_data = new ArrayList<String>();
		//series的list集合数据,以设备
		List<SeriesItem> series = new ArrayList<SeriesItem>();
		//拆分时间段
		List<Timestamp> times = this.getDateList(dateType,startDate,endDate);
		List<MesDriver> driverList = new ArrayList<>();
		if("driver".equals(ifIsDriver)){
			driverList.add(driverDao.findOne(id));
		}else{
			if(TypeScope.productline.equals(typeScope)){
				//当统计范围是产线时
				List<MesProductline> lines = new ArrayList<>();
				lines.add(new MesProductline(id));
				driverList = this.getDriverListForProductLineList(lines);
			}else if(TypeScope.factory.equals(typeScope)){
				//当统计范围是工厂时
				//查询出该工厂下所有的产线
				Companyinfo factorys = cpDao.findOne(id);
				List<MesProductline> lines = this.getProductlineOfFactory(factorys);
				driverList = this.getDriverListForProductLineList(lines);
			}else if(TypeScope.company.equals(typeScope)){
				//当统计范围是公司时
				//查询公司下一共多少工厂
				List<Companyinfo> factorys = cpDao.findByParentid(id);
				List<MesProductline> lines = this.getProductlineOfFactory(factorys);
				driverList = this.getDriverListForProductLineList(lines);			
			}
		}
		//legend中的data集合list,即产品名称集合
		List<String> legend_data = new ArrayList<String>();
		legend_data.add("运行时间");
		legend_data.add("停机时间");
		BarChart.SeriesItem seriesItem1 = new BarChart().new SeriesItem();
		BarChart.SeriesItem seriesItem2 = new BarChart().new SeriesItem();
		seriesItem1.setName("运行时间"); //series设置name值
		seriesItem2.setName("停机时间");
		//series里面的数据
		List<Double> series_data1 = new ArrayList<Double>();
		List<Double> series_data2 = new ArrayList<Double>();
		Date start = times.get(0); //开始时间
		Date end = times.get(times.size()-1); //结束时间
		for(MesDriver md : driverList){
			xAxis_data.add(md.getName());
			double runTime = 0;
			double totalTime = 0;
			List<DriverRecord> driverRecordList = this.getDriverRecordRowkeys(times.get(0), times.get(times.size()-1), md.getId());
			int tempRunTime = mesDriverService.getDriverRunTime(driverRecordList,md.getId());
			if(tempRunTime!=0){
				runTime = DecimalCalculate.div(tempRunTime, 60, 2);
			}
			totalTime = DecimalCalculate.div((end.getTime()-start.getTime()), (60*60*1000), 2);
			series_data1.add(runTime);
			series_data2.add(totalTime-runTime);
		}
		seriesItem1.setData(series_data1);
		seriesItem2.setData(series_data2);
		series.add(seriesItem1);
		series.add(seriesItem2);
		resultMap.put(BarKeyType.legendData, legend_data); //legend中的data
		resultMap.put(BarKeyType.xAxisOfData, xAxis_data); //xAxis中的data
		resultMap.put(BarKeyType.seriesItemList, series);  //series
		return resultMap;
	}
	
	@Override
	public Map<BarKeyType, Object> getEnergyDataForTotal(Timestamp startDate, Timestamp endDate,
			EnergyType energyType, Long driverId, DateType dateType) {
		if(driverId == null)
			return null;
		//当为自定义时间时,如果开始时间或者结束时间为空则返回null;
		if(dateType.equals(DateType.defineDate) && (startDate == null || endDate == null))
			return null;
		//开始时间不能大于结束时间
		if(startDate.getTime() > endDate.getTime())
			return null;
		//返回结果集
		Map<BarKeyType,Object> resultMap = new HashMap<>();
		//series的list集合数据,只有一个SeriesItem对象
		List<SeriesItem> series = new ArrayList<>();
		//拆分时间段
		List<Timestamp> times = this.getDateList(dateType,startDate,endDate);
		//根据设备id查找设备
		MesDriver driver = driverDao.findOne(driverId);
		//xAxis中data的为电能或水能,或气能
		List<String> xAxis_data = new ArrayList<>();
		xAxis_data = this.getDateListStr(dateType, startDate, endDate);
		List<String> legend_data = new ArrayList<String>();
		legend_data.add(driver.getName()+this.getEnergyName(energyType));
		//根据设备,时间查询时间段内的能耗集合
		List<MesEnergy> energyList = null;
		if(times != null){
			energyList = energyDao.findByUpdatetimeBetweenAndmesDriver(times.get(0), times.get(times.size()-1),driverId);
		}
		
        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(driverId)));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,String.valueOf(startDate.getTime()).replace("000", "")));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(endDate.getTime()).replace("000", "")));
        Specification<MesDataWeg> specification = DynamicSpecifications
                .bySearchFilter(MesDataWeg.class, searchList);
        List<MesDataWeg> mesDataWegList = mesDataWegService.findAll(specification);
        List<MesEnergy> Driverenergy = Lists.newArrayList();
        MesEnergy mesEnergy = null;
        for(MesDataWeg obj : mesDataWegList) {
            mesEnergy = new MesEnergy();
            mesEnergy.setEnergytype(obj.getType());
            mesEnergy.setMesDriver(driver);
            mesEnergy.setValue(Double.valueOf(obj.getMetaValue()));
            String updateTimeStr = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());
            updateTimeStr = updateTimeStr + "000";
            updateTimeStr = DateUtils.unixTimestampToDate(Long.valueOf(updateTimeStr));
            mesEnergy.setUpdatetime(Timestamp.valueOf(updateTimeStr));
            String searchEnergyType = "";
            if("ELECTRIC".equals(obj.getType())) {
                searchEnergyType = "electric";
            } else if("GAS".equals(obj.getType())) {
                searchEnergyType = "gas";
            } else if("WATER".equals(obj.getType())) {
                searchEnergyType = "water";
            }
            mesEnergy.setEnergytype(searchEnergyType);
            Driverenergy.add(mesEnergy);
        }
        
		series = this.getEnergySeriesForBarChart(times,Driverenergy,legend_data,energyType);
		resultMap.put(BarKeyType.legendData, legend_data); //xAxis中的legend
		resultMap.put(BarKeyType.xAxisOfData, xAxis_data); //xAxis中的data
		resultMap.put(BarKeyType.seriesItemList, series);  //series
		return resultMap;
	}
	
	@Override
	public Map<BarKeyType, Object> getEnergyDataForDriver(Timestamp startDate, Timestamp endDate, TypeScope typeScope,
			EnergyType energType, Long id, DateType dateType) {
		if(id == null)
			return null;
		//当为自定义时间时,如果开始时间或者结束时间为空则返回null;
		if(dateType.equals(DateType.defineDate) && (startDate == null || endDate == null))
			return null;
		//开始时间不能大于结束时间
		if(startDate.getTime() > endDate.getTime())
			return null;
		//返回结果集
		Map<BarKeyType,Object> resultMap = new HashMap<>();
		//series的list集合数据,只有一个SeriesItem对象
		List<SeriesItem> series = new ArrayList<>();
		//拆分时间段
		List<Timestamp> times = this.getDateList(dateType,startDate,endDate);
		//查询所有和公司id,或工厂id,或产线id相关的设备集合
		List<MesDriver> driverList = this.getDriverListForId(typeScope, id);
		//xAxis中data的设备名称集合
		List<String> xAxis_data = this.getDriverNameList(driverList);
		//根据设备,时间查询时间段内的能耗集合
		List<MesEnergy> energyList = null;
		if(times != null)
			energyList = energyDao.findByUpdatetimeBetween(times.get(0), times.get(times.size()-1));
		
        List<SearchFilter> searchList = Lists.newArrayList();
//        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, String.valueOf(driverid)));
        String time = String.valueOf(startDate.getTime());
        if(time.contains("000000"))
            time = time.replace("000000", "000");
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,time));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(endDate.getTime()).replace("000", "")));
        String searchEnergyType = "";
        if("electric".equals(energType.name())) {
            searchEnergyType = "ELECTRIC";
        } else if("gas".equals(energType.name())) {
            searchEnergyType = "GAS";
        } else if("water".equals(energType.name())) {
            searchEnergyType = "WATER";
        }
        searchList.add(new SearchFilter("type", Operator.EQ, searchEnergyType));
        Specification<MesDataWeg> specification = DynamicSpecifications
                .bySearchFilter(MesDataWeg.class, searchList);
        List<MesDataWeg> mesDataWegList = mesDataWegService.findAll(specification);
        List<MesEnergy> Driverenergy = Lists.newArrayList();
        Map<Long, MesDriver> driverMap = Maps.newHashMap();
        for(MesDriver driver : driverList) {
            driverMap.put(driver.getId(), driver);
        }
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
		/*
		 * 1.组装series中data集合数据;
		 * 2.通过循环driverList,在energyList中查找相匹配的driverid,
		 * 		如果相同则把能耗相加,但需要注意是统计的什么类型的能耗;
		 */
		BarChart.SeriesItem seriesItem = new BarChart().new SeriesItem();
		seriesItem.setName("能耗");
		seriesItem.setData(this.getEnergySeriesItemDataByEnergyType(energType, driverList, Driverenergy));
		
		series.add(seriesItem);
		
		resultMap.put(BarKeyType.xAxisOfData, xAxis_data); //xAxis中的data
		resultMap.put(BarKeyType.seriesItemList, series);  //series
		return resultMap;
	}
	
	@Override
	public Map<BarKeyType, Object> getProductionData(Timestamp startDate, Timestamp endDate, TypeScope typeScope,
			String modelnum, Long id,DateType dateType) {
		if(id == null || dateType == null || dateType.equals(""))
			return null; 
		//当为自定义时间时,如果开始时间或者结束时间为空则返回null;
		if(dateType.equals(DateType.defineDate) && (startDate == null || endDate == null))
			return null;
		//开始时间不能大于结束时间
		if(startDate.getTime() > endDate.getTime())
			return null;
		//返回结果集
		Map<BarKeyType,Object> resultMap = new HashMap<>();
		//xAxis中data的时间段间隔集合
		List<String> xAxis_data = this.getDateListStr(dateType, startDate, endDate);
		//series的list集合数据,以产品号种类来分
		List<SeriesItem> series = null;
		//拆分时间段
		List<Timestamp> times = this.getDateList(dateType,startDate,endDate);
		//legend中的data集合list,即产品名称集合
		List<String> legend_data = null;//this.getDateListStr(dateType, startDate, endDate);
		//根据时间查询出各种产品时间段的合格,不合格量
		List<MesProduction> proList = null;
		
		//TODO:以公司和产线为单位的统计计算先不做了，用的时候再做 -----start
//		if(times != null)
//			proList = this.getProductionList(modelnum, times.get(0), times.get(times.size()-1));
        //TODO:以公司和产线为单位的统计计算先不做了，用的时候再做 -----end
		
		Long count = 0l;//总产量
		Long passCount = 0l; //合格数
		if(TypeScope.productline.equals(typeScope)){
			//当统计范围是产线时
			List<MesProductline> lines = new ArrayList<>();
			lines.add(new MesProductline(id));
			//合格数,不合格数的map
			Map<PassType,Object> countMap = this.getCountMap(lines, proList);
			count = (Long)countMap.get(PassType.PASSCOUNT) + (Long)countMap.get(PassType.FAILCOUNT);
			passCount = (long)countMap.get(PassType.PASSCOUNT);
			//属于该产线的product的集合
			List<MesProduction> productionList = (List<MesProduction>) countMap.get(PassType.matchProductList);
			//查询出来的产量集合中有多少种产品号(productNum)
			List<String> producntNumList = this.getProductNum(productionList);
			legend_data = this.getProductName(producntNumList);
			//计算series
			series = this.getSeriesForBarChart(producntNumList,times,legend_data,productionList);
			
		}else if(TypeScope.factory.equals(typeScope)){
			//当统计范围是工厂时
			//查询出该工厂下所有的产线
			Companyinfo factorys = cpDao.findOne(id);
			List<MesProductline> lines = this.getProductlineOfFactory(factorys);
			//合格数,不合格数的map;从而计算出总产量,及合格产量数
			Map<PassType,Object> countMap = this.getCountMap(lines, proList);
			count = (Long)countMap.get(PassType.PASSCOUNT) + (Long)countMap.get(PassType.FAILCOUNT);
			passCount = (long)countMap.get(PassType.PASSCOUNT);
			//查询出和产线相关的所有产品集合
			List<MesProduction> productionList = (List<MesProduction>) countMap.get(PassType.matchProductList);
			//查询出产量集合中有多少中产品号
			List<String> producntNumList = this.getProductNum(productionList);
			legend_data = this.getProductName(producntNumList);
			//计算series
			series = this.getSeriesForBarChart(producntNumList, times, legend_data, productionList);
			
		}else if(TypeScope.company.equals(typeScope)){
			//当统计范围是公司时
			//查询公司下一共多少工厂
			List<Companyinfo> factorys = cpDao.findByParentid(id);
			List<MesProductline> lines = this.getProductlineOfFactory(factorys);
//			List<Result> productList = Lists.newArrayList();
//			for(MesProductline mesline : lines) {
//				productList.addAll(new HbaseUtil().getResultListByHbase(mesline.getCompanyinfo().getId(),
//						mesline.getId(), null, null, null, null, startDate, endDate, false));
//			}
			Map<String, Object>  rsCountMap = statisticsService.getPassNoPassCountByMysql(id, null, null,
//			Map<String, Object>  rsCountMap = statisticsService.getPassNoPassCountByHbase(id, null, null,
					(StringUtils.isBlank(modelnum)? null : Long.valueOf(modelnum)), null, startDate, endDate);
//			Map<String, Object> totalCountMap = (Map<String, Object>) rsCountMap.get("totalCount");
			count = (long)rsCountMap.get("totalCount");
//			passCount = Long.valueOf(String.valueOf(totalCountMap.size() - (Long)rsCountMap.get("nopassCount")));
			passCount = (long)rsCountMap.get("nopassCount");
			List<MesProduction> productionList = Lists.newArrayList();//getMesProductionList(productList);
			MesProduction oneMoldel = new MesProduction();
			oneMoldel.setProductnum(modelnum);
			oneMoldel.setTotalCount(count);
			productionList.add(oneMoldel);
			/*
			//当统计范围是公司时
			//查询公司下一共多少工厂
			List<Companyinfo> factorys = cpDao.findByParentid(id);
			List<MesProductline> lines = this.getProductlineOfFactory(factorys);
			//合格数,不合格数的map;从而计算出总产量,及合格产量数
			Map<PassType,Object> countMap = this.getCountMap(lines, proList);
			count = (Long)countMap.get(PassType.PASSCOUNT) + (Long)countMap.get(PassType.FAILCOUNT);
			passCount = (long)countMap.get(PassType.PASSCOUNT);
			//查询出和产线相关的所有产品集合
			List<MesProduction> productionList = (List<MesProduction>) countMap.get(PassType.matchProductList);
			*/
			//查询出产量集合中有多少中产品号
			List<String> producntNumList = this.getProductNum(productionList);
			legend_data = this.getProductName(producntNumList);
			//计算series
			series = this.getSeriesForBarChart(producntNumList, times, legend_data, productionList);

		}
		resultMap.put(BarKeyType.count, count);
		resultMap.put(BarKeyType.passCount, passCount);
		resultMap.put(BarKeyType.legendData, legend_data); //legend中的data
		resultMap.put(BarKeyType.xAxisOfData, xAxis_data); //xAxis中的data
		resultMap.put(BarKeyType.seriesItemList, series);  //series
		return resultMap;
	}
	
	/**
	 * 该方法废除，原因没用调用的地方
	 */
	@Override
	public Map<PassType,Object> getProductCountNum(Timestamp startDate, Timestamp endDate, TypeScope typeScope, 
			String modelnum, PassType passType,Long id,DateType dateType) {
		if(id == null || startDate == null || endDate == null)
			return null; 
		if(startDate.getTime() > endDate.getTime()){
			return null;			
		}
		List<Timestamp> dateList = this.getDateList(dateType, startDate, endDate);
		startDate = dateList.get(0);
		endDate = dateList.get(dateList.size()-1);
		//根据时间查询出各种产品时间段的产量集合
		List<MesProduction> proList = this.getProductionList(modelnum, startDate, endDate);
		if(TypeScope.productline.equals(typeScope)){
			//当统计范围是产线时
			List<MesProductline> lines = new ArrayList<>();
			lines.add(new MesProductline(id));
			return this.getCountMap(lines, proList);
		}else if(TypeScope.factory.equals(typeScope)){
			//当统计范围是工厂时
			//包含该工厂下的子工厂...
			List<Companyinfo> factorys = this.getFactoryListByFactoryId(id);
			List<MesProductline> lines = this.getProductlineOfFactory(factorys);
			return this.getCountMap(lines, proList);
		}else if(TypeScope.company.equals(typeScope)){
			//当统计范围是公司时
			List<Companyinfo> factorys = cpDao.findByParentid(id);
			List<Companyinfo> sonFctys = new ArrayList<>();
			for(Companyinfo f : factorys){
				sonFctys.addAll(this.getFactoryListByFactoryId(f.getId()));
			}
			List<MesProductline> lines = this.getProductlineOfFactory(sonFctys);
			return this.getCountMap(lines, proList);
		}
		//当不符合条件时,直接返回null
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<PassType,Object> getProductCountNumByHase(Timestamp startDate, Timestamp endDate, TypeScope typeScope, 
			String modelnum, PassType passType,Long id,DateType dateType) {
		if(id == null || startDate == null || endDate == null)
			return null; 
		if(startDate.getTime() > endDate.getTime()){
			return null;			
		}
		List<Timestamp> dateList = this.getDateList(dateType, startDate, endDate);
		startDate = dateList.get(0);
		endDate = dateList.get(dateList.size()-1);
		Long modelNumLong = null;
		modelNumLong = modelnum == "" || null == modelnum ? null :Long.valueOf(modelnum);
		Map<String, Object>  rsCountMap = statisticsService.getPassNoPassCountByMysql(null, null, null, modelNumLong, null, startDate, endDate);
//		Map<String, Object>  rsCountMap = statisticsService.getPassNoPassCountByHbase(null, null, null, modelNumLong, null, startDate, endDate);
		Map<PassType,Object> resultMap = Maps.newHashMap();
//		Map<String, Object> totalCountMap = (Map<String, Object>) rsCountMap.get("totalCount");
		Long okCount = (Long)rsCountMap.get("okCount");
		Long nopassCount = (Long)rsCountMap.get("nopassCount");
		resultMap.put(PassType.PASSCOUNT, okCount);
		resultMap.put(PassType.FAILCOUNT, nopassCount);
		return resultMap;
		// resultMap.put(PassType.matchProductList, matchProList);
		
//		//根据时间查询出各种产品时间段的产量集合
//		List<MesProduction> proList = this.getProductionList(modelnum, startDate, endDate);
//		if(TypeScope.productline.equals(typeScope)){
//			//当统计范围是产线时
//			List<MesProductline> lines = new ArrayList<>();
//			lines.add(new MesProductline(id));
//			return this.getCountMap(lines, proList);
//		}else if(TypeScope.factory.equals(typeScope)){
//			//当统计范围是工厂时
//			//包含该工厂下的子工厂...
//			List<Companyinfo> factorys = this.getFactoryListByFactoryId(id);
//			List<MesProductline> lines = this.getProductlineOfFactory(factorys);
//			return this.getCountMap(lines, proList);
//		}else if(TypeScope.company.equals(typeScope)){
//			//当统计范围是公司时
//			List<Companyinfo> factorys = cpDao.findByParentid(id);
//			List<Companyinfo> sonFctys = new ArrayList<>();
//			for(Companyinfo f : factorys){
//				sonFctys.addAll(this.getFactoryListByFactoryId(f.getId()));
//			}
//			List<MesProductline> lines = this.getProductlineOfFactory(sonFctys);
//			return this.getCountMap(lines, proList);
//		}
//		//当不符合条件时,直接返回null
//		return null;
	}
	
	/**
	 * 根据EnerType返回对应的中文信息
	 * @param energyType
	 * @return
	 */
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
	 * 暂时弃用
	 * 获取能耗柱状图中seriesItem中的data集合
	 * @param drivers  设备列表
	 * @param energys  能耗表
	 * @param energyType  统计哪种类型的能耗,例如电能,水能,气能
	 * @return
	 */
	private List<Double> getEnergySeriesItemData(EnergyType energyType,List<MesDriver> drivers
			,List<MesEnergy> energys){
		List<Double> driverEnergyCounts = new ArrayList<>();
		if(EnergyType.all.equals(energyType)){
			//统计设备的各种能耗总和
			Double count = 0D;
			for(MesDriver driver : drivers){
				count = 0D;
				for(MesEnergy energy : energys){
					if(driver.getId().compareTo(
							energy.getMesDriver()!=null?energy.getMesDriver().getId():null)==0){
							count += energy.getValue();
					}
				}
				driverEnergyCounts.add(count);
			}
		}else if(EnergyType.electric.equals(energyType)){
			//统计设备的电能
			Double count = 0D;
			for(MesDriver driver : drivers){
				count = 0D;
				for(MesEnergy energy : energys){
					if(driver.getId().compareTo(
							energy.getMesDriver()!=null?energy.getMesDriver().getId():null)==0){
						if(EnergyType.electric.equals(energy.getEnergytype())){
							count += energy.getValue();
						}
					}
				}
				driverEnergyCounts.add(count);
			}
		}else if(EnergyType.water.equals(energyType)){
			//统计设备的水能
			Double count = 0D;
			for(MesDriver driver : drivers){
				count = 0D;
				for(MesEnergy energy : energys){
					if(driver.getId().compareTo(
							energy.getMesDriver()!=null?energy.getMesDriver().getId():null)==0){
						if(EnergyType.water.equals(energy.getEnergytype())){
							count += energy.getValue();
						}
					}
				}
				driverEnergyCounts.add(count);
			}
		}else if(EnergyType.gas.equals(energyType)){
			//统计设备的气能
			Double count = 0D;
			for(MesDriver driver : drivers){
				count = 0D;
				for(MesEnergy energy : energys){
					if(driver.getId().compareTo(
							energy.getMesDriver()!=null?energy.getMesDriver().getId():null)==0){
						if(EnergyType.gas.equals(energy.getEnergytype())){
							count += energy.getValue();
						}
					}
				}
				driverEnergyCounts.add(count);
			}
		}
		return driverEnergyCounts;
	}
	
	/**
	 * 根据能耗类型来累加该能耗类型的总量
	 * @param energyType 能耗类型
	 * @param energys    能耗记录
	 * @return
	 */
	private Double getEnergyTotalForEnergyType(EnergyType energyType,List<MesEnergy> energys){
		Double total = 0D;
		for(MesEnergy energy : energys){
			if(energyType.toString().equalsIgnoreCase(energy.getEnergytype()))
				total += energy.getValue();
		}
		return total;
	}
	
	/**
	 * 获取能耗柱状图中seriesItem中的data集合
	 * @param energyType
	 * @param drivers
	 * @param energys
	 * @return
	 */
	private List<Double> getEnergySeriesItemDataByEnergyType(EnergyType energyType,List<MesDriver> drivers
			,List<MesEnergy> energys){
		List<Double> driverEnergyCounts = new ArrayList<>();
		Double count = 0D;
		for(MesDriver driver : drivers){
			count = 0D;
			for(MesEnergy energy : energys){
				if(driver.getId().compareTo(energy.getMesDriver()!=null?energy.getMesDriver().getId():null)==0){
					if(energyType != null && energyType.toString().equalsIgnoreCase(energy.getEnergytype())){
						//传入的energyType是什么类型,就统计什么类型的能耗
						count += energy.getValue();
					}else if(EnergyType.all.equals(energyType)){
						//统计全部能耗
						count += energy.getValue();
					}
				}
			}
			driverEnergyCounts.add(count);
		}
		return driverEnergyCounts;
	}
	/**
	 * 获取设备的名称
	 * @param drivers 设备集合
	 * @return
	 */
	private List<String> getDriverNameList(List<MesDriver> drivers){
		List<String> driverNames = new ArrayList<>();
		for(MesDriver driver : drivers){
			driverNames.add(driver.getName());
		}
		return driverNames;
	}
	
	/**
	 * 获取设备列表
	 * @param typeScope  公司,或工厂,或产线
	 * @param id  公司id,或工厂id,或产线id
	 * @return
	 */
	private List<MesDriver> getDriverListForId(TypeScope typeScope,Long id){
		if(id == null || typeScope == null)
			return null;
		List<MesDriver> drivers = null;
		if(TypeScope.productline.equals(typeScope)){
			//产线
			drivers = productLineDao.findOne(id).getMesDrivers();
		}else if(TypeScope.factory.equals(typeScope)){
			//工厂
			List<Companyinfo> factorys = this.getFactoryListByFactoryId(id);
			List<MesProductline> productLines = this.getProductlineOfFactory(factorys);
			drivers = this.getDriverListForProductLineList(productLines);
		}else if(TypeScope.company.equals(typeScope)){
			//公司
			List<Companyinfo> finfos = new ArrayList<>();
			id = SecurityUtils.getShiroUser().getCompanyid();
			List<Companyinfo> factorys = this.getFactoryList(id);
			List<Companyinfo> sonFactorys = null;
			for(Companyinfo factory : factorys){
				sonFactorys = this.getFactoryListByFactoryId(factory.getId());
				finfos.addAll(sonFactorys);
			}
			List<MesProductline> productLines = this.getProductlineOfFactory(finfos);
			drivers = this.getDriverListForProductLineList(productLines);
		}
		return drivers;
	}
	
	/**
	 * 根据时间段获取产量表中的数据
	 * @param modelnum  产品型号
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return   返回在该时间段内的产量信息
	 */
	private List<MesProduction> getProductionList(String modelnum,Timestamp startDate,Timestamp endDate){
		if(modelnum != null && !modelnum.equals("") && !modelnum.equals("null")){
			return mesProDao.findByProductnumAndUpdatetimeBetween(modelnum, startDate, endDate);
		}else{
			return mesProDao.findByUpdatetimeBetween(startDate, endDate);
		}
	}
	
	/**
	 * 根据工厂id递归出该id下的所有子工厂
	 * @param factoryId  工厂id
	 * @return
	 */
	@Override
	public List<Companyinfo> getFactoryListByFactoryId(Long factoryId){
		List<Companyinfo> factorys = new ArrayList<>();
		Companyinfo factory = cpDao.findOne(factoryId);
		factorys.add(factory);
		List<Companyinfo> sonFactorys = cpDao.findByParentid(factoryId);
		if(sonFactorys != null && sonFactorys.size() > 0){
			for(Companyinfo fty : sonFactorys){
				factorys.add(fty);
				this.getSonFactoryByFactoryId(fty.getId(), factorys);
			}
		}
		return factorys;
	}
	
	/**
	 * 工厂递归主逻辑
	 * @param factoryId
	 * @param factorys
	 */
	private void getSonFactoryByFactoryId(Long factoryId,List<Companyinfo> factorys){
		List<Companyinfo> sonFactorys = cpDao.findByParentid(factoryId);
		if(sonFactorys != null && sonFactorys.size() > 0){
			for(Companyinfo fty : sonFactorys){
				factorys.add(fty);
				this.getSonFactoryByFactoryId(fty.getId(),factorys);
			}
		}
	}
	
	
	/**
	 * 循环设备时间段,统计该设备在不同时间段的能耗; 目的是封装series中的数据;
	 * @param times 时间段集合
	 * @param mesEnergys  能耗集合
	 * @param energyType 能耗类型
	 * @return
	 */
	private List<SeriesItem> getEnergySeriesForBarChart(List<Timestamp> times,
			List<MesEnergy> mesEnergys,List<String> legend_data,EnergyType energyType){
		List<SeriesItem> series = new ArrayList<>();
		List<Double> series_data = new ArrayList<Double>();
		BarChart.SeriesItem seriesItem = new BarChart().new SeriesItem();
		seriesItem.setName(legend_data.get(0)); 
		//时间循环
		for(int j=0;times!=null && j<times.size()-1;j++){
			//一个时间段的统计总产量
			Double totalEnergy = 0d;
			for(MesEnergy me : mesEnergys){
				if(!energyType.toString().equalsIgnoreCase(me.getEnergytype())){
					continue;
				}
				//在该时间段内
				if(me.getUpdatetime().getTime() >= times.get(j).getTime() 
						&& me.getUpdatetime().getTime() <= times.get(j+1).getTime()){
					totalEnergy += me.getValue();
				}
			}
			//一个时间段循环完成,需要把该时间段的统计数据添加到series中的data列表中
			series_data.add(totalEnergy);
		}
		seriesItem.setData(series_data);
		//把一类产品的数据元素放入到series集合中
		series.add(seriesItem);
		return series;
	}
	
	/**
	 * 循环产品号种类,统计该产品在不同时间段的产量; 目的是封装series中的数据;
	 * @param producntNumList 产品类型号集合
	 * @param times 时间段集合
	 * @param legend_data  产品类名名称集合
	 * @param productionList 
	 * @return
	 */
	private List<SeriesItem> getSeriesForBarChart(List<String> producntNumList,List<Timestamp> times
			,List<String> legend_data,List<MesProduction> productionList){
		/*
		 * 根据items的时间段来统计产量集合productionList不同时间段的产量有多少;
		 */
		List<SeriesItem> series = new ArrayList<>();
		List<Double> series_data = null;
		BarChart.SeriesItem seriesItem = null;
		for(int i=0;producntNumList!=null && i<producntNumList.size();i++){
			String productNum = producntNumList.get(i);
			series_data = new ArrayList<>();
			//series集合中的一个实体元素
			seriesItem = new BarChart().new SeriesItem();
			//产品名称
			seriesItem.setName(legend_data.get(i)); 
			//时间循环
			for(int j=0;times!=null && j<times.size()-1;j++){
				//一个时间段的统计总产量
				Double productNumCount = 0d;
				for(MesProduction production : productionList){
					//是同一类产品
					if(productNum.equals(production.getProductnum())){
						//在该时间段内
                        productNumCount += production.getTotalCount();
//						if(production.getUpdatetime().getTime() >= times.get(j).getTime() 
//								&& production.getUpdatetime().getTime() <= times.get(j+1).getTime())
//							productNumCount += production.getTotalCount();
					}
				}
				//一个时间段循环完成,需要把该时间段的统计数据添加到series中的data列表中
				series_data.add(productNumCount);
			}
			seriesItem.setData(series_data);
			//把一类产品的数据元素放入到series集合中
			series.add(seriesItem);
		}
		return series;
	}
	
	/**
	 * 根据productNum(即与表mes_product中字段modelnum对应)来查询出
	 * 产品名称,参数需要加入公司限制;
	 * @param productNums
	 * @return
	 */
	private List<String> getProductName(List<String> productNums){
		if(productNums == null || productNums.size() <= 0)
			return null;
		List<String> productNames = new ArrayList<>();
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		for(String productNum : productNums){
			MesProduct product =  productDao.findByModelnumAndCompanyinfoId(productNum, companyId);
			if(product != null)
				productNames.add(product.getName());
		}
		return productNames;
	}
	
	/**
	 * 统计出有多少种产品号
	 * @param productions 被统计对象
	 * @return
	 */
	private List<String> getProductNum(List<MesProduction> productions){
		List<String> distinctNameList = new ArrayList<>();
		if(productions == null || productions.size() <= 0)
			return distinctNameList;
		//先把第一条添加进去
		distinctNameList.add(productions.get(0).getProductnum());
		Boolean isMatch;
		for(MesProduction production : productions){
			isMatch =false;
			for(String productnum : distinctNameList){
				if(productnum.equals(production.getProductnum())){
					isMatch = false;
					break;
				}else{
					isMatch = true;
				}
			}
			if(isMatch)
				distinctNameList.add(production.getProductnum());
		}
		return distinctNameList;
	}
	
	/**
	 * 把时间段拆分,返回字符串集合
	 * @param dateType 按什么类型来区分时间段
	 * @param startDate 开始时间
	 * @param endDate  结束时间
	 * @return 时间分段字符串集合
	 */
	public List<String> getDateListStr(DateType dateType,Timestamp startDate, Timestamp endDate){
		List<String> datesStr = null;
		if(DateType.day.equals(dateType)){
			//当天,分24小时段
			datesStr = this.get24HoursStr();
		}else if(DateType.week.equals(dateType)){
			//一星期,当天向前推7天,分7段时间
			datesStr = this.get7DayStr();
		}else if(DateType.month.equals(dateType)){
			//当月,分当月的天数段
			datesStr = this.getMonthDayStr();
		}else if(DateType.defineDate.equals(dateType)){
			//自定义时间:
			datesStr = this.getDefineTimeStr(startDate, endDate);
		}
		return datesStr;
	}
	
	/**
	 * 把时间段拆分
	 * @param dateType 按什么类型来区分时间段
	 * @param startDate 开始时间
	 * @param endDate  结束时间
	 * @return 时间分段集合
	 */
	public List<Timestamp> getDateList(DateType dateType,Timestamp startDate, Timestamp endDate) {
		List<Timestamp> times = null;
		if(DateType.day.equals(dateType)){
			//当天,分24小时段
			times = this.getDay24Hours();
		}else if(DateType.week.equals(dateType)){
			//一星期,当天向前推7天,分7段时间
			times = this.get7Day();
		}else if(DateType.month.equals(dateType)){
			//当月,分当月的天数段
			times = this.getMonthDay();
		}else if(DateType.defineDate.equals(dateType)){
			//自定义时间:
			times = this.getDefineTime(startDate, endDate);
		}
		//填充timestamp中最后一个数据
		if((endDate.getTime() - startDate.getTime()) > 2592000000L){ //如果时间间隔大于一个月
			Timestamp timestamp = times.get(times.size()-1);
			Calendar startCd = Calendar.getInstance();
			startCd.setTime(new Date(timestamp.getTime()));
			//设置当月最后一天
			String last = timestamp.toString().split("-")[0]+"-"+timestamp.toString().split("-")[1]+"-"+
					startCd.getActualMaximum(Calendar.DAY_OF_MONTH)+" 23:59:59";
			Timestamp lastValue = Timestamp.valueOf(last);
			times.add(lastValue);
		}else{
			Timestamp timestamp = times.get(times.size()-1);
			String last = timestamp.toString().split(" ")[0]+" 23:59:59";
			Timestamp lastValue = Timestamp.valueOf(last);
			times.add(lastValue);
		}
		return times;
	}
	
	/**
	 * 拆分自定义时间段
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Timestamp> getDefineTime(Timestamp start,Timestamp end){
		/*
		 * 	1.当日期在一天以内的,以小时类分时间段; 
		 * 		当跨日时,需要注意日期;
		 * 	2.当时间段差超过一天,且小于30天时,以开始时间与结束时间差的天数为分割时间段;
		 *  3.当时间段差超过30天时,以月份段来划分,以跨度的月份来划分;
		 *  	例如: 当跨度了3个月时,就以跨度的月份段来分;
		 */
		if(start == null || end == null)
			return null;
		if((end.getTime() - start.getTime()) <= 86400000L){
			//小于一天
			return this.getDefineTimeForHours(start, end);
		}else if((end.getTime() - start.getTime()) > 86400000L
				&& (end.getTime() - start.getTime()) <= 2592000000L){
			//大于一天,小于等于31天
			return this.getDefineTimeForDay(start, end);
		}else if((end.getTime() - start.getTime()) > 2592000000L){
			//大于31天
			return this.getDefineTimeForMonth(start, end);
		}
		//都不符合直接返回null
		return null;
	}
	
	/**
	 * 拆分自定义时间段,返回字符串集合
	 * @return
	 */
	private List<String> getDefineTimeStr(Timestamp start,Timestamp end){
		if((end.getTime() - start.getTime()) <= 86400000L){
			//小于一天
			return this.getDefineTimeForHoursStr(start, end);
		}else if((end.getTime() - start.getTime()) > 86400000L
				&& (end.getTime() - start.getTime()) <= 2592000000L){
			//大于一天,小于等于31天
			return this.getDefineTimeForDayStr(start, end);
		}else if((end.getTime() - start.getTime()) > 2592000000L){
			//大于31天
			return this.getDefineTimeForMonthStr(start, end);
		}
		//不符合返回null
		return null;
	}
	
	/**
	 * 拆分月份段集合
	 * @param start
	 * @param end
	 * @return
	 */
	private List<Timestamp> getDefineTimeForMonth(Timestamp start,Timestamp end){
		List<Timestamp> months = new ArrayList<>();
		Calendar startCd = Calendar.getInstance();
		startCd.setTime(new Date(start.getTime()));
		//日期设置为1号
		startCd.set(Calendar.DAY_OF_MONTH, 1);
		//小时设置成00
		startCd.set(Calendar.HOUR_OF_DAY, 0);
		//分钟设置成00
		startCd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		startCd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		startCd.set(Calendar.MILLISECOND, 0);
		
		Calendar endCd = Calendar.getInstance();
		endCd.setTime(new Date(end.getTime()));
		
		while(startCd.getTime().getTime() <= endCd.getTime().getTime()){
			months.add(new Timestamp(startCd.getTime().getTime()));
			startCd.add(Calendar.MONTH, 1);
		}
		return months;
	}
	
	/**
	 * 以月为时间段区分,返回字符串集合
	 * @param start
	 * @param end
	 * @return
	 */
	private List<String> getDefineTimeForMonthStr(Timestamp start,Timestamp end){
		List<String> months = new ArrayList<>();
		List<Timestamp> times = this.getDefineTimeForMonth(start, end);
		Calendar cd = Calendar.getInstance();
		for(Timestamp time : times){
			cd.setTime(new Date(time.getTime()));
			months.add((cd.get(Calendar.MONTH)+ 1) + "月");
		}
		return months;
	}
	
	/**
	 * 拆分时间段为天数间隔
	 * @param start
	 * @param end
	 * @return
	 */
	private List<Timestamp> getDefineTimeForDay(Timestamp start,Timestamp end){
		List<Timestamp> days = new ArrayList<>();
		Calendar startCd = Calendar.getInstance();
		startCd.setTime(new Date(start.getTime()));
		//分钟设置成00
		startCd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		startCd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		startCd.set(Calendar.MILLISECOND, 0);
		
		Calendar endCd = Calendar.getInstance();
		endCd.setTime(new Date(end.getTime()));
		//分钟设置成00
		endCd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		endCd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		endCd.set(Calendar.MILLISECOND, 0);
		while(startCd.getTime().getTime() <= endCd.getTime().getTime()){
			days.add(new Timestamp(startCd.getTime().getTime()));
			startCd.add(Calendar.DAY_OF_MONTH, 1);
		}
		return days;
	}
	
	/**
	 * 以天为标准拆分,返回字符串集合
	 * @param start
	 * @param end
	 * @return
	 */
	private List<String> getDefineTimeForDayStr(Timestamp start,Timestamp end){
		List<String> daysStr = new ArrayList<>();
		List<Timestamp> times = this.getDefineTimeForDay(start, end);
		Calendar cd = Calendar.getInstance();
		for(Timestamp time : times){
			cd.setTime(new Date(time.getTime()));
			daysStr.add(cd.get(Calendar.MONTH) + 1 + "月" + cd.get(Calendar.DAY_OF_MONTH) + "日");
		}
		return daysStr;
	}
	
	/**
	 * 获取自定义时间段小时段字符串,例如 13日1时
	 * @param start
	 * @param end
	 * @return
	 */
	private List<String> getDefineTimeForHoursStr(Timestamp start,Timestamp end){
		List<String> hoursStr = new ArrayList<>();
		List<Timestamp> times = this.getDefineTimeForHours(start, end);
		Calendar cd = Calendar.getInstance();
		for(Timestamp time : times){
			cd.setTime(new Date(time.getTime()));
			hoursStr.add(cd.get(Calendar.DAY_OF_MONTH) + "日" + cd.get(Calendar.HOUR_OF_DAY) + "时");
		}
		return hoursStr;
	}
	
	/**
	 * 根据开始,结束时间拆分为以小时的时间段
	 * @param start
	 * @param end
	 * @return
	 */
	private List<Timestamp> getDefineTimeForHours(Timestamp start,Timestamp end){
		List<Timestamp> hours = new ArrayList<>();
		Calendar startCd = Calendar.getInstance();
		startCd.setTime(new Date(start.getTime()));
		//分钟设置成00
		startCd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		startCd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		startCd.set(Calendar.MILLISECOND, 0);
		
		Calendar endCd = Calendar.getInstance();
		endCd.setTime(new Date(end.getTime()));
		//分钟设置成00
		startCd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		startCd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		startCd.set(Calendar.MILLISECOND, 0);
		
		while(startCd.getTime().getTime() <= endCd.getTime().getTime()){
			System.out.println(startCd.getTime() + "-" + endCd.getTime());
			hours.add(new Timestamp(startCd.getTime().getTime()));
			startCd.add(Calendar.HOUR_OF_DAY, 1);
		}
		return hours;
	}
	
	/**
	 * 获取当月天数时间段
	 * @return
	 */
	private List<Timestamp> getMonthDay(){
		List<Timestamp> days = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		//小时设置成00
		cd.set(Calendar.HOUR_OF_DAY, 0);
		//分钟设置成00
		cd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		cd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		cd.set(Calendar.MILLISECOND, 0);
		for(int i=1;i<=cd.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
			/*if(i==30){  //把月末的结束时间置为23:59:59
				cd.set(Calendar.HOUR_OF_DAY, 23);
				cd.set(Calendar.MINUTE, 59);
				cd.set(Calendar.SECOND, 59);
			}*/
			cd.set(Calendar.DAY_OF_MONTH, i);
			days.add(new Timestamp(cd.getTime().getTime()));
		}
		return days;
	}
	
	/**
	 * 获取当月天数时间日的字符串集合,例如: 12日,13日...
	 * @return
	 */
	private List<String> getMonthDayStr(){
		List<String> dayStr = new ArrayList<>();
		List<Timestamp> dayTimes = this.getMonthDay();
		Calendar cd = Calendar.getInstance();
		for(Timestamp time : dayTimes){
			cd.setTime(new Date(time.getTime()));
			dayStr.add(cd.get(Calendar.DAY_OF_MONTH) + "日");
		}
		return dayStr;
	}
	
	/**
	 * 获取当天向前推6天,包含当天的7天时间段的集合
	 * @return
	 */
	private List<Timestamp> get7Day(){
		List<Timestamp> times = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		//小时设置成00
		cd.set(Calendar.HOUR_OF_DAY, 0);
		//分钟设置成00
		cd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		cd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		cd.set(Calendar.MILLISECOND, 0);
		/*Calendar cd2 = Calendar.getInstance();//把今天的结束时间置为23:59:59
		cd2.set(Calendar.HOUR_OF_DAY, 23);
		cd2.set(Calendar.MINUTE, 59);
		cd2.set(Calendar.SECOND, 59);
		cd2.set(Calendar.MILLISECOND, 0);*/
		times.add(new Timestamp(cd.getTime().getTime()));
		for(int i=0;i>-6;i--){
			cd.add(Calendar.DAY_OF_MONTH, -1);
			times.add(new Timestamp(cd.getTime().getTime()));
		}
		Collections.reverse(times);
		return times;
	}
	public static void main(String[] args) {
		ProductAndEnergyAndDriverChartServiceImp ps = new ProductAndEnergyAndDriverChartServiceImp();
		System.out.println(ps.get7Day());
	}
	/**
	 * 获取7天
	 * @return
	 */
	private List<String> get7DayStr(){
		List<String> sivenDay = new ArrayList<>();
		List<Timestamp> times = this.get7Day();
		Calendar cd = Calendar.getInstance();
		for(Timestamp time : times){
			cd.setTime(new Date(time.getTime()));
			sivenDay.add(cd.get(Calendar.MONTH) + 1 + "月"+cd.get(Calendar.DAY_OF_MONTH) + "日");
		}
		return sivenDay;
	}
	
	/**
	 * 获取当天的24小时段集合;
	 * 例如: 2016-12-13 00:00:00 ,2016-12-13 01:00:00
	 * @return
	 */
	private List<Timestamp> getDay24Hours(){
		List<Timestamp> items = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		//分钟设置成00
		cd.set(Calendar.MINUTE, 0);
		//秒钟设置成00
		cd.set(Calendar.SECOND, 0);
		//毫秒设置成0
		cd.set(Calendar.MILLISECOND, 0);
		for(int i=0;i<24;i++){
			cd.set(Calendar.HOUR_OF_DAY,i);
			items.add(new Timestamp(cd.getTime().getTime()));
		}
		return items;
	}
	
	/**
	 * 获得字符串时间整数,例如 2016-12-13 02:00:00 --> 字符串2
	 * @param times
	 * @return
	 */
	private List<String> get24HoursStr(){
		List<String> timeStrs = new ArrayList<>();
		List<Timestamp> times = this.getDay24Hours();
		for(Timestamp time : times){
			timeStrs.add(time.getHours() + "时");
		}
		return timeStrs;
	}
	
	/**
	 * 产量表中的driverId中的productline的id与产线的id对比,
	 * 如果相等则累加
	 * @param lines
	 * @param proList
	 * @return
	 */
	private Map<PassType,Object> getCountMap(List<MesProductline> lines,List<MesProduction> proList){
		Map<PassType,Object> resultMap = new HashMap<>();
		List<MesProduction> matchProList = new ArrayList<>();
		//合格数
		Long passCount = 0L;
		//不合格数
		Long failCount = 0L;
		for(MesProduction production : proList){
			for(MesProductline line : lines){
				//需要判断MesDriver与MesProductline是否为空,因为这个是大数据写入的数据,可能存在错误情况
				if(production.getMesDriver() != null && production.getMesDriver().getMesProductline() != null)
					if(production.getMesDriver().getMesProductline().getId().compareTo(line.getId()) == 0){
						passCount += production.getPassCount();
						failCount += production.getFailureCount();
						matchProList.add(production);
						break;
					}
			}
		}
		resultMap.put(PassType.PASSCOUNT, passCount);
		resultMap.put(PassType.FAILCOUNT, failCount);
		resultMap.put(PassType.matchProductList, matchProList);
		return resultMap;
	}
	
	/**
	 * 获取一个工厂下的产线列表
	 * @param factory
	 * @return
	 */
	@Override
	public List<MesProductline> getProductlineOfFactory(Companyinfo factory){
		List<Companyinfo> factorys = null; 
		if(factory != null){
			factorys = new ArrayList<>();
			factorys.add(factory);
		}
		return this.getProductlineOfFactory(factorys);
	}
	
	/**
	 * 获取工厂列表下的产下列表
	 * @return
	 */
	@Override
	public List<MesProductline> getProductlineOfFactory(List<Companyinfo> factorys){
		List<MesProductline> lines = null;
		if(factorys != null && factorys.size() > 0){
			lines = new ArrayList<>();
			for(Companyinfo factory : factorys){
				lines.addAll(factory.getMesProductlines());
			}
		}
		return lines;
	}
	
	
	@Override
	public List<Companyinfo> getFactoryList(Long parentId){
		return cpDao.findByParentid(parentId);
	}
	
	/**
	 * 根据id获取工厂
	 * @return
	 */
	private List<Companyinfo> getFactoryById(Long id){
		return this.getInfoByIdAndType(id, TypeScope.factory);
	}
	/**
	 * 根据id获取公司
	 * @param id
	 * @return
	 */
	private List<Companyinfo> getCompanyinfoById(Long id){
		return this.getInfoByIdAndType(id, TypeScope.company);
	}
	
	/**
	 * 根据不同条件查询公司或者工厂
	 * @param id
	 * @param type
	 * @return
	 */
	private List<Companyinfo> getInfoByIdAndType(Long id,TypeScope type ){
		return cpDao.findByIdAndCompanytype(id, type.toString());
	}
	
	/**
	 * 返回产线列表下的所有设备集合
	 * @param lines
	 * @return
	 */
	@Override
	public List<MesDriver> getDriverListForProductLineList(List<MesProductline> lines){
		List<MesDriver> drivers = new ArrayList<>();
		for(MesProductline line : lines){
			//if(line.getMesdriver() != null)
			drivers.addAll(driverDao.findByMesProductlineid(line.getId()));
		}
		return drivers;
	}
	
	
	/**
	 * 根据开始时间 和 结束时间获取设备在driverRecord表中的rowkey集合
	 * @param startTime  开始时间
	 * @param endTime  结束时间
	 * @param chooseDriver2   设备id
	 * @return
	 * @throws ParseException
	 */
	public List<DriverRecord> getDriverRecordRowkeys(Timestamp startTime, Timestamp endTime, Long chooseDriver2) throws ParseException {
		Date begin = startTime;
		Date end = endTime;
		AnalyzeSearch analyzeSearch = new AnalyzeSearch();
    	analyzeSearch.setMesDriverId(chooseDriver2);
    	analyzeSearch.setBegin(begin);
    	analyzeSearch.setEnd(end);
    	HBasePageModel page = new HBasePageModel();
    	page.setAnalyzeSearch(analyzeSearch);
    	page.setPagable(false);
    	List<DriverRecord> driverRecords = driverRecordSchedulerPageSearch.getDriverRecordPagable(page);
    	if(driverRecords==null || driverRecords.size()<1){
    		return null;
    	}
		return driverRecords;
	}


	@Override
	public List<MesDriver> getDriverListByCompanyId(Long id) {
		List<MesDriver> driverList = new ArrayList<>();
		List<Companyinfo> factorys = cpDao.findByParentid(id);
		List<MesProductline> lines = this.getProductlineOfFactory(factorys);
		driverList = this.getDriverListForProductLineList(lines);
		return driverList;
	}

	/**
	 * 从Hbase的结果集中获取数据
	 * @param rsList
	 * @return
	 */
	public List<MesProduction> getMesProductionList(List<Result> rsList) {
		List<MesProduction> outList = Lists.newArrayList();
		Map<String, String> rsMap = null;
		MesProduction product = null;
		for (Result rs : rsList) {
			rsMap = getRowByResultForOutPut(rs);
			product = new MesProduction();
			product.setProductnum(rsMap.get("productSn"));
		}
		return outList;
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
                // 工厂
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
	
}
