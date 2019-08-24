<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设备数据告警</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="${contextPath }/styles/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
</head>
<body scroll="no">
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
      <li>设备数据告警</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
        <div class="searchBar search_driver" style="margin-bottom:0">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 设备数据告警查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/MesDriverAlarm/data" data-target="table" onsubmit="return navTabSearch(this)">
            <ul class="searchcontent">
             <li class="form-group">
                <label for="inputText" class="searchtitle">工厂名称</label>
                <select id="company" class="form-control searchtext" data-placeholder="请选择工厂"  id="company" name="company">
                	<option value="">全部</option>
                	<c:forEach var="p" items="${company}">
                  		<option value="${p.id }">${p.companyname}</option>
                	</c:forEach>
                </select>
              </li>
               <li class="form-group">
                <label for="inputText" class="searchtitle">产线名称</label>
                <select id="productline" class="form-control searchtext" data-placeholder="请选择产线"   name="mesDriver">
                	
                </select>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">设备名称</label>
                <select id="mesDriver" class="form-control searchtext" data-placeholder="请选择设备"   name="mesDriver">
                	
                </select>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">开始时间</label>
                <div class="controls input-append date form_datetime" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                  <input class="form-control datetime" type="text" style="background: #fff;; border-color: #c8ced6; width:100%" value="" readonly> 
                  <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" value="" name="search_GTE_updatetime" />
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">结束时间</label>
                <div class="controls input-append date form_datetime2" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
                  <input class="form-control datetime" type="text" style="background: #fff; border-color: #c8ced6;width:100%" value="" readonly> 
                   <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input2" value="" name="search_LTE_updatetime" />
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">测点ID</label>
                <input id="mesPointId" type="text" class="form-control searchtext" id="inputText" name="search_EQ_mesPoints.codekey" value="${param.search_EQ_mesDriver.codekey }"/>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">测点名称</label>
                <input id="mesPoint" type="text" class="form-control searchtext" id="inputText" name="search_EQ_mesPoints.name" value="${param.search_EQ_mesDriver.name }"/>
              </li>
              <li class="form-group alarm_search">
              <button type="submit" class="btn btn-info btn-search1" onclick="time()">搜索</button>
            </li>
            </ul>
          </form>
          </div>
        </div>  
        <table class="table table-striped" id="table" data-field="mesDriverAlarms" data-url="${contextPath}/MesDriverAlarm/data">
            <thead>
              <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
                 <th data-field="mesDriver.mesProductline.companyinfo.companyname" width="100">设备工厂</th>
                 <th data-field="mesDriver.mesProductline.linename" width="100">设备产线</th>
                <th data-field="mesDriver.name" width="100">设备名称</th>
                <th data-field="mesPoints.name" width="100">测点名称</th>
                <th data-field="updatetime" width="100">时间</th>
                <th data-field="tstatecode" width="100">状态</th>
                <th data-field="errorchangevalue" width="100">超出范围</th>
              </tr>
            </thead>
          </table>
       </div>
    </div>
  </div>
</div>
<!-- Modal -->
<script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="fa fa-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body">
        </div>
      </div>
    </div>
  </div>
</script>
<c:set var="ParentTitle" value="Alarm" />
<c:set var="ModuleTitle" value="MesDriverAlarm" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>

<script>
$('.form_datetime').datetimepicker({
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    weekStart: 1,
    todayBtn:'linked',
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1,
    minView: 2
    
 });
$('.form_datetime2').datetimepicker({
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    weekStart: 1,
    todayBtn:'linked',
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1,
    minView: 2
    
 });
</script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript">
function time(){
    var start = $('input[name="search_GTE_updatetime"]').val();
    var end = $('input[name="search_LTE_updatetime"]').val();
    if(start==""||end==""){
	    $('input[name="search_GTE_updatetime"]').val("");
	    $('input[name="search_LTE_updatetime"]').val("");
    }else{
	    start = start.substring(0,10)+" 00:00:00";
	    end = end.substring(0,10)+" 23:59:59";
	    $('input[name="search_GTE_updatetime"]').val(start.toString('yyyy-MM-dd HH:mm:ss'));
	    $('input[name="search_LTE_updatetime"]').val(end.toString('yyyy-MM-dd HH:mm:ss'));
    }
}
$(document).ready(function(){
	$("#company").change(function(event){
        $("#productline").empty();
        $("#productline").append("<option value=''>全部</option>");
        $("#productline").trigger("chosen:updated");
      if($("#company").val()!=""){
          ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#company").val(),function(data){
          	$.each(data,function(idx,item){
          	      $("#productline").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
          	    });
          	      $("#productline").trigger("chosen:updated");
          });
      }
    });
	$("#productline").change(function(event){
        $("#mesDriver").empty();
        $("#mesDriver").append("<option value=''>全部</option>");
        $("#mesDriver").trigger("chosen:updated");
      if($("#productline").val()!=""){
          ajaxTodo("${contextPath}/driver/getmesDriverbyproductlineId/"+$("#productline").val(),function(data){
          	$.each(data,function(idx,item){
          	      $("#mesDriver").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
          	    });
          	      $("#mesDriver").trigger("chosen:updated");
          });
      }
    });
	$("select").chosen({search_contains:true});
	
/* 	$("#mesDriver").change(function(event){
		$("#mesPoint").empty();
		$("#mesPoint").append("<option value=''>全部</option>");
        $("#mesPoint").trigger("chosen:updated");
        if ($("#mesDriver").val()!="") {
			
		}
	}); */
});

</script>

</body>
</html>
