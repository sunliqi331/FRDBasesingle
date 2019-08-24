package com.its.frd.echarts.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseChart {
	public enum OptionEnum{
		TITLE,TOOLTIP,LEGEND,SERIES,TOOLBOX
		,calculable,xAxis,yAxis
	}
	public enum TitleEnum{
		TEXT,SUBTEXT,X
	}
	public enum TooltipEnum{
		trigger,formatter
	}
	public enum LegendEnum{
		orient,left,data	
	}
	public enum SeriesEnum{
		name,type,radius,center,data,itemStyle
		,stack
	}
	public enum ItemStyleEnum{
		emphasis
	}
	public enum seriesDataEnum{
		value,name
	}
	public enum toolboxEnum{
		show,feature
	}
	public enum booleanEnum{
		FALSE,TRUE
	}
	public enum featureEnum{
		datavies,magictype,restore,saveasimage
	}
	public enum dataViewEnum{
		show,readOnly
	}
	public enum magicTypeEnum{
		show,type
	}
	public enum restoreEnum{
		show
	}
	public enum xAxis_yAxisEnum{
		type,data
	}
	
	public Map<OptionEnum,Object> option = new HashMap<>();
	public Map<TitleEnum,Object> title = new HashMap<>();
	public Map<TooltipEnum,Object> tooltip = new HashMap<>();
	public Map<LegendEnum,Object> legend = new HashMap<>();
	public List<String> legenData = new ArrayList<>();
	public Map<SeriesEnum,Object> series = new HashMap<>();
	public List<Map<seriesDataEnum,Object>> seriesData = 
			new ArrayList<>();
	public Map<toolboxEnum,Object> toolbox = new HashMap<>();
	public Map<featureEnum,Object> toolbox_feature = new HashMap<>();
	
	public BaseChart(){
		option.put(OptionEnum.TITLE,title);
		option.put(OptionEnum.TOOLTIP,tooltip);
		option.put(OptionEnum.LEGEND,legend);
		
		option.put(OptionEnum.TOOLBOX, toolbox);
		
		series.put(SeriesEnum.data, seriesData);
		
		toolbox.put(toolboxEnum.feature, toolbox_feature);
		
		legend.put(LegendEnum.data, legenData);
	}
	
	public Map<OptionEnum, Object> getOption() {
		return option;
	}
	public void setOption(Map<OptionEnum, Object> option) {
		this.option = option;
	}
	public Map<TitleEnum, Object> getTitle() {
		return title;
	}
	public void setTitle(Map<TitleEnum, Object> title) {
		this.title = title;
	}
	public Map<TooltipEnum, Object> getTooltip() {
		return tooltip;
	}
	public void setTooltip(Map<TooltipEnum, Object> tooltip) {
		this.tooltip = tooltip;
	}
	public Map<LegendEnum, Object> getLegend() {
		return legend;
	}
	public void setLegend(Map<LegendEnum, Object> legend) {
		this.legend = legend;
	}
	public List<String> getLegenData() {
		return legenData;
	}
	public void setLegenData(List<String> legenData) {
		this.legenData = legenData;
	}
	
}
