<%@ page language="java" contentType="text/html; charset=UTF-8"
	trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>countChart</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>

</head>

<body>
	<button onclick="getDataAndShowPieChart()">我是按钮.</button>
	<div id="main" style="width: 600px; height: 400px;"></div>
</body>

<script type="text/javascript">

	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'));
	//从后台获取数据
	function getDataAndShowPieChart() {
		$.ajax({
			type : "GET",
			url : "http://10.12.65.144:8080/FRDBase/stats/pieChart",
			data : "passType='xxx'&id='222'",
			dataType : "json",
			success : function(data) {
				showPieChart(myChart, data);
			}
		});
	}

	function showPieChart(myChart, data) {
		// 指定图表的配置项和数据
		option = {
			title : {
				text : data.TITLE.TEXT,//'某站点用户访问来源',
				subtext : data.SUBTEXT,
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : data.LEGEND,
			series : data.SERIES
		};

		myChart.setOption(option);
	}
</script>
</html>
