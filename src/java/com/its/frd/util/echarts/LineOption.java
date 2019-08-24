package com.its.frd.util.echarts;

import com.github.abel533.echarts.Grid;
import com.github.abel533.echarts.VisualMap;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.VisualMapType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.MarkPoint;
import com.github.abel533.echarts.series.Series;
import com.its.frd.util.DoubleUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class LineOption extends EchartsOption<LineOption> {
	private CategoryAxis category = new CategoryAxis();
	private Map<String, Line> lineMap = new HashMap<>();

	@Override
	public GsonOption data(Object o_map) throws Exception {
		if(o_map instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Double>> map = (Map<String, Map<String, Double>>)o_map;
			Map<String, Double> makepointMap = new ConcurrentHashMap<>();
			Map<String, Double> markeLineMap = new ConcurrentHashMap<>();
			makepointMap = map.get("makepoint");
			markeLineMap = map.get("markLine");
			map.remove("makepoint");
			map.remove("markLine");
			List<Entry<String, Map<String, Double>>> infoIds =
					new ArrayList<Entry<String, Map<String, Double>>>(map.entrySet());
			Collections.sort(infoIds, new Comparator<Entry<String, Map<String, Double>>>() {
				public int compare(Entry<String, Map<String, Double>> o1, Entry<String, Map<String, Double>> o2) {
					//return (o2.getValue() - o1.getValue());
					String key1 = o1.getKey();
					String key2 = o2.getKey();
					try {
						return Double.parseDouble(key1) < Double.parseDouble(key2) ? -1 : 1;
					} catch (NumberFormatException e) {
						return o1.getKey().compareTo(o2.getKey());
					}
				}
			});
			List<Double> list = new ArrayList<Double>();
			for (Map<String, Double> map_child : map.values()) {
				for(String key : map_child.keySet()){
					Double value = map_child.get(key);
					list.add(value);
				}
			}
			Double max = null;
			Double min = null;
			if (list.size() == 1){
				max = list.get(0);
				min = list.get(0);
			} else if (list.size() >1){
				max = Collections.max(list);
				min = Collections.min(list);
			}
//			T max = Collections.max(map);
//			option.tooltip(Trigger.axis);
			option.tooltip(Trigger.item);
			option.grid(new Grid().left("8%").right("10%").bottom("5%").containLabel(true));
			ValueAxis valueAxis = new ValueAxis().boundaryGap("20%", "20%").scale(true);

			/* 根据上下限显示y坐标轴 */
			if (makepointMap != null){
				Double uppervalues = makepointMap.get("uppervalues");
				Double lowervalues = makepointMap.get("lowervalues");

				List<VisualMap> visualMap_ = new ArrayList<>();
				VisualMap visualMap = new VisualMap();
				visualMap.setShow(false);
//				List<Map<String, Object>> visualMap_list = new ArrayList<>();
				Object[] visualMap_list = new Object[3];
				Map<String, Object> visualMap_map1 = new HashMap<>();
				visualMap_map1.put("gt", uppervalues);
				visualMap_map1.put("color", "#c12e34");
				Map<String, Object> visualMap_map2 = new HashMap<>();
				visualMap_map2.put("lt", lowervalues);
				visualMap_map2.put("color", "#c12e34");
				Map<String, Object> visualMap_map3 = new HashMap<>();
				visualMap_map3.put("lte", uppervalues);
				visualMap_map3.put("gte", lowervalues);
				visualMap_map3.put("color", "#32CD32");
				visualMap_list[0] = visualMap_map1;
				visualMap_list[1] = visualMap_map2;
				visualMap_list[2] = visualMap_map3;
//				visualMap_list(visualMap_map1);
//				visualMap_list.add(visualMap_map2);
				visualMap.setPieces(visualMap_list);
				visualMap_.add(visualMap);
				option.setVisualMap(visualMap_);


				if (null != uppervalues){

					uppervalues = DoubleUtil.add(uppervalues, 0.05);
					if (max != null && uppervalues < max){
						uppervalues = max;
					}
					valueAxis.max(uppervalues);
				}
				if (null != lowervalues){
					lowervalues = DoubleUtil.sub(lowervalues, 0.05);
					if (min != null && lowervalues > min){
						lowervalues = min;
					}
					valueAxis.min(lowervalues);
				}
			}
			option.yAxis(valueAxis);
			category.boundaryGap(false);
			for(Entry<String, Map<String, Double>> entry : infoIds){
				String xAxisData = entry.getKey();
				category.data(xAxisData);
				for (Entry<String, Double> _entry : entry.getValue().entrySet()) {
//                    getLine(_entry.getKey(), makepointMap).data(_entry.getValue())
//                            .label(new ItemStyle().normal(new Normal().formatter("").show(true)));
					getLine(_entry.getKey(), makepointMap, markeLineMap).data(_entry.getValue()).label().normal().show(false);
//                    getLine(_entry.getKey(), makepointMap).data((double)(Math.round(_entry.getValue()*100)/100.0));
//					getLine(_entry.getKey(), makepointMap).data((double)(Math.round(Double.valueOf(_entry.getValue())*100)/100.0));
				}
				map.remove(xAxisData);
			}
			for(String lineName : lineMap.keySet()){
				legend.data(lineName);
				option.series(lineMap.get(lineName));
			}
			valueAxis.axisLabel().textStyle().fontSize(14);
			category.axisLabel().textStyle().fontSize(14);
			legend.textStyle().fontSize(14);
			option.legend(legend);
			option.xAxis(category);
			if (option.series().size() == 1){
				option.series().get(0).label().normal().textStyle().fontSize(14);
			}
			return option;
		}else{
			throw new Exception("格式错误：折线图数据格式为Map<String, Map<String, Long>>");
		}
	}
	private Line getLine(String lineName, Map<String, Double> makepointMap, Map<String, Double> markeLineMap){
		if(lineMap.containsKey(lineName))
			return lineMap.get(lineName);
		Line line = new Line();
		MarkPoint markPoint = new MarkPoint();
		JSONArray jsonArray1 = new JSONArray("[{type:'max',name:'最大值'},{type:'min',name:'最小值'}]");
		markPoint.setData(jsonArray1.toList());
		if (null != markeLineMap){
			MarkLine markLine = new MarkLine();
			JSONArray jsonArray2 = new JSONArray("["
					+ "{type:'average',name:'平均值'}"
					+ "]");
			markLine.setData(jsonArray2.toList());
			line.setMarkLine(markLine);
		}
		if(null != makepointMap) {
			MarkLine markLine = new MarkLine();
			String uppervaluesStr =  "";
			String lowervaluesStr =  "";
			if(null != makepointMap.get("uppervalues") && 0 != makepointMap.get("uppervalues")) {
				uppervaluesStr = "[{name: '上公差',value:'" + makepointMap.get("uppervalues") + "',x: '15%',yAxis: "
						+ makepointMap.get("uppervalues") + ",},{x: '91%',yAxis: " + makepointMap.get("uppervalues")
						+ ",}],";
			}
			if(null != makepointMap.get("lowervalues") && 0 != makepointMap.get("lowervalues")) {
				lowervaluesStr = "[{name: '下公差',value:'" + makepointMap.get("lowervalues") + "',x: '15%',yAxis: "
						+ makepointMap.get("lowervalues") + ",},{x: '91%',yAxis: " + makepointMap.get("lowervalues")
						+ ",}],";
			}
			if(StringUtils.isNotBlank(uppervaluesStr) || StringUtils.isNotBlank(lowervaluesStr)) {
				JSONArray jsonArray2 = new JSONArray("["
						//+ "{type:'average',name:'平均值'},"
						// + "{type:'max',name:'最大值'},"
						// + "{type:'average',name:'z值'},"
						+ uppervaluesStr
						+ lowervaluesStr
						+ "],");
				List markLineList = jsonArray2.toList();
				markLine.setData(jsonArray2.toList());
				markLine.itemStyle().normal().label().textStyle().fontSize(14);
				line.markLine(markLine);
			}
		}
		line.markPoint(markPoint);
//		line.showAllSymbol(false);
		line.label().normal().setShow(false);
		line.label().normal().textStyle().fontSize(22);
//		line.itemStyle().normal().label().show(false);
		/* slq 折线图样式*/
		line.symbolSize(8);
		line.symbol("circle");

		lineMap.put(lineName, line);
		return line.name(lineName);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Map<String , Map<String, Long>> map = new ConcurrentHashMap<>();
		Map<String, Long> _mMap1 = new ConcurrentHashMap<>();
		_mMap1.put("公司1", 6500L);
		_mMap1.put("公司2", 0L);
		map.put("1", _mMap1);
		Map<String, Long> _mMap2 = new ConcurrentHashMap<>();
		_mMap2.put("公司1", 6700L);
		_mMap2.put("公司2", 7100L);
		map.put("12", _mMap2);
		Map<String, Long> _mMap3 = new ConcurrentHashMap<>();
		_mMap3.put("公司1", 6900L);
		_mMap3.put("公司2", 7300L);
		map.put("19", _mMap3);
		Map<String, Long> _mMap4 = new ConcurrentHashMap<>();
		_mMap4.put("公司1", 7100L);
		_mMap4.put("公司2", 7500L);
		map.put("4", _mMap4);
		Map<String, Long> _mMap5 = new ConcurrentHashMap<>();
		_mMap5.put("公司1", 7300L);
		_mMap5.put("公司2", 7700L);
		map.put("20", _mMap5);
		Map<String, Long> _mMap6 = new ConcurrentHashMap<>();
		_mMap6.put("公司1", 7500L);
		_mMap6.put("公司2", 7900L);
		map.put("6", _mMap6);
		Map<String, Long> _mMap7 = new ConcurrentHashMap<>();
		_mMap7.put("公司1", 0L);
		_mMap7.put("公司2", 7900L);
		map.put("7", _mMap7);
		LineOption lineOption = new LineOption();
		GsonOption option = lineOption.title("asdasd").data(map);
		System.out.println(option.toString());
		for(Series<Line> line : option.getSeries()){
			//List<Line> list = series.data();
			//for(Line line : list){
			System.out.println(line.label().normal().formatter("llllll").show(false));
			//}
		}
		System.out.println(option.toString());

	}


}
