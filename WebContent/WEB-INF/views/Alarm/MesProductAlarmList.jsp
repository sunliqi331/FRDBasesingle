<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>产品数据告警</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="${contextPath }/styles/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
      <li>产品数据告警</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
        <div class="searchBar search_driver" style="margin-bottom:0">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 产品数据告警查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/MesProductAlarm/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">开始时间</label>
                <div class="controls input-append date form_datetime" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                  <input class="form-control datetime" type="text" style="background: #fff; border-color: #c8ced6; width:100%" value="" readonly> 
                  <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" value="" name="search_GTE_updatetime" />
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">结束时间</label>
                <div class="controls input-append date form_datetime2" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
                  <input class="form-control datetime" type="text" style="background: #fff; border-color: #c8ced6; width:100%" value="" readonly> 
                   <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input2" value="" name="search_LTE_updatetime" />
              </div>
                
              <div class="form-group">
                <label for="inputText" class="searchtitle">产品工厂</label>
                <select id="chooseCompanyName" class="form-control validate[required] required searchtext"  name="chooseCompanyName" > 
                	<option value="">全部</option>
                	<c:forEach items="${companyinfos}" var="p">
                	<option value="${p.id}">${p.companyname}</option>
                	</c:forEach> 	
                </select>
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">产品产线</label>
                <select id="productLineName" name="productLineName"   class="form-control validate[required] required searchtext">
                 <%--  <<c:forEach items="${productLineSelect }" var="map">
                   <optgroup label="${map.key }">
                    <c:forEach items="${map.value }" var="productLine">
                      <option value="${productLine.id }">${productLine.linename }</option>
                    </c:forEach>
                    </optgroup>
                  </c:forEach>  --%>
                </select>
              </div>
              <div class="form-group">
                <label for="productName" class="searchtitle">产品名称</label>
                <select id="productName" class="form-control validate[required] required searchtext" name="search_EQ_mesProcedureProperty.mesProductProcedure.mesProduct.id">
               		<option value="">全部</option>
              	</select>
              </div>
              <div class="form-group">
                <label for="mesProcedure" class="searchtitle">产品工序</label>
                <select id="mesProcedure" class="form-control validate[required] required searchtext"  name="search_EQ_mesProcedureProperty.mesProductProcedure.procedurenum">
              		
              	</select>
              </div>
              <div class="form-group">
              <button type="submit"  class="btn btn-info btn-search1">搜索</button>
            </div>
          </form>
          </div>
        </div>  
        <table class="table table-striped" id="table" data-field="mesProductAlarms" data-url="${contextPath}/MesProductAlarm/data">
            <thead>
              <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
                 <th data-field="companyname" width="100">产品工厂</th>
               	<th data-field="productline">产品产线</th>
                <th data-field="productmodelnum" width="100">产品类型编号</th>
                <th data-field="productsn" width="100">产品编号</th>
                <th data-field="mesProcedureProperty.mesProductProcedure.mesProduct.name" width="50">产品名称</th>
                <th data-field="mesProcedureProperty.mesProductProcedure.procedurenum" width="100">工序名</th>
                <th data-field="mesProcedureProperty.propertyname" width="100">工序属性名</th>
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
<c:set var="ModuleTitle" value="MesProductAlarm" />
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
<script type="text/javascript">
function time(){
    var start = $('input[name="search_GTE_updatetime"]').val();
    var end = $('input[name="search_LTE_updatetime"]').val();
    start = start.substring(0,10);
    end = end.substring(0,10);
    $('input[name="search_GTE_updatetime"]').val(start.toString('yyyy-MM-dd'));
    $('input[name="search_LTE_updatetime"]').val(end.toString('yyyy-MM-dd'));
}

$(document).ready(function() {
	/*  $.table.setCurrent("table");
     $.table.refreshCurrent("${contextPath}/MesProductAlarm/data"+$("#productName").val()); */
	ajaxTodo("${contextPath}/procedureMonitor/getProductionByCompanyId/0",paintProductNames);
	//ajaxTodo("${contextPath}/productline/getProductlineByCurrentCompanyid",paintProductLineNames);
	//工厂与产线关联
	$("#chooseCompanyName").change(function(event){
          $("#productLineName").empty();
          $("#productLineName").append("<option value=''>全部</option>");
          $("#productLineName").trigger("chosen:updated");
        if($("#chooseCompanyName").val()!=""){
            ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#chooseCompanyName").val(),function(data){
            	$.each(data,function(idx,item){
            	      $("#productLineName").append("<option value='"+ item.linename +"'>"+ item.linename +"</option>");
            	    });
            	      $("#productLineName").trigger("chosen:updated");
            });
        }
      });
	
//产品与产品工序关联 	
    $("#productName").change(function(event){
        $("#mesProcedure").empty();
        $("#mesProcedure").append("<option value=''>全部</option>");
        $("#mesProcedure").trigger("chosen:updated");
      if($("#productName").val()!=""){
          ajaxTodo("${contextPath}/MesProductAlarm/getprocedureForProduct/"+$("#productName").val(),function(data){
          	$.each(data,function(idx,item){
          	      $("#mesProcedure").append("<option value='"+ item.procedurenum +"'>"+ item.procedurenum +"</option>");
          	    });
          	      $("#mesProcedure").trigger("chosen:updated");
          });
      }
    }); 
    $("#productName").trigger('change');
});

function paintProductNames(data){
	  $.each(data,function(idx,item){
	    $("#productName").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
	  });
	    $("#productName").trigger("chosen:updated");
};
/* function paintProductLineNames(data){
	  $.each(data,function(idx,item){
	    $("#productLineName").append("<option value='"+ item.linename +"'>"+ item.linename +"</option>");
	  });
	    $("#productLineName").trigger("chosen:updated");
}; */

</script>

</body>
</html>
