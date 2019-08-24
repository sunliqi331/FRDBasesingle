package com.its.statistics.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CgAnalyzeSearch extends AnalyzeSearch{
	private String analysisMethod;
	
	private int testCount;
	
	private int statisticalStandard;
	
	private int measureRange;
	
	private double maxValue;
	
	private double minValue;
	
	private double standardValue;
	
	private double actualValue;

	
	public String getAnalysisMethod() {
		return analysisMethod;
	}

	public void setAnalysisMethod(String analysisMethod) {
		this.analysisMethod = analysisMethod;
	}


	public int getTestCount() {
		return testCount;
	}

	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}

	public int getStatisticalStandard() {
		return statisticalStandard;
	}

	public void setStatisticalStandard(int statisticalStandard) {
		this.statisticalStandard = statisticalStandard;
	}
	/**
	 * 0:时间段
	 * 1:编号
	 * @return
	 */
	public int getMeasureRange() {
		return measureRange;
	}

	public void setMeasureRange(int measureRange) {
		this.measureRange = measureRange;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getStandardValue() {
		return standardValue;
	}

	public void setStandardValue(double standardValue) {
		this.standardValue = standardValue;
	}

	public double getActualValue() {
		return actualValue;
	}

	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}
	
}
