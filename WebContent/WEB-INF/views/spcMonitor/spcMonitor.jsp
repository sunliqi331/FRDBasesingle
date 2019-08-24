<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="title" content="FRD - Demo">
<title>FRD - Demo</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/assets/html2canvas/html2canvas.js"></script>
<script src="${contextPath}/styles/assets/html2canvas/html2canvas.svg.js"></script>
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
</head>
<script type="text/javascript">
$(document).ready(function(){
	  var subRangeOption;
	  for(var i = 2;i <= 25; i++){
		  subRangeOption += "<option value="+ i +">"+ i +"个</option>"
	  }
	  $("#subRange").append(subRangeOption);
	  var subNumOption;
	  for(var i = 2;i <= 100; i++){
		  subNumOption += "<option value="+ i +">"+ i +"组</option>"
	  }
	  $("#subNum").append(subNumOption);
	  $("select").chosen({search_contains:true});
	  //通过产品ID获取该产品下的工序，并动态产生select
	  function generateProcedureSelect(productId,callback){
		  var option = "";
		  $.ajax({
			  url:"${contextPath}/statistics/generateProductProcedureSelect/"+productId,
			  dataType:"JSON",
			  type:"POST",
			  success:function(data){
					$("#productProcedureId").empty();
			  	$.each(data,function(idx,item){
			  		option += "<option value='"+ item.id +"'>"+ item.procedurename +"</option>";
			  	});
			  	$("#productProcedureId").append(option);
			  	$("#productProcedureId").trigger("chosen:updated");
			  	if(callback)
			  		callback($("#productProcedureId").val());
			  }
		  });
	  }
	  function generateProcedurePropertySelect(procedureId,callback){
		  var option = "";
		  $.ajax({
			  url:"${contextPath}/statistics/generateProcedurePropertySelect/"+procedureId,
			  dataType:"JSON",
			  type:"POST",
			  success:function(data){
				  $("#procedurePropertyId").empty();
			  	$.each(data,function(idx,item){
			  		option += "<option value='"+ item.id +"'>"+ item.propertyname +"</option>";
			  	});
			  	$("#procedurePropertyId").append(option);
			  	$("#procedurePropertyId").trigger("chosen:updated");
			  	if(callback)
			  		callback();
			  }	
		  });
		  
	  }
	  $("#productId").change(function(){
		  generateProcedureSelect($(this).val());
	  });
	 // generateProcedureSelect($("#productId").val(),generateProcedurePropertySelect);
	  $("#productProcedureId").change(function(){
		  generateProcedurePropertySelect($(this).val());
	  });
	 // generateProcedureSelect($("#product").val(),generateProcedurePropertySelect($("#procedure").val()));
});
  
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<body>
<div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp" %>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp" %>
 	 <div class="main-content removeble">
      <ol class="breadcrumb" style="background: #f1f2f7; height:41px; margin:0;line-height:40px;    border-radius: 0; display:block;">
        <li ><i class="fa fa-home"></i>
        <a href="${contextPath}/management/index"> 首页</a></li>
        <li >SPC告警设定</li>
      </ol>
  <div class="main-wrap">
    <div class="main-body">
      <div class="searchBar search_driver" style="margin-bottom: 15px;" >
       <div class="search_header">
       <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> SPC动态查询
           </div>
           <div class="ishidden" style="margin-bottom:5px" >
        <form method="post" action="${contextPath}/mesSpcMonitor/data" data-target="table"
          id="formID" class="form form-inline" onsubmit="return navTabSearch(this)">
         <ul class="searchcontent" style="margin-bottom:0px">
                <li class="form-group" style="width: 50%">
                <label for="inputText" class="searchtitle">选择规则</label> 
                <select id="selectFactory" name="search_EQ_mesSpcMonitorConfig.id" data-placeholder="请选择工厂"  class="form-control searchtext">
                     <option value="">全部</option>
                      <c:forEach var="p" items="${mesSpcMonitorConfigs }">
                        <option value="${p.id }">${p.ruleName }</option>
                      </c:forEach>
                </select>
                </li>
             
              <li class="form-group">
              <button id="search" type="submit" class="btn btn-info btn-search1">搜索</button>
        </li>
        </ul>
            <%--  --%>
        </form>
        </div>
      </div>
      <div id="toolBar clearfix ">
        <div class="btn-group"> 
         <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_mesSpcMonitor" href="${contextPath}/mesSpcMonitor/toAddMesSpcMonitor/0">
           <i class="fa fa-plus"></i>
                    新增
        </a>
         <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="edit_mesSpcMonitor" href="${contextPath}/mesSpcMonitor/toAddMesSpcMonitor/{slt_uid}">
           <i class="fa fa-pencil"></i>
                    修改
        </a>
       </div>
      </div>
       <table class="table table-striped" id="table" data-field="mesSpcMonitors" data-url="${contextPath}/mesSpcMonitor/data">
            <thead>
              <tr>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="productLineName" width="100">产线</th>
                <th data-field="mesDriverName" width="100">测量设备</th>
                <th data-field="productName" width="100">产品名称</th>
                <th data-field="productProcedureName" width="100">工序名称</th>
                <th data-field="procedurePropertyName" width="100">参数名称</th>
              </tr>
            </thead>
          </table>
    </div>
 </div>   
       
    
    
  </div>
  </div>

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
        <div class="modal-body unitBox">
        </div>
      </div>
    </div>
  </div>
</script>
<c:set var="ParentTitle" value="Count"/>
<c:set var="ModuleTitle" value="spcMonitor"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/js/canvas2image.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
</body>
</html>
