package com.its.frd.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverProperty;
import com.its.frd.entity.MesProductline;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;

public interface ProductAndEnergyAndDriverChartService {
	
	public enum TypeScope {
		company,factory,productline
	}
	
	public enum PassType{
		COUNT,PASSCOUNT,FAILCOUNT,matchProductList
	}
	
	public enum BarKeyType{
		count,passCount,dateList,legendData,
		seriesItemList,xAxisOfData, seriesPercentList,
		okPercent, ngPercent,okPercentByEveryTime
		,okDisplay
		,totalDisplay
		,batchIdXvalues
	    ,batchIdYvalues
        ,seriesDataComMaxForBatch
        ,batchXvalueLis
        ,okPercentMaxList
        ,percentMaxList
	}
	
	public enum LineElement{
		xAxisData,seriesData,titleText,unit
	}
	
	public enum DateType{
		month,day,week,defineDate
	}
	
	public enum EnergyType{
		electric,water,gas,all
	}
	
	/**
	 * 获取设备的实时状态
	 * @param driverId 设备主键ID
	 * @return
	 */
	public String getDriverStatus(Long driverId);
	
	/**
	 * 设备的监测完成的次数的直方图数据
	 * @param startDate
	 * @param endDate
	 * @param driveryId 设备id
	 * @param productLineId 产线id
	 * @return
	 */
	//public Map<BarKeyType,Object> getDriverStatsBarChart(Timestamp startDate,Timestamp endDate,Long productLineId,Long driverId,DateType dateType);
	
	/**
	 * 设备属性折线图
	 * @param startDate 开始时间
	 * @param endDate	结束时间
	 * @param driverPropertyId 设备属性id
	 * @return
	 */
	//public Map<LineElement,Object> getDriverPropertyLineChart(Timestamp startDate,Timestamp endDate,Long driverPropertyId);
	
	/**
	 * 合格率饼图
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param typeScope 统计范围,eg:公司,工厂,产线
	 * @param modelnum 产品型号,对应product表中的modelnum
	 * @param passType 统计产品总数,或者合格总数
	 * @param id 公司id,或工厂id,或产线id
	 * @param dateType 时间范围类型
	 * @return 返回产品生产总数或者合格数
	 */
	public Map<PassType,Object> getProductCountNum(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,String modelnum,PassType passType,Long id,DateType dateType);
	
	/**
	 * 直方图产量
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param typeScope 统计范围,eg:公司,工厂,产线
	 * @param modelnum 产品型号,对应product表中的modelnum
	 * @param id id 公司id,或工厂id,或产线id
	 * @param dateType 用作拆分时间用
	 * @return 返回的集合中包含xAxis中的data,series中的数据集合,legend中的data
	 */
	public Map<BarKeyType,Object> getProductionData(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,String modelnum,Long id,DateType dateType);
	
	/**
	 * 直方图能耗
	 * X轴以设备为柱状,统计所有设备
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param typeScope 统计范围,eg:公司,工厂,产线
	 * @param modelnum 产品型号,对应product表中的modelnum
	 * @param id       公司id,或工厂id,或产线id
	 * @param dateType 用作拆分时间用
	 * @param energyType 用作统计哪种类型的能耗
	 * @return 返回的集合中包含xAxis中的data,series中的数据集合,legend中的data
	 */
	public Map<BarKeyType,Object> getEnergyDataForDriver(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,EnergyType energyType,Long id,DateType dateType);
	

	/**
	 * 直方图能耗
	 * X轴以电能,水能,气能,只统计单个设备,按时间段分类显示,
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param driverId  设备id
	 * @param dateType 用作拆分时间用
	 * @param energyType 用作统计哪种类型的能耗
	 * @return 返回的集合中包含xAxis中的data,series中的数据集合,legend中的data
	 */
	public Map<BarKeyType,Object> getEnergyDataForTotal(Timestamp startDate,Timestamp endDate
			,EnergyType energyType,Long driverId,DateType dateType);
	
	/**
	 * 直方图设备运行时间
	 * X轴以设备为柱状,统计所有设备
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param typeScope 统计范围,eg:公司,工厂,产线
	 * @param id       公司id,或工厂id,或产线id
	 * @param dateType 用作拆分时间用
	 * @param ifIsDriver 选择范围是否是设备
	 * @return 返回的集合中包含xAxis中的data,series中的数据集合,legend中的data
	 */
	public Map<BarKeyType,Object> getRuntimeDataForTotal(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,Long id,DateType dateType,String ifIsDriver) throws ParseException;

	/**
	 * 根据工厂id递归出该id下的所有子工厂
	 * @param factoryId  工厂id
	 * @return
	 */
	public List<Companyinfo> getFactoryListByFactoryId(Long factoryId);
	
	/**
	 * 获取公司下工厂列表
	 * @return
	 */
	public List<Companyinfo> getFactoryList(Long parentId);
	
	/**
	 * 返回产线列表下的所有设备集合
	 * @param lines
	 * @return
	 */
	public List<MesDriver> getDriverListForProductLineList(List<MesProductline> lines);
	
	/**
	 * 获取一个工厂下的产线列表
	 * @param factory
	 * @return
	 */
	public List<MesProductline> getProductlineOfFactory(Companyinfo factory);
	
	/**
	 * 获取工厂列表下的产下列表
	 * @return
	 */
	public List<MesProductline> getProductlineOfFactory(List<Companyinfo> factorys);
	
	/**
	 * 根据公司id获得公司下边的所有设备
	 * @param id
	 * @return
	 */
	public List<MesDriver> getDriverListByCompanyId(Long id);

	public Map<PassType, Object> getProductCountNumByHase(Timestamp startDate, Timestamp endDate, TypeScope typeScope,
			String modelnum, PassType passType, Long id, DateType dateType);
	
}
