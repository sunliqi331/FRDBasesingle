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
<title>Qdas</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
</head>
<script type="text/javascript">
$(document).ready(function(){
	  $("select").chosen({search_contains:true});
	  $("#qdasCategory").change(function(){
		  generateQdasSelect($(this).val());
	  });
	  function generateQdasSelect(obj){
		  var option;
		  $.ajax({
			  url:'${contextPath}/qdas/generateQdasSelect',
			  data:{id:obj},
			  type:'POST',
			  dataType:'JSON',
			  success:function(data){
				    $("#id").empty();
		            $.each(data,function(idx,item){
		              option += "<option value='"+ item.id +"'>"+ item.name +"</option>";
		            });
		            $("#id").append(option);
		            $("#id").trigger("chosen:updated");
			  }
		  });
	  }
	  
	  
	  /* $("#formID").submit(function(e){
		  $.ajax({
				type:'POST',
				url: '${contextPath}/qdas/addOrUpdate',
				dataType:"json",
				data:{id:$("#id").val(),"keyfield":$("#key").val()},
				cache: false,
				success: function(data){
					if(data.success == 'success'){
						$.table.refreshCurrent($.table.getCurrent().attr("data-url"));
					}
				},
				error: DWZ.ajaxError
			});
				
				return false;
		 }); */
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
        <li >Qdas配置</li>
      </ol>
  <div class="main-wrap">
    <div class="main-body">
      <div class="searchBar search_driver" style="margin-bottom: 10px;" >
       <div class="search_header">
             <i class="fa fa-search"></i> Qdas变量配置
           </div>
        <form method="post" id="formID" class="form form-inline" action="${contextPath}/qdas/data"
        onsubmit="return navTabSearch(this)">
        	<div class="form-group">
              <label for="id" class="searchtitle">参数类型
              </label>
                <select name="search_EQ_mesqdascategory.id" id="qdasCategory" data-placeholder="请选择参数"
                  class="form-control searchtext">
                  <option value="">全部</option>
                  <c:forEach items="${qdasCategoryList }" var="qdasCategory">
                  	<option value="${qdasCategory.id }">${qdasCategory.name }</option> 
                  </c:forEach>
                </select>
            </div>
        	
            <div class="form-group">
              <label for="id" class="searchtitle">参数名称
              </label>
                <select name="search_EQ_id" id="id" data-placeholder="请选择参数"
                  class="form-control searchtext">
                  <option value="">全部</option>
                  <c:forEach items="${parameters }" var="p">
                  	<option value="${p.id }">${p.name }</option> 
                  </c:forEach>
                </select>
            </div>
            <div class="form-group">
              <label for="key" class="searchtitle">变量名
              </label>
               	<input id="key" type="text" class="form-control searchtext" name="search_LIKE_keyfield" />
            </div>
              <button type="submit" id="searchBtn" class="btn btn-info btn-search1"
                >搜索</button>
        </form>
      </div>
       <div id="toolBar">
            <div class="btn-group pull-left">
            <a class="btn btn-default1" target="dialog" rel="QdasSave" refresh="true" href="${contextPath }/qdas/toAddPage/0"> 
                <i class="fa fa-plus"></i> 
                <span>新增</span>
              </a>
            <a class="btn btn-default1" target="dialog" rel="QdasUpdate" refresh="true" href="${contextPath }/qdas/toAddPage/{slt_uid}"> 
                <i class="fa fa-edit"></i> 
                <span>修改</span>
              </a>
             <%-- <a class="btn btn-default1" target="dialog" rel="QdasSearch" refresh="true" href="${contextPath }/qdas/exportQdasPage"> 
                <i class="fa fa-mail-forward"></i> 
                <span>导出</span>
              </a>  --%>
            </div>
       </div>
       <table class="table table-striped" id="table" data-field="qdas" data-url="${contextPath}/qdas/data">
            <thead>
              <tr>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="mesqdascategory.name" width="100">参数类型</th>
                <th data-field="name" width="100">参数名称</th>
<!--                 <th data-field="parameter" width="100">参数名称</th> -->
                <th data-field="keyfield" width="100">变量名</th>
              </tr>
            </thead>
          </table>
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
  <c:set var="ParentTitle" value="System" />
  <c:set var="ModuleTitle" value="Qdas" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
</body>
</html>
