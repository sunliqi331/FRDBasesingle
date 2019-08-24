package com.its.frd.echarts.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.MarkPoint;
import com.google.common.collect.Lists;

/**
 * 完全以折线图为导向设计的实体模型
 */
public class LineChartOptionModel {
	public enum TitleElement{
		text,textStyle
	}
	public enum LegendElement{
		data, top
	}
	public enum GridElement{
		left,right,bottom,containLabel, show
	}
	public enum ToolboxElement{
	    feature
	}
	public enum XAxisElement{
		type,boundaryGap,data, axisLine, splitLine
	}
	public enum YAxisElement{
		type, name, splitLine, axisLine, axisLabel, min, max, inverse
	}
	public enum TooltipElement{
		trigger, axisPointer
	}
	private Map<TitleElement,Object> title = new HashMap<>();
	private Map<LegendElement,Object> legend = new HashMap<>();
	private Map<GridElement,Object> grid = new HashMap<>();
	private Map<ToolboxElement,Object> toolbox = new HashMap<>();
	private Map<XAxisElement,Object> xAxis = new HashMap<>();
	private Map<YAxisElement,Object> yAxis = new HashMap<>();
	private Map<TooltipElement,Object> tooltip = new HashMap<>();
	private List<SeriesItem> series = new ArrayList<>();
	private List<Map<YAxisElement,Object>> yAxisList = Lists.newArrayList();

    public LineChartOptionModel(){
		this.grid.put(GridElement.left, "3%");
		this.grid.put(GridElement.right, "4%");
		this.grid.put(GridElement.bottom, "3%");
		this.grid.put(GridElement.containLabel, true);
		
		this.xAxis.put(XAxisElement.type, "category");
		this.xAxis.put(XAxisElement.boundaryGap, false);
		
		this.yAxis.put(YAxisElement.type, "value");
		
		//this.title.put(TitleElement.text, "折线图");
		
		this.tooltip.put(TooltipElement.trigger, "axis");
	}
	
	public class SeriesItem{
		private String name = "数据";
		private String type = "line";
		private String color = "";
        private String stack  = "";
        private int yAxisIndex;
		private MarkPoint markPoint;
		private MarkLine markLine;
        private List<Double> data = new ArrayList<>();
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
        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }
        public String getStack() {
            return stack;
        }
        public void setStack(String stack) {
            this.stack = stack;
        }
        public MarkPoint getMarkPoint() {
            return markPoint;
        }
        public void setMarkPoint(MarkPoint markPoint) {
            this.markPoint = markPoint;
        }
        public MarkLine getMarkLine() {
            return markLine;
        }
        public void setMarkLine(MarkLine markLine) {
            this.markLine = markLine;
        }
        public int getyAxisIndex() {
            return yAxisIndex;
        }
        public void setyAxisIndex(int yAxisIndex) {
            this.yAxisIndex = yAxisIndex;
        }
	}

	public Map<TitleElement, Object> getTitle() {
		return title;
	}

	public void setTitle(Map<TitleElement, Object> title) {
		this.title = title;
	}

	public Map<LegendElement, Object> getLegend() {
		return legend;
	}

	public void setLegend(Map<LegendElement, Object> legend) {
		this.legend = legend;
	}

	public Map<GridElement, Object> getGrid() {
		return grid;
	}

	public void setGrid(Map<GridElement, Object> grid) {
		this.grid = grid;
	}

	public Map<ToolboxElement, Object> getToolbox() {
		return toolbox;
	}

	public void setToolbox(Map<ToolboxElement, Object> toolbox) {
		this.toolbox = toolbox;
	}

	public Map<XAxisElement, Object> getxAxis() {
		return xAxis;
	}

	public void setxAxis(Map<XAxisElement, Object> xAxis) {
		this.xAxis = xAxis;
	}

	public Map<YAxisElement, Object> getyAxis() {
		return yAxis;
	}

	public void setyAxis(Map<YAxisElement, Object> yAxis) {
		this.yAxis = yAxis;
	}

	public List<SeriesItem> getSeries() {
		return series;
	}

	public void setSeries(List<SeriesItem> series) {
		this.series = series;
	}

	public Map<TooltipElement, Object> getTooltip() {
		return tooltip;
	}

	public void setTooltip(Map<TooltipElement, Object> tooltip) {
		this.tooltip = tooltip;
	}
	   
    public List<Map<YAxisElement, Object>> getyAxisList() {
        return yAxisList;
    }

    public void setyAxisList(List<Map<YAxisElement, Object>> yAxisList) {
        this.yAxisList = yAxisList;
    }
	
}
