<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>测点类型管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index"> 首页</a></li>
      <li><i class="fa fa-puzzle-piece"></i> 测点类型管理</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
    <div class="searchBar">
          <form class="form-inline" method="post" action="${contextPath }/mesPointType/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">测点类型名称</label>&nbsp&nbsp&nbsp&nbsp
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name"value="${param.search_LIKE_name}">
              </div>
              <button type="submit" class="btn btn-info btn-search">搜索</button>
              <button type="reset" class="btn btn-info btn-search">重    置</button>
            </form>
        </div>
        <div id="toolBar">
          <div class="btn-group pull-left">
	         
                <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" href="${contextPath}/mesPointType/create">
                  <i class="fa fa-pencil"></i> 
                  <span>新增测点类型</span>
                </a>
             
                <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" href="${contextPath }/mesPointType/delById/{id}" title="确认要删除?"> 
                  <i class="fa fa-remove"></i> 
                  <span>删除测点类型</span>
                </a>
     
          </div>
        </div>

        <table class="table table-striped" id="table" data-field="meaPointType" data-url="${contextPath }/mesPointType/data">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22"><input
                class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
              <th data-field="name" width="100">测点类型</th>
              <th data-field="pointtypekey" width="100">key</th>
              <th data-field="companyinfo.companyname" width="100">所属公司</th>
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
<c:set var="ModuleTitle" value="MeaPointType"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>