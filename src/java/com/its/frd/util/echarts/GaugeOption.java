package com.its.frd.util.echarts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.AxisLine;
import com.github.abel533.echarts.axis.AxisTick;
import com.github.abel533.echarts.axis.SplitLine;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Gauge;
import com.github.abel533.echarts.series.gauge.Detail;
import com.github.abel533.echarts.series.gauge.Pointer;
import com.github.abel533.echarts.style.LineStyle;
import com.github.abel533.echarts.style.TextStyle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GaugeOption extends EchartsOption<GaugeOption> {

    Legend legend = new Legend();
    public GsonOption data(Object o_map) throws Exception{
//        if (o_map instanceof Map) {
//            @SuppressWarnings("unchecked")
//            Map<String, Map<String,Double>> map = (Map<String, Map<String,Double>>) o_map;
//            legend.orient(Orient.vertical).left(X.left);
//            Tooltip tooltip = new Tooltip();
//            tooltip.trigger(Trigger.item).formatter("{a} <br />{b} : {c} ({d}%)");
//            option.tooltip(tooltip);
//            for(String key : map.keySet()){
//                legend.data(key);
//                Map<String,Double> value = map.get(key);
//                if(value.size() > 1){
//                    throw new Exception("格式错误：仪表盘数据格式为Map<String, Map<String,Double>>,其中 Map<String,Double>只应该有一组值");
//                }else{
//                    if(value.size() != 0){
//                        for(String _key : value.keySet()){
//                            gauge.name(_key).data(new GaugeData(key, value.get(_key)));
//                        }
//                    }else{
//                        gauge.name("").data(new GaugeData(key, 0));
//                    }
//                }
//            }
//            option.legend(legend).series(gauge);
//            return option;
//        }else{
//            throw new Exception("格式错误：仪表盘数据格式为Map<String, Double>");
//        }
        GsonOption option = new GsonOption();
        // option.tooltip().formatter("{a} <br/>{b} : {c}%");
        // option.toolbox().show(true).feature(Tool.mark, Tool.restore, Tool.saveAsImage);
        option.backgroundColor("#1b1b1b");
        // option.series(new Gauge("业务指标").detail(new Detail().formatter("{value}%")).data(new Data("完成率", 75)));

        Gauge gaugeRs = new Gauge();
        // 仪表盘标题。
        gaugeRs.title().show(true);
        // 仪表盘详情，用于显示数据。
        gaugeRs.detail().show(true);
        
        gaugeRs.startAngle(180);
        gaugeRs.endAngle(0);

        // 系列名称，用于tooltip的显示，legend 的图例筛选，在 setOption 更新数据和配置项时用于指定对应的系列。
        gaugeRs.name("油表");
        // 默认全局居中
        //gaugeRs.center("75%", "50%");
        // 仪表盘半径，可以是相对于容器高宽中较小的一项的一半的百分比，也可以是绝对的数值。
        gaugeRs.radius("80%");
        // 最小的数据值，映射到 minAngle。[ default: 0 ]
        gaugeRs.min(0);
        // 最大的数据值，映射到 maxAngle。[ default: 100 ]
        gaugeRs.max(2);
        // 仪表盘刻度的分割段数。 [ default: 10 ]
        gaugeRs.splitNumber(2);

        // 仪表盘轴线相关配置
         setAxisLine(gaugeRs);
        // 刻度样式配置
        setAxisTick(gaugeRs);
        // 刻度标签配置
        setAxisLabel(gaugeRs);
        // 分隔线样式。
        setSplitLine(gaugeRs); 
        // 仪表盘指针。
        setPointer(gaugeRs);

        // 数据设定
        gaugeRs.data(new Data("耗气量", 0.5));
        setTitle(gaugeRs);

        option.series(gaugeRs);

        return option;
    }

    /**
     * axisLine 仪表盘轴线相关配置。
     * @param gaugeRs
     */
    @SuppressWarnings("serial")
    private void setAxisLine(Gauge gaugeRs) {
        // LineStyle 仪表盘轴线样式。
        gaugeRs.axisLine(new AxisLine().lineStyle(new LineStyle() {
            private LineStyle setLineStyle() {
                LineStyle lineStyle = new LineStyle();

//                Map<Object, Object> colorMap = Maps.newHashMap();
//                colorMap.put(0.2,"lime");
//                colorMap.put(0.8,"#1e90ff");
//                colorMap.put(1,"#ff4500");

                List<Object> colorListOut = Lists.newArrayList();
                List<Object> colorList = Lists.newArrayList();
                colorList.add("0.2");
                colorList.add("lime");
                colorListOut.add(colorList);
                colorList = Lists.newArrayList();
                colorList.add(0.8);
                colorList.add("#1e90ff");
                colorListOut.add(colorList);
                colorList = Lists.newArrayList();
                colorList.add(1);
                colorList.add("#ff4500");
                colorListOut.add(colorList);
//                colorList.add(colorMap);

                // color 仪表盘的轴线可以被分成不同颜色的多段。每段的结束位置和颜色可以通过一个数组来表示。
                // 默认值 [[0.2, '#91c7ae'], [0.8, '#63869e'], [1, '#c23531']]
                // lineStyle.color(colorMap);
                lineStyle.color(colorListOut);
                // width 轴线宽度 [ default: 30 ]
                lineStyle.width(2);
                // shadowColor 阴影颜色。支持的格式同color。
                lineStyle.shadowColor("#bbb");
                // shadowBlur 图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY
                // 一起设置图形的阴影效果。
                lineStyle.shadowBlur(10);
                return lineStyle;
            }
        }.setLineStyle()));
    }

    /**
     * 刻度样式配置
     * @param gaugeRs
     */
    private void setAxisTick(Gauge gaugeRs) {
    	gaugeRs.setAxisTick(new AxisTick().length(2).lineStyle(
    			new LineStyle().color("auto").shadowColor("#bbb").shadowBlur(10)));
    }

    /**
     * 刻度标签配置
     * @param gaugeRs
     */
    @SuppressWarnings("serial")
    private void setAxisLabel(Gauge gaugeRs) {
        gaugeRs.setAxisLabel(new Label() {
            private Label setLabel() {
                Label rs = new Label();
                rs.setTextStyle(new TextStyle() {
                    private TextStyle setTextStyle() {
                        TextStyle textStyle = new TextStyle();
                        textStyle.fontWeight("bolder");
                        textStyle.color("#fff");
                        return textStyle;
                    }
                }.setTextStyle());
                // rs.formatter("function(v){switch (v + '') {case '0' : return 'E';case '1' : return 'Gas';case '2' : return 'F';}}");
                //rs.formatter("function(v){switch (v) {case 0 : return E;case 1 : return Gas;case 2 : return F;}}");
                return rs;
            }
        }.setLabel());
    }

    /**
     * 分隔线样式配置
     * @param gaugeRs
     */
    @SuppressWarnings("serial")
    private void setSplitLine(Gauge gaugeRs) {
        gaugeRs.setSplitLine(new SplitLine() {
            private SplitLine setSplitLine() {
                SplitLine rs = new SplitLine();
                rs.lineStyle(new LineStyle() {
                    private LineStyle setLineStyle() {
                        LineStyle lineStyle = new LineStyle();
                        lineStyle.width(3);
                        lineStyle.color("#bbb");
                        // 阴影颜色。支持的格式同color。
                        lineStyle.shadowColor("#bbb");
                        // 图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。
                        lineStyle.shadowBlur(10);
                        return lineStyle;
                    }
                }.setLineStyle());
                return rs;
            }
        }.setSplitLine());
    }

    /**
     * 仪表盘指针配置
     * @param gaugeRs
     */
    @SuppressWarnings("serial")
    private void setPointer(Gauge gaugeRs) {
        gaugeRs.setPointer(new Pointer() {
            private Pointer setPointer() {
                Pointer rs = new Pointer();
                rs.width(2);
                return rs;
            }
        }.setPointer());
    }

    /**
     * 仪表盘指针配置
     * @param gaugeRs
     */
    @SuppressWarnings("serial")
    private void setTitle(Gauge gaugeRs) {
    	gaugeRs.title().show(true).textStyle().color("#fff");
//        gaugeRs.setTitle(new Title() {
//            private Title setTitle() {
//                Title rs = new Title();
//                rs.borderColor("fff");
//                rs.backgroundColor("#1b1b1b");
//                return rs;
//            }
//        }.setTitle());
    }

    public static void main(String[] args) throws Exception {
        // 地址： http://echarts.baidu.com/doc/example/gauge1.html
        GsonOption option = new GsonOption();
        option.tooltip().formatter("{a} <br/>{b} : {c}%");
        option.toolbox().show(true).feature(Tool.mark, Tool.restore, Tool.saveAsImage);
        option.series(new Gauge("业务指标").detail(new Detail().formatter("{value}%")).data(new Data("完成率", 75)));
        option.exportToHtml("guage1.html");
        option.view();
    }
}
