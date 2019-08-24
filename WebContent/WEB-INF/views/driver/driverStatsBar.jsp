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
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<script src="${contextPath}/js/dateRangeUtil.js"></script>
</head>

<body>
  <div id="container">
      <%@ include file="/WEB-INF/views/include.header.jsp"%>
      <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
       <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li><i class="fa fa-puzzle-piece"></i> 统计</li>
      </ol>
      <div class="main-wrap" >
        <div class="main-body">          
          <div class="searchBar search_driver" style="margin-bottom:15px">
            <div class="search_header">
            <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
              <i class="fa fa-area-chart"></i> 统计
            </div>
            <div class="ishidden" >
           <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">时间选择</label>
                 <select name="search_time" id="search_time" class="form-control searchtext">
                      <option value="1">当天</option>
                      <option value="2">本周</option>
                      <option value="3">本月</option>
                      <option value="define_time">自定义时间段</option>
                 </select> 
              </div>
                <div class="form-group rangetime">
                    <label for="inputText" class="searchtitle">开始时间</label>
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
                <div class="form-group rangetime">
                  <label for="inputText" class="searchtitle">结束时间</label>
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
               
               <input type="hidden" id="chooseCompany" value="${company.id}" />
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择工厂</label>
                 <select id="chooseFactory" name="chooseFactory" data-placeholder="请选择工厂" class="form-control searchtext">
                  <option value=""></option>
                  <c:forEach var="p" items="${companyinfos}">
                  <option value="${p.id }">${p.companyname}</option>
                  </c:forEach>
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择产线</label>
                 <select id="chooseProductLine" name="chooseProductLine" data-placeholder="请选择产线" class="form-control searchtext">
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择设备</label>
                 <select id="chooseMesDriver" name="chooseMesDriver" data-placeholder="请选择设备" class="form-control searchtext">
                  </select> 
              </div>
               <button id="search" type="button" class="btn btn-info  btn-search1" style="margin-left: 0">搜索</button> 
          </form> 
          </div>
        </div>
          <div class="tab_container chart_tab"> 
          <div style="margin-bottom: 10px;background: #eceff3;padding: 4px;height: 40px;color: #637792"> 
          		<div style="margin-top: 5px;"><label for="inputText" class="searchtitle">总产量 :</label> <span id="totalNum">0</span></div>
   	      </div>
           <div class="tab_overx" >
           <div id="driverOutputTable" style="height:340px; margin-left:auto;margin-right:auto;margin-top:20px">
           	<h4 id="driverCount" align="center">产量统计</h4>
             <table class="table table-striped" id="driverTable" data-field="driverOutputs" data-url="#">
		        <thead>
		             <tr>
		               <th data-field="Number" width="22" data-align="center">序号</th>
		               <th data-field="mesDriverName" width="100">设备名称</th>
		               <th data-field="datetime" width="100">日期</th>
		               <th data-field="driver_count" width="100">产量</th>
		             </tr>
           		</thead>
             </table>
             </div>
    </div>
    </div>
      </div>
      </div>
   </div>
   </div>
   
<c:set var="ParentTitle" value="Driver"/>
<c:set var="ModuleTitle" value="drivarStats"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript">
var dateType;
$(document).ready(function(){
	$.table.init("driverTable",{});
    $("select").chosen({search_contains:true});
    //var myChart = echarts.init(document.getElementById('main1'));
    $("#search").click(function (){
    	if($("#chooseMesDriver").val()==null || $("#chooseMesDriver").val() ==''){
            swal("请选择筛选条件");
            return;
        }
    	judgeDate();
    	//var mil = (1000 * 60 * 60 * 24) + 5000; //一天的毫秒数
	  	var tTemp = Date.parse(endDate)-Date.parse(startDate);//所选时间段毫秒数
	  	if(tTemp<=0){ //结束时间不能小于开始时间
	  		swal("时间格式错误");
	  		return;
	  	}
	  	$("#driverTable").bootstrapTable('destroy');
	  	$("#driverTable").attr("data-url","${contextPath }/stats/driverOutputByOne?"+"startDate="+startDate
	  			+"&chooseFactory=" + $("#chooseFactory").val()
                +"&chooseProductLine=" + $("#chooseProductLine").val()
      			+"&endDate="+endDate
      			+"&driverId="+$("#chooseMesDriver").val()
      			+"&dateType="+dateType);

	  	var CountCallBack = function(obj){
	  		$("#totalNum").html(obj.total);
	  	}
	  	$.table.init("driverTable", {},CountCallBack);
	  	
	    
	  	/* var sDate = startDate.split(" ")[0];
	  	var eDate = endDate.split(" ")[0];
	  	if((tTemp<mil)&&(sDate==eDate)){ //如果时间范围是一天之内，显示每个小时的产量
	  		$("#driverCount").html("时产量统计");
	  		$("#driverTable").bootstrapTable('destroy');
	  		$("th[data-field='count']").html("每小时产量");
	  		$("#driverTable").attr("data-url","${contextPath }/stats/driverOutputOfHour?"+"startDate="+startDate
	      			+"&endDate="+endDate+"&driverId="+$("#chooseMesDriver").val());
	  		//var op = $.extend({},$.table._op,[]);
	  	    $.table.init("driverTable", {});
	  	}else{  //如果时间范围大于一天，就以天为单位来显示，显示每天的总产量
	  		$("#driverCount").html("日产量统计");
	  		$("#driverTable").bootstrapTable('destroy');
	  		$.table.setCurrent("driverTable");
	  		$("th[data-field='count']").html("每天产量");
	  		$("#driverTable").attr("data-url","${contextPath }/stats/driverOutputOfDay?"+"startDate="+startDate
	      			+"&endDate="+endDate+"&driverId="+$("#chooseMesDriver").val());
	  		var orignalColumns = [];
	  		 $.each($.table._op.columns,function(idx,item){
	  			orignalColumns.push(item);
	  		 });
	  		var _columns = orignalColumns.push({field: 'updatetime',
	            formatter: function (value, row, index) {
	            	row.updatetime = value.split(" ")[0];
	                return row.mesDriver.name;
	            }
	        });
	  		$.table.init("driverTable", {columns:orignalColumns}); 
	  	} */
    });

    $("#chooseFactory").change(function(event){
          $("#chooseProductLine").empty();
         $("#chooseProductLine").append("<option value=''></option>");
          $("#chooseProductLine").trigger("chosen:updated");
        if($("#chooseFactory").val()!=""){
            ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#chooseFactory").val(),function(data){
            	$.each(data,function(idx,item){
            	      $("#chooseProductLine").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
            	    });
            	      $("#chooseProductLine").trigger("chosen:updated");
            });
        }
      });
    $("#chooseFactory").trigger('change');
    $("#chooseProductLine").change(function(event){
          $("#chooseMesDriver").empty();
          $("#chooseMesDriver").append("<option value=''></option>");
          $("#chooseMesDriver").trigger("chosen:updated");
        if($("#chooseProductLine").val()!=""){
            ajaxTodo("${contextPath}/productline/MesDriverData2/"+$("#chooseFactory").val()+"/"+$("#chooseProductLine").val(),function(data){
            	$.each(data,function(idx,item){
            	      $("#chooseMesDriver").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
            	    });
            	      $("#chooseMesDriver").trigger("chosen:updated");
            });
        }
      });
    
    
    //从后台获取数据
    /* function getDataAndShowPieChart(data) {
        judgeDate();
        //driverPropertyId = "254";
        $.ajax({
            type : "GET",
            url : "${contextPath }/stats/driverStatsBarChartForTotal",
            data : "startDate="+startDate+"&endDate="+endDate+"&driverId="+driverId+"&productLineId="+productLineId,
            dataType : "json", 
            success : function(data) {
                showPieChart(data);
            },
    		  error:function(jqXHR, textStatus, errorThrown){
    			  console.log(textStatus);
    			  swal("出错了:"+jqXHR.status+"-"+textStatus);
    		  }
        });
    } */

/*     function showPieChart(data) {
        // 指定图表的配置项和数据

        option = {
        		color: ['#7ac8f9'],
                title: {
                    text: data.TITLE.TEXT
                },
                legend: data.LEGEND,
                calculable: data.calculable,
                toolbox: data.TOOLBOX,
                tooltip: data.TOOLTIP,
                xAxis: data.xAxis,
                yAxis: data.yAxis,
                series: data.SERIES,
        	}; 
        myChart.clear();
        myChart.setOption(option);
    } */
});
  var startDate;
  var endDate;
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
