package com.its.monitor.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChartsBaseMonitor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7597744173207063996L;

	private String name;
	
	private String ids;
	
	private Map<String,String> map = new HashMap<>();
	
	private String scope;
	
	private String category;
	
	private String chartsType;
	
	private Date begin;
	
	private Date end;
	
	private String timeSequence;
	
	private String pieType;
	
	private String driverId;
	/**
	 * 耗电：ElectricConsumption
	 */
	public static final String CATEGORY_ELECTRIC = "ElectricConsumption";
	/**
	 * 耗电-文本框
	 */
	public static final String CATEGORY_ELECTRIC_TEXT = "耗电";
	public static final String CATEGORY_WATER = "WaterConsumption";
	/**
	 * 耗水-文本框
	 */
	public static final String CATEGORY_WATER_TEXT = "耗水";
	public static final String CATEGORY_GAS = "GasConsumption";
	/**
	 * 耗气-文本框
	 */
	public static final String CATEGORY_GAS_TEXT = "耗气";
	public static final String CATEGORY_ELECTRIC_WATER = "ElectricWaterConsumption";
	public static final String CATEGORY_PRODUCT_STATUS_PASS_FAILURE = "PassFailureRate";
	/**
	 * 统计饼图：设备状态对比
	 */
	public static final String CATEGORY_DRIVER_STATUS_ANALYSIS = "DriverStatusAnalysis";
	/**
	 * 合格-文本框
	 */
	public static final String CATEGORY_PRODUCT_STATUS_PASS_TEXT = "合格";
	/**
	 * 不合格-文本框
	 */
	public static final String CATEGORY_PRODUCT_STATUS_FAIL_TEXT = "不合格";
	/**
	 * 产量计数
	 */
	public static final String CATEGORY_PRODUCT_COUNT = "Capacity";
	/**
	 * 产量：文本框
	 */
	public static final String CATEGORY_PRODUCT_COUNT_TEXT = "产量";

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChartsType() {
		return chartsType;
	}

	public void setChartsType(String chartsType) {
		this.chartsType = chartsType;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getTimeSequence() {
		return timeSequence;
	}

	public void setTimeSequence(String timeSequence) {
		this.timeSequence = timeSequence;
	}

    public String getPieType() {
        return pieType;
    }

    public void setPieType(String pieType) {
        this.pieType = pieType;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

}
