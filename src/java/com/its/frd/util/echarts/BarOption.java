package com.its.frd.util.echarts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;

public class BarOption extends EchartsOption<BarOption> {
	private CategoryAxis category = new CategoryAxis(); 
	private Map<String, Bar> barMap = new HashMap<>();
	@Override
	public GsonOption data(Object o_map) throws Exception{
		if(o_map instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Long>> map = (Map<String, Map<String, Long>>)o_map;
			option.tooltip(Trigger.axis);
			option.yAxis(new ValueAxis());
			for(Entry<String, Map<String, Long>> entry : map.entrySet()){
				String xAxisData = entry.getKey();
				category.data(xAxisData);
				for(Entry<String, Long> _entry : entry.getValue().entrySet()){
					getBar(_entry.getKey()).data(_entry.getValue());
				}
				map.remove(xAxisData);
			}
			for(String barName : barMap.keySet()){
				legend.data(barName);
				option.series(barMap.get(barName));
			}
			option.legend(legend);
			option.xAxis(category);
			return option;
		}else{
			throw new Exception("格式错误：柱状图数据格式为Map<String, Map<String, Long>>");
		}
	}
	private Bar getBar(String barName){
		if(barMap.containsKey(barName))
			return barMap.get(barName);
		Bar bar = new Bar();
		barMap.put(barName, bar);
		return bar.name(barName);
	}
	public static void main(String[] args) throws Exception {
		Map<String , Map<String, Long>> map = new ConcurrentHashMap<>();
		
		Map<String, Long> _mMap1 = new ConcurrentHashMap<>();
		_mMap1.put("产量", 6500L);
		map.put("公司1", _mMap1);

		Map<String, Long> _mMap2 = new ConcurrentHashMap<>();
		_mMap2.put("产量", 6700L);
		map.put("公司2", _mMap2);
		
		Map<String, Long> _mMap3 = new ConcurrentHashMap<>();
		_mMap3.put("产量", 6500L);
		map.put("公司3", _mMap3);
		
		Map<String, Long> _mMap4 = new ConcurrentHashMap<>();
		_mMap4.put("产量", 6700L);
		map.put("公司4", _mMap4);
		
		
		BarOption countBarData = new BarOption();
		System.out.println(countBarData.title("hahaha").data(map));
	}
}
