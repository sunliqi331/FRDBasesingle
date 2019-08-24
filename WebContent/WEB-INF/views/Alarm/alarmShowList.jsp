<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>报警信息显示屏</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<script src="${contextPath}/js/dateRangeUtil.js"></script>

		<script type="text/javascript">
              function time(){
	          		judgeDate();
	          		console.log(startDate);
	          		console.log(endDate);
	          		$('input[name="search_GTE_datetime"]').val(startDate);
	          	    $('input[name="search_LTE_datetime"]').val(endDate);
          	  }
        </script>

</head>

<body>
  <div id="container">
      <%@ include file="/WEB-INF/views/include.header.jsp"%>
      <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
       <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>报警信息显示屏</li>
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
           <form class="form-inline" method="post" action="${contextPath }/MesAlarmShow/tableSearch" data-target="alarmShowTable" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">时间选择</label>
                 <select id="search_time" class="form-control searchtext">
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
                  <input type="hidden" id="begin" value="" name="search_GTE_datetime"/>
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
                  <input type="hidden" id="end" value="" name="search_LTE_datetime"/>
               </div>
               
               <input type="hidden" id="chooseCompany" value="${company.id}" />
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择工厂</label>
                 <select id="chooseFactory" name="search_EQ_mesDriver.mesProductline.companyinfo.id" 
                 		data-placeholder="请选择工厂" class="form-control searchtext">
                  <option value=""></option>
                  <c:forEach var="p" items="${companyinfos}">
                  <option value="${p.id }">${p.companyname}</option>
                  </c:forEach>
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择产线</label>
                 <select id="chooseProductLine" name="search_EQ_mesDriver.mesProductline.id" 
                 		data-placeholder="请选择产线" class="form-control searchtext">
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择设备</label>
                 <select id="chooseMesDriver" name="search_EQ_mesDriver.id" 
                 		data-placeholder="请选择设备" class="form-control searchtext">
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">报警ID &nbsp;&nbsp;</label>
                 <input type="text" class="form-control searchtext" name="search_LIKE_mesAlarmRelation.alarmCode" value="${param.search_LIKE_mesAlarmRelation.alarmCode }"/>
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">报警类型</label>
                 <select data-placeholder="请选择类型" class="form-control searchtext" 
                 		name="search_EQ_mesAlarmRelation.alarmType">
                  <option value=""></option>
                  <c:forEach var="p" items="${itemList}">
                  <option value="${p.value }">${p.value}</option>
                  </c:forEach>
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle" style="width: 68px;">状态</label>
                 <select id="chooseMesDriver" data-placeholder="请选择状态" class="form-control searchtext" 
                 		name="search_EQ_status">
                 	<option value=""></option>
                 	<option value="未确认">未确认</option>
                 	<option value="未确认">已确认</option>
                  </select>
              </div>
              
               <button type="submit" class="btn btn-info  btn-search1" style="margin-left: 0" onclick="time()">搜索</button> 
          </form> 
          </div>
        </div>
          <div class="tab_container chart_tab"> 
          
          <a id="dataConfirm" style="visibility:hidden" class="btn btn-tool" target="selectedTodo" data-target="alarmShowTable" href="${contextPath }/MesAlarmShow/statusChange"></a>
          <a class="btn btn-default1" id="statusConfirm" href="#">
              <i class="fa fa-check"></i>
              <span> 状态确认</span>
           </a>
          
           <div class="tab_overx" >
           <div style="height:340px; margin-left:auto;margin-right:auto;margin-top:20px">
           	<h4 align="center">设备告警信息</h4>
             <table class="table table-striped" id="alarmShowTable" data-field="alarmShowList" data-url="#">
		        <thead>
		             <tr>
		               <th data-field="Number" width="22" data-align="center">序号</th>
		               <th data-checkbox="true" width="22">
                 	   		<input class="cbr checkboxCtrl" type="checkbox">
                       </th>
                       <th data-field="companyName" width="100">工厂</th>
                       <th data-field="productionLineName" width="100">产线</th>
		               <th data-field="mesDriver.name" width="100">设备</th>
		               <th data-field="datetime" width="100">日期</th>
		               <th data-field="mesAlarmRelation.alarmType" width="100">类型</th>
		               <th data-field="mesAlarmRelation.alarmCode" width="100">报警ID</th>
		               <th data-field="mesAlarmRelation.info" width="100">报警信息</th>
		               <th data-field="status" width="100">状态</th>
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
   
<c:set var="ParentTitle" value="Alarm" />
<c:set var="ModuleTitle" value="alarmShow" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $("select").chosen({search_contains:true});
    
	//初始化表格，显示最新的两千条数据
	$("#alarmShowTable").attr("data-url","${contextPath }/MesAlarmShow/initTable");
	$.table.init("alarmShowTable",{});
	
	//获取所选记录的id
    function getIds(){
	    var ids = "";
	    $("#alarmShowTable").find("tr.selected").each(function(i){
	  	    var val = $(this).attr("data-uniqueid");
		    ids += i==0 ? val : ","+val;
	    });
	    return ids;
    }
	
	//删除按钮  的点击事件
    $("#statusConfirm").bind("click",function(){
	    if(getIds!=""){
		    $("#dataConfirm").attr("title","确认变更状态吗？");
		    $("#dataConfirm").trigger("click");
	    }
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
