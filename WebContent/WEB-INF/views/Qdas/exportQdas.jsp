<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<!-- <head> -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="title" content="FRD - Demo">
<title>Qdas</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<style type="text/css">
	.label{
		width: 250px;
	}
	.Qcenter{
	  	text-align: center;
	}
	@media (max-width: 767px){
	.Qcenter{
		text-align:left;
	}
  }
</style>
</head>
<script>
	$(document).ready(function(){
		//$("select").chosen({search_contains:true});
		
		$('.form_datetime1').datetimepicker({
		    language : 'zh-CN',
		    format : 'yyyy-mm-dd hh:ii:ss',
		    weekStart : 1,
		    todayBtn : 'linked',
		    autoclose : 1, 
		    todayHighlight : 1,
		    startView : 2,
		    forceParse : 0,
		    showMeridian : 1,
		  //minView: 2

		  });

		  $('.form_datetime2').datetimepicker({
		    language : 'zh-CN',
		    format : 'yyyy-mm-dd hh:ii:ss',
		    weekStart : 1,
		    todayBtn : 'linked',
		    autoclose : 1,
		    todayHighlight : 1,
		    startView : 2,
		    forceParse : 0,
		    showMeridian : 1,
		  //minView: 2

		  });
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
        <li >Qdas导出</li>
      </ol>
  <div class="main-wrap">
    <div class="main-body">
	<div class="Qcenter">
	<h4>Qdas导出</h4>
<form id="exportForm" action="${contextPath}/QdasStats/generateQdasFile" 
	class="form form-horizontal" method="post" enctype="multipart/form-data">
	<div class="pageFormContent" layoutH="58">
		<div class="row">
			<div class="form-group">
				<label for="productId" class="control-label col-sm-4">产品名称 </label> 
				 <div class="col-sm-6"><select
					id="productId" name="productId" class="form-control searchtext msa">
					<option value="">请选择产品</option>
					<c:forEach items="${products }" var="product">
						<option value="${product.id }">${product.name }</option>
					</c:forEach>
				</select></div>
			</div>
			<div class="form-group" >
                 <label for="chooseProcedure" class="control-label col-sm-4">选择工序</label>
                 <div class="col-sm-6">
                 <select id="chooseProcedure" name="chooseProcedure" class="form-control searchtext">
                  	<option value="">请选择工序</option>
                 </select> 
                  </div>
              </div>
			<div class="form-group" hidden="true">
				<input type="file" name="modelFile" id="modelFile">
			</div>
			<div class="form-group">
				<label for="model" class="control-label col-sm-4">映射文件 </label> 
				 <div class="col-sm-6">
				 	<input type="text" id="model" class="form-control" readonly="readonly">
				</div>
			</div>
			<div class="form-group rangetime ">
				<label for="inputText" class="control-label col-sm-4">开始时间</label>
				 <div class="col-sm-6">
				<div class="controls searchtext input-append date  form_datetime1"
					style="position: relative;" data-date=""
					data-date-format="yyyy-mm-dd" data-link-field="begin">
					<input class="form-control  datetime" name="begin" id="begin"
						type="text" style="background: #e5eaf1; border-color: #c8ced6;"
						value="" readonly> <span class="add-on"
						style="position: absolute; right: 5px; bottom: 0; background: none; padding: 5px 5px 5px 0; border: none;">
						<i class="fa fa-calendar" style="font-size: 16px"></i>
					</span>

					<!--  <input type="hidden" name="begin" id="begin" value="" /> -->
				</div></div>
			</div>
			<div class="form-group  rangetime ">
				<label for="inputText" class="control-label col-sm-4">结束时间</label>
				 <div class="col-sm-6">
				<div class="controls searchtext input-append date form_datetime2"
					style="position: relative;" data-date=""
					data-date-format="yyyy-mm-dd" data-link-field="end">
					<input class="form-control datetime" type="text" name="end"
						id="end" style="background: #e5eaf1; border-color: #c8ced6;"
						value="" readonly> <span class="add-on"
						style="position: absolute; right: 5px; bottom: 0; background: none; padding: 5px 5px 5px 0; border: none;">
						<i class="fa fa-calendar" style="font-size: 16px"></i>
					</span>
				</div></div>
				<!--  <input type="hidden" name="end" id="end" value="" /> -->

			</div>
			<div class="form-group">
				<label for="productId" class="control-label col-sm-4">导出格式</label> 
				 <div class="col-sm-6"><select
					id="suffix" name="suffix" data-placeholder="请选择产品"
					class="form-control searchtext msa">
						<option value="txt">txt</option>
						<option value="DFQ">DFQ</option>
					</select>
				 </div>
			</div>
		</div>
	</div>
	<!-- <div class="modal-footer" align="center"> -->
	<div align="center">
		<button id="confirm" type="button" class="btn btn-primary">确定</button>
		<!-- <button id="close" type="button" class="btn btn-default" data-dismiss="modal">关闭</button> -->
	</div>
</form>
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
    
  </div>
  </div>
  <c:set var="ParentTitle" value="Count" />
  <c:set var="ModuleTitle" value="QdasExport" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
		//产品和对应工序的级联
		$("#productId").bind("change",function(){
			$("#chooseProcedure").empty();
			$("#chooseProcedure").append("<option value=''>请选择工序</option>");
			$("#chooseProcedure").trigger("chosen:update");
			if($("#productId").val()!=""){
                ajaxTodo("${contextPath}/statistics/generateProductProcedureSelect/"+$("#productId").val(),paintProcedure);
            }
			function paintProcedure(data){
	            $.each(data,function(idx,item){
	              $("#chooseProcedure").append("<option value='"+ item.id +"'>"+ item.procedurenum +"</option>");
	            });
	              $("#chooseProcedure").trigger("chosen:updated");
	        };
			
		});
		
		//映射文件
		$("#model").bind("click",function(){
			$("#modelFile").trigger("click");
		});
		$("#modelFile").bind("change",function(){
			var pathArr = $("#modelFile").val().split("\\");
			var fileName = pathArr[pathArr.length-1]; //文件名
			var fileSuffix = fileName.split(".")[1];  //文件后缀
			//验证文件后缀是否正确
		    if(fileSuffix!="csv" || fileSuffix=="" || fileSuffix==null){
			    swal("错误","请选择正确的文件","error");
			    return;
		    }
			$("#model").val(fileName);
		});
		
		$("#confirm").click(function(){
			if($("#productId").val()=="" || $("#productId").val()==null){
				swal("warning","请选择产品","warning");
			    return;
			}
			if($("#chooseProcedure").val()=="" || $("#chooseProcedure").val()==null){
				swal("warning","请选择工序","warning");
			    return;
			}
			if($("#begin").val()=="" || $("#end").val()==""){
				swal("warning","请选择时间","warning");
			    return;
			}
		    $("#exportForm").submit();
		    $("#close").trigger("click");
		    // remove_submit();
			//var formData = new FormData($("#exportForm")[0]);
		});
		
	});
</script>

</body>
</html>
