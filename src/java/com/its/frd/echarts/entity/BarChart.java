package com.its.frd.echarts.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 柱状图
 */
public class BarChart extends BaseChart{
	public enum BarSeriesData{
		name,type,data
	}
	
	public static String BARTYPE = "bar";
	
	private Map<dataViewEnum,Object> dataView = new HashMap<>();
	private Map<magicTypeEnum,Object> magicType = new HashMap<>();
	private Map<restoreEnum,Object> restore = new HashMap<>();
	private Map<restoreEnum,Object> saveAsImage = new HashMap<>();
	private List<Map<xAxis_yAxisEnum,Object>> xAxis = 
			new ArrayList<>();
	private List<Map<xAxis_yAxisEnum,Object>> yAxis = 
			new ArrayList<>();
	private Map<xAxis_yAxisEnum,Object> xAxis_data = 
			new HashMap<>();
	private Map<xAxis_yAxisEnum,Object> yAxis_data =
			new HashMap<>();
	
	
	private List<SeriesItem> barSeries = new ArrayList<>();
	
	public BarChart(){
		
		toolbox_feature.put(featureEnum.datavies,dataView);
		
		toolbox_feature.put(featureEnum.magictype,magicType);
		restore.put(restoreEnum.show, false);
		toolbox_feature.put(featureEnum.restore,restore);
		
		saveAsImage.put(restoreEnum.show, true);//是否从折线图还原
		toolbox_feature.put(featureEnum.saveasimage, saveAsImage);
		
		toolbox.put(toolboxEnum.show, "true");
		toolbox.put(toolboxEnum.feature, toolbox_feature);
		
		yAxis_data.put(xAxis_yAxisEnum.type, "value");
		yAxis.add(yAxis_data);
		
		xAxis_data.put(xAxis_yAxisEnum.type, "category");
		xAxis.add(xAxis_data);
		
		option.put(OptionEnum.calculable, "ture");
		option.put(OptionEnum.xAxis, xAxis);
		option.put(OptionEnum.yAxis, yAxis);
		option.put(OptionEnum.SERIES, barSeries);
	}
	
	public class SeriesItem{
		private String name;
		private String type = "bar";
		private double barWidth = 60;
		private List<Double> data;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public List<Double> getData() {
			return data;
		}
		public void setData(List<Double> data) {
			this.data = data;
		}
		public double getBarWidth() {
			return barWidth;
		}
		public void setBarWidth(double barWidth) {
			this.barWidth = barWidth;
		}
		
	}
	
	public class Grid{
		private String left = "3%";
		private String right = "4%";
		private String bottom = "3%";
		private boolean containLabel = true;
		
		public String getLeft() {
			return left;
		}
		public void setLeft(String left) {
			this.left = left;
		}
		public String getRight() {
			return right;
		}
		public void setRight(String right) {
			this.right = right;
		}
		public String getBottom() {
			return bottom;
		}
		public void setBottom(String bottom) {
			this.bottom = bottom;
		}
		public boolean isContainLabel() {
			return containLabel;
		}
		public void setContainLabel(boolean containLabel) {
			this.containLabel = containLabel;
		}
		
	}
	
	public Map<dataViewEnum, Object> getDataView() {
		return dataView;
	}

	public void setDataView(Map<dataViewEnum, Object> dataView) {
		this.dataView = dataView;
	}

	public Map<magicTypeEnum, Object> getMagicType() {
		return magicType;
	}

	public void setMagicType(Map<magicTypeEnum, Object> magicType) {
		this.magicType = magicType;
	}

	public Map<restoreEnum, Object> getRestore() {
		return restore;
	}

	public void setRestore(Map<restoreEnum, Object> restore) {
		this.restore = restore;
	}

	public Map<restoreEnum, Object> getSaveAsImage() {
		return saveAsImage;
	}

	public void setSaveAsImage(Map<restoreEnum, Object> saveAsImage) {
		this.saveAsImage = saveAsImage;
	}

	public List<Map<xAxis_yAxisEnum, Object>> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<Map<xAxis_yAxisEnum, Object>> xAxis) {
		this.xAxis = xAxis;
	}

	public List<Map<xAxis_yAxisEnum, Object>> getyAxis() {
		return yAxis;
	}

	public void setyAxis(List<Map<xAxis_yAxisEnum, Object>> yAxis) {
		this.yAxis = yAxis;
	}

	public Map<xAxis_yAxisEnum, Object> getxAxis_data() {
		return xAxis_data;
	}

	public void setxAxis_data(Map<xAxis_yAxisEnum, Object> xAxis_data) {
		this.xAxis_data = xAxis_data;
	}

	public Map<xAxis_yAxisEnum, Object> getyAxis_data() {
		return yAxis_data;
	}

	public void setyAxis_data(Map<xAxis_yAxisEnum, Object> yAxis_data) {
		this.yAxis_data = yAxis_data;
	}

	public List<SeriesItem> getBarSeries() {
		return barSeries;
	}

	public void setBarSeries(List<SeriesItem> barSeries) {
		this.barSeries = barSeries;
	}
	
	
}
