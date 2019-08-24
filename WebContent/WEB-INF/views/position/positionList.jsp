<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>职位管理</title>
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
        <li>职位管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 职位查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath}/position/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">职位名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_positionname" value="${param.search_LIKE_positionname}" />
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left clearfix" >
            <shiro:hasPermission name="position:save">
              <a class="btn btn-default1" target="dialog" rel="addPosition" refresh="true" href="${contextPath}/position/addPosition" >
                <i class="fa fa-plus"></i> 
                <span>添加职位</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="position:edit">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="editPosition" href="${contextPath}/position/findById/{slt_uid}">
                <i class="fa fa-pencil"></i> 
                <span>修改职位</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="position:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" href="${contextPath }/position/deleteById" title="确认要删除?"> 
                <i class="fa fa-remove"></i> 
                <span>删除职位</span>
              </a>
              </shiro:hasPermission>
              <%-- <a class="btn btn-default1 btn-tool" target="page" data-target="table" rel=finddrivers href="${contextPath }/productline/findDrivers/{slt_uid}"> 
                <i class="fa fa-eye"></i> 
                <span>查看设备</span>
              </a> --%>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="position" data-url="${contextPath}/position/data">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="positionname" width="100">职位名称</th>
                <!-- <th data-field="linename" width="100">产线名称</th>
                <th data-field="mesdriver" width="100">关联设备</th> -->
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
<c:set var="ParentTitle" value="Company" />
<c:set var="ModuleTitle" value="position" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>