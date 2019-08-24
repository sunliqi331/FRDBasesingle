<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>设备类型管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index">首页</a></li>
        <li>设备类型管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 设备类型查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath}/drivertype/driverTypeData" data-target="driverTypeTable" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle" style="width:96px">设备类型名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_typename" value="${param.search_LIKE_typename}" />
              </div>
              <button  id="searchDriverTypeBtn" type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
            	<shiro:hasPermission name="driverType:save">
              <a class="btn btn-default1" target="dialog" data-target="driverTypeTable" refresh="true" rel="create_DriverType" href="${contextPath}/drivertype/addDriverType?pagename=addDriverType"> 
                <i class="fa fa-plus"></i> 
                <span>添加设备类型</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverType:edit">
              <a class="btn btn-default1" target="dialog" data-target="driverTypeTable" refresh="true" rel="edit_DriverType" href="${contextPath}/drivertype/findById/{slt_uid}">
                <i class="fa fa-pencil"></i> 
                <span>修改设备类型</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverType:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="driverTypeTable" rel="ids" href="${contextPath }/drivertype/deleteDriverType" title="确认要删除?"> 
                <i class="fa fa-remove"></i> 
                <span>删除设备类型</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="driverType:viewProperty">
              <a class="btn btn-default1" target="page" data-target="driverTypeTable" rel="viewDriverTypeProp" href="${contextPath}/drivertype/driverAttributeList/{slt_uid}?pagename=driverAttributeList">
                <i class="fa fa-cogs"></i>
                <span>配置设备类型属性</span>
              </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="driverType:save">
              <a class="btn btn-default1" target="dialog" data-target="driverTypeTable" refresh="true" rel="create_DriverType" href="${contextPath}/drivertype/addMuitiDriverType"> 
                <i class="fa fa-plus"></i> 
                <span>批量添加设备类型</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped driver_type " id="driverTypeTable" data-field="mesDrivertypes" data-url="${contextPath}/drivertype/driverTypeData">
            <thead>
              <tr>
                <th data-field="Number" width="1%" data-align="center">序号	</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="typename" data-width="43%">设备类型名称</th>
                <th data-field="showPic" data-width="43%">设备图片(点击图片可查看大图)</th>
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
  <c:set var="ParentTitle" value="Driver" />
  <c:set var="ModuleTitle" value="driverType" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$("#searchDriverTypeBtn").click(function(){
			console.log("im clicked");
			console.log($(this).parent().html());
		});
		
		

		$.table.init('driverTypeTable',{toolbar:'#toolBar',onClickCell:function(field, value, row, $element){
        	  if(field == 'showPic'){
          	  var op = {};
        	    op.title = "设备图片大图信息";
        	   console.log($(row.showPic).attr("data-url"));
        	     op.url = $(row.showPic).attr("data-url");
        	    op.destroyOnClose=true;
        		$.pdialog.open("propertyDetail",op);
          	  }
            }
		},function(data){
		})

	});
</script>
</html>