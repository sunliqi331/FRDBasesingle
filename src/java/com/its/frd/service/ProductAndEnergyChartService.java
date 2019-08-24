package com.its.frd.service;

import java.sql.Timestamp;
import java.util.Map;

import com.its.frd.service.ProductAndEnergyChartService.DateType;
import com.its.frd.service.ProductAndEnergyChartService.EnergyType;
import com.its.frd.service.ProductAndEnergyChartService.TypeScope;

public interface ProductAndEnergyChartService {
	
	public enum TypeScope {
		company,factory,productline
	}
	
	public enum PassType{
		COUNT,PASSCOUNT,FAILCOUNT,matchProductList
	}
	
	public enum BarKeyType{
		count,passCount,dateList,legendData,
		seriesItemList,xAxisOfData
	}
	
	public enum DateType{
		month,day,week,defineDate
	}
	
	public enum EnergyType{
		electric,water,gas,all
	}
	/**
	 * 合格率饼图
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param typeScope 统计范围,eg:公司,工厂,产线
	 * @param modelnum 产品型号,对应product表中的modelnum
	 * @param passType 统计产品总数,或者合格总数
	 * @param id 公司id,或工厂id,或产线id
	 * @return 返回产品生产总数或者合格数
	 */
	public Map<PassType,Object> getProductCountNum(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,String modelnum,PassType passType,Long id);
	
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
	 * X轴以设备为柱状
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
	 * X轴以电能,水能,气能
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param typeScope 统计范围,eg:公司,工厂,产线
	 * @param modelnum 产品型号,对应product表中的modelnum
	 * @param id       公司id,或工厂id,或产线id
	 * @param dateType 用作拆分时间用
	 * @param energyType 用作统计哪种类型的能耗
	 * @return 返回的集合中包含xAxis中的data,series中的数据集合,legend中的data
	 */
	public Map<BarKeyType,Object> getEnergyDataForTotal(Timestamp startDate,Timestamp endDate,TypeScope typeScope
			,EnergyType energyType,Long id,DateType dateType);
	
}
