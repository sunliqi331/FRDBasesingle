package com.its.frd.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.AxisLine;
import com.github.abel533.echarts.axis.SplitLine;
import com.github.abel533.echarts.code.MarkType;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.MarkPoint;
import com.github.abel533.echarts.style.LineStyle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.frd.echarts.entity.LineChartOptionModelForList;
import com.its.frd.echarts.entity.LineChartOptionModelForList.GridElement;
import com.its.frd.echarts.entity.LineChartOptionModelForList.LegendElement;
import com.its.frd.echarts.entity.LineChartOptionModelForList.TitleElement;
import com.its.frd.echarts.entity.LineChartOptionModelForList.ToolboxElement;
import com.its.frd.echarts.entity.LineChartOptionModelForList.TooltipElement;
import com.its.frd.echarts.entity.LineChartOptionModelForList.XAxisElement;
import com.its.frd.echarts.entity.LineChartOptionModelForList.YAxisElement;
import com.its.frd.echarts.entity.LineChartOptionModelForList;
import com.its.frd.echarts.entity.LineChartOptionSubModel.AxisPointer;
import com.its.frd.echarts.entity.LineChartOptionSubModel.Textstyle;

public class LineChartUtils {

    /**
     * Demo
     * @return
     */
    public static LineChartOptionModelForList setDemo() {
        LineChartOptionModelForList lineOption = new LineChartOptionModelForList();
        // [title]
        lineOption.getTitle().put(TitleElement.text, "Sales Revenue of CAN-LAX 2016-2017");
        Map<Textstyle, String> textStyleMap = Maps.newHashMap();
        textStyleMap.put(Textstyle.fontSize, "14");
        textStyleMap.put(Textstyle.color, "white");
        lineOption.getTitle().put(TitleElement.textStyle, textStyleMap);

        // [tooltip]// 提示框组件
        lineOption.getTooltip().put(TooltipElement.trigger, "axis");
        Map<AxisPointer, String> axisPointerMap = Maps.newHashMap();// 坐标轴指示器，坐标轴触发有效
        axisPointerMap.put(AxisPointer.type, "shadow");// 默认为直线，可选为：'line' | 'shadow'
        lineOption.getTooltip().put(TooltipElement.axisPointer, "axis");
        String legendElementData[] = {"2016", "2017", "Growing Rate"};
        lineOption.getLegend().put(LegendElement.data, legendElementData);
        lineOption.getLegend().put(LegendElement.top, "18");

        // grid
        lineOption.getGrid().put(GridElement.left, "3%");
        lineOption.getGrid().put(GridElement.right, "5%");
        lineOption.getGrid().put(GridElement.bottom, "3%");
        lineOption.getGrid().put(GridElement.containLabel, true);
        lineOption.getGrid().put(GridElement.show, false);  // 网格边框是否显示，上和右边框 

        // toolbox
        Feature featrue = new Feature();
        featrue.dataView.setShow(false);
        featrue.dataView.setReadOnly(false);
        featrue.saveAsImage.setShow(true);
        lineOption.getToolbox().put(ToolboxElement.feature, featrue);

        // XAxis
        String month[] = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        setXAxis(lineOption, month);
        // YAxis设置
        setYAxis(lineOption);
        // series设置
        List<LineChartOptionModelForList.SeriesItem> series = new ArrayList<>();
        series.add(setSeriesItem1());
        series.add(setSeriesItem2());
        series.add(setSeriesItem3());
        lineOption.setSeries(series);
        return lineOption;
    }

    private static LineChartOptionModelForList.SeriesItem setSeriesItem1() {
        LineChartOptionModelForList.SeriesItem seriesItem = new LineChartOptionModelForList().new SeriesItem();
        seriesItem.setName("2016");
        seriesItem.setType("bar");
        MarkPoint markPoint = new MarkPoint();
        List<Data> dataList = Lists.newArrayList();
        Data dataMax = new Data();
        dataMax.setType(MarkType.max);
        dataMax.setName("最大值");
        dataList.add(dataMax);
        Data dataMin = new Data();
        dataMin.setType(MarkType.max);
        dataMin.setName("最大值");
        dataList.add(dataMin);
        markPoint.setData(dataList);
        seriesItem.setMarkPoint(markPoint);

        MarkLine markLine = new MarkLine();
        Data dataeMarkLine = new Data();
        dataeMarkLine.setType(MarkType.average);
        dataeMarkLine.setName("平均值");
        seriesItem.setMarkLine(markLine);

        List<Double> dataSeriesList = Arrays.asList(1741.9D, 977D, 1742.2D,
                1431.1D, 1636.2D, 1447D, 1711.7D, 1921.2D, 2609.6D, 3332.6D, 3647.3D, 2498.1D);
        seriesItem.setData(dataSeriesList);
        return seriesItem;
    }

    private static LineChartOptionModelForList.SeriesItem setSeriesItem2() {
        LineChartOptionModelForList.SeriesItem seriesItem = new LineChartOptionModelForList().new SeriesItem();
        seriesItem.setName("2017");
        seriesItem.setType("bar");

        // markPoint
        MarkPoint markPoint = new MarkPoint();
        List<Data> dataList = Lists.newArrayList();
        Data dataMax = new Data();
        dataMax.setType(MarkType.max);
        dataMax.setName("最大值");
        dataList.add(dataMax);
        Data dataMin = new Data();
        dataMin.setType(MarkType.max);
        dataMin.setName("最大值");
        dataList.add(dataMin);
        markPoint.setData(dataList);
        seriesItem.setMarkPoint(markPoint);

        // markLine
        MarkLine markLine = new MarkLine();
        Data dataeMarkLine = new Data();
        dataeMarkLine.setType(MarkType.average);
        dataeMarkLine.setName("平均值");
        seriesItem.setMarkLine(markLine);

        List<Double> dataSeriesList = Arrays.asList(2609D, 1162.9D, 2942.9D, 5174.6D,
                5114.4D, 5065.8D, 3956.1D, 3691.1D, 4637.6D, 4603.8D, 6401.1D, 4988.4D);
        seriesItem.setData(dataSeriesList);
        return seriesItem;
    }

    private static LineChartOptionModelForList.SeriesItem setSeriesItem3() {
        LineChartOptionModelForList.SeriesItem seriesItem = new LineChartOptionModelForList().new SeriesItem();
        seriesItem.setName("Growing Rate");
        seriesItem.setType("line");
        seriesItem.setyAxisIndex(1);

        // markPoint
        MarkPoint markPoint = new MarkPoint();
        List<Data> dataList = Lists.newArrayList();
        Data dataMax = new Data();
        dataMax.setType(MarkType.max);
        dataMax.setName("最大值");
        dataList.add(dataMax);
        markPoint.setData(dataList);
        seriesItem.setMarkPoint(markPoint);

        // markLine
        MarkLine markLine = new MarkLine();
        Data dataeMarkLine = new Data();
        dataeMarkLine.setType(MarkType.average);
        dataeMarkLine.setName("平均值");
        seriesItem.setMarkLine(markLine);

        List<Double> dataSeriesList = Arrays.asList(49.8D, 19D, 68.9D, 261.6D, 212.6D,
                250.1D, 131.1D, 92.1D, 77.7D, 38.1D, 75.5D, 99.7D);
        seriesItem.setData(dataSeriesList);
        return seriesItem;
    }

    /**
     * 设置 XAxis
     * @param lineOption
     */
    private static void setXAxis(LineChartOptionModelForList lineOption, Object xAxisData) {
        // xAxis
        lineOption.getxAxis().put(XAxisElement.type, "category");
        AxisLine axisLine = new AxisLine();
        LineStyle linetyle = new LineStyle();
        linetyle.setColor("white");
        axisLine.setLineStyle(linetyle);
        lineOption.getxAxis().put(XAxisElement.axisLine, axisLine);
        lineOption.getxAxis().put(XAxisElement.boundaryGap, true);
        SplitLine splitLine = new SplitLine();
        splitLine.setShow(false);
        lineOption.getxAxis().put(XAxisElement.splitLine, splitLine);
        lineOption.getxAxis().put(XAxisElement.data, xAxisData);
    }

    /**
     * 设置 YAxis
     * @param lineOption
     */
    private static void setYAxis(LineChartOptionModelForList lineOption) {
        // yAxis1
        Map<YAxisElement, Object> yAxisMap1 = Maps.newHashMap();
        yAxisMap1.put(YAxisElement.name, "Revenue(10k)");
        yAxisMap1.put(YAxisElement.type, "value");
        SplitLine splitLine = new SplitLine();
        splitLine.setShow(false);
        yAxisMap1.put(YAxisElement.splitLine, splitLine);
        AxisLine axisLine = new AxisLine();
        LineStyle linetyle = new LineStyle();
        linetyle.setColor("white");
        axisLine.setLineStyle(linetyle);
        yAxisMap1.put(YAxisElement.axisLine, axisLine);
        AxisLabel axisLabel = new AxisLabel();
        axisLabel.setFormatter("{value}");
        lineOption.getyAxis().add(yAxisMap1);

        // yAxis2
        Map<YAxisElement, Object> yAxisMap2 = Maps.newHashMap();
        yAxisMap2.put(YAxisElement.name, "Growing\\nRate (%)");
        yAxisMap2.put(YAxisElement.type, "value");
        splitLine = new SplitLine();
        splitLine.setShow(false);
        yAxisMap2.put(YAxisElement.splitLine, splitLine);
        yAxisMap2.put(YAxisElement.min, 0);
        yAxisMap2.put(YAxisElement.max, 300);
        yAxisMap2.put(YAxisElement.inverse, false);
        axisLine = new AxisLine();
        linetyle = new LineStyle();
        linetyle.setColor("white");
        axisLine.setLineStyle(linetyle);
        yAxisMap2.put(YAxisElement.axisLine, axisLine);
        axisLabel = new AxisLabel();
        axisLabel.setFormatter("{value}");
        lineOption.getyAxis().add(yAxisMap2);
    }

    public static void main(String[] args) {

    }

}
