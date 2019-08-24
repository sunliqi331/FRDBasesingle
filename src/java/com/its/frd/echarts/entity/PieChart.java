package com.its.frd.echarts.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 饼图
 * 根据实际业务初始化相关参数.
 */
public class PieChart extends BaseChart{
	
	private Map<ItemStyleEnum,Object> seriesItemStyle = new HashMap<>();
	private Map<String,Object> emphasis = new HashMap<>();
	
	public PieChart(){
		//默认设置色彩
		emphasis.put("shadowBlur", 10);
		emphasis.put("shadowOffsetX", 0);
		emphasis.put("shadowColor", "rgba(0, 0, 0, 0.5)");
		seriesItemStyle.put(ItemStyleEnum.emphasis, emphasis);
		
		series.put(SeriesEnum.itemStyle, seriesItemStyle);
		
		legend.put(LegendEnum.data, legenData);
		legend.put(LegendEnum.orient, "vertical");
		legend.put(LegendEnum.left, "left");
		
		title.put(TitleEnum.TEXT, "合格率统计");
		tooltip.put(TooltipEnum.trigger, "item");
		tooltip.put(TooltipEnum.formatter, "{a} <br/>{b} : {c} ({d}%)");
		
		series.put(SeriesEnum.name, "占比例");
		series.put(SeriesEnum.type, "pie");
		series.put(SeriesEnum.radius, "55%");
		
		legenData.add("合格");
		legenData.add("不合格");
		
		option.put(OptionEnum.SERIES, series);
	}
	
	public Map<SeriesEnum, Object> getSeries() {
		return series;
	}
	public void setSeries(Map<SeriesEnum, Object> series) {
		this.series = series;
	}
	public List<Map<seriesDataEnum, Object>> getSeriesData() {
		return seriesData;
	}
	public void setSeriesData(List<Map<seriesDataEnum, Object>> seriesData) {
		this.seriesData = seriesData;
	}

}
