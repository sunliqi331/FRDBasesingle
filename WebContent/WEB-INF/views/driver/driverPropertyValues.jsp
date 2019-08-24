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
                      <option value="2">一星期</option>
                      <option value="3">当月</option>
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
                  <!-- <option value="">全部</option> -->
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择设备</label>
                 <select id="chooseMesDriver" name="chooseMesDriver" data-placeholder="请选择设备" class="form-control searchtext">
                  <!-- <option value="">全部</option> -->
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择属性</label>
                 <select id="chooseMesDrivertypeProperty" data-placeholder="请选择属性" name="chooseMesDrivertypeProperty" multiple class="form-control searchtext">
                  <!-- <option value="">全部</option> -->
                  </select> 
              </div>
              
               <button id="search" type="button" class="btn btn-info  btn-search1" style="margin-left: 0">搜索</button> 
          </form> 
          </div>
        </div>
          <!-- <div class="tab_container chart_tab"> 
           <div class="tab_overx echartsBox" >
           <div class="row echartsRow">
             <div id="main1" class="col-sm-12" style="height:340px; margin-left:auto;margin-right:auto; margin-top: 20px" ></div>
           </div> 
    		</div> 
    	</div> -->
    	<hr>
    	<div id="drivertable">
    		<table class="table table-striped" id="driverPro" data-field="driverPropertys" data-url="#">
		        <thead>
		             <tr>
		             	<!-- <th data-field="Number" width="22" data-align="center">序号</th>
		             	<th data-field="driverName" width="100">设备名称</th>
		             	<th data-field="time" width="100">时间</th> -->
		             </tr>
           		</thead>
             </table>
    	</div>
    	
      </div>
      </div>
   </div>
   </div>
<c:set var="ParentTitle" value="Driver"/>
<c:set var="ModuleTitle" value="driverPropertyValues"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#drivertable").hide();
    $("select").chosen({search_contains:true});
    
    $("#search").click(function (){
    	if($("#chooseFactory").val() == 0 || $("#chooseProductLine").val() == 0 || $("#chooseMesDriver").val() == 0 || $("#chooseMesDrivertypeProperty").val() == null){
    		swal("请选择筛选条件");
            return false;
        }
    	judgeDate();
    	if(Date.parse(startDate)>=Date.parse(endDate)){
    		swal("时间选择错误。");
    		return ;
    	}
    	driverPropertyId = $("#chooseMesDrivertypeProperty").val();
    	$("#driverPro").bootstrapTable('destroy');
   		$.ajax({
   	        type : "POST",
   	        async:false, 
   	        url : "${contextPath }/stats/getDriverPropertyList",
   	        data: "driverPropertyIds="+driverPropertyId.join(","),
   	        dataType : "json", 
   	        success : function(data) {
   	        	$("table thead tr").empty();
   	        	$("table thead tr").append("<th data-field='Number' width='22' data-align='center'>序号</th>");
   	        	$("table thead tr").append("<th data-field='driverName' width='100'>设备名称</th>");
   	        	$("table thead tr").append("<th data-field='time' width='100'>时间</th>");
   	        	$.each(data,function(index,obj){
   	        		$("table thead tr").append("<th data-field='d"+obj.id+"' width='100'>"+obj.propertyname+"</th>");
   	        	});
   	        },
   			error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
   	    });
  		$("#driverPro").attr("data-url","${contextPath }/stats/driverPropertyValuesHistory?"
  				+ "factoryId=" + $("#chooseFactory").val()
                + "&productLineId=" + $("#chooseProductLine").val()
  				+"&start="+startDate+"&end="+endDate+"&driverId="+$("#chooseMesDriver").val()
  	            +"&driverPropertyIds="+driverPropertyId.join(",")+"");
  	    $.table.init("driverPro", {});
  	    $("#drivertable").show();
        //getDataAndShowPieChart();
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
    $("#chooseMesDrivertypeProperty").change(function(){
    	if($(this).val() == ''){
    		var ids = new Array();
    		$.each($("#chooseMesDrivertypeProperty").find("option"),function(idx,item){
    			var val = $(item).attr("value");	
    			if(val != ''){
    				ids.push(val);
    			}
    		});
    		$("#chooseMesDrivertypeProperty").val(ids);
    		$("#chooseMesDrivertypeProperty").trigger("chosen:updated");
    	}
    });
    $("#chooseMesDriver").change(function(event){
          $("#chooseMesDrivertypeProperty").empty();
          $("#chooseMesDrivertypeProperty").append("<option value=''></option>");
          $("#chooseMesDrivertypeProperty").trigger("chosen:updated");
        if($("#chooseMesDriver").val()!=""){
            ajaxTodo("${contextPath}/driver/MesDrivertypePropertyData/"+$("#chooseMesDriver").val(),function(data){
            	$.each(data,function(idx,item){
            	      $("#chooseMesDrivertypeProperty").append("<option value='"+ item.id +"'>"+ item.propertyname +"</option>");
            	    });
            	      $("#chooseMesDrivertypeProperty").trigger("chosen:updated");
            });
        }
      });
    
    /* function getDataAndShowPieChart(data) {
        $.ajax({
            type : "GET",
            url : "${contextPath }/stats/driverPropertyValuesForLineChart",
            data : "start="+startDate+"&end="+endDate+"&driverPropertyIds="+driverPropertyId.join(",")+"",
            dataType : "json", 
            success : function(data) {
            	$(".echartsBox").empty();
            	var width = 0;
            	if(data.length > 1){
    	        	var width = Math.floor($(".echartsBox").width() / 2 -30);
            	}else{
            		var width = Math.floor($(".echartsBox").width());
            	}
            	$.each(data,function(idx,item){
            		//echarts.dispose($("#main"+(idx+1)));
            		if((idx+1) % 2 != 0){
            			var echartsRow = $('<div class="row"></div>')
            			$(".echartsBox").append(echartsRow);
            		}
            		var echartsDiv = $('<div class="col-sm-6 charts"><div id="main'+ (idx+1) +'" style="width: '+ width +'px; height: 250px;"></div></div>');
            		$(".echartsBox").find(".row:last").append(echartsDiv);
    	            showPieChart(item,(idx+1));
            	});
            },
    		  error:function(jqXHR, textStatus, errorThrown){
    			  console.log(textStatus);
    			  swal("出错了:"+jqXHR.status+"-"+textStatus);
    		  }
        });
    } */

    /* function showPieChart(data,id) {
        // 指定图表的配置项和数据
    var main = echarts.init(document.getElementById('main'+id));
        option = {
        	    title: data.title,
        	    tooltip: data.tooltip,
        	    legend: data.legend,
        	    grid: data.grid,
        	    toolbox: data.toolbox,
        	    xAxis: data.xAxis,
        	    yAxis: data.yAxis,
        	    series: data.series
        	}; 
       
    	//$("#main"+id).remove();
       // myChart.clear();
        main.setOption(option);
    } */
});
  var startDate;
  var endDate;
  var driverPropertyId;
  
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
