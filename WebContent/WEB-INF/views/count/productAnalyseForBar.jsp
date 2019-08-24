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
    <!-- 
    <div id="maindata2" class="maindata2"></div>
    <div id="maindata3" class="maindata3"></div>
     -->
    <div id="maindata4" class="maindata4"></div>
    <!-- 
    <div id="maindata5" class="maindata5"></div>
    <div id="maindata6" class="maindata6"></div>     -->
    <div class="modal-footer">
</div>
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

// 检索种类【1:产量图标 2:合格率图标】
var searchKind = ${searchKind};

//主方法
$(document).ready(function(){
//  console.log(lineOptionData);
    //$("#maindiv").css('height',$(window).height());
    //$("#maindiv").css('width',$(window).width());
    if("1" == searchKind) {
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

    maindata1.setOption(optionsForProduct[0]);
    }
    /*
    var maindata2 = echarts.init(document.getElementById('maindata2'));
    maindata2.clear();
    maindata2.setOption(options[1]);

    var maindata3 = echarts.init(document.getElementById('maindata3'));
    maindata3.clear();
    maindata3.setOption(options[2]);
    */
    if("2" == searchKind) {
    var maindata4 = echarts.init(document.getElementById('maindata4'));
    maindata4.clear();

    var titleTxt4 = "产品 " + productNm + "合格率";
    var titleSubTxt4 = "2018";
    var legendTxt4 = ['直达', '中转', '联程', '邮件'];
    var seriesName4 = "Cargo Source";
    var seriesData4 = [
        {name:"直达",value: 61.8},
        {name:"联程",value: 13.2},
        {name:"中转",value: 24.2},
        {name:"邮件",value: 0.8}
    ];
    seriesData4 = [
        {name:"合格",value: okPercent},
        {name:"不合格",value: ngPercent}
    ];
    console.log("合格:" + okPercent);
    console.log("不合格:" + ngPercent);
    optionsForProduct[1].title.text = titleTxt4;
    optionsForProduct[1].title.subtext = titleSubTxt4;
    optionsForProduct[1].legend.data = new Array("合格","不合格");
    optionsForProduct[1].series[0].name = seriesName4;
    optionsForProduct[1].series[0].data = seriesData4;

    maindata4.setOption(optionsForProduct[1]);
    }
    /*
    var maindata5 = echarts.init(document.getElementById('maindata5'));
    maindata5.clear();
    maindata5.setOption(options[4]);

    var maindata6 = echarts.init(document.getElementById('maindata6'));
    maindata6.clear();
    maindata6.setOption(options[5]); 
    */
});
</script>
