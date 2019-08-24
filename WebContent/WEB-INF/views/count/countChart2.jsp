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
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/animate.css" rel="stylesheet" />
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<script src="${contextPath}/js/dateRangeUtil.js"></script>
<script src="${contextPath}/js/jquery.js}"></script>
<script src="${contextPath}/js/bootstrap.js}"></script>
</head>

<body>
  <div id="container">
      <%@ include file="/WEB-INF/views/include.header.jsp"%>
      <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
       <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>统计</li>
      </ol>
      <div class="main-wrap" >
        <div class="main-body">
       <ul class="tabs clearfix"  >  
        <!-- <li class="active"><a class="tab1" href="#tab1">全部</a></li>   -->
        <li style="margin-left: -1px"><a class="tab2" href="#tab2">产品</a></li>
        <li style="margin-left: -1px"><a class="tab3" href="#tab3">设施</a></li>
      </ul>
          <div class="tab_container chart_tab"> 
         <div id="tab2" class="tab_content" style="display: none;padding-bottom:15px;min-height: 100px;">
          <div class="driver_info">
             <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">产品名称</label>
                 <select name="modelnum" id="modelnum" class="form-control searchtext" style="padding-right: 50px">
                      <!--  <option id="AllModelnum" value="">全部</option> -->
                      <c:forEach var="p" items="${mesProduct}">
                      <option value="${p.modelnum }">${p.name }</option>
                      </c:forEach>
                 </select> 
              </div>

              <!-- 时间选择器  --------------------------------------------start---->
              <div class="form-group">
                 <label for="inputText" class="searchtitle">时间选择</label>
                 <select name="search_time" id="search_time" class="form-control searchtext">
                      <option value="1">本日</option>
                      <option value="2">本周</option>
                      <option value="3">本月</option>
                      <option value="4">本年</option>
                      <option value="define_time">自定义时间段</option>
                 </select> 
              </div>
                <div class="form-group rangetime" style="width:305px;">
                    <label for="inputText" class="searchtitle" style="float:left;padding-top:4px;">开始时间</label>
                     <div class="controls input-append date form_datetime1" style="position:relative" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="begin">
                        <input id="dtp_input1s" class="form-control datetime" type="text" style=" background: #fff; " value="" readonly>
                        <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                          <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                     </div>
                     <input type="hidden" id="begin" value="" name="starttime"/>
                </div>
                <div class="form-group rangetime" style="width:305px;">
                  <label for="inputText" class="searchtitle" style="float:left;padding-top:4px;">结束时间</label>
                  <div class="controls input-append date form_datetime2" style="position:relative"  data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="end">
                    <input class="form-control datetime " type="text" style=" background: #fff;" value="" readonly>
                     <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                  </div>
                  <input type="hidden" id="end" value="" />
               </div>
               <!-- 时间选择器  --------------------------------------------end---->
              <a class="btn btn-default1" id="searchForEcharts" href="#">
                  <i class="fa fa-bar-chart"></i>
                  <span>产量图表展示</span>
               </a>
              <a class="btn btn-default1" id="searchBarForEcharts" href="#">
                  <i class="fa fa-adjust"></i>
                  <span>合格图表展示</span>
               </a>
              </form>
              
          </div>

           <div style="width: 100%; min-height: 350px;">
             <font style="font-size:20px;font-weight:bold ">合格率统计:</font>
             <div style=" height:1px; width:100% ; background:#000000; overflow: hidden"></div>

             <div>
               <div class="chartContent">
                 <div id="main3" style="height:300px;width:480px; margin-left:auto; margign-right:auto; margin-top: 20px; " ></div>
                 <div id="main3_5" style="height:300px;width:480px; margin-left:auto; margign-right:auto; margin-top: 20px;" ></div>
               </div>
               </div>
           </div>

           <div style="width:100%; min-height: 750px;">
             <font style="font-size:20px;font-weight:bold ">产量统计:</font>
             <div style=" height:1px; width:100% ; background:#000000; overflow: hidden"></div>
             <div class="chartContent"><div id="main3_1" style="height:300px;width:930px;margin-left:auto;margin-right:auto; margin-top: 20px;"></div></div>
             <div class="chartContent"><div id="main3_1_5" style="height:300px;width:930px; margin-left:auto;margin-right:auto; margin-top: 20px;" ></div></div>
           </div>
           
           </div>
<script type="text/javascript">
var myChart_3 = echarts.init(document.getElementById('main3'));
var myChart_3_5 =echarts.init($("#main3_5")[0]); 
var myChart_3_1 = echarts.init(document.getElementById('main3_1'));
var myChart_3_1_5 =echarts.init($("#main3_1_5")[0]); 
$(document).ready(function($) {
    productCount(); 
}); 
 
$("#search1").click(productCount);

//从后台获取数据
 function productCount(){
    $("#main3").show();
    $("#main3_5").show();
    $("#main3_1").show();
    $("#main3_1_5").show();
    getDataAndShowPieChart_3();
    getDataAndShowPieChart_3_1();
}

function getDataAndShowPieChart_3(data) {
    //chooseCompanyRange();
    var modelnum = $("#modelnum").val();
    judgeDate();
    $.ajax({
        type : "GET",
        url : "${contextPath }/stats/pieChart",
        //data : "startDate="+startDate+"&endDate="+endDate+"&dateType="+dateType
        //  +"&typeScope="+typeScope+"&modelnum="+modelnum+"&passType=COUNT&id="+id+"",
        data : "startDate="+startDate+"&endDate="+endDate+"&dateType="+dateType
            +"&modelnum="+modelnum+"&passType=COUNT&id="+$("#chooseCompany").val()+"",
        dataType : "json", 
        success : function(data) {
            showPieChart_3(data);
            showPieChart_3_5(data);
        },
          error:function(jqXHR, textStatus, errorThrown){
              console.log(textStatus);
          }
    });
}
function showPieChart_3(data) {
    var series=data.Option.SERIES;
    var legend=data.Option.LEGEND;
    series.radius='55%';
    option_3 = {
        tooltip : {
            trigger : 'item',
            formatter : "{a} <br/>{b} : {c} ({d}%)"
        },
        grid: {
            left: '3%',
            right: '3%',
            bottom: '0%',
            containLabel: true
        },
        legend : legend,
        series : series,
        color:['#7ac8f9', '#de6942'] 
    };
    $("#count").val(data.Count);
    $("#passcount").val(data.PassCount); 
    myChart_3.clear();
    myChart_3.setOption(option_3);
}

function showPieChart_3_5(data) {
    var series=data.Option.SERIES;
    var legend=data.Option.LEGEND; 
    series.radius='55%'; 
    option_3_5 = {
             tooltip: {},
             legend: {
                 data:['合格','不合格']
             },
             xAxis: {
                 data: ["合格","不合格"]
             },
             yAxis: {},
             series: [{
                 name: '数量',
                 type: 'bar',
                 data: [series.data[0].value, series.data[1].value]
             }]
         };
    myChart_3_5.clear();
    myChart_3_5.setOption(option_3_5);
}

//从后台获取数据
function getDataAndShowPieChart_3_1(data) {
    //chooseCompanyRange();
    modelnum = $("#modelnum").val();
    judgeDate();
    $.ajax({
        type : "GET",
        url : "${contextPath }/stats/productionBarChart",
        //data : "startDate="+startDate+"&endDate="+endDate+"&typeScope="+typeScope+"&modelnum="+modelnum+"&passType=COUNT&id="+id+"&dateType="+dateType+"",
        data : "startDate="+startDate+"&endDate="+endDate+"&modelnum="+modelnum+"&passType=COUNT&id="+$("#chooseCompany").val()+"&dateType="+dateType+"",
        dataType : "json", 
        success : function(data) {
            showPieChart_3_1(data);
            showPieChart_3_1_5(data);
        },
          error:function(jqXHR, textStatus, errorThrown){
              console.log(textStatus);
          }
    });
}

function showPieChart_3_1(data) {
    var tempSeries = data.SERIES;
    for(var i=0;i<tempSeries.length;i++){
        delete tempSeries[i].barWidth;
    }
    var option_3_1 = {
            color: ['#7ac8f9'],
            legend: data.LEGEND,
            calculable: data.calculable,
            toolbox: data.TOOLBOX,
            tooltip: data.TOOLTIP,
            xAxis: data.xAxis,
            yAxis: data.yAxis,
            series: tempSeries
         };
    myChart_3_1.clear();
    myChart_3_1.setOption(option_3_1);
}

function showPieChart_3_1_5(data) {
    var tempSeries = data.SERIES;
    $.each(tempSeries,function(index,obj){
        var $smooth = {smooth:true,type:'line'};
        tempSeries[index] = $.extend({},obj,$smooth);
    });
    var option_3_1_5 = {
            color: ['#7ac8f9'],
            legend: data.LEGEND,
            calculable: data.calculable,
            toolbox: data.TOOLBOX,
            tooltip: {"trigger": 'axis'},
            xAxis: data.xAxis,
            yAxis: data.yAxis,
            series: tempSeries
         };
    myChart_3_1_5.clear();
    myChart_3_1_5.setOption(option_3_1_5);
    //console.log(JSON.stringify(myChart_3_1_5.getOption()));
}

/* var resizeMainContainer = function () {
    $("div[id^='main']").each(function(idx,item){
     $(item).css("width",$(item).parent().width()+"px");
     $(item).css("height",$(item).parent().height()+"px");
    });
   }; 
   window.onresize = function () {
       //重置容器高宽
       resizeMainContainer();
       myChart_3.resize();
       myChart_3_1.resize();
   }; */
</script>
      <div id="tab3" class="tab_content " style="display: none;padding-bottom:15px;min-height:100px !important;">
      <div class="driver_info">
          <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
            
            <input type="hidden" id="chooseCompany" value="${company.id}" />
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择工厂</label>
                 <select id="chooseFactory" class="form-control searchtext">
                  <option value="">全部</option> 
                  <c:forEach var="p" items="${companyinfos}">
                  <option value="${p.id }">${p.companyname}</option>
                  </c:forEach>
                 </select>
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择产线</label>
                 <select id="chooseProductLine" class="form-control searchtext">
                  <option value="">全部</option> 
                  </select> 
              </div>
            
             <div class="form-group">
              <label for="inputText" class="searchtitle">选择设备</label>
               <select name="ChooseDriver" id="ChooseDriver" class="form-control searchtext">
                    <option value="">全部</option>
               </select>
               </div>
              <div class="form-group">
               <label for="inputText" class="searchtitle">统计类型</label>
               <select name="EnergyType" id="EnergyType" class="form-control searchtext">
                  <option value="electric">耗电量</option>
                  <option value="water">耗水量</option>
                  <option value="gas">耗气量</option>
                  <!--  <option value ="runtime">运行时间</option> -->
               </select>
            </div>
            
            <div class="form-group countBut">
            <a class="btn btn-default1" id="searchFosearchForEchartsForDriverrEcharts" href="#">
                <i class="fa fa-bar-chart"></i>
                <span>设备分析图表</span>
            </a>
            <button id="exportExcel" type="button" class="btn btn-info" style="float: right;margin-left: 20px;display:none;">导出表格</button> 
            <button id="search2" type="button" class="btn btn-info" style="float: right; margin-left: 20px;display:none;">搜索</button>
            <div style="clear:both"></div>
            </div> 
            <iframe id="ifile" style="display:none"></iframe>
          </form>
      </div>
      <!-- <div class="tab_overx" style="height:750px"> -->
      
      <!-- 选择全部设备 -->
      <div id="allDriver" style="min-height:360px;display:none;">
            <div>
                <table class="table table-striped" id="driverEnergyTable" data-field="DriverEnergy"  data-url="#">
                    <thead>
                        <tr>
                            <th data-field="" >序号</th>
                            <th data-field="updatetime" >时间</th>
                            <th data-field="mesDriver.companyinfo.companyname">工厂</th>
                            <th data-field="mesDriver.mesProductline.linename">产线</th>
                            <th data-field="mesDriver.name">设备名称</th>
                            <th data-field="energytype">能耗类型</th>
                            <th data-field="value">耗量</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <br>
            <div class="chartContent" style="width:100%;overflow: auto;">
                <div id="main4" style="height:350px;width:850px;"></div>
            </div>
      </div>
      
      <!-- 选择具体的设备 -->
      <div id="concreteDriver" style="min-height:500px;">
            <div>
                <table class="table table-striped" id="concreteDriverTable" data-field="Driverenergy"  data-url="#">
                    <thead>
                        <tr>
                            <th data-field="" >序号</th>
                            <th data-field="updatetime" >时间</th>
                            <th data-field="mesDriver.companyinfo.companyname">工厂</th>
                            <th data-field="mesDriver.mesProductline.linename">产线</th>
                            <th data-field="mesDriver.name">设备名称</th>
                            <th data-field="energytype">能耗类型</th>
                            <th data-field="value">耗量</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <br>
            <div style="width:auto;height: 750px;">
                <div id="main4_1" style="height:300px;width:930px;margin-left:auto;margin-right:auto;"></div>
                <div id="main4_1_5" style="height:300px;width:930px;margin-left:auto;margin-right:auto;"></div>
            </div>
      </div>
      
      <!-- 选择设备运行时间 -->
      <div id="driverRunTrime" style="height:380px;">
            <div id="main6" class="main6" style="height:350px;width:850px;margin-left:auto;margin-right:auto;margin-top: 20px;" ></div>
      </div>
<!-- Modal -->
<script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document" style="width:1250px;height:500px;">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="fa fa-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body unitBox" style="padding:0 0 0 0;">
        </div>
      </div>
    </div>
  </div>
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    energyAllDrivers();
});
//alert($("#main4").parents(".tab_container").width());
 /* $("#main4_1").width($("#main4").parents(".tab_container").width()-15); */
/* if($("#main4").parents(".tab_container").width()>600){
    $("#main4").width(600); 
}
else{
  $("#main4").width($("#main4").parents(".tab_container").width()-15);  
} */
//$("#main4").width($("#main4").parents(".tab_container").width()-15);
/* $("#EnergyType").change(getDataAndShowPieChart_4); */

$("#EnergyType").bind("change",function(){
    if($("#EnergyType").val()!="runtime"){
        //$("#exportExcel").show();
        $("#exportExcel").hide();
    }else{
        $("#exportExcel").hide();
    }
});

$("#exportExcel").bind("click",function(){
    if($.table.getCurrentPageData().length == 0){
          swal("请先查询数据");
          return;
    }
    judgeDate();
    swal({
        title: "确定导出数据？",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: '#DD6B55',
        confirmButtonText: '确认',
        cancelButtonText: "取消",
        closeOnConfirm: true
      },
      function(isConfirm){
        if (isConfirm){
          $.blockUI({
                baseZ:99999,
                message:"<h1>请稍等......</h1>",
                onBlock:function(){
                    $.ajax({
                        url:"${contextPath}/stats/exportEnergyExcel",
                        dataType:"JSON",
                        type:"POST",
                        data:"energyType="+$("#EnergyType").val()+"&driverId="+
                        $("#ChooseDriver").val()+"&startDate="+startDate+"&endDate="+endDate+"&typeScope="+typeScope+"&id="+id,
                        success:function(data){
                            if(data.success){
                                var url="${contextPath }/statistics/downloadExport/"+data.success;
                                document.getElementById("ifile").src=url;
                            }
                            $.unblockUI();
                        }
                    }); 
                }
              }); 
        }
      });
    
});


$("#search2").bind("click",function(){
    var tempType = $("#EnergyType").val();
    var tempDriver = $("#ChooseDriver").val();
    //判断能耗类型为耗电，耗水，或者耗气
    if(tempType!="runtime"){ 
        if(tempDriver!=""){ //如果选择的是单个设备
            energyConcreteDrivers();
        }else{  //所有设备
            energyAllDrivers();     
        }
    }else{
        //能耗类型为运行时间
        runtimeDrivers();
    }
    
});
 
 //范围：能耗，设备：所有
 function energyAllDrivers(){
     $("#concreteDriver").hide();
     $("#driverRunTrime").hide();
     //$("#allDriver").show();
     chooseCompanyRange();
     judgeDate();
     //初始化显示 全部设备的  表格和柱状图
     $("#driverEnergyTable").bootstrapTable('destroy');
     $("#driverEnergyTable").attr("data-url","${contextPath}/stats/listAlldriverenergy?energyType="+$("#EnergyType").val()
             +"&typeScope="+typeScope+"&id="+id+"&startDate="+startDate+"&endDate="+endDate);
     $.table.init("driverEnergyTable",{toolbar : '#toolBar1'});
     getDataAndShowPieChart_4();
 }
 
 //范围：能耗，设备：具体某一个
 function energyConcreteDrivers(){
     $("#driverRunTrime").hide();
     $("#allDriver").hide();
     $("#concreteDriver").show();
     judgeDate();
     $("#concreteDriverTable").bootstrapTable('destroy');
     $("#concreteDriverTable").attr("data-url","${contextPath}/stats/searchDriverenergy?"
             +"energyType="+$("#EnergyType").val()+"&driverid="+$("#ChooseDriver").val()
             +"&startDate="+startDate+"&endDate="+endDate);
     $.table.init("concreteDriverTable",{toolbar : '#toolBar1'});
     getDataAndShowPieChart_4_1();
 }
 
 //范围：运行时间，设备：具体某一个/单个
 function runtimeDrivers(){
     $("#concreteDriver").hide();
     $("#allDriver").hide();
     $("#driverRunTrime").show();
     getDataAndShowPieChart_6();
 }

// 基于准备好的dom，初始化echarts实例
var myChart_4 = echarts.init(document.getElementById('main4'));
var myChart_4_1 = echarts.init(document.getElementById('main4_1'));
var myChart_4_1_5 = echarts.init(document.getElementById('main4_1_5'));
var myChart_6 = echarts.init(document.getElementById('main6'));
//从后台获取数据

      //------------所有设备的 能耗柱状折线混合图
      function getDataAndShowPieChart_4(data) {
          chooseCompanyRange();
          judgeDate();
          $.ajax({
              type : "GET",
              url : "${contextPath }/stats/energyBarChart",
              data : "startDate="+startDate+"&endDate="+endDate+"&typeScope="+typeScope+"&energyType="+$("#EnergyType").val()+"&id="+id+"&dateType="+dateType+"",
              dataType : "json", 
              success : function(data) {
                  showPieChart_4(data);
              },
              error:function(jqXHR, textStatus, errorThrown){
              console.log(textStatus);
          }
          });
      }
      function showPieChart_4(data) {
          //给能耗柱状图加上折线
          var tempSeries = data.SERIES;
          var tempLegend = data.LEGEND;
          tempLegend.data.push('能耗柱');
          tempLegend.data.push('能耗折线');
          var seriesName = {"name":"能耗柱"};
          tempSeries[0] = $.extend({},tempSeries[0],seriesName);
          var tempData = {"name":"能耗折线","type":"line",smooth:true,"data": tempSeries[0].data};
          tempSeries.push(tempData);
          // 指定图表的配置项和数据
          var option_4 = {
                color: ['#7ac8f9'],      
                   legend: tempLegend,
                   calculable: data.calculable,
                   toolbox: data.TOOLBOX,
                   tooltip: {"trigger": 'axis'},
                   xAxis: data.xAxis,
                   yAxis: data.yAxis,
                   series: tempSeries
               };
           myChart_4.clear(); 
           myChart_4.setOption(option_4); 
      }
      
      //------------单个设备的能耗柱状图
      function getDataAndShowPieChart_4_1(data) {
          EnergyType = $("#EnergyType").val();
            chooseCompanyRange();
            judgeDate();
            $.ajax({
                type : "GET",
                url : "${contextPath }/stats/energyBarChartForTotal",
                data : "startDate="+startDate+"&endDate="+endDate+"&energyType="+EnergyType+"&driverId="+$("#ChooseDriver").val()+"&dateType="+dateType+"",
                dataType : "json", 
                success : function(data) {
                    showPieChart_4_1(data);
                    showPieChart_4_1_5(data);
                },
              error:function(jqXHR, textStatus, errorThrown){
                  console.log(textStatus);
              } 
            });
        } 
        function showPieChart_4_1(data) {
            // 指定图表的配置项和数据
            data.LEGEND.data.push(data.SERIES[0].name);
            var tempSeries = data.SERIES;
            var $barWidth = {barWidth:35};
            tempSeries[0] = $.extend({},tempSeries[0],$barWidth);
            
            var option_4_1 = {
                    color: ['#7ac8f9'],
                     legend: data.LEGEND,
                     calculable: data.calculable, 
                     toolbox: data.TOOLBOX,
                     tooltip: data.TOOLTIP,
                     xAxis:data.xAxis, 
                     yAxis: data.yAxis,
                     series:tempSeries
                 };
            console.log(JSON.stringify(option_4_1));
            myChart_4_1.clear();
            myChart_4_1.setOption(option_4_1);
        }
       //------------单个设备的能耗折线图
       function showPieChart_4_1_5(data) {
           // 指定图表的配置项和数据
           data.LEGEND.data.push(data.SERIES[0].name);
           var tempSeries = data.SERIES;
           $.each(tempSeries,function(index,obj){
              var $smooth = {smooth:true,type:'line'};
              tempSeries[index] = $.extend({},obj,$smooth);
           });
           var option_4_1_5 = {
                    legend: data.LEGEND,
                    calculable: data.calculable, 
                    toolbox: data.TOOLBOX,
                    tooltip: {"trigger": 'axis'},
                    xAxis: data.xAxis,
                    yAxis: data.yAxis,
                    series:tempSeries
                };
           myChart_4_1_5.clear();
           myChart_4_1_5.setOption(option_4_1_5);
       }

     //------------设备的运行时间柱状图
     function getDataAndShowPieChart_6(data) {
           EnergyType = $("#EnergyType").val();
           chooseCompanyRange();
           judgeDate();
           //限制时间范围，时间不可超过2个月
           var startTime = Date.parse(startDate)/1000; //秒
           var endTime = Date.parse(endDate)/1000;
           if(endTime-startTime>5357000){
                swal("运行时间数据量过大，时间范围不要超过两个月哦！");
                return false;
           } 
           //选择单个设备的扩展
           var ifIsDriver = "";
           if($("#ChooseDriver").val()!=""){
               ifIsDriver = "driver";
               id = $("#ChooseDriver").val();
           }
           $.blockUI({
                baseZ:99999,
                message:"<h1>加载数据中,耗时可能较长,请稍等...</h1>",
           })
           $.ajax({
               type : "GET",
               url : "${contextPath }/stats/driverRuntimeBarChart",
               data : "startDate="+startDate+"&endDate="+endDate+"&typeScope="+typeScope+"&id="+id
                    +"&dateType="+dateType+"&ifIsDriver="+ifIsDriver,
               dataType : "json", 
               success : function(data) {
                   $.unblockUI();
                   showPieChart_6(data);
               },
              error:function(jqXHR, textStatus, errorThrown){
              console.log(textStatus);
          } 
           });
       } 
       function showPieChart_6(data) {
           var tempSeries = data.SERIES;
           for(var i=0;i<tempSeries.length;i++){
               delete tempSeries[i].barWidth;
           }
           // 指定图表的配置项和数据
           var option_6 = {
                    legend: data.LEGEND,
                    calculable: data.calculable, 
                    toolbox: data.TOOLBOX,
                    tooltip: data.TOOLTIP,
                    xAxis:data.xAxis, 
                    yAxis:  {type: 'value',name: '单位：小时'},
                    series:tempSeries
                };
           myChart_6.clear();
           myChart_6.setOption(option_6);
       }
      
       $("#chooseFactory").change(function(eventt){
              $("#chooseProductLine").empty();
              $("#chooseProductLine").append("<option value=''>全部</option>");
              $("#chooseProductLine").trigger("chosen:updated");
            if($("#chooseFactory").val()!=""){
                ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#chooseFactory").val(),paintProductLine);
            }
          });
         
        function paintProductLine(data){
              $.each(data,function(idx,item){
                $("#chooseProductLine").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
              });
                $("#chooseProductLine").trigger("chosen:updated");
            };
      
      //工厂和设备选择框的级联
      $("#chooseProductLine").change(function(event){
            $("#ChooseDriver").empty();
            $("#ChooseDriver").append("<option value=''>全部</option>");
            $("#ChooseDriver").trigger("chosen:updated");
          if($("#chooseProductLine").val()!=""){
              ajaxTodo("${contextPath}/productline/DriverData2/"+$("#chooseProductLine").val(),paintDrivers);
          }
      });
      function paintDrivers(data){
        $.each(data,function(idx,item){
          $("#ChooseDriver").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
        });
          $("#ChooseDriver").trigger("chosen:updated");
      };
        
    </script>
         </div>
      </div>
      </div>
      </div>
   </div>
   </div>
<c:set var="ParentTitle" value="Count"/>
<c:set var="ModuleTitle" value="countChart"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script src="${contextPath}/js/content.js"></script>
<!-- </div> -->
<script type="text/javascript">
$(document).ready(function(){
    $("select").chosen({search_contains:true});
    $("#tab2 .chosen-container-single").css("width","135px");
    $("#tab3 .chosen-container-single").css("width","155px");
});

  var startDate;
  var endDate;
  var id;
  var dateType;
  var typeScope;
  var EnergyType;
  var modelnum;
  
function chooseCompanyRange(){
    if($("#chooseFactory").val()==""){
        id=$("#chooseCompany").val();
        typeScope="company";
    }else if($("#chooseProductLine").val()==""){
        id=$("#chooseFactory").val();
        typeScope="factory";
    }else{
        id=$("#chooseProductLine").val();
        typeScope="productline";
    }
}
  
  $(document).ready(function($) {
     $(".rangetime").hide();  
     $("#search_time").change(function(e){   
     if($("#search_time").val()=="define_time"){
        $(".rangetime").show();   
         }else{
            $(".rangetime").hide();  
         }
    
      });

    $('.form_datetime1').datetimepicker({
    language:  'zh-CN',
       format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
     showMeridian: 1,
        
 });
 
   $('.form_datetime2').datetimepicker({
    language:  'zh-CN',
       format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
     showMeridian: 1,
        
 });
    
    $(".tab_content").hide();
    $("ul.tabs li:first").addClass("active").show();
    $(".tab_content:first").show();
    $("ul.tabs li").click(function() {  
        $("ul.tabs li").removeClass("active");
        $(this).addClass("active");
        $(".tab_content").hide();
        var activeTab = $(this).find("a").attr("href");
        $(activeTab).show();
        return false;  
    });  
    // 产品图标展示
    $("#searchForEcharts").click(function(){
        judgeDate();
        var op = {};
        op.title = "统计分析";
        op.url = "${contextPath}/stats/productAnalyse";
        //op.url = "${contextPath}/statistics/toPropertyHistoryTrend";
        var dataType = 
        /*op.data = {page:JSON.stringify($("#searchForm").serializeObject()),
                productLineId:$("#productLineId").val(),
                productId:$("#productId").val(),
                productProcedureId:$("#productProcedureId").val()}; */
        //op.data = {"test":"123"};
        op.data = {
            "startDate" :startDate,
            "endDate" :endDate,
            "dateType" :dateType,
            "modelnum" :$("#modelnum").val(),
            "passType" :"COUNT",
            "id" :$("#chooseCompany").val(),
            "searchKind" : "1"
        };
        // op.realized = true;
        op.resizable = true;
        /*
        // 定义是否显示最小化按钮
        op.minimizable=true;
        // 定义是否显示最大化按钮
        op.maximizable=true;
        // 定义对话框是否可调整尺寸
        op.resizable=true;
        */
        op.destroyOnClose=false;
        $.pdialog.open("productAnalyseData",op);
        // $.pdialog['_current']
        // $.pdialog.resizeDialog({style: {left: 550, top: 590}}, $.pdialog['_current'], "w");
        // $.pdialog.resizeDialog({style: {left: 550, top: 290}}, $.pdialog.getCurrent(), "n");
    });
    // 合格图标展示
    $("#searchBarForEcharts").click(function(){
        judgeDate();
        var op = {};
        op.title = "统计分析";
        op.url = "${contextPath}/stats/productAnalyse";
        //op.url = "${contextPath}/statistics/toPropertyHistoryTrend";
        var dataType = 
        /*op.data = {page:JSON.stringify($("#searchForm").serializeObject()),
                productLineId:$("#productLineId").val(),
                productId:$("#productId").val(),
                productProcedureId:$("#productProcedureId").val()}; */
        //op.data = {"test":"123"};
        op.data = {
            "startDate" :startDate,
            "endDate" :endDate,
            "dateType" :dateType,
            "modelnum" :$("#modelnum").val(),
            "passType" :"COUNT",
            "id" :$("#chooseCompany").val(),
            "searchKind" :"2"
        };
        op.destroyOnClose=true;
        $.pdialog.open("productAnalyseData",op);
    });

    // 设备图标展示
    $("#searchFosearchForEchartsForDriverrEcharts").click(function(){
        chooseCompanyRange();
        judgeDate();
        var tempType = $("#EnergyType").val();
        var tempDriver = $("#ChooseDriver").val();
        var searchKind = "0";
        //判断能耗类型为耗电，耗水，或者耗气
        if(tempType!="runtime"){ 
            if(tempDriver!=""){
              //如果选择的是单个设备
              searchKind = "0";
            }else{
              //所有设备
              searchKind = "1"; 
            }
        }
        var op = {};
        op.title = "设备图标统计分析";
        op.url = "${contextPath}/stats/productAnalyseForDriver";
        var dataType = 
        op.data = {
            "startDate" :startDate,
            "endDate" :endDate,
            "energyType":$("#EnergyType").val(),
            "typeScope":typeScope,
            "searchKind":searchKind,
            "dateType" :dateType,
            "driverid":$("#ChooseDriver").val(),
            "id" :id
        };
        op.destroyOnClose=true;
        $.pdialog.open("productAnalyseForDriver",op);
    });

  });
 
  function getDate(){
//今天时间：
  var year=new Date().getFullYear();
  var month=new Date().getMonth()+1;
  var day=new Date().getDate();
  }
//判断开始时间和结束时间
function judgeDate(){
  var myDate = new Date();
  //获取当前年
  var year=myDate.getFullYear();
  //获取当前月
  var month=myDate.getMonth()+1;
  //获取当前日
  var date=myDate.getDate(); 
  var h=myDate.getHours();       //获取当前小时数(0-23)
  var m=myDate.getMinutes();     //获取当前分钟数(0-59)
  var s=myDate.getSeconds();  
  var now=year+'-'+p(month)+"-"+p(date)+" "+p(h)+':'+p(m)+":"+p(s);

  switch ($("#search_time").val()){
  case "1":
    startDate = year+'-'+p(month)+"-"+p(date)+" "+"00:00:00";
    endDate = year+'-'+p(month)+"-"+p(date)+" "+"23:59:59";
    dateType="day";
    break;
  case "2":
    var M = new Date(dateRangeUtil.getCurrentWeek()[0]);
    var S = new Date(dateRangeUtil.getCurrentWeek()[1]);
    startDate = M.getFullYear() + '-' + (M.getMonth() + 1) + '-' + M.getDate() + ' 00:00:00';
    endDate = S.getFullYear() + '-' + (S.getMonth() + 1) + '-' + S.getDate() + ' 23:59:59';
    dateType="week";
    break;
  case "3":
    startDate = year+'-'+p(month)+"-"+01+" "+"00:00:00";
    if(p(month)=="1"||p(month)=="3"||p(month)=="5"||p(month)=="7"||p(month)=="8"||p(month)=="10"||p(month)=="12"){
        endDate = year+'-'+p(month)+"-"+"31"+" "+"23:59:59";
    }else if(p(month)=="4"||p(month)=="6"||p(month)=="9"||p(month)=="11"){
        endDate = year+'-'+p(month)+"-"+"30"+" "+"23:59:59";
    }else{
        if((year%4==0 && year%100!=0)||(year%100==0 && year%400==0)){
            endDate = year+'-'+p(month)+"-"+"28"+" "+"23:59:59";
        }else{
            endDate = year+'-'+p(month)+"-"+"29"+" "+"23:59:59";
            }
    }
    dateType="month";
    break;
  case "4":
        startDate = year+'-'+"01"+"-"+p(date)+" "+"00:00:00";
        endDate = year+'-'+"12"+"-"+p(date)+" "+"23:59:59";
        //当选择时间范围为本年时，设置时间范围为自定义(后台没有关于年的封装，所以归类到自定义时间)
        dateType="defineDate";
        break;
  case "define_time":
    startDate = $("#begin").val();
    endDate = $("#end").val();
    dateType="defineDate";
    break;
  }
};
function p(s) {
  return s < 10 ? '0' + s: s;
};

</script>
</body>
</html>