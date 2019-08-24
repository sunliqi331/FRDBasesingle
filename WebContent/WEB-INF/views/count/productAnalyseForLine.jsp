<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<link href="${contextPath }/styles/css/analyse/analyse.css" rel="stylesheet">

<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div>
<div class="maindiv" id="maindiv">
    <div id="maindata1" class="maindata1"></div>
    <div class="modal-footer"></div>
<div class="clearfix"></div>
<div class="modal-footer">
  <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
</div>
</div>
<script src="${contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/chalk.js"></script>
<script type="text/javascript" src="${contextPath }/js/analyse/analyse.js"></script>
<!-- <script type="text/javascript" src="${contextPath }/js/analyse/maindoc.js"></script> -->
<script type="text/javascript" src="${contextPath }/js/analyse/echartProduct.js"></script>

<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>

<script type="text/javascript">
var dataflg = "${dataflg}";
//产品名
var productNm = '${productNm}';
/*
if(0 != dataflg){
   alert(productNm + "：该产品没有数据，请选择其他产品进行分析。");
   window.close();
} */
var contextPathJS = "${contextPath}";
var seriesData = ${seriesData};
var seriesPercentListData = ${seriesPercentListData};
// NG率
var ngPercent = ${ngPercent};
// OK率
var okPercent = ${okPercent};
// 时间段
var xAxisDataFromValue = ${xAxisData};//JSON.parse('');

//主方法
$(document).ready(function(){
    var maindata1 = echarts.init(document.getElementById('maindata1'));
    maindata1.clear();
    var obj1Text = productNm + 'bar';
    var obj2Text = productNm + 'line';
    var protext = "产品" + productNm;
    var yAxisNm1 = "产品产量";
    var yAxisNm2 = "产品产量比例（%）";
    var xAxisData = new Array(
            '0时', '1时', '2时', '3时', '4时', '5时',
            '7时', '8时', '9时', '10时', '11时', '12时',
            '13时', '14时', '15时', '16时', '17时', '18时',
            '19时', '20时', '21时', '22时', '23时', '24时'
    );
    xAxisData = xAxisDataFromValue;
    var seriesdata0 = new Array(
            1741.9, 977, 1742.2, 1431.1, 1636.2,
            1447, 1711.7, 1921.2, 2609.6, 3332.6, 3647.3, 2498.1,
            1741.9, 977, 1742.2, 1431.1, 1636.2,
            1447, 1711.7, 1921.2, 2609.6, 3332.6, 3647.3, 2498.1
    );
    seriesdata0 = seriesData;
    var seriesdata1 = new Array(
            49.8, 19, 68.9, 261.6, 212.6, 250.1,
            131.1, 92.1, 77.7, 38.1, 75.5, 99.7,
            49.8, 19, 68.9, 261.6, 212.6, 250.1,
            131.1, 92.1, 77.7, 38.1, 75.5, 99.7
    );
    seriesdata1 = seriesPercentListData;
    optionsForProduct[0].title.text = protext;
    optionsForProduct[0].legend.data = new Array(obj1Text, obj2Text);
    optionsForProduct[0].xAxis.data = xAxisData;
    optionsForProduct[0].yAxis[0].name = yAxisNm1;
    optionsForProduct[0].yAxis[1].name = yAxisNm2;

    optionsForProduct[0].series[0].data = seriesdata0;
    optionsForProduct[0].series[0].name = obj1Text;
    optionsForProduct[0].series[1].data = seriesdata1;
    optionsForProduct[0].series[1].name = obj2Text;

    // maindata1.setOption(optionsForProduct[0]);
});
</script>
