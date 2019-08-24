<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>网关维护</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
      <li>网关维护</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
    <div class="searchBar">
      <div class="search_header">
      <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
        <i class="fa fa-search"></i> 网关查询条件
      </div>
      <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/mesPointGateway/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="" width="80px">网关名称</label>&nbsp
                <input id="searchName" type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name}">
              </div>
              <input id="property" type="hidden" disabled>
              <button id="search" type="submit" class="btn btn-info btn-search1">搜索</button>
            
            </form>
            </div>
        </div>
        <div id="toolBar">
          <div class="btn-group pull-left">

				<shiro:hasPermission name="gateway:save">
                <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_pointGateway" href="${contextPath}/mesPointGateway/create">
                  <i class="fa fa-plus"></i> 
                  <span>添加网关</span>
                </a>
                </shiro:hasPermission>
           		<shiro:hasPermission name="gateway:edit">
                <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="update_pointGateway" href="${contextPath }/mesPointGateway/findById/{id}">
                  <i class="fa fa-pencil"></i> 
                  <span>修改网关</span>
                </a>
                </shiro:hasPermission>
      			<shiro:hasPermission name="gateway:delete">
                <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" href="${contextPath }/mesPointGateway/delById" title="确认要删除?"> 
                  <i class="fa fa-remove"></i> 
                  <span>删除网关</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateway:edit">
                <a class="btn btn-default1 btn-tool" id="unbindGateway"> 
                  <i class="fa fa-pencil"></i> 
                  <span>解除绑定</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateway:edit">
                <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" refresh="true" rel="update_pointGateway" href="${contextPath }/mesPointGatewayExchange/exchangePage/{id}"> 
                  <i class="fa fa-pencil"></i> 
                  <span>网关替换</span>
                </a>
                </shiro:hasPermission>
<%--                 <shiro:hasPermission name="gateway:viewPoint"> --%>
<%--                 <a class="btn btn-default1 btn-tool" target="page" data-target="table" href="${contextPath }/mesPointGateway/mesPointList/{id}?pagename=mesPointList">  --%>
<!--                   <i class="fa fa-cogs"></i>  -->
<!--                   <span>配置网关测点</span> -->
<!--                 </a> -->
<%--                 </shiro:hasPermission> --%>
            <a id="unbindA" style="visibility:hidden" class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" href="${contextPath}/mesPointGateway/unbindGateway"></a>

          </div>
        </div>

        <table class="table table-striped" id="table" data-field="pointGateways" data-url="${contextPath }/mesPointGateway/data?verify=${verify}">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22"><input
                class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
              <th data-field="name" width="100">网关名称</th>
              <th data-field="mac" width="100">MAC地址</th>
              <th data-field="macCode" width="100">网关编码</th>
              <th data-field="virtualFlag" width="100">虚拟网关</th>
              <th data-field="createTime" width="100">创建时间</th>
              <th data-field="isActive" width="100">是否激活</th>
              <th data-field="companyinfo.companyname" width="100">所属企业</th>
              <th data-field="gateWayStatus" width="100">状态</th>
             <!--  <th data-field="companyinfo.companyname" width="100">所属公司</th> -->
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
<c:set var="ModuleTitle" value="gateway"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript">
$("#search").click(function (){
    $("#property").val("?numPerPage=10&pageNum=1&sortOrder=asc&search_LIKE_name="+$('#searchName').val()+"");
})
$(document).ready(function(){
   window.setInterval(function (){
       $.table.setCurrent("table");
      // $.table.refreshCurrent("${contextPath}/mesPointGateway/data"+$("#property").val()+"&verify=${verify}");
       $.table.refreshCurrent($("table").attr("data-url"),{search_LIKE_name:$('#searchName').val()});
   }, 10000);
  });
$("#unbindGateway").click(function (){
	$("#unbindA").attr("title","确认要解除绑定?");
    $("#unbindA").trigger("click");
})
</script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
</body>
</html>