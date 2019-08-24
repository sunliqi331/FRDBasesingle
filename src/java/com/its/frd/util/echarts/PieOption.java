package com.its.frd.util.echarts;

import java.util.HashMap;
import java.util.Map;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Pie;

public class PieOption extends EchartsOption<PieOption> {

	private Pie pie = new Pie();
	Legend legend = new Legend();
	public GsonOption data(Object o_map) throws Exception{
		if (o_map instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Map<String,Double>> map = (Map<String, Map<String,Double>>) o_map;
			legend.orient(Orient.vertical).left(X.left);
			Tooltip tooltip = new Tooltip();
			tooltip.trigger(Trigger.item).formatter("{a} <br />{b} : {c} ({d}%)");
			option.tooltip(tooltip);
			for(String key : map.keySet()){
				legend.data(key);
				Map<String,Double> value = map.get(key);
				if(value.size() > 1){
					throw new Exception("格式错误：饼图数据格式为Map<String, Map<String,Double>>,其中 Map<String,Double>只应该有一组值");
				}else{
					if(value.size() != 0){
						for(String _key : value.keySet()){
							pie.name(_key).data(new PieData(key, value.get(_key))).radius("50%", "70%");
						}
					}else{
						pie.name("").data(new PieData(key, 0)).radius("50%", "70%");
					}
				}
			}
			option.legend(legend).series(pie);
			return option;
		}else{
			throw new Exception("格式错误：饼图数据格式为Map<String, Double>");
		}
		
	}
    public GsonOption dataHaveRadis(Object o_map, String kind) throws Exception{
        if (o_map instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Map<String,Double>> map = (Map<String, Map<String,Double>>) o_map;
            legend.orient(Orient.vertical).left(X.left);
            Tooltip tooltip = new Tooltip();
            tooltip.trigger(Trigger.item).formatter("{a} <br />{b} : {c} ({d}%)");
            option.tooltip(tooltip);
            for(String key : map.keySet()){
                legend.data(key);
                Map<String,Double> value = map.get(key);
                if(value.size() > 1){
                    throw new Exception("格式错误：饼图数据格式为Map<String, Map<String,Double>>,其中 Map<String,Double>只应该有一组值");
                }else{
                    if(value.size() != 0){
                        for(String _key : value.keySet()){
                            if("pie".equals(kind)) {
                                pie.name(_key).data(new PieData(key, value.get(_key))).radius("50%", "70%");
                            } else {
                                pie.name(_key).data(new PieData(key, value.get(_key)));
                            }

                        }
                    }else{
                        if("pie".equals(kind)) {
                            pie.name("").data(new PieData(key, 0)).radius("50%", "70%");
                        } else {
                            pie.name("").data(new PieData(key, 0));
                        }
                        
                    }
                }
            }
            option.legend(legend).series(pie);
            return option;
        }else{
            throw new Exception("格式错误：饼图数据格式为Map<String, Double>");
        }
        
    }
	public static void main(String[] args) throws Exception {
		Map<String, Map<String,Double>> map = new HashMap<>();
		
		Map<String,Double> _map1 = new HashMap<>();
		_map1.put("产量", 123d);
		map.put("zhangsan", _map1);
		Map<String,Double> _map2 = new HashMap<>();
		_map2.put("产量", 456d);
		map.put("lisi", _map2);
		Map<String,Double> _map3 = new HashMap<>();
		_map3.put("产量", 789d);
		map.put("wangwu", _map3);
		Map<String,Double> _map4 = new HashMap<>();
		_map4.put("产量", 321d);
		map.put("zhaoliu", _map4);
		PieOption pieOption = new PieOption();
		System.out.println(pieOption.title("hahaha").data(map));
	}
}
