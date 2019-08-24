<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>测点维护</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index"> 首页</a></li>
      <li>测点维护</li>
    </ol>
    
    
    <div class="main-wrap">
      <div class="main-body">
    <div class="searchBar">
      <div class="search_header">
      <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
        <i class="fa fa-search"></i> 测点查询条件
      </div>
      <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/mesPoints/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">测点名称</label>
                <input type="text" class="form-control searchtext" style="width:132px;"
                id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name}">
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">测点id</label>
                <input type="text" class="form-control searchtext" style="width:132px;"
                id="inputText" name="search_LIKE_codekey" value="${param.search_LIKE_codekey}">
              </div>
              
              
              <div class="form-group">
                <label for="inputText" class="searchtitle">选择测点类型</label>
                <select name="mesPointType" class="form-control validate[required] required searchtext">
            	<option value="">全部</option>
               <c:forEach var="mpt" items="${mesPointTypes}"> 
                   <option value="${mpt.id}">${mpt.name}</option>
               </c:forEach>
            </select>
              </div>
              
               <div class="form-group">
                <label for="inputText" class="searchtitle">选择网关</label>
                <select name="mesPointGateway" class="form-control validate[required] required searchtext">
            	<option value="">全部</option>
               <c:forEach var="p" items="${mesPointGateway}"> 
                   <option value="${p.id}">${p.name}</option>
               </c:forEach>
            </select>
              </div>
              
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
              <button class="btn btn_search_clear" id="btnSearchClear">清除</button>
            </form>
            </div>
        </div>
        <div id="toolBar">
          <div class="btn-group pull-left">
	          	<shiro:hasPermission name="point:save">
                <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" href="${contextPath}/mesPoints/create">
                  <i class="fa fa-plus"></i> 
                  <span>添加测点</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="point:edit">
                <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="update_pointGateway" href="${contextPath }/mesPoints/findById/{id}">
                  <i class="fa fa-pencil"></i> 
                  <span>修改测点</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="point:delete">
                <a class="btn btn-default1 btn-tool"  data-url="${contextPath }/mesPoints/deleteById" title="确认要删除?"> 
                  <i class="fa fa-remove"></i> 
                  <span>删除测点</span>
                </a>
                </shiro:hasPermission>
                <form action="#" id="relationFileForm" method="post" hidden="true" enctype="multipart/form-data">
                    <input type="file" id="importFile" name="alarmRelationFile">
                </form>
                <a class="btn btn-default1" href="javascript:void(0);" id="importRelation">
                    <i class="fa fa-upload"></i>
                    <span>导入</span>
                </a>

          </div>
        </div>
        <table class="table table-striped" id="table" data-field="points" data-url="${contextPath }/mesPoints/data">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox" group="ids">
              </th>
              <th data-field="codekey" width="100">测点Id</th>
              <th data-field="name" width="100">测点名称</th>
              <th data-field="mesPointGateway.name" width="100">所属网关</th>
              <th data-field="factoryName" width="100">所属工厂</th>
              <th data-field="currentMesProductline.linename" width="100">所属产线</th>
              <th data-field="currentMesDriver.name" width="100">所属设备</th>
              <th data-field="currentMesDrivertypeProperty.propertyname" width="100">绑定设备属性</th>
              <th data-field="currentMesDriver.id" width="100" data-visible="false"></th>
              <th data-field="currentMesProduct.name" width="100">产品</th>
              <th data-field="currentMesProduct.id" width="100" data-visible="false"></th>
              <th data-field="currentMesProductProcedure.procedurename" width="100">绑定工序</th>
              <th data-field="currentMesProductProcedure.id" width="100" data-visible="false"></th>
              <th data-field="currentMesProcedurePropertyName" width="100">绑定参数</th>
              <th data-field="currentMesProcedurePropertyId" width="100" data-visible="false"></th>
              <th data-field="mesPointType.id" width="100" data-visible="false"></th>
              <th data-field="mesPointType.name" width="100">测点类型</th>
              <th data-field="datatype" width="100">数据类型</th>
              <th data-field="units" width="100">单位</th>
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
        <div class="modal-body unitBox">
        </div>
      </div>
    </div>
  </div>
</script>
<c:set var="ParentTitle" value="points"/>
<c:set var="ModuleTitle" value="point"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery.blockUI.js"></script>
</body>
<script type="text/javascript">
    //导入报警信息模板
    $("#importRelation").bind("click",function(){
        $("#importFile").click();
    });
    $("#importFile").bind("change",function(){
        var pathArr = $("#importFile").val().split("\\");
        var fileName = pathArr[pathArr.length-1]; //文件名
        var fileSuffix = fileName.split(".")[1];  //文件后缀
        //验证文件后缀是否正确
        if(fileSuffix!="xlsx" || fileSuffix=="" || fileSuffix==null){
            swal("错误","请选择正确的文件","error");
            return;
        }
        var formData = new FormData($("#relationFileForm")[0]);
        $.ajax({
            url:"${contextPath}/mesPoints/readexcel",
            dataType:"text",
            type:"POST",
            data:formData,
            cache: false,
            contentType: false,
            processData: false,
            success:function(data){
                var data =  eval("("+data+")");
                if("success"== data.result){
                    uplocadSuccess();
                }else{
                    uplocadError();
                }
            }
        });
        $("#importFile").val("");
    });
    function uplocadSuccess(){
        $.table.refreshCurrent();
        swal("成功","导入成功！","success");
    }

    function uplocadError(){
        $.table.refreshCurrent();
        swal("警告","文件内部格式有误，部分内容导入失败！","warning");
    }

	$(document).ready(function(){
		$(".btn-tool").click(function(){
			 $.blockUI(
		      			{
		      				baseZ:99999,
		      				message:"<h1>正在查询该测点能否删除,请稍后......</h1>",
		      			}
		     );
			var $this = $(this);
			var ids = $.table.getSelectedId().join(',');
			function _doPost(){
				ajaxTodo($this.attr('data-url')+"?ids="+ids,function(data){
					dialogReloadCallback(data);
				});
			}
			$.ajax({
				url:'${contextPath}/mesPoints/checkPointIsdeletable',
				dataType:'JSON',
				type:'POST',
				data:{ids:ids},
				success:function(data){
					$.unblockUI();
					if(data.message == 'success'){
						  if ($this.attr('title')) {
					        swal({
					          title: $this.attr('title'),
					          type: "warning",
					          showCancelButton: true,
					          confirmButtonColor: '#DD6B55',
					          confirmButtonText: '确认',
					          cancelButtonText: "取消",
					          closeOnConfirm: false
					        },
					        function(isConfirm){
					          if (isConfirm) _doPost();
					        });
					      } else {
					        _doPost();
					      }
					}else{
						swal({type: "warning",title:"存在关联数据,无法删除"});
					}
				},
				error:function(err){
					$.unblockUI();
				}
			});
			
		});
	});





</script>
</html>